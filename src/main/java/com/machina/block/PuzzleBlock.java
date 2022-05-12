package com.machina.block;

import com.machina.registration.init.TileEntityTypesInit;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class PuzzleBlock extends Block {

	public PuzzleBlock() {
		super(AbstractBlock.Properties.of(Material.HEAVY_METAL, MaterialColor.COLOR_GRAY).strength(-1.0F, 3600000.0F)
				.noDrops().noOcclusion().sound(SoundType.METAL));
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityTypesInit.PUZZLE.get().create();
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
}
