package com.cy4.machina.api.planet.trait;

import java.util.Optional;

import com.cy4.machina.api.annotation.ChangedByReflection;
import com.cy4.machina.init.PlanetTraitInit;
import com.cy4.machina.util.MachinaRL;
import com.cy4.machina.util.helper.CustomRegistryHelper;
import com.cy4.machina.util.objects.TargetField;

import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;

public class PlanetTrait extends ForgeRegistryEntry<PlanetTrait> {

	@ChangedByReflection(when = "commonSetup (when the registry is built)")
	public static final IForgeRegistry<PlanetTrait> REGISTRY = null;

	public static void createRegistry(RegistryEvent.NewRegistry event) {
		CustomRegistryHelper.registerRegistry(new TargetField(PlanetTrait.class, "REGISTRY"), PlanetTrait.class,
				new MachinaRL("planet_trait"), Optional.of(new MachinaRL("not_existing")));
	}

	private int color;

	public PlanetTrait(int color) {
		this.setColor(color);
	}

	public int getColor() { return color; }

	public void setColor(int color) { this.color = color; }

	public ITextComponent getName() {
		return new TranslationTextComponent(
				this.getRegistryName().getNamespace() + ".trait." + this.getRegistryName().getPath())
						.setStyle(Style.EMPTY.withColor(Color.fromRgb(this.getColor())));
	}

	@Override
	public String toString() {
		return getName().getString();
	}

	public boolean exists() {
		return this != PlanetTraitInit.NOT_EXISTING;
	}
}
