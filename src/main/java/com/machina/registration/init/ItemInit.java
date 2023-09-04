package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.api.item.ChemicalItem;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.common.util.NonNullFunction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Machina.MOD_ID);

	//@formatter:off
	public static final RegistryObject<Item> RAW_ALUMINUM = basic("raw_aluminum");
	public static final RegistryObject<Item> ALUMINUM_INGOT = basic("aluminum_ingot");
	public static final RegistryObject<Item> ALUMINUM_NUGGET = basic("aluminum_nugget");
	public static final RegistryObject<Item> COPPER_COIL = basic("copper_coil");
	public static final RegistryObject<Item> TRANSISTOR = basic("transistor");
	public static final RegistryObject<Item> LOGIC_UNIT = basic("logic_unit");
	public static final RegistryObject<Item> PROCESSOR_CORE = basic("processor_core");
	public static final RegistryObject<Item> PROCESSOR = basic("processor");
	public static final RegistryObject<Item> RAW_SILICON_BLEND = basic("raw_silicon_blend");
	
	public static final RegistryObject<Item> SILICON = basic("silicon", p -> new ChemicalItem(p, "Si"));
	public static final RegistryObject<Item> SILICON_BOLUS = basic("silicon_bolus", p -> new ChemicalItem(p, "Si"));
	public static final RegistryObject<Item> HIGH_PURITY_SILICON = basic("high_purity_silicon", p -> new ChemicalItem(p, "Si"));
	public static final RegistryObject<Item> AMMONIUM_NITRATE = basic("ammonium_nitrate", p -> new ChemicalItem(p, "NH4NO3"));
	public static final RegistryObject<Item> LDPE = basic("ldpe", p -> new ChemicalItem(p, "ldpe", "(CH2CH2)"));
	public static final RegistryObject<Item> HDPE = basic("hdpe", p -> new ChemicalItem(p, "hdpe","(CH2CH2)"));
	public static final RegistryObject<Item> UHMWPE = basic("uhmwpe", p -> new ChemicalItem(p, "uhmwpe", "(CH2CH2)"));
	public static final RegistryObject<Item> SODIUM_HYDROXIDE = basic("sodium_hydroxide", p -> new ChemicalItem(p, "NaOH"));
	public static final RegistryObject<Item> SODIUM_CARBONATE = basic("sodium_carbonate", p -> new ChemicalItem(p, "Na2CO3"));
	public static final RegistryObject<Item> CALCIUM_SULPHATE = basic("calcium_sulphate", p -> new ChemicalItem(p, "CaSO4"));
	public static final RegistryObject<Item> PALLADIUM_CHLORIDE = basic("palladium_chloride", p -> new ChemicalItem(p, "PdCl2"));
	public static final RegistryObject<Item> PALLADIUM_ON_CARBON = basic("palladium_on_carbon", p -> new ChemicalItem(p, "Pd/C"));
	public static final RegistryObject<Item> HEXAMINE = basic("hexamine", p -> new ChemicalItem(p, "(CH2)6N4"));
	public static final RegistryObject<Item> NITRONIUM_TETRAFLUOROBORATE = basic("nitronium_tetrafluoroborate", p -> new ChemicalItem(p, "NO2BF4"));
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

		protected ItemBuilder(NonNullFunction<Item.Properties, T> factory) {
			this.factory = factory;
		}

		public static Item basicItem() {
			return new ItemBuilder<>(Item::new).build();
		}

		public static <T extends Item> T basicItem(NonNullFunction<Item.Properties, T> factory) {
			return new ItemBuilder<>(factory).build();
		}

		public static <T extends Item> ItemBuilder<T> create(NonNullFunction<Item.Properties, T> factory) {
			return new ItemBuilder<>(factory);
		}

		public T build() {
			return factory.apply(getProperties());
		}

		public Properties getProperties() {
			return new Properties();
		}
	}

}
