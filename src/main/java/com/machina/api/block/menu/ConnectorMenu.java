package com.machina.api.block.menu;

import com.machina.api.block.entity.ConnectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ConnectorMenu<T extends ConnectorBlockEntity<?, ?>> extends MachinaContainerMenu<T> {

    public final Direction dir;

    public ConnectorMenu(MenuType<?> type, Level level, BlockPos pos, int id, Direction d) {
        super(type, level, pos, id);
        this.dir = d;
    }

    public int id(int index) {
        return this.be.getSlotForSide(dir, index);
    }

    public void setItem(int index, ItemStack item) {
        for (Slot x : this.slots) {
            if (x.index == index && x.container == this.be) {
                x.set(item);
            }
        }
    }

    @Override
    public BlockState getDefaultState() {
        return null;
    }
}
