package com.chenjj.concurrent.threadPool;

/**
 * 主要用于当Queue中的Runnable达到了limit上限时，决定采用何种策略通知提交者。该接口中定义了三种默认的实现。
 */
public interface DenyPolicy {
    void reject(Runnable runnable, ThreadPool threadPool);

    /**
     * 该拒绝策略会直接将任务丢弃
     */
    class DiscardDenyPolicy implements DenyPolicy {

        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            // do noting
        }
    }

    class AbortDenyPolicy implements DenyPolicy {

        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            throw new RunnableDenyException("The runnable " + runnable + " will be abort.");
        }
    }

    /**
     * 该拒绝策略会使任务在提交者所在的线程中执行，而不会被加入到线程池中。
     */
    class RunnerDenyPolicy implements DenyPolicy {

        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            if (!threadPool.isShutdown()) {
                runnable.run();
            }
        }
    }
}
