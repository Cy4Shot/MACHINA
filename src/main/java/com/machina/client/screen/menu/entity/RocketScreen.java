package com.machina.client.screen.menu.entity;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.ClientStarchart;
import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MUI.MuiSlot;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.network.PacketSender;
import com.machina.api.network.c2s.C2SRocketLaunch;
import com.machina.api.network.c2s.C2SRocketSetDestination;
import com.machina.api.rocket.RocketCosts;
import com.machina.api.rocket.RocketProps;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.PlanetHelper;
import com.machina.api.util.StringUtils;
import com.machina.client.screen.StarchartRenderable;
import com.machina.rocket.RocketEntity;
import com.machina.rocket.RocketMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

public class RocketScreen extends MachinaMenuScreen<RocketMenu> {

    private static interface RocketTabDisplay {
        int getIconX();

        Component getName();

        void render(@NotNull GuiGraphics gui, RocketEntity entity, int mx, int my, int i, int j);

        default boolean starchartVisible() {
            return false;
        }
    }

    private final RocketTabDisplay INFO = new RocketTabDisplay() {
        @Override
        public void render(@NotNull GuiGraphics gui, RocketEntity entity, int mx, int my, int i, int j) {
            RocketProps props = entity.getProps();
            Component c = Component.literal(": ");
            MUI.drawString(gui,
                    MUI.uistr("rocket.info.mass").append(c)
                            .append(Component.literal(StringUtils.formatMass(props.mass()))
                                    .withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))),
                    i + 6, j + 6);
            MUI.drawString(gui,
                    MUI.uistr("rocket.info.fuel_type").append(c).append(StringUtils.fluid(props.fuelStack(), true)),
                    i + 6, j + 16);
            MUI.drawString(gui,
                    MUI.uistr("rocket.info.coolant_efficiency").append(c)
                            .append(Component.literal(StringUtils.formatPercent(props.coolantEfficiency()))
                                    .withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
                    i + 6, j + 26);
            MUI.drawString(gui,
                    MUI.uistr("rocket.info.fuel_capacity").append(c)
                            .append(Component.literal(StringUtils.formatFluid(props.fuelStorage()))
                                    .withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
                    i + 6, j + 36);
            MUI.drawString(gui,
                    MUI.uistr("rocket.info.coolant_capacity").append(c)
                            .append(Component.literal(StringUtils.formatFluid(props.coolantStorage()))
                                    .withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_2))),
                    i + 6, j + 46);
            MUI.drawString(gui, MUI.uistr("rocket.info.coolant_type").append(c)
                    .append(StringUtils.fluid(props.coolantStack(), true)), i + 6, j + 56);
            MUI.drawString(gui,
                    MUI.uistr("rocket.info.coolant_efficiency").append(c)
                            .append(Component.literal(StringUtils.formatPercent(props.coolantEfficiency()))
                                    .withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_2))),
                    i + 6, j + 66);
            MUI.drawString(gui, MUI.uistr("rocket.info.storage").append(c).append(Component
                    .literal(String.valueOf(props.slots())).withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))),
                    i + 6, j + 76);
            MUI.drawString(gui,
                    MUI.uistr("rocket.info.max_pressure").append(c)
                            .append(Component.literal(StringUtils.formatPressure(props.maxPressure()))
                                    .withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))),
                    i + 6, j + 86);
        }

        @Override
        public Component getName() {
            return MUI.uistr("rocket.tab.info");
        }

        @Override
        public int getIconX() {
            return 0;
        }
    };

    private final RocketTabDisplay FUELING = new RocketTabDisplay() {
        @Override
        public void render(@NotNull GuiGraphics gui, RocketEntity entity, int mx, int my, int i, int j) {
            ResourceKey<Level> destination = entity.getDestination();

            // TODO: Allow travel back to overworld
            if (destination.equals(Level.OVERWORLD)) {
                MUI.drawString(gui,
                        MUI.uistr("rocket.storage.invalid").withStyle(Style.EMPTY.withBold(true).withColor(MUI.RED)),
                        i + 6, j + 6);
            } else {
                MUI.drawCenteredString(gui, MUI.uistr("rocket.storage.missing_fuel")
                        .withStyle(Style.EMPTY.withBold(true).withColor(MUI.RED)), i + 112, j + 6);
            }

            MUI.drawSlot(gui, i + 44, j + 46, mx, my, true, false);
            MUI.drawSlot(gui, i + 164, j + 46, mx, my, true, false);
        }

        @Override
        public Component getName() {
            return MUI.uistr("rocket.tab.fueling");
        }

        @Override
        public int getIconX() {
            return 16;
        }
    };

    private final RocketTabDisplay STORAGE = new RocketTabDisplay() {
        @Override
        public void render(@NotNull GuiGraphics gui, RocketEntity entity, int mx, int my, int i, int j) {
        }

        @Override
        public Component getName() {
            return MUI.uistr("rocket.tab.storage");
        }

        @Override
        public int getIconX() {
            return 64;
        }
    };

    private final RocketTabDisplay DESTINATION = new RocketTabDisplay() {
        @Override
        public void render(@NotNull GuiGraphics gui, RocketEntity entity, int mx, int my, int i, int j) {
            RocketProps props = entity.getProps();
            RocketCosts costs = entity.getCosts();
            ResourceKey<Level> destination = entity.getDestination();

            // TODO: Allow travel back to overworld
            if (destination.equals(Level.OVERWORLD)) {
                MUI.drawString(gui, MUI.uistr("rocket.destination.invalid")
                        .withStyle(Style.EMPTY.withBold(true).withColor(MUI.RED)), i + 6, j + 6);
                return;
            }

            Planet dst = ClientStarchart.system.planets().get(PlanetHelper.getIdLevel(destination));

            Component c = Component.literal(": ");
            Component ob = Component.literal(" (");
            Component cb = Component.literal(")");
            MUI.drawString(gui,
                    MUI.uistr("rocket.destination.destination").append(c).append(
                            Component.literal(dst.name()).withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))),
                    i + 6, j + 6);
            MUI.drawString(gui,
                    MUI.uistr("rocket.destination.distance").append(c)
                            .append(Component.literal(StringUtils.formatDistanceAU(costs.distance()))
                                    .withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))),
                    i + 6, j + 16);
            MUI.drawString(gui,
                    MUI.uistr("rocket.destination.max_temperature").append(c)
                            .append(Component.literal(StringUtils.formatTemp(costs.maxTemp()))
                                    .withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))),
                    i + 6, j + 26);

            if (costs.fuelRequired() < props.fuelStorage()) {
                MUI.drawString(gui,
                        MUI.uistr("rocket.destination.fuel").append(c)
                                .append(Component.literal(StringUtils.formatFluid(costs.fuelRequired()))
                                        .withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
                        i + 6, j + 36);
            } else {
                Component invalid = Component.literal(StringUtils.formatFluid(costs.fuelRequired())).append(ob)
                        .append(Component.literal(StringUtils.formatFluid(props.fuelStorage()))).append(cb);
                MUI.drawString(gui,
                        MUI.uistr("rocket.destination.fuel").append(c)
                                .append(invalid.copy().withStyle(Style.EMPTY.withBold(true).withColor(MUI.RED))),
                        i + 6, j + 36);
            }

            if (costs.coolantRequired() < props.coolantStorage()) {
                MUI.drawString(gui,
                        MUI.uistr("rocket.destination.coolant").append(c)
                                .append(Component.literal(StringUtils.formatFluid(costs.coolantRequired()))
                                        .withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
                        i + 6, j + 46);
            } else {
                Component invalid = Component.literal(StringUtils.formatFluid(costs.coolantRequired())).append(ob)
                        .append(Component.literal(StringUtils.formatFluid(props.coolantStorage()))).append(cb);
                MUI.drawString(gui,
                        MUI.uistr("rocket.destination.coolant").append(c)
                                .append(invalid.copy().withStyle(Style.EMPTY.withBold(true).withColor(MUI.RED))),
                        i + 6, j + 46);
            }

            if (costs.maxPres() < props.maxPressure()) {
                MUI.drawString(gui,
                        MUI.uistr("rocket.destination.max_pressure").append(c)
                                .append(Component.literal(StringUtils.formatPressure(costs.maxPres()))
                                        .withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
                        i + 6, j + 56);
            } else {
                Component invalid = Component.literal(StringUtils.formatPressure(costs.maxPres())).append(ob)
                        .append(Component.literal(StringUtils.formatPressure(props.maxPressure()))).append(cb);
                MUI.drawString(gui,
                        MUI.uistr("rocket.destination.max_pressure").append(c)
                                .append(invalid.copy().withStyle(Style.EMPTY.withBold(true).withColor(MUI.RED))),
                        i + 6, j + 56);
            }

            // Launch Button
            drawButton(gui, mx, my, 112, 31, MuiSlot.TICK, () -> {
                PacketSender.sendToServer(new C2SRocketLaunch(RocketScreen.this.menu.entity.getId()));
            }, () -> MUI.uistr("rocket.destination.launch"));
            
            MUI.blitCommon(gui, i + 134, j + 84, 405, 13, 17, 6);
            MUI.blitCommon(gui, i + 82, j + 84, 422, 13, 17, 6);
        }

        @Override
        public Component getName() {
            return MUI.uistr("rocket.tab.destination");
        }

        @Override
        public int getIconX() {
            return 32;
        }
    };

    private final RocketTabDisplay STARMAP = new RocketTabDisplay() {
        @Override
        public void render(@NotNull GuiGraphics gui, RocketEntity entity, int mx, int my, int i, int j) {
            int x = i + 1;
            int y = j + 1;
            int w = 225;
            int h = 112;
            MUI.enableClipping(x, y, w, h);
            starchart.render(gui, x, y, width / 2 - (x + w / 2), height / 2 - (y + h / 2), w, h);
            MUI.disableClipping();
        }

        @Override
        public Component getName() {
            return MUI.uistr("rocket.tab.starmap");
        }

        @Override
        public int getIconX() {
            return 48;
        }

        @Override
        public boolean starchartVisible() {
            return true;
        };
    };

    private final List<RocketTabDisplay> TABS = List.of(INFO, FUELING, STORAGE, STARMAP, DESTINATION);

    private final StarchartRenderable starchart;
    private int selected = 0;

    public RocketScreen(RocketMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.starchart = new StarchartRenderable(ClientStarchart.system, true);
        this.starchart.addSelectListener(selected -> {
            ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, new MachinaRL(String.valueOf(selected)));
            PacketSender.sendToServer(new C2SRocketSetDestination(this.menu.entity.getId(), dim));
        });
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
        drawInventory(gui, mx, my);
        drawRocketBackground(gui);

        int i = midWidth();
        int j = midHeight();

        final RocketTabDisplay sel = getSelected();

        MUI.blitRocket(gui, i + 26, j - 72, 253, 0, 121, 26);
        MUI.drawCenteredString(gui, sel.getName().copy().setStyle(Style.EMPTY.withBold(true)), i + 190, j - 63);

        for (int x = 0; x < TABS.size(); x++) {
            final RocketTabDisplay tab = TABS.get(x);
            if (selected != x
                    && (mx < i + 35 + x * 21 || mx > i + 35 + x * 21 + 18 || my < j - 71 || my > j - 71 + 18)) {
                MUI.blitRocket(gui, i + 35 + x * 21, j - 71, 235, 0, 18, 18);
            } else {
                MUI.blitRocket(gui, i + 35 + x * 21, j - 71, 235, 18, 18, 18);
            }

            MUI.blitRocket(gui, i + 36 + x * 21, j - 70, 128 + tab.getIconX(), 288, 16, 16);

            final int x1 = x;
            clickAndHover("tab_" + x, i + 35 + x * 21, j - 71, i + 53 + x * 21, j - 53, () -> true, tab::getName,
                    () -> {
                        if (this.selected != x1) {
                            this.selected = x1;
                            this.init();
                            MUI.click();
                        }
                    });
        }

        MUI.blitRocket(gui, i + 4, j - 47, 253, 26, 227, 114);
        sel.render(gui, this.menu.entity, mx, my, i + 4, j - 47);
        drawOverlay(gui);
    }

    @Override
    public boolean mouseClicked(double mX, double mY, int button) {
        if (inStarchart(mX, mY) && starchart.mouseClicked(mX, mY, button)) {
            return true;
        }
        return super.mouseClicked(mX, mY, button);
    }

    @Override
    public boolean mouseDragged(double mX, double mY, int button, double dX, double dY) {
        if (inStarchart(mX, mY) && starchart.mouseDragged(button, dX, dY, width, height)) {
            return true;
        }
        return super.mouseDragged(mX, mY, button, dX, dY);
    }

    @Override
    public boolean mouseScrolled(double mX, double mY, double delta) {
        if (inStarchart(mX, mY) && starchart.mouseScrolled(delta)) {
            return true;
        }
        return super.mouseScrolled(mX, mY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private RocketTabDisplay getSelected() {
        return TABS.get(selected);
    }

    private boolean inStarchart(double mX, double mY) {
        int i = midWidth();
        int j = midHeight();
        return getSelected().starchartVisible() && mX > i + 4 && mY > j - 46 && mX < i + 4 + 225 && mY < j - 46 + 112;
    }
}
