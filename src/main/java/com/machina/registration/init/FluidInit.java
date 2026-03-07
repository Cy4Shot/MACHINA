package com.machina.registration.init;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.google.common.base.Function;
import com.machina.Machina;
import com.machina.api.fluid.FluidPhase;
import com.machina.api.item.MachinaBucket;
import com.machina.api.natives.CoolpropJNA;
import com.machina.api.util.StringUtils;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class FluidInit {
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Machina.MOD_ID);
	public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister
			.create(NeoForgeRegistries.Keys.FLUID_TYPES, Machina.MOD_ID);
	public static final List<String> BLOCKS = new ArrayList<>();
	public static final List<FluidObject> OBJS = new ArrayList<>();

	public static final FluidObject MOLTEN_IRON = create("molten_iron", "Fe", 0xFF_828282);
	public static final FluidObject MOLTEN_GOLD = create("molten_gold", "Au", 0xFF_e9b115);
	public static final FluidObject MOLTEN_COPPER = create("molten_copper", "Cu", 0xFF_c15a36);
	public static final FluidObject MOLTEN_ALUMINUM = create("molten_aluminum", "Al", 0xFF_a4b1c7);
	public static final FluidObject MOLTEN_NICKEL = create("molten_nickel", "Ni", 0xFF_cfd0a1);
	public static final FluidObject MOLTEN_LEAD = create("molten_lead", "Pb", 0xFF_686883);
	public static final FluidObject MOLTEN_BORON = create("molten_boron", "B", 0xFF_353642);
	public static final FluidObject MOLTEN_PALLADIUM = create("molten_palladium", "Pd", 0xFF_b6c1b5);
	public static final FluidObject MOLTEN_SILVER = create("molten_silver", "Ag", 0xFF_cabfd2);
	public static final FluidObject MOLTEN_STEEL = create("molten_steel", "Fe+C", 0xFF_2c2c2c);
	public static final FluidObject MOLTEN_CONSTANTAN = create("molten_constantan", "Cu+Ni", 0xFF_c67600);
	public static final FluidObject MOLTEN_BISMUTH = create("molten_bismuth", "Bi", 0xFF_597c7a);
	public static final FluidObject MOLTEN_TIN = create("molten_tin", "Sn", 0xFF_d5d2cd);
	public static final FluidObject MOLTEN_ZINC = create("molten_zinc", "Zn", 0xFF_bbc3b8);
	public static final FluidObject MOLTEN_LOW_GRADE_TITANIUM = create("molten_low_grade_titanium", "Ti", 0xFF_948dbe);
	public static final FluidObject MOLTEN_MAGNETITE = create("molten_magnetite", "Fe3O4", 0xFF_9a8f8f);
	public static final FluidObject MOLTEN_GYPSUM = create("molten_gypsum", "CaSO4", 0xFF_d7cc95);
	public static final FluidObject MOLTEN_ILMENITE = create("molten_ilmenite", "FeTiO3", 0xFF_b5936a);
	public static final FluidObject MOLTEN_PLATINUM = create("molten_platinum", "Pt", 0xFF_9de0fa);
	public static final FluidObject MOLTEN_IRIDIUM = create("molten_iridium", "Ir", 0xFF_a1a09e);
	public static final FluidObject MOLTEN_OSMIUM = create("molten_osmium", "Os", 0xFF_8eb0c9);
	public static final FluidObject MOLTEN_COBALT = create("molten_cobalt", "Co", 0xFF_617aeb);
	public static final FluidObject MOLTEN_URANINITE = create("molten_uraninite", "UO2", 0xFF_62bb54);
	public static final FluidObject MOLTEN_THORIUM = create("molten_thorium", "ThO2", 0xFF_d3989c);

	public static final FluidObject OXYGEN = create("oxygen", "O2", 0xFF_ffffff, "Oxygen");
	public static final FluidObject NITROGEN = create("nitrogen", "N2", 0xFF_b2d4db, "Nitrogen");
	public static final FluidObject AMMONIA = create("ammonia", "NH3", 0xFF_44db6c, "Ammonia");
	public static final FluidObject CARBON_DIOXIDE = create("carbon_dioxide", "CO2", 0xFF_f7e4c1, "CarbonDioxide");
	public static final FluidObject CARBON_DISULPHIDE = create("carbon_disulphide", "CS2", 0xFF_f2d891);
	public static final FluidObject HYDROGEN = create("hydrogen", "H2", 0xFF_6ce2ff, "Hydrogen");
	public static final FluidObject METHANE = create("methane", "CH4", 0xFF_ece0f9, "Methane");
	public static final FluidObject ETHANE = create("ethane", "C2H6", 0xFF_bbd68d, "Ethane");
	public static final FluidObject ETHYLENE = create("ethylene", "C2H4", 0xFF_8dd6be, "Ethylene");
	public static final FluidObject CHLORINE = create("chlorine", "Cl2", 0xFF_bfd25f);
	public static final FluidObject BORON_TRIFLUORIDE = create("boron_trifluoride", "BF3", 0xFF_f8f9fa);
	public static final FluidObject FORMALDEHYDE = create("formaldehyde", "CH2O", 0xFF_f5f5f5);
	public static final FluidObject NITROGEN_DIOXIDE = create("nitrogen_dioxide", "NO2", 0xFF_560003);
	public static final FluidObject SULPHUR_DIOXIDE = create("sulphur_dioxide", "SO2", 0xFF_f3f7ef, "SulfurDioxide");
	public static final FluidObject HYDROGEN_BROMIDE = create("hydrogen_bromide", "HBr", 0xFF_e3e3e3);
	public static final FluidObject HYDROGEN_SULPHIDE = create("hydrogen_sulphide", "H2S", 0xFF_acaeb5,
			"HydrogenSulfide");
	public static final FluidObject CARBON_MONOXIDE = create("carbon_monoxide", "CO", 0xFF_e0b9b6, "CarbonMonoxide");
	public static final FluidObject ARGON = create("argon", "Ar", 0xFF_cfcfd0, "Argon");
	public static final FluidObject HELIUM = create("helium", "He", 0xFF_e1e1e1, "Helium");
	public static final FluidObject ACETIC_ACID = create("acetic_acid", "CH3COOH", 0xFF_fbfff1);
	public static final FluidObject BRINE = create("brine", "NaCl", 0xFF_63d1ea);
	public static final FluidObject SULPHUR_TRIOXIDE = create("sulphur_trioxide", "SO3", 0xFF_fafafa);
	public static final FluidObject HYDROCHLORIC_ACID = create("hydrochloric_acid", "HCl", 0xFF_fafbf8);
	public static final FluidObject SULPHURIC_ACID = create("sulphuric_acid", "H2SO4", 0xFF_fcfff8);
	public static final FluidObject BROMINE = create("bromine", "Br2", 0xFF_fc6f37);
	public static final FluidObject BENZENE = create("benzene", "C6H6", 0xFF_c9cbc6, "Benzene");
	public static final FluidObject TOLUENE = create("toluene", "C6H5CH3", 0xFF_d7dadb);
	public static final FluidObject METHANOL = create("methanol", "CH3OH", 0xFF_ecefe6, "Methanol");
	public static final FluidObject ETHANOL = create("ethanol", "CH3CH2OH", 0xFF_f7fdfb, "Ethanol");
	public static final FluidObject HYDROGEN_FLUORIDE = create("hydrogen_fluoride", "HF", 0xFF_e6e7eb);
	public static final FluidObject ACETALDEHYDE = create("acetaldehyde", "CH3CHO", 0xFF_f2f2f2);
	public static final FluidObject BENZYL_CHLORIDE = create("benzyl_chloride", "C6H5CH2Cl", 0xFF_dbdbdc);
	public static final FluidObject NITRIC_ACID = create("nitric_acid", "HNO3", 0xFF_ecf0f0);
	public static final FluidObject BROMOBENZENE = create("bromobenzene", "C6H5Br", 0xFF_c0bf8d);
	public static final FluidObject GLYOXAL = create("glyoxal", "OCHCHO", 0xFF_dad4d6);
	public static final FluidObject BENZYLAMINE = create("benzylamine", "C6H5CH2NH2", 0xFF_cacec5);
	public static final FluidObject HNIW = create("hniw", "C6H6N12O12", 0xFF_bf433f);
	public static final FluidObject HEXOGEN = create("hexogen", "(O2N2CH2)3", 0xFF_eece45);
	public static final FluidObject NITROMETHANE = create("nitromethane", "CH3NO2", 0xFF_deddd5);
	public static final FluidObject LEAD_BISMUTH_EUTECTIC = create("lead_bismuth_eutectic", "Pb/Bi", 0xFF_d39d84);

	public static FluidObject create(String name, String code, int col) {
		return new FluidObject(name, code, b -> b.col(col));
	}

	public static FluidObject create(String name, String code, int col, String coolprops) {
		return new FluidObject(name, code, b -> b.col(col).coolprops(coolprops));
	}

	public static class FluidObject {

		public static final FluidObject WATER = new FluidObject(Fluids.WATER, "water", c -> c);

		private static final Item.Properties BUCKET_PROP = new Item.Properties().stacksTo(1)
				.craftRemainder(Items.BUCKET);
		private static final Block.Properties BLOCK_PROP = Block.Properties.ofFullCopy(Blocks.WATER);

		private final String name;
		private final Chemical CHEM;
		private BaseFlowingFluid.Properties PROPS;
		private DeferredBlock<LiquidBlock> BLOCK;
		private DeferredItem<MachinaBucket> BUCKET;
		private Supplier<FlowingFluid> FLUID;
		private Supplier<BaseFlowingFluid> FLOWING;
		private Supplier<FluidType> FLUID_TYPE;

		// Vanilla constructor
		public FluidObject(FlowingFluid fluid, String name, Function<ChemicalBuilder, ChemicalBuilder> builder) {
			this.name = name;

			CHEM = builder.apply(ChemicalBuilder.init()).build(name);
			FLUID = () -> fluid;
		}

		// Machina constructor
		public FluidObject(String name, String code, Function<ChemicalBuilder, ChemicalBuilder> builder) {
			this.name = name;

			Supplier<FlowingFluid> sFluid = () -> FLUID.get();
			Supplier<FlowingFluid> sFlowing = () -> FLOWING.get();
			Supplier<MachinaBucket> sBucket = () -> BUCKET.get();
			Supplier<LiquidBlock> sBlock = () -> BLOCK.get();
			Supplier<FluidType> sFluidType = () -> FLUID_TYPE.get();
			CHEM = builder.apply(ChemicalBuilder.init()).build(name);

			FLUID_TYPE = builder(name, CHEM);
			PROPS = make(sFluidType, sFluid, sFlowing, sBucket, sBlock);
			BLOCK = BlockInit.register(name + "_block", () -> new LiquidBlock(sFluid.get(), BLOCK_PROP));
			BLOCKS.add(name + "_block");

			BUCKET = ItemInit.register(name + "_bucket", () -> new MachinaBucket(sFluid.get(), BUCKET_PROP, code));
			FLUID = FLUIDS.register(name, () -> new BaseFlowingFluid.Source(PROPS));
			FLOWING = FLUIDS.register("flowing_" + name, () -> new BaseFlowingFluid.Flowing(PROPS));

			OBJS.add(FluidObject.this);
		}

		public String name() {
			return name;
		}

		public LiquidBlock block() {
			return BLOCK.get();
		}

		public MachinaBucket bucket() {
			return BUCKET.get();
		}

		public FlowingFluid fluid() {
			return FLUID.get();
		}

		public BaseFlowingFluid flowing() {
			return FLOWING.get();
		}

		public FluidStack stack() {
			return new FluidStack(fluid(), 1000);
		}

		public Chemical chem() {
			return CHEM;
		}

		public FluidType type() {
			return FLUID_TYPE.get();
		}

		private static BaseFlowingFluid.Properties make(Supplier<FluidType> type, Supplier<FlowingFluid> still,
				Supplier<FlowingFluid> flowing, Supplier<MachinaBucket> bucket, Supplier<LiquidBlock> block) {
			return new BaseFlowingFluid.Properties(type, still, flowing).bucket(bucket).block(block);
		}

		public static Supplier<FluidType> builder(String name, Chemical value) {
			FluidType.Properties props = FluidType.Properties.create().density(value.getDensity()).temperature(0)
					.lightLevel(value.getLuminosity());
			return FLUID_TYPES.register(name, () -> new FluidType(props));
		}
	}

	public static class ChemicalBuilder {

		private final Chemical c;

		private ChemicalBuilder() {
			c = new Chemical("", 0, 0, 1, null);
		}

		public ChemicalBuilder col(int v) {
			c.color = v;
			return this;
		}

		public ChemicalBuilder lum(int v) {
			c.luminosity = v;
			return this;
		}

		public ChemicalBuilder coolprops(String coolprops) {
			c.coolprops = coolprops;
			return this;
		}

		public Chemical build(String name) {
			c.name = name;
			return c;
		}

		public static ChemicalBuilder init() {
			return new ChemicalBuilder();
		}
	}

	public static class Chemical {

		public static final Chemical WATER = new Chemical("Water", 0x0000FF, 0, 1, "Water");

		private String name;
		private int color;
		private int luminosity;
		private final int density;
		private String coolprops;

		public Chemical(String name, int color, int luminosity, int density, String coolprops) {
			this.name = name;
			this.color = color;
			this.luminosity = luminosity;
			this.density = density;
			this.coolprops = coolprops;
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

		public int getDensity() {
			return density;
		}

		public int getLuminosity() {
			return luminosity;
		}

		public String getCoolpropsID() {
			return this.coolprops;
		}

		public FluidPhase getPhase(double T, double P) {
			if (this.coolprops == null) {
				return FluidPhase.UNDEFINED;
			}

			String phaseStr;
			try {
				phaseStr = CoolpropJNA.PhaseSI("T", T, "P", P, this.coolprops);
			} catch (Exception e) {
				return FluidPhase.UNDEFINED;
			}

			// We abstract things a bittle bit here :)
			switch (phaseStr.toLowerCase()) {
			case "liquid":
				return FluidPhase.LIQUID;
			case "gas":
				return FluidPhase.VAPOR;
			case "two_phase":
				return FluidPhase.LIQUID;
			case "supercritical":
				return FluidPhase.SUPERCRITICAL;
			case "supercritical_liquid":
				return FluidPhase.LIQUID;
			case "supercritical_gas":
				return FluidPhase.VAPOR;
			case "solid":
				return FluidPhase.SOLID;
			default:
				return FluidPhase.UNDEFINED;
			}
		}
	}

	public static void setRenderLayers() {
		FLUIDS.getEntries().forEach(ro -> ItemBlockRenderTypes.setRenderLayer(ro.get(), RenderType.translucent()));
	}
}
