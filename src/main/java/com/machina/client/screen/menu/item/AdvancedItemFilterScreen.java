package com.machina.client.screen.menu.item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.IFilteredScreen;
import com.machina.api.client.screen.MUI;
import com.machina.api.client.screen.MUI.MuiSlot;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.item.ConnectorFilterItem.Mode;
import com.machina.item.filter.AdvancedItemFilterItem;
import com.machina.item.menu.AdvancedItemFilterMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;

public class AdvancedItemFilterScreen extends MachinaMenuScreen<AdvancedItemFilterMenu> implements IFilteredScreen {

	public AdvancedItemFilterScreen(AdvancedItemFilterMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void init() {
		super.init();

		int i = midWidth();
		int j = midHeight();

		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 9; c++) {
				int x = i + c * 20 + 26;
				int y = j + r * 20 - 40;
				clickAndHoverItem(x, y, x + 19, y + 19, () -> true, () -> MUI.uistr("item_filter.insert"),
						menu.insertItemFilter(r * 9 + c));
			}
		}
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		drawInventory(gui, mx, my);
		drawBackground(gui);

		int i = midWidth();
		int j = midHeight();
		Mode mode = AdvancedItemFilterItem.getMode(menu.stack);
		NonNullList<Item> items = AdvancedItemFilterItem.getItems(menu.stack);

		// Top Text
		MUI.drawCenteredString(gui, mode.comp()
				.setStyle(Style.EMPTY.withColor(mode == Mode.BLACKLIST ? MUI.RED : MUI.GREEN).withBold(true))
				.append(MUI.uistr("item_filter.for_colon").withStyle(Style.EMPTY.withColor(MUI.CYAN).withBold(false))),
				i + 117, j - 60);

		// Inventory
		MUI.blitCommon(gui, i + 27, j - 40, 0, 0, 179, 59);
		if (mx > i + 27 && mx < i + 27 + 179 && my > j - 40 && my < j - 40 + 59) {
			int mx1 = (mx - i - 27);
			int my1 = (my - j + 40);
			if (mx1 % 20 < 18 && my1 % 20 < 18)
				MUI.blitCommon(gui, mx1 / 20 * 20 + i + 27, my1 / 20 * 20 + j - 40, 368, 2, 19, 19);
		}
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 9; c++) {
				gui.renderFakeItem(items.get(r * 9 + c).getDefaultInstance(), i + c * 20 + 28, j + r * 20 - 39);
			}
		}

		// Toggle Blacklist
		drawToggle(gui, mx, my, 107, 34, mode == Mode.BLACKLIST, MuiSlot.BLACKLIST, MuiSlot.WHITELIST,
				x -> menu.toggleMode(), () -> AdvancedItemFilterItem.getMode(menu.stack).comp());
		MUI.blitCommon(gui, i + 133, j + 40, 405, 13, 17, 6);
		MUI.blitCommon(gui, i + 82, j + 40, 422, 13, 17, 6);

		drawOverlay(gui);
	}

	@Override
	public Collection<FilterSlot> getFilterSlots() {
		int i = midWidth();
		int j = midHeight();
		List<FilterSlot> builder = new ArrayList<>();
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 9; c++) {
				builder.add(new FilterSlot(i + c * 20 + 28, j + r * 20 - 39, menu.insertItemFilter(r * 9 + c)));
			}
		}
		return builder;
	}
}
