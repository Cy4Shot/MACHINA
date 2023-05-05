package com.machina.item;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.machina.registration.init.ItemInit;
import com.machina.util.StringUtils;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;

public class ShipComponentItem extends Item {

	public ShipComponentItem(Properties props) {
		super(props);
	}

	public static ShipComponentType getType(ItemStack stack) {
		return ShipComponentType.fromNBT(stack.getOrCreateTag(), "type");
	}

	public static void setType(ItemStack stack, ShipComponentType type) {
		type.toNBT(stack.getOrCreateTag(), "type");
	}

	public static String getNameForType(ShipComponentType type) {
		return StringUtils.translate("machina.ship_component." + type.getSerializedName());
	}

	public static String getNameForStage(int stage) {
		return getNameForType(ShipComponentType.fromId((byte) stage).get());
	}

	@Override
	public void appendHoverText(ItemStack pStack, World pLevel, List<ITextComponent> pTooltip, ITooltipFlag pFlag) {
		pTooltip.add(StringUtils.toComp(getNameForType(getType(pStack)))
				.setStyle(Style.EMPTY.withColor(Color.fromRgb(0x9D_00fefe))));

		super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
	}

	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
		if (!this.allowdedIn(group))
			return;
		for (ShipComponentType type : ShipComponentType.values()) {
			ItemStack stack = new ItemStack(this, 1);
			setType(stack, type);
			items.add(stack);
		}
	}

	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack = super.getDefaultInstance();
		setType(stack, ShipComponentType.UNIDENTIFIED);
		return stack;
	}

	public static ItemStack randomComponent() {
		ItemStack stack = new ItemStack(ItemInit.SHIP_COMPONENT.get(), 1);
		List<ShipComponentType> types = Arrays.asList(ShipComponentType.REACTOR, ShipComponentType.THRUSTERS,
				ShipComponentType.CORE, ShipComponentType.SHIELDS, ShipComponentType.LIFE_SUPPORT);
		setType(stack, types.get(new Random().nextInt(types.size())));
		return stack;
	}

	public enum ShipComponentType implements IStringSerializable {
		UNIDENTIFIED(0, "unidentified"),
		REACTOR(1, "reactor"),
		CORE(2, "core"),
		THRUSTERS(3, "thrusters"),
		LIFE_SUPPORT(4, "life_support"),
		SHIELDS(5, "shields");

		public final byte nbtID;
		private final String name;

		private ShipComponentType(int id, String n) {
			this.nbtID = (byte) id;
			this.name = n;
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}

		public static ShipComponentType fromNBT(CompoundNBT compoundNBT, String tagname) {
			byte id = 0;
			if (compoundNBT != null && compoundNBT.contains(tagname)) {
				id = compoundNBT.getByte(tagname);
			}
			return fromId(id).orElse(UNIDENTIFIED);
		}

		public static Optional<ShipComponentType> fromId(byte id) {
			for (ShipComponentType type : ShipComponentType.values()) {
				if (type.nbtID == id)
					return Optional.of(type);
			}
			return Optional.empty();
		}

		public void toNBT(CompoundNBT compoundNBT, String tagname) {
			compoundNBT.putByte(tagname, nbtID);
		}
	}
}