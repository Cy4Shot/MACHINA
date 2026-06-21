package com.machina.api.client.screen;

import java.util.function.Supplier;

import com.machina.api.util.StringUtils;
import com.machina.api.util.reflect.BindableFunction;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public class ProgressBar<N extends Number> {

	private final Supplier<N> value;
	private final Supplier<N> max;
	private final Supplier<N> min;

	private final Supplier<Float> fraction;

	private final Supplier<String> formattedValue;
	private final Supplier<String> formattedMax;
	private final Supplier<String> formattedMin;

	private final Supplier<MutableComponent> name;

	public ProgressBar(Supplier<N> value, Supplier<N> max, Supplier<N> min, BindableFunction<N, String> formatter,
			Supplier<MutableComponent> name) {
		this.value = value;
		this.max = max;
		this.min = min;

		this.fraction = () -> {
			float v = value.get().floatValue();
			float m = max.get().floatValue();
			float mi = min.get().floatValue();
			if (m - mi == 0) {
				return 0f;
			}
			return (v - mi) / (m - mi);
		};

		this.formattedValue = formatter.bind(value);
		this.formattedMax = formatter.bind(max);
		this.formattedMin = formatter.bind(min);

		this.name = name;
	}

	@SuppressWarnings("unchecked")
	public ProgressBar(Supplier<N> value, Supplier<N> max, BindableFunction<N, String> formatter) {
		this(value, max, () -> (N) Float.valueOf(0f), formatter, Component::empty);
	}

	@SuppressWarnings("unchecked")
	public ProgressBar(Supplier<N> value, Supplier<N> max, BindableFunction<N, String> formatter,
			Supplier<MutableComponent> name) {
		this(value, max, () -> (N) Float.valueOf(0f), formatter, name);
	}

	public N getValue() {
		return value.get();
	}

	public N getMax() {
		return max.get();
	}

	public N getMin() {
		return min.get();
	}

	public float getFraction() {
		return fraction.get();
	}

	public String getFormattedValue() {
		return formattedValue.get();
	}

	public String getFormattedMax() {
		return formattedMax.get();
	}

	public String getFormattedMin() {
		return formattedMin.get();
	}

	public String getFormattedFraction() {
		return StringUtils.formatPercent(getFraction());
	}

	public MutableComponent getName() {
		return name.get();
	}

	public MutableComponent getFormatted() {
		return getName().append(
				Component.literal(getFormattedValue() + " / " + getFormattedMax() + " (" + getFormattedFraction() + ")")
						.withStyle(Style.EMPTY.withBold(false).withColor(MUI.WHITE)));
	}
}
