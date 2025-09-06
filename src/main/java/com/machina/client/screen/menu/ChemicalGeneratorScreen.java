package com.machina.client.screen.menu;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.util.StringUtils;
import com.machina.block.entity.machine.ChemicalGeneratorBlockEntity;
import com.machina.block.menu.ChemicalGeneratorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class ChemicalGeneratorScreen extends MachinaMenuScreen<ChemicalGeneratorMenu> {

    public ChemicalGeneratorScreen(ChemicalGeneratorMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {

        ChemicalGeneratorBlockEntity entity = this.entity();

        drawInventory(gui, mx, my);
        drawBackground(gui);
        drawFluidBar(gui, 0, -20, 0);
        drawEnergyBar(gui, 0, 20, true, "");

        int i = midWidth();
        int j = midHeight();
        MUI.blitCommon(gui, i + 61, j - 11, 508, 0, 4, 22);
        MUI.blitCommon(gui, i + 167, j - 11, 508, 0, 4, 22);

        Component text = entity.isLit() ? MUI.uistr("chemical_generator.progress")
                : (entity.isEnergyFull() ? MUI.uistr("chemical_generator.no_space")
                : MUI.uistr("chemical_generator.no_input"));
        int color = entity.isLit() ? MUI.CYAN : MUI.RED;
        MUI.drawCenteredString(gui, text, i + 117, j - 54, color);

        if (entity.isLit())
            MUI.drawCenteredString(gui, Component.literal(MUI.uistrs("chemical_generator.generating") + ": "
                    + StringUtils.formatPower(entity.getRate()) + "/t"), i + 117, j - 42);

        drawOverlay(gui);
    }
}
