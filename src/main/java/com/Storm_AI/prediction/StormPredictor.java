package com.Storm_AI.prediction;

import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class StormPredictor {
    private Classifier classifier;
    private Instances dataSet;

    public StormPredictor() {
        // Initialize the classifier
        classifier = new RandomForest();

        // Define attributes
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("temperature"));
        attributes.add(new Attribute("humidity"));
        attributes.add(new Attribute("pressure"));

        ArrayList<String> classValues = new ArrayList<>();
        classValues.add("NoStorm");
        classValues.add("Storm");
        attributes.add(new Attribute("class", classValues));

        // Create an empty dataset with the defined attributes
        dataSet = new Instances("StormPredictionDataSet", attributes, 0);
        dataSet.setClassIndex(dataSet.numAttributes() - 1);
    }

    public void trainModel(String trainingDataFilePath) throws Exception {
        // Load training data
        Instances trainingData = new Instances(new FileReader(new File(trainingDataFilePath)));
        trainingData.setClassIndex(trainingData.numAttributes() - 1);

        // Train the classifier
        classifier.buildClassifier(trainingData);
    }

    public String predict(double temperature, double humidity, double pressure) throws Exception {
        // Create an instance with the given data
        double[] values = new double[dataSet.numAttributes()];
        values[0] = temperature;
        values[1] = humidity;
        values[2] = pressure;
        DenseInstance instance = new DenseInstance(1.0, values);
        instance.setDataset(dataSet);

        // Classify the instance
        double result = classifier.classifyInstance(instance);
        return dataSet.classAttribute().value((int) result);
    }
}

