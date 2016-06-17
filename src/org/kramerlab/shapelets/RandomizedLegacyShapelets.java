/**
 * 
 */
package org.kramerlab.shapelets;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Random;

import org.kramerlab.timeseries.TimeSeries;
import org.kramerlab.timeseries.TimeSeriesDataset;

/**
 * @author razaa
 *
 */
public class RandomizedLegacyShapelets extends LegacyShapelets {
    
    protected static Random rand;
    protected static double percentage;
    
    public RandomizedLegacyShapelets(TimeSeriesDataset trainSet, int minLen, int maxLen, int stepSize) {
        super(trainSet, minLen, maxLen, stepSize);
        try {
            Properties props = new Properties();
            File propsFile = new File(System.getProperty("rs-props", "rs.properties"));
            if (propsFile.exists()) {
                props.load(new FileInputStream(propsFile));
            }
            percentage = Double.parseDouble(props.getProperty("selection_ratio", "10")) / 100;
            if (props.containsKey("rand_seed")) {
                int seed = Integer.parseInt(props.getProperty("rand_seed", "0"));
                rand = new Random(seed);
            } else {
                rand = new Random();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected TimeSeries getNextCandidate() {
        while ((rand.nextFloat() > percentage) && this.hasMoreCandidates) {
            this.incrementCandidatePosition();
        }
        TimeSeries candidate = super.getNextCandidate();
        return candidate;
    }
    
    public double getSamplingPercentage() {
        return percentage;
    }
}
