package com.machina.datagen.lang;

import com.machina.Machina;
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
	}

}