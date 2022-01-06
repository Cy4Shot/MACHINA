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

package com.machina.client.gui;

import java.awt.event.KeyEvent;
import java.util.UUID;

import com.machina.api.client.ClientDataHolder;
import com.machina.api.world.data.PlanetData;
import com.machina.init.KeyBindingsInit;
import com.machina.network.MachinaNetwork;
import com.machina.network.message.C2SDevPlanetCreationGUI;
import com.machina.network.message.C2SDevPlanetCreationGUI.ActionType;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class DevPlanetCreationScreen extends Screen {

	private TextFieldWidget dimensionID;

	public DevPlanetCreationScreen() {
		super(new StringTextComponent("CreativePlanetCreation"));
	}

	@Override
	protected void init() {
		super.init();
		this.addButton(new Button(width / 2 - 55, 50, 40, 20, new StringTextComponent("Create"),
				btn -> this.onCreateButton()));
		this.addButton(new Button(width / 2 - 105, 50, 50, 20, new StringTextComponent("Teleport"),
				btn -> this.onTeleportButton()));

		dimensionID = new TextFieldWidget(font, width / 2 - 150, 50, 40, 20,
				new TranslationTextComponent("Dimension ID"));
		dimensionID.setMaxLength(10);
		children.add(dimensionID);
		this.setInitialFocus(dimensionID);
		dimensionID.setFocus(true);
	}

	@Override
	public void resize(Minecraft pMinecraft, int pWidth, int pHeight) {
		String s = dimensionID.getValue();
		this.init(pMinecraft, pWidth, pHeight);
		dimensionID.setValue(s);
	}

	@Override
	public void tick() {
		super.tick();
		if (dimensionID.getValue().equalsIgnoreCase(
				KeyEvent.getKeyText(KeyBindingsInit.DEV_PLANET_CREATION_SCREEN.getKey().getValue()))) {
			dimensionID.setValue("");
		}
	}

	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void overlayEvent(RenderGameOverlayEvent.Pre event) {
		if (event.getType() != ElementType.CROSSHAIRS) {
			return;
		}

		if (Minecraft.getInstance().screen instanceof DevPlanetCreationScreen) {
			event.setCanceled(true);
		}
	}

	private void onCreateButton() {
		try {
			MachinaNetwork.CHANNEL.sendToServer(
					new C2SDevPlanetCreationGUI(ActionType.CREATE, Integer.valueOf(dimensionID.getValue())));
			minecraft.player.sendMessage(
					new StringTextComponent(
							"Planet with the id of " + Integer.valueOf(dimensionID.getValue()) + " was created!"),
					UUID.randomUUID());
			this.onClose();
		} catch (NumberFormatException e) {
			minecraft.player.sendMessage(new StringTextComponent("Invalid Planet ID!"), UUID.randomUUID());
		}
	}

	private void onTeleportButton() {
		try {
			MachinaNetwork.CHANNEL.sendToServer(
					new C2SDevPlanetCreationGUI(ActionType.TELEPORT, Integer.valueOf(dimensionID.getValue())));
			this.onClose();
		} catch (NumberFormatException e) {
			minecraft.player.sendMessage(new StringTextComponent("Invalid Planet ID!"), UUID.randomUUID());
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.renderBackground(pMatrixStack);
		dimensionID.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);

		PlanetData data = ClientDataHolder.getStarchart().getDataForLevel(minecraft.level);
		for (int i = 0; i < data.getTraits().size(); i++) {
			data.getTraits().get(i).render(pMatrixStack, width / 2 - 140 + (i * 18), 120, true);
		}
	}

	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
		return super.mouseClicked(pMouseX, pMouseY, pButton);
	}

	@Override
	public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
		if (pKeyCode == 257 && dimensionID.isFocused()) {
			onTeleportButton();
			onClose();
			return true;
		}
		return super.keyReleased(pKeyCode, pScanCode, pModifiers);
	}

}
