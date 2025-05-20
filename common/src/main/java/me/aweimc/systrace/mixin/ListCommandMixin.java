package me.aweimc.systrace.mixin;

import com.mojang.brigadier.CommandDispatcher;
import me.aweimc.systrace.util.Command;
import net.minecraft.server.command.ListCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ListCommand.class)
public class ListCommandMixin {
    @Inject(method = "register",at = @At("HEAD"))
    private static void STE_Command(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo ci) {
        Command.register(dispatcher,"ste",3);
    }
}
