package com.Storm_AI.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class WeatherDataFetcher {
    private static final String API_URL = "https://api.weatherapi.com/v1/current.json?key=YOUR_API_KEY&q=LOCATION";

    public WeatherData fetchWeatherData() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(API_URL);
        CloseableHttpResponse response = httpClient.execute(request);

        try {
            String jsonResponse = EntityUtils.toString(response.getEntity());
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            
            WeatherData weatherData = new WeatherData();
            weatherData.setTemperature(jsonObject.get("current").getAsJsonObject().get("temp_c").getAsDouble());
            weatherData.setHumidity(jsonObject.get("current").getAsJsonObject().get("humidity").getAsDouble());
            weatherData.setPressure(jsonObject.get("current").getAsJsonObject().get("pressure_mb").getAsDouble());
            weatherData.setTimestamp(jsonObject.get("location").getAsJsonObject().get("localtime").getAsString());

            return weatherData;
        } finally {
            response.close();
        }
    }
}

