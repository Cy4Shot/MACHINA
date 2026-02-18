package com.machina.datagen.server;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.machina.Machina;
import com.machina.item.CapacitorItem;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.FamiliesInit;
import com.machina.registration.init.FamiliesInit.DirtFamily;
import com.machina.registration.init.FamiliesInit.OreFamily;
import com.machina.registration.init.FamiliesInit.StoneFamily;
import com.machina.registration.init.FamiliesInit.WoodFamily;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.TagInit.ItemTagInit;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

public class DatagenItemTags extends ItemTagsProvider {
	public DatagenItemTags(PackOutput po, CompletableFuture<HolderLookup.Provider> provider,
			CompletableFuture<TagLookup<Block>> lookup, @Nullable ExistingFileHelper helper) {
		super(po, provider, lookup, Machina.MOD_ID, helper);
	}

	@Override
	protected void addTags(@NotNull Provider pProvider) {
		capacitor(ItemInit.BASIC_CAPACITOR);
		capacitor(ItemInit.ADVANCED_CAPACITOR);
		capacitor(ItemInit.SUPREME_CAPACITOR);

		flower(BlockInit.CLOVER);
		flower(BlockInit.PURPLE_GROUNDLILY);
		flower(BlockInit.PINK_GROUNDLILY);
		flower(BlockInit.RED_GROUNDLILY);
		flower(BlockInit.ORANGE_GROUNDLILY);
		flower(BlockInit.YELLOW_GROUNDLILY);
		flower(BlockInit.GREEN_GROUNDLILY);
		flower(BlockInit.TURQUOISE_GROUNDLILY);
		flower(BlockInit.BLUE_GROUNDLILY);
		flower(BlockInit.PURPLE_PETALS);
		flower(BlockInit.RED_PETALS);
		flower(BlockInit.ORANGE_PETALS);
		flower(BlockInit.YELLOW_PETALS);
		flower(BlockInit.GREEN_PETALS);
		flower(BlockInit.TURQUOISE_PETALS);
		flower(BlockInit.BLUE_PETALS);
		smallFlower(BlockInit.SPRUCE_CUP);
		smallFlower(BlockInit.PURPLE_GLOWSHROOM);
		smallFlower(BlockInit.PINK_GLOWSHROOM);
		smallFlower(BlockInit.RED_GLOWSHROOM);
		smallFlower(BlockInit.ORANGE_GLOWSHROOM);
		smallFlower(BlockInit.YELLOW_GLOWSHROOM);
		smallFlower(BlockInit.GREEN_GLOWSHROOM);
		smallFlower(BlockInit.TURQUOISE_GLOWSHROOM);
		smallFlower(BlockInit.BLUE_GLOWSHROOM);
		smallFlower(BlockInit.DRAGON_PEONY);
		smallFlower(BlockInit.SPINDLESPROUT);
		smallFlower(BlockInit.SMALL_FERN);
		smallFlower(BlockInit.DEAD_SMALL_FERN);
		smallFlower(BlockInit.NEEDLEGRASS);

		tallFlower(BlockInit.SPINDLEGRASS);
		tallFlower(BlockInit.NEEDLETHATCH);
		tallFlower(BlockInit.ORPHEUM);

		sand(BlockInit.TROPICAL_SAND);

		FamiliesInit.ORES.forEach(this::oreFamily);
		FamiliesInit.DIRTS.forEach(this::dirtFamily);
		FamiliesInit.STONES.forEach(this::stoneFamily);
		FamiliesInit.WOODS.forEach(this::woodFamily);
		FluidInit.OBJS.forEach(this::fluidObj);
	}

	private void smallFlower(DeferredBlock<? extends BushBlock> flower) {
		flower(flower);
		tag(ItemTags.SMALL_FLOWERS).add(flower.get().asItem());
	}

	private void tallFlower(DeferredBlock<TallFlowerBlock> flower) {
		flower(flower);
		tag(ItemTags.TALL_FLOWERS).add(flower.get().asItem());
	}

	private void flower(DeferredBlock<? extends BushBlock> flower) {
		tag(ItemTags.FLOWERS).add(flower.get().asItem());
	}

