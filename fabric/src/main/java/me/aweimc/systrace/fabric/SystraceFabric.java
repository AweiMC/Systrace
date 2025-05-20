package me.aweimc.systrace.fabric;

import me.aweimc.systrace.Systrace;
import me.aweimc.systrace.util.EnvironmentType;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.api.ModInitializer;

public final class SystraceFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Systrace.init();
    }
}
