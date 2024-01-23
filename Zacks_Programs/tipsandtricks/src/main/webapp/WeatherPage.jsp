<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.io.*, java.net.*, org.json.*, java.sql.*, java.util.*" %>
    <%@ page import="java.text.SimpleDateFormat" %>

<!DOCTYPE html>
<html>
<head>
    <title>Weather App</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: url('https://source.unsplash.com/1600x900/?nature,weather') no-repeat center center fixed;
            background-size: cover;
            margin: 0;
            padding: 0;
            
            display: flex;
            align-items: center;
            justify-content: center;
            height: auto; /* Changed from 100vh to auto */
    min-height: 100vh; /* Add this to ensure the full height is covered, but allows scrolling */
        }

        .container {
    background-color: rgba(255, 255, 255, 0.8);
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0px 0px 10px 0px #000;
    width: 60%; /* Adjusted width to 60% of the screen width */
    max-width: 800px; /* Maximum width set to 800px */
    display: flex;
    flex-direction: column;
    align-items: center;
    max-height: 90vh; /* This will ensure the container does not exceed the viewport height */
    overflow-y: auto;
}


        h2 {
            text-align: center;
            color: #333;
        }

        .form-group {
            margin-bottom: 15px;
            width: 100%;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
        }

        .form-group input, .form-group button {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            box-sizing: border-box;
            margin-bottom: 10px;
        }

        button {
            background-color: #007bff;
            color: white;
            cursor: pointer;
        }

        button:hover {
            background-color: #0056b3;
        }

        .city-box {
        /* existing styles... */
        flex-direction: column; /* Change the flex direction */
        align-items: flex-start; /* Align items to start */
        justify-content: center; /* Centers the content vertically */
    margin-bottom: 1em; /* Adds some space between the city boxes */
    width: 100%;
    text-align: center; /* Center align items */
    box-shadow: 0 4px 8px rgba(0,0,0,0.1); /* Add shadow for depth */
    background: linear-gradient(to bottom, #ffffff, #f1f1f1); /* Cool gradient background */
    padding: 20px;
    margin-bottom: 20px;
    border-radius: 8px; /* Rounded corners for the box */
}

    .city-box span {
        /* Styling for city name */
        font-size: 1.2em; /* Larger text */
        margin-bottom: 0.5em; /* Space between city name and button */
        display: block; /* Ensure the city name is centered */
    font-weight: bold;
    }

    .city-box form {
        width: 100%; /* Make form full width */
    }

    .city-box button {
        width: 100%; /* Make button full width */
        padding: 10px 0; /* Padding for button */
        border-radius: 0 0 5px 5px; /* Round only the bottom corners */
        border-top: 1px solid #ddd; /* Add border-top to button */
    }

    #city-list {
        width: 100%;
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(250px, 1fr)); /* Create a grid layout for cards */
        gap: 1em; /* Space between cards */
        align-items: start;
        grid-template-columns: repeat(3, 1fr); /* This will make sure there are three columns */
    justify-content: center;
    }

    /* Add styles for detaching the image from the city cards */
    .background-image {
        position: absolute; /* Absolute position */
        top: 0;
        left: 0;
        width: 100%;
        height: 100vh;
        z-index: -1; /* Ensure it stays in the background */
    }
    .forecast-day {
    text-align: center; 
    background-color: #f9f9f9; /* Slightly different background for contrast */
    margin: 10px 0; /* Space out forecast days */
    padding: 10px;
    border-radius: 8px; /* Rounded corners for the day forecast */
    box-shadow: 0 2px 4px rgba(0,0,0,0.1); /* Subtle shadow for the day forecast */
/* Center the text inside each forecast day */
}
    </style>
