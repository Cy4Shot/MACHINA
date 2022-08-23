package com.machina.block;

import com.machina.block.tile.CargoCrateTileEntity;
import com.machina.client.model.CustomBlockModel;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class CargoCrateBlock extends Block implements IAnimatedBlock {

	public static final BooleanProperty OPEN = BooleanProperty.create("open");

	public CargoCrateBlock() {
		super(AbstractBlock.Properties.of(Material.HEAVY_METAL, MaterialColor.COLOR_GRAY).strength(-1.0F, 3600000.0F)
				.noDrops().noOcclusion().sound(SoundType.METAL));

		this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, Boolean.valueOf(false)));
	}

	@Override
	public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand,
			BlockRayTraceResult pHit) {
		if (!pLevel.isClientSide()) {
			if (pState.getValue(OPEN).booleanValue()) {
				TileEntity te = pLevel.getBlockEntity(pPos);
				if (te != null && te instanceof CargoCrateTileEntity) {
					CargoCrateTileEntity ccte = (CargoCrateTileEntity) te;
					if (pPlayer.canTakeItem(ccte.getItem(0))) {
						pPlayer.addItem(ccte.getItem(0));
						ccte.setItem(0, ItemStack.EMPTY);
					}
				}
			}
		}

		return ActionResultType.CONSUME;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext pContext) {
		return super.getStateForPlacement(pContext).setValue(OPEN, Boolean.valueOf(false));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
		pBuilder.add(OPEN);
	}

	@Override
	public BlockRenderType getRenderShape(BlockState pState) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityInit.CARGO_CRATE.get().create();
	}

	@Override
	public CustomBlockModel<?> getBlockModel() {
		return CustomBlockModel.create("cargo_crate");
	}
}