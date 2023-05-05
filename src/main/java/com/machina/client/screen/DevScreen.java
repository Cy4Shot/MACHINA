package com.machina.client.screen;

import java.awt.event.KeyEvent;
import java.util.UUID;

import com.machina.client.ClientStarchart;
import com.machina.client.util.UIHelper;
import com.machina.network.MachinaNetwork;
import com.machina.network.c2s.C2SDevPlanetCreationGUI;
import com.machina.network.c2s.C2SDevPlanetCreationGUI.ActionType;
import com.machina.registration.init.KeyBindingsInit;
import com.machina.util.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class DevScreen extends Screen {

	private TextFieldWidget dimensionID;

	public DevScreen() {
		super(StringUtils.EMPTY);
	}

	@Override
	protected void init() {
		super.init();
		this.addButton(
				new Button(width / 2 - 50, 50, 50, 20, StringUtils.toComp("Create"), btn -> this.onCreateButton()));
		this.addButton(new Button(width / 2 - 105, 50, 50, 20, StringUtils.toComp("Teleport"),
				btn -> this.onTeleportButton()));

		dimensionID = new TextFieldWidget(font, width / 2 - 150, 50, 40, 20, StringUtils.toComp("Dimension ID"));
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
				KeyEvent.getKeyText(KeyBindingsInit.DEV_SCREEN.getKey().getValue()))) {
			dimensionID.setValue("");
		}
	}

	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void overlayEvent(RenderGameOverlayEvent.Pre event) {
		if (event.getType() != ElementType.CROSSHAIRS) {
			return;
		}

		if (Minecraft.getInstance().screen instanceof DevScreen) {
			event.setCanceled(true);
		}
	}

	private void onCreateButton() {
		try {

			int id = Integer.valueOf(dimensionID.getValue());
			if (id >= ClientStarchart.getStarchart().size()) {
				invalidId();
				return;
			}

			MachinaNetwork.CHANNEL.sendToServer(new C2SDevPlanetCreationGUI(ActionType.CREATE, id));
			minecraft.player.sendMessage(StringUtils.toComp("Planet with the id of " + id + " was created!"),
					UUID.randomUUID());
			this.onClose();
		} catch (NumberFormatException e) {
			invalidId();
		}
	}

	private void onTeleportButton() {
		try {

			int id = Integer.valueOf(dimensionID.getValue());
			if (id >= ClientStarchart.getStarchart().size()) {
				invalidId();
				return;
			}

			MachinaNetwork.CHANNEL.sendToServer(new C2SDevPlanetCreationGUI(ActionType.TELEPORT, id));
			this.onClose();
		} catch (NumberFormatException e) {
			invalidId();
		}
	}

	private void invalidId() {
		minecraft.player.sendMessage(StringUtils.toComp("Invalid Planet ID!"), UUID.randomUUID());
	}

	@Override
	public void render(MatrixStack stack, int pMouseX, int pMouseY, float pPartialTicks) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.renderBackground(stack);

		super.render(stack, pMouseX, pMouseY, pPartialTicks);
		dimensionID.render(stack, pMouseX, pMouseY, pPartialTicks);

		int x = width / 2 - 150;

		String size = String.valueOf(ClientStarchart.getStarchart().size());
		String dim = minecraft.level.dimension().location().toString();

		UIHelper.drawStringWithBorder(stack, "Planet Count: ", x, 36, 0xFF_cc00ff, 0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, size, x + 70, 36, 0xFF_e3d914, 0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, "Currently In: ", x, 26, 0xFF_ff640a, 0xFF_0e0e0e);
		UIHelper.drawStringWithBorder(stack, dim, x + 70, 26, 0xFF_1bde0d, 0xFF_0e0e0e);
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
