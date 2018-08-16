package com.upperz;

import com.upperz.Runnables.ThresholdCallable;

import java.util.HashMap;
import java.util.concurrent.*;

public class Threshold {

    private HashMap<Integer, Future<Integer>> thresholdMap = new HashMap<>();

    public Threshold(){}

    public void calculate() {
        ExecutorService executor = Executors.newFixedThreadPool(40);
        for(int y = 50; y < 140; y++) {

            Callable<Integer> thresholdCallable = new ThresholdCallable(y);
            Future<Integer> future = executor.submit(thresholdCallable);
            thresholdMap.put(y, future);
        }
        //Runnable thresholdRunner = new ThresholdRunner(y);
        //executor.execute(thresholdRunner);}
        executor.shutdown();
        while (!executor.isTerminated()) {}
        for(int i = 50; i < 140; i++)
            try {
                System.out.println(String.valueOf(thresholdMap.get(i).get()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        System.out.println("\nFinished all threads");
    }
}