	private void sand(DeferredBlock<? extends Block> sand) {
		tag(ItemTags.SAND).add(sand.get().asItem());
	}

	private void capacitor(DeferredItem<? extends CapacitorItem> capacitor) {
		tag(ItemTagInit.CAPACITOR).add(capacitor.get());
	}

	private void fluidObj(FluidObject obj) {
		tag(common("buckets/" + obj.name())).add(obj.bucket());
	}

	private void oreFamily(OreFamily family) {
		family.ore().ifPresent(ore -> {
			tag(common("ores")).addTag(common("ores/" + family.name()));
			tag(common("ores/" + family.name())).add(ore.asItem());
		});
		family.block().ifPresent(block -> {
			tag(common("storage_blocks")).addTag(common("storage_blocks/" + family.name()));
			tag(common("storage_blocks/" + family.name())).add(block.asItem());
		});
		family.rawBlock().ifPresent(block -> {
			tag(common("storage_blocks")).addTag(common("storage_blocks/" + family.name()));
			tag(common("storage_blocks/raw_" + family.name())).add(block.asItem());
		});

		family.nugget().ifPresent(item -> {
			tag(common("nuggets")).addTag(common("nuggets/" + family.name()));
			tag(common("nuggets/" + family.name())).add(item);
		});
		family.ingot().ifPresent(item -> {
			tag(common("ingots")).addTag(common("ingots/" + family.name()));
			tag(common("ingots/" + family.name())).add(item);
		});
		family.dust().ifPresent(item -> {
			tag(common("dusts")).addTag(common("dusts/" + family.name()));
			tag(common("dusts/" + family.name())).add(item);
		});
		family.plate().ifPresent(item -> {
			tag(common("plates")).addTag(common("plates/" + family.name()));
			tag(common("plates/" + family.name())).add(item);
		});
		family.rod().ifPresent(item -> {
			tag(common("rods")).addTag(common("rods/" + family.name()));
			tag(common("rods/" + family.name())).add(item);
		});
		family.wire().ifPresent(item -> {
			tag(common("wires")).addTag(common("wires/" + family.name()));
			tag(common("wires/" + family.name())).add(item);
		});
		family.raw().ifPresent(raw -> {
			tag(common("raw_materials")).addTag(common("raw_materials/" + family.name()));
			tag(common("raw_materials/" + family.name())).add(raw);
		});
	}

	private void dirtFamily(DirtFamily family) {
		tag(ItemTags.DIRT).add(family.dirt().asItem());
		tag(ItemTags.SLABS).add(family.slab().asItem());
		tag(ItemTags.STAIRS).add(family.stairs().asItem());

		if (family.grass().isPresent()) {
			tag(ItemTags.DIRT).add(family.grass().get().asItem());
		}
	}

	private void stoneFamily(StoneFamily family) {
		tag(ItemTags.SLABS).add(family.slab().asItem());
		tag(ItemTags.STAIRS).add(family.stairs().asItem());
		tag(ItemTags.WALLS).add(family.wall().asItem());

		tag(ItemTags.BUTTONS).add(family.button().asItem());
		tag(ItemTags.STONE_BUTTONS).add(family.button().asItem());

		tag(ItemTags.STONE_CRAFTING_MATERIALS).add(family.base().asItem());
	}