</head>
<body>
<div class="background-image" style="background: url('https://source.unsplash.com/1600x900/?nature,weather') no-repeat center center fixed;"></div>
    <div class="container">
        <h2>Weather Search</h2>
        <form action="/WeatherServlet" method="post">
           <div class="form-group">
                <label for="city">City:</label>
                <input type="text" id="city" name="city" required>
            </div>
            <div class="form-group">
                <button type="submit">Search</button>
            </div>
        </form>

        <!-- Database query to fetch cities for the current user -->
        <% 
            HttpSession User_Session = request.getSession();
            Integer userId = (Integer) User_Session.getAttribute("UserID");
            List<String> cities = new ArrayList<>();
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                // Initialize database connection
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                conn = DriverManager.getConnection("jdbc:sqlserver://weatherapp.database.windows.net:1433;database=WeatherAppDB;user=weatherapp@weatherapp;password=!!Tarpon778381!!;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
                String sql = "SELECT CityName FROM UserCities WHERE UserID = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, userId);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    cities.add(rs.getString("CityName"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try { if (rs != null) rs.close(); } catch (Exception e) {};
                try { if (pstmt != null) pstmt.close(); } catch (Exception e) {};
                try { if (conn != null) conn.close(); } catch (Exception e) {};
            }
        %>

        <!-- Displaying cities with delete option -->
        <div id="city-list">
    <% 
        for (String city : cities) {
            // Fetch weather data for each city
            try {
                String apiKey = "cc8d58c7601141f2a7c30245242301";
                String urlString = "http://api.weatherapi.com/v1/forecast.json?key=" + apiKey + "&q=" + city + "&days=3&aqi=no&alerts=no";
                URL url = new URL(urlString);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer res = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    res.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(res.toString());
                // Process and display the weather data for this city
                JSONArray forecastDays = jsonResponse.getJSONObject("forecast").getJSONArray("forecastday");

                // Display city name and delete button
                %>
                <div class="city-box">
                    <span><%= city %></span>
                    
                <% 
                    // Display weather data for the city
                    for (int i = 0; i < forecastDays.length(); i++) {
                    	JSONObject day = forecastDays.getJSONObject(i).getJSONObject("day");
                    	String dateStr = forecastDays.getJSONObject(i).getString("date"); // "2024-01-23"
                        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat targetFormat = new SimpleDateFormat("EEEE"); // "EEEE" for the full name of the day
                        java.util.Date date = originalFormat.parse(dateStr); // Use fully qualified class name here
                        String formattedDay = targetFormat.format(date);
                        %>
                        <div class="forecast-day">
                            <h3><%= formattedDay %></h3>
                            <p>Max Temp: <%= day.getDouble("maxtemp_f") %>°F</p>
                            <p>Min Temp: <%= day.getDouble("mintemp_f") %>°F</p>
                            <p>Chance of Rain: <%= day.getInt("daily_chance_of_rain") %>%</p>
                        </div>
                        <%
                    }
                %>
                <form action="/DeleteCityServlet" method="post">
                        <input type="hidden" name="city" value="<%= city %>">
                        <button type="button" onclick="deleteCity('<%= city %>', this.parentElement)">Delete</button>
                    </form>
                </div>
                <%
            } catch (Exception e) {
                out.println("<p>Error fetching weather for " + city + ": " + e.getMessage() + "</p>");
            }
        }
    %>
</div>
    
<div id="weather-info">
        <!-- Placeholder for displaying weather information -->
        <% 
    // This block should only execute if a city search has been performed
    String searchCity = request.getParameter("city");
    if(searchCity != null && !searchCity.isEmpty()) {
        try {
            String apiKey = "cc8d58c7601141f2a7c30245242301"; // Replace with your actual API key
            String urlString = "http://api.weatherapi.com/v1/forecast.json?key=" + apiKey + "&q=" + searchCity + "&days=3&aqi=no&alerts=no";
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer responseBuffer = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                responseBuffer.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(responseBuffer.toString());
            JSONArray forecastDays = jsonResponse.getJSONObject("forecast").getJSONArray("forecastday");

            // Only create a city-box for the searched city
            %>
            <div class="city-box">
                <span><%= searchCity %></span>
                <% 
                    for (int i = 0; i < forecastDays.length(); i++) {
                        JSONObject day = forecastDays.getJSONObject(i).getJSONObject("day");
                        // Display weather data for the searched city
                        %>
                        <div class="forecast-day">
                            <h3><%= forecastDays.getJSONObject(i).getString("date") %></h3>
                            <p>Max Temp: <%= day.getDouble("maxtemp_f") %>°F</p>
                            <p>Min Temp: <%= day.getDouble("mintemp_f") %>°F</p>
                            <p>Chance of Rain: <%= day.getInt("daily_chance_of_rain") %>%</p>
                        </div>
                        <%
                    }
                %>
                <!-- Add a delete button for the searched city -->
                <form action="/DeleteCityServlet" method="post">
                    <input type="hidden" name="city" value="<%= searchCity %>">
                    <button type="button" onclick="deleteCity('<%= searchCity %>', this.parentElement)">Delete</button>
                </form>
            </div>
            <%
        } catch (Exception e) {
            out.println("<p>Error fetching weather for " + searchCity + ": " + e.getMessage() + "</p>");
        }
    }
%>

    </div>
    </div>

    
    <script>
    function deleteCity(cityName, formElement) {
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/DeleteCityServlet", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                // Assuming the server sends back a success response
                // Remove the city element from the list
                formElement.closest('.city-box').remove();
            } else if (this.readyState == 4) {
                // Handle error, you can check this.status for the error code
                console.error('An error occurred:', this.statusText);
            }
        };
        xhr.send("city=" + encodeURIComponent(cityName));
    }
</script>

    
</body>
</html>