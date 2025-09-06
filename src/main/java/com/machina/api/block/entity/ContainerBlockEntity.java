package com.machina.api.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.LockCode;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ContainerBlockEntity extends BaseBlockEntity implements WorldlyContainer {

    private LockCode lockKey = LockCode.NO_LOCK;
    protected final NonNullList<ItemStack> items = NonNullList.create();

    public ContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void itemStorage() {
        this.items.add(ItemStack.EMPTY);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        this.lockKey = LockCode.fromTag(tag);
        ContainerHelper.loadAllItems(tag, this.items);
        super.load(tag);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        ContainerHelper.saveAllItems(tag, this.items);
        this.lockKey.addToTag(tag);
        super.saveAdditional(tag);
    }

    public abstract boolean hasItemIO();

    LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.values());

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (hasItemIO() && side != null) {
            if (cap == ForgeCapabilities.ITEM_HANDLER && !this.remove) {
                return handlers[side.ordinal()].cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        for (LazyOptional<? extends IItemHandler> handler : handlers) {
            handler.invalidate();
        }
        super.invalidateCaps();
    }

    @Override
    public void reviveCaps() {
        handlers = SidedInvWrapper.create(this, Direction.values());
        this.items.clear();
        super.reviveCaps();
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public @NotNull ItemStack getItem(int pIndex) {
        return items.get(pIndex);
    }

    @Override
    public @NotNull ItemStack removeItem(int pIndex, int pCount) {
        ItemStack stack = ContainerHelper.removeItem(items, pIndex, pCount);
        if (!stack.isEmpty()) {
            this.setChanged();
        }
        return stack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int pIndex) {
        return ContainerHelper.takeItem(items, pIndex);
    }

    @Override
    public void setItem(int pIndex, @NotNull ItemStack pStack) {
        this.items.set(pIndex, pStack);
        if (pStack.getCount() > this.getMaxStackSize()) {
            pStack.setCount(this.getMaxStackSize());
        }
        this.setChanged();
    }

    public boolean canOpen(Player player) {
        return canUnlock(player, this.lockKey, Component.empty());
    }

    public static boolean canUnlock(Player player, LockCode lock, Component name) {
        if (!player.isSpectator() && !lock.unlocksWith(player.getMainHandItem())) {
            player.displayClientMessage(Component.translatable("container.isLocked", name), true);
            player.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction dir) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int p_19235_, @NotNull ItemStack p_19236_, Direction p_19237_) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int p_19239_, @NotNull ItemStack p_19240_, @NotNull Direction p_19241_) {
        return false;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        if ((this.level != null ? this.level.getBlockEntity(this.worldPosition) : null) != this) {
            return false;
        } else {
            return !(pPlayer.distanceToSqr((double) this.worldPosition.getX() + 0.5D,
                    (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

}
