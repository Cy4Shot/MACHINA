package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.item.BlueprintItem;
import com.machina.item.CatalystItem;
import com.machina.item.ChemicalItem;
import com.machina.item.MachinaDiscItem;
import com.machina.item.ScannerItem;
import com.machina.item.ShipComponentItem;
import com.machina.registration.Registration;

import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.util.NonNullFunction;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ItemInit {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Machina.MOD_ID);

	//@formatter:off
	public static final RegistryObject<BlueprintItem> BLUEPRINT = basic("blueprint", BlueprintItem::new);
	public static final RegistryObject<ShipComponentItem> SHIP_COMPONENT = basic("ship_component", ShipComponentItem::new);
	public static final RegistryObject<ScannerItem> SCANNER = basic("scanner", ScannerItem::new);
	public static final RegistryObject<CatalystItem> IRON_CATALYST = basic("iron_catalyst", p -> new CatalystItem(p, 500));
	public static final RegistryObject<Item> REINFORCED_STICK = basic("reinforced_stick");
	public static final RegistryObject<Item> STEEL_INGOT = basic("steel_ingot");
	public static final RegistryObject<Item> STEEL_NUGGET = basic("steel_nugget");
	public static final RegistryObject<Item> ALUMINUM_INGOT = basic("aluminum_ingot");
	public static final RegistryObject<Item> ALUMINUM_NUGGET = basic("aluminum_nugget");
	public static final RegistryObject<Item> COPPER_INGOT = basic("copper_ingot");
	public static final RegistryObject<Item> COPPER_NUGGET = basic("copper_nugget");
	public static final RegistryObject<Item> COPPER_COIL = basic("copper_coil");
	public static final RegistryObject<Item> TRANSISTOR = basic("transistor");
	public static final RegistryObject<Item> PROCESSOR = basic("processor");
	public static final RegistryObject<Item> SILICON = basic("silicon", p -> new ChemicalItem(p, "Si"));
	public static final RegistryObject<Item> SILICON_BOLUS = basic("silicon_bolus", p -> new ChemicalItem(p, "Si"));
	public static final RegistryObject<Item> HIGH_PURITY_SILICON = basic("high_purity_silicon", p -> new ChemicalItem(p, "Si"));
	public static final RegistryObject<Item> AMMONIUM_NITRATE = basic("ammonium_nitrate", p -> new ChemicalItem(p, "NH4NO3"));
	public static final RegistryObject<Item> LDPE = basic("ldpe", p -> new ChemicalItem(p, "ldpe", "(CH2CH2)"));
	public static final RegistryObject<Item> HDPE = basic("hdpe", p -> new ChemicalItem(p, "hdpe","(CH2CH2)"));
	public static final RegistryObject<Item> UHMWPE = basic("uhmwpe", p -> new ChemicalItem(p, "uhmwpe", "(CH2CH2)"));
	public static final RegistryObject<MachinaDiscItem> BEYOND_DISC = basic("beyond_disc", p -> new MachinaDiscItem(() -> SoundInit.BEYOND.sound()));
	public static final RegistryObject<MachinaDiscItem> LOOK_UP_DISC = basic("look_up_disc", p -> new MachinaDiscItem(() -> SoundInit.LOOK_UP.sound()));
	public static final RegistryObject<MachinaDiscItem> BOSS_01_DISC = basic("boss_01_disc", p -> new MachinaDiscItem(() -> SoundInit.BOSS_01.sound()));
	public static final RegistryObject<MachinaDiscItem> BOSS_02_DISC = basic("boss_02_disc", p -> new MachinaDiscItem(() -> SoundInit.BOSS_02.sound()));
	public static final RegistryObject<MachinaDiscItem> BOSS_03_DISC = basic("boss_03_disc", p -> new MachinaDiscItem(() -> SoundInit.BOSS_03.sound()));
	//@formatter:on

	public static RegistryObject<Item> basic(String name) {
		return register(name, () -> ItemBuilder.basicItem());
	}

	public static <T extends Item> RegistryObject<T> basic(String name, NonNullFunction<Item.Properties, T> factory) {
		return register(name, () -> ItemBuilder.basicItem(factory));
	}

	public static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item) {
		return ITEMS.register(name, item);
	}

	public static class ItemBuilder<T extends Item> {

		private final NonNullFunction<Item.Properties, T> factory;
		private ItemGroup tab = ItemGroup.TAB_MISC;

		protected ItemBuilder(NonNullFunction<Item.Properties, T> factory) {
			this.factory = factory;
		}

		public static Item basicItem() {
			return new ItemBuilder<>(Item::new).tab(Registration.MAIN_GROUP).build();
		}

		public static <T extends Item> T basicItem(NonNullFunction<Item.Properties, T> factory) {
			return new ItemBuilder<>(factory).tab(Registration.MAIN_GROUP).build();
		}

		public static <T extends Item> ItemBuilder<T> create(NonNullFunction<Item.Properties, T> factory) {
			return new ItemBuilder<>(factory);
		}

		public T build() {
			return factory.apply(getProperties());
		}

		public ItemBuilder<T> tab(ItemGroup tab) {
			this.tab = tab;
			return this;
		}

		public Properties getProperties() {
			return new Properties().tab(tab);
		}
	}

}
