package com.cy4.machina.mixin;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.cy4.machina.api.capability.trait.CapabilityPlanetTrait;
import com.mojang.datafixers.DataFixer;

import net.minecraft.world.chunk.storage.ChunkLoader;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorld;

@Mixin(ChunkManager.class)
public abstract class ChunkManagerMixin extends ChunkLoader {

	protected ChunkManagerMixin(File pRegionFolder, DataFixer pFixerUpper, boolean pSync) {
		super(pRegionFolder, pFixerUpper, pSync);
	}

	@Shadow
	private ServerWorld level;

	@Inject(method = "saveAllChunks(Z)V", at = @At("HEAD"), cancellable = true)
	private void saveAllChunksMixin(boolean flush, CallbackInfo ci) {
		CapabilityPlanetTrait.syncCapabilityWithClients(level);
	}

}