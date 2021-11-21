package com.cy4.machina.api.planet.trait;

import java.util.List;
import java.util.Optional;

import com.cy4.machina.api.annotation.ChangedByReflection;
import com.cy4.machina.api.util.Color;
import com.cy4.machina.init.PlanetTraitInit;
import com.cy4.machina.util.MachinaRL;
import com.cy4.machina.util.helper.CustomRegistryHelper;
import com.cy4.machina.util.objects.TargetField;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.util.ITooltipFlag;
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
				new MachinaRL("planet_trait"), Optional.of(new MachinaRL("not_existing")),
				Optional.of(new MachinaRL("planet_trait_registry")));
	}

	private int color;

	public PlanetTrait(int color) {
		this.setColor(color);
	}

	public int getColor() { return color; }

	public void setColor(int color) { this.color = color; }

	public ITextComponent getName() {
		return new TranslationTextComponent(
				this.getRegistryName().getNamespace() + ".planet_trait." + this.getRegistryName().getPath())
						.setStyle(Style.EMPTY.withColor(net.minecraft.util.text.Color.fromRgb(this.getColor())));
	}

	@Override
	public String toString() {
		return getName().getString();
	}

	public List<ITextComponent> getTooltip(ITooltipFlag flag) {
		return Lists.newArrayList(getName());
	}

	public List<ITextComponent> getDescription() {
		return Lists.newArrayList(new TranslationTextComponent(
				getRegistryName().getNamespace() + ".planet_trait." + getRegistryName().getPath() + ".description"));
	}
	
	public boolean hasDescription() { return true; }
	
	public boolean showsInJei() { return true; }

	public boolean exists() {
		return this != PlanetTraitInit.NOT_EXISTING;
	}

	public TextureAtlasSprite getIcon() { return PlanetTraitSpriteUploader.getFromInstance(this); }

	public void render(MatrixStack matrixStack, int xPosition, int yPosition, boolean coloured) {
		Minecraft minecraft = Minecraft.getInstance();
		TextureAtlasSprite textureatlassprite = getIcon();
		minecraft.getTextureManager().bind(textureatlassprite.atlas().location());
		Color colour = new Color(this.color);
		float[] compFloat = new float[] {1.0f, 1.0f, 1.0f, 1.0f};
		float[] colourArray = colour.getComponents(compFloat);
		if (coloured) {
			RenderSystem.color4f(colourArray[0], colourArray[1], colourArray[2], colourArray[3]);
		} else {
			RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		}
		AbstractGui.blit(matrixStack, xPosition, yPosition, 12, 16, 16, textureatlassprite);
	}
	
}
