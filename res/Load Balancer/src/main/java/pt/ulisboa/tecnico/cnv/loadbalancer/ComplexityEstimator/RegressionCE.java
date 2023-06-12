package pt.ulisboa.tecnico.cnv.loadbalancer.ComplexityEstimator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

public class RegressionCE {

    private OLSMultipleLinearRegression regModel = new OLSMultipleLinearRegression();
    private double[] regParameters;
    private List<Double> accComplexities = new ArrayList<>();
    private List<List<Double>> accFeatures = new ArrayList<>(); 
    
    public double[] getParameters() {
        return regParameters;
    }

    public void clearModelData() {
        accComplexities.clear();
        accFeatures.clear();
    }

    public void addDataToModel(Double complexity, List<Double> features) {
        accComplexities.add(complexity);
        accFeatures.add(features);
    }

    public void addDataToModel(List<Double> complexity, List<List<Double>> features) {
        accComplexities.addAll(complexity);
        accFeatures.addAll(features);
    }
        
    public void updateModelParameters(List<Double> complexities, List<List<Double>> features) {
        clearModelData();
        addDataToModel(complexities, features);
        updateParameters();
    }

    public void updateParameters() {

        System.out.println(accComplexities.toString());
        System.out.println(accFeatures.toString());

        regModel.newSampleData(
            accComplexities
                .stream()
                .mapToDouble(Double::doubleValue)
                .toArray(), 
            accFeatures
                .stream()
                .map(feature -> feature.stream().mapToDouble(Double::doubleValue).toArray())
                .toArray(double[][]::new)
        );

        regParameters = regModel.estimateRegressionParameters();
    }

    public Double estimateComplexity(double[] reqArgs) {
        double estimate = regParameters[0];
        for (int i = 1; i < regParameters.length; i++) {
            estimate += regParameters[i]*reqArgs[i-1];
        }
        return estimate;
    }

}
