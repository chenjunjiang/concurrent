package com.chenjj.concurrent.metrics.plugin;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.ClassLoadingGaugeSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

import java.util.concurrent.TimeUnit;

/**
 * 20-11-18 17:02:39 ==============================================================
 *
 * -- Gauges ----------------------------------------------------------------------
 * PS-MarkSweep.count
 *              value = 0
 * PS-MarkSweep.time
 *              value = 0
 * PS-Scavenge.count
 *              value = 1
 * PS-Scavenge.time
 *              value = 7
 * blocked.count
 *              value = 0
 * count
 *              value = 7
 * daemon.count
 *              value = 6
 * deadlock.count
 *              value = 0
 * deadlocks
 *              value = []
 * loaded
 *              value = 2168
 * new.count
 *              value = 0
 * runnable.count
 *              value = 4
 * terminated.count
 *              value = 0
 * timed_waiting.count
 *              value = 0
 * unloaded
 *              value = 0
 * waiting.count
 *              value = 3
 */
public class JvmInstrumentationTest {
    private static final MetricRegistry registry = new MetricRegistry();
    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();

    public static void main(String[] args) throws InterruptedException {
        registry.registerAll(new GarbageCollectorMetricSet());
        registry.registerAll(new ThreadStatesGaugeSet());
        registry.registerAll(new ClassLoadingGaugeSet());
        reporter.start(10, TimeUnit.SECONDS);
        Thread.currentThread().join();
    }
}
