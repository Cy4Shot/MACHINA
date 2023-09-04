package com.machina.api.item;

import java.util.List;

import com.machina.Machina;
import com.machina.api.util.StringUtils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ChemicalItem extends Item {

	private final String toolTipKey;
	private final String chem;

	public ChemicalItem(Properties pProperties, String toolTipKey, String chem) {
		super(pProperties);

		this.toolTipKey = toolTipKey;
		this.chem = chem;
	}

	public ChemicalItem(Properties pProperties, String chem) {
		super(pProperties);

		this.toolTipKey = "";
		this.chem = chem;
	}
	
	@Override
	public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> tip, @NotNull TooltipFlag flag) {
		if (!toolTipKey.equals("")) {
			tip.add(Component.translatable(Machina.MOD_ID + ".tooltip." + toolTipKey)
					.setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x9D_00fefe))));
		}
		tip.add(Component.literal(StringUtils.chemical(chem))
				.setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x9D_AAAAAA))));
		super.appendHoverText(stack, level, tip, flag);
	}
}