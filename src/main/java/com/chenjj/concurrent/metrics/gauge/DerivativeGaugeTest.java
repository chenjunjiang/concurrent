package com.chenjj.concurrent.metrics.gauge;

import com.codahale.metrics.*;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

/**
 * 20-11-17 16:59:25 ==============================================================
 *
 * -- Gauges ----------------------------------------------------------------------
 * cache-stat
 *              value = CacheStats{hitCount=9, missCount=1, loadSuccessCount=1, loadExceptionCount=0, totalLoadTime=1686600, evictionCount=0}
 * loadExeptionCount
 *              value = 0
 * missCount
 *              value = 1
 *
 * 通过输出我们不难发现，cache-stat会输出所有的cache stats信息，而其他两个则只会输出派生出来的value。
 */
public class DerivativeGaugeTest {
    // 定义Cache
    private static final LoadingCache<String, String> cache = CacheBuilder.newBuilder().maximumSize(10)
            .expireAfterAccess(5, TimeUnit.SECONDS)
            // 开启Cache Stats统计功能
            .recordStats()
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    return key.toUpperCase();
                }
            });
    private static final MetricRegistry registry = new MetricRegistry();
    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();

    public static void main(String[] args) throws InterruptedException {
        reporter.start(10, TimeUnit.SECONDS);
        Gauge<CacheStats> cacheGauge = registry.register("cache-stat", cache::stats);
        // 通过cacheGauge派生missCount metric，并且注册到registry
        registry.register("missCount", new DerivativeGauge<CacheStats, Long>(cacheGauge) {
            @Override
            protected Long transform(CacheStats stats) {
                return stats.missCount();
            }
        });

        // 通过cacheGauge派生loadExceptionCountMetric，并且注册到registry
        registry.register("loadExeptionCount", new DerivativeGauge<CacheStats, Long>(cacheGauge) {
            @Override
            protected Long transform(CacheStats stats) {
                return stats.loadExceptionCount();
            }
        });

        while (true) {
            business();
            TimeUnit.SECONDS.sleep(1);
        }
    }

    private static void business() {
        cache.getUnchecked("alex");
    }
}
