import java.util.ArrayList;
import java.util.List;

/**
 * Labeled Feature vector object: object with properties label and List of Features
 */
public class LabeledFeatures {
    public String label; //for now just an int
    public List<Double> features;

    public LabeledFeatures(String label, List<Double> features){
        this.label = label;
        this.features = new ArrayList<>();
        for(double element : features){
            this.features.add(element);
        }
    }

    public LabeledFeatures(String label){
        this.label = label;
        this.features = new ArrayList<>();
    }
}
