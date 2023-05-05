package com.machina.item;

import java.util.List;

import com.machina.registration.Registration;
import com.machina.util.text.StringUtils;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;

public class ChemicalBlockItem extends BlockItem {
	private final String chem;

	public ChemicalBlockItem(Block b, Properties pProperties, String chem) {
		super(b, pProperties.tab(Registration.CHEMISTRY_GROUP));
		this.chem = chem;
	}

	@Override
	public void appendHoverText(ItemStack pStack, World pLevel, List<ITextComponent> tip, ITooltipFlag pFlag) {
		tip.add(StringUtils.toComp(StringUtils.chemical(chem))
				.setStyle(Style.EMPTY.withColor(Color.fromRgb(0x9D_AAAAAA))));
		super.appendHoverText(pStack, pLevel, tip, pFlag);
	}
}
