package com.machina.api.cap.sided;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.network.c2s.C2SSideConfig;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.PacketDistributor;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;

public enum Side implements StringRepresentable {
    NONE(407),
    OUTPUT(423),
    INPUT(415),
    BOTH(431);

    public static final Side[] NONES = new Side[]{Side.NONE, Side.NONE, Side.NONE, Side.NONE, Side.NONE, Side.NONE};
    public static final Side[] INPUTS = new Side[]{Side.INPUT, Side.INPUT, Side.INPUT, Side.INPUT, Side.INPUT,
            Side.INPUT};
    public static final Side[] OUTPUTS = new Side[]{Side.OUTPUT, Side.OUTPUT, Side.OUTPUT, Side.OUTPUT, Side.OUTPUT,
            Side.OUTPUT};
    public static final Side[] BOTHS = new Side[]{Side.BOTH, Side.BOTH, Side.BOTH, Side.BOTH, Side.BOTH, Side.BOTH};

    private final int tx;
    private final int ty;

    Side(int tx) {
        this.tx = tx;
        this.ty = 81;
    }

    public boolean isConnected() {
        return this != NONE;
    }

    public boolean isInput() {
        return this == INPUT || this == BOTH;
    }

    public boolean isOutput() {
        return this == OUTPUT || this == BOTH;
    }

    public int x() {
        return this.tx;
    }

    public int y() {
        return this.ty;
    }

    public @NotNull String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public void save(CompoundTag tag, String key) {
        tag.putString(key, this.getSerializedName());
    }

    public static Side load(CompoundTag tag, String key) {
        return valueOf(tag.getString(key).toUpperCase());
    }

    public static CompoundTag serialize(Side[] sides) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (Side side : sides) {
            list.add(StringTag.valueOf(side.getSerializedName()));
        }
        tag.put("sides", list);
        return tag;
    }

    public static Side[] deserialize(CompoundTag tag) {
        ListTag list = tag.getList("sides", 8);
        Side[] sides = new Side[list.size()];
        for (int i = 0; i < list.size(); i++) {
            sides[i] = Side.valueOf(list.getString(i).toUpperCase());
        }
        return sides;
    }

    public static void cycle(Side[] input, Direction d, MachinaBlockEntity e, String tag) {
        input[d.ordinal()] = values()[(input[d.ordinal()].ordinal() + 1) % values().length];

        if (Objects.requireNonNull(e.getLevel()).isClientSide()) {
            PacketDistributor.sendToServer(new C2SSideConfig(tag, e.getBlockPos(), getRaw(input)));
            if (e.getLevel().getModelDataManager() != null) {
                e.getLevel().getModelDataManager().requestRefresh(e);
            }
        } else {
            e.setChanged();
        }
    }

    public static byte[] getRaw(Side[] input) {
        byte[] raw = new byte[6];
        for (int i = 0; i < 6; i++) {
            raw[i] = (byte) input[i].ordinal();
        }
        return raw;
    }

    public static void fromRaw(Side[] input, byte[] raw) {
        if (raw.length == 6) {
            for (int i = 0; i < 6; i++) {
                input[i] = Side.values()[raw[i]];
            }
        }
    }
}