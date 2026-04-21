package com.machina.client.screen;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.util.PlanetHelper;
import com.machina.api.util.StringUtils;
import com.machina.api.util.math.VecUtil;
import com.machina.weather.WeatherEvent;
import com.machina.weather.manager.ClientWeatherManager;
import com.machina.weather.system.ClientWeatherSystem;
import com.machina.weather.system.ServerWeatherSystem;
import com.machina.weather.system.ServerWeatherSystem.RoughTemperatureRange;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

public class MeteoProbeScreen extends Screen {

	private static final int RING_COLOR = 0xAA7DA3BC;
	private static final int MAJOR_AXIS_COLOR = 0xFFC5D8E8;
	private static final int MINOR_AXIS_COLOR = 0x806E8799;
	private static final int WIND_ARROW_COLOR = 0xFF00FEFE;
	private static final int DAY_COLOR = 0xF6C453;
	private static final int NIGHT_COLOR = 0x6EA6FF;
	private static final int SPRING_COLOR = 0x66C85A;
	private static final int SUMMER_COLOR = 0xFFC05A;
	private static final int AUTUMN_COLOR = 0xD88945;
	private static final int WINTER_COLOR = 0x8EC9F6;
	private static final int RANGE_LEFT_COLOR = MUI.ACC_1;
	private static final int RANGE_RIGHT_COLOR = MUI.ACC_2;
	private static final int TEMP_HOT_COLOR = 0xFF7A3A;
	private static final int TEMP_COLD_COLOR = 0x6EA6FF;
	private static final float EARTH_TEMP = 288.0F;

	private static final double MC_DAY_TICKS = 24000.0D;
	private static final double MIN_SIM_SEASON_TICKS = MC_DAY_TICKS * 12.0D;
	private static final double MAX_SIM_SEASON_TICKS = MC_DAY_TICKS * 360.0D;

	private static final String[] SEASONS = { "spring", "summer", "autumn", "winter" };

	private static interface ProbeTabDisplay {
		int getIconX();

		Component getName();

		void render(@NotNull GuiGraphics gui, Context ctx, int mouseX, int mouseY, int i, int j);
	}

