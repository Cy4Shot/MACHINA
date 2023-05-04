package com.machina.block.multiblock.haber;

import com.machina.block.multiblock.MultiblockBlock;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.helper.SoundHelper;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class HaberPortBlock extends MultiblockBlock {

	public HaberPortBlock() {
		super(Properties.of(Material.HEAVY_METAL, MaterialColor.COLOR_GRAY).harvestLevel(2).strength(6f)
				.sound(SoundType.METAL));
	}

	@Override
	public TileEntityType<?> getTE() {
		return TileEntityInit.HABER_PORT.get();
	}

	@Override
	public boolean isMaster() {
		return false;
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

			if (FluidUtil.getFluidHandler(player.getItemInHand(hand)).isPresent() || player.isCreative())
				return ActionResultType.SUCCESS;
		}
		
		return ActionResultType.CONSUME;
	}
}