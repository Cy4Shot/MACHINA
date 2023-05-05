package com.machina.block.tinted;

import com.machina.block.tile.basic.TintedTileEntity;
import com.machina.item.TintedItem;
import com.machina.util.helper.PlanetHelper;

import net.minecraft.block.Block;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITinted {
	
	public int getTintIndex();
	
	public default void drop(World world, BlockPos pos, Block b) {
		if (!world.isClientSide()) {
			TileEntity te = world.getBlockEntity(pos);
			if (te != null && te instanceof TintedTileEntity) {
				int id = ((TintedTileEntity) te).id;
				if (id == -1) {
					RegistryKey<World> dim = world.dimension();
					if (PlanetHelper.isDimensionPlanet(dim)) {
						id = PlanetHelper.getId(dim);
					}
				}
				ItemStack stack = ((TintedItem) b.asItem()).getDefaultInstance();
				TintedItem.applyToStack(stack, id);
				world.addFreshEntity(
						new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack));
			}
		}
	}

}
