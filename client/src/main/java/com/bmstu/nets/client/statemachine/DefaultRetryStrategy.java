package com.bmstu.nets.client.statemachine;

import java.util.Optional;

public class DefaultRetryStrategy
        implements RetryStrategy {

    private int maxRetryCount;

    public DefaultRetryStrategy(int maxRetryCount) {
        // TODO check maxRetryCount > 0
        this.maxRetryCount = maxRetryCount;
    }

    @Override
    public Optional<Integer> afterSeconds(int retryNumber) {
        //return retryNumber > maxRetryCount ? Optional.<Integer>absent() : Optional.of(delay(retryNumber)); TODO
        return Optional.of(3);
    }
}
