package com.machina.api.util.block;

import com.machina.api.cap.sided.ConnectionSide;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BlockProperties {
	public static final BooleanProperty LIT = BooleanProperty.create("lit");
	public static final IntegerProperty VARIANT_4 = IntegerProperty.create("variant", 0, 3);

	public static final EnumProperty<ConnectionSide> UP_SIDE = EnumProperty.create("up_side", ConnectionSide.class);
	public static final EnumProperty<ConnectionSide> DOWN_SIDE = EnumProperty.create("down_side", ConnectionSide.class);
	public static final EnumProperty<ConnectionSide> NORTH_SIDE = EnumProperty.create("north_side",
			ConnectionSide.class);
	public static final EnumProperty<ConnectionSide> EAST_SIDE = EnumProperty.create("east_side", ConnectionSide.class);
	public static final EnumProperty<ConnectionSide> SOUTH_SIDE = EnumProperty.create("south_side",
			ConnectionSide.class);
	public static final EnumProperty<ConnectionSide> WEST_SIDE = EnumProperty.create("west_side", ConnectionSide.class);
}
