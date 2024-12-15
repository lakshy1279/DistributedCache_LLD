package org.example.utils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StripedExecutor {
    private List<ExecutorService> executors;
    public StripedExecutor(int numThreads) {
        for(int i = 0; i < numThreads; i++)
        {
            executors.add(Executors.newSingleThreadExecutor());
        }
    }

    public Future<Void> submit(int id, Runnable task) {
          return CompletableFuture.runAsync(task, executors.get(id));
    }
}
