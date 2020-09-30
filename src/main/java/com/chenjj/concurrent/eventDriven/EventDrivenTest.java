package com.chenjj.concurrent.eventDriven;

/**
 * 通过这个例子发现，不同数据的处理过程之间根本无需知道彼此的存在，这一切都由EventDispatcher这个Router来控制，它会给你
 * 想要的一切，只是一种松耦合的设计。
 */
public class EventDrivenTest {
    public static void main(String[] args) {
        EventDispatcher dispatcher = new EventDispatcher();
        dispatcher.registerChannel(InputEvent.class, new InputEventHandler(dispatcher));
        dispatcher.registerChannel(ResultEvent.class, new ResultEventHandler());
        dispatcher.dispatch(new InputEvent(1, 2));
    }

    static class InputEvent extends Event {
        private final int x;
        private final int y;

        InputEvent(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    /**
     * 用于存放结果的Event
     */
    static class ResultEvent extends Event {
        private final int result;

        ResultEvent(int result) {
            this.result = result;
        }

        public int getResult() {
            return result;
        }
    }

    static class InputEventHandler implements Channel<InputEvent> {
        private final EventDispatcher dispatcher;

        InputEventHandler(EventDispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        /**
         * 将计算的结果构造成新的Event提交给Router
         *
         * @param message
         */
        @Override
        public void dispatch(InputEvent message) {
            System.out.printf("X:%d, Y:%d\n", message.getX(), message.getY());
            int result = message.getX() + message.getY();
            dispatcher.dispatch(new ResultEvent(result));
        }
    }

    /**
     * 处理ResultEvent的Handler(Channel)，只是简单将计算结果输出到控制台
     */
    static class ResultEventHandler implements Channel<ResultEvent> {

        /**
         * 处理Message
         *
         * @param message
         */
        @Override
        public void dispatch(ResultEvent message) {
            System.out.printf("The result is: " + message.getResult());
        }
    }
}
