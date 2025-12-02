package com.machina.client.screen.menu;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MUI.MuiSlot;
import com.machina.api.util.StringUtils;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.block.entity.machine.RocketAssemblyStationBlockEntity;
import com.machina.block.menu.RocketAssemblyStationMenu;
import com.machina.registration.init.FluidInit;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class RocketAssemblyStationScreen extends MachinaMenuScreen<RocketAssemblyStationMenu> {

    public RocketAssemblyStationScreen(RocketAssemblyStationMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
        RocketAssemblyStationBlockEntity entity = this.entity();

        drawInventory(gui, mx, my);
        drawRocketBackground(gui);
        drawEnergyBarSmall(gui, 50, -60, entity.getEnergyF() == 1, "none");

        int i = midWidth();
        int j = midHeight();

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
        MUI.drawString(gui, MUI.uistr("rocket_part_bench.mass").append(c).append(Component
                .literal(StringUtils.formatMass(5200)).withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))),
                i + 110, j - 45);
        MUI.drawString(gui, MUI.uistr("rocket_part_bench.fuel_type").append(c)
                .append(StringUtils.fluid(new FluidStack(FluidInit.AMMONIA.fluid(), 1), true)), i + 110, j - 35);
        MUI.drawString(gui, MUI.uistr("rocket_part_bench.efficiency").append(c).append(Component
                .literal(StringUtils.formatPercent(0.5f)).withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
                i + 110, j - 25);
        MUI.drawString(gui, MUI.uistr("rocket_part_bench.fuel_capacity").append(c).append(Component
                .literal(StringUtils.formatFluid(10_000)).withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
                i + 110, j - 15);
        MUI.drawString(gui, MUI.uistr("rocket_part_bench.coolant_capacity").append(c).append(Component
                .literal(StringUtils.formatFluid(15_000)).withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_2))),
                i + 110, j - 5);
        MUI.drawString(gui, MUI.uistr("rocket_part_bench.coolant_type").append(c)
                .append(StringUtils.fluid(new FluidStack(Fluids.WATER, 1), true)), i + 110, j + 5);
        MUI.drawString(gui, MUI.uistr("rocket_part_bench.efficiency").append(c).append(Component
                .literal(StringUtils.formatPercent(0.67f)).withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
                i + 110, j + 15);
        MUI.drawString(gui, MUI.uistr("rocket_part_bench.storage").append(c).append(
                Component.literal(String.valueOf(12)).withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
                i + 110, j + 25);
        MUI.drawString(gui, MUI.uistr("rocket_part_bench.max_pressure").append(c).append(Component
                .literal(StringUtils.formatPressure(4000f)).withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
                i + 110, j + 35);

        // Slots
        MUI.drawLine(gui, i + 71, j - 52, i + 38, j - 47, true, MUI.CYAN);
        MUI.drawLine(gui, i + 71, j - 27, i + 45, j - 20, true, MUI.CYAN);
        MUI.drawLine(gui, i + 71, j - 2, i + 51, j + 3, true, MUI.CYAN);
        MUI.drawLine(gui, i + 71, j + 23, i + 41, j + 18, true, MUI.CYAN);
        MUI.drawLine(gui, i + 71, j + 48, i + 47, j + 44, true, MUI.CYAN);

        drawNoFacingSlot(gui, 4, mx, my, 79, -60, MuiSlot.PLUS, "Shield");
        drawNoFacingSlot(gui, 3, mx, my, 79, -35, MuiSlot.PLUS, "Life Support");
        drawNoFacingSlot(gui, 2, mx, my, 79, -10, MuiSlot.PLUS, "Chassis");
        drawNoFacingSlot(gui, 1, mx, my, 79, 15, MuiSlot.PLUS, "Fuel Tank");
        drawNoFacingSlot(gui, 0, mx, my, 79, 40, MuiSlot.PLUS, "Thrusters");

        // Craft button
        drawButton(gui, mx, my, 159, 46, MuiSlot.TICK, () -> {
            System.out.println("CRAFT");
        }, () -> Component.literal("Craft"));
        MUI.blitCommon(gui, i + 185, j + 52, 405, 13, 17, 6);
        MUI.blitCommon(gui, i + 133, j + 52, 422, 13, 17, 6);

        drawOverlay(gui);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            int i = midWidth();
            int j = midHeight();
        }
        return super.mouseClicked(x, y, button);
    }
}
