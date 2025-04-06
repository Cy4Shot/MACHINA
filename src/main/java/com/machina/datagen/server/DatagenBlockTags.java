package com.machina.datagen.server;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.machina.Machina;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.FamiliesInit;
import com.machina.registration.init.FamiliesInit.DirtFamily;
import com.machina.registration.init.FamiliesInit.OreFamily;
import com.machina.registration.init.FamiliesInit.StoneFamily;
import com.machina.registration.init.FamiliesInit.WoodFamily;
import com.machina.registration.init.TagInit.BlockTagInit;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class DatagenBlockTags extends BlockTagsProvider {
	public DatagenBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
			@Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, Machina.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(@NotNull Provider pProvider) {
		smallFlower(BlockInit.SPRUCE_CUP, BlockInit.POTTED_SPRUCE_CUP);
		smallFlower(BlockInit.PURPLE_GLOWSHROOM, BlockInit.POTTED_PURPLE_GLOWSHROOM);
		smallFlower(BlockInit.PINK_GLOWSHROOM, BlockInit.POTTED_PINK_GLOWSHROOM);
		smallFlower(BlockInit.RED_GLOWSHROOM, BlockInit.POTTED_RED_GLOWSHROOM);
		smallFlower(BlockInit.ORANGE_GLOWSHROOM, BlockInit.POTTED_ORANGE_GLOWSHROOM);
		smallFlower(BlockInit.YELLOW_GLOWSHROOM, BlockInit.POTTED_YELLOW_GLOWSHROOM);
		smallFlower(BlockInit.GREEN_GLOWSHROOM, BlockInit.POTTED_GREEN_GLOWSHROOM);
		smallFlower(BlockInit.TURQUOISE_GLOWSHROOM, BlockInit.POTTED_TURQUOISE_GLOWSHROOM);
		smallFlower(BlockInit.BLUE_GLOWSHROOM, BlockInit.POTTED_BLUE_GLOWSHROOM);
		smallFlower(BlockInit.DRAGON_PEONY, BlockInit.POTTED_DRAGON_PEONY);
		smallFlower(BlockInit.SPINDLESPROUT, BlockInit.POTTED_SPINDLESPROUT);
		smallFlower(BlockInit.SMALL_FERN, BlockInit.POTTED_SMALL_FERN);
		smallFlower(BlockInit.DEAD_SMALL_FERN, BlockInit.POTTED_DEAD_SMALL_FERN);
		smallFlower(BlockInit.NEEDLEGRASS, BlockInit.POTTED_NEEDLEGRASS);

		tallFlower(BlockInit.SPINDLEGRASS);
		tallFlower(BlockInit.NEEDLETHATCH);
		tallFlower(BlockInit.ORPHEUM);

		tag(BlockTags.FLOWERS).add(BlockInit.CLOVER.get(), BlockInit.PURPLE_GROUNDLILY.get(),
				BlockInit.PINK_GROUNDLILY.get(), BlockInit.RED_GROUNDLILY.get(), BlockInit.ORANGE_GROUNDLILY.get(),
				BlockInit.YELLOW_GROUNDLILY.get(), BlockInit.GREEN_GROUNDLILY.get(),
				BlockInit.TURQUOISE_GROUNDLILY.get(), BlockInit.BLUE_GROUNDLILY.get(), BlockInit.PURPLE_PETALS.get(),
				BlockInit.RED_PETALS.get(), BlockInit.ORANGE_PETALS.get(), BlockInit.YELLOW_PETALS.get(),
				BlockInit.GREEN_PETALS.get(), BlockInit.TURQUOISE_PETALS.get(), BlockInit.BLUE_PETALS.get());

		tag(BlockTagInit.PLANET_CARVABLE).add(Blocks.STONE, Blocks.GRAVEL, Blocks.WATER);

		FamiliesInit.ORES.forEach(this::oreFamily);
		FamiliesInit.DIRTS.forEach(this::dirtFamily);
		FamiliesInit.STONES.forEach(this::stoneFamily);
		FamiliesInit.WOODS.forEach(this::woodFamily);
	}

	private void smallFlower(RegistryObject<? extends Block> flower, RegistryObject<FlowerPotBlock> potted) {
		tag(BlockTags.FLOWERS).add(flower.get());
		tag(BlockTags.SMALL_FLOWERS).add(flower.get());
		tag(BlockTags.FLOWER_POTS).add(potted.get());
	}

	private void tallFlower(RegistryObject<TallFlowerBlock> flower) {
		tag(BlockTags.FLOWERS).add(flower.get());
		tag(BlockTags.TALL_FLOWERS).add(flower.get());
	}

	private void oreFamily(OreFamily family) {
		family.getOre().ifPresent(ore -> {
			tag(common("ores")).addTag(common("ores/" + family.name()));
			tag(common("ores/" + family.name())).add(ore);
		});
		family.getBlock().ifPresent(block -> {
			tag(common("storage_blocks")).addTag(common("storage_blocks/" + family.name()));
			tag(common("storage_blocks/" + family.name())).add(block);
		});
		family.getRawBlock().ifPresent(block -> {
			tag(common("storage_blocks")).addTag(common("storage_blocks/" + family.name()));
			tag(common("storage_blocks/raw_" + family.name())).add(block);
		});
	}

	private void dirtFamily(DirtFamily family) {
		tag(BlockTags.DIRT).add(family.dirt());
		tag(BlockTagInit.PLANET_CARVABLE).add(family.dirt(), family.slab(), family.stairs());
		tag(BlockTags.SLABS).add(family.slab());
		tag(BlockTags.STAIRS).add(family.stairs());
		tag(BlockTags.OVERWORLD_CARVER_REPLACEABLES).add(family.dirt(), family.slab(), family.stairs());

		if (family.grass().isPresent()) {
			tag(BlockTags.DIRT).add(family.grass().get());
			tag(BlockTagInit.PLANET_CARVABLE).add(family.grass().get());
			tag(BlockTags.OVERWORLD_CARVER_REPLACEABLES).add(family.grass().get());
		}
	}

	private void stoneFamily(StoneFamily family) {
		tag(BlockTags.SLABS).add(family.slab());
		tag(BlockTags.STAIRS).add(family.stairs());
		tag(BlockTags.WALLS).add(family.wall());

		tag(BlockTags.BUTTONS).add(family.button());
		tag(BlockTags.STONE_BUTTONS).add(family.button());
		tag(BlockTags.PRESSURE_PLATES).add(family.pressure_plate());
		tag(BlockTags.STONE_PRESSURE_PLATES).add(family.pressure_plate());

		tag(BlockTags.OVERWORLD_CARVER_REPLACEABLES).add(family.base());
	}

	private void woodFamily(WoodFamily family) {
		tag(BlockTags.SLABS).add(family.slab());
		tag(BlockTags.STAIRS).add(family.stairs());

		tag(BlockTags.BUTTONS).add(family.button());
		tag(BlockTags.WOODEN_BUTTONS).add(family.button());
		tag(BlockTags.DOORS).add(family.door());
		tag(BlockTags.WOODEN_DOORS).add(family.door());
		tag(BlockTags.TRAPDOORS).add(family.trapdoor());
		tag(BlockTags.WOODEN_TRAPDOORS).add(family.trapdoor());
		tag(BlockTags.FENCES).add(family.fence());
		tag(BlockTags.WOODEN_FENCES).add(family.fence());
		tag(BlockTags.FENCE_GATES).add(family.fencegate());
		tag(BlockTags.PRESSURE_PLATES).add(family.pressure_plate());
		tag(BlockTags.WOODEN_PRESSURE_PLATES).add(family.pressure_plate());

		tag(BlockTags.ALL_SIGNS).add(family.signblock(), family.wallsignblock());
		tag(BlockTags.SIGNS).add(family.signblock());
		tag(BlockTags.WALL_SIGNS).add(family.wallsignblock());
		tag(BlockTags.ALL_HANGING_SIGNS).add(family.hangingsignblock(), family.hangingwallsignblock());
		tag(BlockTags.CEILING_HANGING_SIGNS).add(family.hangingsignblock());
		tag(BlockTags.WALL_HANGING_SIGNS).add(family.hangingwallsignblock());
		tag(BlockTags.LOGS).add(family.log(), family.stripped_log(), family.wood(), family.stripped_wood());

		tag(BlockTags.PLANKS).add(family.planks());
		tag(BlockTags.LEAVES).add(family.leaves());
	}

	private static TagKey<Block> common(String name) {
		return TagKey.create(Registries.BLOCK, new ResourceLocation("c", name));
	}
}