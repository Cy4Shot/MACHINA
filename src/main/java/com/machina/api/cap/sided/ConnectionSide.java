package com.machina.api.cap.sided;

import java.util.Locale;

import org.jetbrains.annotations.NotNull;

import com.machina.Machina;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringRepresentable;

public enum ConnectionSide implements StringRepresentable {
	NORMAL,
	INPUT,
	OUTPUT,
	NONE;

	public boolean isConnected() {
		return this != NONE;
	}

	public boolean isInput() {
		return this == INPUT;
	}

	public boolean isOutput() {
		return this == OUTPUT;
	}

	public boolean isNone() {
		return this == NONE;
	}

	public boolean isIO() {
		return isInput() || isOutput();
	}

	public void save(CompoundTag tag, String key) {
		tag.putString(key, this.getSerializedName());
	}

	public static ConnectionSide load(CompoundTag tag, String key) {
		return valueOf(tag.getString(key).toUpperCase());
	}

	@Override
	public @NotNull String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public MutableComponent comp() {
		return Component.translatable(Machina.MOD_ID + ".connection_side." + name().toLowerCase());
	}

	public ConnectionSide toggleIO() {
		if (isInput())
			return OUTPUT;
		if (isOutput())
			return INPUT;
		return this;
	}
}
