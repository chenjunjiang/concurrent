package com.chenjj.concurrent.jmh;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 对比两种List容器的add方法的性能时，我们将分别对其进行十组测试，每组测试都将会对List执行1 000 000次的add调用，
 * 然后通过最大值、最小值、平均值的方式对其进行对比（当然，这里并不考虑不同容器的内存开销，
 * 事实上，ArrayList和LinkedList在使用的过程中，它们的内存开销肯定是不一样的）。
 * 我们的这种测试存在诸多问题，具体列举如下：
 * 1、使用Stopwatch进行时间计算，其实是在Stopwatch内部记录了方法执行的开始纳秒数，这种操作本身会导致一些CPU时间的浪费。
 * 2、在代码的运行过程中，JVM可能会对其进行运行时的优化，比如循环展开、运行时编译等，这样会导致某组未经优化的性能数据参与统计计算。
 * 3、arrayListPerfTest方法和linkedListPerfTest的运行环境并不公平，比如，在第一个测试方法执行的过程中或者执行结束后，
 * 其所在的JVM进程或许已经进行了profiler的优化，还有第一个测试方法所开辟的内存有可能也未被释放。
 */
public class ArrayListVSLinkedList {
    private final static String DATA = "DUMMY DATA";
    private final static int MAX_CAPACITY = 100_00_00;
    private final static int MAX_ITERATIONS = 10;

    public static void main(String[] args) {
        arrayListPerfTest(MAX_ITERATIONS);
        System.out.println(Strings.repeat("#", 100));
        linkedListPerfTest(MAX_ITERATIONS);
    }

    private static void test(List<String> list) {
        for (int i = 0; i < MAX_CAPACITY; i++) {
            list.add(DATA);
        }
    }

    private static void arrayListPerfTest(int iterations) {
        for (int i = 0; i < iterations; i++) {
            final List<String> lits = new ArrayList<>();
            final Stopwatch stopwatch = Stopwatch.createStarted();
            test(lits);
            System.out.println(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
        }
    }

    private static void linkedListPerfTest(int iterations) {
        for (int i = 0; i < iterations; i++) {
            final List<String> lits = new LinkedList<>();
            final Stopwatch stopwatch = Stopwatch.createStarted();
            test(lits);
            System.out.println(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
        }
    }
}
