// A simple servlet to process an HTTP get request.
// Main servlet in first-example web-app

// Users of Tomcat 10 onwards should be aware that, as a result of the move from Java EE to Jakarta EE as part of the
// transfer of Java EE to the Eclipse Foundation, the primary package for all implemented APIs has changed
// from javax.* to jakarta.*. This will almost certainly require code changes to enable applications to migrate
// from Tomcat 9 and earlier to Tomcat 10 and later. 

//import javax.servlet.*;   //used for Tomcat 9.x and earlier only
//import javax.servlet.http.*;  //used for Tomcat 9.x and earlier only
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.sql.*;
import java.lang.*;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JOptionPane;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class DeleteCityServlet extends HttpServlet {
    private Connection connection;
    private Statement statement;

    String htmlTable = null;
    String sqlInput = null;

    public void init() throws ServletException {
        // Properties properties = new Properties();
        // FileInputStream filein = null;
        SQLServerDataSource dataSource = null;
        try {
            dataSource = new SQLServerDataSource();
            dataSource.setURL(
                    "jdbc:sqlserver://weatherapp.database.windows.net:1433;database=WeatherAppDB;user=weatherapp@weatherapp;password=!!Tarpon778381!!;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
            dataSource.setUser("weatherapp");
            dataSource.setPassword("!!Tarpon778381!!");
            connection = dataSource.getConnection();
            statement = connection.createStatement();
        } catch (SQLException e) {
            // htmlTable = "sql Exception";
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("UserID");
        String cityName = request.getParameter("city");

        if (userId == null || cityName == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing user ID or city name.");
            return;
        }

        // Use try-with-resources to ensure that resources are closed
        try {
                PreparedStatement pstmt = connection.prepareStatement("DELETE FROM UserCities WHERE UserID = ? AND CityName = ?");

            pstmt.setInt(1, userId);
            pstmt.setString(2, cityName);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                response.setContentType("text/plain");
                response.getWriter().write("Deleted");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "City not found for the user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        }
    }
}