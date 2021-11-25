/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.compat.jei.trait;

import java.util.LinkedList;
import java.util.List;

import com.cy4.machina.api.planet.trait.PlanetTrait;
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
