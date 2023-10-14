<!DOCTYPE html>
<html>

<head>
  <meta charset="UTF-8">
  <title> Login Page </title>
  <style>
    Body {
      font-family: Calibri, Helvetica, sans-serif;
      background-color: rgb(0, 6, 0);
    }

    button {
      background-color: #4CAF50;

      color: rgb(92, 217, 24);
      height: 20px;
      margin: 10px 0px;
      border: none;
      cursor: pointer;
    }

    form {
      border: 3px solid #fefbfb;
      border-right-style: hidden;
      border-left-style: hidden;
    }

    textarea[type=text] {
      width: 85%;

      margin: 8px 0;
      padding: 12px 20px;
      display: inline-block;
      box-sizing: border-box;
      background-color: #0904f9;
      font-weight: bolder;
      padding: 0 0 200px 0;
    }

    button:hover {
      opacity: 0.7;
    }

    label {
      color: #fcfdf9;
    }

    table, th, td {
      border: 1px solid rgb(225, 237, 48);
      color: #fefbfb;
    }

    .container {
      width: 50%;
      padding: 5%;
      background-color: rgb(0, 0, 0);
      padding-top: 1%;
      padding-bottom: 1%;

    }
  </style>
</head>

<body>
  <center>
    <h1 h1 style="color:rgb(229, 255, 0);"> Welcome to the Spring 2023 Project 4 Enterprise System </h1>
  </center>
  <center>
    <h2 h2 style="color:rgb(0, 255, 60);"> A Servlet/JSP-based Multi-tiered Enterprise Application Using A Tomcat
      Containter </h2>
  </center>
  <form action="/Project4/RootUserServlet" method="get">
    <center>
      <div class="container">
        <label>You are connected to the Project 4 Enterprise System database as a root-level user. </label>
        <br />
        <label>Please enter any valid SQL query or update command in the box below. </label>
        <br />
        <br />
        <textarea rows = "5" cols = "50" style="color: #fefbfb;" type="text" placeholder="select * from suppliers" name="sqlInput" id="sqlInput"></textarea>
        <br />
        <button type="submit" style="background-color:rgb(23, 93, 34); color:rgb(58, 235, 82);">Execute Command</button>
        <button type="reset" style="background-color:rgb(67, 11, 11); color:rgb(240, 111, 111);">Reset Form</button>
        <button type="submit" name="action" value="doSomething" style="background-color:rgb(109, 118, 39);color:rgb(185, 249, 9);">Clear Results</button>
        <br />
        <br />
        <label>All execution results will appear below this line. </label>
      </div>
    </center>
  </form>
  <br />
  <center><label>Database Results: </label> </center>
  <br />
  <center><div>
   
   <p> <%= session.getAttribute("htmlString") %></p>
   
  </div></center>
</body>

</html>