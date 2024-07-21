package com.Storm_AI.processing;

import com.Storm_AI.data.WeatherData;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.filters.unsupervised.attribute.Normalize;

import java.util.ArrayList;

public class DataProcessor {

    public Instances preprocessWeatherData(WeatherData weatherData) {
        // Define attributes
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("temperature"));
        attributes.add(new Attribute("humidity"));
        attributes.add(new Attribute("pressure"));

        // Create an empty dataset with the defined attributes
        Instances dataSet = new Instances("WeatherDataSet", attributes, 0);

        // Create an instance and add it to the dataset
        double[] values = new double[dataSet.numAttributes()];
        values[0] = weatherData.getTemperature();
        values[1] = weatherData.getHumidity();
        values[2] = weatherData.getPressure();
        dataSet.add(new DenseInstance(1.0, values));

        // Handle missing values
        ReplaceMissingValues replaceMissingValues = new ReplaceMissingValues();
        try {
            replaceMissingValues.setInputFormat(dataSet);
            dataSet = Filter.useFilter(dataSet, replaceMissingValues);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Normalize the dataset
        Normalize normalize = new Normalize();
        try {
            normalize.setInputFormat(dataSet);
            dataSet = Filter.useFilter(dataSet, normalize);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataSet;
    }
}

