package com.cy4.machina.init;

import com.cy4.machina.api.annotation.registries.RegistryHolder;
import com.cy4.machina.api.annotation.registries.RegisterBlock;
import com.cy4.machina.block.AnimatedBuilder;
import com.cy4.machina.block.AnimatedBuilderMount;
import com.cy4.machina.block.ConsoleBlock;
import com.cy4.machina.block.PadSizeRelay;
import com.cy4.machina.block.PumpBlock;
import com.cy4.machina.block.RocketBlock;
import com.cy4.machina.block.RocketMount;
import com.cy4.machina.block.TankBlock;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

@RegistryHolder
public class BlockInit {
    @RegisterBlock("rocket_platform_block")
    public static final Block ROCKET_PLATFORM_BLOCK = new Block(AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE));

    @RegisterBlock("animated_builder_mount")
    public static final Block ANIMATED_BUILDER_MOUNT = new AnimatedBuilderMount(AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE));

    @RegisterBlock("animated_builder")
    public static final Block ANIMATED_BUILDER = new AnimatedBuilder(AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE));

    @RegisterBlock("pad_size_relay")
    public static final Block PAD_SIZE_RELAY = new PadSizeRelay(AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE));

    @RegisterBlock("pad_console")
    public static final Block CONSOLE = new ConsoleBlock(AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE).noOcclusion());

    @RegisterBlock("rocket")
    public static final Block ROCKET = new RocketBlock(AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE).noOcclusion());
    
    @RegisterBlock("rocket_mount")
    public static final Block ROCKET_MOUNT = new RocketMount(AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE));
    
    
    
    @RegisterBlock("tank")
	public static final TankBlock TANK = new TankBlock(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK));

	@RegisterBlock("pump")
	public static final PumpBlock PUMP = new PumpBlock();
}
