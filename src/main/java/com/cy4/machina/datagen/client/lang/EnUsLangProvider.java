package com.cy4.machina.datagen.client.lang;

import com.cy4.machina.Machina;
import com.cy4.machina.api.planet.PlanetTrait;
import com.cy4.machina.init.FluidInit;
import com.cy4.machina.init.PlanetTraitInit;

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
		add(PlanetTraitInit.LOW_GRAVITY, "Low Gravity");
		add(PlanetTraitInit.SUPERHOT, "Superhot");
		
		addItemGroup("machinaItemGroup", "Machina");
		
		add(FluidInit.HYDROGEN_BUCKET, "Hydrogen Bucket");
		add(FluidInit.LIQUID_HYDROGEN_BUCKET, "Liquid Hydrogen Bucket");
		add(FluidInit.OXYGEN_BUCKET, "Oxygen Bucket");
		
		addCommandFeedback("planet_traits.add_trait.success", "Trait added!");
		addCommandFeedback("planet_traits.add_trait.duplicate", "This planet already has the trait %s!");
		addCommandFeedback("planet_traits.remove_trait.success", "Trait removed!");
		addCommandFeedback("planet_traits.remove_trait.not_existing", "This planet does not have the trait %s!");
		addCommandFeedback("planet_traits.list_traits.success", "This planet has the following traits: \n§6%s");
		addCommandFeedback("planet_traits.list_traits.no_traits", "This planet has no traits!");
		addCommandFeedback("planet_traits.not_planet", "This dimension is not a planet!");
		
		addCommandArgumentFeedback("planet_trait.invalid", "Invalid Planet Trait: §6%s");
		
		addDamageSourceMsg("liquidHydrogen", "%1$s stayed too much in hydrogen... Never do that at home kids!", "%1$s encountered hydrogen whilst fighting %2$s!");
	}
	
	private void addItemGroup(String key, String name) {
		add("itemGroup." + key, name);
	}
	
	private void addTrait(String key, String name) {
		add(Machina.MOD_ID + ".trait." + key, name);
	}
	
	private void add(PlanetTrait trait, String name) {
		add(trait.getRegistryName().getNamespace() + ".trait." + trait.getRegistryName().getPath(), name);
	}
	
	private void addCommandFeedback(String key, String name) {
		add("command." + key, name);
	}
	
	private void addCommandArgumentFeedback(String key, String name) {
		add("argument." + key, name);
	}
	
	private void addDamageSourceMsg(String name, String normal, String diedWhilstFighting) {
		add("death.attack." + name, normal);
		add("death.attack." + name + ".player", diedWhilstFighting);
	}

}