	private final ProbeTabDisplay SUMMARY = new ProbeTabDisplay() {
		@Override
		public void render(@NotNull GuiGraphics gui, Context ctx, int mouseX, int mouseY, int i, int j) {
			Component c = Component.literal(": ");
			drawSummaryProfile(gui, i, j, ctx);
			MUI.drawString(gui, MUI.uistr("meteo_probe.summary.time").append(c).append(
					Component.literal(ctx.clockTime).withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))), i + 8,
					j + 30);
			MUI.drawString(gui,
					MUI.uistr("meteo_probe.summary.daynight").append(c)
							.append(MUI.uistr(ctx.isDay ? "meteo_probe.time.day" : "meteo_probe.time.night")
									.withStyle(Style.EMPTY.withBold(true).withColor(getDayNightColor(ctx.isDay)))),
					i + 8, j + 42);
			MUI.drawString(gui,
					MUI.uistr("meteo_probe.summary.season").append(c)
							.append(Component.translatable(ctx.seasonKey)
									.withStyle(Style.EMPTY.withBold(true).withColor(getSeasonColor(ctx.seasonKey)))),
					i + 8, j + 54);
			MUI.drawString(gui,
					MUI.uistr("meteo_probe.weather.current").append(c)
							.append(getWeatherDisplayName(ctx.weatherEvent)
									.copy().withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_2))),
					i + 8, j + 66);
			MUI.drawString(gui,
					MUI.uistr("meteo_probe.summary.wind").append(c)
							.append(Component.literal(VecUtil.vec2ToCardinal(ctx.wind.x, ctx.wind.y))
									.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_2)))
							.append(Component.literal(" " + String.format("(%.2f)", ctx.windIntensity))
									.withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))),
					i + 8, j + 78);
			MUI.drawString(gui, MUI.uistr("meteo_probe.summary.temperature").append(c)
					.append(Component.literal(StringUtils.formatTemp(ctx.currentTemperature)).withStyle(
							Style.EMPTY.withBold(true).withColor(getCurrentTemperatureColor(ctx.currentTemperature)))),
					i + 8, j + 90);
			MUI.drawString(gui,
					MUI.uistr("meteo_probe.summary.today_range").append(c).append(formatRange(ctx.dayMin, ctx.dayMax)),
					i + 8, j + 102);
			MUI.drawString(gui,
					MUI.uistr("meteo_probe.summary.year_range").append(c).append(formatRange(ctx.yearMin, ctx.yearMax)),
					i + 8, j + 114);
		}

		@Override
		public Component getName() {
			return MUI.uistr("meteo_probe.tab.summary");
		}

		@Override
		public int getIconX() {
			return 0;
		}
	};

	private final ProbeTabDisplay TIME = new ProbeTabDisplay() {
		@Override
		public void render(@NotNull GuiGraphics gui, Context ctx, int mouseX, int mouseY, int i, int j) {
			Component c = Component.literal(": ");
			MUI.drawString(gui, MUI.uistr("meteo_probe.time.clock").append(c).append(
					Component.literal(ctx.clockTime).withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))), i + 8,
					j + 30);
			MUI.drawString(gui,
					MUI.uistr("meteo_probe.time.phase").append(c)
							.append(MUI.uistr(ctx.isDay ? "meteo_probe.time.day" : "meteo_probe.time.night")
									.withStyle(Style.EMPTY.withBold(true).withColor(getDayNightColor(ctx.isDay)))),
					i + 8, j + 42);
			MUI.drawString(gui,
					MUI.uistr("meteo_probe.time.season").append(c)
							.append(Component.translatable(ctx.seasonKey)
									.withStyle(Style.EMPTY.withBold(true).withColor(getSeasonColor(ctx.seasonKey)))),
					i + 8, j + 54);

			int barX = i + 10;
			int barY = j + 80;
			int barW = 210;
			int barH = 10;
			int splitX = barX + barW / 2;
			int markerX = barX + (int) (ctx.dayTick / 24000.0F * barW);

			MUI.drawString(gui, MUI.uistr("meteo_probe.time.cycle"), barX, barY - 11);
			gui.fill(barX, barY, splitX, barY + barH, 0x889D8B2E);
			gui.fill(splitX, barY, barX + barW, barY + barH, 0x88516A92);
			gui.fill(barX, barY, barX + barW, barY + 1, 0xFFD6E4EF);
			gui.fill(barX, barY + barH - 1, barX + barW, barY + barH, 0xFFD6E4EF);
			gui.fill(barX, barY, barX + 1, barY + barH, 0xFFD6E4EF);
			gui.fill(barX + barW - 1, barY, barX + barW, barY + barH, 0xFFD6E4EF);
			drawCycleTicks(gui, barX, barY, barW, barH);
			gui.fill(markerX - 1, barY - 3, markerX + 1, barY + barH + 3, 0xFF00FEFE);

			drawCycleLabel(gui, MUI.uistr("meteo_probe.time.noon"), barX + barW / 4, barY + 16, barX, barX + barW);
			drawCycleLabel(gui, MUI.uistr("meteo_probe.time.sunset"), splitX, barY + 16, barX, barX + barW);
			drawCycleLabel(gui, MUI.uistr("meteo_probe.time.midnight"), barX + (barW * 3 / 4), barY + 16, barX,
					barX + barW);
		}

		@Override
		public Component getName() {
			return MUI.uistr("meteo_probe.tab.time");
		}

		@Override
		public int getIconX() {
			return 16;
		}
	};

	private final ProbeTabDisplay WIND = new ProbeTabDisplay() {
		@Override
		public void render(@NotNull GuiGraphics gui, Context ctx, int mouseX, int mouseY, int i, int j) {
			int cx = i + 235 / 2;
			int cy = j - 3 + 146 / 2;
			int radius = 40;

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

			drawCardinal(gui, "N", cx - font.width("N") / 2, cy - radius - 10);
			drawCardinal(gui, "S", cx - font.width("S") / 2, cy + radius + 2);
			drawCardinal(gui, "W", cx - radius - 9, cy - 5);
			drawCardinal(gui, "E", cx + radius + 3, cy - 5);

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
					MUI.uistr("meteo_probe.wind.direction").append(Component.literal(": "))
							.append(Component.literal(String.format("%.2f", wind.x))
									.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1)))
							.append(Component.literal("x "))
							.append(Component.literal(String.format("%.2f", wind.y))
									.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1)))
							.append(Component.literal("z")),
					i + 4, j + 126, MUI.CYAN);
			MUI.drawString(gui,
					MUI.uistr("meteo_probe.wind.intensity").append(Component.literal(": "))
							.append(Component.literal(String.format("%.2f", intensity))
									.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_2))),
					i + 4, j + 114, MUI.CYAN);
		}

		@Override
		public Component getName() {
			return MUI.uistr("meteo_probe.tab.wind");
		}

		@Override
		public int getIconX() {
			return 32;
		}
	};

	private final ProbeTabDisplay TEMPERATURE = new ProbeTabDisplay() {
		@Override
		public void render(@NotNull GuiGraphics gui, Context ctx, int mouseX, int mouseY, int i, int j) {
			Component c = Component.literal(": ");
			MUI.drawString(gui, MUI.uistr("meteo_probe.temp.current").append(c)
					.append(Component.literal(StringUtils.formatTemp(ctx.currentTemperature)).withStyle(
							Style.EMPTY.withBold(true).withColor(getCurrentTemperatureColor(ctx.currentTemperature)))),
					i + 8, j + 30);
			MUI.drawString(gui,
					MUI.uistr("meteo_probe.temp.day_range").append(c).append(formatRange(ctx.dayMin, ctx.dayMax)),
					i + 8, j + 42);
			MUI.drawString(gui,
					MUI.uistr("meteo_probe.temp.year_range").append(c).append(formatRange(ctx.yearMin, ctx.yearMax)),
					i + 8, j + 54);

			MUI.drawString(gui, MUI.uistr("meteo_probe.temp.today"), i + 8, j + 70);
			drawTemperatureBar(gui, i + 8, j + 82, 214, ctx.dayMin, ctx.dayMax, ctx.currentTemperature, 0xA000FF88,
					0xFF00FFCC);

			MUI.drawString(gui, MUI.uistr("meteo_probe.temp.year"), i + 8, j + 96);
			drawTemperatureBar(gui, i + 8, j + 108, 214, ctx.yearMin, ctx.yearMax, ctx.currentTemperature, 0xA0E600FF,
					0xFFE6AAFF);
		}

		@Override
		public Component getName() {
			return MUI.uistr("meteo_probe.tab.temperature");
		}

		@Override
		public int getIconX() {
			return 48;
		}
	};

	private final List<ProbeTabDisplay> TABS = List.of(SUMMARY, TIME, WIND, TEMPERATURE);

	protected int imageWidth = 235;
	protected int imageHeight = 146;

	private int selected = 0;
	private Vec2 smoothedWind = new Vec2(0.0F, 0.0F);

	public MeteoProbeScreen() {
		super(Component.empty());
	}

	public static void open() {
		Minecraft mc = Minecraft.getInstance();
		mc.setScreen(new MeteoProbeScreen());
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
		ProbeTabDisplay sel = getSelectedTab();
		Context ctx = createContext();

		MUI.blitCommon(gui, i, j, 179, 94, 235, 146);
		MUI.drawStringVertical(gui, MUI.uistr("meteo_probe"), i + 245, j);
		if (ctx == null) {
			MUI.drawCenteredString(gui, MUI.uistr("meteo_probe.no_data").withStyle(Style.EMPTY.withBold(true)),
					i + this.imageWidth / 2, j + this.imageHeight / 2 - 4);
		} else {
			sel.render(gui, ctx, mouseX, mouseY, i, j + 4);
		}

		renderTabs(gui, mouseX, mouseY, i, j);
	}

	private void renderTabs(GuiGraphics gui, int mouseX, int mouseY, int i, int j) {
		int headerX = i + 57;
		int headerY = j + 2;
		MUI.blitRocket(gui, headerX, headerY, 253, 0, 121, 21);

		for (int idx = 0; idx < TABS.size(); idx++) {
			ProbeTabDisplay tab = TABS.get(idx);
			int x = headerX + 9 + idx * 28;
			int y = headerY + 1;
			boolean hovered = mouseX >= x && mouseX <= x + 18 && mouseY >= y && mouseY <= y + 18;
			if (selected != idx && !hovered) {
				MUI.blitRocket(gui, x, y, 235, 0, 18, 18);
			} else {
				MUI.blitRocket(gui, x, y, 235, 18, 18, 18);
			}
			MUI.blitRocket(gui, x + 1, y + 1, 128 + tab.getIconX(), 304, 16, 16);

			if (hovered) {
				MUI.renderTooltip(gui, mouseX, mouseY, tab.getName());
			}
		}
	}

	private void drawTemperatureBar(GuiGraphics gui, int x, int y, int width, float min, float max, float current,
			int fillColor, int markerColor) {
		if (max - min < 1.0E-4F) {
			max = min + 1.0F;
		}
		gui.fill(x, y, x + width, y + 8, 0xA0182733);
		int fillWidth = (int) (width * Mth.clamp((max - min) / (max - min), 0.0F, 1.0F));
		gui.fill(x, y + 1, x + fillWidth, y + 7, fillColor);
		int markerX = x + (int) (Mth.clamp((current - min) / (max - min), 0.0F, 1.0F) * width);
		gui.fill(markerX - 1, y - 2, markerX + 1, y + 10, markerColor);
		gui.fill(x, y, x + width, y + 1, 0xFFD6E4EF);
		gui.fill(x, y + 7, x + width, y + 8, 0xFFD6E4EF);
	}

	private void drawSummaryProfile(GuiGraphics gui, int i, int j, Context ctx) {
		int width = 96;
		int height = 34;
		int x = i + this.imageWidth - width - 8;
		int y = j + 28;

		gui.fill(x - 1, y - 1, x + width + 1, y + height + 1, 0xCC4E6E86);
		gui.fill(x, y, x + width, y + height, 0xB0162330);

		int iconSize = 16;
		int iconX = x + 8;
		int iconY = y + (height - iconSize) / 2;
		gui.pose().pushPose();
		gui.pose().scale(2.0f, 2.0f, 1.0f);
		ctx.planet.drawIcon(gui, iconX / 2 + iconSize / 4, iconY / 2 + iconSize / 4, 1.0f);
		gui.pose().popPose();

		int textStartX = iconX + iconSize + 6;
		String clipped = font.plainSubstrByWidth(ctx.planet.name(), width - (textStartX - x) - 4);
		Component planetName = Component.literal(clipped).withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE));
		int textX = Math.max(textStartX, x + width - 4 - font.width(planetName));
		int textY = y + (height - font.lineHeight) / 2;
		MUI.drawString(gui, planetName, textX, textY);
	}

	private void drawCycleTicks(GuiGraphics gui, int x, int y, int w, int h) {
		for (int tick = 0; tick <= 24; tick++) {
			int tx = x + (w * tick) / 24;
			boolean major = tick % 6 == 0;
			int top = y + (major ? -2 : 0);
			int bottom = y + h + (major ? 2 : 1);
			gui.fill(tx, top, tx + 1, bottom, major ? 0xFFD6E4EF : 0x88D6E4EF);
		}
	}

	private void drawCycleLabel(GuiGraphics gui, Component label, int centerX, int y, int minX, int maxX) {
		int w = font.width(label);
		int x = Mth.clamp(centerX - w / 2, minX, maxX - w);
		MUI.drawString(gui, label, x, y, MUI.CYAN);
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

	private Context createContext() {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) {
			return null;
		}
		ClientLevel level = mc.level;
		Planet planet = PlanetHelper.getPlanetFor(level);
		ClientWeatherSystem system = ClientWeatherManager.getSystem(level);
		if (planet == null || system == null) {
			return null;
		}

		Vec2 wind = system.getWindDirection();
		if (wind == null) {
			wind = new Vec2(0.0F, 0.0F);
		}
		float intensity = (float) Math.sqrt(wind.x * wind.x + wind.y * wind.y);

		long dayTick = Math.floorMod(level.getDayTime(), 24000L);
		String clock = minecraftClock(dayTick);
		boolean isDay = dayTick < 12000L;

		float currentTemperature = system.getTemperature();
		RoughTemperatureRange range = ServerWeatherSystem.calculateRoughTemperatureRange(planet);
		float seasonalSignal = computeSeasonalSignal(planet, level.getGameTime());
		float dayMin = Mth.clamp(range.reference() + seasonalSignal * range.seasonalSwing() - range.dayNightSwing(),
				range.min(), range.max());
		float dayMax = Mth.clamp(range.reference() + seasonalSignal * range.seasonalSwing() + range.dayNightSwing(),
				range.min(), range.max());
		WeatherEvent weatherEvent = system.getCurrentEvent();

		return new Context(wind, intensity, dayTick, clock, isDay, seasonTranslationKey(planet, level.getGameTime()),
				currentTemperature, dayMin, dayMax, range.min(), range.max(), weatherEvent, planet);
	}

	private Component getWeatherDisplayName(WeatherEvent event) {
		if (event == null) {
			return MUI.uistr("meteo_probe.weather.unknown.name");
		}
		return Component.translatable(event.getDescriptionId());
	}

	private String minecraftClock(long dayTick) {
		long shifted = (dayTick + 6000L) % 24000L;
		int hours = (int) (shifted / 1000L);
		int minutes = (int) ((shifted % 1000L) * 60L / 1000L);
		return String.format("%02d:%02d", hours, minutes);
	}

	private String seasonTranslationKey(Planet planet, long gameTime) {
		return "season." + planet.type().name().getPath() + "." + SEASONS[seasonIndex(planet, gameTime)];
	}

	private int seasonIndex(Planet planet, long gameTime) {
		double seasonTicks = getSimulatedSeasonTicks(planet);
		double meanMotion = (Math.PI * 2.0D) / seasonTicks;
		double meanAnomaly = meanMotion * gameTime + planet.where_in_orbit();
		double trueAnomaly = planet.trueAnomalyFromMean(meanAnomaly, Math.min(Math.max(planet.e(), 0.0D), 0.99D));
		double normalized = ((trueAnomaly % (Math.PI * 2.0D)) + (Math.PI * 2.0D)) % (Math.PI * 2.0D);
		return (int) (normalized / (Math.PI * 2.0D) * 4.0D) % 4;
	}

	private float computeSeasonalSignal(Planet planet, long gameTime) {
		double seasonTicks = getSimulatedSeasonTicks(planet);
		double meanMotion = (Math.PI * 2.0D) / seasonTicks;
		double meanAnomaly = meanMotion * gameTime + planet.where_in_orbit();
		double trueAnomaly = planet.trueAnomalyFromMean(meanAnomaly, Math.min(Math.max(planet.e(), 0.0D), 0.99D));
		double phaseBias = Math.toRadians(planet.axial_tilt()) * 0.25D;
		return (float) Math.sin(trueAnomaly + phaseBias);
	}

	private double getSimulatedSeasonTicks(Planet planet) {
		double periodDays = planet.orb_period();
		if (!Double.isFinite(periodDays) || periodDays <= 0.0D) {
			periodDays = 365.0D;
		}
		double rawTicks = periodDays * MC_DAY_TICKS;
		return Mth.clamp(rawTicks, MIN_SIM_SEASON_TICKS, MAX_SIM_SEASON_TICKS);
	}

	private int getDayNightColor(boolean isDay) {
		return isDay ? DAY_COLOR : NIGHT_COLOR;
	}

	private int getCurrentTemperatureColor(float temperature) {
		return temperature < EARTH_TEMP ? TEMP_COLD_COLOR : TEMP_HOT_COLOR;
	}

	private Component formatRange(float min, float max) {
		return Component.literal(StringUtils.formatTemp(min))
				.withStyle(Style.EMPTY.withBold(true).withColor(RANGE_LEFT_COLOR))
				.append(Component.literal(" - ").withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE)))
				.append(Component.literal(StringUtils.formatTemp(max))
						.withStyle(Style.EMPTY.withBold(true).withColor(RANGE_RIGHT_COLOR)));
	}

	private int getSeasonColor(String seasonKey) {
		if (seasonKey.endsWith(".spring")) {
			return SPRING_COLOR;
		}
		if (seasonKey.endsWith(".summer")) {
			return SUMMER_COLOR;
		}
		if (seasonKey.endsWith(".autumn")) {
			return AUTUMN_COLOR;
		}
		if (seasonKey.endsWith(".winter")) {
			return WINTER_COLOR;
		}
		return MUI.ACC_2;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0) {
			int i = midWidth();
			int j = midHeight();
			int headerX = i + 57;
			int headerY = j + 2;
			for (int idx = 0; idx < TABS.size(); idx++) {
				int x = headerX + 9 + idx * 28;
				int y = headerY + 1;
				if (mouseX >= x && mouseX <= x + 18 && mouseY >= y && mouseY <= y + 18) {
					if (this.selected != idx) {
						this.selected = idx;
						MUI.click();
					}
					return true;
				}
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private ProbeTabDisplay getSelectedTab() {
		return TABS.get(this.selected);
	}

	private record Context(Vec2 wind, float windIntensity, long dayTick, String clockTime, boolean isDay,
			String seasonKey, float currentTemperature, float dayMin, float dayMax, float yearMin, float yearMax,
			WeatherEvent weatherEvent, Planet planet) {
	}
}
