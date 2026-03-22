package com.machina.datagen.client;

import java.util.LinkedHashMap;
import java.util.Objects;

import com.machina.Machina;
import com.machina.api.util.MachinaRL;
import com.machina.datagen.client.builder.BEWLRModelLoaderBuilder;
import com.machina.registration.init.FamiliesInit;
import com.machina.registration.init.FamiliesInit.OreFamily;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;
import com.machina.registration.init.FruitInit;
import com.machina.registration.init.FruitInit.Fruit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.RocketPartInit;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

public class DatagenItemModels extends ItemModelProvider {
	private static final LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();

	static {
		trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
		trimMaterials.put(TrimMaterials.IRON, 0.2F);
		trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
		trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
		trimMaterials.put(TrimMaterials.COPPER, 0.5F);
		trimMaterials.put(TrimMaterials.GOLD, 0.6F);
		trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
		trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
		trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
		trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
	}

	public DatagenItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, Machina.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		simpleItem(ItemInit.BASIC_CAPACITOR);
		simpleItem(ItemInit.ADVANCED_CAPACITOR);
		simpleItem(ItemInit.SUPREME_CAPACITOR);

		simpleItem(ItemInit.MOULD_BASE);
		simpleItem(ItemInit.MOULD_PLATE);
		simpleItem(ItemInit.MOULD_ROD);
		simpleItem(ItemInit.MOULD_WIRE);

		simpleItem(ItemInit.ITEM_FILTER);
		simpleItem(ItemInit.ADVANCED_ITEM_FILTER);
		simpleItem(ItemInit.FLUID_FILTER);

		simpleItem(ItemInit.COAL_CHUNK);
		simpleItem(ItemInit.AMMONIUM_NITRATE);
		simpleItem(ItemInit.COPPER_COIL);

		simpleItem(ItemInit.TROPICAL_SIGN);
		simpleItem(ItemInit.TROPICAL_HANGING_SIGN);

		simpleItem(ItemInit.DEAD_TROPICAL_SIGN);
		simpleItem(ItemInit.DEAD_TROPICAL_HANGING_SIGN);

		simpleItem(ItemInit.PINE_SIGN);
		simpleItem(ItemInit.PINE_HANGING_SIGN);

		simpleItem(ItemInit.CONIFEROUS_SIGN);
		simpleItem(ItemInit.CONIFEROUS_HANGING_SIGN);

		simpleItem(ItemInit.CYCAD_SIGN);
		simpleItem(ItemInit.CYCAD_HANGING_SIGN);

		bewlr(ItemInit.ROCKET.get());

