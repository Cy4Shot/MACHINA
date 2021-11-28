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

package com.machina.api.module;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * The base interface for modules. Any class inheriting this interface
 * <strong>has to</strong> have an empty public constructor (NO PARAMETER). <br>
 * For registering modules, please see the {@link Module} annotation. <br>
 * <br>
 * 
 * What are modules: <br>
 * Modules are parts of the mod. They are not separate to the mod though. <br>
 * Advantages of modules:
 * <ul>
 * <li>organisation: using modules, packages like (main <code>block</code>
 * packages) won't have hundreds of classes</li>
 * <li>ease of use: using modules, all classes that are related to some
 * behaviour are in one place, so they can be found very quickly</li>
 * </ul>
 * 
 * @author matyrobbrt
 *
 */
public interface IModule {

	default void onCommonSetup(final FMLCommonSetupEvent event) {

	}

	default void onClientSetup(final FMLClientSetupEvent event) {

	}

	default void register(final IEventBus modBus, final IEventBus forgeBus) {
		modBus.addListener(this::onCommonSetup);
		modBus.addListener(this::onClientSetup);
	}

	/*
	 * ===================================================================
	 * ========================= HELPER METHODS =============================
	 * ===================================================================
	 */

	static <M extends Container, U extends Screen & IHasContainer<M>> void registerScreen(
			ContainerType<? extends M> pType, ScreenManager.IScreenFactory<M, U> pFactory) {
		ScreenManager.register(pType, pFactory);
	}

}
