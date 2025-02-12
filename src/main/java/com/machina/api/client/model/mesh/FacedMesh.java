package com.machina.api.client.model.mesh;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.joml.Vector3f;

import com.machina.api.util.MachinaRL;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElementRotation;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.IModelBuilder;

@OnlyIn(Dist.CLIENT)
public class FacedMesh implements IMesh {

	public final Vector3f from;
	public final Vector3f to;
	public final Map<Direction, MeshFace> faces;
	public final boolean shade;

	public static FacedMesh cube(Vector3f from, Vector3f to, String tex) {
		Material m = new Material(TextureAtlas.LOCATION_BLOCKS, new MachinaRL(tex));
		Map<Direction, MeshFace> faces = Arrays.stream(Direction.values())
				.collect(Collectors.toMap(x -> x, f -> new MeshFace(f, m, new BlockFaceUV(null, 0))));
		return new FacedMesh(from, to, faces, true);
	}

	public static FacedMesh pipe(Vector3f from, Vector3f to, String tex, String tex_2) {
		Material m = new Material(TextureAtlas.LOCATION_BLOCKS, new MachinaRL(tex));
		Material m1 = new Material(TextureAtlas.LOCATION_BLOCKS, new MachinaRL(tex_2));
		Map<Direction, MeshFace> faces = Map.of(
		//@formatter:off
			Direction.UP, new MeshFace(Direction.UP, m, new BlockFaceUV(null, 0)),
			Direction.DOWN, new MeshFace(Direction.DOWN, m, new BlockFaceUV(null, 180)),
			Direction.EAST, new MeshFace(Direction.EAST, m1, new BlockFaceUV(null, 0)),
			Direction.WEST, new MeshFace(Direction.WEST, m1, new BlockFaceUV(null, 180))
		//@formatter:on
		);
		return new FacedMesh(from, to, faces, true);
	}

	private FacedMesh(Vector3f f, Vector3f t, Map<Direction, MeshFace> faces, boolean shade) {
		this.from = f;
		this.to = t;
		this.faces = faces;
		this.shade = shade;
		this.fillUvs();
		this.faces.values().forEach(face -> face.parent = this);
	}

	private void fillUvs() {
		for (Map.Entry<Direction, MeshFace> entry : this.faces.entrySet()) {
			float[] afloat = this.uvsByFace(entry.getKey());
			(entry.getValue()).uv.setMissingUv(afloat);
		}

	}

	public float[] uvsByFace(Direction d) {
		float width, height;
		switch (d) {
		case DOWN:
		case UP:
			width = Mth.ceil(this.to.x() - this.from.x());
			height = Mth.ceil(this.to.z() - this.from.z());
			break;
		case SOUTH:
		case NORTH:
			width = Mth.ceil(this.to.x() - this.from.x());
			height = Mth.ceil(this.to.y() - this.from.y());
			break;
		case WEST:
		case EAST:
			width = Mth.ceil(this.to.z() - this.from.z());
			height = Mth.ceil(this.to.y() - this.from.y());
			break;
		default:
			return new float[] { 0, 0, 0, 0 };
		}
		return new float[] { 0, 0, width, height };
	}

	@Override
	public void addQuads(IModelBuilder<?> modelBuilder, Function<Material, TextureAtlasSprite> spriteGetter,
			BlockElementRotation rotation) {
		for (Direction direction : faces.keySet()) {
			MeshFace face = faces.get(direction);
			TextureAtlasSprite sprite = spriteGetter.apply(face.texture);
			BakedQuad quad = bakeQuad(from, to, face, sprite, direction, rotation, shade);

			if (face.cullForDirection == null)
				modelBuilder.addUnculledFace(quad);
			else
				modelBuilder.addCulledFace(face.cullForDirection, quad);
		}
	}

}
