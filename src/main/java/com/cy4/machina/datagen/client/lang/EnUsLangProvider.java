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
		
		addTrait("water_world", "Water World");
		addTrait("continental", "Continental");
		addTrait("landmass", "Landmass");
		addTrait("mountainous", "Mountainous");
		addTrait("hilly", "Hilly");
		addTrait("flat", "Flat");
		addTrait("ore_rich", "Ore Rich");
		addTrait("ore_barren", "Ore Barren");
		addTrait("canyons", "Canyons");
		addTrait("fiords", "Fiords");
		addTrait("ravines", "Ravines");
		addTrait("lakes", "Lakes");
		addTrait("volcanic", "Volcanic");
		addTrait("frozen", "Frozen");
		addTrait("layered", "Layered");
		
		addItemGroup("machinaItemGroup", "Machina");
	}
	
	private void addItemGroup(String key, String name) {
		add("itemGroup." + key, name);
	}
	
	private void addTrait(String key, String name) {
		add(Machina.MOD_ID + ".trait." + key, name);
	}

}
