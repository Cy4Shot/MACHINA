package com.machina.item.filter;

import com.machina.Machina;
import com.machina.api.cap.item.ConduitItemStorage;
import com.machina.api.item.ConnectorFilterItem;
import com.machina.item.menu.AdvancedItemFilterMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class AdvancedItemFilterItem extends ConnectorFilterItem<ItemStack, ConduitItemStorage> {

    private static final String ITEMS = "items";

    public AdvancedItemFilterItem(Properties props) {
        super(props);
    }

    public static NonNullList<Item> getItems(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        NonNullList<Item> builder = NonNullList.withSize(27, Items.AIR);
        if (nbt.contains(ITEMS)) {
            ListTag items = nbt.getList(ITEMS, Tag.TAG_STRING);
            for (int i = 0; i < 27; i++) {
                ResourceLocation itemName = new ResourceLocation(items.getString(i));
                Item item = ForgeRegistries.ITEMS.getValue(itemName);
                if (item != null)
                    builder.set(i, item);
            }
        }
        return builder;
    }

    public static ItemStack set(ItemStack stack, NonNullList<Item> types, Mode mode) {
        CompoundTag tag = stack.getOrCreateTag();
        if (types != null) {
            ListTag items = new ListTag();
            types.forEach(x -> {
                ResourceLocation key = ForgeRegistries.ITEMS.getKey(x);
                if (key != null) {
                    items.add(StringTag.valueOf(key.toString()));
                }
            });
            tag.put(ITEMS, items);
        }
        if (mode != null)
            tag.putString(MODE, mode.name());
        stack.setTag(tag);
        return stack;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> tooltip,
                                @NotNull TooltipFlag flag) {
        if (getItems(stack).isEmpty()) {
            tooltip.add(Component.translatable(Machina.MOD_ID + ".tooltip.item_filter.empty")
                    .setStyle(Style.EMPTY.withColor(65278)));
        } else {
            tooltip.add(Component.translatable(Machina.MOD_ID + ".tooltip.item_filter.configured")
                    .setStyle(Style.EMPTY.withColor(65278)));
        }

        super.appendHoverText(stack, level, tooltip, flag);
    }

    @Override
    public boolean filter(ItemStack stack, ItemStack original) {
        Mode mode = getMode(stack);
        List<Item> items = getItems(stack);

        if (original.getItem() == Items.AIR)
            return false;

        if (Objects.requireNonNull(mode) == Mode.BLACKLIST) {
            return !items.contains(original.getItem());
        }
        return items.contains(original.getItem());
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (level.isClientSide())
            return super.use(level, player, hand);

        NetworkHooks.openScreen((ServerPlayer) player, new MenuProvider() {
            @Override
            public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
                return new AdvancedItemFilterMenu(id, inv, hand);
            }

            @Override
            public @NotNull Component getDisplayName() {
                return Component.empty();
            }
        }, buf -> buf.writeBoolean(hand == InteractionHand.MAIN_HAND));
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }
}