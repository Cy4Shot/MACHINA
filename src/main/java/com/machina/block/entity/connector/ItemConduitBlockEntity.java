package com.machina.block.entity.connector;

import com.machina.api.block.entity.ConnectorBlockEntity;
import com.machina.api.cap.item.ConduitItemStorage;
import com.machina.api.util.reflect.QuintFunction;
import com.machina.block.menu.connector.ItemConduitMenu;
import com.machina.config.CommonConfig;
import com.machina.registration.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class ItemConduitBlockEntity extends ConnectorBlockEntity<ItemStack, ConduitItemStorage> {

    public ItemConduitBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public ItemConduitBlockEntity(BlockPos pos, BlockState state) {
        this(BlockEntityInit.ITEM_CONDUIT.get(), pos, state);
    }

    @Override
    public int getRate() {
        return CommonConfig.conduitTransferRate.get();
    }

    @Override
    public ConduitItemStorage createStorage(Direction side) {
        return new ConduitItemStorage(this, side);
    }

    @Override
    public Capability<?> getCapability() {
        return ForgeCapabilities.ITEM_HANDLER;
    }

    @Override
    public QuintFunction<Integer, Level, BlockPos, Inventory, Direction, AbstractContainerMenu> getMenu() {
        return ItemConduitMenu::new;
    }

    @Override
    public int slotsPerSide() {
        return 1;
    }

}
