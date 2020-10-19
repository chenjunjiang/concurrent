package com.chenjj.concurrent.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicFieldUpdaterTest {
    public static void main(String[] args) {
        AtomicIntegerFieldUpdater<Alex> updater = AtomicIntegerFieldUpdater.newUpdater(Alex.class, "salary");
        Alex alex = new Alex();
        // 原子操作
        int result = updater.addAndGet(alex, 1);
        assert result == 1;
    }

    static class Alex {
        volatile int salary;

        public int getSalary() {
            return salary;
        }
    }
}
