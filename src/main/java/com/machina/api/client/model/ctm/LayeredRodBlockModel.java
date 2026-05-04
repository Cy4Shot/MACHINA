package com.machina.api.client.model.ctm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.gson.JsonObject;
import com.machina.api.util.MachinaRL;

import earth.terrarium.athena.api.client.models.AthenaBlockModel;
import earth.terrarium.athena.api.client.models.AthenaModelFactory;
import earth.terrarium.athena.api.client.models.AthenaQuad;
import earth.terrarium.athena.api.client.utils.AppearanceAndTintGetter;
import earth.terrarium.athena.api.client.utils.CtmUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

public class LayeredRodBlockModel implements AthenaBlockModel {

	public static final ResourceLocation RL = MachinaRL.create("layered_rod");
	public static final AthenaModelFactory FACTORY = new Factory();

	private static final float IL = 6f / 16f;
	private static final float IR = 10f / 16f;
	private static final float ID = 6f / 16f;

	//@formatter:off
	private static final List<AthenaQuad> CENTER = List.of(
			new AthenaQuad(2, 0f, 1f, 1f, 0f, Rotation.NONE, 0f, false),
			new AthenaQuad(2, 1f, 0f, 1f, 0f, Rotation.NONE, 0f, false),
			new AthenaQuad(7, IL, IR, 1f, 0f, Rotation.NONE, ID, false));
	private static final List<AthenaQuad> TOP = List.of(
			new AthenaQuad(1, 0f, 1f, 1f, 0f, Rotation.NONE, 0f, false),
			new AthenaQuad(1, 1f, 0f, 1f, 0f, Rotation.NONE, 0f, false),
			new AthenaQuad(6, IL, IR, 1f, 0f, Rotation.NONE, ID, false));
	private static final List<AthenaQuad> BOTTOM = List.of(
			new AthenaQuad(3, 0f, 1f, 1f, 0f, Rotation.NONE, 0f, false),
			new AthenaQuad(3, 1f, 0f, 1f, 0f, Rotation.NONE, 0f, false),
			new AthenaQuad(8, IL, IR, 1f, 0f, Rotation.NONE, ID, false));
	private static final List<AthenaQuad> SELF = List.of(
			new AthenaQuad(4, 0f, 1f, 1f, 0f, Rotation.NONE, 0f, false),
			new AthenaQuad(4, 1f, 0f, 1f, 0f, Rotation.NONE, 0f, false),
			new AthenaQuad(9, IL, IR, 1f, 0f, Rotation.NONE, ID, false));
	private static final List<AthenaQuad> CAP = List.of(
			new AthenaQuad(0, 0f, 1f, 1f, 0f, Rotation.NONE, 0f, false),
			new AthenaQuad(0, 1f, 0f, 1f, 0f, Rotation.NONE, 0f, false));
	//@formatter:on

	private final Int2ObjectMap<Material> materials;

	public LayeredRodBlockModel(Int2ObjectMap<Material> materials) {
		this.materials = materials;
	}

	@Override
	public List<AthenaQuad> getQuads(AppearanceAndTintGetter level, BlockState state, BlockPos pos,
			Direction direction) {
		BlockPos occludingPos = pos.relative(direction);
		BlockState appearance = level.getAppearance(state, pos, direction, level.getBlockState(occludingPos),
				occludingPos);
		BlockState occludingAppearance = level.getAppearance(occludingPos, direction.getOpposite(), state, pos);
		if (!appearance.isAir() && occludingAppearance.is(appearance.getBlock())) {
			return List.of();
		}

		if (direction.getAxis().isVertical()) {
			return CAP;
		}

		BlockPos posAbove = pos.above();
		BlockPos posBelow = pos.below();
		BlockState appearanceAbove = level.getAppearance(state, pos, direction, level.getBlockState(posAbove),
				posAbove);
		BlockState appearanceBelow = level.getAppearance(state, pos, direction, level.getBlockState(posBelow),
				posBelow);
		final boolean min = !appearanceAbove.isAir()
				&& level.getAppearance(posAbove, direction, state, pos).is(appearanceAbove.getBlock());
		final boolean max = !appearanceBelow.isAir()
				&& level.getAppearance(posBelow, direction, state, pos).is(appearanceBelow.getBlock());

		if (min && max) {
			return CENTER;
		} else if (min) {
			return BOTTOM;
		} else if (max) {
			return TOP;
		}
		return SELF;
	}

	@Override
	public Map<Direction, List<AthenaQuad>> getDefaultQuads(Direction direction) {
		Map<Direction, List<AthenaQuad>> quads = new HashMap<>(Direction.values().length);
		for (Direction dir : Direction.values()) {
			quads.put(dir, SELF);
		}
		return quads;
	}

	@Override
	public Int2ObjectMap<TextureAtlasSprite> getTextures(Function<Material, TextureAtlasSprite> getter) {
		Int2ObjectMap<TextureAtlasSprite> textures = new Int2ObjectArrayMap<>();
		for (var entry : materials.int2ObjectEntrySet()) {
			textures.put(entry.getIntKey(), getter.apply(entry.getValue()));
		}
		return textures;
	}

	private static class Factory implements AthenaModelFactory {

		@Override
		public Supplier<AthenaBlockModel> create(JsonObject json) {
			final var materials = parseMaterials(GsonHelper.getAsJsonObject(json, "ctm_textures"));
			return () -> new LayeredRodBlockModel(materials);
		}

		private static Int2ObjectMap<Material> parseMaterials(JsonObject json) {
			Int2ObjectMap<Material> materials = new Int2ObjectArrayMap<>();

			materials.put(0, CtmUtils.blockMat(GsonHelper.getAsString(json, "particle")));
			materials.put(1, CtmUtils.blockMat(GsonHelper.getAsString(json, "top")));
			materials.put(2, CtmUtils.blockMat(GsonHelper.getAsString(json, "center")));
			materials.put(3, CtmUtils.blockMat(GsonHelper.getAsString(json, "bottom")));
			materials.put(4, CtmUtils.blockMat(GsonHelper.getAsString(json, "self")));

			materials.put(6, CtmUtils.blockMat(GsonHelper.getAsString(json, "inner_top")));
			materials.put(7, CtmUtils.blockMat(GsonHelper.getAsString(json, "inner_center")));
			materials.put(8, CtmUtils.blockMat(GsonHelper.getAsString(json, "inner_bottom")));
			materials.put(9, CtmUtils.blockMat(GsonHelper.getAsString(json, "inner_self")));

			return materials;
		}
	}

}