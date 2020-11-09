package com.chenjj.concurrent.containers.blockQueue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 在DelayQueue中，每一个元素都必须是Delayed接口的子类
 */
public class DelayQueueTest {
    public static void main(String[] args) throws InterruptedException {
        DelayQueue<DelayedEntry> delayQueue = new DelayQueue<>();
        delayQueue.put(new DelayedEntry("A", 10 * 1000L));
        delayQueue.put(new DelayedEntry("B", 5 * 1000L));
        final long timestamp = System.currentTimeMillis();
        // 非阻塞读方法，立即返回null，原因是当前A、B元素不会有一个到达过期时间
        assert delayQueue.poll() == null;
        // B的延迟时间先到
        DelayedEntry value = delayQueue.take();
        assert value.getValue().equals("B");
        assert (System.currentTimeMillis() - timestamp) >= 5_000L;
        value = delayQueue.take();
        assert value.getValue().equals("A");
        assert (System.currentTimeMillis() - timestamp) >= 10_000L;
    }

    static class DelayedEntry implements Delayed {
        // 数据内容
        private final String value;
        // 用于计算失效时间
        private final long time;

        DelayedEntry(String value, long delayTime) {
            this.value = value;
            this.time = delayTime + System.currentTimeMillis();
        }

        /**
         * 返回当前元素的延迟时间还剩多少个时间单位
         *
         * @param unit
         * @return
         */
        @Override
        public long getDelay(TimeUnit unit) {
            long delta = time - System.currentTimeMillis();
            return unit.convert(delta, TimeUnit.MILLISECONDS);
        }

        /**
         * 队列头部的元素是最早即将失效的元素
         *
         * @param o
         * @return
         */
        @Override
        public int compareTo(Delayed o) {
            if (this.time < ((DelayedEntry) o).time) {
                return -1;
            } else if (this.time > ((DelayedEntry) o).time) {
                return 1;
            } else {
                return 0;
            }
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "DelayedEntry{" +
                    "value='" + value + '\'' +
                    ", time=" + time +
                    '}';
        }
    }
}
