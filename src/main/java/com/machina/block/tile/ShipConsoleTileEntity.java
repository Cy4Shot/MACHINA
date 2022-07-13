package com.machina.block.tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.machina.block.ShipConsoleBlock;
import com.machina.block.container.ShipConstructContainer;
import com.machina.block.container.ShipLaunchContainer;
import com.machina.block.tile.base.BaseLockableTileEntity;
import com.machina.recipe.ShipConsoleRecipe;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.TileEntityTypesInit;
import com.machina.util.MachinaRL;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

public class ShipConsoleTileEntity extends BaseLockableTileEntity implements ITickableTileEntity {

	public int stage = 1, progress = 0, destination = 0, fuel = 0;
	public boolean isInProgress = false, completed = false;
	public List<BlockPos> erroredPos = new ArrayList<>();

	protected final IIntArray data = new IIntArray() {
		public int get(int index) {
			switch (index) {
			case 0:
				return ShipConsoleTileEntity.this.stage;
			case 1:
				return ShipConsoleTileEntity.this.progress;
			case 2:
				return ShipConsoleTileEntity.this.completed ? 1 : 0;
			case 3:
				return ShipConsoleTileEntity.this.destination;
			case 4:
				return ShipConsoleTileEntity.this.fuel;
			default:
				return 0;
			}
		}

		public void set(int index, int value) {
			switch (index) {
			case 0:
				ShipConsoleTileEntity.this.stage = value;
				break;
			case 1:
				ShipConsoleTileEntity.this.progress = value;
				break;
			case 2:
				ShipConsoleTileEntity.this.completed = (value == 1);
				break;
			case 3:
				ShipConsoleTileEntity.this.destination = value;
				break;
			case 4:
				ShipConsoleTileEntity.this.fuel = value;
			}
		}

		@Override
		public int getCount() {
			return 5;
		}
	};

	public ShipConsoleTileEntity(TileEntityType<?> type) {
		super(type, 4);
	}

	public ShipConsoleTileEntity() {
		this(TileEntityTypesInit.SHIP_CONSOLE.get());
	}

	public List<ItemStack> getItemsForStage() {
		try {
			ShipConsoleRecipe recipe = (ShipConsoleRecipe) RecipeInit
					.getRecipes(RecipeInit.SHIP_CONSOLE_RECIPE, this.level.getRecipeManager())
					.get(new MachinaRL("ship_console_" + stage));

			return recipe.createRequirements();
		} catch (Exception e) {
			return Collections.nCopies(4, ItemStack.EMPTY);
		}
	}

	public List<ItemStack> getMissingItems() {
		List<ItemStack> missing = new ArrayList<>();
		getItemsForStage().forEach(item -> {
			ItemStack item1 = item.copy();
			for (ItemStack stack : getItems()) {
				if (item1.getItem().equals(stack.getItem())) {
					item1.shrink(stack.getCount());
				}
			}
			if (!item1.isEmpty()) {
				missing.add(item1);
			}
		});
		return missing;
	}

	public ItemStack getItemForStage(int id) {
		return getItemsForStage().get(id);
	}

	public IIntArray getData() {
		return this.data;
	}

	public void buttonPressed() {
		scan();
		if (this.erroredPos.size() > 0)
			return;
		
		if (this.stage <= 5 && this.progress == 0) {
			this.isInProgress = true;
			clear();
		}
	}

	public void clear() {
		for (int i = 0; i < getContainerSize(); i++)
			setItem(i, ItemStack.EMPTY);
	}

	@Override
	public void tick() {
		if (this.level.isClientSide())
			return;

		if (this.isInProgress) {
			this.progress++;
			if (this.progress == 100) {
				if (this.stage == 5) {
					this.completed = true;
				} else {
					this.stage++;
				}
				this.isInProgress = false;
				this.progress = 0;
				sync();
			}
		}
	}

	public void scan() {
		int sizeX = 3;
		int sizeY = 6;
		int sizeZ = 3;
		int halfX = (sizeX - 1) / 2;

		Direction d = getBlockState().getValue(ShipConsoleBlock.FACING);
		BlockPos cur = getBlockPos().relative(d.getOpposite());
		BlockPos pos1 = cur.relative(d.getClockWise(), halfX);
		BlockPos pos2 = cur.relative(d.getCounterClockWise(), halfX).relative(d.getOpposite(), sizeZ - 1)
				.relative(Direction.UP, sizeY - 1);

		this.erroredPos.clear();
		for (BlockPos p : BlockPos.betweenClosed(pos1, pos2)) {
			if (!this.level.getBlockState(p.immutable()).isAir()) {
				this.erroredPos.add(p.immutable());
			}
		}

		sync();
	}
	
	public void refuel() {
		this.fuel += 100;
		
		sync();
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		if (this.completed) {
			return new ShipLaunchContainer(id, player, this);
		} else {
			return new ShipConstructContainer(id, player, this);
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {

		ListNBT poss = new ListNBT();
		this.erroredPos.forEach(pos -> {
			poss.add(NBTUtil.writeBlockPos(pos));
		});

		nbt.put("Errors", poss);
		nbt.putInt("Stage", this.stage);
		nbt.putInt("Progress", this.progress);
		nbt.putBoolean("InProgress", this.isInProgress);
		nbt.putBoolean("Completed", this.completed);
		nbt.putInt("Destination", this.destination);
		nbt.putInt("Fuel", this.fuel);

		return super.save(nbt);
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		this.erroredPos.clear();
		ListNBT cons = nbt.getList("Errors", Constants.NBT.TAG_COMPOUND);
		for (int j = 0; j < cons.size(); j++) {
			erroredPos.add(NBTUtil.readBlockPos(cons.getCompound(j)));
		}

		this.stage = nbt.getInt("Stage");
		this.progress = nbt.getInt("Progress");
		this.isInProgress = nbt.getBoolean("InProgress");
		this.completed = nbt.getBoolean("Completed");
		this.destination = nbt.getInt("Destination");
		this.fuel = nbt.getInt("Fuel");
		
		super.load(state, nbt);
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		BlockPos p = getBlockPos();
		int dist = 20;
		return new AxisAlignedBB(p.offset(-dist, -dist, -dist), p.offset(dist, dist, dist));
	}
}
