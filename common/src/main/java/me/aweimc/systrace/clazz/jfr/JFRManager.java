package me.aweimc.systrace.clazz.jfr;

import jdk.jfr.Recording;
import me.aweimc.systrace.Systrace;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class JFRManager {
    public static final String FILE_PREFIX = "Systrace_recording_";
    public static final String FILE_NAME = "Systrace-Profiling";
    public static String JFR_NAME;
    private static boolean recordingActive = false;
    private static Recording recording;
    private static long startTimeNs = 0;

    public static final Path OUTPUT_DIR = Path.of("systrace_jfr");

    public static boolean checkAndWarn() {
        boolean supported = JFRSupportChecker.isJFRAvailable();
        if (!supported) {
            Systrace.LOGGER.info("[JFR SupportChecker] Flight Recorder is not supported in this environment. Skipping recording startup.");
        }
        return supported;
    }

    private static String generateFileName() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return FILE_PREFIX + timestamp + ".jfr";
    }

    public static void start() {
        if (recordingActive) return;
        if (!checkAndWarn()) return;
        if(!isActive()) recording = new Recording();

        try {
            if (!Files.exists(OUTPUT_DIR)) {
                Files.createDirectories(OUTPUT_DIR);
            }

            String fileName = generateFileName();
            Path outputPath = OUTPUT_DIR.resolve(fileName);
            JFR_NAME = outputPath.toString();

            recording.setName(FILE_NAME);
            recording.setToDisk(true);
            recording.setDestination(outputPath);

            enableEvents(recording);

            recording.start();
            recordingActive = true;
            startTimeNs = System.nanoTime();

            Systrace.LOGGER.info("[JFR] Recording started, saving to: {}", outputPath.toAbsolutePath());


        } catch (IOException e) {
            throw new RuntimeException("Failed to start JFR recording", e);
        }
    }

    public static void stop() {
        if (!recordingActive) return;

        try {
            recording.stop();
            recording.close();

            long durationNs = System.nanoTime() - startTimeNs;
            double seconds = durationNs / 1_000_000_000.0;
            Systrace.LOGGER.info(String.format("[JFR] Recording stopped after %.2f seconds", seconds));

        } catch (Exception e) {
            Systrace.LOGGER.error("[JFR] Error while stopping recording", e);
        } finally {
            recordingActive = false;
        }
    }

    public static boolean isActive() {
        return recordingActive;
    }

    private static void enableEvents(Recording recording) {
        // 方法采样分析（默认 10ms，自定义为 20ms）
        recording.enable("jdk.ExecutionSample").withThreshold(Duration.ofMillis(20));

        // 内存分配分析
        recording.enable("jdk.ObjectAllocationInNewTLAB").withStackTrace();
        recording.enable("jdk.ObjectAllocationOutsideTLAB").withStackTrace();

        // GC 与堆信息
        recording.enable("jdk.GarbageCollection");
        recording.enable("jdk.GCHeapSummary");
        recording.enable("jdk.GCPhasePause");
        recording.enable("jdk.GCReferenceStatistics");

        // 线程状态与锁分析
        recording.enable("jdk.ThreadSleep");
        recording.enable("jdk.ThreadPark");
        recording.enable("jdk.JavaMonitorEnter");
        recording.enable("jdk.JavaMonitorWait");
        recording.enable("jdk.NativeMethodSample");

        // 类加载相关
        recording.enable("jdk.ClassLoadingStatistics");
        recording.enable("jdk.ClassUnload");

        // 可选扩展事件
        recording.enable("jdk.ThreadStart");
        recording.enable("jdk.ThreadEnd");
        recording.enable("jdk.SocketRead");
        recording.enable("jdk.SocketWrite");
        recording.enable("jdk.FileRead");
        recording.enable("jdk.FileWrite");
    }
}
