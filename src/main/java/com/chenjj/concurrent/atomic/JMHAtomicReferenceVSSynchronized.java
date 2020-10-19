package com.chenjj.concurrent.atomic;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 20)
@Measurement(iterations = 20)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class JMHAtomicReferenceVSSynchronized {
    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(JMHAtomicReferenceVSSynchronized.class.getSimpleName())
                .forks(1)
                .timeout(TimeValue.seconds(10))
                .addProfiler(StackProfiler.class)
                .build();
        new Runner(options).run();
    }

    @State(Scope.Group)
    public static class SynchronizedRace {
        private DebitCard debitCard = new DebitCard("Alex", 0);

        public void syncInc() {
            synchronized (JMHAtomicReferenceVSSynchronized.class) {
                final DebitCard dc = debitCard;
                final DebitCard newDc = new DebitCard(dc.getAccount(), dc.getAmount() + 10);
                this.debitCard = newDc;
            }
        }
    }

    @State(Scope.Group)
    public static class AtomicReferenceRace {
        private AtomicReference<DebitCard> debitCardAtomicReference = new AtomicReference<>(new DebitCard("Alex", 0));

        public void casInc() {
            final DebitCard dc = debitCardAtomicReference.get();
            final DebitCard newDc = new DebitCard(dc.getAccount(), dc.getAmount() + 10);
            debitCardAtomicReference.compareAndSet(dc, newDc);
        }
    }

    @Benchmark
    @GroupThreads(10)
    @Group("sync")
    public void syncInc(SynchronizedRace race) {
        race.syncInc();
    }

    @Benchmark
    @GroupThreads(10)
    @Group("cas")
    public void casInc(AtomicReferenceRace race) {
        race.casInc();
    }
}
