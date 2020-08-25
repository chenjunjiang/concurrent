package com.chenjj.concurrent.thread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 假设你有一个App，主要用于查询航班信息，你的App是没有这些实时数据的，当用户发起查询请求时，你需要到各大航空公司
 * 的接口获取信息，最后统一整理加工返回到App客户端，我们将每一个航空公司的查询都交给一个线程去做，然后在它们结束工作
 * 之后统一对数据进行整理，这样就可以极大地节约时间，从而提高用户的体验。
 */
public class TestJoin3 {
    // 各大航空公司
    private static List<String> fightCompany = Arrays.asList("CSA", "CEA", "HNA");

    public static void main(String[] args) {
        // 查询上海到北京的航班
        List<String> results = search("SH", "BJ");
        System.out.println("============result============");
        results.forEach(System.out::println);
    }

    private static List<String> search(String original, String dest) {
        final List<String> result = new ArrayList<>();
        List<FightQueryTask> tasks = fightCompany.stream().map(f -> createFightQueryTask(f, original, dest)).collect(Collectors.toList());
        // 启动线程
        tasks.forEach(Thread::start);
        // 分别调用每一个线程的join方法，阻塞当前线程
        tasks.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        tasks.stream().map(FightQuery::get).forEach(result::addAll);
        return result;
    }

    private static FightQueryTask createFightQueryTask(String flight, String original, String dest) {
        return new FightQueryTask(flight, original, dest);
    }
}
