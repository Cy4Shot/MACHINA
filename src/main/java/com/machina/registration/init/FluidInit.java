package com.machina.registration.init;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.registration.Registration;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidInit {

	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS,
			Machina.MOD_ID);

	public static final List<String> BLOCKS = new ArrayList<>();

	//@formatter:off
	
	//Blocks
	public static final RegistryObject<FlowingFluidBlock> LIQUID_HYDROGEN_BLOCK = block("liquid_hydrogen_block", () -> FluidInit.LIQUID_HYDROGEN.get());
	
	// Items
	public static final RegistryObject<BucketItem> OXYGEN_BUCKET = bucket("oxygen_bucket", () -> FluidInit.OXYGEN.get());
	public static final RegistryObject<BucketItem> HYDROGEN_BUCKET = bucket("hydrogen_bucket", () -> FluidInit.HYDROGEN.get());
	public static final RegistryObject<BucketItem> LIQUID_HYDROGEN_BUCKET = bucket("liquid_hydrogen_bucket", () -> FluidInit.LIQUID_HYDROGEN.get());
	
	// Fluids
	public static final RegistryObject<ForgeFlowingFluid> OXYGEN = register("oxygen", Prop.OXYGEN, false);
	public static final RegistryObject<ForgeFlowingFluid> OXYGEN_FLOWING = register("oxygen", Prop.OXYGEN, true);
	public static final RegistryObject<ForgeFlowingFluid> HYDROGEN = register("hydrogen", Prop.HYDROGEN, false);
	public static final RegistryObject<ForgeFlowingFluid> HYDROGEN_FLOWING = register("hydrogen", Prop.HYDROGEN, true);
	public static final RegistryObject<ForgeFlowingFluid> LIQUID_HYDROGEN = register("liquid_hydrogen", Prop.LIQUID_HYDROGEN, false);
	public static final RegistryObject<ForgeFlowingFluid> LIQUID_HYDROGEN_FLOWING = register("liquid_hydrogen", Prop.LIQUID_HYDROGEN, true);

	//@formatter:on

	public static final RegistryObject<ForgeFlowingFluid> register(String name, ForgeFlowingFluid.Properties p,
			boolean f) {
		if (f)
			return FLUIDS.register(name + "_flowing", () -> new ForgeFlowingFluid.Flowing(p));
		else
			return FLUIDS.register(name, () -> new ForgeFlowingFluid.Source(p));
	}

	private static RegistryObject<FlowingFluidBlock> block(String name, Supplier<? extends FlowingFluid> sup) {
		BLOCKS.add(name);
		return BlockInit.register(name, () -> new FlowingFluidBlock(sup, AbstractBlock.Properties.copy(Blocks.WATER)));
	}

	private static RegistryObject<BucketItem> bucket(String name, Supplier<FlowingFluid> fluid) {
		return ItemInit.register(name, () -> new BucketItem(fluid,
				new Item.Properties().tab(Registration.MACHINA_ITEM_GROUP).stacksTo(1).craftRemainder(Items.BUCKET)));
	}

	public static class Prop {
		
		public static final ResourceLocation STILL_RL = new ResourceLocation("block/water_still");
		public static final ResourceLocation FLOWING_RL = new ResourceLocation("block/water_flow");
		public static final ResourceLocation OVERLAY_RL = new ResourceLocation("block/water_overlay");

		public static final ForgeFlowingFluid.Properties OXYGEN = gas(ChemicalValues.OXYGEN,
				() -> FluidInit.OXYGEN.get(), () -> OXYGEN_FLOWING.get(), () -> OXYGEN_BUCKET.get());

		public static final ForgeFlowingFluid.Properties HYDROGEN = gas(ChemicalValues.HYDROGEN,
				() -> FluidInit.HYDROGEN.get(), () -> HYDROGEN_FLOWING.get(), () -> HYDROGEN_BUCKET.get());

		public static final ForgeFlowingFluid.Properties LIQUID_HYDROGEN = liquid(ChemicalValues.LIQUID_HYDROGEN,
				() -> FluidInit.LIQUID_HYDROGEN.get(), () -> LIQUID_HYDROGEN_FLOWING.get(),
				() -> LIQUID_HYDROGEN_BUCKET.get(), () -> LIQUID_HYDROGEN_BLOCK.get());

		private static ForgeFlowingFluid.Properties gas(ChemicalValues value, Supplier<FlowingFluid> still,
				Supplier<FlowingFluid> flowing, Supplier<BucketItem> bucket) {
			return new ForgeFlowingFluid.Properties(still, flowing,
					FluidAttributes.builder(STILL_RL, FLOWING_RL).color(value.getColor())
							.density(Math.round(value.getDensity())).viscosity(Math.round(value.getDensity()))
							.temperature(Math.round(value.getTemperature())).luminosity(value.getLuminosity())
							.overlay(OVERLAY_RL).gaseous()).bucket(bucket);
		}

		private static ForgeFlowingFluid.Properties liquid(ChemicalValues value, Supplier<FlowingFluid> still,
				Supplier<FlowingFluid> flowing, Supplier<BucketItem> bucket, Supplier<FlowingFluidBlock> block) {
			return new ForgeFlowingFluid.Properties(still, flowing,
					FluidAttributes.builder(STILL_RL, FLOWING_RL).color(value.getColor())
							.density(Math.round(value.getDensity())).viscosity(Math.round(value.getDensity()))
							.temperature(Math.round(value.getTemperature())).luminosity(value.getLuminosity())
							.overlay(OVERLAY_RL)).bucket(bucket).block(block);
		}
	}

	public enum ChemicalValues {

		//@formatter:off
		OXYGEN("oxygen", 0xFF_FFFFFF, 0, 90.19F, -1_141),
		HYDROGEN("hydrogen", 0xFF_6CE2FF, 0, 20.28F, -70.85F),
		LIQUID_HYDROGEN("liquid_hydrogen", 0xFF_898FFF, 0, 20.28F, 70.85F);
		//@formatter:on

		private final String name;
		private final int color;
		private final int luminosity;
		private final float temperature;
		private final float density;

		ChemicalValues(String name, int color, int luminosity, float temperature, float density) {
			this.name = name;
			this.color = color;
			this.luminosity = luminosity;
			this.temperature = temperature;
			this.density = density;
		}

		public String getName() {
			return name;
		}

		public int getColor() {
			return color;
		}

		public float getTemperature() {
			return temperature;
		}

		public float getDensity() {
			return density;
		}

		public int getLuminosity() {
			return luminosity;
		}
	}
}
