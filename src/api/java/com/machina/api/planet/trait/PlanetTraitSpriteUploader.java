/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license:
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
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
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

package com.machina.api.planet.trait;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.machina.api.util.MachinaRL;

import net.minecraft.client.renderer.texture.SpriteUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;

@OnlyIn(Dist.CLIENT)
public class PlanetTraitSpriteUploader extends SpriteUploader {

	private static final PlanetTraitSpriteUploader INSTANCE = null;

	public PlanetTraitSpriteUploader(TextureManager manager) {
		super(manager, new MachinaRL("textures/atlas/planet_traits.png"), "planet_trait");
	}

	@Override
	protected Stream<ResourceLocation> getResourcesToLoad() {
		return PlanetTrait.REGISTRY.getValues().stream().filter(PlanetTrait::exists).map(PlanetTrait::getRegistryName);
	}

	public TextureAtlasSprite get(PlanetTrait trait) {
		return this.getSprite(PlanetTrait.REGISTRY.getKey(trait));
	}

	/**
	 * This does not always return `null`. {@link #setInstance} will set the
	 * instance of this class when it is needed. This method <strong>will (should)
	 * not</strong> be called before {@link ColorHandlerEvent.Block}
	 */
	public static TextureAtlasSprite getFromInstance(PlanetTrait trait) {
		if (INSTANCE != null) { return INSTANCE.get(trait); }
		return null;
	}

	private static boolean instanceSet = false;

	public static void setInstance(PlanetTraitSpriteUploader instance) {
		if (!instanceSet) {
			try {
				Field instanceField = PlanetTraitSpriteUploader.class.getDeclaredField("INSTANCE");
				instanceField.setAccessible(true);
				FieldUtils.removeFinalModifier(instanceField);
				instanceField.set(PlanetTraitSpriteUploader.class, instance);
				instanceSet = true;
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		} else {
			throw new IllegalStateException(
					"Cannot set the instance of the PlanetTraitSpriteUploader when it was already set!");
		}
	}
}
