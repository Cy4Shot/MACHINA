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
		
		addCommandFeedback("planet_traits.add_trait.success", "Trait added!");
		addCommandFeedback("planet_traits.add_trait.duplicate", "This planet already has the trait %s!");
		addCommandFeedback("planet_traits.remove_trait.success", "Trait removed!");
		addCommandFeedback("planet_traits.remove_trait.not_existing", "This planet does not have the trait %s!");
		addCommandFeedback("planet_traits.list_traits.success", "This planet has the following traits: \n§6%s");
		addCommandFeedback("planet_traits.list_traits.no_traits", "This planet has no traits!");
		addCommandFeedback("planet_traits.not_planet", "This dimension is not a planet!");
		
		addCommandArgumentFeedback("planet_trait.invalid", "Invalid Planet Trait: §6%s");
	}
	
	private void addItemGroup(String key, String name) {
		add("itemGroup." + key, name);
	}
	
	private void addTrait(String key, String name) {
		add(Machina.MOD_ID + ".trait." + key, name);
	}
	
	private void addCommandFeedback(String key, String name) {
		add("command." + key, name);
	}
	
	private void addCommandArgumentFeedback(String key, String name) {
		add("argument." + key, name);
	}

}
