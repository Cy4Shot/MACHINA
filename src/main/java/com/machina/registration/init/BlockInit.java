package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.block.CargoCrateBlock;
import com.machina.block.ShipConsoleBlock;
import com.machina.registration.Registration;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			Machina.MOD_ID);

	public static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
		return BLOCKS.register(name, block);
	}

	public static final RegistryObject<ShipConsoleBlock> SHIP_CONSOLE = register("ship_console",
			() -> new ShipConsoleBlock());
	
	public static final RegistryObject<CargoCrateBlock> CARGO_CRATE = register("cargo_crate",
			() -> new CargoCrateBlock());

	public static final RegistryObject<Block> ALIEN_STONE = register("alien_stone",
			() -> new Block(AbstractBlock.Properties.copy(Blocks.STONE)));

	public static final RegistryObject<Block> TWILIGHT_DIRT = register("twilight_dirt",
			() -> new Block(AbstractBlock.Properties.copy(Blocks.DIRT)));

	public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
		BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> {
			event.getRegistry()
					.register(new BlockItem(block, new Item.Properties().tab(Registration.MACHINA_ITEM_GROUP))
							.setRegistryName(block.getRegistryName()));
		});
	}
}
