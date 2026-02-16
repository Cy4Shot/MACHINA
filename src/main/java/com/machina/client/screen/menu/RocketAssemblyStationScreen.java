package com.machina.client.screen.menu;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MUI.MuiSlot;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.network.PacketSender;
import com.machina.api.network.c2s.C2SAssemblyStationCraft;
import com.machina.api.util.StringUtils;
import com.machina.block.entity.machine.RocketAssemblyStationBlockEntity;
import com.machina.block.menu.RocketAssemblyStationMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;

public class RocketAssemblyStationScreen extends MachinaMenuScreen<RocketAssemblyStationMenu> {

    public RocketAssemblyStationScreen(RocketAssemblyStationMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
        RocketAssemblyStationBlockEntity entity = this.entity();

        drawInventory(gui, mx, my);
        drawRocketBackground(gui);

        int i = midWidth();
        int j = midHeight();

        // Progress Mode
        if (entity.isCrafting()) {
            drawEnergyBar(gui, 0, -24, entity.hasPower(), "rocket_assembly_station.no_power");
            int x = i + 51;
            int y = j + 10;
            registerHoverable("progress_bar", x + 1, y + 1, x + 136, y + 18,
                    () -> Component.literal(StringUtils.formatPercent(entity.getProgressPercent())));
            MUI.drawBar(gui, x, y, entity.getProgressPercent(), true, MUI.uistrs("rocket_assembly_station.progress")
                    + " (" + StringUtils.formatPercent(entity.getProgressPercent()) + ")", "", (xp, yp, p) -> {
                        MUI.blitCommon(gui, xp + 2, yp + 4, 0, 404, (int) (131 * p), 14);
                    });
            return;
        }

        // Energy
        drawEnergyBarSmall(gui, 50, -60, entity.getEnergyF() == 1, "none");

        // Draw rocket ship animation
        long tick = mc.level.getGameTime() / 10;
        float alpha = 0.7f + (tick % 3) * 0.05f + (tick % 5) * 0.03f + mc.getFrameTime() / 20;
        MUI.drawWithAlpha(alpha, () -> {
            int frame = (int) (mc.level.getGameTime() / 10 % 8);
            MUI.blitRocket(gui, i + 2, j - 60, frame * 64, 158, 64, 116);
        });

        // Second Rocket Background
        MUI.blitRocket(gui, i + 106, j - 50, 0, 288, 125, 118);

        MUI.blitCommon(gui, i + 122, j - 63, 369, 80, 17, 8);
        MUI.blitCommon(gui, i + 122, j - 55, 508, 0, 4, 5);

        MUI.blitCommon(gui, i + 198, j - 63, 390, 80, 17, 8);
        MUI.blitCommon(gui, i + 211, j - 55, 508, 0, 4, 5);

        // Text
        Component c = Component.literal(": ");
        MUI.drawString(gui,
                MUI.uistr("rocket_assembly_station.mass").append(c)
                        .append(Component.literal(StringUtils.formatMass(entity.getTotalMass()))
                                .withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))),
                i + 110, j - 45);
        MUI.drawString(gui, MUI.uistr("rocket_assembly_station.fuel_type").append(c)
                .append(StringUtils.fluid(entity.getTotalFuelType().orElse(FluidStack.EMPTY), true)), i + 110, j - 35);
        MUI.drawString(gui,
                MUI.uistr("rocket_assembly_station.coolant_efficiency").append(c)
                        .append(Component.literal(StringUtils.formatPercent(entity.getTotalFuelEfficiency().orElse(0f)))
                                .withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
                i + 110, j - 25);
        MUI.drawString(gui,
                MUI.uistr("rocket_assembly_station.fuel_capacity").append(c)
                        .append(Component.literal(StringUtils.formatFluid(entity.getTotalFuelStorage().orElse(0)))
                                .withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
                i + 110, j - 15);
        MUI.drawString(gui,
                MUI.uistr("rocket_assembly_station.coolant_capacity").append(c)
                        .append(Component.literal(StringUtils.formatFluid(entity.getTotalCoolantStorage().orElse(0)))
                                .withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_2))),
                i + 110, j - 5);
        MUI.drawString(gui,
                MUI.uistr("rocket_assembly_station.coolant_type").append(c)
                        .append(StringUtils.fluid(entity.getTotalCoolantType().orElse(FluidStack.EMPTY), true)),
                i + 110, j + 5);
        MUI.drawString(gui,
                MUI.uistr("rocket_assembly_station.coolant_efficiency").append(c)
                        .append(Component
                                .literal(StringUtils.formatPercent(entity.getTotalCoolantEfficiency().orElse(0f)))
                                .withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_2))),
                i + 110, j + 15);
        MUI.drawString(gui,
                MUI.uistr("rocket_assembly_station.storage").append(c)
                        .append(Component.literal(String.valueOf(entity.getTotalSlots().orElse(0)))
                                .withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))),
                i + 110, j + 25);
        MUI.drawString(gui,
                MUI.uistr("rocket_assembly_station.max_pressure").append(c)
                        .append(Component.literal(StringUtils.formatPressure(entity.getTotalMaxPressure().orElse(0f)))
                                .withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))),
                i + 110, j + 35);

        // Slots
        MUI.drawLine(gui, i + 71, j - 52, i + 38, j - 47, true, entity.getItem(4).isEmpty() ? MUI.RED : MUI.CYAN);
        MUI.drawLine(gui, i + 71, j - 27, i + 45, j - 20, true, entity.getItem(3).isEmpty() ? MUI.RED : MUI.CYAN);
        MUI.drawLine(gui, i + 71, j - 2, i + 51, j + 3, true, entity.getItem(2).isEmpty() ? MUI.RED : MUI.CYAN);
        MUI.drawLine(gui, i + 71, j + 23, i + 41, j + 18, true, entity.getItem(1).isEmpty() ? MUI.RED : MUI.CYAN);
        MUI.drawLine(gui, i + 71, j + 48, i + 47, j + 44, true, entity.getItem(0).isEmpty() ? MUI.RED : MUI.CYAN);

        drawNoFacingSlot(gui, 4, mx, my, 79, -60, MuiSlot.PLUS, "rocket_assembly_station.shield");
        drawNoFacingSlot(gui, 3, mx, my, 79, -35, MuiSlot.PLUS, "rocket_assembly_station.life_support");
        drawNoFacingSlot(gui, 2, mx, my, 79, -10, MuiSlot.PLUS, "rocket_assembly_station.chassis");
        drawNoFacingSlot(gui, 1, mx, my, 79, 15, MuiSlot.PLUS, "rocket_assembly_station.fuel_tank");
        drawNoFacingSlot(gui, 0, mx, my, 79, 40, MuiSlot.PLUS, "rocket_assembly_station.thruster");

        // Craft button
        drawButton(gui, mx, my, 159, 46, MuiSlot.TICK, () -> entity.areSlotsFilled(),() -> {
            PacketSender.sendToServer(new C2SAssemblyStationCraft(this.entity().getBlockPos()));
            MUI.click();
        }, () -> Component.literal("Craft"));
        MUI.blitCommon(gui, i + 185, j + 52, 405, 13, 17, 6);
        MUI.blitCommon(gui, i + 133, j + 52, 422, 13, 17, 6);

        drawOverlay(gui);
    }
}
