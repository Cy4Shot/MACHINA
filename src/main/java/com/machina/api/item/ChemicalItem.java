package com.machina.api.item;

import com.machina.Machina;
import com.machina.api.util.StringUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

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
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tip, TooltipFlag tooltipFlag) {
        if (!toolTipKey.isEmpty()) {
            tip.add(Component.translatable(Machina.MOD_ID + ".tooltip." + toolTipKey)
                    .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x9D_00fefe))));
        }
        tip.add(Component.literal(StringUtils.chemical(chem))
                .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x9D_AAAAAA))));
        super.appendHoverText(stack, context, tip, tooltipFlag);
    }
}