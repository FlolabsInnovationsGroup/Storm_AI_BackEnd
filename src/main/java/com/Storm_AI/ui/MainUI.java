package com.Storm_AI.ui;

import com.Storm_AI.ai.ChatBot;
import com.Storm_AI.data.WeatherData;
import com.Storm_AI.data.WeatherDataFetcher;
import com.Storm_AI.processing.DataProcessor;
import com.Storm_AI.prediction.StormPredictor;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import weka.core.Instances;

import java.io.IOException;

public class MainUI extends Application {

    private Label temperatureLabel;
    private Label humidityLabel;
    private Label pressureLabel;
    private Label predictionLabel;
    private TextArea chatArea;
    private TextField userInputField;
    private ChatBot chatBot;
    private StormPredictor stormPredictor;

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Storm AI");

        temperatureLabel = new Label("Temperature: Loading...");
        humidityLabel = new Label("Humidity: Loading...");
        pressureLabel = new Label("Pressure: Loading...");
        predictionLabel = new Label("Storm Prediction: Loading...");

        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setPrefHeight(200);

        userInputField = new TextField();
        userInputField.setPromptText("Ask a question...");
        userInputField.setOnAction(event -> handleUserInput());

        VBox vbox = new VBox(10, temperatureLabel, humidityLabel, pressureLabel, predictionLabel, chatArea, userInputField);
        Scene scene = new Scene(vbox, 400, 400);

        primaryStage.setScene(scene);
        primaryStage.show();

        chatBot = new ChatBot();
        stormPredictor = new StormPredictor();
        fetchWeatherData();
    }

    private void handleUserInput() {
        String userInput = userInputField.getText();
        if (userInput.isEmpty()) {
            return;
        }

        chatArea.appendText("You: " + userInput + "\n");
        userInputField.clear();

        new Thread(() -> {
            try {
                String response = chatBot.getResponse(userInput);
                chatArea.appendText("AI: " + response + "\n");
            } catch (IOException e) {
                e.printStackTrace();
                chatArea.appendText("AI: Error fetching response\n");
            }
        }).start();
    }

    private void fetchWeatherData() throws IOException {
        new Thread(() -> {
            try {
                WeatherDataFetcher fetcher = new WeatherDataFetcher();
                WeatherData data = fetcher.fetchWeatherData();

                // Process the data
                DataProcessor processor = new DataProcessor();
                Instances processedData = processor.preprocessWeatherData(data);

                // Train the model (for demonstration, using the same data)
                stormPredictor.trainModel("path/to/training/data.arff");

                // Make a prediction
                String prediction = stormPredictor.predict(data.getTemperature(), data.getHumidity(), data.getPressure());

                updateUI(data, prediction);
                System.out.print(processedData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void updateUI(WeatherData data, String prediction) {
        javafx.application.Platform.runLater(() -> {
            temperatureLabel.setText("Temperature: " + data.getTemperature() + "°C");
            humidityLabel.setText("Humidity: " + data.getHumidity() + "%");
            pressureLabel.setText("Pressure: " + data.getPressure() + " mb");
            predictionLabel.setText("Storm Prediction: " + prediction);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
