package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.block.tile.machine.TestBE;
import com.machina.block.tile.multiblock.TestMasterMBE;
import com.machina.block.tile.multiblock.TestPartMBE;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister
			.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Machina.MOD_ID);

	public static final RegistryObject<BlockEntityType<TestBE>> TEST = register("test", TestBE::new,
			() -> BlockInit.TEST.get());
	public static final RegistryObject<BlockEntityType<TestMasterMBE>> TEST_MASTER = register("test_master",
			TestMasterMBE::new, () -> BlockInit.TEST_MASTER.get());
	public static final RegistryObject<BlockEntityType<TestPartMBE>> TEST_PART = register("test_part", TestPartMBE::new,
			() -> BlockInit.TEST_PART.get());

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