		// Dynamic
		FruitInit.FRUITS.forEach(this::fruit);
		FluidInit.OBJS.forEach(this::bucket);
		FamiliesInit.ORES.forEach(this::oreFamily);
		RocketPartInit.ROCKET_PARTS.getEntries().forEach(part -> bewlr(part.get().getItem()));
	}

	private String name(Item item) {
		return BuiltInRegistries.ITEM.getKey(item).getPath();
	}

	private void oreFamily(OreFamily fam) {
		fam.getDust().ifPresent(this::simpleItem);
		fam.getIngot().ifPresent(this::simpleItem);
		fam.getNugget().ifPresent(ingot -> {
			if (!(ingot instanceof BlockItem)) {
				this.simpleItem(ingot);
			}
		});
		fam.plate().ifPresent(this::simpleItem);
		fam.rod().ifPresent(this::simpleItem);
		fam.wire().ifPresent(this::simpleItem);
		fam.getRaw().ifPresent(this::simpleItem);
	}

	protected void bucket(FluidObject obj) {
		DynamicFluidContainerModelBuilder<ItemModelBuilder> builder = withExistingParent(name(obj.fluid().getBucket()),
				ResourceLocation.fromNamespaceAndPath("neoforge", "item/bucket"))
				.customLoader(DynamicFluidContainerModelBuilder::begin);
		if (obj.fluid().getFluidType().getDensity() < 0) {
			builder.flipGas(true);
		}
		builder.fluid(obj.fluid());
	}

	@SuppressWarnings("unused")
	private void trimmedArmorItem(DeferredItem<Item> itemDeferredBlock) {
		if (itemDeferredBlock.get() instanceof ArmorItem armorItem) {
			trimMaterials.forEach((trimMaterial, value) -> {

				float trimValue = value;

				String armorType = switch (armorItem.getEquipmentSlot()) {
				case HEAD -> "helmet";
				case CHEST -> "chestplate";
				case LEGS -> "leggings";
				case FEET -> "boots";
				default -> "";
				};

				String armorItemPath = "item/" + armorItem;
				String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
				String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
				ResourceLocation armorItemResLoc = MachinaRL.create(armorItemPath);
				ResourceLocation trimResLoc = ResourceLocation.withDefaultNamespace(trimPath);
				ResourceLocation trimNameResLoc = MachinaRL.create(currentTrimName);

				existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

				getBuilder(currentTrimName).parent(new ModelFile.UncheckedModelFile("item/generated"))
						.texture("layer0", armorItemResLoc).texture("layer1", trimResLoc);

				this.withExistingParent(itemDeferredBlock.getId().getPath(), mcLoc("item/generated")).override()
						.model(new ModelFile.UncheckedModelFile(trimNameResLoc))
						.predicate(mcLoc("trim_type"), trimValue).end()
						.texture("layer0", MachinaRL.create("item/" + itemDeferredBlock.getId().getPath()));
			});
		}
	}

	private void bewlr(Item item) {
		getBuilder(name(item)).customLoader(BEWLRModelLoaderBuilder::new);
	}

	private void simpleItem(Item item) {
		String name = name(item);
		withExistingParent(name, ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
				MachinaRL.create("item/" + name));
	}

	private void simpleItem(DeferredItem<? extends Item> item) {
		withExistingParent(item.getId().getPath(), ResourceLocation.withDefaultNamespace("item/generated"))
				.texture("layer0", MachinaRL.create("item/" + item.getId().getPath()));
	}

	public void evenSimplerBlockItem(DeferredBlock<? extends Block> block) {
		this.withExistingParent(
				Machina.MOD_ID + ":" + Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block.get())).getPath(),
				modLoc("block/" + Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block.get())).getPath()));
	}

	public void trapdoorItem(DeferredBlock<Block> block) {
		this.withExistingParent(Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block.get())).getPath(), modLoc(
				"block/" + Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block.get())).getPath() + "_bottom"));
	}

	public void fenceItem(DeferredBlock<Block> block, DeferredBlock<Block> baseBlock) {
		this.withExistingParent(Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block.get())).getPath(),
				mcLoc("block/fence_inventory"))
				.texture("texture", ResourceLocation.fromNamespaceAndPath(Machina.MOD_ID,
						"block/" + Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(baseBlock.get())).getPath()));
	}

	public void buttonItem(DeferredBlock<Block> block, DeferredBlock<Block> baseBlock) {
		this.withExistingParent(Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block.get())).getPath(),
				mcLoc("block/button_inventory"))
				.texture("texture", ResourceLocation.fromNamespaceAndPath(Machina.MOD_ID,
						"block/" + Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(baseBlock.get())).getPath()));
	}

	public void wallItem(DeferredBlock<Block> block, DeferredBlock<Block> baseBlock) {
		this.withExistingParent(Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block.get())).getPath(),
				mcLoc("block/wall_inventory"))
				.texture("wall", ResourceLocation.fromNamespaceAndPath(Machina.MOD_ID,
						"block/" + Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(baseBlock.get())).getPath()));
	}

	private void fruit(Fruit fruit) {
		simpleItem(fruit.item().get());
	}

	@SuppressWarnings("unused")
	private ItemModelBuilder handheldItem(DeferredItem<Item> item) {
		return withExistingParent(item.getId().getPath(), ResourceLocation.withDefaultNamespace("item/handheld"))
				.texture("layer0", MachinaRL.create("item/" + item.getId().getPath()));
	}

	@SuppressWarnings("unused")
	private ItemModelBuilder simpleBlockItem(DeferredBlock<? extends Block> item) {
		return withExistingParent(item.getId().getPath(), ResourceLocation.withDefaultNamespace("item/generated"))
				.texture("layer0", MachinaRL.create("item/" + item.getId().getPath()));
	}

	@SuppressWarnings("unused")
	private ItemModelBuilder simpleBlockItemBlockTexture(DeferredBlock<? extends Block> item) {
		return withExistingParent(item.getId().getPath(), ResourceLocation.withDefaultNamespace("item/generated"))
				.texture("layer0", MachinaRL.create("block/" + item.getId().getPath()));
	}
}