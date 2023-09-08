package com.machina.datagen.client.lang;

import com.machina.Machina;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.TabInit;

import net.minecraft.data.PackOutput;

public class DatagenLangEnUs extends DatagenLang {

	public DatagenLangEnUs(PackOutput gen) {
		super(gen, "en_us", Machina.MOD_ID);
	}

	@Override
	protected void addTranslations() {
		// Creative Tabs
		add(TabInit.MACHINA_RESOURCES, "Machina");
		
		// Items
		add(ItemInit.RAW_ALUMINUM.get(), "Raw Aluminum");
		add(ItemInit.ALUMINUM_INGOT.get(), "Aluminum Ingot");
		add(ItemInit.ALUMINUM_NUGGET.get(), "Aluminum Nugget");
		add(ItemInit.COPPER_COIL.get(), "Copper Coil");
		add(ItemInit.PROCESSOR.get(), "Processor");
		add(ItemInit.RAW_SILICON_BLEND.get(), "Raw Silicon Blend");
		add(ItemInit.SILICON.get(), "Silicon");
		add(ItemInit.SILICON_BOLUS.get(), "Silicon Bolus");
		add(ItemInit.HIGH_PURITY_SILICON.get(), "High Purity Silicon");
		add(ItemInit.TRANSISTOR.get(), "Transistor");
		add(ItemInit.AMMONIUM_NITRATE.get(), "Ammonium Nitrate");
		add(ItemInit.LDPE.get(), "LDPE");
		add(ItemInit.HDPE.get(), "HDPE");
		add(ItemInit.UHMWPE.get(), "UHMWPE");
		add(ItemInit.SODIUM_HYDROXIDE.get(), "Sodium Hydroxide");
		add(ItemInit.SODIUM_CARBONATE.get(), "Sodium Carbonate");
		add(ItemInit.CALCIUM_SULPHATE.get(), "Calcium Sulphate");
		add(ItemInit.PALLADIUM_CHLORIDE.get(), "Palladium Chloride");
		add(ItemInit.PALLADIUM_ON_CARBON.get(), "Palladium on Carbon");
		add(ItemInit.HEXAMINE.get(), "Hexamine");
		add(ItemInit.NITRONIUM_TETRAFLUOROBORATE.get(), "Nitronium Tetrafluoroborate");
		add(ItemInit.LOGIC_UNIT.get(), "Logic Unit");
		add(ItemInit.PROCESSOR_CORE.get(), "Processor Core");

		// Tooltips
		addTooltip("ldpe", "Low Density Polyethylene");
		addTooltip("hdpe", "High Density Polyethylene");
		addTooltip("uhmwpe", "Ultra High Molecular Weight Polyethylene");
		
		// Fluids
		add(FluidInit.OXYGEN, "Oxygen", "Bucket");
		add(FluidInit.NITROGEN, "Nitrogen", "Bucket");
		add(FluidInit.AMMONIA, "Ammonia", "Bucket");
		add(FluidInit.CARBON_DIOXIDE, "Carbon Dioxide", "Bucket");
		add(FluidInit.HYDROGEN, "Hydrogen", "Bucket");
		add(FluidInit.METHANE, "Methane", "Bucket");
		add(FluidInit.ETHANE, "Ethane", "Bucket");
		add(FluidInit.ETHYLENE, "Ethylene", "Bucket");
		add(FluidInit.CHLORINE, "Chlorine", "Bucket");
		add(FluidInit.BORON_TRIFLUORIDE, "Boron Trifluoride", "Bucket");
		add(FluidInit.FORMALDEHYDE, "Formaldehyde", "Bucket");
		add(FluidInit.NITROGEN_DIOXIDE, "Nitrogen Dioxide", "Bucket");
		add(FluidInit.SULPHUR_DIOXIDE, "Sulphur Dioxide", "Bucket");
		add(FluidInit.HYDROGEN_BROMIDE, "Hydrogen Bromide", "Bucket");
		add(FluidInit.CARBON_MONOXIDE, "Carbon Monoxide", "Bucket");
		add(FluidInit.ARGON, "Argon", "Bucket");

		add(FluidInit.ACETIC_ACID, "Acetic Acid", "Bucket");
		add(FluidInit.BRINE, "Brine", "Bucket");
		add(FluidInit.HYDROCHLORIC_ACID, "Hydrochloric Acid", "Bucket");
		add(FluidInit.SULPHURIC_ACID, "Sulphuric Acid", "Bucket");
		add(FluidInit.BROMINE, "Bromine", "Bucket");
		add(FluidInit.BENZENE, "Benzene", "Bucket");
		add(FluidInit.TOLUENE, "Toluene", "Bucket");
		add(FluidInit.METHANOL, "Methanol", "Bucket");
		add(FluidInit.ETHANOL, "Ethanol", "Bucket");
		add(FluidInit.HYDROGEN_FLUORIDE, "Hydrogen Fluoride", "Bucket");
		add(FluidInit.ACETALDEHYDE, "Acetaldehyde", "Bucket");
		add(FluidInit.BENZYL_CHLORIDE, "Benzyl Chloride", "Bucket");
		add(FluidInit.NITRIC_ACID, "Nitric Acid", "Bucket");
		add(FluidInit.BROMOBENZENE, "Bromobenzene", "Bucket");
		add(FluidInit.GLYOXAL, "Glyoxal", "Bucket");
		add(FluidInit.BENZYLAMINE, "Benzylamine", "Bucket");
		add(FluidInit.HNIW, "HNIW (CL-20)", "Bucket");
		add(FluidInit.HEXOGEN, "Hexogen (RDX)", "Bucket");
		add(FluidInit.NITROMETHANE, "Nitromethane", "Bucket");
		add(FluidInit.SULPHUR_TRIOXIDE, "Sulphur Trioxide", "Bucket");
		add(FluidInit.MOLTEN_LEAD, "Molten Lead", "Bucket");
		add(FluidInit.MOLTEN_BISMUTH, "Molten Bismuth", "Bucket");
		add(FluidInit.LEAD_BISMUTH_EUTECTIC, "Lead Bismuth Eutectic", "Bucket");
	}
}