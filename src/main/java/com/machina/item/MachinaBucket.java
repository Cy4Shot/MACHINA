package com.machina.item;
import java.util.List;
import java.util.function.Supplier;

import com.machina.util.StringUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import org.jetbrains.annotations.NotNull;

public class MachinaBucket extends BucketItem {

	private final String chem;

	public MachinaBucket(Fluid pContent, Properties pProperties, String chemical) {
		super(pContent, pProperties);
		this.chem = chemical;
	}

	public MachinaBucket(Supplier<? extends Fluid> supplier, Item.Properties builder, String chemical) {
		super(supplier, builder);
		this.chem = chemical;
	}
	
	@Override
	public void appendHoverText(@NotNull ItemStack stack, Level level, List<Component> tip, @NotNull TooltipFlag flag) {
		tip.add(Component.translatable(StringUtils.chemical(chem))
				.setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x9D_AAAAAA))));
		super.appendHoverText(stack, level, tip, flag);
	}
	
	@Override
	public ICapabilityProvider initCapabilities(@NotNull ItemStack stack, CompoundTag nbt) {
		return new FluidBucketWrapper(stack);
	}
}