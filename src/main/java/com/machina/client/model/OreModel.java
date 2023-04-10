package com.machina.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.machina.block.ore.OreBlock;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.BlockFaceUV;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.BlockPart;
import net.minecraft.client.renderer.model.BlockPartFace;
import net.minecraft.client.renderer.model.FaceBakery;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.SimpleBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;

public class OreModel implements IDynamicBakedModel {

	BlockModel bm;

	static BlockPart partB;
	static BlockPart partF;

	static {
		float EP = 0.01f;
		Vector3f f1 = new Vector3f(0, 0, 0);
		Vector3f t1 = new Vector3f(16, 16, 16);
		Vector3f f2 = new Vector3f(0 - EP, 0 - EP, 0 - EP);
		Vector3f t2 = new Vector3f(16 + EP, 16 + EP, 16 + EP);

		Map<Direction, BlockPartFace> mapFacesIn = Maps.newEnumMap(Direction.class);

		for (Direction face : Direction.values()) {
			BlockFaceUV uvface = new BlockFaceUV(new float[] { 0, 0, 16, 16 }, 0);
			mapFacesIn.put(face, new BlockPartFace(null, -1, null, uvface));
		}

		partB = new BlockPart(f1, t1, mapFacesIn, null, true);
		partF = new BlockPart(f2, t2, mapFacesIn, null, true);

	}

	private final IModelTransform modelState;
	private final ItemOverrideList overrides;
	private final ItemCameraTransforms transforms;

	public OreModel(IModelTransform modelState, ItemOverrideList overrides, ItemCameraTransforms transforms) {
		this.modelState = modelState;
		this.overrides = overrides;
		this.transforms = transforms;
		this.bm = new BlockModel(null, new ArrayList<>(), new HashMap<>(), false, BlockModel.GuiLight.FRONT,
				ItemCameraTransforms.NO_TRANSFORMS, ItemOverrideList.EMPTY.getOverrides());
	}

	@Nullable
	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData extraData) {
		List<BakedQuad> quads = new ArrayList<>();
		if (state == null || !(state.getBlock() instanceof OreBlock)) {
			return quads;
		}

		OreBlock block = (OreBlock) state.getBlock();
		TextureAtlasSprite bg = getTexture(block.getBgTexturePath());
		TextureAtlasSprite fg = getTexture(block.getFgTexturePath());

		SimpleBakedModel.Builder builderB = new SimpleBakedModel.Builder(this.bm.customData, ItemOverrideList.EMPTY)
				.particle(bg);
		SimpleBakedModel.Builder builderF = new SimpleBakedModel.Builder(this.bm.customData, ItemOverrideList.EMPTY)
				.particle(fg);

		for (Map.Entry<Direction, BlockPartFace> e : partB.faces.entrySet()) {
			builderB.addUnculledFace(makeBakedQuad(partB, e.getValue(), bg, e.getKey(), modelState, null));
		}

		for (Map.Entry<Direction, BlockPartFace> e : partF.faces.entrySet()) {
			builderF.addUnculledFace(makeBakedQuad(partF, e.getValue(), fg, e.getKey(), modelState, null));
		}

		quads.addAll(builderB.build().getQuads(null, side, rand, extraData));
		quads.addAll(builderF.build().getQuads(null, side, rand, extraData));
		return quads;
	}

	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean usesBlockLight() {
		return false;
	}

	@Override
	public boolean isCustomRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return getTexture(new ResourceLocation("block/stone"));
	}

	@Override
	public ItemOverrideList getOverrides() {
		return overrides;
	}

	@Override
	public ItemCameraTransforms getTransforms() {
		return transforms;
	}

	public static TextureAtlasSprite getTexture(ResourceLocation resLoc) {
		return getTexture(resLoc, AtlasTexture.LOCATION_BLOCKS);
	}

	public static TextureAtlasSprite getTexture(ResourceLocation resLoc, ResourceLocation atlasResLoc) {
		return Minecraft.getInstance().getTextureAtlas(atlasResLoc).apply(resLoc);
	}

	public static BakedQuad makeBakedQuad(BlockPart blockPart, BlockPartFace partFace, TextureAtlasSprite atlasSprite,
			Direction dir, IModelTransform modelRotation, ResourceLocation modelResLoc) {
		return new FaceBakery().bakeQuad(blockPart.from, blockPart.to, partFace, atlasSprite, dir, modelRotation,
				blockPart.rotation, true, modelResLoc);
	}
}
