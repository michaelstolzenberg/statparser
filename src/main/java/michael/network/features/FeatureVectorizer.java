package michael.network.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FeatureVectorizer {
    public final Map<Feature, Integer> featureMapping;
    public List embeddings;
    
    public FeatureVectorizer() {
        featureMapping = new HashMap<>();
        this.embeddings = null;
    }

    /**
     * Vectorize a set of feature-value pairs.
     *
     * @param features       Feature-value pairs.
     * @param addNewFeatures Add unseen features. This should be done during training, but not during use of the model.
     * @return The feature vector.
     */
    public List<Integer> vectorize(Set<FeatureValue> features, boolean addNewFeatures) {
        
        // Pass 1: add new features to the mapping.
        if (addNewFeatures) {
            for (FeatureValue featureValue : features) {
                if(!featureValue.feature.value.layer.equals(AddressedValue.Layer.TOKEN)){
                 
                    Integer featureIdx = featureMapping.get(featureValue.feature);
                    if (featureIdx == null) {
                        featureIdx = featureMapping.size() + 1;
                        //System.out.println(featureValue.feature.values+" "+featureIdx);
                        featureMapping.put(featureValue.feature, featureIdx);
                    }
                }
            }
        }
        
        // Pass 2: initialize the vector.
        List<Integer> featureVector = new ArrayList<>();
        embeddings = new ArrayList();
        for (FeatureValue featureValue : features) {
            if(featureValue.feature.value.layer.equals(AddressedValue.Layer.TOKEN)){
                    embeddings.add(featureValue.feature.value.value);
            }else{
                Integer featureIdx = featureMapping.get(featureValue.feature);
            
                if (featureIdx != null) {
                    int n = featureIdx;
                    featureVector.add(n);
                }
            }
        }
        return featureVector;
    }

    public int nFeatures() {
        return featureMapping.size();
    }
}
