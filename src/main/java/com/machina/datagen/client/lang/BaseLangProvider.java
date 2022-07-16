package com.machina.datagen.client.lang;

import com.machina.Machina;
import com.machina.planet.attribute.PlanetAttributeType;
import com.machina.planet.trait.PlanetTrait;
import com.machina.registration.init.FluidInit.FluidObject;

import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fluids.FluidStack;

public abstract class BaseLangProvider extends LanguageProvider{

	public BaseLangProvider(DataGenerator gen,  String locale) {
		super(gen, Machina.MOD_ID, locale);
	}
	
	protected void addItemGroup(String key, String name) {
		add("itemGroup." + key, name);
	}

	protected void add(PlanetTrait trait, String name) {
		add(trait.getRegistryName().getNamespace() + ".planet_trait." + trait.getRegistryName().getPath(), name);
	}
	
	protected void add(PlanetAttributeType<?> attr, String name) {
		add(attr.getRegistryName().getNamespace() + ".planet_attribute." + attr.getRegistryName().getPath(), name);
	}

	protected void add(Fluid fluid, String name) {
		add(new FluidStack(fluid, 2).getTranslationKey(), name);
	}
	
	protected void add(FluidObject obj, String name, String bucket) {
		add(obj.fluid(), name);
		add(obj.flowing(), name);
		add(obj.bucket(), name + " " + bucket);
	}

	protected void addCommandFeedback(String key, String name) {
		add("command." + key, name);
	}
	
	protected void addCommandArgumentFeedback(String key, String name) {
		add("argument." + key, name);
	}
	
	protected void addTerminalCommand(String name, String desc) {
		add("machina.terminal.description." + name, desc);
	}
	
	protected void addTerminalFeedback(String comm, String id, String text) {
		add("machina.terminal.feedback." + comm + "." + id, text);
	}
	
	protected void addScreen(String screen, String id, String locale) {
		add("machina.screen." + screen + "." + id, locale);
	}
	
	protected void addShipComponent(String id, String locale) {
		add("machina.ship_component." + id, locale);
	}
	
}
