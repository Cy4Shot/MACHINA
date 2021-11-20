package com.cy4.machina.init;

import static com.cy4.machina.api.util.MachinaRegistryObject.fluid;

import java.util.function.Supplier;

import com.cy4.machina.Machina;
import com.cy4.machina.api.ChemicalValues;
import com.cy4.machina.api.annotation.registries.RegisterBlock;
import com.cy4.machina.api.annotation.registries.RegisterFluid;
import com.cy4.machina.api.annotation.registries.RegisterItem;
import com.cy4.machina.api.annotation.registries.RegistryHolder;
import com.cy4.machina.api.util.MachinaRegistryObject;
import com.cy4.machina.block.fluid.LiquidHydrogenBlock;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

/**
 * @deprecated
 * The owner of the mod doesnt like the idea of a fluid being used as fuel so he can 
 * get rid of it
 * @author matyrobbrt
 *
 */
@Deprecated
@RegistryHolder
public final class FluidInit {

	public static final ResourceLocation STILL_RL = new ResourceLocation("block/water_still");
	public static final ResourceLocation FLOWING_RL = new ResourceLocation("block/water_flow");
	public static final ResourceLocation OVERLAY_RL = new ResourceLocation("block/water_overlay");

	//@formatter:off
	@RegisterFluid("oxygen")
	public static final MachinaRegistryObject<FlowingFluid> OXYGEN = fluid(() -> new ForgeFlowingFluid.Source(Properties.OXYGEN_PROPERTIES));
	@RegisterFluid("oxygen_flowing")
	public static final MachinaRegistryObject<FlowingFluid> OXYGEN_FLOWING = fluid(() -> new ForgeFlowingFluid.Flowing(Properties.OXYGEN_PROPERTIES));

	@RegisterFluid("hydrogen")
	public static final MachinaRegistryObject<FlowingFluid> HYDROGEN = fluid(() -> new ForgeFlowingFluid.Source(Properties.HYDROGEN_PROPERTIES));
	@RegisterFluid("hydrogen_flowing")
	public static final MachinaRegistryObject<FlowingFluid> HYDROGEN_FLOWING = fluid(() -> new ForgeFlowingFluid.Flowing(Properties.HYDROGEN_PROPERTIES));

	@RegisterFluid("liquid_hydrogen")
	public static final MachinaRegistryObject<FlowingFluid> LIQUID_HYDROGEN = fluid(() -> new ForgeFlowingFluid.Source(Properties.LIQUID_HYDROGEN_PROPERTIES));
	@RegisterFluid("liquid_hydrogen_flowing")
	public static final MachinaRegistryObject<FlowingFluid> LIQUID_HYDROGEN_FLOWING = fluid(() -> new ForgeFlowingFluid.Flowing(Properties.LIQUID_HYDROGEN_PROPERTIES));
	@RegisterBlock("liquid_hydrogen")
	public static final FlowingFluidBlock LIQUID_HYDROGEN_BLOCK = new LiquidHydrogenBlock(LIQUID_HYDROGEN::get, AbstractBlock.Properties.copy(Blocks.WATER));

	//@formatter:on
	public static class Properties {

		public static final ForgeFlowingFluid.Properties OXYGEN_PROPERTIES = registerChemicalGas(ChemicalValues.OXYGEN,
				OXYGEN::get, OXYGEN_FLOWING::get, OXYGEN_BUCKET);

		public static final ForgeFlowingFluid.Properties HYDROGEN_PROPERTIES = registerChemicalGas(
				ChemicalValues.HYDROGEN, HYDROGEN::get, HYDROGEN_FLOWING::get, HYDROGEN_BUCKET);

		public static final ForgeFlowingFluid.Properties LIQUID_HYDROGEN_PROPERTIES = registerChemicalLiquid(
				ChemicalValues.LIQUID_HYDROGEN, LIQUID_HYDROGEN::get, LIQUID_HYDROGEN_FLOWING::get,
				LIQUID_HYDROGEN_BUCKET, LIQUID_HYDROGEN_BLOCK);

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
