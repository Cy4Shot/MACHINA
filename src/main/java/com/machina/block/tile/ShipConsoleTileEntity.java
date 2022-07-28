package com.machina.block.tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.machina.block.ShipConsoleBlock;
import com.machina.block.container.ShipConstructContainer;
import com.machina.block.container.ShipLaunchContainer;
import com.machina.block.tile.base.BaseLockableTileEntity;
import com.machina.block.tile.base.IFluidTileEntity;
import com.machina.config.CommonConfig;
import com.machina.network.MachinaNetwork;
import com.machina.network.s2c.S2CLaunchShip;
import com.machina.recipe.ShipConsoleRecipe;
import com.machina.registration.init.AttributeInit;
import com.machina.registration.init.RecipeInit;
import com.machina.registration.init.SoundInit;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.MachinaRL;
import com.machina.util.math.DirectionUtil;
import com.machina.util.server.ServerHelper;
import com.machina.world.data.PlanetData;
import com.machina.world.data.StarchartData;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;

public class ShipConsoleTileEntity extends BaseLockableTileEntity implements ITickableTileEntity {

	public int stage = 1, progress = 0, destination = -1;
	public int waterFuel = 0, aluminiumFuel = 0, ammoniaNitrateFuel = 0;
	public int hWaterFuel = 0, hAluminiumFuel = 0, hAmmoniaNitrateFuel = 0;
	public int animTick = 0;
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
			}
		}

		@Override
		public int getCount() {
			return 4;
		}
	};

	public ShipConsoleTileEntity(TileEntityType<?> type) {
		super(type, 4);
	}

	public ShipConsoleTileEntity() {
		this(TileEntityInit.SHIP_CONSOLE.get());
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
		for (Direction d : Direction.values()) {
			TileEntity te = level.getBlockEntity(worldPosition.relative(d));
			if (te != null && te instanceof FuelStorageUnitTileEntity) {
				FuelStorageUnitTileEntity fte = (FuelStorageUnitTileEntity) te;
				FluidStack water = fte.getFluid();
				ItemStack aluminium = fte.getItem(0);
				ItemStack ammonium_nitrate = fte.getItem(1);

				if (!water.isEmpty() && this.hWaterFuel < this.waterFuel) {
					int displaced = water.getAmount()
							- Math.max(0, (water.getAmount() + this.hWaterFuel) - this.waterFuel);
					water.setAmount(water.getAmount() - displaced);
					this.hWaterFuel += displaced;
				}

				if (!aluminium.isEmpty() && this.hAluminiumFuel < this.aluminiumFuel) {
					int displaced = aluminium.getCount()
							- Math.max(0, (aluminium.getCount() + this.hAluminiumFuel) - this.aluminiumFuel);
					aluminium.setCount(aluminium.getCount() - displaced);
					this.hAluminiumFuel += displaced;
				}

				if (!ammonium_nitrate.isEmpty() && this.hAmmoniaNitrateFuel < this.ammoniaNitrateFuel) {
					int displaced = ammonium_nitrate.getCount() - Math.max(0,
							(ammonium_nitrate.getCount() + this.hAmmoniaNitrateFuel) - this.ammoniaNitrateFuel);
					ammonium_nitrate.setCount(ammonium_nitrate.getCount() - displaced);
					this.hAmmoniaNitrateFuel += displaced;
				}
			}
		}

		sync();
	}
	
	public void launchAnim(int tick) {
		this.animTick = tick;
		sync();
	}

	public void calculateFuel() {
		if (this.level.isClientSide())
			return;

		PlanetData dest = StarchartData.getDataForDimension(ServerHelper.server(), this.destination);
		PlanetData orig = StarchartData.getDataForDimension(ServerHelper.server(), this.level.dimension());
		float dist = dest.getAttribute(AttributeInit.DISTANCE) - orig.getAttribute(AttributeInit.DISTANCE);
		float temp = dest.getAttribute(AttributeInit.TEMPERATURE) + orig.getAttribute(AttributeInit.TEMPERATURE);
		float atmo = dest.getAttribute(AttributeInit.ATMOSPHERIC_PRESSURE)
				+ orig.getAttribute(AttributeInit.ATMOSPHERIC_PRESSURE);

		this.ammoniaNitrateFuel = (int) (dist * CommonConfig.ammoniaNitrateMult.get());
		this.aluminiumFuel = (int) (atmo * CommonConfig.aluminiumMult.get());
		this.waterFuel = (int) (temp * CommonConfig.waterMult.get()) * IFluidTileEntity.BUCKET;

		sync();
	}

	public void launch(UUID id) {
		ServerPlayerEntity play = ServerHelper.server().getPlayerList().getPlayer(id);
		Direction dir = this.getBlockState().getValue(ShipConsoleBlock.FACING);
		BlockPos p = worldPosition.relative(dir, -2);
		play.connection.teleport(p.getX() + 0.5D, p.getY() + 2.6D, p.getZ() + 0.5D, DirectionUtil.toYaw(dir), 0);
		this.level.playSound(null, p.above(2), SoundInit.ROCKET_LAUNCH.sound(), SoundCategory.MASTER, 1f, 1f);
		MachinaNetwork.sendTo(MachinaNetwork.CHANNEL, new S2CLaunchShip(worldPosition), play);
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
		nbt.putInt("WaterFuel", this.waterFuel);
		nbt.putInt("AluminiumFuel", this.aluminiumFuel);
		nbt.putInt("AmmoniaNitrateFuel", this.ammoniaNitrateFuel);
		nbt.putInt("HasWaterFuel", this.hWaterFuel);
		nbt.putInt("HasAluminiumFuel", this.hAluminiumFuel);
		nbt.putInt("HasAmmoniaNitrateFuel", this.hAmmoniaNitrateFuel);
		nbt.putInt("AnimTick", this.animTick);

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
		this.waterFuel = nbt.getInt("WaterFuel");
		this.aluminiumFuel = nbt.getInt("AluminiumFuel");
		this.ammoniaNitrateFuel = nbt.getInt("AmmoniaNitrateFuel");
		this.hWaterFuel = nbt.getInt("HasWaterFuel");
		this.hAluminiumFuel = nbt.getInt("HasAluminiumFuel");
		this.hAmmoniaNitrateFuel = nbt.getInt("HasAmmoniaNitrateFuel");
		this.animTick = nbt.getInt("AnimTick");

		super.load(state, nbt);
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		BlockPos p = getBlockPos();
		int dist = 20;
		return new AxisAlignedBB(p.offset(-dist, -dist, -dist), p.offset(dist, dist, dist));
	}
}
