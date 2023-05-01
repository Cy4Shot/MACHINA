package com.machina.block;

import com.machina.block.tile.machine.FabricatorTileEntity;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.helper.BlockHelper;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class FabricatorBlock extends HorizontalFacingBlock {

	public FabricatorBlock() {
		super(AbstractBlock.Properties.of(Material.HEAVY_METAL, MaterialColor.COLOR_GRAY).harvestLevel(2).strength(6f)
				.noOcclusion().sound(SoundType.METAL));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityInit.FABRICATOR.get().create();
	}

	@Override
	public ActionResultType use(BlockState pState, World level, BlockPos pos, PlayerEntity player, Hand pHand,
			BlockRayTraceResult pHit) {
		if (!level.isClientSide()) {
			BlockHelper.openGui(level, pos, player, FabricatorTileEntity.class);
		}
		return ActionResultType.SUCCESS;
	}
}