package com.chenjj.concurrent.balking;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 代表正在编辑的文档
 */
public class Document {
    // 如果文档发生改变，changed会设置为true
    private boolean changed = false;
    // 每次待保存的内容
    private List<String> content = new ArrayList<>();

    private final FileWriter writer;

    // 自动保存文档的线程
    private static AutoSaveThread autoSaveThread;

    private Document(String documentPath, String documentName) throws IOException {
        this.writer = new FileWriter(new File(documentPath, documentName));
    }

    /**
     * 创建文档，顺便启动自动保存
     *
     * @return
     */
    public static Document create(String documentPath, String documentName) throws IOException {
        Document document = new Document(documentPath, documentName);
        autoSaveThread = new AutoSaveThread(document);
        autoSaveThread.start();
        return document;
    }

    /**
     * 编辑文档，其实就是往content列表中提交字符串
     *
     * @param text
     */
    public void edit(String text) {
        synchronized (this) {
            this.content.add(text);
            this.changed = true;
        }
    }

    /**
     * 关闭文档的时候首先中断自动保存线程，然后关闭writer释放资源
     *
     * @throws IOException
     */
    public void close() throws IOException {
        autoSaveThread.interrupt();
        writer.close();
    }

    /**
     * 外部显示进行文档保存
     */
    public void save() throws IOException {
        synchronized (this) {
            // 如果文档已经被保存，表示已经没有新的编辑内容需要保存了，则直接返回
            if (!changed) {
                return;
            }
            System.out.println(Thread.currentThread() + " execute the save action");
            // 将内容写入文档中
            for (String cacheLine : content) {
                this.writer.write(cacheLine);
                this.writer.write("\r\n");
            }
            this.writer.flush();
            // 表示此时已经没有新编辑的内容了
            this.changed = false;
            this.content.clear();
        }
    }
}
