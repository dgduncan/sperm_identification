package com.upperz;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Java conversion by Derek Duncan on 6/7/16.
 *
 * Source and Credit to Antonio Carlos Sobieranski
 */
public class WatershedSegmenter
{
    private Mat markers = new Mat();

    public void setMarkers(Mat markerImage)
    {
        markerImage.convertTo(markers, CvType.CV_32S);
    }

    public Mat process(Mat image)
    {
        Imgproc.watershed(image, markers);
        markers.convertTo(markers, CvType.CV_8U);
        return markers;

    }


}
