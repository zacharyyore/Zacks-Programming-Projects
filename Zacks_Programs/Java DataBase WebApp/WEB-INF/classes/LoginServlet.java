// A simple servlet to process an HTTP get request.
// Main servlet in first-example web-app

// Users of Tomcat 10 onwards should be aware that, as a result of the move from Java EE to Jakarta EE as part of the
// transfer of Java EE to the Eclipse Foundation, the primary package for all implemented APIs has changed
// from javax.* to jakarta.*. This will almost certainly require code changes to enable applications to migrate
// from Tomcat 9 and earlier to Tomcat 10 and later. 

//import javax.servlet.*;   //used for Tomcat 9.x and earlier only
//import javax.servlet.http.*;  //used for Tomcat 9.x and earlier only
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {   
   // process "get" requests from clients
   public String[] parts;

   public LoginServlet(){
   parts = new String[2];
   }

   public boolean readUsersFromFile(String username, String password) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader("/Library/Tomcat1015/webapps/Project4/WEB-INF/lib/credentials.txt"))) {
        String line;
        String pass = null;
        int x = 0;
        while ((line = br.readLine()) != null) {
            parts = line.split(", ");
            if (parts.length == 2) {
                String Username = parts[0].trim();
                String Password = parts[1].trim();
                if (Username.equals(username) && Password.equals(password)) {
                    return true;
                }
            }

        }
    }
    return false;
}


   @Override
protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException  {


   String user = request.getParameter("username");
        String pass = request.getParameter("password");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (readUsersFromFile(user, pass)) {
            if(user.equals("root")){
                response.sendRedirect("rootHome.jsp");
            }
            if(user.equals("client")){
                response.sendRedirect("clientHome.jsp");
            }
            if(user.equals("dataentryuser")){
                response.sendRedirect("dataEntryHome.jsp");
            }
        } else {
         response.sendRedirect("LoginError.html");
        }
    
      
   } //end doGet() method
   
} //end WelcomeServlet class
