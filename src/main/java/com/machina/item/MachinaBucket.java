package com.machina.item;

import java.util.List;
import java.util.function.Supplier;

import com.machina.util.text.StringUtils;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

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
	public void appendHoverText(ItemStack pStack, World pLevel, List<ITextComponent> tip, ITooltipFlag pFlag) {
		tip.add(StringUtils.toComp(StringUtils.chemical(chem))
				.setStyle(Style.EMPTY.withColor(Color.fromRgb(0x9D_AAAAAA))));
		super.appendHoverText(pStack, pLevel, tip, pFlag);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		return new FluidBucketWrapper(stack);
	}
}