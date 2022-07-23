package com.machina.client.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.machina.block.container.ShipLaunchContainer;
import com.machina.block.tile.base.IFluidTileEntity;
import com.machina.client.ClientStarchart;
import com.machina.client.screen.base.TerminalScreen;
import com.machina.client.util.IStarchartSelector;
import com.machina.config.ClientConfig;
import com.machina.network.MachinaNetwork;
import com.machina.network.c2s.C2SCalculateRocketFuel;
import com.machina.network.c2s.C2SLaunchShip;
import com.machina.network.c2s.C2SRefuel;
import com.machina.network.c2s.C2SSetShipDestination;
import com.machina.registration.init.AttributeInit;
import com.machina.util.math.MathUtil;
import com.machina.world.data.PlanetData;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ShipLaunchScreen extends TerminalScreen<ShipLaunchContainer> {

	public ShipLaunchScreen(ShipLaunchContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}

	@SuppressWarnings("resource")
	@Override
	public List<TerminalCommand> createCommands() {
		List<TerminalCommand> list = new ArrayList<>();

		// Clear
		list.add(new TerminalCommand("clear", t -> {
			clear();
		}));

		// Neofetch
		list.add(new TerminalCommand("neofetch", t -> {
			space();
			add("\u2588\u2588\u2588\u2581\u2588\u2588\u2588\u2581\u2588\u2588\u2588\u2581\u2588\u2588\u2588");
			add("\u2588\u2581\u2581\u2581\u2581\u2588\u2581\u2581\u2588\u2581\u2588\u2581\u2588\u2581\u2588");
			add("\u2588\u2588\u2588\u2581\u2581\u2588\u2581\u2581\u2588\u2588\u2588\u2581\u2588\u2588\u2581");
			add("\u2581\u2581\u2588\u2581\u2581\u2588\u2581\u2581\u2588\u2581\u2588\u2581\u2588\u2581\u2588");
			add("\u2588\u2588\u2588\u2581\u2581\u2588\u2581\u2581\u2588\u2581\u2588\u2581\u2588\u2581\u2588");
			space();
			add(t, "star_os");
			add(t, "star_cpu");
			add(t, "star_status");
		}));

		// Set Destination
		list.add(new TerminalCommand("destination", t -> {
			space();
			add(t, "loading");
			createTimer(ClientConfig.starchartLoadDuration.get(), () -> {
				StarchartSelectScreen.select(new IStarchartSelector() {
					@Override
					public void accept(ResourceLocation loc) {
						int id = Integer.valueOf(loc.getPath());
						MachinaNetwork.CHANNEL.sendToServer(
								new C2SSetShipDestination(ShipLaunchScreen.this.menu.te.getBlockPos(), id));
						MachinaNetwork.CHANNEL
								.sendToServer(new C2SCalculateRocketFuel(ShipLaunchScreen.this.menu.te.getBlockPos()));
						PlanetData data = ClientStarchart.getPlanetData(id);
						Minecraft.getInstance().setScreen(ShipLaunchScreen.this);
						space();
						add(t.getFeedback("set") + data.getAttributeFormatted(AttributeInit.PLANET_NAME));
						add(t, "calculating");
						createTimer(ClientConfig.fuelCalculateDuration.get(), () -> {
							space();
							add(t, "required");
							add(t.getFeedback("water")
									+ MathUtil.engineering(
											ShipLaunchScreen.this.menu.te.hWaterFuel / IFluidTileEntity.BUCKET, "B")
									+ " / " + MathUtil.engineering(
											ShipLaunchScreen.this.menu.te.waterFuel / IFluidTileEntity.BUCKET, "B"));
							add(t.getFeedback("aluminium") + ShipLaunchScreen.this.menu.te.hAluminiumFuel + " / "
									+ ShipLaunchScreen.this.menu.te.aluminiumFuel);
							add(t.getFeedback("ammonium_nitrate") + ShipLaunchScreen.this.menu.te.hAmmoniaNitrateFuel
									+ " / " + ShipLaunchScreen.this.menu.te.ammoniaNitrateFuel);
						});
					}
				});
			});
		}));

		// Fuel
		list.add(new TerminalCommand("fuel", t -> {
			space();
			add(t, "stored");
			add(t.getFeedback("water")
					+ MathUtil.engineering(ShipLaunchScreen.this.menu.te.hWaterFuel / IFluidTileEntity.BUCKET, "B")
					+ " / "
					+ MathUtil.engineering(ShipLaunchScreen.this.menu.te.waterFuel / IFluidTileEntity.BUCKET, "B"));
			add(t.getFeedback("aluminium") + ShipLaunchScreen.this.menu.te.hAluminiumFuel + " / "
					+ ShipLaunchScreen.this.menu.te.aluminiumFuel);
			add(t.getFeedback("ammonium_nitrate") + ShipLaunchScreen.this.menu.te.hAmmoniaNitrateFuel + " / "
					+ ShipLaunchScreen.this.menu.te.ammoniaNitrateFuel);
		}));

		// Refuel
		list.add(new TerminalCommand("refuel", t -> {
			space();
			add(t, "info");
			add(t, "await");
			awaitResponse(s -> {
				space();
				add(t, "progress");
				final int oldWater = this.menu.te.hWaterFuel;
				final int oldAluminium = this.menu.te.hAluminiumFuel;
				final int oldAmmoniaNitrate = this.menu.te.hAmmoniaNitrateFuel;
				MachinaNetwork.CHANNEL.sendToServer(new C2SRefuel(this.menu.te.getBlockPos()));
				createTimer(ClientConfig.refuelDuration.get(), () -> {
					space();
					add(t, "complete");
					add(t, "gain");
					add(t.getFeedback("water") + MathUtil.engineering(
							(ShipLaunchScreen.this.menu.te.hWaterFuel - oldWater) / IFluidTileEntity.BUCKET, "B"));
					add(t.getFeedback("aluminium") + (ShipLaunchScreen.this.menu.te.hAluminiumFuel - oldAluminium));
					add(t.getFeedback("ammonium_nitrate")
							+ (ShipLaunchScreen.this.menu.te.hAmmoniaNitrateFuel - oldAmmoniaNitrate));
				});
				return true;
			});
		}));

		// Launch
		list.add(new TerminalCommand("launch", t -> {
			space();
			if (this.menu.te.destination == -1) {
				add(t, "unset");
				return;
			}
			Random r = new Random();
			PlanetData data = ClientStarchart.getPlanetData(this.menu.te.destination);
			add(t.getFeedback("dest") + data.getAttributeFormatted(AttributeInit.PLANET_NAME));
			add(t.getFeedback("dist") + data.getAttributeFormatted(AttributeInit.DISTANCE));
			space();
			add(t, "required");
			add(t.getFeedback("water")
					+ MathUtil.engineering(ShipLaunchScreen.this.menu.te.hWaterFuel / IFluidTileEntity.BUCKET, "B")
					+ " / "
					+ MathUtil.engineering(ShipLaunchScreen.this.menu.te.waterFuel / IFluidTileEntity.BUCKET, "B"));
			add(t.getFeedback("aluminium") + ShipLaunchScreen.this.menu.te.hAluminiumFuel + " / "
					+ ShipLaunchScreen.this.menu.te.aluminiumFuel);
			add(t.getFeedback("ammonium_nitrate") + ShipLaunchScreen.this.menu.te.hAmmoniaNitrateFuel + " / "
					+ ShipLaunchScreen.this.menu.te.ammoniaNitrateFuel);
			space();
			add(t, "await");
			space();
			awaitResponse(s -> {
				clear();
				add(t, "load_booting");
				createTimer(ClientConfig.launchInitDuration.get() + r.nextInt(20), () -> {
					space();
					add(t, "load_booting_1");
					add(t, "load_booting_2");
					add(t, "load_booting_3");
					add(t, "load_booting_4");
					add(t, "load_booting_5");
					space();
					add(t, "load_reactor");
					createTimer(ClientConfig.launchInitDuration.get() + r.nextInt(20), () -> {
						space();
						add(t, "load_reactor_1");
						add(t, "load_reactor_2");
						add(t, "load_reactor_3");
						add(t, "load_reactor_4");
						space();
						add(t, "load_thrusters");
						createTimer(ClientConfig.launchInitDuration.get() + r.nextInt(20), () -> {
							space();
							add(t, "load_thrusters_1");
							add(t, "load_thrusters_2");
							add(t, "load_thrusters_3");
							space();
							add(t, "load_navigation");
							createTimer(ClientConfig.launchInitDuration.get() + r.nextInt(20), () -> {
								space();
								add(t, "load_navigation_1");
								add(t, "load_navigation_2");
								add(t, "load_navigation_3");
								add(t, "load_navigation_4");
								space();
								add(t, "load_comms");
								createTimer(ClientConfig.launchInitDuration.get() + r.nextInt(20), () -> {
									space();
									add(t, "load_comms_1");
									add(t, "load_comms_2");
									add(t, "load_comms_3");
									add(t, "load_comms_4");
									add(t, "load_comms_5");
									space();
									add(t, "load_preparing");
									createTimer(ClientConfig.launchInitDuration.get() + r.nextInt(20), () -> {
										space();
										add(t, "load_preparing_1");
										add(t, "load_preparing_2");
										add(t, "load_preparing_3");
										add(t, "load_preparing_4");
										add(t, "load_preparing_5");
										add(t, "load_preparing_6");
										add(t, "load_preparing_7");
										add(t, "load_preparing_8");
										space();
										add(t, "confirm");
										add(t, "warning");
										space();
										awaitResponse(o -> {
											if (!o.equals("confirm")) {
												return true;
											}

											clear();
											add(t, "seated");
											space();
											add(t, "init");
											createTimer(ClientConfig.lifeSupportInitDuration.get(), () -> {
												MachinaNetwork.CHANNEL.sendToServer(
														new C2SLaunchShip(ShipLaunchScreen.this.menu.te.getBlockPos(),
																Minecraft.getInstance().player.getUUID()));
												Minecraft.getInstance().setScreen(null);
											});

											return true;
										});
									});
								});
							});
						});
					});
				});
				return true;
			});
		}));

		return list;
	}
}
