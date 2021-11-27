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

package com.cy4.machina.compat.jei.trait;

import java.util.LinkedList;
import java.util.List;

import com.machina.api.planet.trait.PlanetTrait;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import mezz.jei.api.ingredients.IIngredientRenderer;

public class PlanetTraitJEIRenderer implements IIngredientRenderer<PlanetTrait> {

	@SuppressWarnings("deprecation")
	@Override
	public void render(MatrixStack matrixStack, int xPosition, int yPosition, PlanetTrait trait) {
		if (trait != null) {
			RenderSystem.pushMatrix();
			RenderSystem.multMatrix(matrixStack.last().pose());
			RenderSystem.enableDepthTest();
			RenderHelper.turnBackOn();
			trait.render(matrixStack, xPosition, yPosition, trait.getProperties().jeiProperties().colouredInJei());
			RenderSystem.disableBlend();
			RenderHelper.turnOff();
			RenderSystem.popMatrix();
		}
	}

	@Override
	public List<ITextComponent> getTooltip(PlanetTrait trait, ITooltipFlag tooltipFlag) {
		List<ITextComponent> tooltips = new LinkedList<>();
		tooltips.addAll(trait.getTooltip(tooltipFlag));
		tooltips.add(new TranslationTextComponent("jei.planet_trait.type"));
		return tooltips;
	}

}
