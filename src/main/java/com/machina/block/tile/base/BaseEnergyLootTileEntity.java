package com.machina.block.tile.base;

import java.util.Random;

import javax.annotation.Nullable;

import com.machina.util.server.ItemStackUtil;
import com.machina.util.text.StringUtils;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.LockCode;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class BaseEnergyLootTileEntity extends BaseEnergyTileEntity implements IInventory, INameable {
	private LockCode lockKey = LockCode.NO_LOCK;
	private ITextComponent name;
	@Nullable
	protected ResourceLocation lootTable;
	protected long lootTableSeed;

	protected final int slots;
	protected NonNullList<ItemStack> items;

	public BaseEnergyLootTileEntity(TileEntityType<?> type, int slots) {
		super(type);

		this.slots = slots;
		this.items = NonNullList.withSize(slots, ItemStack.EMPTY);
	}

	@Override
	public int getContainerSize() {
		return slots;
	}

	protected NonNullList<ItemStack> getItems() {
		return items;
	}

	protected void setItems(NonNullList<ItemStack> pItems) {
		this.items = pItems;
		this.sync();
	}

	@Override
	public void setItem(int pIndex, ItemStack pStack) {
		this.unpackLootTable((PlayerEntity) null);
		this.getItems().set(pIndex, pStack);
		if (pStack.getCount() > this.getMaxStackSize()) {
			pStack.setCount(this.getMaxStackSize());
		}

		this.setChanged();
		this.sync();
	}

	protected ITextComponent getDefaultName() {
		return StringUtils.EMPTY;
	}

	public ItemStack getItem(int id) {
		return this.items.get(id);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		this.lockKey.addToTag(compound);
		if (this.name != null) {
			compound.putString("CustomName", ITextComponent.Serializer.toJson(this.name));
		}

		if (!this.trySaveLootTable(compound)) {
			ItemStackUtil.saveAllItems(compound, this.items, "I");
		}
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.lockKey = LockCode.fromTag(compound);
		if (compound.contains("CustomName", 8)) {
			this.name = ITextComponent.Serializer.fromJson(compound.getString("CustomName"));
		}
		this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(compound)) {
			ItemStackUtil.loadAllItems(compound, this.items, "I");
		}
	}

	public static void setLootTable(IBlockReader pLevel, Random pRandom, BlockPos pPos, ResourceLocation pLootTable) {
		TileEntity tileentity = pLevel.getBlockEntity(pPos);
		if (tileentity instanceof LockableLootTileEntity) {
			((LockableLootTileEntity) tileentity).setLootTable(pLootTable, pRandom.nextLong());
		}

	}

	protected boolean tryLoadLootTable(CompoundNBT pTag) {
		if (pTag.contains("LootTable", 8)) {
			this.lootTable = new ResourceLocation(pTag.getString("LootTable"));
			this.lootTableSeed = pTag.getLong("LootTableSeed");
			return true;
		} else {
			return false;
		}
	}

	protected boolean trySaveLootTable(CompoundNBT pTag) {
		if (this.lootTable == null) {
			return false;
		} else {
			pTag.putString("LootTable", this.lootTable.toString());
			if (this.lootTableSeed != 0L) {
				pTag.putLong("LootTableSeed", this.lootTableSeed);
			}

			return true;
		}
	}

	public void unpackLootTable(@Nullable PlayerEntity pPlayer) {
		if (this.lootTable != null && this.level.getServer() != null) {
			LootTable loottable = this.level.getServer().getLootTables().get(this.lootTable);
			if (pPlayer instanceof ServerPlayerEntity) {
				CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayerEntity) pPlayer, this.lootTable);
			}

			this.lootTable = null;
			LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld) this.level))
					.withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(this.worldPosition))
					.withOptionalRandomSeed(this.lootTableSeed);
			if (pPlayer != null) {
				lootcontext$builder.withLuck(pPlayer.getLuck()).withParameter(LootParameters.THIS_ENTITY, pPlayer);
			}

			loottable.fill(this, lootcontext$builder.create(LootParameterSets.CHEST));
		}

	}

	public void setLootTable(ResourceLocation pLootTable, long pLootTableSeed) {
		this.lootTable = pLootTable;
		this.lootTableSeed = pLootTableSeed;
	}

	public boolean isEmpty() {
		this.unpackLootTable((PlayerEntity) null);
		return this.getItems().stream().allMatch(ItemStack::isEmpty);
	}

	public ItemStack removeItem(int pIndex, int pCount) {
		this.unpackLootTable((PlayerEntity) null);
		ItemStack itemstack = ItemStackHelper.removeItem(this.getItems(), pIndex, pCount);
		if (!itemstack.isEmpty()) {
			this.setChanged();
		}

		return itemstack;
	}

	public ItemStack removeItemNoUpdate(int pIndex) {
		this.unpackLootTable((PlayerEntity) null);
		return ItemStackHelper.takeItem(this.getItems(), pIndex);
	}

	public boolean stillValid(PlayerEntity pPlayer) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return !(pPlayer.distanceToSqr((double) this.worldPosition.getX() + 0.5D,
					(double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) > 64.0D);
		}
	}

	public void clearContent() {
		this.getItems().clear();
	}

	public boolean canOpen(PlayerEntity pPlayer) {
		return canUnlock(pPlayer, this.lockKey, this.getDisplayName())
				&& (this.lootTable == null || !pPlayer.isSpectator());
	}

	public void setCustomName(ITextComponent pName) {
		this.name = pName;
	}

	public ITextComponent getName() {
		return this.name != null ? this.name : this.getDefaultName();
	}

	public ITextComponent getDisplayName() {
		return this.getName();
	}

	@Nullable
	public ITextComponent getCustomName() {
		return this.name;
	}

	public static boolean canUnlock(PlayerEntity pPlayer, LockCode pCode, ITextComponent p_213905_2_) {
		if (!pPlayer.isSpectator() && !pCode.unlocksWith(pPlayer.getMainHandItem())) {
			pPlayer.displayClientMessage(new TranslationTextComponent("container.isLocked", p_213905_2_), true);
			pPlayer.playNotifySound(SoundEvents.CHEST_LOCKED, SoundCategory.BLOCKS, 1.0F, 1.0F);
			return false;
		} else {
			return true;
		}
	}

	private LazyOptional<?> itemHandler = LazyOptional.of(() -> createUnSidedHandler());

	protected IItemHandler createUnSidedHandler() {
		return new InvWrapper(this);
	}

	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction si) {
		if (!this.remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return itemHandler.cast();
		return super.getCapability(cap, si);
	}

	@Override
	protected void invalidateCaps() {
		super.invalidateCaps();
		itemHandler.invalidate();
	}
}
