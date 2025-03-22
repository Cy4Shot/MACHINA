package com.machina.client.screen.menu.connector;

import org.jetbrains.annotations.NotNull;

import com.machina.api.cap.sided.ConnectionSide;
import com.machina.api.cap.sided.Side;
import com.machina.api.client.screen.MachinaMenuScreen;
import com.machina.api.item.ConnectorFilterItem.Mode;
import com.machina.api.network.PacketSender;
import com.machina.api.network.c2s.C2SMenuSetItem;
import com.machina.api.network.c2s.C2SMenuToggleConnector;
import com.machina.block.menu.connector.ItemConduitMenu;
import com.machina.item.ItemFilterItem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class ItemConduitScreen extends MachinaMenuScreen<ItemConduitMenu> {

	public ItemConduitScreen(ItemConduitMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float pt, int mx, int my) {
		int id = menu.id(0);
		int i = midWidth();
		int j = midHeight();

		drawInventory(gui, mx, my);
		drawMiniBackground(gui);

		// Top slot
		drawNoFacingSlot(gui, id, mx, my, 107, 4, SpecialSlot.DROP, "item_conduit.filter");
		blitCommon(gui, i + 133, j + 10, 405, 13, 17, 6);
		blitCommon(gui, i + 82, j + 10, 422, 13, 17, 6);

		// Mode Slot
		drawToggle(gui, mx, my, 107, 34, ItemFilterItem.getMode(menu.getBlockEntity().getItem(id)) == Mode.BLACKLIST,
				SpecialSlot.BLACKLIST, SpecialSlot.WHITELIST, (val) -> {
					ItemStack stack = ItemFilterItem.set(menu.getBlockEntity().getItem(id), null,
							val ? Mode.BLACKLIST : Mode.WHITELIST);
					PacketSender
							.sendToServer(new C2SMenuSetItem(menu.id(0), stack, menu.getBlockEntity().getBlockPos()));
				}, () -> ItemFilterItem.getMode(menu.getBlockEntity().getItem(id)).comp());

		// IO Slot
		ConnectionSide side = menu.be.getConnection(menu.dir);
		if (side.isIO()) {
			drawToggleIO(gui, mx, my, 140, 34, side.isInput() ? Side.INPUT : Side.OUTPUT,
					() -> menu.be.getConnection(menu.dir).comp()
							.setStyle(Style.EMPTY.withColor(
									menu.be.getConnection(menu.dir) == ConnectionSide.INPUT ? 0x0377fc : 0xfc9003)),
					() -> {
						PacketSender.sendToServer(new C2SMenuToggleConnector(menu.dir, menu.be.getBlockPos()));
					});
		}

		drawOverlay(gui);
	}
}
