package com.machina.datagen.client.lang;

import com.machina.planet.attribute.PlanetAttributeType;
import com.machina.planet.trait.PlanetTrait;
import com.machina.registration.init.FluidInit.FluidObject;
import com.machina.registration.init.SoundInit.Sound;
import com.machina.research.Research;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fluids.FluidStack;

public abstract class BaseLangProvider extends LanguageProvider {

	String modid;
	String music_disc;

	public BaseLangProvider(DataGenerator gen, String locale, String modid) {
		super(gen, modid, locale);
		this.modid = modid;
	}

	protected void addItemGroup(ItemGroup key, String name) {
		add(((TranslationTextComponent) key.getDisplayName()).getKey(), name);
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

	protected void add(Sound sound, String name) {
		add(modid + ".subtitle." + sound.name(), name);
	}
	
	protected void addTooltip(String item, String name) {
		add(modid + ".tooltip." + item, name);
	}

	protected void add(KeyBinding key, String name) {
		add(key.getName(), name);
	}
	
	protected void add(Research r, String name, String desc) {
		add(r.getNameKey(), name);
		add(r.getDescKey(), desc);
	}
	
	protected void addDamageSource(DamageSource source, String message) {
		add("death.attack." + source.getMsgId(), message);
	}
	
	protected void addDamageSourceAttacker(DamageSource source, String message) {
		add("death.attack." + source.getMsgId() + ".player", message);
	}

	protected void addKeyCategory(String category, String name) {
		add("key.category." + category, name);
	}

	protected void addCommandFeedback(String key, String name) {
		add("command." + key, name);
	}

	protected void addCommandArgumentFeedback(String key, String name) {
		add("argument." + key, name);
	}

	protected void addTerminalCommand(String name, String desc) {
		add(modid + ".terminal.description." + name, desc);
	}

	protected void addTerminalFeedback(String comm, String id, String text) {
		add(modid + ".terminal.feedback." + comm + "." + id, text);
	}

	protected void addScreen(String screen, String id, String locale) {
		add(modid + ".screen." + screen + "." + id, locale);
	}

	protected void addShipComponent(String id, String locale) {
		add(modid + ".ship_component." + id, locale);
	}

	public void addMusicDisc(Item key, String desc) {
		add(key.getDescriptionId(), this.music_disc);
		add(key.getDescriptionId() + ".desc", desc);
	}
}
