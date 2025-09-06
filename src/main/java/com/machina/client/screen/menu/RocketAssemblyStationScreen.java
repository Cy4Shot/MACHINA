package com.machina.client.screen.menu;

import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.block.entity.machine.RocketAssemblyStationBlockEntity;
import com.machina.block.menu.RocketAssemblyStationMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
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
        drawEnergyBarSmall(gui, 54, 58, entity.getEnergyF() == 1, "none");

        int i = midWidth();
        int j = midHeight();

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
