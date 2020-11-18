package com.chenjj.concurrent.metrics.reporter;

import com.codahale.metrics.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * MetricSet不仅是一个特殊的Metric，还是一个存放Metric的集合，之前的代码通过若干次的
 * register方法调用将对应的Metric实例注册到Metric Registry中，这种方式会稍显麻烦，
 * 如果是通过MetricSet接口，那么注册的过程就会简洁很多，另外MetricSet的出现，可以便于
 * 对某些不同类型的Metric进行归类。
 */
public class BusinessService extends Thread implements MetricSet {
    private final Map<String, Metric> metrics = new HashMap<>();
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
    // 统计文件上传成功率
    private static final RatioGauge successGauge = new RatioGauge() {
        @Override
        protected Ratio getRatio() {
            return Ratio.of(successBusiness.getCount(), totalBusiness.getCount());
        }
    };

    public BusinessService() {
        metrics.put("cloud-disk-upload-total", totalBusiness);
        metrics.put("cloud-disk-upload-success", successBusiness);
        metrics.put("cloud-disk-upload-fail", failBusiness);
        metrics.put("cloud-disk-upload-frequency", timer);
        metrics.put("cloud-disk-upload-volume", histogram);
        metrics.put("cloud-disk-upload-suc-rate", successGauge);
    }

    @Override
    public void run() {
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

    /**
     * A map of metric names to metrics.
     *
     * @return the metrics
     */
    @Override
    public Map<String, Metric> getMetrics() {
        return metrics;
    }
}
