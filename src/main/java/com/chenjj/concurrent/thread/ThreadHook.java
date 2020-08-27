package com.chenjj.concurrent.thread;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 我们在开发中经常会遇到Hook线程，比如为了防止某个程序被重复启动，在进程启动的时候会创建一个lock文件，
 * 进程收到中断信号的时候会删除这个lock文件，我们在mysql服务器、zookeeper、kafka等系统中都能看到lock
 * 文件的存在，下面我们模拟一个防止重复启动的程序。
 *
 * PosixFilePermissions它只能用于操作系统与POSIX兼容，Windows不支持POSIX文件系统，linux是支持的。
 */
public class ThreadHook {
    private final static String LOCK_PATH = ".";
    private final static String LOCK_FILE = "test.lock";
    private final static String PERMISSIONS = "rw-------";

    public static void main(String[] args) throws IOException {
        // 注入Hook线程，在程序退出时删除lock文件
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("The program received kill SIGNAL.");
            getLockFile().toFile().delete();
        }));

        // 检查是否存在lock文件
        checkRunning();

        // 简单模拟当前程序正在运行
        for (; ; ) {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("program is running.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void checkRunning() throws IOException {
        Path path = getLockFile();
        if (path.toFile().exists()) {
            throw new RuntimeException("The program already running.");
        }
        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString(PERMISSIONS);
        Files.createFile(path, PosixFilePermissions.asFileAttribute(permissions));
    }

    private static Path getLockFile() {
        return Paths.get(LOCK_PATH, LOCK_FILE);
    }
}
