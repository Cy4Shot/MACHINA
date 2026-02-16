package com.machina.block.entity.machine;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.api.util.block.BlockHelper;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.menu.ChemicalGeneratorMenu;
import com.machina.config.CommonConfig;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.FluidInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;

public class ChemicalGeneratorBlockEntity extends MachinaBlockEntity {

    // TODO: Make this data driven somehow.
    @SuppressWarnings("serial")
    private static final Map<Fluid, Integer> BURNABLES = new HashMap<>() {
        {
            put(FluidInit.HYDROGEN.fluid(), 100);
            put(FluidInit.METHANE.fluid(), 98);
            put(FluidInit.ETHANE.fluid(), 95);
            put(FluidInit.ETHYLENE.fluid(), 90);
            put(FluidInit.AMMONIA.fluid(), 80);
            put(FluidInit.CARBON_MONOXIDE.fluid(), 70);
            put(FluidInit.FORMALDEHYDE.fluid(), 60);
            put(FluidInit.METHANOL.fluid(), 60);
            put(FluidInit.ETHANOL.fluid(), 67);
            put(FluidInit.TOLUENE.fluid(), 70);
            put(FluidInit.BENZENE.fluid(), 70);
            put(FluidInit.NITROMETHANE.fluid(), 60);
            put(FluidInit.ACETALDEHYDE.fluid(), 50);
            put(FluidInit.BENZYLAMINE.fluid(), 50);
            put(FluidInit.ACETIC_ACID.fluid(), 40);
            put(FluidInit.BENZYL_CHLORIDE.fluid(), 30);
        }
    };

    public ChemicalGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public ChemicalGeneratorBlockEntity(BlockPos pos, BlockState state) {
        this(BlockEntityInit.CHEMICAL_GENERATOR.get(), pos, state);
    }

    @Override
    public void createStorages() {
        energyStorage(Side.OUTPUTS);
        fluidStorage(16_000, f -> BURNABLES.containsKey(f.getFluid()), Side.INPUTS);
    }

    @Override
    public boolean isLit() {
        return !getFluid(0).isEmpty() && !this.isEnergyFull();
    }

    public int getRate() {
        FluidStack fluid = getFluid(0).copy();
        return BURNABLES.getOrDefault(fluid.getFluid(), 0);
    }

    @Override
    public void tick() {
        if (this.level != null && this.level.isClientSide())
            return;

        FluidStack fluid = getFluid(0).copy();
        int rate = BURNABLES.getOrDefault(fluid.getFluid(), 0);

        if (this.isLit()) {
            receiveEnergy(rate, false);
            if (fluid.getAmount() > 0) {
                fluid.setAmount(fluid.getAmount() - 1);
                setFluid(0, fluid);
            } else {
                setFluid(0, FluidStack.EMPTY);
            }
        }

        BlockHelper.sendEnergy(level, worldPosition, getEnergy(), CommonConfig.chemicalGeneratorTransferRate.get(),
                this);

        sync();

        super.tick();
    }

    @Override
    public int getMaxEnergy() {
        return CommonConfig.chemicalGeneratorCapacity.get();
    }

    @Override
    protected QuadFunction<Integer, Level, BlockPos, Inventory, AbstractContainerMenu> createMenu() {
        return ChemicalGeneratorMenu::new;
    }

    @Override
    public boolean activeModel() {
        return false;
    }
}
