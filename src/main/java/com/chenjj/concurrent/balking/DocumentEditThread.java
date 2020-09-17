package com.chenjj.concurrent.balking;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * 类似于主动编辑文档的作者
 */
public class DocumentEditThread extends Thread {
    private final String documentPath;
    private final String documentName;

    private final Scanner scanner = new Scanner(System.in);

    public DocumentEditThread(String documentPath, String documentName) {
        super("DocumentEditThread");
        this.documentPath = documentPath;
        this.documentName = documentName;
    }

    @Override
    public void run() {
        int times = 1;
        try {
            Document document = Document.create(documentPath, documentName);
            while (true) {
                String text = scanner.next();
                if ("quit".equals(text)) {
                    document.close();
                    break;
                }
                // 将内容编辑到文档中
                document.edit(text);
                System.out.println(Thread.currentThread() + " times: " + times);
                if (times == 5) {
                    // 假设用户在输入5次之后ctrl+s主动保存
                    document.save();
                    times = 0;
                }
                times++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
