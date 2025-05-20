package me.aweimc.systrace.clazz;

import me.aweimc.systrace.Systrace;
import org.spongepowered.asm.mixin.transformer.Config;

import java.io.IOException;
import java.lang.management.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class ClassManager {
    private static final String SECTION_SEPARATOR = "=====================================";
    private static final String SUB_SECTION_SEPARATOR = "-------------------------------------";
    private static final String PLUGIN_INFO_SEPARATOR = ">>>>>-----[Plugin Info]-----<<<<<";
    private static final double CTR_VERSION = 1.0;

    public static String LastOutPath;
    public static void saveToFile() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String baseDir = "logs/class_tracker_" + timestamp; // 创建带时间戳的日志文件夹
        LastOutPath = baseDir;
        try {
            Files.createDirectories(Paths.get(baseDir)); // 创建目录
        } catch (IOException e) {
            Systrace.LOGGER.error("Failed to create log directory: {}", baseDir, e);
            return;
        }

        String next = System.lineSeparator();

        // 1. 系统信息
        StringBuilder systemInfo = new StringBuilder();
        systemInfo.append(colorize("[System Info]", "35")).append(next);
        systemInfo.append(String.format("SystraceX Version: %s", CTR_VERSION)).append(next);
        systemInfo.append(String.format("Java Version: %s", Runtime.version())).append(next);
        saveLogToFile(baseDir, "system_info.log", systemInfo.toString());

        // 2. 类加载信息
        StringBuilder classLoadingInfo = new StringBuilder();
        ClassLoadingMXBean clb = ManagementFactory.getClassLoadingMXBean();
        classLoadingInfo.append(colorize("[Class Loading Stats]", "35")).append(next);
        classLoadingInfo.append(String.format("Total Loaded Classes: %d", clb.getTotalLoadedClassCount())).append(next);
        classLoadingInfo.append(String.format("Currently Loaded Classes: %d", clb.getLoadedClassCount())).append(next);
        classLoadingInfo.append(String.format("Unloaded Classes: %d", clb.getUnloadedClassCount())).append(next);
        saveLogToFile(baseDir, "class_loading.log", classLoadingInfo.toString());

        // 3. 线程信息
        StringBuilder threadInfo = new StringBuilder();
        getThreadString(threadInfo, next);
        saveLogToFile(baseDir, "threads.log", threadInfo.toString());

        // 4. 硬件信息
        StringBuilder hardwareInfo = new StringBuilder();
        getHardwareString(hardwareInfo, next);
        saveLogToFile(baseDir, "hardware_info.log", hardwareInfo.toString());

        // 5. Mixin 信息
        StringBuilder mixinInfo = new StringBuilder();
        getMixin(mixinInfo, next);
        saveLogToFile(baseDir, "mixin_info.log", mixinInfo.toString());

        // 6. JVM 内存信息
        StringBuilder memoryInfo = new StringBuilder();
        getObject(memoryInfo, next);
        saveLogToFile(baseDir, "memory_info.log", memoryInfo.toString());


//        // 提示用户文件已保存
//        ToastUtil.send(
//                Text.translatable("ruok.setting.debug.tracker.save.file.title"),
//                Text.translatable("ruok.setting.debug.tracker.save.file.info", baseDir)
//        );
    }

    /**
     * 将日志内容写入文件
     */
    private static void saveLogToFile(String dir, String filename, String content) {
        try {
            Files.write(Paths.get(dir, filename), content.getBytes());
        } catch (IOException e) {
            Systrace.LOGGER.error("Failed to write log: {}", filename, e);
        }
    }


    private static void getObject(StringBuilder logContent, String next) {
        logContent.append(colorize("[MemoryMXBean Information]", "35")).append(next);

        // 获取 JVM 内存管理信息
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        List<MemoryPoolMXBean> memoryPools = ManagementFactory.getMemoryPoolMXBeans();
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();

        // 堆内存使用情况
        MemoryUsage heapMemory = memoryMXBean.getHeapMemoryUsage();
        logContent.append("Heap Memory Used: ")
                .append(heapMemory.getUsed() / (1024 * 1024))
                .append(" MB / ")
                .append(heapMemory.getMax() / (1024 * 1024))
                .append(" MB").append(next);

        // 遍历 JVM 的不同内存池
        for (MemoryPoolMXBean pool : memoryPools) {
            MemoryUsage usage = pool.getUsage();
            logContent.append("Memory Pool [").append(pool.getName()).append("]: ")
                    .append(usage.getUsed() / (1024 * 1024)).append(" MB / ")
                    .append(usage.getMax() / (1024 * 1024)).append(" MB").append(next);
        }

        // 统计 GC 次数
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            logContent.append("GC [").append(gcBean.getName()).append("] Count: ")
                    .append(gcBean.getCollectionCount()).append(next);
        }

        logContent.append(SECTION_SEPARATOR).append(next);
    }

    private static void getThreadString(StringBuilder logContent, String next) {
        // Thread Info
        logContent.append(colorize("[Thread Information]", "35")).append(next);
        Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
        map.forEach((thread, stackTraceElements) -> {
            logContent.append(String.format("  Thread: %s (ID: %d)", thread.getName(), thread.getId())).append(next);
            logContent.append(String.format("  State: %s", thread.getState())).append(next);
            logContent.append(String.format("  Thread Group: %s", thread.getThreadGroup().getName())).append(next);
            logContent.append("  -------- Stack Trace --------").append(next);

            // Output thread stack trace with indentation for readability
            for (StackTraceElement st : stackTraceElements) {
                logContent.append(String.format("    File: %s", st.getFileName())).append(next);
                logContent.append(String.format("    Class: %s", st.getClassName())).append(next);
                logContent.append(String.format("    Method: %s", st.getMethodName())).append(next);
                logContent.append(String.format("    ClassLoader: %s", st.getClassLoaderName())).append(next);
                logContent.append(String.format("    Module: %s", st.getModuleName())).append(next);
                logContent.append(String.format("    Module Version: %s", st.getModuleVersion())).append(next);
                logContent.append("    ------------------------------").append(next);
            }
            logContent.append(SECTION_SEPARATOR).append(next);
        });
    }

    private static void getHardwareString(StringBuilder logContent, String next) {
        // Hardware Information
        logContent.append(colorize("[Hardware Information]", "35")).append(next);
        logContent.append("  ").append(HardwareInfoUtil.getCpuInfo()).append(next);
        logContent.append("  ").append(HardwareInfoUtil.getMemoryInfo()).append(next);
        logContent.append("  ").append(HardwareInfoUtil.getDiskInfo()).append(next);
        logContent.append("  ").append(HardwareInfoUtil.getOsInfo()).append(next);
        logContent.append(SECTION_SEPARATOR).append(next);
    }

    public static void getMixin(StringBuilder logContent, String next) {
        logContent.append(colorize("[MixinClass Details]", "35")).append(next);

        // Map：Target -> Mixin 列表
        Map<String, List<String>> targetToMixins = new HashMap<>();

        getMixinConfigs().forEach((string, config) -> {
            logContent.append(String.format("  [Configs]: %s", config.getConfig().getName())).append(next);
            logContent.append(String.format("  [Priority]: %d", config.getConfig().getPriority())).append(next);
            logContent.append(String.format("  [Package]: %s", config.getConfig().getMixinPackage())).append(next);
            logContent.append(String.format("  [Environment Version]: %s", config.getConfig().getEnvironment().getVersion())).append(next);
            logContent.append("  [Targets]").append(next);

            for (String target : config.getConfig().getTargets()) {
                logContent.append(String.format("      - %s", target)).append(next);
                targetToMixins.computeIfAbsent(target, k -> new ArrayList<>()).add(config.getConfig().getName());
            }

            if (config.getConfig().getPlugin() != null) {
                logContent.append(PLUGIN_INFO_SEPARATOR).append(next);
                logContent.append(String.format("    [RefMapperConfig]: %s", config.getConfig().getPlugin().getRefMapperConfig())).append(next);
                if (config.getConfig().getPlugin().getMixins() != null) {
                    logContent.append("    [Mixins]").append(next);
                    config.getConfig().getPlugin().getMixins().forEach(e -> logContent.append(String.format("        - %s", e)).append(next));
                }
                logContent.append(PLUGIN_INFO_SEPARATOR).append(next);
            }

            logContent.append(SUB_SECTION_SEPARATOR).append(next);
        });

        logContent.append(SECTION_SEPARATOR).append(next);

        // **附加部分**：按 Target 归类 Mixin，并排序（从多到少）
        logContent.append(colorize("[Mixin Target Summary]", "36")).append(next);

        targetToMixins.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue().size(), a.getValue().size())) // 按 Mixins 数量降序
                .forEach(entry -> {
                    String target = entry.getKey();
                    List<String> mixins = entry.getValue();
                    logContent.append(String.format(" - Target (Mixins: %d): %s", mixins.size(), target)).append(next);
                    for (String mixin : mixins) {
                        logContent.append(String.format("   - (%s)", mixin)).append(next);
                    }
                });

        logContent.append(SECTION_SEPARATOR).append(next);
    }


    private static Map<String, Config> getMixinConfigs() {
        // 获取 Config 类的 Class 对象
        Class<?> configClass = Config.class;
        // 获取 allConfigs 字段的 Field 对象
        Field allConfigsField;
        try {
            allConfigsField = configClass.getDeclaredField("allConfigs");
        } catch (NoSuchFieldException e) {
            Systrace.LOGGER.error("Field 'allConfigs' not found in class Config.", e);
            throw new RuntimeException("Field 'allConfigs' not found in class Config.", e);
        }

        // 设置该字段可访问，绕过 Java 的访问控制检查
        allConfigsField.setAccessible(true);

        // 获取 allConfigs 字段的值
        try {
            @SuppressWarnings("unchecked")
            Map<String, Config> allConfigs = (Map<String, Config>) allConfigsField.get(null);
            return allConfigs;
        } catch (IllegalAccessException e) {
            Systrace.LOGGER.error("Illegal access to field 'allConfigs'.", e);
            throw new RuntimeException("Illegal access to field 'allConfigs'.", e);
        }
    }

    private static String colorize(String text, String colorCode) {
        // 如果在支持 ANSI 转义码的环境中使用颜色，否则返回原文本
        if (System.console() != null) {
            return "\u001B[" + colorCode + "m" + text + "\u001B[0m";
        }
        return text;
    }
}