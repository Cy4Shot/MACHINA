package com.machina.block.entity.machine;

import java.util.Optional;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.menu.GrinderMenu;
import com.machina.recipe.GrinderRecipe;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.RecipeInit;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GrinderBlockEntity extends MachinaBlockEntity {

	private GrinderRecipe recipe = null;
	private int progress = 0;

	public GrinderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public GrinderBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.GRINDER.get(), pos, state);
	}

	@Override
	public void createStorages() {
		energyStorage(Side.INPUTS);
		itemStorage(Side.INPUTS);
		itemStorage(Side.OUTPUTS);
	}

	@Override
	public int getMaxEnergy() {
		return 1_000_000;
	}

	@Override
	public boolean isLit() {
		return this.recipe != null && this.progress > 0;
	}

	public float getProgress() {
		if (this.recipe == null)
			return 0;
		if (!this.recipe.hasTime() || this.recipe.getTime() == 0)
			return 1;
		return (float) this.progress / (float) this.recipe.getTime();
	}

	public int ticksRemaining() {
		if (this.recipe == null)
			return 0;
		return this.recipe.getTime() - this.progress;
	}

	@Override
	public void tick() {
		if (this.level.isClientSide())
			return;

		Optional<GrinderRecipe> rec = RecipeInit.GRINDER.maps().findRecipe(this);
		rec.ifPresentOrElse(r -> {
			this.recipe = r;

			if (hasPower(r) && hasSpace(r)) {
				int consumed = consumeEnergy(r.getPowerRate());
				if (consumed < r.getPowerRate()) {
					receiveEnergy(consumed, false);
					return;
				}

				this.progress++;
				if (this.progress >= r.getTime()) {
					getItem(0).shrink(1);
					if (getItem(1).isEmpty())
						setItem(1, r.getOutputItems().get(0));
					else
						getItem(1).grow(1);

					this.progress = 0;
					setChanged();
				}
			}
		}, () -> {
			this.progress = 0;
			this.recipe = null;
		});
		super.tick();
	}

	protected boolean hasPower(GrinderRecipe r) {
		return getEnergy() >= r.getPowerRate();
	}

	protected boolean hasSpace(GrinderRecipe r) {
		return getItem(1).isEmpty() || (getItem(1).getCount() < getItem(1).getMaxStackSize()
				&& ItemStack.isSameItem(getItem(1), r.getOutputItems().get(0)));
	}

	public boolean hasSpace() {
		if (this.recipe == null)
			return false;
		return hasSpace(this.recipe);
	}
	
	public boolean hasPower() {
		if (this.recipe == null)
			return false;
		return hasSpace(this.recipe);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		tag.putInt("progress", this.progress);
		tag.putString("recipe", this.recipe == null ? "" : this.recipe.getId().toString());
		super.saveAdditional(tag);
	}

	@Override
	public void load(CompoundTag tag) {
		this.progress = tag.getInt("progress");
		String r = tag.getString("recipe");
		this.recipe = r == "" ? null : RecipeInit.GRINDER.maps().getRecipe(new ResourceLocation(r));
		super.load(tag);
	}

	@Override
	protected QuadFunction<Integer, Level, BlockPos, Inventory, AbstractContainerMenu> createMenu() {
		return GrinderMenu::new;
	}

	@Override
	public boolean activeModel() {
		return false;
	}

}
