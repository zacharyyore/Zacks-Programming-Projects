<!DOCTYPE html>
<html>

<head>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title> Login Page </title>
  <style>
    Body {
      font-family: Calibri, Helvetica, sans-serif;
      background-color: rgb(0, 0, 0);
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

    input[type=text] {


      margin: 8px 0;

      display: inline-block;
      box-sizing: border-box;
      background-color: #deaf40;
      font-weight: bolder;

    }

    button:hover {
      opacity: 0.7;
    }

    label {
      color: #fcfdf9;
    }


    .container {
      width: 50%;
      padding: 5%;
      background-color: rgb(0, 0, 0);
      padding-top: 1%;
      padding-bottom: 1%;

    }

    table,
    th,
    td {
      border: 1px solid rgb(225, 237, 48);
      color: #fefbfb;
    }
  </style>
</head>

<body>
  <center>
    <h1 h1 style="color:rgb(255, 0, 0);"> Welcome to the Spring 2023 Project 4 Enterprise System </h1>
  </center>
  <center>
    <h2 h2 style="color:rgb(0, 242, 255);"> Data Entry Application </h2>
  </center>
  <form action="/Project4/LoginServlet" method="get">
    <center>
      <div class="container">
        <label>You are connected to the Project 4 Enterprise System database as a data-entry-level user. </label>
        <br />
        <label>Enter the data values in a form below to add a new record to the corresponding database label. </label>
  </form>
  </center>
  <br />
  <form action="/Project4/DEUserServlet" method="get">
    <label>Supplier Record Insert</label>
    <br />
    <center>
      <table style="width:50%; border-color:rgb(185, 249, 9);">
        <tr>
          <th>snum</th>
          <th>sname</th>
          <th>status</th>
          <th>city</th>
        </tr>
        <tr>
          <td>
            <center><input type="text" id="snum" name="snum"></center>
          </td>
          <td>
            <center><input type="text" id="sname" name="sname"></center>
          </td>
          <td>
            <center><input type="text" id="status" name="status"></center>
          </td>
          <td>
            <center><input type="text" id="city" name="Scity"></center>
          </td>

        </tr>
      </table>
    </center>
    <center><button type="submit" name="snumBut" value="doSomethingS" style="background-color:rgb(23, 93, 34); color:rgb(58, 235, 82);">Enter Supplier
        Record Into Database</button>
      <button type="reset" style="background-color:rgb(67, 11, 11); color:rgb(240, 111, 111);">Clear Data and
        Results</button>
    </center>

  </form>
  <br />
  <form action="/Project4/DEUserServlet" method="get">
    <label>Parts Record Insert</label>
    <br />
    <center>
      <table style="width:50%; border-color:rgb(185, 249, 9);">
        <tr>
          <th>pnum</th>
          <th>pname</th>
          <th>color</th>
          <th>weight</th>
          <th>city</th>
        </tr>
        <tr>
          <td>
            <center><input type="text" id="pnum" name="pnum"></center>
          </td>
          <td>
            <center><input type="text" id="pname" name="pname"></center>
          </td>
          <td>
            <center><input type="text" id="color" name="color"></center>
          </td>
          <td>
            <center><input type="text" id="weight" name="weight"></center>
          </td>
          <td>
            <center><input type="text" id="Pcity" name="Pcity"></center>
          </td>

        </tr>
      </table>
    </center>
    <center><button type="submit" name="pnumBut" value="doSomethingP" style="background-color:rgb(23, 93, 34); color:rgb(58, 235, 82);">Enter Part Record
        Into Database</button>
      <button type="reset" style="background-color:rgb(67, 11, 11); color:rgb(240, 111, 111);">Clear Data and
        Results</button>
    </center>

  </form>
  <br />
  <form action="/Project4/DEUserServlet" method="get">
    <label>Job Record Insert</label>
    <br />
    <center>
      <table style="width:50%; border-color:rgb(185, 249, 9);">
        <tr>
          <th>jnum</th>
          <th>jname</th>
          <th>numworkers</th>
          <th>city</th>
        </tr>
        <tr>
          <td>
            <center><input type="text" id="jnum" name="jnum"></center>
          </td>
          <td>
            <center><input type="text" id="jname" name="jname"></center>
          </td>
          <td>
            <center><input type="text" id="numworkers" name="numworkers"></center>
          </td>
          <td>
            <center><input type="text" id="Jcity" name="Jcity"></center>
          </td>

        </tr>
      </table>
    </center>
    <center><button type="submit" name="jnumBut" value="doSomethingJ" style="background-color:rgb(23, 93, 34); color:rgb(58, 235, 82);">Enter Job Record
        Into Database</button>
      <button type="reset" style="background-color:rgb(67, 11, 11); color:rgb(240, 111, 111);">Clear Data and
        Results</button>
    </center>

  </form>
  <br />
  <form action="/Project4/DEUserServlet" method="get">
    <label>Shipments Record Insert</label>
    <br />
    <center>
      <table style="width:50%; border-color:rgb(185, 249, 9);">
        <tr>
          <th>snum</th>
          <th>pnum</th>
          <th>jnum</th>
          <th>quantity</th>
        </tr>
        <tr>
          <td>
            <center><input type="text" id="snumS" name="snumS"></center>
          </td>
          <td>
            <center><input type="text" id="pnumS" name="pnumS"></center>
          </td>
          <td>
            <center><input type="text" id="jnumS" name="jnumS"></center>
          </td>
          <td>
            <center><input type="text" id="quantity" name="quantity"></center>
          </td>

        </tr>
      </table>
    </center>
    <center><button type="submit" name="qnumBut" value="doSomethingQ" style="background-color:rgb(23, 93, 34); color:rgb(58, 235, 82);">Enter Shipment
        Record into Database</button>
      <button type="reset" style="background-color:rgb(67, 11, 11); color:rgb(240, 111, 111);">Clear Data and
        Results</button>
    </center>

  </form>
  <br />
  <center><label>Database Results:</label></center>
  </div>
  <center><div>
   
    <p> <%= session.getAttribute("htmlString") %></p>
    
   </div></center>
   <br />
</body>

</html>