package com.chenjj.concurrent.balking;

import java.util.concurrent.TimeUnit;

public class AutoSaveThread extends Thread {
    private final Document document;

    public AutoSaveThread(Document document) {
        super("DocumentAutoSaveThread");
        this.document = document;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 每个1s自动保存一次文档
                document.save();
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                break;
            }
        }
    }
}
