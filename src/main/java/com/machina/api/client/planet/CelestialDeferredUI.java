package com.machina.api.client.planet;

import net.minecraft.client.gui.GuiGraphics;

@FunctionalInterface
public interface CelestialDeferredUI {
    void render(GuiGraphics gui, int width, int height);
}
