package com.machina.registration.init;

import java.util.List;
import java.util.function.Consumer;

import com.machina.Machina;
import com.machina.registration.init.FamiliesInit.ItemLikeFamily;
import com.machina.registration.init.FluidInit.FluidObject;
import com.machina.registration.init.FruitInit.Fruit;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TabInit {
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
			.create(Registries.CREATIVE_MODE_TAB, Machina.MOD_ID);

	public static final RegistryObject<CreativeModeTab> MACHINA_MACHINERY = create("machina_machinery",
			BlockInit.FURNACE_GENERATOR, a -> {
				add(a, ItemInit.BLUEPRINT);

				add(a, BlockInit.ENERGY_CABLE);
				add(a, BlockInit.FLUID_PIPE);
				add(a, BlockInit.ITEM_CONDUIT);

				add(a, ItemInit.FLUID_FILTER);
				add(a, ItemInit.ITEM_FILTER);
				add(a, ItemInit.ADVANCED_ITEM_FILTER);

				add(a, BlockInit.BATTERY);
				add(a, BlockInit.TANK);
				add(a, BlockInit.BASIC_MACHINE_CASE);
				add(a, BlockInit.FURNACE_GENERATOR);
				add(a, BlockInit.GRINDER);
				add(a, BlockInit.COMPRESSOR);
				add(a, BlockInit.MELTER);
				add(a, BlockInit.MIXER);
				add(a, BlockInit.COMPOSTER_VAT);

				add(a, BlockInit.CREATIVE_BATTERY);

				add(a, ItemInit.BASIC_CAPACITOR);
				add(a, ItemInit.ADVANCED_CAPACITOR);
				add(a, ItemInit.SUPREME_CAPACITOR);
				add(a, ItemInit.MOULD_BASE);
				add(a, ItemInit.MOULD_PLATE);
				add(a, ItemInit.MOULD_PLATE);
				add(a, ItemInit.MOULD_ROD);
				add(a, ItemInit.MOULD_WIRE);
			});

	public static final RegistryObject<CreativeModeTab> MACHINA_RESOURCES = create("machina_resources",
			ItemInit.ALUMINUM_INGOT, a -> {

				add(a, ItemInit.COAL_CHUNK);
				family(a, FamiliesInit.ORES);

				add(a, ItemInit.SILICON);
				add(a, ItemInit.RAW_SILICON_BLEND);
				add(a, ItemInit.SILICON_BOLUS);
				add(a, ItemInit.HIGH_PURITY_SILICON);

				add(a, ItemInit.LDPE);
				add(a, ItemInit.HDPE);
				add(a, ItemInit.UHMWPE);

				add(a, ItemInit.AMMONIUM_NITRATE);
				add(a, ItemInit.CALCIUM_SULPHATE);
				add(a, ItemInit.HEXAMINE);
				add(a, ItemInit.NITRONIUM_TETRAFLUOROBORATE);
				add(a, ItemInit.PALLADIUM_CHLORIDE);
				add(a, ItemInit.PALLADIUM_ON_CARBON);
				add(a, ItemInit.SODIUM_CARBONATE);
				add(a, ItemInit.SODIUM_HYDROXIDE);

				for (FluidObject e : FluidInit.OBJS) {
					add(a, e.bucket());
				}
			});

	public static final RegistryObject<CreativeModeTab> MACHINA_WORLDGEN = create("machina_worldgen",
			BlockInit.TROPICAL_GRASS_BLOCK, a -> {
				family(a, FamiliesInit.DIRTS);

				add(a, BlockInit.ALUMINUM_ORE);
				family(a, FamiliesInit.STONES);

				family(a, FamiliesInit.WOODS);

				add(a, BlockInit.BROWN_MUSHROOM_STALK);
				add(a, BlockInit.GREEN_MUSHROOM_STALK);
				add(a, BlockInit.PURPLE_MUSHROOM_STALK);
				add(a, BlockInit.YELLOW_MUSHROOM_STALK);
				add(a, BlockInit.BROWN_MUSHROOM_CAP);
				add(a, BlockInit.RED_MUSHROOM_CAP);
				add(a, BlockInit.PURPLE_MUSHROOM_CAP);
				add(a, BlockInit.YELLOW_MUSHROOM_CAP);
				add(a, BlockInit.BROWN_MUSHROOM_GILLS);
				add(a, BlockInit.RED_MUSHROOM_GILLS);
				add(a, BlockInit.PURPLE_MUSHROOM_GILLS);
				add(a, BlockInit.YELLOW_MUSHROOM_GILLS);
				add(a, BlockInit.SPECKLED_BROWN_MUSHROOM_CAP);
				add(a, BlockInit.SPECKLED_RED_MUSHROOM_CAP);
				add(a, BlockInit.SPECKLED_PURPLE_MUSHROOM_CAP);
				add(a, BlockInit.SPECKLED_YELLOW_MUSHROOM_CAP);
				add(a, BlockInit.IMBUED_BROWN_MUSHROOM_CAP);
				add(a, BlockInit.IMBUED_RED_MUSHROOM_CAP);
				add(a, BlockInit.IMBUED_PURPLE_MUSHROOM_CAP);
				add(a, BlockInit.IMBUED_YELLOW_MUSHROOM_CAP);

				add(a, BlockInit.TROPICAL_GRASS);
				add(a, BlockInit.TWISTED_GRASS);
				add(a, BlockInit.CONIFEROUS_GRASS);
				add(a, BlockInit.SHORT_CONIFEROUS_GRASS);
				add(a, BlockInit.WINDSWEPT_GRASS);
				add(a, BlockInit.MYCELIAL_GRASS);

				add(a, BlockInit.PURPLE_GROUNDLILY);
				add(a, BlockInit.PINK_GROUNDLILY);
				add(a, BlockInit.RED_GROUNDLILY);
				add(a, BlockInit.ORANGE_GROUNDLILY);
				add(a, BlockInit.YELLOW_GROUNDLILY);
				add(a, BlockInit.GREEN_GROUNDLILY);
				add(a, BlockInit.TURQUOISE_GROUNDLILY);
				add(a, BlockInit.BLUE_GROUNDLILY);
				add(a, BlockInit.PURPLE_WATERLILY);
				add(a, BlockInit.PINK_WATERLILY);
				add(a, BlockInit.RED_WATERLILY);
				add(a, BlockInit.ORANGE_WATERLILY);
				add(a, BlockInit.YELLOW_WATERLILY);
				add(a, BlockInit.GREEN_WATERLILY);
				add(a, BlockInit.TURQUOISE_WATERLILY);
				add(a, BlockInit.BLUE_WATERLILY);

				add(a, BlockInit.NEEDLEGRASS);
				add(a, BlockInit.NEEDLETHATCH);

				add(a, BlockInit.SPINDLESPROUT);
				add(a, BlockInit.SMALL_FERN);
				add(a, BlockInit.DEAD_SMALL_FERN);
				add(a, BlockInit.CLOVER);
				add(a, BlockInit.SPINDLEGRASS);

				add(a, BlockInit.ORPHEUM);
				add(a, BlockInit.DRAGON_PEONY);

				add(a, BlockInit.PURPLE_PETALS);
				add(a, BlockInit.RED_PETALS);
				add(a, BlockInit.ORANGE_PETALS);
				add(a, BlockInit.YELLOW_PETALS);
				add(a, BlockInit.GREEN_PETALS);
				add(a, BlockInit.TURQUOISE_PETALS);
				add(a, BlockInit.BLUE_PETALS);

				add(a, BlockInit.SPRUCE_CUP);

				add(a, BlockInit.PURPLE_GLOWSHROOM);
				add(a, BlockInit.PINK_GLOWSHROOM);
				add(a, BlockInit.RED_GLOWSHROOM);
				add(a, BlockInit.ORANGE_GLOWSHROOM);
				add(a, BlockInit.YELLOW_GLOWSHROOM);
				add(a, BlockInit.GREEN_GLOWSHROOM);
				add(a, BlockInit.TURQUOISE_GLOWSHROOM);
				add(a, BlockInit.BLUE_GLOWSHROOM);

				fruit(a, FruitInit.FRUITS);
			});

	public static final RegistryObject<CreativeModeTab> MACHINA_MISCELLANEOUS = create("machina_misc",
			ItemInit.LOGIC_UNIT, a -> {
				add(a, ItemInit.COPPER_COIL);
				add(a, ItemInit.LOGIC_UNIT);
				add(a, ItemInit.PROCESSOR);
				add(a, ItemInit.PROCESSOR_CORE);
				add(a, ItemInit.TRANSISTOR);
			});

	public static void add(CreativeModeTab.Output adder, ItemLike item) {
		adder.accept(item);
	}

	public static void add(CreativeModeTab.Output adder, RegistryObject<? extends ItemLike> item) {
		add(adder, item.get());
	}

	public static void add(CreativeModeTab.Output adder, Fruit fruit) {
		add(adder, fruit.item());
	}

	public static void fruit(CreativeModeTab.Output adder, List<Fruit> fruit) {
		fruit.forEach(f -> add(adder, f));
	}

	public static void family(CreativeModeTab.Output adder, List<? extends ItemLikeFamily> family) {
		family.forEach(f -> f.tab().forEach(i -> add(adder, i)));
	}

	public static RegistryObject<CreativeModeTab> create(String name, RegistryObject<? extends ItemLike> item,
			Consumer<CreativeModeTab.Output> gen) {
		return CREATIVE_MODE_TABS.register(name,
				() -> CreativeModeTab.builder().icon(() -> new ItemStack(item.get()))
						.title(Component.translatable(Machina.MOD_ID + ".creativemodetab." + name))
						.displayItems((p, a) -> gen.accept(a)).build());
	}
}
