package com.machina.api.item;

import java.util.List;
import java.util.function.Supplier;

import com.machina.api.rocket.part.RocketPart;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class RocketPartItem extends Item {

	private final Supplier<RocketPart<?>> part;

	public RocketPartItem(Properties props, Supplier<RocketPart<?>> part) {
		super(props.stacksTo(1));
		this.part = part;
	}

	@Override
	public void appendHoverText(ItemStack s, Level l, List<Component> def, TooltipFlag flag) {
		super.appendHoverText(s, l, def, flag);
	}

	public RocketPart<?> getRocketPart() {
		return part.get();
	}

}
