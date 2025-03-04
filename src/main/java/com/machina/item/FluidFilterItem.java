package com.machina.item;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.machina.api.cap.fluid.PipeFluidStorage;
import com.machina.api.item.ConnectorFilterItem;
import com.machina.item.menu.FluidFilterMenu;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidFilterItem extends ConnectorFilterItem<FluidStack, PipeFluidStorage> {

	private static final String FLUID = "fluid";
	private static final String MODE = "mode";

	public FluidFilterItem(Properties props) {
		super(props);
	}

	public static Fluid getFluid(ItemStack stack) {
		CompoundTag nbt = stack.getOrCreateTag();
		if (nbt.contains(FLUID)) {
			ResourceLocation fluidName = new ResourceLocation(nbt.getString(FLUID));
			Fluid f = ForgeRegistries.FLUIDS.getValue(fluidName);
			if (f != null)
				return f;
		}
		return Fluids.EMPTY;
	}

	public static Mode getMode(ItemStack stack) {
		CompoundTag nbt = stack.getOrCreateTag();
		if (nbt.contains(MODE)) {
			try {
				return Mode.valueOf(nbt.getString(MODE));
			} catch (IllegalArgumentException e) {
				// Do nothing here :)
			}
		}
		return Mode.BLACKLIST;
	}

	public static void set(ItemStack stack, Fluid type, Mode mode) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putString(FLUID, ForgeRegistries.FLUIDS.getKey(type).toString());
		tag.putString(MODE, mode.name());
		stack.setTag(tag);
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> tooltip,
			@NotNull TooltipFlag flag) {
//		Fluid fluid = getFluid(stack);
//		Mode mode = getMode(stack);
//		if (fluid != Fluids.EMPTY) {
//			tooltip.add(bp.getName().setStyle(Style.EMPTY.withColor(65278)));
//		} else {
//			tooltip.add(bp.getName().setStyle(Style.EMPTY.withColor(65278)));
//		}

		super.appendHoverText(stack, level, tooltip, flag);
	}

	@Override
	public boolean filter(ItemStack stack, FluidStack original) {
		Mode mode = getMode(stack);
		Fluid fluid = getFluid(stack);

		switch (mode) {
		case BLACKLIST:
			return !original.getFluid().equals(fluid);
		default:
			return original.getFluid().equals(fluid);
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (level.isClientSide())
			return super.use(level, player, hand);

		ItemStack stack = player.getItemInHand(hand);
		NetworkHooks.openScreen((ServerPlayer) player, new MenuProvider() {
			@Override
			public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
				return new FluidFilterMenu(id, inv, stack);
			}

			@Override
			public Component getDisplayName() {
				return Component.empty();
			}
		}, buf -> {
			buf.writeBoolean(hand == InteractionHand.MAIN_HAND);
		});
		return InteractionResultHolder.consume(stack);
	}
}