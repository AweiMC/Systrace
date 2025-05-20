package me.aweimc.systrace.clazz.jfr;

public class JFRSupportChecker {

    public static boolean isJFRAvailable() {
        return isJDKEnvironment() && isJFRClassPresent();
    }

    private static boolean isJFRClassPresent() {
        try {
            Class.forName("jdk.jfr.Recording");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    private static boolean isJDKEnvironment() {
        String runtime = System.getProperty("java.runtime.name");
        String vmName = System.getProperty("java.vm.name");
        return (runtime != null && runtime.toLowerCase().contains("jdk")) ||
                (vmName != null && vmName.toLowerCase().contains("jdk"));
    }
}
