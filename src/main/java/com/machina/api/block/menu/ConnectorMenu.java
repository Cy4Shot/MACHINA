package com.machina.api.block.menu;

import com.machina.api.block.entity.ConnectorBlockEntity;
import com.machina.api.cap.sided.ConnectionSide;

import net.minecraft.core.Direction;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ConnectorMenu<T extends ConnectorBlockEntity<?, ?>> extends MachinaContainerMenu<T> {

	public final Direction dir;

	public ConnectorMenu(MenuType<?> type, int id, ContainerLevelAccess level, Direction d) {
		super(type, id, level);
		this.dir = d;
	}

	public int id(int index) {
		return this.access.evaluate((level, pos) -> {
			if (level.getBlockEntity(pos) instanceof ConnectorBlockEntity<?, ?> connector) {
				return connector.getSlotForSide(dir, index);
			}
			return -1;
		}).orElse(-1);
	}

	public void setItem(int index, ItemStack item) {
		for (Slot x : this.slots) {
			if (x.index == index) {
				x.set(item);
			}
		}
	}

	@Override
	public BlockState getDefaultState() {
		return null;
	}

	public ConnectionSide getConnection() {
		return this.access.evaluate((level, pos) -> level.getBlockEntity(pos)).map(e -> {
			if (e instanceof ConnectorBlockEntity<?, ?> cbe) {
				return cbe.getConnection(dir);
			}
			return ConnectionSide.NONE;
		}).orElse(ConnectionSide.NONE);
	}
}
