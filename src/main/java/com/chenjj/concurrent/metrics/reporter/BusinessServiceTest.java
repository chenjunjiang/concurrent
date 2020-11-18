package com.chenjj.concurrent.metrics.reporter;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

public class BusinessServiceTest {
    private static final MetricRegistry registry = new MetricRegistry();
    // 定义reporter
    private static final JmxReporter reporter = JmxReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();

    public static void main(String[] args) {
        reporter.start();
        BusinessService businessService = new BusinessService();
        registry.registerAll(businessService);
        businessService.start();
    }
}
