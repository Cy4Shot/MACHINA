package com.machina.client.screen;

import java.util.ArrayList;
import java.util.List;

import com.machina.block.container.ShipLaunchContainer;
import com.machina.client.ClientStarchart;
import com.machina.client.screen.base.TerminalScreen;
import com.machina.client.util.IStarchartSelector;
import com.machina.config.ClientConfig;
import com.machina.network.MachinaNetwork;
import com.machina.network.c2s.C2SSetShipDestination;
import com.machina.registration.init.PlanetAttributeTypesInit;
import com.machina.world.data.PlanetData;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ShipLaunchScreen extends TerminalScreen<ShipLaunchContainer> {

	public ShipLaunchScreen(ShipLaunchContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}

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
						PlanetData data = ClientStarchart.getPlanetData(id);
						Minecraft.getInstance().setScreen(ShipLaunchScreen.this);
						space();
						add(t.getFeedback("set") + data.getAttributeFormatted(PlanetAttributeTypesInit.PLANET_NAME));
						MachinaNetwork.CHANNEL.sendToServer(
								new C2SSetShipDestination(ShipLaunchScreen.this.menu.te.getBlockPos(), id));
					}
				});
			});
		}));

		return list;
	}
}
