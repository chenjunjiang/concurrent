package com.chenjj.concurrent.metrics.reporter;

import com.codahale.metrics.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 20-11-18 14:29:22 ==============================================================
 *
 * -- Gauges ----------------------------------------------------------------------
 * cloud-disk-upload-suc-rate
 *              value = 0.8305084745762712
 *
 * -- Counters --------------------------------------------------------------------
 * cloud-disk-upload-fail
 *              count = 0
 * cloud-disk-upload-success
 *              count = 49
 * cloud-disk-upload-total
 *              count = 59
 *
 * -- Histograms ------------------------------------------------------------------
 * cloud-disk-upload-volume
 *              count = 49
 *                min = 128
 *                max = 9641
 *               mean = 4907.03
 *             stddev = 2781.87
 *             median = 5469.00
 *               75% <= 7044.00
 *               95% <= 9312.00
 *               98% <= 9641.00
 *               99% <= 9641.00
 *             99.9% <= 9641.00
 *
 * -- Timers ----------------------------------------------------------------------
 * cloud-disk-upload-frequency
 *              count = 58
 *          mean rate = 4.39 calls/second
 *      1-minute rate = 2.44 calls/second
 *      5-minute rate = 2.25 calls/second
 *     15-minute rate = 2.22 calls/second
 *                min = 0.00 seconds
 *                max = 0.30 seconds
 *               mean = 0.17 seconds
 *             stddev = 0.07 seconds
 *             median = 0.20 seconds
 *               75% <= 0.20 seconds
 *               95% <= 0.20 seconds
 *               98% <= 0.20 seconds
 *               99% <= 0.30 seconds
 *             99.9% <= 0.30 seconds
 */
public class ConsoleReporterTest {
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
    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
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
