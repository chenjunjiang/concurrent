package com.chenjj.concurrent.eventBus;

import java.io.IOException;
import java.nio.file.*;

public class DirectoryTargetMonitor {
    private WatchService watchService;
    private final EventBus eventBus;
    private final Path path;
    private volatile boolean start = false;

    public DirectoryTargetMonitor(EventBus eventBus, String targetPath) {
        this(eventBus, targetPath, "");
    }

    // 传入EventBus和需要监控的目录
    public DirectoryTargetMonitor(EventBus eventBus, String targetPath, String... morePaths) {
        this.eventBus = eventBus;
        this.path = Paths.get(targetPath, morePaths);
    }

    public void startMonitor() throws Exception {
        this.watchService = FileSystems.getDefault().newWatchService();
        // 为路径注册感兴趣的事件
        this.path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_CREATE);
        System.out.printf("The directory [%s] is monitoring...... \n", path);
        this.start = true;
        while (start) {
            WatchKey watchKey = null;
            try {
                // 当有事件发生时会返回对应的WatchKey
                watchKey = watchService.take();
                watchKey.pollEvents().forEach(watchEvent -> {
                    WatchEvent.Kind kind = watchEvent.kind();
                    Path path = (Path) watchEvent.context();
                    Path child = DirectoryTargetMonitor.this.path.resolve(path);
                    // 提交到EventBus
                    eventBus.post(new FileChangeEvent(child, kind));
                });
            } catch (Exception e) {
                this.start = false;
            } finally {
                if (watchKey != null) {
                    watchKey.reset();
                }
            }
        }
    }

    public void stopMonitor() throws Exception {
        System.out.printf("The directory [%s] monitor will be stop...... \n", path);
        // 上面的watchService.take()会抛出InterruptedException
        Thread.currentThread().interrupt();
        this.start = false;
        this.watchService.close();
        System.out.printf("The directory [%s] monitor will be stop done. \n", path);
    }
}
