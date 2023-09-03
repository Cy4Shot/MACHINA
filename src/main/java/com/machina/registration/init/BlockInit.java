package com.machina.registration.init;

import java.util.function.Function;
import java.util.function.Supplier;

import com.machina.Machina;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			Machina.MOD_ID);

	public static final RegistryObject<Block> ALUMINUM_BLOCK = register("aluminum_block", Blocks.IRON_BLOCK);
	public static final RegistryObject<Block> ALUMINUM_ORE = register("aluminum_ore", Blocks.IRON_ORE);

	private static <T extends Block> Supplier<T> of(Block block, Function<Block.Properties, Block.Properties> extra,
			Function<Block.Properties, T> constructor) {
		return () -> constructor.apply((extra.apply(Block.Properties.copy(block))));
	}

	public static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
		return BLOCKS.<T>register(name, block);
	}

	public static RegistryObject<Block> register(String name, Block prop) {
		return register(name, prop, a -> a, Block::new);
	}

	public static <T extends Block> RegistryObject<T> register(String name, Block prop,
			Function<Block.Properties, T> constructor) {
		return register(name, prop, a -> a, constructor);
	}

	public static <T extends Block> RegistryObject<T> register(String name, Block prop,
			Function<Block.Properties, Block.Properties> extra, Function<Block.Properties, T> constructor) {
		RegistryObject<T> ro = register(name, BlockInit.<T>of(prop, extra, constructor));
		registerBlockItem(name, ro);
		return ro;
	}

	private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
		return ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
	}
}
