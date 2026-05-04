package com.machina.api.client.model.ctm;

import earth.terrarium.athena.api.client.models.FactoryManager;

public class CustomCTMRegistrar {
	public static void register() {
		FactoryManager.register(LayeredRodBlockModel.RL, LayeredRodBlockModel.FACTORY);
	}
}
