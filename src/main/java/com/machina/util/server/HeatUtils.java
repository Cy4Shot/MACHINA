package com.machina.util.server;

import java.util.HashMap;
import java.util.Map;

import com.machina.block.tile.base.IHeatTileEntity;
import com.machina.config.CommonConfig;
import com.machina.registration.init.AttributeInit;
import com.machina.world.data.StarchartData;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HeatUtils {

	@SuppressWarnings("serial")
	public static final Map<Block, Float> heatMap = new HashMap<Block, Float>() {
		{
			// Hot Blocks
			put(Blocks.LAVA, 103f);
			put(Blocks.TORCH, 12f);
			put(Blocks.CAMPFIRE, 25f);
			put(Blocks.SOUL_CAMPFIRE, 57f);
			put(Blocks.FIRE, 42f);
			put(Blocks.SOUL_FIRE, 89f);
			put(Blocks.GLOWSTONE, 14f);

			// Cold Blocks
			put(Blocks.WATER, -3f);
			put(Blocks.FROSTED_ICE, -4f);
			put(Blocks.PACKED_ICE, -13f);
			put(Blocks.BLUE_ICE, -26f);
		}
	};

	public static float calculateTemperatureRegulators(BlockPos pos, World world) {
		float max = 0;
		for (Direction d : Direction.values()) {
			TileEntity te = world.getBlockEntity(pos.relative(d));
			if (te != null && te instanceof IHeatTileEntity) {
				IHeatTileEntity hte = (IHeatTileEntity) te;
				if (hte.isGenerator() && Math.abs(hte.getHeat()) > max)
					max = hte.getHeat();
			}
		}
		return max;
	}

	public static float getHeatOffset(BlockPos pos, World world) {
		float builder = 0f;
		for (Direction d : Direction.values()) {
			builder += heatMap.getOrDefault(world.getBlockState(pos.relative(d)).getBlock(), 0f);
		}
		return builder;
	}

	public static int getRange() {
		return CommonConfig.maxHeat.get();
	}

	public static float propFull(float heat, RegistryKey<World> dim) {
		return normalizeHeat(heat, dim) / (float) getRange();
	}

	public static float getHeat(RegistryKey<World> dim) {
		if (PlanetUtils.isDimensionPlanet(dim)) {
			return StarchartData.getDataForDimension(ServerHelper.server(), dim)
					.getAttribute(AttributeInit.TEMPERATURE);
		} else {
			return AttributeInit.TEMPERATURE.ser.def();
		}
	}

	public static float normalizeHeat(float heat, RegistryKey<World> dim) {
		return getHeat(dim) + heat;
	}

	public static float limitHeat(float heat, RegistryKey<World> dim) {
		if (normalizeHeat(heat, dim) > 0) {
			if (normalizeHeat(heat, dim) < getRange())
				return heat;
			return max(dim);
		}
		return min(dim);

	}

	public static float min(RegistryKey<World> dim) {
		return -getHeat(dim);
	}

	public static float max(RegistryKey<World> dim) {
		return getRange() - getHeat(dim);
	}
}
