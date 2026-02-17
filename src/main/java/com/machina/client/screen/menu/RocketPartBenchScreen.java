package com.machina.client.screen.menu;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MUI.MuiSlot;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.network.c2s.C2SPartBenchCraft;
import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.api.rocket.part.impl.*;
import com.machina.api.util.PlayerHelper;
import com.machina.api.util.StringUtils;
import com.machina.block.entity.machine.RocketPartBenchBlockEntity;
import com.machina.block.menu.RocketPartBenchMenu;
import com.machina.registration.init.RocketPartInit;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class RocketPartBenchScreen extends MachinaMenuScreen<RocketPartBenchMenu> {

    private List<? extends RocketPart<?>> parts = new ArrayList<>();
    private int selected = 0;
    private float scrollDist = 0;

    public RocketPartBenchScreen(RocketPartBenchMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        updateParts();
    }

    private void updateParts() {
        switch (selected) {
        case 0:
            parts = RocketPartInit.THRUSTERS.values().stream().map(DeferredHolder::get).toList();
            break;
        case 1:
            parts = RocketPartInit.FUEL_TANKS.values().stream().map(DeferredHolder::get).toList();
            break;
        case 2:
            parts = RocketPartInit.CHASSIS.values().stream().map(DeferredHolder::get).toList();
            break;
        case 3:
            parts = RocketPartInit.LIFE_SUPPORTS.values().stream().map(DeferredHolder::get).toList();
            break;
        default:
            parts = RocketPartInit.SHIELDS.values().stream().map(DeferredHolder::get).toList();
            break;
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
        RocketPartBenchBlockEntity entity = this.entity();

        drawInventory(gui, mx, my);
        drawRocketBackground(gui);

        int i = midWidth();
        int j = midHeight();

        // Progress Mode
        if (entity.isCrafting()) {
            drawEnergyBar(gui, 0, -24, entity.getEnergyF() > 0, "rocket_part_bench.no_power");
            int x = i + 51;
            int y = j + 10;
            registerHoverable("progress_bar", x + 1, y + 1, x + 136, y + 18,
                    () -> Component.literal(StringUtils.formatPercent(entity.getProgressPercent())));
            MUI.drawBar(gui, x, y, entity.getProgressPercent(), true, MUI.uistrs("rocket_part_bench.progress") + " ("
                    + StringUtils.formatPercent(entity.getProgressPercent()) + ")", "", (xp, yp, p) -> {
                        MUI.blitCommon(gui, xp + 2, yp + 4, 0, 404, (int) (131 * p), 14);
                    });
            return;
        }

        // Energy
        drawEnergyBarSmall(gui, 70, -58, entity.getEnergyF() == 1, "none");

        // Tabs
        MUI.blitRocket(gui, i + 26, j - 72, 253, 0, 121, 26);
        for (int x = 0; x < 5; x++) {
            RocketPartType type = RocketPartType.values()[x];
            if (selected != x
                    && (mx < i + 35 + x * 21 || mx > i + 35 + x * 21 + 18 || my < j - 71 || my > j - 71 + 18)) {
                MUI.blitRocket(gui, i + 35 + x * 21, j - 71, 235, 0, 18, 18);
            } else {
                MUI.blitRocket(gui, i + 35 + x * 21, j - 71, 235, 18, 18, 18);
            }

            MUI.blitRocket(gui, i + 36 + x * 21, j - 70, type.getX(), 0, 16, 16);

            final int x1 = x;
            clickAndHover("tab_" + x, i + 35 + x * 21, j - 71, i + 53 + x * 21, j - 53, () -> true, type::getName,
                    () -> {
                        if (this.selected != x1) {
                            this.selected = x1;
                            this.scrollDist = 0;
                            updateParts();
                            MUI.click();
                        }
                    });
        }

        // Main Body Background
        MUI.blitRocket(gui, i + 4, j - 46, 253, 26, 227, 114);

        Queue<Runnable> tooltips = new ArrayDeque<>();

        // Main Body
        MUI.enableClipping(i + 5, j - 45, 225, 112);
        for (int x = 0; x < parts.size(); x++) {
            int h = j + x * 80 - (int) (scrollDist);
            RocketPart<?> part = parts.get(x);
            MUI.rocketPart(gui, i + 48, h - 8, 24, aliveTicks % 360, -15f, part);

            MUI.drawString(gui, part.getName().withStyle(Style.EMPTY.withBold(true)), i + 90, h - 35);
            Component c = Component.literal(": ");
            MUI.drawString(gui,
                    MUI.uistr("rocket_part_bench.mass").append(c)
                            .append(Component.literal(StringUtils.formatMass(part.getMass()))
                                    .withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))),
                    i + 90, h - 20);
            switch (selected) {
            case 0:
                ThrusterPart<?> thruster = (ThrusterPart<?>) part;
                MUI.drawString(gui,
                        MUI.uistr("rocket_part_bench.fuel_type").append(c)
                                .append(StringUtils.fluid(new FluidStack(thruster.getFuel().fluid(), 1), true)),
                        i + 90, h - 10);
                MUI.drawString(gui,
                        MUI.uistr("rocket_part_bench.efficiency").append(c)
                                .append(Component.literal(StringUtils.formatPercent(thruster.getFuelEfficiency()))
                                        .withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
                        i + 90, h);
                break;
            case 1:
                FuelTankPart<?> tank = (FuelTankPart<?>) part;
                MUI.drawString(gui,
                        MUI.uistr("rocket_part_bench.fuel_capacity").append(c)
                                .append(Component.literal(StringUtils.formatFluid(tank.getFuelStorage()))
                                        .withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
                        i + 90, h - 10);
                MUI.drawString(gui,
                        MUI.uistr("rocket_part_bench.coolant_capacity").append(c)
                                .append(Component.literal(StringUtils.formatFluid(tank.getCoolantStorage()))
                                        .withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_2))),
                        i + 90, h);
                break;
            case 2:
                ChassisPart<?> chassis = (ChassisPart<?>) part;
                MUI.drawString(gui,
                        MUI.uistr("rocket_part_bench.coolant_type").append(c)
                                .append(StringUtils.fluid(new FluidStack(chassis.getCoolant().fluid(), 1), true)),
                        i + 90, h - 10);
                MUI.drawString(gui,
                        MUI.uistr("rocket_part_bench.efficiency").append(c)
                                .append(Component.literal(StringUtils.formatPercent(chassis.getCoolantEfficiency()))
                                        .withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
                        i + 90, h);
                break;
            case 3:
                LifeSupportPart<?> lifeSupport = (LifeSupportPart<?>) part;
                MUI.drawString(gui,
                        MUI.uistr("rocket_part_bench.storage").append(c)
                                .append(Component.literal(String.valueOf(lifeSupport.getSlots()))
                                        .withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
                        i + 90, h - 10);
                break;
            case 4:
                ShieldPart<?> shield = (ShieldPart<?>) part;
                MUI.drawString(gui,
                        MUI.uistr("rocket_part_bench.max_pressure").append(c)
                                .append(Component.literal(StringUtils.formatPressure(shield.getMaxAtmPressure()))
                                        .withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))),
                        i + 90, h - 10);
                break;
            }

            entity.getRecipe(part).ifPresent(r -> {
                boolean hasAll = true;
                boolean hasPower = entity.hasPower(r.value());

                int x1 = i + 90;
                for (ItemStack s : r.value().getInputItems()) {
                    boolean has = PlayerHelper.hasAll(mc.player, s);
                    hasAll &= has;
                    MUI.renderItemDeferred(gui, x1, h + 15, mx, my, has, s, tooltips);
                    x1 += 20;
                }

                if (hasAll) {
                    int but_shade = 94;
                    if (mx > i + 204 && mx < i + 221) {
                        if (my > j - 45 && my < j + 67) {
                            if (my > h + 15 && my < h + 32) {
                                but_shade = 113;
                                tooltips.add(() -> MUI.renderTooltip(gui, mx, my,
                                        MUI.uistr("rocket_part_bench.craft")
                                                .withStyle(Style.EMPTY.withColor(MUI.CYAN).withBold(true))
                                                .append(hasPower ? Component.literal("")
                                                        : Component.literal(" ")
                                                                .append(MUI.uistr("rocket_part_bench.unavailable")
                                                                        .withStyle(Style.EMPTY.withColor(MUI.RED)
                                                                                .withBold(false).withItalic(true)))),
                                        MUI.uistr("rocket_part_bench.requires").append(c)
                                                .append(Component.literal(StringUtils.formatPower(r.value().getPowerRate()))
                                                        .withStyle(Style.EMPTY.withColor(hasPower ? MUI.GREEN : MUI.RED)
                                                                .withBold(true)))));
                            }
                        }
                    }
                    MUI.blitCommon(gui, i + 204, h + 15, 466, but_shade, 19, 19);
                    MuiSlot.TICK.draw(gui, i + 208, h + 19, this.aliveTicks);

                    MUI.blitCommon(gui, i + 198, h + 16, hasPower ? 387 : 393, 0, 3, 16);
                    MUI.blitCommon(gui, i + 225, h + 16, hasPower ? 390 : 396, 0, 3, 16);
                } else {
                    MuiSlot.CROSS_R.draw(gui, i + 212, h + 19, this.aliveTicks);
                }
            });

            if (x != parts.size() - 1) {
                MUI.blitCommon(gui, i + 6, h + 36, 179, 92, 184, 2);
                MUI.blitCommon(gui, i + 192, h + 36, 179, 92, 38, 2);
            }
        }
        MUI.disableClipping();

        while (!tooltips.isEmpty()) {
            tooltips.poll().run();
        }

        drawOverlay(gui);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double scrollX, double scrollY) {
        if (scrollY != 0) {
            float max = Math.max(0, parts.size() * 80 - 112);
            this.scrollDist -= (float) (scrollY * 10);
            if (this.scrollDist < 0.0F)
                this.scrollDist = 0.0F;
            if (this.scrollDist > max)
                this.scrollDist = max;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            int i = midWidth();
            int j = midHeight();

            // Detect click on craft button
            if (x > i + 204 && x < i + 221) {
                if (y > j - 45 && y < j + 67) {
                    for (int x1 = 0; x1 < parts.size(); x1++) {
                        RocketPartBenchBlockEntity entity = this.entity();
                        RocketPart<?> part = parts.get(x1);
                        Optional<RecipeHolder<? extends MachinaRecipe<RocketPartBenchBlockEntity>>> or = entity.getRecipe(part);
                        if (or.isPresent() && mc.player != null) {
                            MachinaRecipe<RocketPartBenchBlockEntity> r = or.get().value();
                            if (PlayerHelper.hasAll(mc.player, r.getInputItems()) && entity.hasPower(r)) {
                                int h = j + x1 * 80 - (int) (scrollDist);
                                if (y > h + 15 && y < h + 32) {
                                    PacketDistributor.sendToServer(new C2SPartBenchCraft(part, entity.getBlockPos()));
                                    MUI.click();
                                    mc.player.closeContainer();
                                    return true;
                                }
                            }
                        }
                    }

                }
            }
        }
        return super.mouseClicked(x, y, button);
    }
}
