package com.chenjj.concurrent.monitorThread;

import java.util.concurrent.TimeUnit;

public class TestObservable {
    public static void main(String[] args) {
        // ObservableThread和Thread在使用上没有什么区别
        /*Observable observableThread = new ObservableThread<>(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("done.");
            return null;
        });
        observableThread.start();*/

        TaskLifeCycle<String> lifeCycle = new TaskLifeCycle.EmptyLifeCycle<String>() {
            @Override
            public void onFinish(Thread thread, String result) {
                System.out.println("The result is: " + result);
            }
        };
        Observable observableThread = new ObservableThread<>(lifeCycle, () -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("done.");
            return "Hello Observer";
        });
        observableThread.start();
    }
}
