
import java.sql.*;
import java.lang.*;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JOptionPane;
import com.mysql.cj.jdbc.MysqlDataSource;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class RootUserServlet extends HttpServlet {

    private Connection connection;
    private Statement statement;

    String htmlTable = null;
    String sqlInput = null;

    public void init() throws ServletException {
        Properties properties = new Properties();
        FileInputStream filein = null;
        MysqlDataSource dataSource = null;
        try {
            filein = new FileInputStream("/Library/Tomcat1015/webapps/Project4/WEB-INF/lib/root.properties");
            properties.load(filein);
            dataSource = new MysqlDataSource();
            dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
            dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
            connection = dataSource.getConnection();
            statement = connection.createStatement();
        } catch (SQLException e) {
            //htmlTable = "sql Exception";
            e.printStackTrace();
        } catch (IOException e) {
            //htmlTable = "IO Exception";
            e.printStackTrace();
        }
    }

    private static String convertResultSetToHtmlTable(ResultSet resultSet) throws SQLException {
        // Get the metadata of the result set
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Start building the HTML table
        StringBuilder htmlTable = new StringBuilder("<table>\n<tr>");

        // Add the table headers
        for (int i = 1; i <= columnCount; i++) {
            htmlTable.append("<th>").append(metaData.getColumnLabel(i)).append("</th>");
        }
        htmlTable.append("</tr>\n");

        // Add the table rows
        while (resultSet.next()) {
            htmlTable.append("<tr>");
            for (int i = 1; i <= columnCount; i++) {
                htmlTable.append("<td>").append(resultSet.getString(i)).append("</td>");
            }
            htmlTable.append("</tr>\n");
        }

        // Finish building the HTML table
        htmlTable.append("</table>");
        return htmlTable.toString();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        sqlInput = request.getParameter("sqlInput");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        //init();
        try {

            String str = sqlInput.toLowerCase();
            String substring = "select";
            if (sqlInput != null && sqlInput != "") {
                if (str.contains(substring)) {
                    ResultSet resultSet = statement.executeQuery(sqlInput);
                    htmlTable = convertResultSetToHtmlTable(resultSet);
                } else if (!str.contains(substring)) {
                    PreparedStatement preparedStmt = connection.prepareStatement(sqlInput);
                        preparedStmt.executeUpdate();
                        int succUpdate = preparedStmt.getUpdateCount();

                    if (str.contains("shipments") && str.contains("insert")) {
                        String requiredString = sqlInput.substring(sqlInput.indexOf("(") + 1, sqlInput.indexOf(")"));
                        requiredString = requiredString.replaceAll("\\s", "");
                        String[] array = requiredString.split(",");
                        if(Integer.parseInt(array[3]) >= 100){
                            String update = "update suppliers set status = status + 5 where snum = " + array[0];
                         preparedStmt = connection.prepareStatement(update);
                        preparedStmt.executeUpdate();
                        int succcUpdate = preparedStmt.getUpdateCount();
                        htmlTable = "<center><h3 h3 style=\"color:rgb(0, 3, 0); background-color: rgb(3, 255, 11); padding: 10px;\">The statement executed succesfully. <br />" + succUpdate + " row(s) affected. <br /> Business Logic Detected! - Updating Supplier Status <br /> Business Logic Updated " + succcUpdate + " supplier status marks. </h3></center>";
                        }
                        else {
                            htmlTable = "<center><h3 h3 style=\"color:rgb(0, 3, 0); background-color: rgb(3, 255, 11); padding: 10px;\">The statement executed succesfully. <br />" + succUpdate + " row(s) affected. <br /> Business Logic Not Triggered!</h3></center>";
                        }
                    } else {
                        htmlTable = "<center><h3 h3 style=\"color:rgb(0, 3, 0); background-color: rgb(3, 255, 11); padding: 10px;\">The statement executed succesfully! <br />" + succUpdate + " row(s) affected. <br /> Business Logic Not Triggered!</h3></center>";
                    }
                }
            }

            String action = request.getParameter("action");
            if ("doSomething".equals(action)) {
                htmlTable = "";
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            htmlTable = "<center><h3 h3 style=\"color:rgb(252, 252, 252); background-color: rgb(255, 0, 0); padding: 10px;\">Error executing the SQL statement: <br />" + sqlException.getMessage() + "</h3></center>";
        }

        HttpSession session = request.getSession();
        session.setAttribute("htmlString", htmlTable);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/rootHome.jsp");
        dispatcher.forward(request, response);

    }
}
