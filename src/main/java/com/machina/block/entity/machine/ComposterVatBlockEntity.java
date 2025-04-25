package com.machina.block.entity.machine;

import org.jetbrains.annotations.NotNull;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.menu.ComposterVatMenu;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.FluidInit;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class ComposterVatBlockEntity extends MachinaBlockEntity {

	private int progress = 0;

	public ComposterVatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public ComposterVatBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.COMPOSTER_VAT.get(), pos, state);
	}

	@Override
	public void createStorages() {
		energyStorage(Side.INPUTS);
		itemStorage(Side.INPUTS);
		fluidStorage(16_000, s -> s.getFluid().equals(FluidInit.AMMONIA.fluid()), Side.OUTPUTS);
	}

	public int getPowerRate() {
		// TODO: Config
		return 30;
	}

	public boolean meetsRequirements() {
		return getEnergy() >= getPowerRate();
	}

	protected boolean drainRequirements() {
		int consumed = consumeEnergy(getPowerRate());
		if (consumed < getPowerRate()) {
			receiveEnergy(consumed, false);
			return false;
		}
		return true;
	}

	private int getVal(ItemStack stack) {
		if (!ComposterBlock.COMPOSTABLES.containsKey(stack.getItem()))
			return 0;
		return (int) (ComposterBlock.COMPOSTABLES.get(stack.getItem()) * 1000);
	}

	public boolean hasRecipe() {
		return ComposterBlock.COMPOSTABLES.containsKey(getItem(0).getItem());
	}

	public boolean hasSpace() {
		return hasSpace(getVal(getItem(0)));
	}

	protected boolean hasSpace(int val) {
		return getFluid(0).getAmount() + val < getTankCapacity(0);
	}

	@Override
	public void tick() {
		if (this.level != null && this.level.isClientSide())
			return;

		if (hasRecipe()) {
			int val = getVal(getItem(0));
			if (meetsRequirements() && hasSpace(val)) {
				if (!drainRequirements()) {
					return;
				}

				this.progress++;
				if (this.progress >= 100) {
					getItem(0).shrink(1);
					fill(0, new FluidStack(FluidInit.AMMONIA.fluid(), val), FluidAction.EXECUTE);
					this.progress = 0;
					setChanged();
				}
			}
		} else {
			this.progress = 0;
		}
		super.tick();
	}

	@Override
	public boolean isLit() {
		return hasRecipe() && this.progress > 0 && meetsRequirements();
	}

	public float getProgress() {
		return (float) this.progress / (float) 100;
	}

	public int ticksRemaining() {
		return 100 - this.progress;
	}

	@Override
	public int getMaxEnergy() {
		// TODO: Config
		return 1_000_000;
	}

	@Override
	protected QuadFunction<Integer, Level, BlockPos, Inventory, AbstractContainerMenu> createMenu() {
		return ComposterVatMenu::new;
	}

	@Override
	public boolean activeModel() {
		return false;
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		tag.putInt("progress", this.progress);
		super.saveAdditional(tag);
	}

	@Override
	public void load(@NotNull CompoundTag tag) {
		this.progress = tag.getInt("progress");
		super.load(tag);
	}
}
