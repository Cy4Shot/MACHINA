package com.machina.registration.init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.registration.Registration;
import com.machina.util.text.StringUtils;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
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

	// Gases
	public static final FluidObject OXYGEN = gas("oxygen", b -> b.col(0xFF_ffffff).tem(90.19F).den(1143f));
	public static final FluidObject NITROGEN = gas("nitrogen", b -> b.col(0xFF_b2d4db).tem(90.19F).den(1161f));
	public static final FluidObject AMMONIA = gas("ammonia", b -> b.col(0xFF_44db6c).tem(90.19F).den(730f));
	public static final FluidObject CARBON_DIOXIDE = gas("carbon_dioxide", b -> b.col(0xFF_f7e4c1).tem(1870f).den(1143f));
	public static final FluidObject HYDROGEN = gas("hydrogen", b -> b.col(0xFF_6ce2ff).tem(90.19F).den(70.86f));
	
	// Liquids
	public static final FluidObject LIQUID_HYDROGEN = liquid("liquid_hydrogen", b -> b.col(0xFF_898fff).tem(20.28F).den(70.86f));
	public static final FluidObject LIQUID_AMMONIA = liquid("liquid_ammonia", b -> b.col(0xFF_1e6e33).tem(20.28F).den(730f));
	
	// Atmosphere 
	public static final List<FluidObject> ATMOSPHERE = Arrays.asList(OXYGEN, NITROGEN, AMMONIA, CARBON_DIOXIDE, HYDROGEN);
	public static final Double[] ATM_DEFAULT = new Double[] {0.20946D, 0.78084D, 0D, 0.00417D, 0D};

	//@formatter:on

	public static FluidObject gas(String name, Function<ChemicalBuilder, ChemicalBuilder> builder) {
		return new FluidObject(name, true, builder);
	}

	public static FluidObject liquid(String name, Function<ChemicalBuilder, ChemicalBuilder> builder) {
		return new FluidObject(name, false, builder);
	}

	public static class FluidObject {

		private static final ResourceLocation STILL_RL = new ResourceLocation("block/water_still");
		private static final ResourceLocation FLOWING_RL = new ResourceLocation("block/water_flow");
		private static final ResourceLocation OVERLAY_RL = new ResourceLocation("block/water_overlay");
		private static final Item.Properties BUCKET_PROP = new Item.Properties().tab(Registration.MACHINA_ITEM_GROUP)
				.stacksTo(1).craftRemainder(Items.BUCKET);
		private static final AbstractBlock.Properties BLOCK_PROP = AbstractBlock.Properties.copy(Blocks.WATER);

		private Chemical CHEM;
		private ForgeFlowingFluid.Properties PROPS;
		private RegistryObject<FlowingFluidBlock> BLOCK;
		private RegistryObject<BucketItem> BUCKET;
		private RegistryObject<ForgeFlowingFluid> FLUID;
		private RegistryObject<ForgeFlowingFluid> FLOWING;

		public FluidObject(String name, boolean gas, Function<ChemicalBuilder, ChemicalBuilder> builder) {

			Supplier<FlowingFluid> sFluid = () -> FLUID.get();
			Supplier<FlowingFluid> sFlowing = () -> FLOWING.get();
			Supplier<BucketItem> sBucket = () -> BUCKET.get();
			Supplier<FlowingFluidBlock> sBlock = () -> BLOCK.get();
			CHEM = builder.apply(ChemicalBuilder.init()).build(gas, name);

			if (gas) {
				PROPS = gas(CHEM, sFluid, sFlowing, sBucket);
			} else {
				PROPS = liquid(CHEM, sFluid, sFlowing, sBucket, sBlock);
				BLOCK = BlockInit.register(name + "_block", () -> new FlowingFluidBlock(sFluid, BLOCK_PROP));
				BLOCKS.add(name);
			}

			BUCKET = ItemInit.register(name + "_bucket", () -> new BucketItem(sFluid, BUCKET_PROP));
			FLUID = FLUIDS.register(name, () -> new ForgeFlowingFluid.Source(PROPS));
			FLOWING = FLUIDS.register(name + "_flowing", () -> new ForgeFlowingFluid.Flowing(PROPS));
		}

		public FlowingFluidBlock block() {
			return BLOCK.get();
		}
		
		public BucketItem bucket() {
			return BUCKET.get();
		}
		
		public ForgeFlowingFluid fluid() {
			return FLUID.get();
		}
		
		public ForgeFlowingFluid flowing() {
			return FLOWING.get();
		}
		
		public Chemical chem() {
			return CHEM;
		}

		private static ForgeFlowingFluid.Properties gas(Chemical value, Supplier<FlowingFluid> still,
				Supplier<FlowingFluid> flowing, Supplier<BucketItem> bucket) {
			return new ForgeFlowingFluid.Properties(still, flowing, builder(value).gaseous()).bucket(bucket);
		}

		private static ForgeFlowingFluid.Properties liquid(Chemical value, Supplier<FlowingFluid> still,
				Supplier<FlowingFluid> flowing, Supplier<BucketItem> bucket, Supplier<FlowingFluidBlock> block) {
			return new ForgeFlowingFluid.Properties(still, flowing, builder(value)).bucket(bucket).block(block);
		}

		private static FluidAttributes.Builder builder(Chemical value) {
			return FluidAttributes.builder(STILL_RL, FLOWING_RL).color(value.getColor())
					.density(Math.round(value.getDensity())).viscosity(Math.round(value.getDensity()))
					.temperature(Math.round(value.getTemperature())).luminosity(value.getLuminosity())
					.overlay(OVERLAY_RL);
		}
	}

	public static class ChemicalBuilder {

		private Chemical c;

		private ChemicalBuilder() {
			c = new Chemical("", 0, 0, 50F, 1);
		}

		public ChemicalBuilder col(int v) {
			c.color = v;
			return this;
		}

		public ChemicalBuilder lum(int v) {
			c.luminosity = v;
			return this;
		}

		public ChemicalBuilder den(float v) {
			c.density = v;
			return this;
		}

		public ChemicalBuilder tem(float v) {
			c.temperature = v;
			return this;
		}

		public Chemical build(boolean gas, String name) {
			c.name = name;
			if (gas)
				c.density *= -1;
			return c;
		}

		public static ChemicalBuilder init() {
			return new ChemicalBuilder();
		}
	}

	public static class Chemical {
		private String name;
		private int color;
		private int luminosity;
		private float temperature;
		private float density;

		public Chemical(String name, int color, int luminosity, float temperature, float density) {
			this.name = name;
			this.color = color;
			this.luminosity = luminosity;
			this.temperature = temperature;
			this.density = density;
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
	}

	public static void setRenderLayers() {
		FLUIDS.getEntries().forEach(ro -> {
			RenderTypeLookup.setRenderLayer(ro.get(), RenderType.translucent());
		});
	}
}
