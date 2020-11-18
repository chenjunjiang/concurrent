package com.chenjj.concurrent.metrics.plugin;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;

import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

/**
 * 20-11-18 16:37:02 ==============================================================
 *
 * -- Gauges ----------------------------------------------------------------------
 * restful-hc
 *              value = {restful-hc=Result{isHealthy=false, message=Detected RESTful server is unhealthy., timestamp=2020-11-18T16:37:03.149+0800}}
 */
public class RESTfulServiceHealthCheckTest {
    public static void main(String[] args) throws InterruptedException {
        final HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();
        healthCheckRegistry.register("restful-hc", new RESTfulServiceHealthCheck());
        final MetricRegistry registry = new MetricRegistry();
        final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
        registry.register("restful-hc", (Gauge<SortedMap<String, HealthCheck.Result>>) () -> healthCheckRegistry.runHealthChecks());
        reporter.start(10, TimeUnit.SECONDS);
        Thread.currentThread().join();
    }
}
