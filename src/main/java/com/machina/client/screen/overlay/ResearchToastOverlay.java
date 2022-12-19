package com.machina.client.screen.overlay;

import java.util.PriorityQueue;
import java.util.Queue;

import com.machina.client.util.UIHelper;
import com.machina.research.Research;
import com.machina.util.text.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class ResearchToastOverlay {

	private static Queue<Toast> q = new PriorityQueue<>();
	private static Toast current = null;

	public static void playToast(Research res) {
		q.add(new Toast(res));
	}

	public static class Toast {
		Research res;
		public int tick;

		public Toast(Research res) {
			this.res = res;
			this.tick = 0;
		}

		public void tick() {
			this.tick++;
		}

		public boolean isComplete() {
			return this.tick == 1000;
		}
	}

	public static void render(MatrixStack stack, MainWindow window) {
		if (current == null || current.isComplete()) {
			if (q.isEmpty()) {
				current = null;
				return;
			}
			current = q.remove();
		}

		Minecraft mc = Minecraft.getInstance();
		if (current.tick == 0 && !mc.isPaused() && mc.screen == null) {
			mc.getSoundManager().play(SimpleSound.forUI(SoundEvents.PLAYER_LEVELUP, 1f));
		}

		int width = window.getGuiScaledWidth();
		int height = window.getGuiScaledHeight();
		float scale = width > 600 ? 1.5f : 1f;
		width = MathHelper.ceil(width / scale);
		height = MathHelper.ceil(height / scale);
		float alpha = 1f;
		int a = (int) (alpha * 256) << 24;
		String text = StringUtils
				.translateScreen("research_toast.complete") + StringUtils.translate(current.res.getNameKey());
		int w = UIHelper.getWidth(text);

		RenderSystem.pushMatrix();
		RenderSystem.scalef(scale, scale, scale);
		RenderSystem.color4f(1f, 1f, 1f, alpha);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		UIHelper.bindPrgrs();
		UIHelper.betterBlit(stack, (width - w) / 2 - 6, height / 4 + 7.5f, 240, 0, 3, 16, 256);
		UIHelper.betterBlit(stack, (width + w) / 2 + 3, height / 4 + 7.5f, 253, 0, 3, 16, 256);
		for (int i = 0; i < w + 6; i++) {
			UIHelper.betterBlit(stack, (width - w) / 2 - 3 + i, height / 4 + 7.5f, 250, 0, 1, 16, 256);
		}
		UIHelper.drawCenteredStringWithBorder(stack, text, width / 2, height / 4 + 12, 0x00fefe + a, 0x0e0e0e + a);
		RenderSystem.disableBlend();
		RenderSystem.popMatrix();

		if (mc.isPaused() || mc.screen != null)
			return;
		current.tick();
	}

}
