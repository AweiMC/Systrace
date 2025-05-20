package me.aweimc.systrace.clazz;
import com.sun.management.OperatingSystemMXBean;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.software.os.OperatingSystem;

import java.lang.management.ManagementFactory;

public class HardwareInfoUtil {

    private static final String nl = System.lineSeparator();;
    private static final SystemInfo systemInfo = new SystemInfo();
    private static final CentralProcessor processor = systemInfo.getHardware().getProcessor();
    private static final GlobalMemory memory = systemInfo.getHardware().getMemory();
    private static final OperatingSystem os = systemInfo.getOperatingSystem();
    private static final OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    public static String getCpuInfo() {
        StringBuilder cpuInfo = new StringBuilder();
        cpuInfo.append("CPU: ").append(processor.getProcessorIdentifier().getName()).append(nl);
        cpuInfo.append("Cores: ").append(processor.getLogicalProcessorCount()).append(nl);
        cpuInfo.append("System Load: ").append(osmxb.getProcessCpuLoad() * 100).append(nl);
        return cpuInfo.toString();
    }

    public static String getMemoryInfo() {
        StringBuilder memoryInfo = new StringBuilder();
        memoryInfo.append("Total Memory: ").append(memory.getTotal() / (1024 * 1024 * 1024)).append(" GB").append(nl);
        memoryInfo.append("Available Memory: ").append(memory.getAvailable() / (1024 * 1024 * 1024)).append(" GB").append(nl);
        return memoryInfo.toString();
    }

    public static String getDiskInfo() {
        StringBuilder diskInfo = new StringBuilder();
        for (HWDiskStore disk : systemInfo.getHardware().getDiskStores()) {
            diskInfo.append("Disk: ").append(disk.getModel()).append(nl);
            diskInfo.append("Size: ").append(disk.getSize() / (1024 * 1024 * 1024)).append(" GB").append(nl);
        }
        return diskInfo.toString();
    }

    public static String getOsInfo() {
        StringBuilder osInfo = new StringBuilder();
        osInfo.append("OS: ").append(os.getFamily()).append(" ").append(os.getVersionInfo().getVersion()).append(nl);
        osInfo.append("Architecture: ").append(osmxb.getArch()).append(nl);
        return osInfo.toString();
    }
}
