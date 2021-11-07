package com.cy4.machina.datagen.client.lang;

import com.cy4.machina.Machina;

import net.minecraft.data.DataGenerator;

import net.minecraftforge.common.data.LanguageProvider;

public class EnUsLangProvider extends LanguageProvider {

	public EnUsLangProvider(DataGenerator gen) {
		super(gen, Machina.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		addItemGroup("machinaItemGroup", "Machina");
	}
	
	private void addItemGroup(String key, String name) {
		add("itemGroup." + key, name);
	}

}
