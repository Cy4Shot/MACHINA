package com.machina.registration.init;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.joml.Vector3f;

import com.google.common.base.Function;
import com.machina.Machina;
import com.machina.api.fluid.BaseFluidType;
import com.machina.api.item.MachinaBucket;
import com.machina.api.util.StringUtils;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FluidInit {
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS,
			Machina.MOD_ID);
	public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister
			.create(ForgeRegistries.Keys.FLUID_TYPES, Machina.MOD_ID);
	public static final List<String> BLOCKS = new ArrayList<>();
	public static final List<FluidObject> OBJS = new ArrayList<>();

	// Gases
	public static final FluidObject OXYGEN = gas("oxygen", "O2", 0xFF_ffffff);
	public static final FluidObject NITROGEN = gas("nitrogen", "N2", 0xFF_b2d4db);
	public static final FluidObject AMMONIA = gas("ammonia", "NH3", 0xFF_44db6c);
	public static final FluidObject CARBON_DIOXIDE = gas("carbon_dioxide", "CO2", 0xFF_f7e4c1);
	public static final FluidObject HYDROGEN = gas("hydrogen", "H2", 0xFF_6ce2ff);
	public static final FluidObject METHANE = gas("methane", "CH4", 0xFF_ece0f9);
	public static final FluidObject ETHANE = gas("ethane", "C2H6", 0xFF_bbd68d);
	public static final FluidObject ETHYLENE = gas("ethylene", "C2H4", 0xFF_8dd6be);
	public static final FluidObject CHLORINE = gas("chlorine", "Cl2", 0xFF_bfd25f);
	public static final FluidObject BORON_TRIFLUORIDE = gas("boron_trifluoride", "BF3", 0xFF_f8f9fa);
	public static final FluidObject FORMALDEHYDE = gas("formaldehyde", "CH2O", 0xFF_f5f5f5);
	public static final FluidObject NITROGEN_DIOXIDE = gas("nitrogen_dioxide", "NO2", 0xFF_560003);
	public static final FluidObject SULPHUR_DIOXIDE = gas("sulphur_dioxide", "SO2", 0xFF_f3f7ef);
	public static final FluidObject HYDROGEN_BROMIDE = gas("hydrogen_bromide", "HBr", 0xFF_e3e3e3);
	public static final FluidObject CARBON_MONOXIDE = gas("carbon_monoxide", "CO", 0xFF_e0b9b6);

	// Liquids
	public static final FluidObject ACETIC_ACID = liquid("acetic_acid", "CH3COOH", 0xFF_fbfff1);
	public static final FluidObject BRINE = liquid("brine", "NaCl", 0xFF_63d1ea);
	public static final FluidObject SULPHUR_TRIOXIDE = liquid("sulphur_trioxide", "SO3", 0xFF_fafafa);
	public static final FluidObject HYDROCHLORIC_ACID = liquid("hydrochloric_acid", "HCl", 0xFF_fafbf8);
	public static final FluidObject SULPHURIC_ACID = liquid("sulphuric_acid", "H2SO4", 0xFF_fcfff8);
	public static final FluidObject BROMINE = liquid("bromine", "Br2", 0xFF_fc6f37);
	public static final FluidObject BENZENE = liquid("benzene", "C6H6", 0xFF_c9cbc6);
	public static final FluidObject TOLUENE = liquid("toluene", "C6H5CH3", 0xFF_d7dadb);
	public static final FluidObject METHANOL = liquid("methanol", "CH3OH", 0xFF_ecefe6);
	public static final FluidObject ETHANOL = liquid("ethanol", "CH3CH2OH", 0xFF_f7fdfb);
	public static final FluidObject HYDROGEN_FLUORIDE = liquid("hydrogen_fluoride", "HF", 0xFF_e6e7eb);
	public static final FluidObject ACETALDEHYDE = liquid("acetaldehyde", "CH3CHO", 0xFF_f2f2f2);
	public static final FluidObject BENZYL_CHLORIDE = liquid("benzyl_chloride", "C6H5CH2Cl", 0xFF_dbdbdc);
	public static final FluidObject NITRIC_ACID = liquid("nitric_acid", "HNO3", 0xFF_ecf0f0);
	public static final FluidObject BROMOBENZENE = liquid("bromobenzene", "C6H5Br", 0xFF_c0bf8d);
	public static final FluidObject GLYOXAL = liquid("glyoxal", "OCHCHO", 0xFF_dad4d6);
	public static final FluidObject BENZYLAMINE = liquid("benzylamine", "C6H5CH2NH2", 0xFF_cacec5);
	public static final FluidObject HNIW = liquid("hniw", "C6H6N12O12", 0xFF_bf433f);
	public static final FluidObject HEXOGEN = liquid("hexogen", "(O2N2CH2)3", 0xFF_eece45);
	public static final FluidObject NITROMETHANE = liquid("nitromethane", "CH3NO2", 0xFF_deddd5);
	public static final FluidObject MOLTEN_LEAD = liquid("molten_lead", "Pb", 0xFF_6c6a71);
	public static final FluidObject MOLTEN_BISMUTH = liquid("molten_bismuth", "Bi", 0xFF_597c7a);
	public static final FluidObject LEAD_BISMUTH_EUTECTIC = liquid("lead_bismuth_eutectic", "Pb/Bi", 0xFF_d39d84);

	public static FluidObject gas(String name, String code, int col) {
		return new FluidObject(name, code, true, b -> b.col(col));
	}

	public static FluidObject liquid(String name, String code, int col) {
		return new FluidObject(name, code, false, b -> b.col(col));
	}

	public static class FluidObject {

		private static final ResourceLocation STILL_RL = new ResourceLocation("block/water_still");
		private static final ResourceLocation FLOWING_RL = new ResourceLocation("block/water_flow");
		private static final ResourceLocation OVERLAY_RL = new ResourceLocation("block/water_overlay");
		private static final Item.Properties BUCKET_PROP = new Item.Properties().stacksTo(1)
				.craftRemainder(Items.BUCKET);
		private static final Block.Properties BLOCK_PROP = Block.Properties.copy(Blocks.WATER);

		private Chemical CHEM;
		private ForgeFlowingFluid.Properties PROPS;
		private RegistryObject<LiquidBlock> BLOCK;
		private RegistryObject<MachinaBucket> BUCKET;
		private RegistryObject<ForgeFlowingFluid> FLUID;
		private RegistryObject<ForgeFlowingFluid> FLOWING;
		private RegistryObject<BaseFluidType> FLUID_TYPE;
		public boolean gas;

		public FluidObject(String name, String code, boolean gas, Function<ChemicalBuilder, ChemicalBuilder> builder) {
			this.gas = gas;

			Supplier<FlowingFluid> sFluid = () -> FLUID.get();
			Supplier<FlowingFluid> sFlowing = () -> FLOWING.get();
			Supplier<MachinaBucket> sBucket = () -> BUCKET.get();
			Supplier<LiquidBlock> sBlock = () -> BLOCK.get();
			Supplier<FluidType> sFluidType = () -> FLUID_TYPE.get();
			CHEM = builder.apply(ChemicalBuilder.init()).build(gas, name);

			FLUID_TYPE = builder(name, CHEM);
			if (gas) {
				PROPS = gas(sFluidType, sFluid, sFlowing, sBucket);
			} else {
				PROPS = liquid(sFluidType, sFluid, sFlowing, sBucket, sBlock);
				BLOCK = BlockInit.register(name + "_block", () -> new LiquidBlock(sFluid, BLOCK_PROP));
				BLOCKS.add(name + "_block");
			}

			BUCKET = ItemInit.register(name + "_bucket", () -> new MachinaBucket(sFluid, BUCKET_PROP, code));
			FLUID = FLUIDS.register(name, () -> new ForgeFlowingFluid.Source(PROPS));
			FLOWING = FLUIDS.register(name + "_flowing", () -> new ForgeFlowingFluid.Flowing(PROPS));

			OBJS.add(FluidObject.this);
		}

		public LiquidBlock block() {
			return BLOCK.get();
		}

		public MachinaBucket bucket() {
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

		private static ForgeFlowingFluid.Properties gas(Supplier<FluidType> type, Supplier<FlowingFluid> still,
				Supplier<FlowingFluid> flowing, Supplier<MachinaBucket> bucket) {
			return new ForgeFlowingFluid.Properties(type, still, flowing).bucket(bucket);
		}

		private static ForgeFlowingFluid.Properties liquid(Supplier<FluidType> type, Supplier<FlowingFluid> still,
				Supplier<FlowingFluid> flowing, Supplier<MachinaBucket> bucket, Supplier<LiquidBlock> block) {
			return new ForgeFlowingFluid.Properties(type, still, flowing).bucket(bucket).block(block);
		}

		public static RegistryObject<BaseFluidType> builder(String name, Chemical value) {
			int r = (value.getColor() >> 16) & 0xFF;
			int g = (value.getColor() >> 8) & 0xFF;
			int b = (value.getColor() >> 0) & 0xFF;
			Vector3f fog = new Vector3f((float) r / 255, (float) g / 255, (float) b / 255);
			FluidType.Properties props = FluidType.Properties.create().density(value.getDensity())
					.temperature(value.getTemperature()).lightLevel(value.getLuminosity());
			return FLUID_TYPES.register(name,
					() -> new BaseFluidType(STILL_RL, FLOWING_RL, OVERLAY_RL, value.getColor(), fog, props));
		}
	}

	public static class ChemicalBuilder {

		private Chemical c;

		private ChemicalBuilder() {
			c = new Chemical("", 0, 0, 0, 1);
		}

		public ChemicalBuilder col(int v) {
			c.color = v;
			return this;
		}

		public ChemicalBuilder lum(int v) {
			c.luminosity = v;
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
		private int temperature;
		private int density;

		public Chemical(String name, int color, int luminosity, int temperature, int density) {
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

		public int getTemperature() {
			return temperature;
		}

		public int getDensity() {
			return density;
		}

		public int getLuminosity() {
			return luminosity;
		}
	}

	public static void setRenderLayers() {
		FLUIDS.getEntries().forEach(ro -> {
			ItemBlockRenderTypes.setRenderLayer(ro.get(), RenderType.translucent());
		});
	}
}
