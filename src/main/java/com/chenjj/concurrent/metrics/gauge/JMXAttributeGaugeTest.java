package com.chenjj.concurrent.metrics.gauge;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.JmxAttributeGauge;
import com.codahale.metrics.MetricRegistry;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.concurrent.TimeUnit;

/**
 * 20-11-17 15:22:25 ==============================================================
 *
 * -- Gauges ----------------------------------------------------------------------
 * com.chenjj.concurrent.metrics.gauge.JMXAttributeGaugeTest.Heap Memory
 *              value = javax.management.openmbean.CompositeDataSupport(compositeType=javax.management.openmbean.CompositeType(name=java.lang.management.MemoryUsage,items=((itemName=committed,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)),(itemName=init,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)),(itemName=max,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)),(itemName=used,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)))),contents={committed=124780544, init=130023424, max=1840250880, used=8191392})
 * com.chenjj.concurrent.metrics.gauge.JMXAttributeGaugeTest.NonHeap Memory
 *              value = javax.management.openmbean.CompositeDataSupport(compositeType=javax.management.openmbean.CompositeType(name=java.lang.management.MemoryUsage,items=((itemName=committed,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)),(itemName=init,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)),(itemName=max,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)),(itemName=used,itemType=javax.management.openmbean.SimpleType(name=java.lang.Long)))),contents={committed=14942208, init=2555904, max=-1, used=14684608})
 */
public class JMXAttributeGaugeTest {
    private static final MetricRegistry registry = new MetricRegistry();
    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();

    public static void main(String[] args) throws MalformedObjectNameException, InterruptedException {
        // // 启动Reporter，每隔10秒的时间输出一次数据。
        reporter.start(10, TimeUnit.SECONDS);
        // 注册JmxAttributeGauge，主要输出堆内存的使用情况
        registry.register(MetricRegistry.name(JMXAttributeGaugeTest.class, "Heap Memory"),
                new JmxAttributeGauge(new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage"));

        // 注册JmxAttributeGauge，主要输出非堆内存的使用情况
        registry.register(MetricRegistry.name(JMXAttributeGaugeTest.class, "NonHeap Memory"),
                new JmxAttributeGauge(new ObjectName("java.lang:type=Memory"), "NonHeapMemoryUsage"));

        Thread.currentThread().join();
    }
}
