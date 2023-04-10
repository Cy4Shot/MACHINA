package com.machina.datagen.client;

import com.machina.Machina;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;
import com.machina.util.text.MachinaRL;
import com.machina.registration.init.ItemInit;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.loaders.DynamicBucketModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {

	public ItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, Machina.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		block(BlockInit.SHIP_CONSOLE.get());
		block(BlockInit.ATMOSPHERIC_SEPARATOR.get());
		block(BlockInit.TEMPERATURE_REGULATOR.get());
		block(BlockInit.ALIEN_STONE.get());
		block(BlockInit.ALIEN_STONE_SLAB.get());
		block(BlockInit.ALIEN_STONE_STAIRS.get());
		block(BlockInit.TWILIGHT_DIRT.get());
		block(BlockInit.TWILIGHT_DIRT_SLAB.get());
		block(BlockInit.TWILIGHT_DIRT_STAIRS.get());
		block(BlockInit.WASTELAND_DIRT.get());
		block(BlockInit.WASTELAND_DIRT_SLAB.get());
		block(BlockInit.WASTELAND_DIRT_STAIRS.get());
		block(BlockInit.WASTELAND_SAND.get());
		block(BlockInit.WASTELAND_SANDSTONE.get());
		block(BlockInit.WASTELAND_SANDSTONE_SLAB.get());
		block(BlockInit.WASTELAND_SANDSTONE_STAIRS.get());
		block(BlockInit.WASTELAND_SANDSTONE_WALL.get());
		block(BlockInit.STEEL_BLOCK.get());
		block(BlockInit.ALUMINUM_BLOCK.get());
		block(BlockInit.ALUMINUM_ORE.get());
		block(BlockInit.COPPER_BLOCK.get());
		block(BlockInit.COPPER_ORE.get());
		block(BlockInit.IRON_SCAFFOLDING.get());
		block(BlockInit.STEEL_SCAFFOLDING.get());
		block(BlockInit.ALUMINUM_SCAFFOLDING.get());
		block(BlockInit.COPPER_SCAFFOLDING.get());
		block(BlockInit.PUZZLE_BLOCK.get());
		block(BlockInit.PRESSURIZED_CHAMBER.get());
		block(BlockInit.BATTERY.get());
		block(BlockInit.TANK.get());
		block(BlockInit.CABLE.get());
		block(BlockInit.CREATIVE_BATTERY.get());
		block(BlockInit.REINFORCED_TILE.get());
		block(BlockInit.FUEL_STORAGE_UNIT.get());
		block(BlockInit.FURNACE_GENERATOR.get());
		block(BlockInit.STATE_CONVERTER.get());
		block(BlockInit.FLUID_HOPPER.get());
		block(BlockInit.IRON_CHASSIS.get());
		block(BlockInit.STEEL_CHASSIS.get());
		BlockInit.ORE_MAP.values().forEach(m -> {
			m.values().forEach(b -> {
				block(b.get());
			});
		});

		geo(BlockInit.IRON_SCAFFOLDING.get());
		geo(BlockInit.STEEL_SCAFFOLDING.get());
		geo(BlockInit.ALUMINUM_SCAFFOLDING.get());
		geo(BlockInit.COPPER_SCAFFOLDING.get());
		geo(BlockInit.COMPONENT_ANALYZER.get());
		geo(BlockInit.CARGO_CRATE.get());

		item(ItemInit.BLUEPRINT.get());
		item(ItemInit.REINFORCED_STICK.get());
		item(ItemInit.STEEL_INGOT.get());
		item(ItemInit.STEEL_NUGGET.get());
		item(ItemInit.ALUMINUM_INGOT.get());
		item(ItemInit.ALUMINUM_NUGGET.get());
		item(ItemInit.COPPER_INGOT.get());
		item(ItemInit.COPPER_NUGGET.get());
		item(ItemInit.COPPER_COIL.get());
		item(ItemInit.IRON_CATALYST.get());
		item(ItemInit.PROCESSOR.get());
		item(ItemInit.SILICON.get());
		item(ItemInit.SCANNER.get());
		item(ItemInit.TRANSISTOR.get());
		item(ItemInit.AMMONIUM_NITRATE.get());
		item(ItemInit.BEYOND_DISC.get());
		item(ItemInit.LOOK_UP_DISC.get());
		item(ItemInit.BOSS_01_DISC.get());
		item(ItemInit.BOSS_02_DISC.get());
		item(ItemInit.BOSS_03_DISC.get());
		item(ItemInit.LDPE.get());
		item(ItemInit.HDPE.get());
		item(ItemInit.UHMWPE.get());

		bucket(FluidInit.OXYGEN);
		bucket(FluidInit.NITROGEN);
		bucket(FluidInit.AMMONIA);
		bucket(FluidInit.CARBON_DIOXIDE);
		bucket(FluidInit.HYDROGEN);
		bucket(FluidInit.LIQUID_HYDROGEN);
		bucket(FluidInit.LIQUID_AMMONIA);
		bucket(FluidInit.ETHANE);
		bucket(FluidInit.PROPANE);
		bucket(FluidInit.ETHYLENE);
		bucket(FluidInit.PROPYLENE);
	}

	protected void bucket(FluidObject obj) {
		DynamicBucketModelBuilder<ItemModelBuilder> builder = withExistingParent(
				obj.fluid().getBucket().getRegistryName().getPath(), new ResourceLocation("forge", "item/bucket"))
						.customLoader(DynamicBucketModelBuilder::begin);
		if (obj.fluid().getAttributes().isGaseous()) {
			builder.flipGas(true);
		}
		builder.fluid(obj.fluid());
	}

	protected void geo(Block block) {
		getBuilder(block.asItem().getRegistryName().toString()).parent(getExistingFile(new MachinaRL("item/geo_item")));
	}

	protected void block(Block block) {
		getBuilder(block.asItem().getRegistryName().toString())
				.parent(getExistingFile(new MachinaRL("block/" + block.asItem().getRegistryName().getPath())));
	}

	protected void item(Item item, ResourceLocation texture) {
		ResourceLocation itemTexture = new ResourceLocation(texture.getNamespace(), "item/" + texture.getPath());
		if (existingFileHelper.exists(itemTexture, ResourcePackType.CLIENT_RESOURCES, ".png", "textures")) {
			getBuilder(item.getRegistryName().getPath()).parent(getExistingFile(mcLoc("item/generated")))
					.texture("layer0", itemTexture);
		} else {
			Machina.LOGGER.warn(
					"Texture for " + item.getRegistryName().toString() + " not present at " + itemTexture.toString());
		}
	}

	protected void item(Item item) {
		item(item, item.getRegistryName());
	}

}
