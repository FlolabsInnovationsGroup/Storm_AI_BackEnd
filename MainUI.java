package main.java.com.Storm_AI.ui;

import com.example.data.WeatherData;
import com.example.data.WeatherDataFetcher;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainUI extends Application {

    private Label temperatureLabel;
    private Label humidityLabel;
    private Label pressureLabel;
    private Label predictionLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Storm AI");

        temperatureLabel = new Label("Temperature: Loading...");
        humidityLabel = new Label("Humidity: Loading...");
        pressureLabel = new Label("Pressure: Loading...");
        predictionLabel = new Label("Storm Prediction: Loading...");

        VBox vbox = new VBox(10, temperatureLabel, humidityLabel, pressureLabel, predictionLabel);
        Scene scene = new Scene(vbox, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.show();

        fetchWeatherData();
    }

    private void fetchWeatherData() {
        new Thread(() -> {
            try {
                WeatherDataFetcher fetcher = new WeatherDataFetcher();
                WeatherData data = fetcher.fetchWeatherData();

                // Simulate a prediction for demonstration
                String prediction = "No storm expected";

                updateUI(data, prediction);
            } catch (IOException e) {
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