	private void woodFamily(WoodFamily family) {
		tag(ItemTags.SLABS).add(family.slab().asItem());
		tag(ItemTags.STAIRS).add(family.stairs().asItem());

		tag(ItemTags.BUTTONS).add(family.button().asItem());
		tag(ItemTags.WOODEN_BUTTONS).add(family.button().asItem());
		tag(ItemTags.DOORS).add(family.door().asItem());
		tag(ItemTags.WOODEN_DOORS).add(family.door().asItem());
		tag(ItemTags.TRAPDOORS).add(family.trapdoor().asItem());
		tag(ItemTags.WOODEN_TRAPDOORS).add(family.trapdoor().asItem());
		tag(ItemTags.FENCES).add(family.fence().asItem());
		tag(ItemTags.WOODEN_FENCES).add(family.fence().asItem());
		tag(ItemTags.FENCE_GATES).add(family.fencegate().asItem());
		tag(ItemTags.WOODEN_PRESSURE_PLATES).add(family.pressure_plate().asItem());

		tag(ItemTags.SIGNS).add(family.sign());
		tag(ItemTags.HANGING_SIGNS).add(family.hangingsign());

		tag(ItemTags.LOGS).add(family.log().asItem(), family.stripped_log().asItem(), family.wood().asItem(),
				family.stripped_wood().asItem());
		tag(ItemTags.PLANKS).add(family.planks().asItem());
		tag(ItemTags.LEAVES).add(Stream.of(family.leaves()).map(Block::asItem).toArray(Item[]::new));

		tag(common("slabs/" + family.name())).add(family.slab().asItem());
		tag(common("stairs/" + family.name())).add(family.stairs().asItem());
		tag(common("buttons/" + family.name())).add(family.button().asItem());
		tag(common("wooden_buttons/" + family.name())).add(family.button().asItem());
		tag(common("doors/" + family.name())).add(family.door().asItem());
		tag(common("wooden_doors/" + family.name())).add(family.door().asItem());
		tag(common("trapdoors/" + family.name())).add(family.trapdoor().asItem());
		tag(common("wooden_trapdoors/" + family.name())).add(family.trapdoor().asItem());
		tag(common("fences/" + family.name())).add(family.fence().asItem());
		tag(common("wooden_fences/" + family.name())).add(family.fence().asItem());
		tag(common("fencegates/" + family.name())).add(family.fencegate().asItem());
		tag(common("wooden_pressure_plates/" + family.name())).add(family.pressure_plate().asItem());
		tag(common("signs/" + family.name())).add(family.sign());
		tag(common("hanging_signs/" + family.name())).add(family.hangingsign());
		tag(common("logs/" + family.name())).add(family.log().asItem(), family.stripped_log().asItem(),
				family.wood().asItem(), family.stripped_wood().asItem());
		tag(common("planks/" + family.name())).add(family.planks().asItem());
		tag(common("leaves/" + family.name())).add(Stream.of(family.leaves()).map(Block::asItem).toArray(Item[]::new));
		tag(common("wooden_logs/" + family.name())).add(family.log().asItem(), family.stripped_log().asItem(),
				family.wood().asItem(), family.stripped_wood().asItem());
		tag(common("wooden_planks/" + family.name())).add(family.planks().asItem());

		tag(common("slabs")).addTag(common("slabs/" + family.name()));
		tag(common("stairs")).addTag(common("stairs/" + family.name()));
		tag(common("buttons")).addTag(common("buttons/" + family.name()));
		tag(common("wooden_buttons")).addTag(common("wooden_buttons/" + family.name()));
		tag(common("doors")).addTag(common("doors/" + family.name()));
		tag(common("wooden_doors")).addTag(common("wooden_doors/" + family.name()));
		tag(common("trapdoors")).addTag(common("trapdoors/" + family.name()));
		tag(common("wooden_trapdoors")).addTag(common("wooden_trapdoors/" + family.name()));
		tag(common("fences")).addTag(common("fences/" + family.name()));
		tag(common("wooden_fences")).addTag(common("wooden_fences/" + family.name()));
		tag(common("fencegates")).addTag(common("fencegates/" + family.name()));
		tag(common("wooden_pressure_plates")).addTag(common("wooden_pressure_plates/" + family.name()));
		tag(common("signs")).addTag(common("signs/" + family.name()));
		tag(common("hanging_signs")).addTag(common("hanging_signs/" + family.name()));
		tag(common("logs")).addTag(common("logs/" + family.name()));
		tag(common("planks")).addTag(common("planks/" + family.name()));
		tag(common("wooden_logs")).addTag(common("wooden_logs/" + family.name()));
		tag(common("wooden_planks")).addTag(common("wooden_planks/" + family.name()));
	}

	private static TagKey<Item> common(String name) {
		return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", name));
	}
}