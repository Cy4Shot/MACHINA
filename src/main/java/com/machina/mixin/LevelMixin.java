package com.machina.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

@Mixin(Level.class)
public class LevelMixin {

	@Shadow
	public ResourceKey<Level> dimension() {
		throw new IllegalStateException("Mixin failed to shadow getItem()");
	}
}
