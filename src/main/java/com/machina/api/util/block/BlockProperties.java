package com.machina.api.util.block;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BlockProperties {
	public static final BooleanProperty LIT = BooleanProperty.create("lit");
	public static final IntegerProperty VARIANT_4 = IntegerProperty.create("variant", 0, 3);
}
