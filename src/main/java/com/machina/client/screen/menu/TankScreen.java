package com.machina.client.screen.menu;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MUI.MuiSlot;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.block.entity.machine.TankBlockEntity;
import com.machina.block.menu.TankMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class TankScreen extends MachinaMenuScreen<TankMenu> {

    public TankScreen(TankMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @SuppressWarnings("removal")
    @Override
    protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
        drawInventory(gui, mx, my);
        drawBackground(gui);

        drawFluidBar(gui, 0, 0, 0);

        drawUpFacingSlot(gui, 0, mx, my, 20, 30, MuiSlot.PLUS, "tank.input");
        drawUpFacingSlot(gui, 1, mx, my, 197, 30, MuiSlot.MINUS, "tank.output");

        int i = midWidth();
        int j = midHeight();
        MUI.blitCommon(gui, i + 27, j + 7, 508, 0, 4, 21);
        MUI.blitCommon(gui, i + 27, j - 1, 369, 80, 17, 8);

        MUI.blitCommon(gui, i + 204, j + 7, 508, 0, 4, 21);
        MUI.blitCommon(gui, i + 191, j - 1, 390, 80, 17, 8);

        FluidStack fluid = this.<TankBlockEntity>entity().getFluid(0);
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
