package me.aweimc.systrace.util;

public class EnvironmentType {
    public static boolean isServer() {
        return EnvType.equals(Type.SERVER);
    }
    public static boolean isClient() {
        return EnvType.equals(Type.CLIENT);
    }

    public enum Type{
        SERVER,CLIENT
    }
    private static Type EnvType = Type.CLIENT;

    public static Type getEnvType() {
        if(EnvType==null) return Type.SERVER;
        return EnvType;
    }

    public static void setEnvType(Type envType) {
        EnvType = envType;
    }
    public static void setClientState(boolean client) {
        if(client) EnvType=Type.CLIENT;
        else EnvType=Type.SERVER;
    }
}
