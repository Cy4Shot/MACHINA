package com.machina.item;

import java.util.List;

import com.machina.Machina;
import com.machina.registration.Registration;
import com.machina.util.StringUtils;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;

public class ChemicalItem extends Item {

	private final String toolTipKey;
	private final String chem;

	public ChemicalItem(Properties pProperties, String toolTipKey, String chem) {
		super(pProperties.tab(Registration.CHEMISTRY_GROUP));

		this.toolTipKey = toolTipKey;
		this.chem = chem;
	}

	public ChemicalItem(Properties pProperties, String chem) {
		super(pProperties.tab(Registration.CHEMISTRY_GROUP));

		this.toolTipKey = "";
		this.chem = chem;
	}

	@Override
	public void appendHoverText(ItemStack pStack, World pLevel, List<ITextComponent> tip, ITooltipFlag pFlag) {
		if (toolTipKey != "") {
			tip.add(StringUtils.translateComp(Machina.MOD_ID + ".tooltip." + toolTipKey)
					.setStyle(Style.EMPTY.withColor(Color.fromRgb(0x9D_00fefe))));
		}
		tip.add(StringUtils.toComp(StringUtils.chemical(chem))
				.setStyle(Style.EMPTY.withColor(Color.fromRgb(0x9D_AAAAAA))));
		super.appendHoverText(pStack, pLevel, tip, pFlag);
	}
}
