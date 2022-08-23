package com.machina.block;

import com.machina.block.tile.CustomModelTileEntity;
import com.machina.client.model.CustomBlockModel;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class CustomModelBlock extends Block implements IAnimatedBlock {

	final String model;

	public CustomModelBlock(Properties props, String model) {
		super(props.noOcclusion().isViewBlocking(BlockInit::never));
		this.model = model;
	}

	@Override
	public CustomBlockModel<?> getBlockModel() {
		return CustomBlockModel.create(model);
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		CustomModelTileEntity cmte = TileEntityInit.CUSTOM_MODEL.get().create();
		cmte.name = model;
		return cmte;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
}