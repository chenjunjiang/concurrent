package com.chenjj.concurrent.utils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        final int[] products = getProductsByCategoryId();
        List<ProductPrice> list = Arrays.stream(products).mapToObj(ProductPrice::new).collect(Collectors.toList());
        final CountDownLatch latch = new CountDownLatch(products.length);
        list.forEach(productPrice -> {
            new Thread(() -> {
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
                    // 执行完毕之后，计数count down
                    latch.countDown();
                }
            }).start();
        });

        // 主线程一直等待到所有子任务执行完毕才能继续执行
        latch.await();
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
