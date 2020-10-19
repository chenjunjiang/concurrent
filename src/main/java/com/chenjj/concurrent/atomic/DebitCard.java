package com.chenjj.concurrent.atomic;

/**
 * 个人账号被设计为不可变对象，一旦创建就无法进行修改。
 * 个人账号类只包含两个字段：账号名、现金数字。
 * 为了便于验证，我们约定个人账号的现金只能增多而不能减少。
 */
public class DebitCard {
    private final String account;
    private final int amount;

    public DebitCard(String account, int amount) {
        this.account = account;
        this.amount = amount;
    }

    public String getAccount() {
        return account;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "DebitCard{" +
                "account='" + account + '\'' +
                ", amount=" + amount +
                '}';
    }
}
