package me.aweimc.systrace.fabric;

import net.fabricmc.api.ModInitializer;

import me.aweimc.systrace.Systrace;

public final class SystraceFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        Systrace.init();
    }
}
