package com.machina.item.filter;

import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.machina.Machina;
import com.machina.api.cap.fluid.PipeFluidStorage;
import com.machina.api.item.ConnectorFilterItem;
import com.machina.item.menu.FluidFilterMenu;
import com.machina.registration.init.DataComponentsInit;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
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
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidFilterItem extends ConnectorFilterItem<FluidStack, PipeFluidStorage> {

	public FluidFilterItem(Properties props) {
		super(props.component(DataComponentsInit.FLUID, Fluids.EMPTY));
	}

	public static Fluid getFluid(ItemStack stack) {
		return stack.get(DataComponentsInit.FLUID);
	}

	public static ItemStack set(ItemStack stack, Fluid type, Mode mode) {
		stack.set(DataComponentsInit.FLUID, type);
		stack.set(DataComponentsInit.FILTER_MODE, mode);
		return stack;
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, TooltipContext ctx, @NotNull List<Component> tooltip,
			@NotNull TooltipFlag flag) {
		Fluid fluid = getFluid(stack);
		Mode mode = getMode(stack);
		if (fluid != Fluids.EMPTY) {
			int col = IClientFluidTypeExtensions.of(fluid).getTintColor(new FluidStack(fluid, 1));
			tooltip.add(Component.translatable(fluid.getFluidType().getDescriptionId())
					.setStyle(Style.EMPTY.withColor(col)));
			tooltip.add(mode.comp().setStyle(Style.EMPTY.withColor(65278)));
		} else {
			tooltip.add(Component.translatable(Machina.MOD_ID + ".tooltip.fluid_filter.empty")
					.setStyle(Style.EMPTY.withColor(65278)));
		}

		super.appendHoverText(stack, ctx, tooltip, flag);
	}

	@Override
	public boolean filter(ItemStack stack, FluidStack original) {
		Mode mode = getMode(stack);
		Fluid fluid = getFluid(stack);

		if (Objects.requireNonNull(mode) == Mode.BLACKLIST) {
			return !original.getFluid().equals(fluid);
		}
		return original.getFluid().equals(fluid);
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player,
			@NotNull InteractionHand hand) {
		if (level.isClientSide())
			return super.use(level, player, hand);

		((ServerPlayer) player).openMenu(new MenuProvider() {
			@Override
			public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
				return new FluidFilterMenu(id, inv, hand);
			}

			@Override
			public @NotNull Component getDisplayName() {
				return Component.empty();
			}
		}, buf -> buf.writeBoolean(hand == InteractionHand.MAIN_HAND));
		return InteractionResultHolder.consume(player.getItemInHand(hand));
	}
}