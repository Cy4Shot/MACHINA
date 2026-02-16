package com.machina.block.entity.machine;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.api.item.RocketItem;
import com.machina.api.item.RocketPartItem;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.api.rocket.part.impl.ChassisPart;
import com.machina.api.rocket.part.impl.FuelTankPart;
import com.machina.api.rocket.part.impl.LifeSupportPart;
import com.machina.api.rocket.part.impl.ShieldPart;
import com.machina.api.rocket.part.impl.ThrusterPart;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.menu.RocketAssemblyStationMenu;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.ItemInit;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;

public class RocketAssemblyStationBlockEntity extends MachinaBlockEntity {

    private int progress = 0;
    private RocketPart<?>[] parts = null;

    public RocketAssemblyStationBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public RocketAssemblyStationBlockEntity(BlockPos pos, BlockState state) {
        this(BlockEntityInit.ROCKET_ASSEMBLY_STATION.get(), pos, state);
    }

    @Override
    public void createStorages() {
        energyStorage(Side.INPUTS);
        itemStorage(Side.INPUTS);
        itemStorage(Side.INPUTS);
        itemStorage(Side.INPUTS);
        itemStorage(Side.INPUTS);
        itemStorage(Side.INPUTS);
    }

    public boolean areSlotsFilled() {
        for (RocketPartType type : RocketPartType.values()) {
            if (getPart(type) == null)
                return false;
        }
        return true;
    }

    public void startCrafting() {
        if (!areSlotsFilled())
            return;

        this.parts = new RocketPart<?>[5];
        for (RocketPartType type : RocketPartType.values()) {
            this.parts[type.ordinal()] = getPart(type);
            this.setItem(getSlot(type), ItemStack.EMPTY);
        }
        this.progress = getMaxProgress();
        this.setChanged();
    }

    @Override
    public boolean hasItemIO() {
        return false;
    }

    @Override
    public boolean activeModel() {
        return false;
    }

    public boolean hasPower() {
        return this.getEnergy() >= getPowerRate();
    }

    public int getPowerRate() {
        return 200;
    }

    protected boolean consumePower() {
        int consumed = consumeEnergy(getPowerRate());
        if (consumed < getPowerRate()) {
            receiveEnergy(consumed, false);
            return false;
        }
        return true;
    }

    public boolean isCrafting() {
        return this.progress > 0 && this.parts != null;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return 200;
    }

    public float getProgressPercent() {
        if (this.progress <= 0)
            return 0f;
        return 1f - ((float) this.progress / (float) getMaxProgress());
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.putBoolean("has_rocket_parts", this.parts != null);
        if (this.parts != null) {
            ListTag rocket_parts = new ListTag();
            for (RocketPart<?> part : this.parts) {
                rocket_parts.add(part.toNBT());
            }
            tag.put("rocket_parts", rocket_parts);
        }
        tag.putInt("progress", progress);
        super.saveAdditional(tag);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        this.progress = tag.getInt("progress");
        if (tag.getBoolean("has_rocket_parts")) {
            this.parts = new RocketPart<?>[5];
            ListTag rocket_parts = tag.getList("rocket_parts", Tag.TAG_COMPOUND);
            for (int i = 0; i < rocket_parts.size(); i++) {
                this.parts[i] = RocketPart.fromNBT(rocket_parts.getCompound(i));
            }
        } else {
            this.parts = null;
        }
        super.load(tag);
    }

    @Override
    public int getMaxEnergy() {
        // TODO: Config
        return 100_000;
    }

    @Override
    protected QuadFunction<Integer, Level, BlockPos, Inventory, AbstractContainerMenu> createMenu() {
        return RocketAssemblyStationMenu::new;
    }

    @Override
    public void tick() {
        if (this.level == null || this.level.isClientSide())
            return;
        if (!this.isCrafting())
            return;

        if (this.hasPower()) {
            if (!consumePower()) {
                return;
            }
            this.progress--;
            if (this.progress == 0) {
                ItemStack rocket = new ItemStack(ItemInit.ROCKET.get(), 1);
                for (RocketPartType type : RocketPartType.values()) {
                    RocketItem.setPart(rocket, type, this.parts[type.ordinal()]);
                }
                RocketItem.initProperties(rocket);

                Vec3 pos = this.getBlockPos().above().getCenter();
                ItemEntity itementity = new ItemEntity(level, pos.x, pos.y, pos.z, rocket);
                itementity.setDeltaMovement(level.random.triangle(0.0D, 0.11485000171139836D),
                        level.random.triangle(0.2D, 0.11485000171139836D),
                        level.random.triangle(0.0D, 0.11485000171139836D));
                level.addFreshEntity(itementity);
                this.parts = null;
            }
            this.setChanged();
        }
    }

    private int getSlot(RocketPartType type) {
        switch (type) {
        case THRUSTER:
            return 0;
        case FUEL_TANK:
            return 1;
        case CHASSIS:
            return 2;
        case LIFE_SUPPORT:
            return 3;
        case SHIELD:
            return 4;
        }
        return -1;
    }

    private <T, P extends RocketPart<?>> Optional<T> partGetter(RocketPartType type, Class<P> partClass,
            Function<P, T> getter) {
        RocketPart<?> part = getPart(type);
        if (part != null) {
            if (partClass.isInstance(part)) {
                return Optional.of(getter.apply(partClass.cast(part)));
            }
        }
        return Optional.empty();
    }

    public RocketPart<?> getPart(RocketPartType type) {
        ItemStack stack = getItem(getSlot(type));
        if (!stack.isEmpty()) {
            if (stack.getItem() instanceof RocketPartItem rpi) {
                return rpi.getRocketPart();
            }
        }
        return null;
    }

    public float getTotalMass() {
        return (float) Stream.of(RocketPartType.values()).map(this::getPart).filter(Objects::nonNull)
                .mapToDouble(RocketPart::getMass).sum();
    }

    public Optional<FluidStack> getTotalFuelType() {
        return partGetter(RocketPartType.THRUSTER, ThrusterPart.class, t -> new FluidStack(t.getFuel().fluid(), 1));
    }

    public Optional<Float> getTotalFuelEfficiency() {
        return partGetter(RocketPartType.THRUSTER, ThrusterPart.class, ThrusterPart::getFuelEfficiency);
    }

    public Optional<Integer> getTotalFuelStorage() {
        return partGetter(RocketPartType.FUEL_TANK, FuelTankPart.class, FuelTankPart::getFuelStorage);
    }

    public Optional<Integer> getTotalCoolantStorage() {
        return partGetter(RocketPartType.FUEL_TANK, FuelTankPart.class, FuelTankPart::getCoolantStorage);
    }

    public Optional<FluidStack> getTotalCoolantType() {
        return partGetter(RocketPartType.CHASSIS, ChassisPart.class, t -> new FluidStack(t.getCoolant().fluid(), 1));
    }

    public Optional<Float> getTotalCoolantEfficiency() {
        return partGetter(RocketPartType.CHASSIS, ChassisPart.class, ChassisPart::getCoolantEfficiency);
    }

    public Optional<Integer> getTotalSlots() {
        return partGetter(RocketPartType.LIFE_SUPPORT, LifeSupportPart.class, LifeSupportPart::getSlots);
    }

    public Optional<Float> getTotalMaxPressure() {
        return partGetter(RocketPartType.SHIELD, ShieldPart.class, ShieldPart::getMaxAtmPressure);
    }
}