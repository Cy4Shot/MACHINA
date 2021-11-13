package com.cy4.machina.init;

import java.util.function.Supplier;

import com.cy4.machina.Machina;
import com.cy4.machina.api.ChemicalValues;
import com.cy4.machina.api.annotation.registries.RegisterBlock;
import com.cy4.machina.api.annotation.registries.RegisterItem;
import com.cy4.machina.api.annotation.registries.RegistryHolder;
import com.cy4.machina.block.fluid.LiquidHydrogenBlock;

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

@RegistryHolder
public class FluidInit {

	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS,
			Machina.MOD_ID);

	public static final ResourceLocation STILL_RL = new ResourceLocation("block/water_still");
	public static final ResourceLocation FLOWING_RL = new ResourceLocation("block/water_flow");
	public static final ResourceLocation OVERLAY_RL = new ResourceLocation("block/water_overlay");

	public static final RegistryObject<FlowingFluid> OXYGEN = FLUIDS.register("oxygen",
			() -> new ForgeFlowingFluid.Source(Properties.OXYGEN_PROPERTIES));
	public static final RegistryObject<FlowingFluid> OXYGEN_FLOWING = FLUIDS.register("oxygen_flowing",
			() -> new ForgeFlowingFluid.Flowing(Properties.OXYGEN_PROPERTIES));

	public static final RegistryObject<FlowingFluid> HYDROGEN = FLUIDS.register("hydrogen",
			() -> new ForgeFlowingFluid.Source(Properties.HYDROGEN_PROPERTIES));
	public static final RegistryObject<FlowingFluid> HYDROGEN_FLOWING = FLUIDS.register("hydrogen_flowing",
			() -> new ForgeFlowingFluid.Flowing(Properties.HYDROGEN_PROPERTIES));

	public static final RegistryObject<FlowingFluid> LIQUID_HYDROGEN = FLUIDS.register("liquid_hydrogen",
			() -> new ForgeFlowingFluid.Source(Properties.LIQUID_HYDROGEN_PROPERTIES));
	public static final RegistryObject<FlowingFluid> LIQUID_HYDROGEN_FLOWING = FLUIDS.register("liquid_hydrogen_flowing",
			() -> new ForgeFlowingFluid.Flowing(Properties.LIQUID_HYDROGEN_PROPERTIES));
	@RegisterBlock("liquid_hydrogen")
	public static final FlowingFluidBlock LIQUID_HYDROGEN_BLOCK = new LiquidHydrogenBlock(LIQUID_HYDROGEN::get, AbstractBlock.Properties.copy(Blocks.WATER));

	public static class Properties {

		public static final ForgeFlowingFluid.Properties OXYGEN_PROPERTIES = registerChemicalGas(ChemicalValues.OXYGEN,
				OXYGEN::get, OXYGEN_FLOWING::get, OXYGEN_BUCKET);

		public static final ForgeFlowingFluid.Properties HYDROGEN_PROPERTIES = registerChemicalGas(ChemicalValues.HYDROGEN,
				HYDROGEN::get, HYDROGEN_FLOWING::get, HYDROGEN_BUCKET);

		public static final ForgeFlowingFluid.Properties LIQUID_HYDROGEN_PROPERTIES = registerChemicalLiquid(ChemicalValues.LIQUID_HYDROGEN,
				LIQUID_HYDROGEN::get, LIQUID_HYDROGEN_FLOWING::get, LIQUID_HYDROGEN_BUCKET, LIQUID_HYDROGEN_BLOCK);

		private static ForgeFlowingFluid.Properties registerChemicalGas(ChemicalValues value,
				Supplier<FlowingFluid> still, Supplier<FlowingFluid> flowing, Item bucket) {
			return new ForgeFlowingFluid.Properties(still, flowing,
					FluidAttributes.builder(STILL_RL, FLOWING_RL).color(value.getColor())
					.density(Math.round(value.getDensity())).viscosity(Math.round(value.getDensity()))
					.temperature(Math.round(value.getTemperature())).luminosity(value.getLuminosity())
					.overlay(OVERLAY_RL).gaseous()).bucket(() -> bucket);
		}

		private static ForgeFlowingFluid.Properties registerChemicalLiquid(ChemicalValues value,
				Supplier<FlowingFluid> still, Supplier<FlowingFluid> flowing, Item bucket, FlowingFluidBlock block) {
			return new ForgeFlowingFluid.Properties(still, flowing,
					FluidAttributes.builder(STILL_RL, FLOWING_RL).color(value.getColor())
					.density(Math.round(value.getDensity())).viscosity(Math.round(value.getDensity()))
					.temperature(Math.round(value.getTemperature())).luminosity(value.getLuminosity())
					.overlay(OVERLAY_RL)).bucket(() -> bucket).block(() -> block);
		}

	}

	@RegisterItem("oxygen_bucket")
	public static final BucketItem OXYGEN_BUCKET = createBucket(OXYGEN::get);
	@RegisterItem("hydrogen_bucket")
	public static final BucketItem HYDROGEN_BUCKET = createBucket(HYDROGEN::get);
	@RegisterItem("liquid_hydrogen_bucket")
	public static final BucketItem LIQUID_HYDROGEN_BUCKET = createBucket(LIQUID_HYDROGEN::get);

	private static BucketItem createBucket(Supplier<FlowingFluid> fluid) {
		return new BucketItem(fluid,
				new Item.Properties().tab(Machina.MACHINA_ITEM_GROUP).stacksTo(1).craftRemainder(Items.BUCKET));
	}

}
