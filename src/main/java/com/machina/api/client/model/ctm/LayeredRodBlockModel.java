package com.machina.api.client.model.ctm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.gson.JsonObject;
import com.machina.api.util.MachinaRL;

import earth.terrarium.athena.api.client.models.AthenaBlockModel;
import earth.terrarium.athena.api.client.models.AthenaModelFactory;
import earth.terrarium.athena.api.client.models.AthenaQuad;
import earth.terrarium.athena.api.client.utils.AppearanceAndTintGetter;
import earth.terrarium.athena.api.client.utils.CtmState.ConnectionCheck;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class LayeredRodBlockModel implements AthenaBlockModel {

	public static final ResourceLocation RL = MachinaRL.create("layered_rod");
	public static final AthenaModelFactory FACTORY = new Factory();

	private static final float IL = 6f / 16f;
	private static final float IR = 10f / 16f;
	private static final float ID = 6f / 16f;

	//@formatter:off
	private static final Function<Boolean, List<AthenaQuad>> CENTER = lit -> List.of(
			new AthenaQuad(2, 0f, 1f, 1f, 0f, Rotation.NONE, 0f, false),
			new AthenaQuad(2, 1f, 0f, 1f, 0f, Rotation.NONE, 0f, false),
			new AthenaQuad(lit ? 11 : 7, IL, IR, 1f, 0f, Rotation.NONE, ID, false));
	private static final Function<Boolean, List<AthenaQuad>> TOP = lit -> List.of(
			new AthenaQuad(1, 0f, 1f, 1f, 0f, Rotation.NONE, 0f, false),
			new AthenaQuad(1, 1f, 0f, 1f, 0f, Rotation.NONE, 0f, false),
			new AthenaQuad(lit ? 10 : 6, IL, IR, 1f, 0f, Rotation.NONE, ID, false));
	private static final Function<Boolean, List<AthenaQuad>> BOTTOM = lit -> List.of(
			new AthenaQuad(3, 0f, 1f, 1f, 0f, Rotation.NONE, 0f, false),
			new AthenaQuad(3, 1f, 0f, 1f, 0f, Rotation.NONE, 0f, false),
			new AthenaQuad(lit ? 12 : 8, IL, IR, 1f, 0f, Rotation.NONE, ID, false));
	private static final Function<Boolean, List<AthenaQuad>> SELF = lit -> List.of(
			new AthenaQuad(4, 0f, 1f, 1f, 0f, Rotation.NONE, 0f, false),
			new AthenaQuad(4, 1f, 0f, 1f, 0f, Rotation.NONE, 0f, false),
			new AthenaQuad(lit ? 13 : 9, IL, IR, 1f, 0f, Rotation.NONE, ID, false));
	private static final List<AthenaQuad> CAP = List.of(
			new AthenaQuad(0, 0f, 1f, 1f, 0f, Rotation.NONE, 0f, false),
			new AthenaQuad(0, 1f, 0f, 1f, 0f, Rotation.NONE, 0f, false));
	//@formatter:on

	private final Int2ObjectMap<Material> materials;
	private final BiPredicate<BlockState, BlockState> connectTo;

	public LayeredRodBlockModel(Int2ObjectMap<Material> materials, BiPredicate<BlockState, BlockState> connectTo) {
		this.materials = materials;
		this.connectTo = connectTo;
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

		BlockPos posAbove = pos.above();
		BlockPos posBelow = pos.below();
		BlockState appearanceAbove = level.getAppearance(state, pos, direction, level.getBlockState(posAbove),
				posAbove);
		BlockState appearanceBelow = level.getAppearance(state, pos, direction, level.getBlockState(posBelow),
				posBelow);
		final boolean min = !appearanceAbove.isAir() && ConnectionCheck
				.test(CtmUtils.check(level, state, pos, direction, connectTo), level, state, pos, posAbove, direction);
		final boolean max = !appearanceBelow.isAir() && ConnectionCheck
				.test(CtmUtils.check(level, state, pos, direction, connectTo), level, state, pos, posBelow, direction);

		if (direction.equals(Direction.UP) && min) {
			return List.of();
		} else if (direction.equals(Direction.DOWN) && max) {
			return List.of();
		} else if (direction.getAxis().isVertical()) {
			return CAP;
		}

		boolean lit = state.getValue(BlockStateProperties.LIT);
		if (min && max) {
			return CENTER.apply(lit);
		} else if (min) {
			return BOTTOM.apply(lit);
		} else if (max) {
			return TOP.apply(lit);
		}
		return SELF.apply(lit);
	}

	@Override
	public Map<Direction, List<AthenaQuad>> getDefaultQuads(Direction direction) {
		Map<Direction, List<AthenaQuad>> quads = new HashMap<>(Direction.values().length);
		for (Direction dir : Direction.values()) {
			quads.put(dir, SELF.apply(false));
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
			BiPredicate<BlockState, BlockState> conditions = CtmUtils.parseCondition(json);
			return () -> new LayeredRodBlockModel(materials, conditions);
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

			materials.put(10, CtmUtils.blockMat(GsonHelper.getAsString(json, "lit_inner_top")));
			materials.put(11, CtmUtils.blockMat(GsonHelper.getAsString(json, "lit_inner_center")));
			materials.put(12, CtmUtils.blockMat(GsonHelper.getAsString(json, "lit_inner_bottom")));
			materials.put(13, CtmUtils.blockMat(GsonHelper.getAsString(json, "lit_inner_self")));

			return materials;
		}
	}

}