package com.chenjj.concurrent.eventDriven;

import java.util.concurrent.TimeUnit;

public class AsyncEventDrivenTest {
    public static void main(String[] args) {
        AsyncEventDispatcher dispatcher = new AsyncEventDispatcher();
        AsyncInputEventHandler inputEventHandler = new AsyncInputEventHandler(dispatcher);
        AsyncResultEventHandler resultEventHandler = new AsyncResultEventHandler();
        dispatcher.registerChannel(InputEvent.class, inputEventHandler);
        dispatcher.registerChannel(ResultEvent.class, resultEventHandler);
        dispatcher.dispatch(new InputEvent(1, 2));
        dispatcher.dispatch(new InputEvent(5, 6));
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

    static class AsyncInputEventHandler extends AsyncChannel {
        private final AsyncEventDispatcher dispatcher;

        AsyncInputEventHandler(AsyncEventDispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        @Override
        protected void handle(Event message) {
            InputEvent inputEvent = (InputEvent) message;
            System.out.printf("X:%d, Y:%d\n", inputEvent.getX(), inputEvent.getY());
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int result = inputEvent.getX() + inputEvent.getY();
            dispatcher.dispatch(new ResultEvent(result));
        }
    }

    static class AsyncResultEventHandler extends AsyncChannel {

        @Override
        protected void handle(Event message) {
            ResultEvent resultEvent = (ResultEvent) message;
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("The result is: " + resultEvent.getResult());
        }
    }
}
