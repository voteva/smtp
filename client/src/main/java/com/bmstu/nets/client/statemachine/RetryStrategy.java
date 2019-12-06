package com.bmstu.nets.client.statemachine;

import java.util.Optional;

public interface RetryStrategy {
    Optional<Integer> afterSeconds(int retryNumber);
}
