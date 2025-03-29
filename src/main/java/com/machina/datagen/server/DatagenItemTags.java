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
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

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

		FamiliesInit.ORES.forEach(this::oreFamily);
		FamiliesInit.DIRTS.forEach(this::dirtFamily);
		FamiliesInit.STONES.forEach(this::stoneFamily);
		FamiliesInit.WOODS.forEach(this::woodFamily);
	}

	private void smallFlower(RegistryObject<? extends BushBlock> flower) {
		flower(flower);
		tag(ItemTags.SMALL_FLOWERS).add(flower.get().asItem());
	}

	private void tallFlower(RegistryObject<TallFlowerBlock> flower) {
		flower(flower);
		tag(ItemTags.TALL_FLOWERS).add(flower.get().asItem());
	}

	private void flower(RegistryObject<? extends BushBlock> flower) {
		tag(ItemTags.FLOWERS).add(flower.get().asItem());
	}

	private void capacitor(RegistryObject<? extends CapacitorItem> capacitor) {
		tag(ItemTagInit.CAPACITOR).add(capacitor.get());
	}

	private void oreFamily(OreFamily family) {
		family.getOre().ifPresent(ore -> {
			tag(forge("ores")).add(ore.asItem());
			tag(forge("ores/" + family.name())).add(ore.asItem());
		});
		family.getBlock().ifPresent(block -> {
			tag(forge("storage_blocks")).add(block.asItem());
			tag(forge("storage_blocks/" + family.name())).add(block.asItem());
		});

		family.getNugget().ifPresent(item -> {
			tag(forge("nuggets")).add(item);
			tag(forge("nuggets/" + family.name())).add(item);
		});
		family.getIngot().ifPresent(item -> {
			tag(forge("ingots")).add(item);
			tag(forge("ingots/" + family.name())).add(item);
		});
		family.getDust().ifPresent(item -> {
			tag(forge("dusts")).add(item);
			tag(forge("dusts/" + family.name())).add(item);
		});
		family.plate().ifPresent(item -> {
			tag(forge("plates")).add(item);
			tag(forge("plates/" + family.name())).add(item);
		});
		family.rod().ifPresent(item -> {
			tag(forge("rods")).add(item);
			tag(forge("rods/" + family.name())).add(item);
		});
		family.wire().ifPresent(item -> {
			tag(forge("wires")).add(item);
			tag(forge("wires/" + family.name())).add(item);
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
	}

	private static TagKey<Item> forge(String name) {
		return TagKey.create(Registries.ITEM, new ResourceLocation("forge", name));
	}
}