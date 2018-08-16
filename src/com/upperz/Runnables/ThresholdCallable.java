package com.upperz.Runnables;

import com.upperz.SpermIdentification;
import org.opencv.highgui.Highgui;

import java.util.concurrent.Callable;

public class ThresholdCallable implements Callable {

    private int threshold;

    public ThresholdCallable(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public Object call() throws Exception {
        SpermIdentification spermIdentification = new SpermIdentification();
        spermIdentification.setImage(Highgui.imread("/Users/dgduncan/Desktop/chip2-sel-p01-amp.png"));
        spermIdentification.setM_wsthLevel(threshold);
        return spermIdentification.execute();

    }
}
