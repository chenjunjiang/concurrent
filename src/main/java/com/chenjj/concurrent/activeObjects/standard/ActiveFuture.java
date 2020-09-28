package com.chenjj.concurrent.activeObjects.standard;

import com.chenjj.concurrent.future.FutureTask;

public class ActiveFuture<T> extends FutureTask<T> {
    @Override
    public void finish(T result) {
        super.finish(result);
    }
}
