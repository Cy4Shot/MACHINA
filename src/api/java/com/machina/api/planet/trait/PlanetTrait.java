/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license:
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
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
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

import java.util.List;
import java.util.Optional;

import com.cy4.machina.init.PlanetTraitInit;
import com.cy4.machina.util.MachinaRL;
import com.cy4.machina.util.helper.CustomRegistryHelper;
import com.cy4.machina.util.objects.TargetField;
import com.google.common.collect.Lists;
import com.machina.api.annotation.ChangedByReflection;
import com.machina.api.planet.attribute.PlanetAttributeList;
import com.machina.api.registry.annotation.RegisterCustomRegistry;
import com.machina.api.registry.annotation.RegistryHolder;
import com.machina.api.util.Color;
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

@RegistryHolder
public class PlanetTrait extends ForgeRegistryEntry<PlanetTrait> {

	@ChangedByReflection(when = "commonSetup (when the registry is built)")
	public static final IForgeRegistry<PlanetTrait> REGISTRY = null;

	@RegisterCustomRegistry
	public static void createRegistry(RegistryEvent.NewRegistry event) {
		CustomRegistryHelper.<PlanetTrait>registerRegistry(new TargetField(PlanetTrait.class, "REGISTRY"), PlanetTrait.class,
				new MachinaRL("planet_trait"), Optional.of(new MachinaRL("not_existing")),
				Optional.of(new MachinaRL("planet_trait_registry")));
	}

	private final Properties properties;

	public PlanetTrait(Properties properties) {
		this.properties = properties;
	}

	/**
	 * Use the {@link PlanetTrait#PlanetTrait(Properties)} constructor (the one
	 * using {@link Properties})
	 *
	 * @deprecated
	 * @param color
	 */
	@Deprecated
	public PlanetTrait(int color) {
		this(new Properties(color));
	}

	public Properties getProperties() { return properties; }

	public int getColor() { return properties.getColour(); }

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

	public boolean hasDescription() {
		return properties.jeiProperties.hasJeiDescription;
	}

	public boolean showsInJei() {
		return properties.jeiProperties.showsInJei;
	}

	public boolean exists() {
		return this != PlanetTraitInit.NOT_EXISTING;
	}

	public TextureAtlasSprite getIcon() { return PlanetTraitSpriteUploader.getFromInstance(this); }

	public void render(MatrixStack matrixStack, int xPosition, int yPosition, boolean coloured) {
		Minecraft minecraft = Minecraft.getInstance();
		TextureAtlasSprite textureatlassprite = getIcon();
		minecraft.getTextureManager().bind(textureatlassprite.atlas().location());
		Color colour = new Color(getColor());
		float[] compFloat = new float[] {
				1.0f, 1.0f, 1.0f, 1.0f
		};
		float[] colourArray = colour.getComponents(compFloat);
		if (coloured) {
			RenderSystem.color4f(colourArray[0], colourArray[1], colourArray[2], colourArray[3]);
		} else {
			RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		}
		AbstractGui.blit(matrixStack, xPosition, yPosition, 12, 16, 16, textureatlassprite);
	}
	
	public boolean hasAttributeModifier() { return false; }
	public void addAttribute(PlanetAttributeList attributes) {}
	public void removeAttribute(PlanetAttributeList attributes) {}

	public static class Properties {

		private final int colour;
		private JeiProperties jeiProperties = new JeiProperties();

		public Properties(int colour) {
			this.colour = colour;
		}

		public Properties withJeiProperties(JeiProperties jeiProperties) {
			this.jeiProperties = jeiProperties;
			return this;
		}

		public int getColour() { return colour; }

		public JeiProperties jeiProperties() {
			return jeiProperties;
		}
	}

	public static class JeiProperties {

		private boolean showsInJei = true;
		private boolean hasJeiDescription = true;
		private boolean colouredInJei = true;

		public JeiProperties() {
			//
		}

		public JeiProperties setShowsInJei(boolean value) {
			showsInJei = value;
			return this;
		}

		public JeiProperties setHasJeiDescription(boolean value) {
			hasJeiDescription = value;
			return this;
		}

		public void setColouredInJei(boolean colouredInJei) {
			this.colouredInJei = colouredInJei;
		}

		public boolean showsInJei() {
			return showsInJei;
		}

		public boolean hasJeiDescription() {
			return hasJeiDescription;
		}

		public boolean colouredInJei() {
			return colouredInJei;
		}

	}

}
