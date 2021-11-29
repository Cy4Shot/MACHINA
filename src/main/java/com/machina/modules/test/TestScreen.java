package com.machina.modules.test;

import com.machina.api.client.gui.BaseScreen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class TestScreen extends BaseScreen<TestContainer> {

	protected TestScreen(TestContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pMenu, pPlayerInventory, pTitle, textureRL("test_screen"));
	}

}
