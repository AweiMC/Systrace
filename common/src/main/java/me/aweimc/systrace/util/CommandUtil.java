package me.aweimc.systrace.util;

import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;

public class CommandUtil {
    public static <S> CLiteralArgumentBuilder<S> literal(String name) {
        return CLiteralArgumentBuilder.of(name);
    }
    public static  CLiteralArgumentBuilder<ClientCommandSource> cLiteral(String name) {
        return CLiteralArgumentBuilder.of(name);
    }
    public static  CLiteralArgumentBuilder<ServerCommandSource> sLiteral(String name) {
        return CLiteralArgumentBuilder.of(name);
    }
    public static CLiteralArgumentBuilder<CommandSource> nLiteral(String name) {
        return CLiteralArgumentBuilder.of(name);
    }
}
