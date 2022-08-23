package com.machina.block;

import com.machina.block.tile.ComponentAnalyzerTileEntity;
import com.machina.client.model.CustomBlockModel;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ComponentAnalyzerBlock extends HorizontalFacingBlock implements IAnimatedBlock {

	public static final VoxelShape BASE = Block.box(1, 0, 1, 15, 4.6D, 15);
	public static final VoxelShape SHAPE_N = Block.box(1.6D, 4.6D, 11, 14.4D, 14.6D, 15);
	public static final VoxelShape SHAPE_E = Block.box(1, 4.6D, 1.6D, 5, 14.6D, 14.4D);
	public static final VoxelShape SHAPE_S = Block.box(1.6D, 4.6D, 1, 14.4D, 14.6D, 5);
	public static final VoxelShape SHAPE_W = Block.box(11, 4.6D, 1.6D, 15, 14.6D, 14.4D);

	public ComponentAnalyzerBlock() {
		super(AbstractBlock.Properties.of(Material.HEAVY_METAL, MaterialColor.COLOR_GRAY).harvestLevel(2).strength(6f)
				.noOcclusion().sound(SoundType.METAL));
	}

	@Override
	public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
		switch (pState.getValue(FACING)) {
		case EAST:
			return VoxelShapes.or(BASE, SHAPE_E);
		case SOUTH:
			return VoxelShapes.or(BASE, SHAPE_S);
		case WEST:
			return VoxelShapes.or(BASE, SHAPE_W);
		default:
			return VoxelShapes.or(BASE, SHAPE_N);
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityInit.COMPONENT_ANALYZER.get().create();
	}

	@Override
	public ActionResultType use(BlockState pState, World level, BlockPos pos, PlayerEntity player, Hand pHand,
			BlockRayTraceResult pHit) {
		if (!level.isClientSide()) {
			TileEntity te = level.getBlockEntity(pos);
			if (te instanceof ComponentAnalyzerTileEntity)
				NetworkHooks.openGui((ServerPlayerEntity) player, (ComponentAnalyzerTileEntity) te, pos);
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public CustomBlockModel<?> getBlockModel() {
		return CustomBlockModel.create("component_analyzer");
	}
}