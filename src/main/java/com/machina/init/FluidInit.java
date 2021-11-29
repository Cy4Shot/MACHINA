/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.machina.init;

import static com.machina.api.util.MachinaRegistryObject.fluid;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.api.ChemicalValues;
import com.machina.api.registry.annotation.RegisterBlock;
import com.machina.api.registry.annotation.RegisterFluid;
import com.machina.api.registry.annotation.RegisterItem;
import com.machina.api.registry.annotation.RegistryHolder;
import com.machina.api.util.MachinaRegistryObject;
import com.machina.block.fluid.LiquidHydrogenBlock;

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
