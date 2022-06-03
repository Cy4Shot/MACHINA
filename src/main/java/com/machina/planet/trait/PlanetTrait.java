package com.machina.planet.trait;

import com.machina.registration.init.PlanetTraitInit;
import com.machina.util.color.Color;
import com.machina.util.text.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class PlanetTrait extends ForgeRegistryEntry<PlanetTrait> {

	private final int color;

	public PlanetTrait(int color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return StringUtils.translate(getRegistryName().getNamespace() + ".planet_trait." + getRegistryName().getPath());
	}

	public boolean exists() {
		return this != PlanetTraitInit.NONE;
	}

	public void render(MatrixStack matrixStack, int xPosition, int yPosition, boolean coloured) {
		TextureAtlasSprite textureatlassprite = getSprite();
		Minecraft.getInstance().getTextureManager().bind(textureatlassprite.atlas().location());
		float[] colourArray = new Color(getColor()).getComponents(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
		if (coloured)
			RenderSystem.color4f(colourArray[0], colourArray[1], colourArray[2], colourArray[3]);
		else
			RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		AbstractGui.blit(matrixStack, xPosition, yPosition, 12, 16, 16, textureatlassprite);
	}

	public int getColor() {
		return color;
	}

	public TextureAtlasSprite getSprite() {
		return PlanetTraitSpriteUploader.getFromInstance(this);
	}

}
