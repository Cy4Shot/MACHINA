package com.machina.api.block.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import com.machina.api.cap.sided.Side;
import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.recipe.MachinaRecipeMaps;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public abstract class RecipeBlockEntity extends MachinaBlockEntity {

	protected enum SlotType {
		INPUT,
		OUTPUT,
		EPHEMERAL,
	}

	private record RecipeSlot(int id, boolean item, SlotType type) {
	}

	private List<RecipeSlot> slots;
	private MachinaRecipe<?> recipe = null;
	private MachinaRecipe<?> temporaryRecipe = null;
	private int tickCount = 0;

	private int progress = 0;

	public RecipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	protected void itemSlot(SlotType type) {
		int id = -1;
		switch (type) {
		case INPUT:
			id = itemStorage(Side.INPUTS);
			break;
		case OUTPUT:
			id = itemStorage(Side.OUTPUTS);
			break;
		case EPHEMERAL:
			id = itemStorage(Side.NONES);
			break;
		}
		if (this.slots == null) {
			this.slots = new ArrayList<>();
		}
		slots.add(new RecipeSlot(id, true, type));
	}

	protected void fluidSlot(int capacity, Predicate<FluidStack> valid, SlotType type) {
		int id = -1;
		switch (type) {
		case INPUT:
			id = fluidStorage(capacity, valid, Side.INPUTS);
			break;
		case OUTPUT:
			id = fluidStorage(capacity, valid, Side.OUTPUTS);
			break;
		case EPHEMERAL:
			id = fluidStorage(capacity, valid, Side.NONES);
			break;
		}
		if (this.slots == null) {
			this.slots = new ArrayList<>();
		}
		slots.add(new RecipeSlot(id, false, type));
	}

	@Override
	public boolean isLit() {
		return this.recipe != null && this.progress > 0 && this.meetsRequirements(recipe)
				|| (this.temporaryRecipe != null && this.getRecipeMap().hasPeriodicConsumption()
						&& this.ticksRemaining() >= 0);
	}

	public float getProgress() {
		if (this.recipe == null)
			return 0;
		if (getRecipeMap().hasPeriodicConsumption()) {
			return (float) this.progress / (float) this.recipe.getPeriodicConsumption();
		}
		if (!getRecipe().maps().hasTime() || this.recipe.getTime() == 0)
			return 1;
		return (float) this.progress / (float) this.recipe.getTime();
	}

	public int ticksRemaining() {
		if (this.recipe == null)
			return 0;
		if (getRecipeMap().hasPeriodicConsumption()) {
			return this.recipe.getPeriodicConsumption() - this.progress;
		}
		return this.recipe.getTime() - this.progress;
	}

	@Override
	public void tick() {
		if (this.level != null && this.level.isClientSide())
			return;

		Optional<MachinaRecipe<RecipeBlockEntity>> rec = getRecipeMap().findRecipe(this);
		rec.ifPresentOrElse(r -> {
			if (tickCount == 0) {
				this.temporaryRecipe = r;
				tickCount = 2;
				setChanged();
			}
			if (this.recipe != r) {
				this.recipe = r;
				this.progress = 0;

				setChanged();
			}

			if (getRecipeMap().hasPeriodicConsumption() && r.getPeriodicConsumption() > 1) {
				if (this.progress == 0) {
					useInputs(r, false);
					produceOutputs(r, true);
				} else if (this.progress == r.getPeriodicConsumption() + 1) {
					useInputs(r, true);
					produceOutputs(r, false);
					this.progress = 0;
				} else {
					useInputs(r, true);
					produceOutputs(r, true);
				}
				this.progress++;
				setChanged();
			} else {
				if (meetsRequirements(r) && hasSpace(r)) {
					if (!drainRequirements(r)) {
						return;
					}

					this.progress++;
					if (this.progress >= r.getTime()) {
						useInputs(r, false);
						produceOutputs(r, false);
						this.progress = 0;
						setChanged();
					}
				}
			}
		}, () -> {
			this.progress = 0;
			this.recipe = null;
		});

		if (recipe == null && tickCount > 0) {
			tickCount--;
			if (tickCount == 0) {
				this.temporaryRecipe = null;
			}
			setChanged();
		}

		super.tick();
	}

	@SuppressWarnings("unchecked")
	private MachinaRecipeMaps<RecipeBlockEntity> getRecipeMap() {
		return (MachinaRecipeMaps<RecipeBlockEntity>) getRecipe().maps();
	}

	protected abstract RecipeRegistryObject<? extends RecipeBlockEntity> getRecipe();

	protected boolean meetsRequirements(MachinaRecipe<?> r) {
		boolean meets = true;
		if (getRecipe().maps().hasEnergy() && getEnergy() < r.getPowerRate()) {
			meets = false;
		}
		return meets;
	}

	protected boolean drainRequirements(MachinaRecipe<?> r) {
		if (getRecipe().maps().hasEnergy()) {
			int consumed = consumeEnergy(r.getPowerRate());
			if (consumed < r.getPowerRate()) {
				receiveEnergy(consumed, false);
				return false;
			}
		}
		return true;
	}

	protected boolean hasSpace(MachinaRecipe<?> r) {
		for (ItemStack i : r.getOutputItems()) {
			boolean found = false;
			for (RecipeSlot s : slots) {
				if (s.type == SlotType.OUTPUT && s.item) {
					ItemStack stack = getItem(s.id());
					if (stack.isEmpty() || ItemStack.isSameItem(stack, i)
							&& stack.getCount() + i.getCount() <= stack.getMaxStackSize()) {
						found = true;
						break;
					}
				}
			}
			if (!found)
				return false;
		}

		for (FluidStack f : r.getOutputFluids()) {
			boolean found = false;
			for (RecipeSlot s : slots) {
				if (s.type == SlotType.OUTPUT && !s.item) {
					FluidStack stack = getFluid(s.id());
					if (stack.isEmpty()
							|| stack.isFluidEqual(f) && stack.getAmount() + f.getAmount() <= getTankCapacity(s.id())) {
						found = true;
						break;
					}
				}
			}
			if (!found)
				return false;
		}
		return true;
	}

	protected void useInputs(MachinaRecipe<?> r, boolean periodic) {
		if (!periodic) {
			for (Ingredient i : r.getInputItems()) {
				int desired = i.getItems()[0].getCount();
				int count = 0;
				for (RecipeSlot s : slots) {
					if (s.type == SlotType.INPUT && s.item) {
						ItemStack stack = getItem(s.id());
						if (i.test(stack)) {
							int toShrink = Math.min(desired - count, stack.getCount());
							stack.shrink(toShrink);
							setItem(s.id(), stack);
							count += toShrink;
						}
						if (count >= desired)
							break;
					}
				}
			}
		}

		for (FluidStack f : r.getInputFluids()) {
			int desired = f.getAmount();
			int count = 0;
			for (RecipeSlot s : slots) {
				if (s.type == SlotType.INPUT && !s.item) {
					FluidStack stack = getFluid(s.id());
					if (stack.isFluidEqual(f)) {
						FluidStack filled = drain(s.id(), desired - count, FluidAction.EXECUTE);
						count += filled.getAmount();
					}
					if (count >= desired)
						break;
				}
			}
		}
	}

	protected void produceOutputs(MachinaRecipe<?> r, boolean periodic) {
		if (!periodic) {
			for (ItemStack i : r.getOutputItems()) {
				for (RecipeSlot s : slots) {
					if (s.type == SlotType.OUTPUT && s.item) {
						ItemStack stack = getItem(s.id());
						if (stack.isEmpty()) {
							setItem(s.id(), i.copy());
							break;
						} else if (ItemStack.isSameItem(stack, i)
								&& stack.getCount() + i.getCount() <= stack.getMaxStackSize()) {
							stack.grow(i.getCount());
							setItem(s.id(), stack);
							break;
						}
					}
				}
			}
		}

		for (FluidStack f : r.getOutputFluids()) {
			for (RecipeSlot s : slots) {
				if (s.type == SlotType.OUTPUT && !s.item) {
					FluidStack stack = getFluid(s.id());
					if (stack.isEmpty()) {
						fill(s.id(), f.copy(), FluidAction.EXECUTE);
						break;
					} else if (stack.isFluidEqual(f) && stack.getAmount() + f.getAmount() <= getTankCapacity(s.id())) {
						fill(s.id(), f, FluidAction.EXECUTE);
						break;
					}
				}
			}
		}
	}

	public boolean hasRecipe() {
		return this.temporaryRecipe != null || this.recipe != null;
	}

	public boolean hasSpace() {
		return this.recipe != null && hasSpace(this.recipe);
	}

	public boolean meetsRequirements() {
		return this.recipe != null && meetsRequirements(this.recipe)
				|| (this.temporaryRecipe != null && getRecipeMap().hasPeriodicConsumption());
	}

	public int getPowerRate() {
		return this.recipe == null ? 0 : this.recipe.getPowerRate();
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		tag.putInt("progress", this.progress);
		tag.putString("recipe", this.recipe == null ? "" : this.recipe.getId().toString());
		tag.putString("temporary", this.temporaryRecipe == null ? "" : this.temporaryRecipe.getId().toString());
		tag.putInt("tickCount", this.tickCount);
		super.saveAdditional(tag);
	}

	@Override
	public void load(@NotNull CompoundTag tag) {
		this.progress = tag.getInt("progress");
		String r = tag.getString("recipe");
		this.recipe = r.isEmpty() ? null : (MachinaRecipe<?>) getRecipeMap().getRecipe(new ResourceLocation(r));
		String t = tag.getString("temporary");
		this.temporaryRecipe = t.isEmpty() ? null
				: (MachinaRecipe<?>) getRecipeMap().getRecipe(new ResourceLocation(t));
		this.tickCount = tag.getInt("tickCount");
		super.load(tag);
	}

}
