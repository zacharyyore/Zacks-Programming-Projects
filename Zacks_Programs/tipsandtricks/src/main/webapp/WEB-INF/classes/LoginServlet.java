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

public class LoginServlet extends HttpServlet {
    private Connection connection;
    private Statement statement;

    String htmlTable = null;
    String sqlInput = null;

    public void init() throws ServletException {
        //Properties properties = new Properties();
        //FileInputStream filein = null;
        SQLServerDataSource dataSource = null;
        try {
            dataSource = new SQLServerDataSource();
            dataSource.setURL("jdbc:sqlserver://weatherapp.database.windows.net:1433;database=WeatherAppDB;user=weatherapp@weatherapp;password=!!Tarpon778381!!;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
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

        String user = request.getParameter("username");
        String prepared = "SELECT Password FROM Users WHERE Username = ?";
        String pass = request.getParameter("password");
        String FindUserID = "SELECT UserID FROM Users WHERE Username = ?";
        response.setContentType("text/html");
        int userID = -1;
        try {
            PreparedStatement statement = connection.prepareStatement(prepared);
            statement.setString(1, user);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("Password");
                if (storedPassword.equals(pass)) {
                    statement = connection.prepareStatement(FindUserID);
                    statement.setString(1, user);
                    resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        userID = resultSet.getInt("UserID");
                    }
                    HttpSession session = request.getSession();
                    session.setAttribute("UserID", userID);
                    response.sendRedirect(request.getContextPath() + "/WeatherPage.jsp");
                } else {
                    System.out.println("passwords did not match");
                }
            } else {
                System.out.println("did not work");
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        /*if (pass) {
            response.sendRedirect(request.getContextPath() + "/WeatherPage.jsp");
        } else {
            // Handle login failure
        }
        */
    }
}