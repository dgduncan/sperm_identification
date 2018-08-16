package com.upperz;

import org.opencv.core.Core;

public class Main {

    //Load OpenCV library for use
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String[] args) {

        long startTime = System.nanoTime();

        Threshold threshold = new Threshold();
        threshold.calculate();

        System.out.println("Elapsed Time in Seconds : " + (System.nanoTime() - startTime) / Math.pow(10, 9));

    }
}
