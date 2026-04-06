package com.machina.client.screen;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.weather.manager.ClientWeatherManager;
import com.machina.weather.system.ClientWeatherSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

public class WindCompassScreen extends Screen {
	private static final int RING_COLOR = 0xAA7DA3BC;
	private static final int MAJOR_AXIS_COLOR = 0xFFC5D8E8;
	private static final int MINOR_AXIS_COLOR = 0x806E8799;
	private static final int WIND_ARROW_COLOR = 0xFF00FEFE;

	protected int imageWidth = 235;
	protected int imageHeight = 100;

	private Vec2 smoothedWind = new Vec2(0.0F, 0.0F);

	public WindCompassScreen() {
		super(Component.empty());
	}

	public static void open() {
		Minecraft mc = Minecraft.getInstance();
		mc.setScreen(new WindCompassScreen());
	}

	protected int midWidth() {
		return (this.width - this.imageWidth) / 2;
	}

	protected int midHeight() {
		return (this.height - this.imageHeight) / 2;
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
		super.render(gui, mouseX, mouseY, partialTick);
		int i = midWidth();
		int j = midHeight();

		MUI.blitCommon(gui, i, j - 36, 179, 94, 235, 146);
		MUI.drawStringVertical(gui, MUI.uistr("wind_compass"), i + 245, j - 36);

		int cx = i + 235 / 2;
		int cy = j - 36 + 146 / 2;
		int radius = 48;

		MUI.drawDebugCircle(gui, cx, cy, radius, 72, RING_COLOR);
		MUI.drawDebugCircle(gui, cx, cy, (int) (radius * 0.66F), 60, 0x9095AEC1);
		MUI.drawDebugCircle(gui, cx, cy, (int) (radius * 0.33F), 48, 0x608AA3B6);
		drawTicks(gui, cx, cy, radius, 24);

		MUI.drawDebugLine(gui, cx - radius, cy, cx + radius, cy, MAJOR_AXIS_COLOR);
		MUI.drawDebugLine(gui, cx, cy - radius, cx, cy + radius, MAJOR_AXIS_COLOR);
		MUI.drawDebugLine(gui, cx - (int) (radius * 0.7F), cy - (int) (radius * 0.7F), cx + (int) (radius * 0.7F),
				cy + (int) (radius * 0.7F), MINOR_AXIS_COLOR);
		MUI.drawDebugLine(gui, cx - (int) (radius * 0.7F), cy + (int) (radius * 0.7F), cx + (int) (radius * 0.7F),
				cy - (int) (radius * 0.7F), MINOR_AXIS_COLOR);

		drawCardinal(gui, "N", cx - font.width("N") / 2, cy - radius - 14);
		drawCardinal(gui, "S", cx - font.width("S") / 2, cy + radius + 6);
		drawCardinal(gui, "W", cx - radius - 15, cy - 5);
		drawCardinal(gui, "E", cx + radius + 9, cy - 5);

		Vec2 wind = getSmoothedWind();
		float intensity = (float) Math.sqrt(wind.x * wind.x + wind.y * wind.y);
		if (intensity > 1.0E-4F) {
			float nx = wind.x / intensity;
			float ny = wind.y / intensity;
			float arrowLen = Mth.clamp(radius * (0.30F + intensity * 0.45F), radius * 0.25F, radius * 0.9F);
			float tipX = cx + nx * arrowLen;
			float tipY = cy + ny * arrowLen;

			MUI.drawDebugLine(gui, cx, cy, tipX, tipY, WIND_ARROW_COLOR);

			int markerSize = 6;
			int drawX = (int) tipX - markerSize / 2;
			int drawY = (int) tipY - markerSize / 2;
			MUI.blitCommon(gui, drawX, drawY, 467, 80, markerSize, markerSize);
		}

		MUI.drawString(gui,
				MUI.uistr("wind_compass.wind").append(Component.literal(": "))
						.append(Component.literal(String.format("%.2f", wind.x))
								.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1)))
						.append(Component.literal("x "))
						.append(Component.literal(String.format("%.2f", wind.y))
								.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1)))
						.append(Component.literal("z")),
				i + 4, j - 32, MUI.CYAN);
		MUI.drawString(gui, MUI.uistr("wind_compass.intensity").append(Component.literal(": ")).append(Component
				.literal(String.format("%.2f", intensity)).withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_2))),
				i + 4, j - 22, MUI.CYAN);
	}

	private void drawCardinal(GuiGraphics gui, String label, int x, int y) {
		MUI.drawString(gui, Component.literal(label), x, y, MUI.CYAN);
	}

	private void drawTicks(GuiGraphics gui, int cx, int cy, int radius, int count) {
		float step = (float) (Math.PI * 2.0D / count);
		for (int i = 0; i < count; i++) {
			float a = i * step;
			float cos = (float) Math.cos(a);
			float sin = (float) Math.sin(a);
			float inner = radius - (i % 2 == 0 ? 10.0F : 6.0F);
			float x1 = cx + cos * inner;
			float y1 = cy + sin * inner;
			float x2 = cx + cos * radius;
			float y2 = cy + sin * radius;
			MUI.drawDebugLine(gui, x1, y1, x2, y2, 0x90B8D1E4);
		}
	}

	private Vec2 getSmoothedWind() {
		Vec2 target = getWindVector();
		float alpha = 0.02F;
		smoothedWind = new Vec2(Mth.lerp(alpha, smoothedWind.x, target.x), Mth.lerp(alpha, smoothedWind.y, target.y));
		return smoothedWind;
	}

	private Vec2 getWindVector() {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) {
			return new Vec2(0.0F, 0.0F);
		}
		ClientWeatherSystem system = ClientWeatherManager.getSystem(mc.level);
		if (system == null || system.getWindDirection() == null) {
			return new Vec2(0.0F, 0.0F);
		}
		return system.getWindDirection();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
