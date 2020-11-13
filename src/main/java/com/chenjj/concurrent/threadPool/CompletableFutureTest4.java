package com.chenjj.concurrent.threadPool;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureTest4 {
    public static void main(String[] args) {
        // 通过thenCompose将两个Future合并成一个Future
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Java")
                // s为上一个Future的计算结果
                .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " Scala"));
        // 合并后的Future通过thenApply方法组成任务链
        // JAVA SCALA
        completableFuture.thenApply(String::toUpperCase).thenAccept(System.out::println);

        // s1为第一个Future的计算结果，s2为第二个Future的计算结果
        completableFuture = CompletableFuture.supplyAsync(() -> "Java").thenCombine(CompletableFuture
                .supplyAsync(() -> "Scala"), (s1, s2) -> s1 + s2);
        // 合并后的Future通过thenApply方法组成任务链
        completableFuture.thenApply(String::toUpperCase).thenAccept(System.out::println);
    }
}
