package com.machina.client.screen.menu.item;

import com.machina.api.client.screen.IFilteredScreen;
import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MUI.MuiSlot;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.item.ConnectorFilterItem.Mode;
import com.machina.item.filter.ItemFilterItem;
import com.machina.item.menu.ItemFilterMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class ItemFilterScreen extends MachinaMenuScreen<ItemFilterMenu> implements IFilteredScreen {

	public ItemFilterScreen(ItemFilterMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void init() {
		super.init();

		int i = midWidth();
		int j = midHeight();

		clickAndHoverItem(i + 89, j + 34, i + 89 + 17, j + 34 + 17, () -> true, () -> MUI.uistr("item_filter.insert"),
				menu::insertItemFilter);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		drawInventory(gui, mx, my);
		drawMiniBackground(gui);

		int i1 = midWidth();
		int j1 = midHeight();
		Mode mode = ItemFilterItem.getMode(menu.stack);
		Item item = ItemFilterItem.getItem(menu.stack);

		MUI.drawCenteredString(gui, mode.comp()
        				.setStyle(Style.EMPTY.withColor(mode == Mode.BLACKLIST ? MUI.RED : MUI.GREEN).withBold(true))
        				.append(MUI.uistr("item_filter.for").withStyle(Style.EMPTY.withColor(MUI.CYAN).withBold(false))),
        		i1 + 117, j1 + 4);
		MUI.drawCenteredString(gui, Component.translatable(item.getDescriptionId())
				.setStyle(Style.EMPTY.withColor(MUI.WHITE).withBold(true)), i1 + 117, j1 + 6 + font.lineHeight);

		drawGhostSlot(gui, () -> false, mx, my, 89, 34, MuiSlot.DUST, "",
				(i, j) -> gui.renderItem(new ItemStack(menu.getCurrentFilter(), 1), i + 1, j + 1));

		drawToggle(gui, mx, my, 125, 34, mode == Mode.BLACKLIST, MuiSlot.BLACKLIST, MuiSlot.WHITELIST,
				x -> menu.toggleMode(), mode::comp);

		MUI.blitCommon(gui, i1 + 151, j1 + 40, 405, 13, 17, 6);
		MUI.blitCommon(gui, i1 + 64, j1 + 40, 422, 13, 17, 6);

		drawOverlay(gui);
    }

	@Override
	public Collection<FilterSlot> getFilterSlots() {
		int i = midWidth();
		int j = midHeight();
		return List.of(new FilterSlot(i + 90, j + 35, menu::insertItemFilter));
	}
}
