package com.machina.client.screen.menu;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.api.rocket.part.impl.ChassisPart;
import com.machina.api.rocket.part.impl.FuelTankPart;
import com.machina.api.rocket.part.impl.LifeSupportPart;
import com.machina.api.rocket.part.impl.ShieldPart;
import com.machina.api.rocket.part.impl.ThrusterPart;
import com.machina.api.util.StringUtils;
import com.machina.block.menu.RocketPartBenchMenu;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.RocketPartInit;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.RegistryObject;

public class RocketPartBenchScreen extends MachinaMenuScreen<RocketPartBenchMenu> {

	public RocketPartBenchScreen(RocketPartBenchMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	private int selected = 0;
	private float scrollDist = 0;

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
//		RocketPartBenchBlockEntity entity = this.<RocketPartBenchBlockEntity>entity();

		drawInventory(gui, mx, my);
		drawRocketBackground(gui);
		drawEnergyBarSmall(gui, 70, -58, true, "");

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
						this.selected = x1;
						MUI.click();
					});
		}

		// Main Body Background
		MUI.blitRocket(gui, i + 4, j - 46, 253, 26, 227, 114);

		// Main Body
		MUI.enableClipping(i + 5, j - 45, 225, 112);
		List<? extends RocketPart<?>> parts;
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
		for (int x = 0; x < parts.size(); x++) {
			int h = j + x * 60 - (int) (scrollDist);
			RocketPart<?> part = parts.get(x);
			MUI.rocketPart(gui, i + 45, h - 16, 16, aliveTicks % 360, -15f, part);

			MUI.drawString(gui, part.getName(), i + 90, h - 35);
			switch (selected) {
			case 0:
				ThrusterPart<?> thruster = (ThrusterPart<?>) part;
				MUI.drawString(gui,
						Component.literal("Fuel Type: ")
								.append(StringUtils.fluid(new FluidStack(thruster.getFuel().fluid(), 1), true)),
						i + 90, h - 25);
				MUI.drawString(gui,
						Component.literal("Efficiency: " + StringUtils.formatPercent(thruster.getFuelEfficiency())),
						i + 90, h - 15);
				break;
			case 1:
				FuelTankPart<?> tank = (FuelTankPart<?>) part;
				MUI.drawString(gui,
						Component.literal("Fuel Capacity: " + StringUtils.formatFluid(tank.getFuelStorage())), i + 90,
						h - 25);
				MUI.drawString(gui,
						Component.literal("Coolant Capacity: " + StringUtils.formatFluid(tank.getCoolantStorage())),
						i + 90, h - 15);
				break;
			case 2:
				ChassisPart<?> chassis = (ChassisPart<?>) part;
				MUI.drawString(gui,
						Component.literal("Coolant Type: ")
								.append(StringUtils.fluid(new FluidStack(chassis.getCoolant().fluid(), 1), true)),
						i + 90, h - 25);
				MUI.drawString(gui,
						Component.literal("Efficiency: " + StringUtils.formatPercent(chassis.getCoolantEfficiency())),
						i + 90, h - 15);
				break;
			case 3:
				LifeSupportPart<?> lifeSupport = (LifeSupportPart<?>) part;
				MUI.drawString(gui, Component.literal("Storage Slots: " + String.valueOf(lifeSupport.getSlots())),
						i + 90, h - 25);
				break;
			case 4:
				ShieldPart<?> shield = (ShieldPart<?>) part;
				MUI.drawString(gui,
						Component.literal(
								"Max Atm Pressure: " + StringUtils.formatPressure(shield.getMaxAtmPressure())),
						i + 90, h - 25);
				break;
			}

			MUI.renderItem(gui, i + 90, h - 5, mx, my, true, new ItemStack(ItemInit.CONSTANTAN_INGOT.get(), 30));
			MUI.renderItem(gui, i + 110, h - 5, mx, my, true, new ItemStack(ItemInit.ALUMINUM_PLATE.get(), 78));
			MUI.renderItem(gui, i + 130, h - 5, mx, my, true, new ItemStack(ItemInit.COPPER_ROD.get(), 34));
		}
		MUI.disableClipping();

		drawOverlay(gui);
	}

	@Override
	public boolean mouseScrolled(double mx, double my, double scroll) {
		if (scroll != 0) {
			this.scrollDist -= scroll * 10;
			if (this.scrollDist < 0.0F)
				this.scrollDist = 0.0F;
			if (this.scrollDist > 200f)
				this.scrollDist = 200f;
			return true;
		}
		return false;
	}
}
