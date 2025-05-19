package com.machina.client.screen.menu;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MUI.MuiSlot;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.api.rocket.part.impl.ChassisPart;
import com.machina.api.rocket.part.impl.FuelTankPart;
import com.machina.api.rocket.part.impl.LifeSupportPart;
import com.machina.api.rocket.part.impl.ShieldPart;
import com.machina.api.rocket.part.impl.ThrusterPart;
import com.machina.api.util.StringUtils;
import com.machina.block.entity.machine.RocketPartBenchBlockEntity;
import com.machina.block.menu.RocketPartBenchMenu;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.RocketPartInit;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.RegistryObject;

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
			parts = RocketPartInit.THRUSTERS.values().stream().map(RegistryObject::get).toList();
			break;
		case 1:
			parts = RocketPartInit.FUEL_TANKS.values().stream().map(RegistryObject::get).toList();
			break;
		case 2:
			parts = RocketPartInit.CHASSIS.values().stream().map(RegistryObject::get).toList();
			break;
		case 3:
			parts = RocketPartInit.LIFE_SUPPORTS.values().stream().map(RegistryObject::get).toList();
			break;
		default:
			parts = RocketPartInit.SHIELDS.values().stream().map(RegistryObject::get).toList();
			break;
		}
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		RocketPartBenchBlockEntity entity = this.<RocketPartBenchBlockEntity>entity();

		drawInventory(gui, mx, my);
		drawRocketBackground(gui);
		drawEnergyBarSmall(gui, 70, -58, entity.getEnergyF() == 1, "none");

		int i = midWidth();
		int j = midHeight();

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
			clickAndHover("tab_" + x, i + 35 + x * 21, j - 71, i + 53 + x * 21, j - 53, () -> true,
					() -> type.getName(), () -> {
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

			MUI.renderItemDeferred(gui, i + 90, h + 15, mx, my, true, false,
					new ItemStack(ItemInit.CONSTANTAN_INGOT.get(), 30), tooltips);
			MUI.renderItemDeferred(gui, i + 110, h + 15, mx, my, true, true,
					new ItemStack(ItemInit.ALUMINUM_PLATE.get(), 78), tooltips);
			MUI.renderItemDeferred(gui, i + 130, h + 15, mx, my, true, false,
					new ItemStack(ItemInit.COPPER_ROD.get(), 34), tooltips);

			boolean allowed = true;
			if (allowed) {
				int but_shade = 94;
				if (allowed & mx > i + 204 && mx < i + 221) {
					if (my > j - 45 && my < j + 67) {
						if (my > h + 15 && my < h + 32) {
							but_shade = 113;
							tooltips.add(() -> MUI.renderTooltip(gui, mx, my, MUI.uistr("rocket_part_bench.craft")));
						}
					}
				}
				MUI.blitCommon(gui, i + 204, h + 15, 466, but_shade, 19, 19);
				MuiSlot.TICK.draw(gui, i + 208, h + 19, this.aliveTicks);

				MUI.blitCommon(gui, i + 198, h + 16, 387, 0, 3, 16);
				MUI.blitCommon(gui, i + 225, h + 16, 390, 0, 3, 16);
			} else {
				MuiSlot.CROSS_R.draw(gui, i + 212, h + 19, this.aliveTicks);
			}

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
	public boolean mouseScrolled(double mx, double my, double scroll) {
		if (scroll != 0) {
			float max = Math.max(0, parts.size() * 80 - 112);
			this.scrollDist -= scroll * 10;
			if (this.scrollDist < 0.0F)
				this.scrollDist = 0.0F;
			if (this.scrollDist > max)
				this.scrollDist = max;
			return true;
		}
		return false;
	}
}
