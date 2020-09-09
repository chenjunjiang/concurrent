package com.chenjj.concurrent.monitorThread;

public interface TaskLifeCycle<T> {
    // 任务启动时会触发onStart方法
    void onStart(Thread thread);

    // 任务正在运行时会触发onRunning方法
    void onRunning(Thread thread);

    // 任务运行结束时会触发onFinish方法，其中result是任务执行结束后的结果
    void onFinish(Thread thread, T result);

    // 任务发送异常时会触发onError
    void onError(Thread thread, Exception e);

    /**
     * TaskLifeCycle的空实现，主要是为了让使用者保持对Thread类的使用习惯
     *
     * @param <T>
     */
    class EmptyLifeCycle<T> implements TaskLifeCycle<T> {

        @Override
        public void onStart(Thread thread) {

        }

        @Override
        public void onRunning(Thread thread) {

        }

        @Override
        public void onFinish(Thread thread, T result) {

        }

        @Override
        public void onError(Thread thread, Exception e) {

        }
    }
}
