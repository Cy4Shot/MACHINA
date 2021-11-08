package com.cy4.machina.firesTesting;

import com.cy4.machina.Machina;
import com.cy4.machina.firesTesting.blocks.tiles.RocketTile;
import com.cy4.machina.init.BlockInit;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityTypesInit {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Machina.MOD_ID);

    /**
    public static final RegistryObject<TileEntityType<StrongBlockTileEntity>> STRONGBLOCK_TILE = TILE_ENTITY_TYPE.register(
            "strong_block_tile", () -> TileEntityType.Builder.of(StrongBlockTileEntity::new,
                    WarenaiBlocksInit.WARENAI_BLOCK_BLACK.get(),
                    WarenaiBlocksInit.WARENAI_BLOCK_BLACK_STAIRS.get(),
                    WarenaiBlocksInit.WARENAI_BLOCK_BLACK_SLAB.get(),
                    WarenaiBlocksInit.WARENAI_BLOCK_BLACK_FENCE.get(),
                    WarenaiBlocksInit.WARENAI_BLOCK_BLACK_WALL.get()
            ).build(null));
    **/

    public static final RegistryObject<TileEntityType<RocketTile>> ROCKET_TILE = TILE_ENTITY_TYPE.register(
            "rocket_tile", () -> TileEntityType.Builder.of(RocketTile::new, BlockInit.ROCKET).build(null));
}
