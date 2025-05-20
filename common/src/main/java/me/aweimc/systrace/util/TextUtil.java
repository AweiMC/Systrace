package me.aweimc.systrace.util;

import net.minecraft.text.Text;

public class TextUtil {
    public static Text adaptiveText(String translationKey, String fallbackLiteral) {
        if (EnvironmentType.isClient()) {
            return Text.translatable(translationKey);
        } else {
            return Text.literal(fallbackLiteral);
        }
    }

    public static Text adaptiveText(String translationKey, String fallbackFormat, Object... args) {
        if (EnvironmentType.isClient()) {
            return Text.translatable(translationKey, args);
        } else {
            return Text.literal(String.format(fallbackFormat, args));
        }
    }
}
