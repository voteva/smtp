package com.bmstu.nets.client.scheduler;

import com.bmstu.nets.client.queue.MessageQueueMap;
import lombok.SneakyThrows;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

public class MessageFileReaderSchedulerTest {

    @Ignore
    @Test
    @SneakyThrows(InterruptedException.class)
    public void testRun() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new MessageFileReaderScheduler());
        Thread.sleep(5000);
        executorService.shutdownNow();

        MessageQueueMap messageQueueMap = MessageQueueMap.instance();
        assertEquals(1, messageQueueMap.getAllDomains().size());
    }
}
