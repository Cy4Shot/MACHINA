package com.machina.item;

import java.util.List;

import com.machina.Machina;
import com.machina.util.text.StringUtils;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;

public class ChemicalItem extends Item {

	private final String toolTipKey;
	private final List<Pair<String, Integer>> compound;

	@SuppressWarnings("unchecked")
	public ChemicalItem(Properties pProperties, String toolTipKey, Pair<String, Integer>... els) {
		super(pProperties);

		this.toolTipKey = toolTipKey;
		this.compound = List.of(els);
	}

	@SuppressWarnings("unchecked")
	public ChemicalItem(Properties pProperties, Pair<String, Integer>... els) {
		super(pProperties);

		this.toolTipKey = "";
		this.compound = List.of(els);
	}

	@Override
	public void appendHoverText(ItemStack pStack, World pLevel, List<ITextComponent> tip, ITooltipFlag pFlag) {
		if (toolTipKey != "") {
			tip.add(StringUtils.translateComp(Machina.MOD_ID + ".tooltip." + toolTipKey)
					.setStyle(Style.EMPTY.withColor(Color.fromRgb(0x9D_00fefe))));
		}
		tip.add(StringUtils.toComp(StringUtils.buildCompound(compound))
				.setStyle(Style.EMPTY.withColor(Color.fromRgb(0x9D_AAAAAA))));
		super.appendHoverText(pStack, pLevel, tip, pFlag);
	}
}
