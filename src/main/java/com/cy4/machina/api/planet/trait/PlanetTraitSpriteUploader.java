package com.cy4.machina.api.planet.trait;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.cy4.machina.util.MachinaRL;

import net.minecraft.client.renderer.texture.SpriteUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;

/**
 * The owner of the mod doesnt like it he can get rid of it.
 * @deprecated
 * @author matyrobbrt
 *
 */
@Deprecated
@OnlyIn(Dist.CLIENT)
public class PlanetTraitSpriteUploader extends SpriteUploader {

	private static final PlanetTraitSpriteUploader INSTANCE = null;

	public PlanetTraitSpriteUploader(TextureManager manager) {
		super(manager, new MachinaRL("textures/atlas/planet_traits.png"), "planet_trait");
	}

	@Override
	protected Stream<ResourceLocation> getResourcesToLoad() { return PlanetTrait.REGISTRY.getKeys().stream(); }

	public TextureAtlasSprite get(PlanetTrait trait) {
		return this.getSprite(PlanetTrait.REGISTRY.getKey(trait));
	}

	/**
	 * This does not always return `null`. {@link #setInstance} will set the
	 * instance of this class when it is needed. This method <strong>will (should)
	 * not</strong> be called before {@link ColorHandlerEvent.Block}
	 */
	public static TextureAtlasSprite getFromInstance(PlanetTrait trait) {
		if (INSTANCE != null) {
			return INSTANCE.get(trait);
		}
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
