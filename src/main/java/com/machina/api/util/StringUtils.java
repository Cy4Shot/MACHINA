package com.machina.api.util;

import com.machina.Machina;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringUtils {

    private static final String[] SMALL_UNITS = { "", "K", "M", "B", "T", "P", "E", "Z", "Y" };
    private static final String[] LARGE_UNITS = { "", "m", "μ", "n", "p", "f", "a", "z", "y" };
    private static final String[] MASS_LARGE_UNITS = { "kg", "t", "kt", "Mt", "Gt", "Tt", "Pt", "Et", "Zt", "Yt" };
    private static final String[] MASS_SMALL_UNITS = { "kg", "g", "mg", "µg", "ng", "pg", "fg", "ag", "zg", "yg" };

    public static final String TREE_V = "│";
    public static final String TREE_H = "─";
    public static final String TREE_F = "├";
    public static final String TREE_L = "└";

    private static final Charset utf8Charset = StandardCharsets.UTF_8;
    private static final Charset defaultCharset = Charset.defaultCharset();

    public static void printlnUtf8(String msg) {
        new PrintStream(System.out, true, utf8Charset)
                .println(new String(msg.getBytes(StandardCharsets.UTF_8), defaultCharset));
    }

    public static String chemical(String data) {
        StringBuilder out = new StringBuilder();
        for (char c : data.toCharArray()) {
            out.append(Character.isDigit(c) ? getSubscript(Integer.toString(c - '0')) : c);
        }
        return out.toString();
    }

    public static String getSubscript(String pString) {
        final int subscriptZeroCodepoint = 0x2080;
        StringBuilder builder = new StringBuilder();
        for (char character : pString.toCharArray()) {
            builder.append(Character.toChars(subscriptZeroCodepoint + Character.getNumericValue(character)));
        }
        return builder.toString();
    }

    public static String translate(String key, Object... params) {
        return Component.translatable(key, params).getString();
    }

    public static MutableComponent translateMultiblockComp(String key) {
        return Component.translatable(Machina.MOD_ID + ".multiblock." + key);
    }

    private static String formatNumberWithUnit(double number) {

        if (number >= 1_000) {
            int magnitude = 0;
            while (number >= 1_000 && magnitude < SMALL_UNITS.length - 1) {
                number /= 1_000;
                magnitude++;
            }
            return String.format("%.1f%s", number, SMALL_UNITS[magnitude]);
        } else if (number > 0) {
            int magnitude = 0;
            while (number < 1 && magnitude < LARGE_UNITS.length - 1) {
                number *= 1_000;
                magnitude++;
            }
            return String.format("%.1f%s", number, LARGE_UNITS[magnitude]);
        }

        return String.format("%.1f", number);
    }

    public static String formatTemp(double temp) {
        return formatNumberWithUnit(temp) + "K";
    }

    public static String formatPower(int energy) {
        return formatNumberWithUnit(energy) + "RF";
    }

    public static String formatFluid(int mb) {
        return formatNumberWithUnit((double) mb / 1_000D) + "B";
    }

    public static String formatPressure(float pressure) {
        return formatNumberWithUnit(pressure) + "Pa";
    }

    public static String formatRadiation(float rad) {
        return formatNumberWithUnit(rad) + "rad";
    }

    public static String formatMass(double kg) {
        String formatted;
        String unit;
        if (kg >= 1.0) {
            int magnitude = 0;
            double value = kg;
            while (value >= 1000 && magnitude < MASS_LARGE_UNITS.length - 1) {
                value /= 1000.0;
                magnitude++;
            }
            formatted = String.format("%.2f", value);
            unit = MASS_LARGE_UNITS[magnitude];
        } else if (kg > 0) {
            int magnitude = 0;
            double value = kg;
            while (value < 1 && magnitude < MASS_SMALL_UNITS.length - 1) {
                value *= 1000.0;
                magnitude++;
            }
            formatted = String.format("%.2f", value);
            unit = MASS_SMALL_UNITS[magnitude];

        } else {
            formatted = String.format("%.2f", kg);
            unit = "kg";
        }
        formatted = formatted.replaceAll("\\.?0+$", "");
        return formatted + unit;
    }

    public static String formatPercent(float percent) {
        return String.format("%.2f%%", percent * 100);
    }

    public static String formatTicks(float ticks) {
        return formatNumberWithUnit(ticks / 20f) + "s";
    }

    public static MutableComponent fluid(FluidStack stack, boolean bold) {
        return stack.getDisplayName().copy().withStyle(
                Style.EMPTY.withBold(bold).withColor(IClientFluidTypeExtensions.of(stack.getFluid()).getTintColor()));
    }
}
