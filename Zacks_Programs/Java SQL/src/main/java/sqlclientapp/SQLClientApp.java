package sqlclientapp;

/*
Name: Zachary Yore
Course: CNT 4714 Spring 2023
Assignment title: Project 3 – A Two-tier Client-Server Application
Date: March 9, 2023
Class: CNT 4714
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Statement;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Properties;
import javax.swing.table.DefaultTableModel;

public class SQLClientApp extends JFrame {

    public String fileName;
    public java.sql.Connection conn;
    Statement st = null;
    public String username = null;
    public String password = null;
    Object data1[][];
    public JScrollPane jsp;
    public DefaultTableModel listTableModel;
    public JPasswordField text6;
    public int query = 0;
    public int update = 0;
    public String errorMessage;
    public int succUpdate = 0;

    public SQLClientApp() throws SQLException, FileNotFoundException, IOException, ClassNotFoundException {

        JPanel panel = new JPanel();

        JButton but1 = new JButton("Connect to Database");
        JButton but2 = new JButton("Clear SQL Command");
        JButton but3 = new JButton("Execute SQL Command");
        JButton but4 = new JButton("Clear Result Window");

        JTextField text1 = new JTextField("Properties File");
        String s1[] = {"", "root.properties", "client.properties"};
        JComboBox combo = new JComboBox(s1);

        JTextField text2 = new JTextField("Connection Details");

        JTextField text3 = new JTextField("Username");
        JTextField text4 = new JTextField(""); // username input

        JTextField text5 = new JTextField("Password");
        text6 = new JPasswordField(""); // password input

        JTextField text7 = new JTextField("Enter An SQL Command");
        JTextArea text8 = new JTextArea(""); // SQL command input

        JTextField text9 = new JTextField("NO CONNECTION NOW"); // connection output

        JTextField text10 = new JTextField("SQL Execution Result Window");
        JTextField text11 = new JTextField(""); // Result window

        text1.setBackground(Color.lightGray);
        text3.setBackground(Color.lightGray);
        text5.setBackground(Color.lightGray);
        text9.setBackground(Color.BLACK);

        text2.setOpaque(false);
        text7.setOpaque(false);
        text10.setOpaque(false);

        text1.setEditable(false);
        text2.setEditable(false);
        text3.setEditable(false);
        text4.setEditable(true);
        text5.setEditable(false);
        text6.setEditable(true);
        text7.setEditable(false);
        text8.setEditable(true);
        text9.setEditable(false);
        text10.setEditable(false);
        text11.setEditable(false);

        text2.setForeground(Color.BLUE);
        text10.setForeground(Color.BLUE);
        text7.setForeground(Color.BLUE);
        but1.setForeground(Color.YELLOW);
        text9.setForeground(Color.RED);
        but2.setForeground(Color.red);

        but1.setBackground(Color.BLUE);
        but1.setOpaque(true);
        but1.setBorderPainted(false);
        but2.setBackground(Color.WHITE);
        but2.setOpaque(true);
        but2.setBorderPainted(false);
        but3.setBackground(Color.GREEN);
        but3.setOpaque(true);
        but3.setBorderPainted(false);
        but4.setBackground(Color.YELLOW);
        but4.setOpaque(true);
        but4.setBorderPainted(false);

        text2.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        text7.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        text10.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        Dimension size1 = text1.getPreferredSize();
        Dimension size2 = text2.getPreferredSize();
        Dimension size4 = text4.getPreferredSize();
        Dimension size5 = text5.getPreferredSize();
        Dimension size7 = text7.getPreferredSize();
        Dimension size10 = text10.getPreferredSize();

        text8.setWrapStyleWord(true);
        JScrollPane scrollPane1 = new JScrollPane(text8);
        //text11.setWrapStyleWord(true);
        JScrollPane scrollPane2 = new JScrollPane(text11);

        text2.setBounds(10, 10, size2.width, size2.height);
        text1.setBounds(10, 30, 120, 30);
        combo.setBounds(130, 30, 240, 30);
        text3.setBounds(10, 65, 120, 30);
        text5.setBounds(10, 100, 120, 30);
        text4.setBounds(130, 65, 240, 30);
        text6.setBounds(130, 100, 240, 30);
        but1.setBounds(15, 165, 175, 30);
        text9.setBounds(15, 210, 870, 30);
        text7.setBounds(400, 10, size7.width, size7.height);
        scrollPane1.setBounds(400, 35, 480, 125);
        but2.setBounds(420, 165, 175, 30);
        but3.setBounds(650, 165, 190, 30);
        text10.setBounds(45, 255, size10.width, size10.height);
        scrollPane2.setBounds(45, 275, 800, 235);
        but4.setBounds(15, 525, 175, 30);

        panel.setLayout(null);
        panel.add(text2);
        panel.add(text1);
        panel.add(text3);
        panel.add(text5);
        panel.add(text4);
        panel.add(text6);
        panel.add(but1);
        panel.add(text9);
        panel.add(text7);
        panel.add(scrollPane1);
        panel.add(but2);
        panel.add(but3);
        panel.add(text10);
        panel.add(but4);
        panel.add(scrollPane2);
        panel.add(combo);

        add(panel);
        setTitle("SQL Client App-(MJL-CNT 4714-Spring2023-Project 3)");
        setBackground(Color.lightGray);
        setSize(900, 600);

        setVisible(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        combo.addActionListener((ActionEvent e) -> { // combobox sets filename
            fileName = (String) combo.getSelectedItem();
            System.out.println(fileName);

        });

        but1.addActionListener((ActionEvent e) -> { // connect to DB button
            System.out.println("-------- MySQL JDBC Connection Testing ------------");

            Properties props = new Properties();
            FileInputStream in = null;
            try {
                in = new FileInputStream(fileName);

                props.load(in);

                in.close();

                String driver = props.getProperty("DB_DRIVER_CLASS");
                if (driver != null) {
                    try {
                        Class.forName(driver);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(SQLClientApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                String url = props.getProperty("DB_URL");
                username = props.getProperty("DB_USERNAME");
                password = props.getProperty("DB_PASSWORD");

                conn = null;
                if (text4.getText().equals(username) && text6.getText().equals(password)) {
                    conn = DriverManager.getConnection(url, username, password);
                } else {
                    //conn.close();
                    System.out.println("wrong password");
                    text9.setForeground(Color.RED);
                    text9.setText("NOT CONNECTED - User Credentials Do Not Match Properties File!");
                }

                if (conn != null) {
                    text9.setForeground(Color.YELLOW);
                    text9.setText("CONNECTED TO: " + url);
                    System.out.println("You made it, take control your database now!");
                } else {
                    System.out.println("Failed to make connection!");
                }
            } catch (IOException ex) {
                Logger.getLogger(SQLClientApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(SQLClientApp.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        but3.addActionListener((ActionEvent e) -> { // Execute SQL command

            String input[] = text8.getText().split(";");
            for (int x = 0; x < input.length; x++) {

                String str = input[x].replaceAll("\\s", "");
                str = str.toLowerCase();
                String substring = "select";
                if (str.contains(substring)) {

                    try {
                        st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet rs = st.executeQuery(input[x]);
                        ResultSetMetaData metaData = rs.getMetaData();
                        rs.last();
                        int rows = rs.getRow();
                        rs.beforeFirst();
                        OperationsLog(query += 1, update);
                        int colummm = metaData.getColumnCount();
                        Object[] Colheads = new Object[colummm];

                        for (int i = 1; i <= colummm; i++) {
                            Colheads[i - 1] = metaData.getColumnName(i);
                        }

                        data1 = new Object[rows][colummm];

                        for (int i1 = 0; i1 < rows; i1++) {
                            rs.next();
                            for (int j1 = 0; j1 < Colheads.length; j1++) {
                                data1[i1][j1] = rs.getString(j1 + 1);
                            }
                        }

                        listTableModel = new DefaultTableModel(data1, Colheads);
                        JTable table = new JTable(listTableModel);

                        text11.setVisible(false);
                        scrollPane2.setVisible(false);
                        jsp = new JScrollPane(table);
                        jsp.setBounds(45, 275, 800, 235);
                        panel.add(jsp);

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        errorMessage = ex.getMessage();
                        NewWindow1 Error = new NewWindow1();
                    }
                } else {
                    System.out.println("not select");
                    try {
                        PreparedStatement preparedStmt = conn.prepareStatement(input[x]);
                        preparedStmt.executeUpdate();
                        OperationsLog(query, update += 1);
                        succUpdate = preparedStmt.getUpdateCount();
                        NewWindow2 succ = new NewWindow2();
                    } catch (SQLException ex) {
                        Logger.getLogger(SQLClientApp.class.getName()).log(Level.SEVERE, null, ex);
                        if (username.equals("client")) {
                            errorMessage = ex.getMessage();
                            NewWindow1 Error = new NewWindow1();
                        }
                        errorMessage = ex.getMessage();
                        NewWindow1 Error = new NewWindow1();
                    }
                }
            }

        });

        but4.addActionListener((ActionEvent e) -> {
            listTableModel.setRowCount(0);
            listTableModel.setColumnCount(0);
        });

        but2.addActionListener((ActionEvent e) -> {
            text8.setText("");
        });

    }

    void OperationsLog(int query, int update) {

        if (username.equals("root") && text6.getText().equals(password)) {
            Properties props = new Properties();
            FileInputStream in = null;
            try {
                in = new FileInputStream("operations.properties");

                props.load(in);

                in.close();

                String driver = props.getProperty("DB_DRIVER_CLASS");
                if (driver != null) {
                    try {
                        Class.forName(driver);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(SQLClientApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                String url = props.getProperty("DB_URL");
                username = props.getProperty("DB_USERNAME");
                password = props.getProperty("DB_PASSWORD");

                java.sql.Connection con = null;
                con = DriverManager.getConnection(url, username, password);

                try {
                    PreparedStatement preparedStmt = con.prepareStatement("UPDATE operationscount SET num_queries = " + query + ", num_updates = " + update + ";");
                    preparedStmt.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(SQLClientApp.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (IOException ex) {
                Logger.getLogger(SQLClientApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(SQLClientApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class NewWindow1 {

        JFrame frame = new JFrame();
        JLabel label = new JLabel(errorMessage);
        JLabel label1 = new JLabel();
        JButton but = new JButton("OK");
        Dimension size1 = label.getPreferredSize();
        Dimension size2 = but.getPreferredSize();

        NewWindow1() {

            label1.setIcon(new ImageIcon(new ImageIcon("javaErrorPic.jpeg").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
            Dimension size3 = label1.getPreferredSize();

            frame.setTitle("Database error");

            label.setBounds(230, 60, size1.width, size1.height);
            label1.setBounds(40, 30, size3.width, size3.height);
            but.setBounds(550, 130, 80, size2.height);

            frame.add(but);
            frame.add(label);
            frame.add(label1);
            frame.setSize(700, 200);
            frame.setLayout(null);
            frame.setVisible(true);

            but.addActionListener((ActionEvent e) -> {
                frame.dispose();
            });
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    public class NewWindow2 {

        JFrame frame = new JFrame();
        JLabel label = new JLabel("Successful Update..." + succUpdate + " rows updated.");
        JLabel label1 = new JLabel();
        JButton but = new JButton("OK");
        Dimension size1 = label.getPreferredSize();
        Dimension size2 = but.getPreferredSize();

        NewWindow2() {

            label1.setIcon(new ImageIcon(new ImageIcon("java.jpeg").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
            Dimension size3 = label1.getPreferredSize();

            frame.setTitle("Successful Update");

            label.setBounds(230, 60, size1.width, size1.height);
            label1.setBounds(40, 30, size3.width, size3.height);
            but.setBounds(400, 130, 80, size2.height);

            frame.add(but);
            frame.add(label);
            frame.add(label1);
            frame.setSize(550, 200);
            frame.setLayout(null);
            frame.setVisible(true);

            but.addActionListener((ActionEvent e) -> {
                frame.dispose();
            });
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    public static void main(String[] args) throws SQLException, IOException, FileNotFoundException, ClassNotFoundException {
        SQLClientApp com = new SQLClientApp();
    }
}
