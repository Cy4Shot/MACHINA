package com.machina.client.screen;

import com.machina.client.util.UIHelper;
import com.machina.util.text.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VLCWarningScreen extends Screen {
	public static boolean accepted = false;
	public static final String URL = "https://nightlies.videolan.org/";

	public VLCWarningScreen() {
		super(StringUtils.EMPTY);
	}

	protected void init() {
		super.init();
		this.addButton(new Button(this.width / 2 - 155, 2 * this.height / 3, 150, 20,
				StringUtils.translateCompScreen("vlc.link"), (b) -> {
					this.handleComponentClicked(Style.EMPTY.withClickEvent(new ClickEvent(Action.OPEN_URL, URL)));
				}));
		this.addButton(new Button(this.width / 2 + 5, 2 * this.height / 3, 150, 20,
				StringUtils.translateCompScreen("vlc.title"), (b) -> {
					accepted = true;
					this.minecraft.setScreen((Screen) null);
				}));
	}

	public void render(MatrixStack stack, int mX, int mY, float t) {
		this.renderBackground(stack);
		UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("vlc.warning1"), width / 2,
				height / 2 - 30, 0xFF_ff0000, 0xFF_0e0e0e);
		UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("vlc.warning2"), width / 2,
				height / 2 - 10, 0xFF_ff0000, 0xFF_0e0e0e);
		UIHelper.drawCenteredStringWithBorder(stack, StringUtils.translateScreen("vlc.warning3"), width / 2,
				height / 2 + 10, 0xFF_ff0000, 0xFF_0e0e0e);
		super.render(stack, mX, mY, t);
	}

	public boolean shouldCloseOnEsc() {
		return false;
	}
}