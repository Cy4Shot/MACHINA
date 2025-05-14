package com.machina.mixin.forge;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.machina.api.rocket.part.RocketPart;
import com.machina.registration.init.RegistryInit;
import com.machina.registration.init.RegistryInit.RocketPartCallbacks;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryManager;

@Mixin(targets = "net.minecraftforge.registries.GameData$ItemCallbacks")
public class MixinItemCallbacks {

	private static final ResourceLocation ROCKET_PART_TO_ITEM = RocketPartCallbacks.ROCKET_PART_TO_ITEM;

	@Inject(method = "onAdd", at = @At("HEAD"))
	private void onAddInject(IForgeRegistryInternal<Item> owner, RegistryManager stage, int id, ResourceKey<Item> key,
			Item item, Item oldItem, CallbackInfo ci) {
//		if (item instanceof RocketPartItem rpi) {
//			rpi.registerParts(getOrCreateMap(owner, stage), item);
//		}
	}

	@Inject(method = "onClear", at = @At("HEAD"))
	private void onClearInject(IForgeRegistryInternal<Item> owner, RegistryManager stage, CallbackInfo ci) {
//		getOrCreateMap(owner, stage).clear();
	}

	@SuppressWarnings("unchecked")
	private Map<RocketPart<?>, Item> getOrCreateMap(IForgeRegistryInternal<Item> owner, RegistryManager stage) {
		Map<RocketPart<?>, Item> map = owner.getSlaveMap(ROCKET_PART_TO_ITEM, Map.class);
		if (map == null) {
			ResourceKey<? extends Registry<RocketPart<?>>> key = RegistryInit.ROCKET_PARTS.getRegistryKey();
			map = stage.getRegistry(key).getSlaveMap(ROCKET_PART_TO_ITEM, Map.class);
			owner.setSlaveMap(ROCKET_PART_TO_ITEM, map);
		}
		return map;
	}
}