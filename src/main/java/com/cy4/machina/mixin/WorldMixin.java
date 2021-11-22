package com.cy4.machina.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.BlockState;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(World.class)
public abstract class WorldMixin {

	@Shadow
	protected abstract RegistryKey<World> dimension();

	@Shadow
	protected abstract boolean setBlock(BlockPos pPos, BlockState pState, int pFlags, int pRecursionLeft);

	// @Inject(method = "dimensionType", at = @At("HEAD"), cancellable = true)
	// @SuppressWarnings("unused")
	// private void dimensionTypeMixin(CallbackInfoReturnable<DimensionType> ci) {
	// if (PlanetUtils.isDimensionPlanet(dimension())
	// && CapabilityPlanetTrait.worldHasTrait(((World) (Object) this),
	// PlanetTraitInit.SUPERHOT)) {
	// // TODO random crashes happen, find a better way
	// ci.setReturnValue(((World) (Object) this).getServer().registryAccess()
	// .registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY)
	// .getOrThrow(DynamicDimensionFactory.SUPERHOT_KEY));
	// }
	// }
}
