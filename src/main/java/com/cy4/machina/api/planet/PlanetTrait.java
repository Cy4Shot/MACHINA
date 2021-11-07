package com.cy4.machina.api.planet;

import java.lang.reflect.Field;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.cy4.machina.Machina;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class PlanetTrait extends ForgeRegistryEntry<PlanetTrait> {

	public static final IForgeRegistry<PlanetTrait> REGISTRY = null;

	public static void createRegistry(RegistryEvent.NewRegistry event) {
		RegistryBuilder<PlanetTrait> registryBuilder = new RegistryBuilder<>();
		registryBuilder.setName(new ResourceLocation(Machina.MOD_ID, "planet_trait_registry"));
		registryBuilder.setType(PlanetTrait.class);
		
		try {
			Field registryField = PlanetTrait.class.getDeclaredField("REGISTRY");
			registryField.setAccessible(true);
			FieldUtils.removeFinalModifier(registryField);
			IForgeRegistry<PlanetTrait> registry = registryBuilder.create();
			registryField.set(PlanetTrait.class, registry);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
		}
	}

	private int color;

	public PlanetTrait(int color) {
		this.setColor(color);
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public ITextComponent getName() {
		return new TranslationTextComponent(
				this.getRegistryName().getNamespace() + ".trait." + this.getRegistryName().getPath())
						.setStyle(Style.EMPTY.withColor(Color.fromRgb(this.getColor())));
	}
	
	@Override
	public String toString() {
		return getName().getString();
	}
}
