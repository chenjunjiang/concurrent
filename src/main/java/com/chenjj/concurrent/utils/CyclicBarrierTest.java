package com.chenjj.concurrent.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CyclicBarrierTest {
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        final int[] products = getProductsByCategoryId();
        List<ProductPrice> list = Arrays.stream(products).mapToObj(ProductPrice::new).collect(Collectors.toList());
        //final CyclicBarrier barrier = new CyclicBarrier(list.size());
        final CyclicBarrier barrier = new CyclicBarrier(list.size() + 1);
        final List<Thread> threads = new ArrayList<>();
        list.forEach(productPrice -> {
            Thread thread = new Thread(() -> {
                System.out.println(productPrice.getProID() + "-> start calculate price.");
                try {
                    TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
                    if (productPrice.proID % 2 == 0) {
                        productPrice.setPrice(productPrice.proID * 0.9D);
                    } else {
                        productPrice.setPrice(productPrice.proID * 0.71D);
                    }
                    System.out.println(productPrice.getProID() + "-> price calculate completed.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //在此等待其它子线程到达barrier point，所有线程都到达后才会继续执行
                    try {
                        barrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
            threads.add(thread);
            thread.start();
        });

        // 等待所有子任务结束，这种方式不太优雅,可以把barrier的parties数量加1,然后主线程调用await方法
        /*threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });*/
        barrier.await();
        System.out.println("all of prices calculate finished.");
        list.forEach(System.out::println);
    }

    private static int[] getProductsByCategoryId() {
        return IntStream.rangeClosed(1, 10).toArray();
    }

    private static class ProductPrice {
        private final int proID;
        private double price;

        private ProductPrice(int proID) {
            this.proID = proID;
        }

        private ProductPrice(int proID, double price) {
            this.proID = proID;
            this.price = price;
        }

        public int getProID() {
            return proID;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "ProductPrice{" +
                    "proID=" + proID +
                    ", price=" + price +
                    '}';
        }
    }
}
