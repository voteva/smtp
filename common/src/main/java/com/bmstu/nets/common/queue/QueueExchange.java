package com.bmstu.nets.common.queue;

import javax.annotation.Nonnull;

public interface QueueExchange<T> {
    void enqueue(@Nonnull T item);
    T dequeue();
}
