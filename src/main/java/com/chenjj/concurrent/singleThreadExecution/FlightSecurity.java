package com.chenjj.concurrent.singleThreadExecution;

/**
 * Java中经常会听到线程安全的类和线程非安全的类，所谓线程安全的类是指多个线程在对某个类的实例同时进行操作时，
 * 不会引起数据不一致的问题，反之则是非线程安全的类，在线程安全类中经常会看到synchronized关键字，比如：StringBuffer
 */
public class FlightSecurity {
    private int count;
    // 登机牌
    private String boardingPass = "";
    // 身份证
    private String idCard = "";

    public synchronized void pass(String boardingPass, String idCard) {
        this.boardingPass = boardingPass;
        this.idCard = idCard;
        this.count++;
        check();
    }

    /*public void pass(String boardingPass, String idCard) {
        this.boardingPass = boardingPass;
        this.idCard = idCard;
        this.count++;
        check();
    }*/

    private void check() {
        // 登机牌和身份证首字母与不相同则表示不通过
        if (this.boardingPass.charAt(0) != idCard.charAt(0)) {
            throw new RuntimeException("===Exception===" + toString());
        }
    }

    public String toString() {
        return "The " + count + " passengers, boardingPass [" + boardingPass + "], idCard [" + idCard + "]";
    }
}
