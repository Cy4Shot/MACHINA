package com.machina.block.entity.machine;

import com.google.common.base.Predicates;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.api.util.ItemStackUtil;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.menu.TankMenu;
import com.machina.registration.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

public class TankBlockEntity extends MachinaBlockEntity {

    public TankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public TankBlockEntity(BlockPos pos, BlockState state) {
        this(BlockEntityInit.TANK.get(), pos, state);
    }

    @Override
    public void createStorages() {
        fluidStorage(10_000, Predicates.alwaysTrue(), Side.BOTHS);
        itemStorage(Side.INPUTS);
        itemStorage(Side.OUTPUTS);
    }

    @Override
    public void tick() {
        if (this.level != null && this.level.isClientSide()) {
            return;
        }

        // Fluid IN
        ItemStack input = getItem(0);
        if (ItemStackUtil.hasFluid(input)) {
            input.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(storage -> {
                FluidStack extracted = storage.drain(Integer.MAX_VALUE, FluidAction.SIMULATE);
                int inserted = this.fill(0, extracted, FluidAction.EXECUTE);
                storage.drain(inserted, FluidAction.EXECUTE);
                if (extracted.getAmount() > 0) {
                    setItem(0, storage.getContainer());
                    this.setChanged();
                }
            });

        }

        // Fluid OUT
        ItemStack output = getItem(1);
        if (ItemStackUtil.hasFluid(output)) {
            output.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(storage -> {
                FluidStack extracted = this.drain(0, Integer.MAX_VALUE, FluidAction.SIMULATE);
                int inserted = storage.fill(extracted, FluidAction.EXECUTE);
                this.drain(0, inserted, FluidAction.EXECUTE);
                if (extracted.getAmount() > 0) {
                    setItem(1, storage.getContainer());
                    this.setChanged();
                }
            });
        }
    }

    @Override
    protected QuadFunction<Integer, Level, BlockPos, Inventory, AbstractContainerMenu> createMenu() {
        return TankMenu::new;
    }

    @Override
    public int getMaxEnergy() {
        return 0;
    }

    @Override
    public boolean activeModel() {
        return true;
    }

    public boolean clicked(@NotNull ServerPlayer player, @NotNull InteractionHand hand, ItemStack stack) {
        LazyOptional<IFluidHandlerItem> fluidHandlerItem = FluidUtil.getFluidHandler(stack.copyWithCount(1));
        if (fluidHandlerItem.isPresent()) {
            IFluidHandlerItem handler = fluidHandlerItem.resolve().get();
            FluidStack fluidInItem;
            for (int tank = 0; tank < getTanks(); tank++) {
                FluidStack fluid = getFluid(tank);
                if (fluid.isEmpty()) {
                    fluidInItem = handler.drain(Integer.MAX_VALUE, FluidAction.SIMULATE);
                } else {
                    fluidInItem = handler.drain(new FluidStack(fluid.getFluid(), Integer.MAX_VALUE),
                            FluidAction.SIMULATE);
                }
                if (fluidInItem.isEmpty()) {
                    if (!fluid.isEmpty()) {
                        int filled = handler.fill(fluid,
                                player.isCreative() ? FluidAction.SIMULATE : FluidAction.EXECUTE);
                        ItemStack container = handler.getContainer();
                        if (filled > 0) {
                            if (stack.getCount() == 1) {
                                player.setItemInHand(hand, container);
                            } else if (stack.getCount() > 1 && player.getInventory().add(container)) {
                                stack.shrink(1);
                            } else {
                                player.drop(container, false, true);
                                stack.shrink(1);
                            }
                            if (player.isCreative()) {
                                IFluidHandlerItem newHandler = FluidUtil.getFluidHandler(stack.copyWithCount(1))
                                        .resolve().get();
                                newHandler.fill(fluid, FluidAction.EXECUTE);
                                container = newHandler.getContainer();
                                if (!player.getInventory().add(container)) {
                                    player.drop(container, false, true);
                                }
                            }
                            this.drain(tank, filled, FluidAction.EXECUTE);
                            player.playNotifySound(SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 1f, 1f);
                            return true;
                        }
                    }
                } else {
                    int filledAmount = fill(tank, fluidInItem, IFluidHandler.FluidAction.SIMULATE);
                    if (filledAmount > 0) {
                        boolean filled = false;
                        FluidStack fluidToFill = handler.drain(new FluidStack(fluidInItem.getFluid(), filledAmount),
                                player.isCreative() ? IFluidHandler.FluidAction.SIMULATE
                                        : IFluidHandler.FluidAction.EXECUTE);
                        if (!fluidToFill.isEmpty()) {
                            ItemStack container = handler.getContainer();
                            if (player.isCreative()) {
                                filled = true;
                            } else if (!container.isEmpty()) {
                                if (stack.getCount() == 1) {
                                    player.setItemInHand(hand, container);
                                    filled = true;
                                } else if (player.getInventory().add(container)) {
                                    stack.shrink(1);
                                    filled = true;
                                }
                            } else {
                                stack.shrink(1);
                                if (stack.isEmpty()) {
                                    player.setItemInHand(hand, ItemStack.EMPTY);
                                }
                                filled = true;
                            }
                            if (filled) {
                                fill(tank, fluidToFill, IFluidHandler.FluidAction.EXECUTE);
                                player.playNotifySound(SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1f, 1f);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
