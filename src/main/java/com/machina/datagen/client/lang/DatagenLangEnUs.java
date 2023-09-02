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
		add(ItemInit.ALUMINUM_INGOT.get(), "Aluminum Ingot");
		
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