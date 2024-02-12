package cezeri.image_processing;

/**
 * @author  : Hasan NASER.
 * Date     : 13.12.2020
 * Time     : 13:26
 * About    : implement "image moment", "centrel moment" and "Hu Moments Invariants"
 *
 * referance : https://www.learnopencv.com/shape-matching-using-hu-moments-c-python/
 *===================================================================================
 */


public class MomentInvariants {


    public double[][] imageArray;
    private final double nCentrelMoment_30;
    private final double nCentrelMoment_12;
    private final double nCentrelMoment_21;
    private final double nCentrelMoment_03;
    private final double nCentrelMoment_20;
    private final double nCentrelMoment_02;
    private final double nCentrelMoment_11;


    public MomentInvariants(double[][] imageArray){
        this.imageArray = imageArray;
        this.nCentrelMoment_30 = this.nCentrelMoment(3, 0);
        this.nCentrelMoment_12 = this.nCentrelMoment(1, 2);
        this.nCentrelMoment_21 = this.nCentrelMoment(2, 1);
        this.nCentrelMoment_03 = this.nCentrelMoment(0, 3);
        this.nCentrelMoment_20 = this.nCentrelMoment(2, 0);
        this.nCentrelMoment_02 = this.nCentrelMoment(0, 2);
        this.nCentrelMoment_11 = this.nCentrelMoment(1, 1);
    }


     /**
      * @param i x value of pixel
      * @param j y value of pixel
      * @return normalized central moments
      */
    public double nCentrelMoment(int i, int j) {
        return this.centrelMoment(i, j) / Math.pow(this.centrelMoment(0, 0), (((i + j) / 2 + 1)));
    }

     /**
      * @param i x value of pixel
      * @param j y value of pixel
      * @return Central moment
      * about :Central moments are very similar to the raw image moments we saw earlier, except that we subtract off the centroid from the x and y in the moment formula
      */
    public double centrelMoment( int i, int j) {
        double result = 0.0;
        double moment00 = this.moment(0, 0);
        double f1 = (this.moment( 1, 0) / moment00);
        double f2 = (this.moment( 0, 1) / moment00);
        for (int x = 0; x < this.imageArray.length; ++x) {
            for (int y = 0; y < this.imageArray[0].length; ++y) {
                result += (
                        Math.pow((x - f1), i)
                                *
                                Math.pow((x - f2), j)
                )
                        * this.imageArray[x][y];
            }
        }
        return result;

    }

     /**
      * image moment
      * @param i x value of pixel
      * @param j y value of pixel
      * @return image moment
      * about : Image moments are a weighted average of image pixel intensities
      */
    public double moment( int i, int j) {
        double result = 0.0;
        for (int x = 0; x < this.imageArray.length; ++x) {
            for (int y = 0; y < this.imageArray[0].length; ++y) {
                result += (Math.pow(x, i) * Math.pow(y, j)) * this.imageArray[x][y];
            }
        }
        return result;
    }


    /* Hu Moments ( or rather Hu moment invariants ) are a set of 7 numbers calculated using central
      moments that are invariant to image transformations. The first 6 moments have been proved to be invariant to translation,
     scale, and rotation, and reflection. While the 7th moment’s sign changes for image reflection. */

    public double getMomentInvariants_h0(){
        return this.nCentrelMoment_20 + this.nCentrelMoment_02;
    }
    public double getMomentInvariants_h1(){
        return Math.pow(nCentrelMoment_20 - nCentrelMoment_02, 2) + 4 * Math.pow(nCentrelMoment_11, 2);
    }
    public double getMomentInvariants_h2(){
        return Math.pow(nCentrelMoment_30 - nCentrelMoment_12, 2) + Math.pow((3 * nCentrelMoment_21 - nCentrelMoment_03), 2);
    }
    public double getMomentInvariants_h3(){
        return Math.pow(nCentrelMoment_30 + nCentrelMoment_21, 2) + Math.pow(nCentrelMoment_21 + nCentrelMoment_03, 2);
    }
    public double getMomentInvariants_h4(){
        return (nCentrelMoment_30 - 3 * nCentrelMoment_12)
                * (nCentrelMoment_30 + nCentrelMoment_12) * ( Math.pow(nCentrelMoment_30 + nCentrelMoment_12, 2) - 3 * Math.pow(nCentrelMoment_21 + nCentrelMoment_03, 2) )
                + ( (3 * nCentrelMoment_21 - nCentrelMoment_03) * ( 3 * Math.pow(nCentrelMoment_30 + nCentrelMoment_12, 2) - Math.pow(nCentrelMoment_21 + nCentrelMoment_03, 2) ) );
    }
    public double getMomentInvariants_h5(){
        return (nCentrelMoment_20-nCentrelMoment_02)
                * ( Math.pow((nCentrelMoment_30+nCentrelMoment_12),2) - Math.pow((nCentrelMoment_21+nCentrelMoment_03),2) + 4 * nCentrelMoment_11 * (nCentrelMoment_30 +nCentrelMoment_12) * (nCentrelMoment_21 +nCentrelMoment_03) );

    }
    public double getMomentInvariants_h6(){
        return (3*nCentrelMoment_21 - nCentrelMoment_03)
                * (nCentrelMoment_30 + nCentrelMoment_12) * ( Math.pow(nCentrelMoment_30 + nCentrelMoment_12, 2) - 3 * Math.pow(nCentrelMoment_21 + nCentrelMoment_03, 2) )
                + ( (nCentrelMoment_30 - 3 * nCentrelMoment_12) * (nCentrelMoment_21 - nCentrelMoment_03) * ( 3 * Math.pow(nCentrelMoment_30 + nCentrelMoment_12, 2) - Math.pow(nCentrelMoment_21 + nCentrelMoment_03, 2) ) );
    }
}
