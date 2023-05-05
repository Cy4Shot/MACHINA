package com.machina.block;

import com.machina.block.tile.machine.MelterTileEntity;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.helper.BlockHelper;
import com.machina.util.helper.SoundHelper;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class MelterBlock extends HorizontalFacingBlock {

	public MelterBlock() {
		super(AbstractBlock.Properties.of(Material.HEAVY_METAL, MaterialColor.COLOR_GRAY).harvestLevel(2).strength(6f)
				.sound(SoundType.METAL));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityInit.MELTER.get().create();
	}

	@Override
	public ActionResultType use(BlockState pState, World level, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult hit) {
		if (!level.isClientSide()) {
			TileEntity tank = level.getBlockEntity(pos);
			if (tank != null) {
				IFluidHandler handler = tank
						.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, hit.getDirection())
						.orElse(null);
				if (FluidUtil.interactWithFluidHandler(player, hand, handler)) {
					if (player instanceof ServerPlayerEntity)
						SoundHelper.playSoundFromServer((ServerPlayerEntity) player, SoundEvents.BUCKET_FILL);
				}
			}

			if (FluidUtil.getFluidHandler(player.getItemInHand(hand)).isPresent())
				return ActionResultType.SUCCESS;
			BlockHelper.openGui(level, pos, player, MelterTileEntity.class);
		}
		return ActionResultType.SUCCESS;
	}
}