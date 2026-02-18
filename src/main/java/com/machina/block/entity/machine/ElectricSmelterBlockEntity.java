package com.machina.block.entity.machine;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.menu.ElectricSmelterMenu;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ElectricSmelterBlockEntity extends MachinaBlockEntity {

	private RecipeHolder<SmeltingRecipe> recipe = null;
	private int progress = 0;

	public ElectricSmelterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public ElectricSmelterBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.ELECTRIC_SMELTER.get(), pos, state);
	}

	@Override
	public void createStorages() {
		energyStorage(Side.INPUTS);
		itemStorage(Side.INPUTS);
		itemStorage(Side.OUTPUTS);
	}

	@Override
	public boolean isLit() {
		return this.recipe != null && this.progress > 0 && this.meetsRequirements();
	}

	public float getProgress() {
		if (this.recipe == null)
			return 0;
		return (float) this.progress / (float) this.recipe.value().getCookingTime();
	}

	public int ticksRemaining() {
		if (this.recipe == null)
			return 0;
		return this.recipe.value().getCookingTime() - this.progress;
	}

	@Override
	public void tick() {
		if (this.level == null || this.level.isClientSide()) {
			return;
		}

		Optional<RecipeHolder<SmeltingRecipe>> rec = this.level.getRecipeManager().getRecipeFor(RecipeType.SMELTING,
				new SingleRecipeInput(this.getItem(0)), level);
		rec.ifPresentOrElse(r -> {
			if (this.recipe.id() != r.id()) {
				this.recipe = r;
				this.progress = 0;
				setChanged();
			}

			if (meetsRequirements() && hasSpace(r.value())) {
				if (!drainRequirements()) {
					return;
				}

				this.progress++;
				if (this.progress >= r.value().getCookingTime()) {
					useInputs(r.value());
					produceOutputs(r.value());
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

	protected boolean hasSpace(SmeltingRecipe r) {
		ItemStack stack = getItem(1);
		return stack.isEmpty() || ItemStack.isSameItem(stack, r.result)
				&& stack.getCount() + r.result.getCount() <= stack.getMaxStackSize();
	}

	public boolean hasRecipe() {
		return this.recipe != null;
	}

	public boolean hasSpace() {
		return this.recipe != null && hasSpace(this.recipe.value());
	}

	protected void useInputs(SmeltingRecipe r) {
		int desired = r.getIngredients().get(0).getItems()[0].getCount();
		ItemStack stack = getItem(0);
		stack.shrink(desired);
		setItem(0, stack);
	}

	protected void produceOutputs(SmeltingRecipe r) {
		ItemStack stack = getItem(1);
		ItemStack i = r.result.copy();
		if (stack.isEmpty()) {
			setItem(1, i);
		} else if (ItemStack.isSameItem(stack, i) && stack.getCount() + i.getCount() <= stack.getMaxStackSize()) {
			stack.grow(i.getCount());
			setItem(1, stack);
		}
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, Provider registries) {
		tag.putInt("progress", this.progress);
		tag.putString("recipe", this.recipe == null ? "NULL" : this.recipe.id().toString());
		super.saveAdditional(tag, registries);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadAdditional(@NotNull CompoundTag tag, Provider registries) {
		this.progress = tag.getInt("progress");
		String r = tag.getString("recipe");
		if (!r.isEmpty() && !r.equals("NULL") && this.level != null) {
			this.level.getRecipeManager().byKey(ResourceLocation.parse(r)).ifPresent(rx -> {
				if (rx.value() instanceof SmeltingRecipe) {
					this.recipe = (RecipeHolder<SmeltingRecipe>) rx;
				}
			});
		} else {
			this.recipe = null;
		}
		super.loadAdditional(tag, registries);
	}

	@Override
	public int getMaxEnergy() {
		// TODO: Config
		return 1_000_000;
	}

	public int getPowerRate() {
		// TODO: Config
		return 50;
	}

	@Override
	protected QuadFunction<Integer, Level, BlockPos, Inventory, AbstractContainerMenu> createMenu() {
		return ElectricSmelterMenu::new;
	}

	@Override
	public boolean activeModel() {
		return false;
	}
}
