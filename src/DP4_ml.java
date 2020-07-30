import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Class to do machine learning component of Query. Machine learning will include training
 * location features with location labels. Ultimately, machine learning algo will receive
 * location features with a request for label which the ml algo will be responsible for
 * delivering.
 */

/**
 * Class with machine learning functions and operations.
 */
public class DP4_ml {

    //constants:
    private final int NUMBER_OF_SAMPLES = 100;
    private final int NUMBER_OF_FEATURES = 7;

    //object properties for running and testing algo:
    public List<LabeledFeatures> samples;

    public DP4_ml(){
        this.samples = new ArrayList<>();
        getInitialRandomizedData(this.samples);
    }


    /**
     * Uses gaussian probability distribution to generate randomized data for use with ml algo
     * @param samples matrix containing the labels and features to be used
     */
    private void getInitialRandomizedData(List<LabeledFeatures> samples){
        Random rval = new Random((new Date().getTime()));

        //populate samples list:
        for(int i=0;i<NUMBER_OF_SAMPLES;i++){
            samples.add(new LabeledFeatures(String.valueOf(i))); //for now
            for(int j=0;j<NUMBER_OF_FEATURES;j++){
                (samples.get(i)).features.add(rval.nextGaussian());
            }
        }
    }


    // implementation of Multinomial Logistic Regression with Least Squares Estimation for estimating feature weights
    // and ... for predictions/score determination

    /**
     * Function for estimating paramerters through least squares estimation
     * @param samples
     */
    private void leastSquaresEstimation(List<LabeledFeatures> samples){

    }

    /**
     * Function for obtaining prediction based on given input
     * @param input
     */
    public void prediction(LabeledFeatures input){

    }

}
