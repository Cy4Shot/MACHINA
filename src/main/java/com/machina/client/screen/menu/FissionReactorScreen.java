package com.machina.client.screen.menu;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MUI.MuiSlot;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.network.c2s.C2SFissionReactorControl;
import com.machina.api.network.c2s.C2SFissionReactorControl.Action;
import com.machina.api.util.StringUtils;
import com.machina.block.entity.machine.fission_reactor.FissionReactorControllerBlockEntity;
import com.machina.block.entity.machine.fission_reactor.FissionReactorControllerBlockEntity.MeltdownCause;
import com.machina.block.entity.machine.fission_reactor.FissionReactorControllerBlockEntity.ReactorState;
import com.machina.block.menu.FissionReactorMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class FissionReactorScreen extends MachinaMenuScreen<FissionReactorMenu> {

	public FissionReactorScreen(FissionReactorMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		FissionReactorControllerBlockEntity entity = this.entity();

		drawInventory(gui, mx, my);
		drawBackground(gui);

		int i = midWidth();
		int j = midHeight();

		if (entity == null || !entity.formed) {
			MUI.drawCenteredString(gui, Component.translatable("gui.machina.fission.not_formed"), i + 117, j - 30, MUI.RED);
			MUI.drawCenteredString(gui, Component.translatable("gui.machina.fission.build_structure"), i + 117, j - 15, MUI.WHITE);
			drawOverlay(gui);
			return;
		}

		ReactorState state = this.menu.getReactorState();

		// Left side: IO
		drawFissionSlotUp(gui, 0, mx, my, 16, -62, MuiSlot.ROD, "fission.fuel_input");
		drawReactorFluidBar(gui, 17, -32);
		drawFissionSlotDown(gui, 1, mx, my, 16, 20, MuiSlot.DUST, "fission.waste_output");

		// Status panel
		drawStatusPanel(gui, i + 55, j - 58, state, entity.getActiveFuel());
		drawSteamOutput(gui, i + 55, j - 27, mx, my);

		// Only show reactivity when reactor is active or fuel is in slot
		boolean hasFuel = !this.menu.getSlot(0).getItem().isEmpty();
		boolean reactorActive = state == ReactorState.RUNNING || state == ReactorState.STARTING || state == ReactorState.MELTDOWN_WARNING;
		if (hasFuel || reactorActive) {
			drawReactivityGauge(gui, i + 55, j + 15, mx, my);
		}

		// Control buttons
		drawControlButtons(gui, mx, my, i + 75, j + 50, state);
		drawEnergyBarSmall(gui, -82, 60, entity.getEnergyF() > 0, "fission.no_power");

		// Burn progress when running
		if (state == ReactorState.RUNNING || state == ReactorState.MELTDOWN_WARNING || state == ReactorState.STARTING) {
			drawBurnProgress(gui, i + 144, j - 27, mx, my);
		}

		// Startup progress
		if (state == ReactorState.STARTING) {
			drawStartupProgress(gui, i + 144, j + 15, mx, my);
		}

		// Meltdown warning
		if (state == ReactorState.MELTDOWN_WARNING) {
			drawMeltdownWarning(gui, i + 144, j + 15, mx, my);
		}


		drawOverlay(gui);
	}

	private void drawReactivityGauge(GuiGraphics gui, int x, int y, int mx, int my) {
		MUI.drawString(gui, Component.translatable("gui.machina.fission.reactivity"), x, y - 8, MUI.CYAN);

		gui.fill(x, y, x + 75, y + 12, 0xFF101010);
		gui.fill(x + 1, y + 1, x + 74, y + 11, 0xFF202020);

		float reactivity = this.menu.getReactivity();
		float minAllowed = this.menu.getMinReactivity();
		float maxAllowed = this.menu.getMaxReactivity();

		float minGauge = 0.990f;
		float maxGauge = 1.010f;
		float range = maxGauge - minGauge;

		int minOptX = x + 2 + (int) (((minAllowed - minGauge) / range) * 71);
		int maxOptX = x + 2 + (int) (((maxAllowed - minGauge) / range) * 71);
		gui.fill(minOptX, y + 2, maxOptX, y + 10, 0xFF006600);

		int reactivityX = x + 2 + (int) (((reactivity - minGauge) / range) * 71);
		reactivityX = Math.max(x + 2, Math.min(x + 72, reactivityX));

		int indicatorColor;
		if (reactivity >= minAllowed && reactivity <= maxAllowed) {
			indicatorColor = 0xFF00FF00;
		} else if (reactivity > maxAllowed) {
			indicatorColor = 0xFFFF0000;
		} else {
			indicatorColor = 0xFFFFFF00;
		}

		gui.fill(reactivityX - 1, y + 1, reactivityX + 2, y + 11, indicatorColor);

		MUI.drawString(gui, Component.literal(String.format("%.4f", reactivity)), x, y + 14,
				reactivity >= minAllowed && reactivity <= maxAllowed ? MUI.GREEN : MUI.RED);

		registerHoverable("reactivity_gauge", x, y, x + 75, y + 12,
				() -> Component.translatable("gui.machina.fission.reactivity_tooltip",
					String.format("%.5f", this.menu.getReactivity()),
					String.format("%.4f", this.menu.getOptimalReactivity()),
					String.format("%.4f", this.menu.getReactivityTolerance())));
	}

	private void drawStatusPanel(GuiGraphics gui, int x, int y, ReactorState state, ItemStack activeFuel) {
		Component statusText;
		int statusColor;

		switch (state) {
		case IDLE:
			statusText = Component.translatable("gui.machina.fission.status.idle");
			statusColor = MUI.CYAN;
			break;
		case STARTING:
			if (!activeFuel.isEmpty()) {
				statusText = Component.translatable("gui.machina.fission.status.starting_fuel", activeFuel.getHoverName());
			} else {
				statusText = Component.translatable("gui.machina.fission.status.starting");
			}
			statusColor = MUI.ACC_1;
			break;
		case RUNNING:
			if (!activeFuel.isEmpty()) {
				statusText = Component.translatable("gui.machina.fission.status.active_fuel", activeFuel.getHoverName());
			} else {
				statusText = Component.translatable("gui.machina.fission.status.running");
			}
			statusColor = MUI.GREEN;
			break;
		case SCRAMMED:
			statusText = Component.translatable("gui.machina.fission.status.scrammed");
			statusColor = MUI.RED;
			break;
		case MELTDOWN_WARNING:
			if ((System.currentTimeMillis() / 250) % 2 == 0) {
				statusText = Component.translatable("gui.machina.fission.status.meltdown");
				statusColor = 0xFFFF0000;
			} else {
				statusText = Component.translatable("gui.machina.fission.status.warning");
				statusColor = 0xFFFFFF00;
			}
			break;
		default:
			statusText = Component.translatable("gui.machina.fission.status.unknown");
			statusColor = MUI.RED;
		}

		MUI.drawString(gui, statusText, x, y, statusColor);
	}

	private void drawBurnProgress(GuiGraphics gui, int x, int y, int mx, int my) {
		float burnProgress = this.menu.getBurnProgress();

		MUI.drawString(gui, Component.translatable("gui.machina.fission.fuel_burn"), x, y - 8, MUI.CYAN);

		gui.fill(x, y, x + 75, y + 12, 0xFF101010);
		gui.fill(x + 1, y + 1, x + 74, y + 11, 0xFF202020);

		int fillWidth = (int) (burnProgress * 71);
		gui.fill(x + 2, y + 2, x + 2 + fillWidth, y + 10, 0xFF555500);

		int indicatorX = x + 2 + fillWidth;
		indicatorX = Math.max(x + 2, Math.min(x + 72, indicatorX));

		int indicatorColor = burnProgress >= 1.0f ? 0xFFFFFF00 : 0xFFAAAA00;
		gui.fill(indicatorX - 1, y + 1, indicatorX + 2, y + 11, indicatorColor);

		MUI.drawString(gui, Component.literal(StringUtils.formatPercent(burnProgress)), x, y + 14,
				burnProgress > 0.5f ? MUI.GREEN : MUI.ACC_1);

		registerHoverable("burn_gauge", x, y, x + 75, y + 12,
				() -> Component.translatable("gui.machina.fission.fuel_burn_tooltip", StringUtils.formatPercent(this.menu.getBurnProgress())));
	}

	private void drawSteamOutput(GuiGraphics gui, int x, int y, int mx, int my) {
		int currentSteam = this.menu.getCurrentSteamRate();
		int maxSteam = this.menu.getMaxSteamRate();

		MUI.drawString(gui, Component.translatable("gui.machina.fission.steam_output"), x, y - 8, MUI.CYAN);

		gui.fill(x, y, x + 75, y + 12, 0xFF101010);
		gui.fill(x + 1, y + 1, x + 74, y + 11, 0xFF202020);

		if (maxSteam > 0) {
			int fillWidth = (int) ((float) currentSteam / maxSteam * 71);
			gui.fill(x + 2, y + 2, x + 2 + fillWidth, y + 10, 0xFF005555);

			int indicatorX = x + 2 + fillWidth;
			indicatorX = Math.max(x + 2, Math.min(x + 72, indicatorX));

			int indicatorColor = currentSteam >= maxSteam ? 0xFF00FFFF : 0xFF00AAAA;
			gui.fill(indicatorX - 1, y + 1, indicatorX + 2, y + 11, indicatorColor);
		}

		String steamStr = maxSteam > 0 ? currentSteam + "/" + maxSteam + " mB/t" : "0 mB/t";
		int steamColor = (maxSteam > 0 && currentSteam >= maxSteam) ? MUI.GREEN : MUI.WHITE;
		MUI.drawString(gui, Component.literal(steamStr), x, y + 14, steamColor);

		registerHoverable("steam_gauge", x, y, x + 75, y + 12,
				() -> Component.translatable("gui.machina.fission.steam_tooltip", this.menu.getCurrentSteamRate(), this.menu.getMaxSteamRate()));
	}

	private void drawControlButtons(GuiGraphics gui, int mx, int my, int x, int y, ReactorState state) {
		boolean canStart = this.menu.canStart();
		boolean canScram = this.menu.canScram();

		// START button with red ears when disabled
		drawReactorButton(gui, mx, my, x - midWidth(), y - midHeight(), MuiSlot.TICK, canStart,
				() -> {
					PacketDistributor.sendToServer(new C2SFissionReactorControl(
							this.<FissionReactorControllerBlockEntity>entity().getBlockPos(), Action.START));
					MUI.click();
				},
				() -> {
					FissionReactorControllerBlockEntity e = this.entity();
					if (this.menu.canStart()) {
						int required = e != null ? e.getStartupEnergyRequired() : 0;
						return Component.translatable("gui.machina.fission.start_tooltip", StringUtils.formatPower(required));
					} else if (this.menu.getReactorState() != ReactorState.IDLE) {
						return Component.translatable("gui.machina.fission.already_active");
					} else {
						int required = e != null ? e.getStartupEnergyRequired() : 0;
						int current = e != null ? e.getEnergy() : 0;
						if (required > 0 && current < required) {
							return Component.translatable("gui.machina.fission.need_power", StringUtils.formatPower(required), StringUtils.formatPower(current));
						}
						return Component.translatable("gui.machina.fission.insert_fuel");
					}
				});

		// SCRAM button with red ears when disabled
		drawReactorButton(gui, mx, my, x + 36 - midWidth(), y - midHeight(), MuiSlot.CROSS_R, canScram,
				() -> {
					PacketDistributor.sendToServer(new C2SFissionReactorControl(
							this.<FissionReactorControllerBlockEntity>entity().getBlockPos(), Action.SCRAM));
					MUI.click();
				},
				() -> this.menu.canScram() ? Component.translatable("gui.machina.fission.scram_tooltip")
						: Component.translatable("gui.machina.fission.not_running"));
	}

	private void drawReactorButton(GuiGraphics gui, int mx, int my, int x, int y, MuiSlot slot, boolean active,
			Runnable onClick, Supplier<Component> onHover) {
		String key = "reactor_button_" + x + "_" + y;

		int i = midWidth() + x;
		int j = midHeight() + y;
		int h = active && mx > i && mx < i + 18 && my > j && my < j + 18 ? 113 : 94;
		MUI.blitCommon(gui, i, j, 466, h, 19, 19);
		slot.draw(gui, i + 4, j + 4, this.aliveTicks);

		int earOffset = active ? 0 : 6;
		MUI.blitCommon(gui, i - 6, j + 1, 387 + earOffset, 0, 3, 16);
		MUI.blitCommon(gui, i + 21, j + 1, 390 + earOffset, 0, 3, 16);

		clickAndHover(key, i, j, i + 18, j + 18, () -> active, onHover, onClick);
	}

	private void drawStartupProgress(GuiGraphics gui, int x, int y, int mx, int my) {
		float progress = this.menu.getStartupPercent();

		MUI.drawString(gui, Component.translatable("gui.machina.fission.startup"), x, y - 8, MUI.ACC_1);

		gui.fill(x, y, x + 75, y + 12, 0xFF101010);
		gui.fill(x + 1, y + 1, x + 74, y + 11, 0xFF202020);

		int fillWidth = (int) (progress * 71);
		gui.fill(x + 2, y + 2, x + 2 + fillWidth, y + 10, 0xFF005522);

		int indicatorX = x + 2 + fillWidth;
		indicatorX = Math.max(x + 2, Math.min(x + 72, indicatorX));
		gui.fill(indicatorX - 1, y + 1, indicatorX + 2, y + 11, 0xFF00FF88);

		MUI.drawString(gui, Component.literal(StringUtils.formatPercent(progress)), x, y + 14, MUI.ACC_1);

		registerHoverable("startup_gauge", x, y, x + 75, y + 12,
				() -> Component.translatable("gui.machina.fission.startup_tooltip", StringUtils.formatPercent(this.menu.getStartupPercent())));
	}

	private void drawMeltdownWarning(GuiGraphics gui, int x, int y, int mx, int my) {
		float countdown = this.menu.getMeltdownPercent();
		int timerSeconds = (int) (this.menu.getMeltdownTimer() / 20.0f);
		MeltdownCause cause = this.menu.getMeltdownCause();

		int labelColor = (System.currentTimeMillis() / 250) % 2 == 0 ? 0xFFFF0000 : 0xFFFF8800;
		MUI.drawString(gui, Component.translatable("gui.machina.fission.meltdown_label"), x, y - 8, labelColor);

		gui.fill(x, y, x + 75, y + 12, 0xFF101010);
		gui.fill(x + 1, y + 1, x + 74, y + 11, 0xFF202020);

		int fillWidth = (int) (countdown * 71);
		int barColor = (System.currentTimeMillis() / 100) % 2 == 0 ? 0xFF550000 : 0xFF553300;
		gui.fill(x + 2, y + 2, x + 2 + fillWidth, y + 10, barColor);

		int indicatorX = x + 2 + fillWidth;
		indicatorX = Math.max(x + 2, Math.min(x + 72, indicatorX));
		int indicatorColor = (System.currentTimeMillis() / 100) % 2 == 0 ? 0xFFFF0000 : 0xFFFF8800;
		gui.fill(indicatorX - 1, y + 1, indicatorX + 2, y + 11, indicatorColor);

		String warningKey = cause == MeltdownCause.NO_WATER ? "gui.machina.fission.meltdown_timer_water" : "gui.machina.fission.meltdown_timer_rods";
		MUI.drawString(gui, Component.translatable(warningKey, timerSeconds), x, y + 14, 0xFFFFFF00);

		registerHoverable("meltdown_gauge", x, y, x + 75, y + 12,
				() -> Component.translatable("gui.machina.fission.meltdown_tooltip", (int) (this.menu.getMeltdownTimer() / 20.0f)));
	}

	private void drawReactorFluidBar(GuiGraphics gui, int x, int y) {
		FissionReactorControllerBlockEntity entity = this.entity();
		if (entity != null) {
			drawBarVert(gui, x, y, this.menu.getWaterBar(), (ii, jj, p) -> {
				float prop = this.menu.getWaterPercent();
				MUI.renderFluid(gui, entity.getFluid(0), ii + 1, jj + 41, 14, (int) (40 * prop), 0);
			});
		}
	}

	private void drawFissionSlotUp(GuiGraphics gui, int slotIndex, int mx, int my, int x, int y, MuiSlot slot, String hover) {
		int i = midWidth() + x;
		int j = midHeight() + y;
		int h = mx > i && mx < i + 19 && my > j && my < j + 21 ? 115 : 94;
		MUI.blitCommon(gui, i, j, 433, h, 19, 21);

		boolean isEmpty = this.menu.getSlot(slotIndex).getItem().isEmpty();
		if (isEmpty) {
			slot.draw(gui, i + 4, j + 4, this.aliveTicks);
		}

		MUI.blitCommon(gui, i - 6, j + 1, 387, 0, 3, 16);
		MUI.blitCommon(gui, i + 21, j + 1, 390, 0, 3, 16);

		if (!hover.isEmpty()) {
			registerHoverable("fission_slot_up_" + x + "_" + y, i - 1, j + 1, i + 18, j + 20,
					() -> this.menu.getSlot(slotIndex).getItem().isEmpty() ? MUI.uistr(hover) : null);
		}
	}

	private void drawFissionSlotDown(GuiGraphics gui, int slotIndex, int mx, int my, int x, int y, MuiSlot slot, String hover) {
		int i = midWidth() + x;
		int j = midHeight() + y;
		int h = mx > i && mx < i + 19 && my > j && my < j + 21 ? 115 : 94;
		MUI.blitCommon(gui, i, j, 414, h, 19, 21);

		boolean isEmpty = this.menu.getSlot(slotIndex).getItem().isEmpty();
		if (isEmpty) {
			slot.draw(gui, i + 4, j + 6, this.aliveTicks);
		}

		MUI.blitCommon(gui, i - 6, j + 4, 387, 0, 3, 16);
		MUI.blitCommon(gui, i + 21, j + 4, 390, 0, 3, 16);

		if (!hover.isEmpty()) {
			registerHoverable("fission_slot_down_" + x + "_" + y, i - 1, j + 1, i + 18, j + 20,
					() -> this.menu.getSlot(slotIndex).getItem().isEmpty() ? MUI.uistr(hover) : null);
		}
	}
}
