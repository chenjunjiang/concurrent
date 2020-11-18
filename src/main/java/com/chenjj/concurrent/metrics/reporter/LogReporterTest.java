package com.chenjj.concurrent.metrics.reporter;

import com.codahale.metrics.*;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class LogReporterTest {
    private static final MetricRegistry registry = new MetricRegistry();
    // 统计所有上传文件数量
    private static final Counter totalBusiness = new Counter();
    // 统计所有上传成功的数量
    private static final Counter successBusiness = new Counter();
    // 统计所有上传失败的数量
    private static final Counter failBusiness = new Counter();
    // 统计每一个文件上传耗时
    private static final Timer timer = new Timer();
    // 统计上传字节
    private static final Histogram histogram = new Histogram(new ExponentiallyDecayingReservoir());
    // 定义reporter
    private static final Slf4jReporter reporter = Slf4jReporter.forRegistry(registry)
            // "com.chenjj.concurrent.metrics"要与log4j2.xml中的logger name一致
            .outputTo(LoggerFactory.getLogger("com.chenjj.concurrent.metrics"))
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();
    // 统计文件上传成功率
    private static final RatioGauge successGauge = new RatioGauge() {
        @Override
        protected Ratio getRatio() {
            return Ratio.of(successBusiness.getCount(), totalBusiness.getCount());
        }
    };

    static {
        registry.register("cloud-disk-upload-total", totalBusiness);
        registry.register("cloud-disk-upload-success", successBusiness);
        registry.register("cloud-disk-upload-fail", failBusiness);
        registry.register("cloud-disk-upload-frequency", timer);
        registry.register("cloud-disk-upload-volume", histogram);
        registry.register("cloud-disk-upload-suc-rate", successGauge);
    }

    public static void main(String[] args) {
        reporter.start(10, TimeUnit.SECONDS);
        while (true) {
            upload(new byte[ThreadLocalRandom.current().nextInt(10_000)]);
        }
    }

    /**
     * 模拟文件上传
     *
     * @param buffer
     */
    private static void upload(byte[] buffer) {
        totalBusiness.inc();
        Timer.Context context = timer.time();
        try {
            int x = 1 / ThreadLocalRandom.current().nextInt(10);
            TimeUnit.MILLISECONDS.sleep(200);
            // 上传成功后，将字节数量纳入histogram统计
            histogram.update(buffer.length);
            // 上传成功后增加successBusiness
            successBusiness.inc();
        } catch (Exception e) {
            // 上传失败failBusiness增加
        } finally {
            context.close();
        }
    }
}
