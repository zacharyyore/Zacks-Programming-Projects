package com.myweatherapp;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;

public class WeatherDataServlet extends HttpServlet {
    private static final String API_KEY = "cc8d58c7601141f2a7c30245242301";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String cityName = request.getParameter("city");
        if (cityName == null || cityName.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "City name is required");
            return;
        }

        List<WeatherData> weatherDataList = getWeatherForecast(cityName);
        request.setAttribute("weatherData", weatherDataList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("weatherPage.jsp");
        dispatcher.forward(request, response);
    }

    private List<WeatherData> getWeatherForecast(String cityName) throws IOException {
        String url = "http://api.weatherapi.com/v1/forecast.json?key=" + API_KEY + "&q=" + cityName + "&days=3";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        return parseWeatherData(jsonResponse);
    }

    private List<WeatherData> parseWeatherData(JSONObject jsonResponse) {
        JSONArray forecastDays = jsonResponse.getJSONObject("forecast").getJSONArray("forecastday");
        List<WeatherData> weatherDataList = new ArrayList<>();

        for (int i = 0; i < forecastDays.length(); i++) {
            JSONObject dayForecast = forecastDays.getJSONObject(i).getJSONObject("day");
            WeatherData data = new WeatherData();
            data.date = forecastDays.getJSONObject(i).getString("date");
            data.maxTemp = dayForecast.getDouble("maxtemp_f");
            data.minTemp = dayForecast.getDouble("mintemp_f");
            data.chanceOfRain = dayForecast.getInt("daily_chance_of_rain");
            weatherDataList.add(data);
        }

        return weatherDataList;
    }

    class WeatherData {
        private String date;
        private double maxTemp;
        private double minTemp;
        private int chanceOfRain;
    
        // Constructor
        public WeatherData() {
        }
    
        // Getters
        public String getDate() {
            return date;
        }
    
        public double getMaxTemp() {
            return maxTemp;
        }
    
        public double getMinTemp() {
            return minTemp;
        }
    
        public int getChanceOfRain() {
            return chanceOfRain;
        }
    
        // Setters
        public void setDate(String date) {
            this.date = date;
        }
    
        public void setMaxTemp(double maxTemp) {
            this.maxTemp = maxTemp;
        }
    
        public void setMinTemp(double minTemp) {
            this.minTemp = minTemp;
        }
    
        public void setChanceOfRain(int chanceOfRain) {
            this.chanceOfRain = chanceOfRain;
        }
    
        // Optional: Override toString method for easy printing/debugging
        @Override
        public String toString() {
            return "WeatherData{" +
                    "date='" + date + '\'' +
                    ", maxTemp=" + maxTemp +
                    ", minTemp=" + minTemp +
                    ", chanceOfRain=" + chanceOfRain +
                    '}';
        }
    }
}    