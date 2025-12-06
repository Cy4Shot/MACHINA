package com.machina.client.screen.menu.entity;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.rocket.RocketMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class RocketScreen extends MachinaMenuScreen<RocketMenu>  {

    public RocketScreen(RocketMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
        drawInventory(gui, mx, my);
        drawBackground(gui);

        int i = midWidth();
        int j = midHeight();

        drawOverlay(gui);
    }
}
