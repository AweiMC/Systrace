package me.aweimc.systrace.fabric.client;

import me.aweimc.systrace.clazz.ClassManager;
import me.aweimc.systrace.clazz.jfr.JFRManager;
import me.aweimc.systrace.util.CommandUtil;
import me.aweimc.systrace.util.EnvironmentType;
import me.aweimc.systrace.util.TextInfo;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;

public final class SystraceFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        final MinecraftClient mc = MinecraftClient.getInstance();
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        EnvironmentType.setEnvType(EnvironmentType.Type.CLIENT);
        ClientCommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess) ->
                commandDispatcher.register(CommandUtil.<FabricClientCommandSource>literal("stec").executes((context) -> {
                    ClassManager.saveToFile();
                    TextInfo.sendOutFileMessage(context, (source, text) -> mc.player.sendMessage(text), ClassManager.LastOutPath);
                    return 1;
                })
                        .then(CommandUtil.<FabricClientCommandSource>literal("jfr")
                                .executes(context -> {
                                    TextInfo.sendJFREmptyMessage(context,(source, message) -> mc.player.sendMessage(message));
                                    return 0;
                                })
                                .then(CommandUtil.<FabricClientCommandSource>literal("start").executes((context) -> {
                                    JFRManager.start();
                                    TextInfo.sendJFRStartMessage(context,((source, message) -> mc.player.sendMessage(message)));
                                    return 0;
                                }))
                                .then(CommandUtil.<FabricClientCommandSource>literal("stop").executes((context) -> {
                                    JFRManager.stop();
                                    TextInfo.sendJFRStopMessage(context,(source, message) -> mc.player.sendMessage(message),JFRManager.JFR_NAME);
                                    return 0;
                                }))
                )));

    }
}
