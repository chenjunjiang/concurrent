package com.chenjj.concurrent.metrics.reporter;

import com.codahale.metrics.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 在构造CsvReporter的时候需要注意的一点是，build方法的入参必须是一个存在的目录地址，
 * 以用于存放Reporter输出的CSV文件，如果该目录不存在则会出现错误。
 * CsvReporter不会将MetricRegistry中的所有度量指标输出到一个CSV文件中去，而是会为每一个Metric生成
 * 一个CSV文件，这也是非常容易理解的，毕竟不同的Metric所输出的度量指标数据存在差异。
 */
public class CsvReporterTest {
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
    private static final Path PATH = Paths.get("D:", "metrics");
    // 定义reporter
    private static final CsvReporter reporter = CsvReporter.forRegistry(registry)
            .formatFor(Locale.US)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build(PATH.toFile());
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
