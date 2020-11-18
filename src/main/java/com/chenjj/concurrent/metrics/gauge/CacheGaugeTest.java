package com.chenjj.concurrent.metrics.gauge;

import com.codahale.metrics.CachedGauge;
import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

/**
 * 20-11-17 16:17:22 ==============================================================
 *
 * -- Gauges ----------------------------------------------------------------------
 * cached-db-size
 * ===============queryFromDB===============
 *              value = 1605601042247
 *
 *
 * 20-11-17 16:17:32 ==============================================================
 *
 * -- Gauges ----------------------------------------------------------------------
 * cached-db-size
 *              value = 1605601042247
 *
 *
 * 20-11-17 16:17:42 ==============================================================
 *
 * -- Gauges ----------------------------------------------------------------------
 * cached-db-size
 *              value = 1605601042247
 *
 *
 * 20-11-17 16:17:52 ==============================================================
 *
 * -- Gauges ----------------------------------------------------------------------
 * cached-db-size
 *              value = 1605601042247
 *
 *
 * 20-11-17 16:18:02 ==============================================================
 *
 * -- Gauges ----------------------------------------------------------------------
 * cached-db-size
 * ===============queryFromDB===============
 *              value = 1605601082225
 *
 * 在30秒内Value的值没有发生任何变化，因为它是直接从缓存中获取的数据。
 */
public class CacheGaugeTest {
    private static final MetricRegistry registry = new MetricRegistry();
    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();

    public static void main(String[] args) throws InterruptedException {
        reporter.start(10, TimeUnit.SECONDS);
        registry.register("cached-db-size", new CachedGauge<Long>(30, TimeUnit.SECONDS) {
            @Override
            protected Long loadValue() {
                // 模拟从数据库中查询数据
                return queryFromDB();
            }
        });
        Thread.currentThread().join();
    }

    private static Long queryFromDB() {
        System.out.println("===============queryFromDB===============");
        return System.currentTimeMillis();
    }
}
