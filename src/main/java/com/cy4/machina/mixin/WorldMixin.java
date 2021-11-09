package com.cy4.machina.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cy4.machina.api.capability.trait.CapabilityPlanetTrait;
import com.cy4.machina.api.planet.PlanetUtils;
import com.cy4.machina.init.PlanetTraitInit;
import com.cy4.machina.world.DynamicDimensionFactory;

import net.minecraft.block.BlockState;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;

@Mixin(World.class)
public abstract class WorldMixin {
	
	private World thisWorld = (World) (Object) this;
	
	@Shadow
	protected abstract RegistryKey<World> dimension();

	@Shadow
	protected abstract boolean setBlock(BlockPos pPos, BlockState pState, int pFlags, int pRecursionLeft);
	
	@Inject(method = "dimensionType", at = @At("HEAD"), cancellable = true)
	private void dimensionTypeMixin(CallbackInfoReturnable<DimensionType> ci) {
		if (PlanetUtils.isDimensionPlanet(dimension()) && CapabilityPlanetTrait.worldHasTrait(thisWorld, PlanetTraitInit.SUPERHOT)) {
			ci.setReturnValue(thisWorld.getServer().registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getOrThrow(DynamicDimensionFactory.SUPERHOT_KEY));
		}
	}
}
