package com.machina.block.multiblock.haber;

import com.machina.block.multiblock.HorizontalMultiblockBlock;
import com.machina.block.tile.multiblock.haber.HaberControllerTileEntity;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.helper.BlockHelper;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class HaberControllerBlock extends HorizontalMultiblockBlock {

	public HaberControllerBlock() {
		super(Properties.of(Material.HEAVY_METAL, MaterialColor.COLOR_GRAY).harvestLevel(2).strength(6f)
				.sound(SoundType.METAL));
	}

	@Override
	public TileEntityType<?> getTE() {
		return TileEntityInit.HABER_CONTROLLER.get();
	}

	@Override
	public boolean isMaster() {
		return true;
	}

	@Override
	public ActionResultType use(BlockState pState, World level, BlockPos pos, PlayerEntity player, Hand pHand,
			BlockRayTraceResult pHit) {
		if (!level.isClientSide()) {
			BlockHelper.openGui(level, pos, player, HaberControllerTileEntity.class);
		}
		return ActionResultType.SUCCESS;
	}
}