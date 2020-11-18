package com.chenjj.concurrent.metrics.plugin;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;

import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

/**
 * 20-11-18 16:57:58 ==============================================================
 *
 * -- Gauges ----------------------------------------------------------------------
 * app-health-check
 *              value = {restful-hc=Result{isHealthy=false, message=Detected RESTful server is unhealthy., timestamp=2020-11-18T16:58:00.366+0800}, thread-dead-lock-hc=Result{isHealthy=true, timestamp=2020-11-18T16:58:00.367+0800}}
 */
public class HealthCheckSetTest {
    public static void main(String[] args) throws InterruptedException {
        final HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();
        // 只需要调用两次register即可
        healthCheckRegistry.register("restful-hc", new RESTfulServiceHealthCheck());
        healthCheckRegistry.register("thread-dead-lock-hc", new ThreadDeadlockHealthCheck());
        final MetricRegistry registry = new MetricRegistry();
        final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
        registry.register("app-health-check", (Gauge<SortedMap<String, HealthCheck.Result>>) () -> healthCheckRegistry.runHealthChecks());
        reporter.start(10, TimeUnit.SECONDS);
        Thread.currentThread().join();
    }
}
