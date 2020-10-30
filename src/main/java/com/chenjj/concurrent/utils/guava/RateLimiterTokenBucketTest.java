package com.chenjj.concurrent.utils.guava;

public class RateLimiterTokenBucketTest {
    private static final RateLimiterTokenBucket tokenBucket = new RateLimiterTokenBucket();

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                while (true) {
                    // 抢购商品
                    try {
                        tokenBucket.bookOrder(prodID -> {
                            System.out.println("User: " + Thread.currentThread() + " book the prod order and prodID: " + prodID);
                        });
                    } catch (RateLimiterTokenBucket.OrderFailedException e) {
                        System.out.println("book order failed, please try again.");
                        // 抢购失败，尝试重新抢购
                    } catch (RateLimiterTokenBucket.NoProductionException e) {
                        // 商品已售罄，退出抢购
                        System.out.println("No available production now.");
                        break;
                    }
                }
            }).start();
        }
    }
}
