package com.machina.modules.rocket;

import static com.machina.api.ModIDs.MACHINA;
import static com.machina.api.module.ModuleHelper.cutout;

import com.machina.api.annotation.RL;
import com.machina.api.module.IModule;
import com.machina.api.module.Module;
import com.machina.api.registry.annotation.AutoBlockItem;
import com.machina.api.registry.annotation.RegisterBlock;
import com.machina.api.registry.annotation.RegisterTileEntityType;
import com.machina.modules.rocket.block.AnimatedBuilder;
import com.machina.modules.rocket.block.AnimatedBuilderMount;
import com.machina.modules.rocket.block.ConsoleBlock;
import com.machina.modules.rocket.block.PadSizeRelay;
import com.machina.modules.rocket.block.RocketBlock;
import com.machina.modules.rocket.block.RocketMount;
import com.machina.modules.rocket.client.RocketTileRenderer;
import com.machina.modules.rocket.tile_entity.RocketTile;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntityType;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Module(id = @RL(modid = MACHINA, path = "rocket"))
public class RocketModule implements IModule {

	@AutoBlockItem
	@RegisterBlock("rocket")
	public static final Block ROCKET_BLOCK = new RocketBlock();

	@AutoBlockItem
	@RegisterBlock("rocket_platform_block")
	public static final Block ROCKET_PLATFORM_BLOCK = new Block(AbstractBlock.Properties.copy(Blocks.GRAY_CONCRETE));

	@AutoBlockItem
	@RegisterBlock("animated_builder_mount")
	public static final Block ANIMATED_BUILDER_MOUNT_BLOCK = new AnimatedBuilderMount();

	@AutoBlockItem
	@RegisterBlock("animated_builder")
	public static final Block ANIMATED_BUILDER_BLOCK = new AnimatedBuilder();

	@AutoBlockItem
	@RegisterBlock("pad_size_relay")
	public static final Block PAD_SIZE_RELAY_BLOCK = new PadSizeRelay();

	@AutoBlockItem
	@RegisterBlock("pad_console")
	public static final Block CONSOLE_BLOCK = new ConsoleBlock();

	@AutoBlockItem
	@RegisterBlock("rocket_mount")
	public static final Block ROCKET_MOUNT_BLOCK = new RocketMount();

	@RegisterTileEntityType("rocket_tile")
	public static final TileEntityType<RocketTile> ROCKET_TILE = TileEntityType.Builder
			.of(RocketTile::new, ROCKET_BLOCK).build(null);

	@Override
	public void onClientSetup(FMLClientSetupEvent event) {
		cutout(RocketModule.ROCKET_BLOCK);
		ClientRegistry.bindTileEntityRenderer(RocketModule.ROCKET_TILE, RocketTileRenderer::new);
	}

}
