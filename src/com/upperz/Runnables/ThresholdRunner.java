package com.upperz.Runnables;


import com.upperz.SpermIdentification;
import org.opencv.highgui.Highgui;

public class ThresholdRunner implements Runnable {

    private int threshold;

    ThresholdRunner(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public void run() {

        SpermIdentification spermIdentification = new SpermIdentification();
        spermIdentification.setImage(Highgui.imread("/Users/dgduncan/Desktop/chip2-sel-p01-amp.png"));
        spermIdentification.setM_wsthLevel(threshold);
        spermIdentification.execute();

    }
}
