/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.machina.events;

import com.machina.Machina;
import com.machina.api.planet.trait.pool.PlanetTraitPoolManager;
import com.machina.api.world.data.StarchartData;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE)
public class ForgeEvents {

	@SubscribeEvent
	public static void addReloadListeners(AddReloadListenerEvent event) {
		event.addListener(Machina.planetTraitPoolManager);
	}

	@SubscribeEvent
	public static void debug(ItemTossEvent event) {
		if (event.getEntity().level.isClientSide) { return; }
		MinecraftServer s = ServerLifecycleHooks.getCurrentServer();
		StarchartData.getStarchartForServer(s).generateStarchart(s.getLevel(World.OVERWORLD).getSeed());
		StarchartData.getDefaultInstance(s).syncClients();
		StarchartData.getStarchartForServer(s).debugStarchart();
	}

	// @SubscribeEvent
	// public static void onWorldLoaded(PlayerEvent.PlayerLoggedInEvent event) {
	// if (!event.getPlayer().level.isClientSide()) {
	// StarchartHelper.syncCapabilityWithClients(event.getPlayer().level);
	// }
	// }

	@SubscribeEvent
	public static void onPlayerLogin(final PlayerLoggedInEvent e) {
		if (e.getEntity().level.isClientSide) { return; }
		System.out.println("SYNCEY TIME");
		StarchartData.getDefaultInstance(e.getEntity().getServer()).syncClient((ServerPlayerEntity) e.getPlayer());
	}

	// @SubscribeEvent
	// public static void handleEffectBan(LivingEntityAddEffectEvent event) {
	// World level = event.getEntity().level;
	// if (PlanetUtils.isDimensionPlanet(level.dimension())) {
	// if (CapabilityPlanetTrait.worldHasTrait(level, PlanetTraitInit.SUPERHOT)
	// && event.getEffect().getEffect() == Effects.FIRE_RESISTANCE) {
	// event.setCanceled(true);
	// }
	// }
	// }
}
