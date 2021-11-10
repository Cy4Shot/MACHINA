package com.cy4.machina.api.util.helper;

import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class TextComponentHelper {
	
	private TextComponentHelper() {
    }

    /**
     * Helper to apply an integer color style to a given text component.
     *
     * @param component Component to color.
     * @param color     RGB color to apply.
     *
     * @return Colored component.
     */
    public static IFormattableTextComponent color(IFormattableTextComponent component, int color) {
        return component.setStyle(component.getStyle()
              .withColor(Color.fromRgb(color)));
    }

    /**
     * Builds a formattable text component out of a list of components using a "smart" combination system to allow for automatic replacements, and coloring to take
     * place.
     *
     * @param components Argument components.
     *
     * @return Formattable Text Component.
     */
    public static IFormattableTextComponent build(Object... components) {
        IFormattableTextComponent result = null;
        Style cachedStyle = Style.EMPTY;
        for (Object component : components) {
            if (component == null) {
                continue;
            }
            IFormattableTextComponent current = null;
            if (component instanceof IHasTextComponent) {
                current = ((IHasTextComponent) component).getComponent().copy();
            } else {
                current = getString(component.toString());
            }
            if (current == null) {
                continue;
            }
            if (!cachedStyle.isEmpty()) {
                current.setStyle(cachedStyle);
                cachedStyle = Style.EMPTY;
            }
            if (result == null) {
                result = current;
            } else {
                result.append(current);
            }
        }
        return result;
    }

    /**
     * Helper to call the constructor for string text components and also convert any non-breaking spaces to spaces so that they render properly.
     *
     * @param component String
     *
     * @return String Text Component.
     */
    public static StringTextComponent getString(String component) {
        return new StringTextComponent(cleanString(component));
    }

    private static String cleanString(String component) {
        return component.replace("\u00A0", " ");
    }

    /**
     * Helper to call the constructor for translation text components in case we end up ever needing to do any extra processing.
     *
     * @param key  Translation Key.
     * @param args Arguments.
     *
     * @return Translation Text Component.
     */
    public static TranslationTextComponent translate(String key, Object... args) {
        return new TranslationTextComponent(key, args);
    }

    public static boolean hasStyleType(Style current, TextFormatting formatting) {
        switch (formatting) {
            case OBFUSCATED:
                return current.isObfuscated();
            case BOLD:
                return current.isBold();
            case STRIKETHROUGH:
                return current.isStrikethrough();
            case UNDERLINE:
                return current.isUnderlined();
            case ITALIC:
                return current.isItalic();
            case RESET:
                return current.isEmpty();
            default:
                return current.getColor() != null;
        }
    }

}
