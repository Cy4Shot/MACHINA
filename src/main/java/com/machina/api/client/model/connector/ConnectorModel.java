package com.machina.api.client.model.connector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import com.machina.api.cap.sided.ConnectionSide;
import com.machina.api.client.model.mesh.FacedMesh;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.math.VecUtil;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElementRotation;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.IModelBuilder;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public class ConnectorModel implements IDynamicBakedModel {

	private final boolean isAmbientOcclusion;
	private final boolean isGui3d;
	private final boolean useBlockLight;
	private final Function<Material, TextureAtlasSprite> spriteGetter;

	public ConnectorModel(boolean isAmbientOcclusion, boolean isGui3d, boolean useBlockLight,
			Function<Material, TextureAtlasSprite> spriteGetter) {
		this.isAmbientOcclusion = isAmbientOcclusion;
		this.isGui3d = isGui3d;
		this.useBlockLight = useBlockLight;
		this.spriteGetter = spriteGetter;
	}

	@Override
	public boolean useAmbientOcclusion() {
		return this.isAmbientOcclusion;
	}

	@Override
	public boolean isGui3d() {
		return this.isGui3d;
	}

	@Override
	public boolean usesBlockLight() {
		return this.useBlockLight;
	}

	@Override
	public boolean isCustomRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return spriteGetter
				.apply(new Material(TextureAtlas.LOCATION_BLOCKS, new MachinaRL("textures/blocks/energy/middle.png")));
	}

	@Override
	public ItemOverrides getOverrides() {
		return ItemOverrides.EMPTY;
	}

	private static final String connText(String type, String side) {
		return "block/connector/" + type + "/" + side;
	}

	private static final Function<String, FacedMesh> MIDDLE = t -> FacedMesh.cube(new Vector3f(6f, 6f, 6f),
			new Vector3f(10f, 10f, 10f), connText(t, "middle"));
	private static final Function<String, FacedMesh> MULTI = t -> FacedMesh.pipe(new Vector3f(6.5f, 6.5f, 0f),
			new Vector3f(9.5f, 9.5f, 8f), connText(t, "multi"));

	@Override
	public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
			@NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
		List<BakedQuad> quads = new ArrayList<>();
		IModelBuilder<?> builder = IModelBuilder.collecting(quads);

//		String type = ConnectorModelData.type(extraData);

		if (ConnectorModelData.middle(extraData))
			MIDDLE.apply("energy").addQuads(builder, spriteGetter,
					new BlockElementRotation(VecUtil.XP, Direction.Axis.X, 0, false));

		for (Direction d : Direction.values()) {
			if (ConnectorModelData.getSide(extraData, d) == ConnectionSide.NONE)
				continue;
			System.out.println(d);
			MULTI.apply("energy").addQuads(builder, spriteGetter, VecUtil.dirToBer(d, new Vector3f(0.5f, 0.5f, 0.5f)));
		}

		return quads;
	}

	public static final class ConnectorModelData {
		private ConnectorModelData() {
		}

		/** @formatter:off
		 * Bits [0-1]: Down
		 * Bits [2-3]: Up
		 * Bits [4-5]: North
		 * Bits [6-7]: South
		 * Bits [8-9]: West
		 * Bits [10-11]: East
		 * Bit 12: Middle
		 * Bit 14-16 Type
		 * @formatter:on
		 */
		public static final ModelProperty<Long> PROPERTY = new ModelProperty<>();

		private static int GENERIC_MASK = 0b11;
		private static int MIDDLE_MASK = 0b100000000000;

		private static ConnectionSide getSide(Long data, int index) {
			return ConnectionSide.values()[(int) (data >> (index * 2) & GENERIC_MASK)];
		}

		public static ConnectionSide getSide(ModelData data, Direction d) {
			Long l = data.get(PROPERTY);
			if (l == null)
				return ConnectionSide.NONE;
			return getSide(l, d.get3DDataValue());
		}

		private static boolean middle(Long data) {
			return (data >> 12 & 1) == 1;
		}

		public static boolean middle(ModelData data) {
			Long l = data.get(PROPERTY);
			if (l == null)
				return false;
			return middle(data.get(PROPERTY));
		}

		public static String type(ModelData data) {
			Long l = data.get(PROPERTY);
			if (l == null)
				return "none";
			return "type_" + (l >> 12 & 0b111);
		}
	}

}
