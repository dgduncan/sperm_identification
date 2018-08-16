package com.upperz;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Java conversion by Derek Duncan on 6/7/16.
 *
 * Source and Credit to Antonio Carlos Sobieranski
 */
public class SpermIdentification
{
    /************OPENCV******************/
    Mat m_inputImage;
    Mat m_outputImage;
    Mat m_resultImage;
    /************************************/


    /**************NATIVE*****************/
    int m_windowSize;
    int m_wsthLevel;

    private String TAG = getClass().getSimpleName();
    /*************************************/

    public SpermIdentification()
    {
        m_inputImage = null;
        m_windowSize = 70;
        m_wsthLevel = 100;
    }


    /***
     * method used to start the image conversion process
     */
    public int execute()
    {

        if(m_inputImage != null)
        {
            if(m_inputImage.empty())
            {
                System.out.println("Empty input image, unknown functionality from here on out");
            }
        }
        else
        {
            System.out.println("Null input image, unknown functionality from here on out");
        }


        Mat wsImage = Ws();

        return FindHeadCandidates(wsImage).size();


    }

    /***
     * Used to find the heads of sperm.
     * @param inputImage image you wish to find the heads of sperm in
     * @return a list of matrices that contains the information for the sperm heads
     */
    public List<MatOfPoint> FindHeadCandidates(Mat inputImage)
    {
        Mat buff = inputImage.clone();

        Imgproc.Canny(buff, buff, 100, 100, 3, true);


        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(buff.clone(), contours, new Mat(),  Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);



        List<MatOfPoint2f> contoursPolyApprox = new ArrayList<>();
        for (int idx = 0; idx < contours.size(); idx++)
        {
            //Required for approxPolyDP calculation
            MatOfPoint2f contoursAs2f = new MatOfPoint2f(contours.get(idx).toArray());

            MatOfPoint2f polyApprox = new MatOfPoint2f();
            Imgproc.approxPolyDP(contoursAs2f, polyApprox, 1, true);
            //cv::approxPolyDP(contours.at(idx), polyApprox, 1, true);
            contoursPolyApprox.add(polyApprox);
        }

        /*REQUIRED CONVERSION*/
        List<MatOfPoint> spermHeads = new ArrayList<>();//= contoursPolyApprox;
        for(int i = 0; i < contoursPolyApprox.size(); i++)
        {
            MatOfPoint approxf1 = new MatOfPoint();
            contoursPolyApprox.get(i).convertTo(approxf1, CvType.CV_32S);
            spermHeads.add(approxf1);

        }



        Mat buffResult = m_inputImage.clone();

        for (int idx = 0; idx < spermHeads.size(); idx++)
        {
            Imgproc.drawContours(buffResult, spermHeads, idx, new Scalar(0, 0, 255), 2);

            Point pxy;
            pxy = spermHeads.get(idx).toList().get(0);

            //pxy = spermHeads.get(idx).
            //cv::Point2d pxy = spermHeads.at(idx).at(0);
            //String sst =  "#" + String.valueOf(idx+1);
            //Imgproc.putText(buffResult, sst, new Point(pxy.x, pxy.y), Core.FONT_HERSHEY_SCRIPT_SIMPLEX, 2, new Scalar(255), 2);
            //cv::putText(buffResult, sst.str(), cv::Point(pxy.x,pxy.y), cv::FONT_HERSHEY_SCRIPT_SIMPLEX, 2, cv::Scalar::all(255), 2, 2);
        }





        m_outputImage = buffResult;

        /*System.out.println("Threshold value : " +
                String.valueOf(m_wsthLevel) +
                " : Spermhead count : " +
                String.valueOf(spermHeads.size()));*/

        return spermHeads;




    }


    /***
     * Set the value of m_inputImage
     * @param inputImage the image you want to assign to m_inputImage
     */
    public void setImage(Mat inputImage)
    {
        m_inputImage = inputImage.clone();
    }

    /***
     * set the wsthLevel
     * @param newLevel the value to be set to wsthLevel
     */
    public void setM_wsthLevel(int newLevel)
    {
        m_wsthLevel = newLevel;
    }

    /***
     * returns the outputImage generated by the program
     * @return the matrix value of the image
     */
    public Mat getOutputImage()
    {
        return m_outputImage;
    }

    /***
     * returns the resultImage generated by the program
     * @return the matrix value of the result
     */
    public Mat getResultImage()
    {
        return m_resultImage;
    }


    /***
     * heavy image processing of the image
     * @return the fully processed image
     */
    public Mat Ws()
    {
        Mat binary = new Mat();
        Imgproc.cvtColor(m_inputImage, binary, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(binary, binary, m_wsthLevel, 255, Imgproc.THRESH_BINARY);


        Mat fg = new Mat();
        Imgproc.erode(binary, fg, new Mat(), new Point(-1,-1), 2);

        // Identify image pixels without objects
        Mat bg = new Mat();
        Imgproc.dilate(binary, bg, new Mat(), new Point(-1, -1), 2);
        Imgproc.threshold(bg, bg, 1, m_wsthLevel, Imgproc.THRESH_BINARY_INV);


        // Create markers image
        Mat markers = new Mat(binary.size(), CvType.CV_8U, new Scalar(0));
        Core.add(fg, bg, markers);


        WatershedSegmenter watershedSegmenter = new WatershedSegmenter();
        watershedSegmenter.setMarkers(markers);


        Mat result = watershedSegmenter.process(m_inputImage);
        result.convertTo(result, CvType.CV_8U);

        m_resultImage = result;

        return result;

    }
}
