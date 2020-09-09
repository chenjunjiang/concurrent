package com.chenjj.concurrent.monitorThread;

public class ObservableThread<T> extends Thread implements Observable {
    private final TaskLifeCycle<T> lifeCycle;
    private final Task<T> task;
    private Cycle cycle;

    public ObservableThread(Task<T> task) {
        this(new TaskLifeCycle.EmptyLifeCycle<>(), task);
    }

    public ObservableThread(TaskLifeCycle<T> lifeCycle, Task<T> task) {
        super();
        if (task == null) {
            throw new IllegalArgumentException("The task is required.");
        }
        this.lifeCycle = lifeCycle;
        this.task = task;
    }

    @Override
    public final void run() {
        // 在执行任务的时候会触发相应的事件
        this.update(Cycle.STARTED, null, null);
        try {
            this.update(Cycle.RUNNING, null, null);
            T result = this.task.call();
            this.update(Cycle.DONE, result, null);
        } catch (Exception e) {
            this.update(Cycle.ERROR, null, e);
        }
    }

    @Override
    public Cycle getCycle() {
        return this.cycle;
    }

    /**
     * 通知事件的监听者(这里指lifeCycle，也就是TaskLifeCycle的实现者，在真正的观察者模式中它可以是个集合)
     *
     * @param cycle
     * @param result
     * @param e
     */
    private void update(Cycle cycle, T result, Exception e) {
        this.cycle = cycle;
        if (lifeCycle == null) {
            return;
        }
        try {
            switch (cycle) {
                case STARTED:
                    this.lifeCycle.onStart(currentThread());
                    break;
                case RUNNING:
                    this.lifeCycle.onRunning(currentThread());
                    break;
                case DONE:
                    this.lifeCycle.onFinish(currentThread(), result);
                    break;
                case ERROR:
                    this.lifeCycle.onError(currentThread(), e);
                    break;
            }
        } catch (Exception ex) {
            if (cycle == Cycle.ERROR) {
                throw ex;
            }
        }
    }
}
