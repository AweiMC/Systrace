package me.aweimc.systrace.util;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import me.aweimc.systrace.clazz.jfr.JFRManager;

public class TextInfo {

    public static <S> void sendOutFileMessage(CommandContext<S> context, MessageSender<S> sender, String path) {
        S source = context.getSource();
        sender.send(source, separator("systrace.text.separation", "========== SysTrack =========", Formatting.GREEN));
        sender.send(source, styled("systrace.text.out.done", "✔ The information has been exported successfully!", Formatting.GREEN, true));
        sender.send(source, clickablePath("systrace.text.out.path", "📁 File path: %s", path));
        sender.send(source, separator("systrace.text.separation", "========== SysTrack =========", Formatting.GREEN));
    }

    public static <S> void sendJFRStartMessage(CommandContext<S> context, MessageSender<S> sender) {
        S source = context.getSource();
        sender.send(source, separator("systrace.text.jfr.separation", "========== SysTrack-JFR =========", Formatting.GOLD));
        sender.send(source, styled("systrace.text.jfr.start", "▶ JFR has started, please wait...", Formatting.YELLOW, false));
        sender.send(source, separator("systrace.text.jfr.separation", "========== SysTrack-JFR =========", Formatting.GOLD));

    }


    public static <S> void sendJFRStopMessage(CommandContext<S> context, MessageSender<S> sender, String fileName) {
        S source = context.getSource();
        sender.send(source, separator("systrace.text.jfr.separation", "========== SysTrack-JFR =========", Formatting.GOLD));
        sender.send(source, clickablePath("systrace.text.jfr.stop", "■ JFR has been closed! File name: %s",JFRManager.OUTPUT_DIR.toString(), fileName));
        sender.send(source, styled("systrace.text.jfr.tip", "💡 The JFR file needs to be opened using the JDK Mission Control program tool", Formatting.GRAY, true));
        sender.send(source, separator("systrace.text.jfr.separation", "========== SysTrack-JFR =========", Formatting.GOLD));

    }


    public static <S> void sendJFREmptyMessage(CommandContext<S> context, MessageSender<S> sender) {
        S source = context.getSource();
        sender.send(source, separator("systrace.text.jfr.separation", "========== SysTrack-JFR =========", Formatting.GOLD));
        sender.send(source, getJFRStateText());
        sender.send(source, separator("systrace.text.jfr.separation", "========== SysTrack-JFR =========", Formatting.GOLD));
    }

    public static Text getJFRStateText() {
        return JFRManager.isActive()
                ? styled("systrace.text.jfr.state.running", "Running", Formatting.GREEN, true)
                : styled("systrace.text.jfr.state.stopped", "Stopped", Formatting.RED, true);
    }

    // ========== 样式封装 ==========

    public static Text separator(String key, String fallback, Formatting color) {
        return TextUtil.adaptiveText(key, fallback).copy().formatted(color);
    }

    public static Text styled(String key, String fallback, Formatting color, boolean italic, Object... args) {
        return TextUtil.adaptiveText(key, fallback, args).copy()
                .setStyle(Style.EMPTY.withColor(color).withItalic(italic));
    }

    public static Text clickablePath(String key, String fallback, String path) {
        return TextUtil.adaptiveText(key, fallback, path).copy().setStyle(
                Style.EMPTY
                        .withColor(Formatting.AQUA)
                        .withUnderline(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, path))
        );
    }
    public static Text clickablePath(String key, String fallback, String path, Object... objects) {
        return TextUtil.adaptiveText(key, fallback, objects).copy().setStyle(
                Style.EMPTY
                        .withColor(Formatting.AQUA)
                        .withUnderline(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, path))
        );
    }
}
