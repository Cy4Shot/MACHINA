package com.machina.network;

import java.util.function.Function;

import com.machina.network.c2s.C2SAtmosphericSeparatorSelect;
import com.machina.network.c2s.C2SCalculateRocketFuel;
import com.machina.network.c2s.C2SClearTank;
import com.machina.network.c2s.C2SCompletePuzzle;
import com.machina.network.c2s.C2SDevPlanetCreationGUI;
import com.machina.network.c2s.C2SEtchBlueprint;
import com.machina.network.c2s.C2SFabricatorFabricate;
import com.machina.network.c2s.C2SLaunchShip;
import com.machina.network.c2s.C2SRefuel;
import com.machina.network.c2s.C2SSetShipDestination;
import com.machina.network.c2s.C2SShipConsoleGUIButton;
import com.machina.network.c2s.C2SShipLaunchEffect;
import com.machina.network.c2s.C2SSpawnParticle;
import com.machina.network.s2c.S2CFluidSync;
import com.machina.network.s2c.S2CLaunchShip;
import com.machina.network.s2c.S2CResearchSync;
import com.machina.network.s2c.S2CResearchToast;
import com.machina.network.s2c.S2CStarchartSync;
import com.machina.util.text.MachinaRL;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MachinaNetwork extends BaseNetwork {

	public static final String NETWORK_VERSION = "0.1.0";

	public static final SimpleChannel CHANNEL = newSimpleChannel("channel");

	public static void init() {
		// Server -> Client
		registerServerToClient(S2CStarchartSync.class, S2CStarchartSync::decode);
		registerServerToClient(S2CResearchSync.class, S2CResearchSync::decode);
		registerServerToClient(S2CFluidSync.class, S2CFluidSync::decode);
		registerServerToClient(S2CLaunchShip.class, S2CLaunchShip::decode);
		registerServerToClient(S2CResearchToast.class, S2CResearchToast::decode);

		// Client -> Server
		registerClientToServer(C2SDevPlanetCreationGUI.class, C2SDevPlanetCreationGUI::decode);
		registerClientToServer(C2SShipConsoleGUIButton.class, C2SShipConsoleGUIButton::decode);
		registerClientToServer(C2SCompletePuzzle.class, C2SCompletePuzzle::decode);
		registerClientToServer(C2SSetShipDestination.class, C2SSetShipDestination::decode);
		registerClientToServer(C2SRefuel.class, C2SRefuel::decode);
		registerClientToServer(C2SAtmosphericSeparatorSelect.class, C2SAtmosphericSeparatorSelect::decode);
		registerClientToServer(C2SCalculateRocketFuel.class, C2SCalculateRocketFuel::decode);
		registerClientToServer(C2SLaunchShip.class, C2SLaunchShip::decode);
		registerClientToServer(C2SSpawnParticle.class, C2SSpawnParticle::decode);
		registerClientToServer(C2SShipLaunchEffect.class, C2SShipLaunchEffect::decode);
		registerClientToServer(C2SEtchBlueprint.class, C2SEtchBlueprint::decode);
		registerClientToServer(C2SFabricatorFabricate.class, C2SFabricatorFabricate::decode);
		registerClientToServer(C2SClearTank.class, C2SClearTank::decode);

		BaseNetwork.MACHINA_CHANNEL = CHANNEL;
	}

	private static SimpleChannel newSimpleChannel(String name) {
		return NetworkRegistry.newSimpleChannel(new MachinaRL(name), () -> NETWORK_VERSION,
				version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));
	}

	private static <M extends INetworkMessage> void registerServerToClient(Class<M> type,
			Function<PacketBuffer, M> decoder) {
		registerServerToClient(CHANNEL, type, decoder);
	}

	private static <M extends INetworkMessage> void registerClientToServer(Class<M> type,
			Function<PacketBuffer, M> decoder) {
		registerClientToServer(CHANNEL, type, decoder);
	}

}
