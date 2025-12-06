package com.machina.api.item;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import com.machina.api.client.bewlr.RocketBEWLR;
import com.machina.api.client.screen.MUI;
import com.machina.api.rocket.RocketEntity;
import com.machina.api.rocket.RocketProps;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.api.rocket.part.impl.ChassisPart;
import com.machina.api.rocket.part.impl.FuelTankPart;
import com.machina.api.rocket.part.impl.LifeSupportPart;
import com.machina.api.rocket.part.impl.ShieldPart;
import com.machina.api.rocket.part.impl.ThrusterPart;
import com.machina.api.util.StringUtils;
import com.machina.registration.init.RocketPartInit;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class RocketItem extends Item {

    private static final String PROPERTY_KEY = "rocket_prop";

    public RocketItem(Properties props) {
        super(props.stacksTo(1));
    }

    public static RocketPart<?> getPart(ItemStack stack, RocketPartType type) {
        CompoundTag nbt = stack.getOrCreateTag();
        if (nbt.contains(type.getNBTName()))
            return RocketPart.fromNBT(nbt.getCompound(type.getNBTName()));
        return null;
    }

    public static void setPart(ItemStack stack, RocketPartType type, RocketPart<?> part) {
        stack.getOrCreateTag().put(type.getNBTName(), part.toNBT());
    }

    public static void initProperties(ItemStack stack) {
        ThrusterPart<?> thruster = (ThrusterPart<?>) Objects.requireNonNull(getPart(stack, RocketPartType.THRUSTER));
        FuelTankPart<?> fuel_tank = (FuelTankPart<?>) Objects.requireNonNull(getPart(stack, RocketPartType.FUEL_TANK));
        ChassisPart<?> chassis = (ChassisPart<?>) Objects.requireNonNull(getPart(stack, RocketPartType.CHASSIS));
        LifeSupportPart<?> life_support = (LifeSupportPart<?>) Objects
                .requireNonNull(getPart(stack, RocketPartType.LIFE_SUPPORT));
        ShieldPart<?> shield = (ShieldPart<?>) Objects.requireNonNull(getPart(stack, RocketPartType.SHIELD));

        final RocketProps props = RocketProps.fromParts(thruster, fuel_tank, chassis, life_support, shield);
        stack.getOrCreateTag().put(PROPERTY_KEY, props.toNBT());
    }
    
    public static void setProperties(ItemStack stack, RocketProps props) {
        stack.getOrCreateTag().put(PROPERTY_KEY, props.toNBT());
    }

    private static RocketProps getProperties(ItemStack stack) {
        if (!stack.getOrCreateTag().contains(PROPERTY_KEY)) {
            return null;
        }
        return RocketProps.fromNBT(stack.getOrCreateTag().getCompound(PROPERTY_KEY));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> tooltip,
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
        super.appendHoverText(stack, level, tooltip, flag);
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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        } else if (!(level instanceof ServerLevel)) {
            return InteractionResultHolder.success(itemstack);
        } else {
            BlockPos blockpos = blockhitresult.getBlockPos();
            if (level.mayInteract(player, blockpos)
                    && player.mayUseItemAt(blockpos, blockhitresult.getDirection(), itemstack)) {
                RocketEntity rocket = new RocketEntity(level, getProperties(itemstack));
                rocket.setPos(blockpos.getCenter());
                level.addFreshEntity(rocket);

                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                player.awardStat(Stats.ITEM_USED.get(this));
                level.gameEvent(player, GameEvent.ENTITY_PLACE, rocket.position());
                return InteractionResultHolder.consume(itemstack);
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        }
    }
}
