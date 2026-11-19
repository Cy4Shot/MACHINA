package com.machina.block.machine.steam_turbine;

import com.machina.api.block.MultiblockBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.entity.machine.steam_turbine.SteamTurbineBladeBlockEntity;
import com.machina.block.menu.SteamTurbineMenu;
import com.machina.registration.init.BlockEntityInit;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.IItemHandler;

public class SteamTurbineBladeBlock extends MultiblockBlock {

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
		return Shapes.empty();
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
		return true;
	}

	@Override
	public boolean skipRendering(BlockState state, BlockState adjacent, net.minecraft.core.Direction dir) {
		return true;
	}

	@Override
	protected boolean isTickable() {
		return true;
	}

	public SteamTurbineBladeBlock(Properties props) {
		super(props);
	}

	@Override
	public boolean isMaster() {
		return false;
	}

	@Override
	public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
		return SteamTurbineBladeBlockEntity.class;
	}

	@Override
	public BlockEntityType<?> getBlockEntityType() {
		return BlockEntityInit.STEAM_TURBINE_BLADE.get();
	}

	@Override
	protected QuadFunction<Integer, Inventory, ContainerLevelAccess, IItemHandler, AbstractContainerMenu> createMenu() {
		return SteamTurbineMenu::new;
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return simpleCodec(SteamTurbineBladeBlock::new);
	}
}
