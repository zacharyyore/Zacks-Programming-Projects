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

public class DEUserServlet extends HttpServlet {

    private Connection connection;
    private Statement statement;

    String htmlTable = null;
    String sqlInput = null;

    public void init() throws ServletException {
        Properties properties = new Properties();
        FileInputStream filein = null;
        MysqlDataSource dataSource = null;
        try {
            filein = new FileInputStream("/Library/Tomcat1015/webapps/Project4/WEB-INF/lib/dataentryuser.properties");
            properties.load(filein);
            dataSource = new MysqlDataSource();
            dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
            dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
            connection = dataSource.getConnection();
            statement = connection.createStatement();
        } catch (SQLException e) {
            htmlTable = "sql Exception";
            e.printStackTrace();
        } catch (IOException e) {
            htmlTable = "IO Exception";
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        sqlInput = request.getParameter("sqlInput");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        //init();
        try {
            String snum = request.getParameter("snum");
            String sname = request.getParameter("sname");
            String status = request.getParameter("status");
            String Scity = request.getParameter("Scity");

            String pnum = request.getParameter("pnum");
            String pname = request.getParameter("pname");
            String color = request.getParameter("color");
            String weight = request.getParameter("weight");
            String Pcity = request.getParameter("Pcity");


            String jnum = request.getParameter("jnum");
            String jname = request.getParameter("jname");
            String numworkers = request.getParameter("numworkers");
            String Jcity = request.getParameter("Jcity");

            String snumS = request.getParameter("snumS");
            String pnumS = request.getParameter("pnumS");
            String jnumS = request.getParameter("jnumS");
            String quantity = request.getParameter("quantity");


            if (snum != null && snum != "") {
                String snumBut = request.getParameter("snumBut");
                if ("doSomethingS".equals(snumBut)) {
                    PreparedStatement preparedStmt = connection.prepareStatement("insert into suppliers values(\"" + snum + "\",\"" + sname + "\"," + status + ",\"" + Scity + "\");");
                    preparedStmt.executeUpdate();
                    htmlTable = "<center><h3 h3 style=\"color:rgb(0, 3, 0); background-color: rgb(3, 255, 11); padding: 10px;\"> New suppliers records: (" + snum + ", " + sname + ", " + status + ", " + Scity + ") - successfully entered into database. </h3></center>";
                }
            }


            if (pnum != null && pnum != "") {
                String pnumBut = request.getParameter("pnumBut");
                if ("doSomethingP".equals(pnumBut)) {
                    PreparedStatement preparedStmt = connection.prepareStatement("insert into parts values(\"" + pnum + "\",\"" + pname + "\",\"" + color + "\"," + weight + ",\"" + Pcity + "\");");
                    preparedStmt.executeUpdate();
                    htmlTable = "<center><h3 h3 style=\"color:rgb(0, 3, 0); background-color: rgb(3, 255, 11); padding: 10px;\"> New parts records: (" + pnum + ", " + pname + ", " + color + ", " + weight + ", " + Pcity + ") - successfully entered into database. </h3></center>";
                }
            }

            if (jnum != null && jnum != "") {
                String jnumBut = request.getParameter("jnumBut");
                if ("doSomethingJ".equals(jnumBut)) {
                    PreparedStatement preparedStmt = connection.prepareStatement("insert into jobs values(\"" + jnum + "\",\"" + jname + "\"," + numworkers + ",\"" + Jcity + "\");");
                    preparedStmt.executeUpdate();
                    htmlTable = "<center><h3 h3 style=\"color:rgb(0, 3, 0); background-color: rgb(3, 255, 11); padding: 10px;\"> New jobs records: (" + jnum + ", " + jname + ", " + numworkers + ", " + Jcity + ") - successfully entered into database. </h3></center>";
                }
            }

            if (snumS != null && snumS != "") {
                String qnumBut = request.getParameter("qnumBut");
                if ("doSomethingQ".equals(qnumBut)) {
                    PreparedStatement preparedStmt = connection.prepareStatement("insert into shipments values(\"" + snumS + "\",\"" + pnumS + "\",\"" + jnumS + "\"," + quantity + ");");
                    preparedStmt.executeUpdate();
                    if(Integer.parseInt(quantity) >= 100){
                        String update = "update suppliers set status = status + 5 where snum = " + "\"" + snumS + "\"";
                        preparedStmt = connection.prepareStatement(update);
                       preparedStmt.executeUpdate();
                       htmlTable = "<center><h3 h3 style=\"color:rgb(0, 3, 0); background-color: rgb(3, 255, 11); padding: 10px;\"> New shipments records: (" + snumS + ", " + pnumS + ", " + jnumS + ", " + quantity + ") - successfully entered into database. Business logic triggered.</h3></center>";
                    }
                    else{
                    htmlTable = "<center><h3 h3 style=\"color:rgb(0, 3, 0); background-color: rgb(3, 255, 11); padding: 10px;\"> New shipments records: (" + snumS + ", " + pnumS + ", " + jnumS + ", " + quantity + ") - successfully entered into database. Business logic not triggered. </h3></center>";
                    }
                }
            }


        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
             htmlTable = "<center><h3 h3 style=\"color:rgb(252, 252, 252); background-color: rgb(255, 0, 0); padding: 10px;\">Error executing the SQL statement: <br />" + sqlException.getMessage() + "</h3></center>";
        }

        HttpSession session = request.getSession();
        session.setAttribute("htmlString", htmlTable);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/dataEntryHome.jsp");
        dispatcher.forward(request, response);

    }
}
