package com.machina.registration.init;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.registration.Registration;
import com.machina.util.text.StringUtils;

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
	public static final RegistryObject<FlowingFluidBlock> LIQUID_AMMONIA_BLOCK = block("liquid_ammonia_block", () -> FluidInit.LIQUID_AMMONIA.get());
	
	// Items
	public static final RegistryObject<BucketItem> OXYGEN_BUCKET = bucket("oxygen_bucket", () -> FluidInit.OXYGEN.get());
	public static final RegistryObject<BucketItem> NITROGEN_BUCKET = bucket("nitrogen_bucket", () -> FluidInit.NITROGEN.get());
	public static final RegistryObject<BucketItem> AMMONIA_BUCKET = bucket("ammonia_bucket", () -> FluidInit.AMMONIA.get());
	public static final RegistryObject<BucketItem> CARBON_DIOXIDE_BUCKET = bucket("carbon_dioxide_bucket", () -> FluidInit.CARBON_DIOXIDE.get());
	public static final RegistryObject<BucketItem> HYDROGEN_BUCKET = bucket("hydrogen_bucket", () -> FluidInit.HYDROGEN.get());
	public static final RegistryObject<BucketItem> LIQUID_HYDROGEN_BUCKET = bucket("liquid_hydrogen_bucket", () -> FluidInit.LIQUID_HYDROGEN.get());
	public static final RegistryObject<BucketItem> LIQUID_AMMONIA_BUCKET = bucket("liquid_ammonia_bucket", () -> FluidInit.LIQUID_AMMONIA.get());
	
	// Fluids
	public static final RegistryObject<ForgeFlowingFluid> OXYGEN = register("oxygen", Prop.OXYGEN, false);
	public static final RegistryObject<ForgeFlowingFluid> OXYGEN_FLOWING = register("oxygen", Prop.OXYGEN, true);
	public static final RegistryObject<ForgeFlowingFluid> NITROGEN = register("nitrogen", Prop.NITROGEN, false);
	public static final RegistryObject<ForgeFlowingFluid> NITROGEN_FLOWING = register("nitrogen", Prop.NITROGEN, true);
	public static final RegistryObject<ForgeFlowingFluid> AMMONIA = register("ammonia", Prop.AMMONIA, false);
	public static final RegistryObject<ForgeFlowingFluid> AMMONIA_FLOWING = register("ammonia", Prop.AMMONIA, true);
	public static final RegistryObject<ForgeFlowingFluid> CARBON_DIOXIDE = register("carbon_dioxide", Prop.CARBON_DIOXIDE, false);
	public static final RegistryObject<ForgeFlowingFluid> CARBON_DIOXIDE_FLOWING = register("carbon_dioxide", Prop.CARBON_DIOXIDE, true);
	public static final RegistryObject<ForgeFlowingFluid> HYDROGEN = register("hydrogen", Prop.HYDROGEN, false);
	public static final RegistryObject<ForgeFlowingFluid> HYDROGEN_FLOWING = register("hydrogen", Prop.HYDROGEN, true);
	public static final RegistryObject<ForgeFlowingFluid> LIQUID_HYDROGEN = register("liquid_hydrogen", Prop.LIQUID_HYDROGEN, false);
	public static final RegistryObject<ForgeFlowingFluid> LIQUID_HYDROGEN_FLOWING = register("liquid_hydrogen", Prop.LIQUID_HYDROGEN, true);
	public static final RegistryObject<ForgeFlowingFluid> LIQUID_AMMONIA = register("liquid_ammonia", Prop.LIQUID_AMMONIA, false);
	public static final RegistryObject<ForgeFlowingFluid> LIQUID_AMMONIA_FLOWING = register("liquid_ammonia", Prop.LIQUID_AMMONIA, true);

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
		
		public static final ForgeFlowingFluid.Properties NITROGEN = gas(ChemicalValues.NITROGEN,
				() -> FluidInit.NITROGEN.get(), () -> NITROGEN_FLOWING.get(), () -> NITROGEN_BUCKET.get());
		
		public static final ForgeFlowingFluid.Properties AMMONIA = gas(ChemicalValues.AMMONIA,
				() -> FluidInit.AMMONIA.get(), () -> AMMONIA_FLOWING.get(), () -> AMMONIA_BUCKET.get());
		
		public static final ForgeFlowingFluid.Properties CARBON_DIOXIDE = gas(ChemicalValues.CARBON_DIOXIDE,
				() -> FluidInit.CARBON_DIOXIDE.get(), () -> CARBON_DIOXIDE_FLOWING.get(), () -> CARBON_DIOXIDE_BUCKET.get());

		public static final ForgeFlowingFluid.Properties HYDROGEN = gas(ChemicalValues.HYDROGEN,
				() -> FluidInit.HYDROGEN.get(), () -> HYDROGEN_FLOWING.get(), () -> HYDROGEN_BUCKET.get());

		public static final ForgeFlowingFluid.Properties LIQUID_HYDROGEN = liquid(ChemicalValues.LIQUID_HYDROGEN,
				() -> FluidInit.LIQUID_HYDROGEN.get(), () -> LIQUID_HYDROGEN_FLOWING.get(),
				() -> LIQUID_HYDROGEN_BUCKET.get(), () -> LIQUID_HYDROGEN_BLOCK.get());
		
		public static final ForgeFlowingFluid.Properties LIQUID_AMMONIA = liquid(ChemicalValues.LIQUID_AMMONIA,
				() -> FluidInit.LIQUID_AMMONIA.get(), () -> LIQUID_AMMONIA_FLOWING.get(),
				() -> LIQUID_AMMONIA_BUCKET.get(), () -> LIQUID_AMMONIA_BLOCK.get());

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
		OXYGEN("oxygen", 0xFF_FFFFFF, 0, 90.19F, -1_143, 0),
		NITROGEN("nitrogen", 0xFF_6be5fa, 0, 90.19F, -1_161, 1),
		AMMONIA("ammonia", 0xFF_44db6c, 0, 90.19F, -0_730, 2),
		CARBON_DIOXIDE("carbon_dioxide", 0xFF_f7e4c1, 0, 90.19F, -1_870, 3),
		HYDROGEN("hydrogen", 0xFF_6CE2FF, 0, 90.19F, -70.85F, 4),
		LIQUID_HYDROGEN("liquid_hydrogen", 0xFF_898FFF, 0, 20.28F, 70.85F, -1),
		LIQUID_AMMONIA("liquid_ammonia", 0xFF_1e6e33, 0, 20.28F, 0_730, -1);
		//@formatter:on

		private final String name;
		private final int color;
		private final int luminosity;
		private final float temperature;
		private final float density;
		private final int atmosphere;

		ChemicalValues(String name, int color, int luminosity, float temperature, float density, int atmosphere) {
			this.name = name;
			this.color = color;
			this.luminosity = luminosity;
			this.temperature = temperature;
			this.density = density;
			this.atmosphere = atmosphere;
		}

		public String getName() {
			return name;
		}
		
		public String getDisplayName() {
			return StringUtils.translate("fluid." + Machina.MOD_ID + "." + name);
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
		
		public int getAtmosphereId() {
			return atmosphere;
		}
	}
}
