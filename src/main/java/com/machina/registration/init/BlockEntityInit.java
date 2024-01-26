package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister
			.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Machina.MOD_ID);

	@SuppressWarnings("unused")
	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String n,
			BlockEntityType.BlockEntitySupplier<T> s, Supplier<Block> b) {
		return BLOCK_ENTITY_TYPES.register(n, () -> BlockEntityType.Builder.of(s, b.get()).build(null));
	}

	@SuppressWarnings("unused")
	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerMany(String n,
			BlockEntityType.BlockEntitySupplier<T> s, Supplier<Block[]> b) {
		return BLOCK_ENTITY_TYPES.register(n, () -> BlockEntityType.Builder.of(s, b.get()).build(null));
	}
}
