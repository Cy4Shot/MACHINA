package com.machina.block;

import javax.annotation.Nullable;

import com.machina.block.tile.machine.BatteryTileEntity;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.helper.BlockHelper;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BatteryBlock extends HorizontalFacingBlock {

	public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

	public BatteryBlock() {
		super(AbstractBlock.Properties.of(Material.HEAVY_METAL, MaterialColor.COLOR_GRAY).harvestLevel(2).strength(6f)
				.sound(SoundType.METAL));
		this.registerDefaultState(
				this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityInit.BATTERY.get().create();
	}

	@Override
	public ActionResultType use(BlockState pState, World level, BlockPos pos, PlayerEntity player, Hand pHand,
			BlockRayTraceResult pHit) {
		if (!level.isClientSide()) {
			BlockHelper.openGui(level, pos, player, BatteryTileEntity.class);
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext pContext) {
		return super.getStateForPlacement(pContext).setValue(LIT,
				Boolean.valueOf(pContext.getLevel().hasNeighborSignal(pContext.getClickedPos())));
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos p, Block bl, BlockPos fp, boolean im) {
		world.setBlock(p, state.setValue(LIT, Boolean.valueOf(world.getBestNeighborSignal(p) > 0)), 2);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(LIT);
		super.createBlockStateDefinition(pBuilder);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState pState) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, World world, BlockPos pos) {
		TileEntity te = world.getBlockEntity(pos);
		if (te instanceof BatteryTileEntity) {
			BatteryTileEntity bte = (BatteryTileEntity) te;
			return (int) ((float) bte.getEnergy() * 15 / (float) bte.getMaxEnergy());
		}
		return 0;
	}
}