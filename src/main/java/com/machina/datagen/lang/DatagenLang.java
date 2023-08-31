package com.machina.datagen.lang;

import java.util.Map;
import java.util.TreeMap;

import javax.swing.text.JTextComponent.KeyBinding;

import com.machina.Machina;

import net.minecraft.data.PackOutput;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.RegistryObject;

public abstract class DatagenLang extends LanguageProvider {

	protected final Map<String, String> data = new TreeMap<>();

	String modid;
	String music_disc;

	public DatagenLang(PackOutput gen, String locale, String modid) {
		super(gen, modid, locale);
		this.modid = modid;
	}

	protected void add(Fluid fluid, String name) {
		add(new FluidStack(fluid, 2).getTranslationKey(), name);
	}

	protected void add(RegistryObject<CreativeModeTab> tab, String name) {
		add(Machina.MOD_ID + ".creativemodetab." + tab.getId().getPath(), name);
	}

	protected void addTooltip(String item, String name) {
		add(modid + ".tooltip." + item, name);
	}

	protected void add(KeyBinding key, String name) {
		add(key.actionName, name);
	}

	protected void addJeiCat(String cat, String name) {
		add("gui.jei.category." + cat, name);
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

	protected void addMultiblock(String id, String locale) {
		add(modid + ".multiblock." + id, locale);
	}

	public void addMusicDisc(Item key, String desc) {
		add(key.getDescriptionId(), this.music_disc);
		add(key.getDescriptionId() + ".desc", desc);
	}

	public void add(String key, String value) {
		if (data.put(key, value) != null)
			throw new IllegalStateException("Duplicate translation key " + key);
		super.add(key, value);
	}
}
