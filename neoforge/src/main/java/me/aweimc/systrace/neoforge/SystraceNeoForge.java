package me.aweimc.systrace.neoforge;

import me.aweimc.systrace.util.Command;
import me.aweimc.systrace.util.EnvironmentType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;

import me.aweimc.systrace.Systrace;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import org.jetbrains.annotations.NotNull;

@Mod(Systrace.MOD_ID)
@EventBusSubscriber()
public final class SystraceNeoForge {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onClientCommand(@NotNull RegisterClientCommandsEvent event) {
        Command.register(event.getDispatcher(),"stec",0);
    }
    public SystraceNeoForge() {
        EnvironmentType.setClientState(FMLEnvironment.dist.isClient());
        Systrace.init();
    }
}
