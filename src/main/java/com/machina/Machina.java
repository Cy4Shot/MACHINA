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

package com.machina;

import java.io.File;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.machina.api.ModIDs;
import com.machina.api.planet.trait.pool.PlanetTraitPoolManager;
import com.machina.api.registry.annotation.MachinaAnnotationProcessor;
import com.machina.api.tile_entity.MachinaEnergyStorage;
import com.machina.api.util.MachinaRL;
import com.machina.api.world.DynamicDimensionHelper;
import com.machina.api.world.data.PlanetDimensionData;
import com.machina.api.world.data.StarchartData;
import com.machina.client.ClientSetup;
import com.machina.config.ClientConfig;
import com.machina.config.CommonConfig;
import com.machina.config.ServerConfig;
import com.machina.init.CommandInit;
import com.machina.init.ItemInit;
import com.machina.network.MachinaNetwork;
import com.matyrobbrt.lib.ModSetup;
import com.matyrobbrt.lib.annotation.SyncValue;
import com.matyrobbrt.lib.registry.annotation.AnnotationProcessor;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import software.bernie.geckolib3.GeckoLib;

@Mod(Machina.MOD_ID)
public class Machina extends ModSetup {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = ModIDs.MACHINA;

	public static final MachinaRL MACHINA_ID = new MachinaRL(MOD_ID);

	public static final String CONFIG_DIR_PATH = "config/" + MOD_ID + "/";
	public static final File CONFIF_DIR = new File(CONFIG_DIR_PATH);

	public static final MachinaAnnotationProcessor ANNOTATION_PROCESSOR = new MachinaAnnotationProcessor(MOD_ID);

	public static PlanetTraitPoolManager planetTraitPoolManager = new PlanetTraitPoolManager();

	public Machina() {
		super(MOD_ID);
		GeckoLib.initialize();

		if (!CONFIF_DIR.exists()) {
			LOGGER.info("Created Machina config folder!");
			CONFIF_DIR.mkdirs();
		}

		ANNOTATION_PROCESSOR.afterInit(() -> {
			CommonConfig.register();
			ClientConfig.register();
			ServerConfig.register();
		});

		ANNOTATION_PROCESSOR.setAutoBlockItemTab(block -> MACHINA_ITEM_GROUP);

		forgeBus.addListener(EventPriority.HIGH, this::onServerStart);
		forgeBus.addListener(EventPriority.HIGH, this::onRegisterCommands);

		SyncValue.Helper.registerSerializer(MachinaEnergyStorage.class, MachinaEnergyStorage::fromNbt,
				(nbt, energy) -> energy.serialize(nbt));

		SyncValue.Helper.registerSerializer(FluidTank.class, nbt -> new FluidTank(nbt.getInt("TankCapacity")),
				(nbt, tank) -> {
					tank.writeToNBT(nbt);
					nbt.putInt("TankCapacity", tank.getCapacity());
				});
	}

	public static final ItemGroup MACHINA_ITEM_GROUP = new ItemGroup(ItemGroup.TABS.length, "machinaItemGroup") {

		@Override
		public ItemStack makeIcon() {
			// return BlockInit.ROCKET_PLATFORM_BLOCK.asItem().getDefaultInstance();
			return ItemInit.ITEM_GROUP_ICON.getDefaultInstance();
		}
	};

	public void onRegisterCommands(final RegisterCommandsEvent event) {
		CommandInit.registerCommands(event);
	}

	@Override
	public AnnotationProcessor annotationProcessor() {
		return ANNOTATION_PROCESSOR;
	}

	@Override
	public Optional<Supplier<com.matyrobbrt.lib.ClientSetup>> clientSetup() {
		return Optional.of(() -> new ClientSetup(modBus));
	}

	@Override
	public void onCommonSetup(final FMLCommonSetupEvent event) {
		MachinaNetwork.init();
		PlanetTraitPoolManager.INSTANCE = planetTraitPoolManager;
	}

	public static boolean isDevEnvironment() { return !FMLEnvironment.production; }

	// Load all saved data
	public void onServerStart(final FMLServerStartingEvent event) {

		MinecraftServer server = event.getServer();

		PlanetDimensionData.getDefaultInstance(server).dimensionIds
				.forEach(id -> DynamicDimensionHelper.createPlanet(server, id));

		StarchartData.getDefaultInstance(server).generateIf(server.getLevel(World.OVERWORLD).getSeed());
	}
}
