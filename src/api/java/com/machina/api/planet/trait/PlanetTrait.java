/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.machina.api.planet.trait;

import static com.machina.api.ModIDs.MACHINA;

import com.machina.api.registry.annotation.RegisterPlanetTrait;
import com.machina.api.util.Color;
import com.matyrobbrt.lib.registry.annotation.RegistryHolder;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Items;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;

@RegistryHolder(modid = MACHINA)
public class PlanetTrait extends ForgeRegistryEntry<PlanetTrait> {

	private final String description;
	private final int color;

	public PlanetTrait(int color, String description) {
		this.color = color;
		this.description = description;
	}

	@Override
	public String toString() {
		return new TranslationTextComponent(getRegistryName().getNamespace() + ".planet_trait." + getRegistryName().getPath()).getString();
	}

	public boolean exists() {
		return this != NONE;
	}

	public void render(MatrixStack matrixStack, int xPosition, int yPosition, boolean coloured) {
		Minecraft minecraft = Minecraft.getInstance();
		TextureAtlasSprite textureatlassprite = getSprite();
		minecraft.getTextureManager().bind(textureatlassprite.atlas().location());
		Color colour = new Color(getColor());
		float[] compFloat = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
		float[] colourArray = colour.getComponents(compFloat);
		if (coloured) {
			RenderSystem.color4f(colourArray[0], colourArray[1], colourArray[2], colourArray[3]);
		} else {
			RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		}
		AbstractGui.blit(matrixStack, xPosition, yPosition, 12, 16, 16, textureatlassprite);
	}

	public String getDescription() {
		return description;
	}

	public int getColor() {
		return color;
	}

	public TextureAtlasSprite getSprite() {
		return PlanetTraitSpriteUploader.getFromInstance(this);
	}

	/**
	 * This is similar to {@link Items#AIR}. It is an empty trait that will be the
	 * default value of
	 * {@link IForgeRegistry#getValue(net.minecraft.util.ResourceLocation)}. <br>
	 * <strong>DO NOT ADD TO A PLANET</strong>
	 */
	@RegisterPlanetTrait(id = "none")
	public static final PlanetTrait NONE = new PlanetTrait(0, "");

}
