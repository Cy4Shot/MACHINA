package com.machina.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientVaultState {

	private static boolean inVault = false;

	public static boolean isInVault() {
		return inVault;
	}

	public static void setInVault(boolean value) {
		inVault = value;
	}
}
