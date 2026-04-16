package com.machina.client.screen.menu;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MUI.MuiSlot;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.network.c2s.C2SRocketRefuelingStationSetTank;
import com.machina.block.entity.machine.RocketRefuelingStationBlockEntity;
import com.machina.block.menu.RocketRefuelingStationMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class RocketRefuelingStationScreen extends MachinaMenuScreen<RocketRefuelingStationMenu> {

	public RocketRefuelingStationScreen(RocketRefuelingStationMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@SuppressWarnings("removal")
	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		drawInventory(gui, mx, my);
		drawBackground(gui);

		if (!this.menu.hasTrackedRocket()) {
			int i = midWidth();
			int j = midHeight();

			MUI.drawCenteredString(gui, MUI.uistr("rocket_refueling_station.no_rockets"), i + 118, j - 32, MUI.RED);
			MUI.drawCenteredString(gui, MUI.uistr("rocket_refueling_station.scan_hint"), i + 118, j - 0, MUI.CYAN);

			drawOverlay(gui);
			return;
		}

		drawButton(gui, mx, my, 11, -68, this.menu.isFuelTankSelected() ? MuiSlot.TEMP : MuiSlot.FLUID,
				() -> PacketDistributor.sendToServer(new C2SRocketRefuelingStationSetTank(this.menu.getBlockPos(),
						this.menu.isFuelTankSelected() ? RocketRefuelingStationBlockEntity.COOLANT_TANK
								: RocketRefuelingStationBlockEntity.FUEL_TANK)),
				() -> this.menu.isFuelTankSelected() ? MUI.uistr("rocket_refueling_station.switch_to_coolant")
						: MUI.uistr("rocket_refueling_station.switch_to_fuel"));
		drawFluidBar(gui, 0, 0, 0);

		drawUpFacingSlot(gui, 0, mx, my, 20, 30, MuiSlot.PLUS, "rocket_refueling_station.input");
		drawUpFacingSlot(gui, 1, mx, my, 197, 30, MuiSlot.MINUS, "rocket_refueling_station.output");

		int i = midWidth();
		int j = midHeight();
		BlockPos trackedPos = this.<RocketRefuelingStationBlockEntity>entity().getTrackedRocketPos();
		Component coords = trackedPos == null
				? Component.literal("X:?").withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))
						.append(Component.literal(" "))
						.append(Component.literal("Y:?").withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_2)))
						.append(Component.literal(" "))
						.append(Component.literal("Z:?").withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1)))
				: Component.literal("X:" + trackedPos.getX())
						.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))
						.append(Component.literal(" "))
						.append(Component.literal("Y:" + trackedPos.getY())
								.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_2)))
						.append(Component.literal(" "))
						.append(Component.literal("Z:" + trackedPos.getZ())
								.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1)));
		Component selectedMode = this.menu.isFuelTankSelected()
				? MUI.uistr("rocket_refueling_station.mode_fuel")
				: MUI.uistr("rocket_refueling_station.mode_coolant");
		MUI.drawCenteredString(gui, coords, i + 118, j - 57);
		MUI.drawCenteredString(gui, selectedMode.copy().withStyle(Style.EMPTY.withBold(true)), i + 118, j - 44,
				MUI.CYAN);
		MUI.blitCommon(gui, i + 27, j + 7, 508, 0, 4, 21);
		MUI.blitCommon(gui, i + 27, j - 1, 369, 80, 17, 8);

		MUI.blitCommon(gui, i + 204, j + 7, 508, 0, 4, 21);
		MUI.blitCommon(gui, i + 191, j - 1, 390, 80, 17, 8);

		FluidStack fluid = this.<RocketRefuelingStationBlockEntity>entity().getFluid(0);
		if (fluid.isEmpty()) {
			MUI.drawCenteredString(gui, MUI.uistr("tank.empty"), i + 118, j - 30, MUI.RED);
		} else {
			int col = IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor();
			MUI.drawCenteredString(gui, fluid.getDisplayName().copy().withStyle(Style.EMPTY.withBold(true)), i + 118,
					j - 30, col);
		}

		drawOverlay(gui);
	}
}
