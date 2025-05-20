package me.aweimc.systrace.forge;

import me.aweimc.systrace.Systrace;
import me.aweimc.systrace.util.Command;
import me.aweimc.systrace.util.EnvironmentType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import net.minecraftforge.fml.common.Mod;

@Mod(Systrace.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class SystraceForge {
    public SystraceForge() {
        // Run our common setup.

        Systrace.init();
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onClientCommand(@NotNull RegisterClientCommandsEvent event) {
        EnvironmentType.setClientState(FMLEnvironment.dist.isClient());
        Command.register(event.getDispatcher(),"stec",0);
    }
}
