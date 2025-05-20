package me.aweimc.systrace.util;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import me.aweimc.systrace.clazz.ClassManager;
import me.aweimc.systrace.clazz.jfr.JFRManager;

public class Command {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, String sCommand,int level) {
        dispatcher.register((CommandUtil.sLiteral(sCommand).requires(x -> x.hasPermissionLevel(level))
                .executes((context) -> {
                    ClassManager.saveToFile();
                    TextInfo.sendOutFileMessage(context,((source, message) -> context.getSource().sendMessage(message)),ClassManager.LastOutPath);
                    return 1;
                }))
                .then(CommandManager.literal("jfr")
                .executes(context -> {
                    TextInfo.sendJFREmptyMessage(context,(source, message) -> context.getSource().sendMessage(message));
                    return 0;
                })
                        .then(CommandManager.literal("start").executes((context) -> {
                            JFRManager.start();
                            TextInfo.sendJFRStartMessage(context,((source, message) -> context.getSource().sendMessage(message)));
                            return 0;
                        }))
                        .then(CommandManager.literal("stop").executes((context) -> {
                            JFRManager.stop();
                            TextInfo.sendJFRStopMessage(context,(source, message) -> context.getSource().sendMessage(message),JFRManager.JFR_NAME);
                            return 0;
                        }))

        ));

    }



}
