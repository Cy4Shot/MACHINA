package com.machina.api.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.screen.MUI;
import com.machina.api.rocket.RocketProps;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.api.rocket.part.impl.ChassisPart;
import com.machina.api.rocket.part.impl.FuelTankPart;
import com.machina.api.rocket.part.impl.LifeSupportPart;
import com.machina.api.rocket.part.impl.ShieldPart;
import com.machina.api.rocket.part.impl.ThrusterPart;
import com.machina.api.util.StringUtils;
import com.machina.client.bewlr.RocketBEWLR;
import com.machina.registration.init.DataComponentsInit;
import com.machina.registration.init.RocketPartInit;
import com.machina.rocket.RocketEntity;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class RocketItem extends Item {

	public RocketItem(Properties props) {
		super(props.stacksTo(1).component(DataComponentsInit.ROCKET_PROPS, RocketProps.NULL)
				.component(DataComponentsInit.ROCKET_PARTS, Map.of()));
	}

	public static RocketPart<?> getPart(ItemStack stack, RocketPartType type) {
		return stack.get(DataComponentsInit.ROCKET_PARTS).get(type);
	}

	public static void setPart(ItemStack stack, RocketPartType type, RocketPart<?> part) {
		Map<RocketPartType, RocketPart<?>> parts = new HashMap<>(stack.get(DataComponentsInit.ROCKET_PARTS));
		parts.put(type, part);
		stack.set(DataComponentsInit.ROCKET_PARTS, parts);
	}

	public static void initProperties(ItemStack stack) {
		ThrusterPart<?> thruster = (ThrusterPart<?>) Objects.requireNonNull(getPart(stack, RocketPartType.THRUSTER));
		FuelTankPart<?> fuel_tank = (FuelTankPart<?>) Objects.requireNonNull(getPart(stack, RocketPartType.FUEL_TANK));
		ChassisPart<?> chassis = (ChassisPart<?>) Objects.requireNonNull(getPart(stack, RocketPartType.CHASSIS));
		LifeSupportPart<?> life_support = (LifeSupportPart<?>) Objects
				.requireNonNull(getPart(stack, RocketPartType.LIFE_SUPPORT));
		ShieldPart<?> shield = (ShieldPart<?>) Objects.requireNonNull(getPart(stack, RocketPartType.SHIELD));

		setProperties(stack, RocketProps.fromParts(thruster, fuel_tank, chassis, life_support, shield));
	}

	public static void setProperties(ItemStack stack, RocketProps props) {
		stack.set(DataComponentsInit.ROCKET_PROPS, props);
	}

	private static RocketProps getProperties(ItemStack stack) {
		return stack.get(DataComponentsInit.ROCKET_PROPS);
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, TooltipContext context, @NotNull List<Component> tooltip,
			@NotNull TooltipFlag flag) {

		RocketProps props = getProperties(stack);
		if (props == null) {
			tooltip.add(Component.literal("ERROR: UNINITIALIZED ROCKET")
					.withStyle(Style.EMPTY.withBold(true).withColor(MUI.RED)));
		} else {
			Component c = Component.literal(": ");
			tooltip.add(MUI.uistr("rocket_assembly_station.mass").append(c)
					.append(Component.literal(StringUtils.formatMass(props.mass()))
							.withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))));
			tooltip.add(MUI.uistr("rocket_assembly_station.fuel_type").append(c)
					.append(StringUtils.fluid(props.fuelStack(), true)));
			tooltip.add(MUI.uistr("rocket_assembly_station.coolant_efficiency").append(c)
					.append(Component.literal(StringUtils.formatPercent(props.fuelEfficiency()))
							.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))));
			tooltip.add(MUI.uistr("rocket_assembly_station.fuel_capacity").append(c)
					.append(Component.literal(StringUtils.formatFluid(props.fuelStorage()))
							.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_1))));
			tooltip.add(MUI.uistr("rocket_assembly_station.coolant_capacity").append(c)
					.append(Component.literal(StringUtils.formatFluid(props.coolantStorage()))
							.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_2))));
			tooltip.add(MUI.uistr("rocket_assembly_station.coolant_type").append(c)
					.append(StringUtils.fluid(props.coolantStack(), true)));
			tooltip.add(MUI.uistr("rocket_assembly_station.coolant_efficiency").append(c)
					.append(Component.literal(StringUtils.formatPercent(props.coolantEfficiency()))
							.withStyle(Style.EMPTY.withBold(true).withColor(MUI.ACC_2))));
			tooltip.add(MUI.uistr("rocket_assembly_station.storage").append(c)
					.append(Component.literal(String.valueOf(props.coolantStorage()))
							.withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))));
			tooltip.add(MUI.uistr("rocket_assembly_station.max_pressure").append(c)
					.append(Component.literal(StringUtils.formatPressure(props.maxPressure()))
							.withStyle(Style.EMPTY.withBold(true).withColor(MUI.WHITE))));
		}
		super.appendHoverText(stack, context, tooltip, flag);
	}

	@Override
	public @NotNull ItemStack getDefaultInstance() {
		ItemStack stack = super.getDefaultInstance();
		setPart(stack, RocketPartType.CHASSIS, RocketPartInit.SIMPLE_CHASSIS.get());
		setPart(stack, RocketPartType.FUEL_TANK, RocketPartInit.SIMPLE_FUEL_TANK.get());
		setPart(stack, RocketPartType.LIFE_SUPPORT, RocketPartInit.SIMPLE_LIFE_SUPPORT.get());
		setPart(stack, RocketPartType.SHIELD, RocketPartInit.SIMPLE_SHIELD.get());
		setPart(stack, RocketPartType.THRUSTER, RocketPartInit.SIMPLE_THRUSTER.get());
		return stack;
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return RocketBEWLR.INSTANCE;
			}
		});
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		ItemStack itemstack = ctx.getItemInHand();
		if (!(ctx.getLevel() instanceof ServerLevel)) {
			return InteractionResult.SUCCESS;
		} else if (ctx.getClickedFace().equals(Direction.UP)) {
			BlockPos blockpos = ctx.getClickedPos();
			if (ctx.getLevel().mayInteract(ctx.getPlayer(), blockpos)
					&& ctx.getPlayer().mayUseItemAt(blockpos, Direction.UP, itemstack)) {
				RocketEntity rocket = new RocketEntity(ctx.getLevel(), getProperties(itemstack));
				rocket.setPos(blockpos.above().getCenter().subtract(0, 0.5D, 0));
				rocket.setYRot(ctx.getHorizontalDirection().getOpposite().toYRot());
				ctx.getLevel().addFreshEntity(rocket);

				if (!ctx.getPlayer().getAbilities().instabuild) {
					itemstack.shrink(1);
				}

				ctx.getPlayer().awardStat(Stats.ITEM_USED.get(this));
				ctx.getLevel().gameEvent(ctx.getPlayer(), GameEvent.ENTITY_PLACE, rocket.position());
				return InteractionResult.CONSUME;
			}
		}

		return InteractionResult.FAIL;
	}
}
