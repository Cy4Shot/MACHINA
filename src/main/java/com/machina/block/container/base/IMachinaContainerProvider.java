package com.machina.block.container.base;

import com.machina.util.text.StringUtils;

import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;

public interface IMachinaContainerProvider extends INamedContainerProvider {
	@Override
	default ITextComponent getDisplayName() {
		return StringUtils.EMPTY;
	}
}
