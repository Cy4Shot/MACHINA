package com.machina.registration.init;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.registration.init.BlockInit.MachinaOre;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public class OverworldOresInit {

	public static final List<OverworldOre> ORES = new ArrayList<>();

	public static final void register() {
		register("aluminum_ore_upper", BlockInit.ALUMINUM_ORE, 14, triangle(80, 64, 192));
		register("aluminum_ore_middle", BlockInit.ALUMINUM_ORE, 14, triangle(20, -32, 96));
		register("aluminum_ore_bottom", BlockInit.ALUMINUM_ORE, 14, between(8, -64, 32));

		register("nickel_ore_shallow", BlockInit.NICKEL_ORE, 9, rareOrePlacement(24,
				HeightRangePlacement.triangle(VerticalAnchor.absolute(32), VerticalAnchor.absolute(128))));
		register("nickel_ore_deep", BlockInit.NICKEL_ORE, 9, triangle(18, -64, 16));
		register("nickel_ore_bottom", BlockInit.NICKEL_ORE, 9, between(10, -64, -8));

		register("lead_ore_middle", BlockInit.LEAD_ORE, 12, triangle(10, -16, 48));
		register("lead_ore_deep", BlockInit.LEAD_ORE, 12, triangle(20, -64, 16));
		register("lead_ore_bottom", BlockInit.LEAD_ORE, 12, between(12, -64, -16));

		register("boron_ore_rare_upper", BlockInit.BORON_ORE, 8, rareOrePlacement(28,
				HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(96))));
		register("boron_ore_deep_peak", BlockInit.BORON_ORE, 8, triangle(22, -64, 0));
		register("boron_ore_bottom_dense", BlockInit.BORON_ORE, 8, between(14, -64, -32));

		register("palladium_ore_deep_peak", BlockInit.PALLADIUM_ORE, 6, rareOrePlacement(40,
				HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(32))));
		register("palladium_ore_bottom", BlockInit.PALLADIUM_ORE, 6, rareOrePlacement(60,
				HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-16))));

		register("silver_ore_upper", BlockInit.SILVER_ORE, 10, triangle(18, 48, 160));
		register("silver_ore_middle", BlockInit.SILVER_ORE, 10, between(12, 0, 96));
		register("silver_ore_deep_trace", BlockInit.SILVER_ORE, 10, between(4, -32, 32));

		register("fluorite_ore_upper", BlockInit.FLUORITE_ORE, 16, triangle(10, 32, 160));
		register("fluorite_ore_middle", BlockInit.FLUORITE_ORE, 16, between(8, -16, 96));

		register("saltpeter_ore_mid_focus", BlockInit.SALTPETER_ORE, 14, triangle(13, 16, 64));
		register("saltpeter_ore_deep", BlockInit.SALTPETER_ORE, 14, between(8, -32, 32));

		register("pyrite_ore_upper", BlockInit.PYRITE_ORE, 20, triangle(10, 64, 192));
		register("pyrite_ore_middle", BlockInit.PYRITE_ORE, 20, between(8, 0, 96));
		register("pyrite_ore_deep_trace", BlockInit.PYRITE_ORE, 20, between(6, -32, 32));

		register("bismuth_ore_middle", BlockInit.BISMUTH_ORE, 11, triangle(7, -16, 64));
		register("bismuth_ore_deep", BlockInit.BISMUTH_ORE, 11, triangle(10, -64, 0));
		register("bismuth_ore_bottom", BlockInit.BISMUTH_ORE, 11, between(5, -64, -24));

		register("tin_ore_upper", BlockInit.TIN_ORE, 12, triangle(20, 64, 192));
		register("tin_ore_middle", BlockInit.TIN_ORE, 12, between(16, 0, 96));
		register("tin_ore_deep", BlockInit.TIN_ORE, 12, between(6, -32, 32));

		register("zinc_ore_upper", BlockInit.ZINC_ORE, 11, triangle(18, 80, 192));
		register("zinc_ore_middle", BlockInit.ZINC_ORE, 11, between(14, 16, 128));
		register("zinc_ore_deep_trace", BlockInit.ZINC_ORE, 11, between(4, -16, 48));

		register("low_grade_titanium_ore_deep_peak", BlockInit.LOW_GRADE_TITANIUM_ORE, 6, rareOrePlacement(36,
				HeightRangePlacement.triangle(VerticalAnchor.absolute(-48), VerticalAnchor.absolute(16))));
		register("low_grade_titanium_ore_bottom", BlockInit.LOW_GRADE_TITANIUM_ORE, 6, rareOrePlacement(48,
				HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-24))));
	}

	public static void register(String name, MachinaOre ore, int veinSize, List<PlacementModifier> modifiers) {
		ORES.add(new OverworldOre(ResourceLocation.fromNamespaceAndPath(Machina.MOD_ID, name), ore.stoneOre(), veinSize,
				modifiers));
	}

	public static List<PlacementModifier> orePlacement(PlacementModifier pCountPlacement,
			PlacementModifier pHeightRange) {
		return List.of(pCountPlacement, InSquarePlacement.spread(), pHeightRange, BiomeFilter.biome());
	}

	public static List<PlacementModifier> commonOrePlacement(int pCount, PlacementModifier pHeightRange) {
		return orePlacement(CountPlacement.of(pCount), pHeightRange);
	}

	public static List<PlacementModifier> rareOrePlacement(int pChance, PlacementModifier pHeightRange) {
		return orePlacement(RarityFilter.onAverageOnceEvery(pChance), pHeightRange);
	}

	public static List<PlacementModifier> between(int count, int min, int max) {
		return commonOrePlacement(count,
				HeightRangePlacement.uniform(VerticalAnchor.absolute(min), VerticalAnchor.absolute(max)));
	}

	public static List<PlacementModifier> triangle(int count, int min, int max) {
		return commonOrePlacement(count,
				HeightRangePlacement.triangle(VerticalAnchor.absolute(min), VerticalAnchor.absolute(max)));
	}

	public record OverworldOre(ResourceLocation loc, Supplier<? extends Block> block, int veinSize,
			List<PlacementModifier> modifiers) {
	}
}
