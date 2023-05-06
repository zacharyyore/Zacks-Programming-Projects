package niledotcom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NileDotCom extends JFrame {

    private JSplitPane jsp;
    private JButton but1, but2, but3, but4, but5, but6, but7, but8;
    private JTextField text5, text6, text7, text8;
    public String[] dirc = new String[4];
    public String[] dirc1 = new String[6];
    public String ID;
    public int itemsPurch = 1;
    public String[][] purchasedItems = new String[100][6];
    public String time;

    public NileDotCom() {
        this.ID = ID;
        this.itemsPurch = 1;
        setTitle("Nile Dot Com - Spring 2023");

        JPanel panel1 = new JPanel();
        panel1.setBackground(Color.DARK_GRAY);
        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.CYAN);

        JButton but1 = new JButton("Find Item #1");
        JButton but2 = new JButton("View Current Order");
        JButton but3 = new JButton("Start New Order");
        JButton but4 = new JButton("Purchase Item #1");
        JButton but5 = new JButton("Complete Order - Check Out");
        JButton but6 = new JButton("Exit(Close App)");
        but2.setEnabled(false);
        but4.setEnabled(false);
        but5.setEnabled(false);

        //but1.addActionListener(this);
        JTextField text1 = new JTextField("Enter ID for Item #1:");
        JTextField text2 = new JTextField("Enter quantity for Item #1:");
        JTextField text3 = new JTextField("Details for Item #1:");
        JTextField text4 = new JTextField("Order subtotal for 0 Item(s):");
        JTextField text5 = new JTextField("");
        JTextField text6 = new JTextField("");
        JTextField text7 = new JTextField("");
        JTextField text8 = new JTextField("");

        text1.setBackground(Color.DARK_GRAY);
        text2.setBackground(Color.DARK_GRAY);
        text3.setBackground(Color.DARK_GRAY);
        text4.setBackground(Color.DARK_GRAY);
        text1.setEditable(false);
        text2.setEditable(false);
        text3.setEditable(false);
        text4.setEditable(false);
        text5.setEditable(true);
        text6.setEditable(true);
        text7.setEditable(false);
        text8.setEditable(false);
        text1.setForeground(Color.YELLOW);
        text2.setForeground(Color.YELLOW);
        text3.setForeground(Color.YELLOW);
        text4.setForeground(Color.YELLOW);
        text1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        text2.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        text3.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        text4.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        Dimension size1 = text1.getPreferredSize();
        Dimension size2 = text2.getPreferredSize();
        Dimension size3 = text3.getPreferredSize();
        Dimension size4 = text4.getPreferredSize();

        but1.setBounds(20, 5, 350, 29);
        but2.setBounds(20, 45, 350, 29);
        but3.setBounds(20, 85, 350, 29);
        but4.setBounds(370, 5, 350, 29);
        but5.setBounds(370, 45, 350, 29);
        but6.setBounds(370, 85, 350, 29);

        but2.setBackground(Color.CYAN);
        but2.setOpaque(true);
        but2.setForeground(Color.GRAY);
        but2.setBorderPainted(false);
        but4.setBackground(Color.CYAN);
        but4.setOpaque(true);
        but4.setForeground(Color.GRAY);
        but4.setBorderPainted(false);
        but5.setBackground(Color.CYAN);
        but5.setOpaque(true);
        but5.setForeground(Color.GRAY);
        but5.setBorderPainted(false);

        text1.setBounds(130, 20, size1.width, size1.height);
        text2.setBounds(90, 60, size2.width, size2.height);
        text3.setBounds(135, 100, size3.width, size3.height);
        text4.setBounds(83, 140, size4.width, size4.height);
        text5.setBounds(260, 15, 450, 29);
        text6.setBounds(260, 55, 450, 29);
        text7.setBounds(260, 95, 450, 29);
        text8.setBounds(260, 135, 450, 29);

        panel1.setLayout(null);
        panel1.add(text1);
        panel1.add(text2);
        panel1.add(text3);
        panel1.add(text4);
        panel1.add(text5);
        panel1.add(text6);
        panel1.add(text7);
        panel1.add(text8);

        panel2.setLayout(null);
        panel2.add(but1);
        panel2.add(but2);
        panel2.add(but3);
        panel2.add(but4);
        panel2.add(but5);
        panel2.add(but6);

        jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel1, panel2);
        jsp.setDividerSize(0);
        jsp.setResizeWeight(0.6);
        add(jsp);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(750, 400);
        setVisible(true);

        try {
            File myObj = new File("transactions.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        but1.addActionListener((ActionEvent e) -> { // find item button
            try {
                ID = text5.getText();
                dirc = findItem();
                if (dirc != null && "true".equals(dirc[2])) {
                    but1.setEnabled(false);
                    but4.setEnabled(true);
                    dirc[2] = dirc[3];
                    dirc[3] = text6.getText();
                    for (int i = 0; i <= 3; i++) {
                        dirc1[i] = dirc[i];
                    }

                    but1.setBackground(Color.CYAN);
                    but1.setOpaque(true);
                    but1.setForeground(Color.GRAY);
                    but1.setBorderPainted(false);

                    but4.setBackground(Color.WHITE);
                    but4.setOpaque(false);
                    but4.setForeground(Color.BLACK);
                    but4.setBorderPainted(true);

                    text4.setText("Order subtotal for " + text6.getText() + " Item(s):");

                    int quant = Integer.parseInt(dirc[3]);

                    if (quant <= 4) {
                        dirc1[4] = "0%";
                    } else if (quant >= 5 && quant <= 9) {
                        dirc1[4] = "10%";
                    } else if (quant >= 10 && quant <= 14) {
                        dirc1[4] = "15%";
                    } else if (quant >= 15) {
                        dirc1[4] = "20%";
                    }

                    String str2 = dirc1[4].substring(0, dirc1[4].length() - 1);
                    double cost = Double.parseDouble(dirc[3]) * Double.parseDouble(dirc[2]);
                    double discount = Double.parseDouble(str2) / 100;
                    DecimalFormat dfZero = new DecimalFormat("0.00");

                    if (Integer.parseInt(str2) > 0) {
                        double cost1 = cost * discount;
                        double cost2 = cost - cost1;
                        dirc1[5] = "$" + String.valueOf(dfZero.format(cost2));
                    } else if (Integer.parseInt(str2) == 0) {
                        dirc1[5] = "$" + String.valueOf(dfZero.format(cost));
                    }

                    text8.setText(dirc1[5]);

                    dirc1[2] = "$" + dirc[2];
                    String str1 = "";
                    StringBuilder sb = new StringBuilder();
                    for (String str : dirc1) {
                        sb.append(str).append(" ");
                    }

                    str1 = sb.substring(0, sb.length() - 1);
                    text7.setText(str1);

                } else if (dirc != null && "false".equals(dirc[2])) {
                    NewWindow2 myWindow = new NewWindow2();
                    text5.setText("");
                    text6.setText("");
                    text7.setText("");
                    text8.setText("");

                } else {
                    NewWindow1 myWindow = new NewWindow1();
                    text5.setText("");
                    text6.setText("");
                    text7.setText("");
                    text8.setText("");
                }
            } catch (IOException ex) {
                Logger.getLogger(NileDotCom.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        but2.addActionListener((ActionEvent e) -> {
            NewWindow4 myWindow = new NewWindow4();
        });

        but3.addActionListener((ActionEvent e) -> { // Start new order button
            itemsPurch = 1;
            int i = itemsPurch - 1;

            but1.setEnabled(true);
            but2.setEnabled(false);
            but4.setEnabled(false);
            but5.setEnabled(false);

            but2.setBackground(Color.CYAN);
            but2.setOpaque(true);
            but2.setForeground(Color.GRAY);
            but2.setBorderPainted(false);

            but1.setBackground(Color.WHITE);
            but1.setOpaque(false);
            but1.setForeground(Color.BLACK);
            but1.setBorderPainted(true);

            text5.setText("");
            text6.setText("");
            text7.setText("");
            text8.setText("");
            text5.setEditable(true);
            text6.setEditable(true);

            but1.setText("Find Item #" + itemsPurch);
            but4.setText("Purchase Item #" + itemsPurch);
            text1.setText("Enter ID for Item #" + itemsPurch + ":");
            text2.setText("Enter quantity for Item #" + itemsPurch + ":");
            text3.setText("Details for Item #" + itemsPurch + ":");
            text4.setText("Order subtotal for " + i + " Item(s):");
        });

        but4.addActionListener((ActionEvent e) -> {// purchase Item button
            but2.setEnabled(true);
            but1.setEnabled(true);
            but5.setEnabled(true);
            but4.setEnabled(false);
            itemsPurch += 1;
            int i = itemsPurch - 1;
            int z = i - 1;

            but1.setText("Find Item #" + itemsPurch);
            but4.setText("Purchase Item #" + itemsPurch);
            text5.setText("");
            text6.setText("");

            text1.setText("Enter ID for Item #" + itemsPurch + ":");
            text2.setText("Enter quantity for Item #" + itemsPurch + ":");
            text3.setText("Details for Item #" + i + ":");

            for (int l = 0; l < 6; l++) {
                purchasedItems[z][l] = dirc1[l];
            }

            for (int o = 0; o < 100; o++) {
                for (int j = 0; j < 6; j++) {
                }
            }

            NewWindow3 myWindow = new NewWindow3();
            but1.setBackground(Color.WHITE);
            but1.setOpaque(false);
            but1.setForeground(Color.BLACK);
            but1.setBorderPainted(true);
            but2.setBackground(Color.WHITE);
            but2.setOpaque(false);
            but2.setForeground(Color.BLACK);
            but2.setBorderPainted(true);
            but5.setBackground(Color.WHITE);
            but5.setOpaque(false);
            but5.setForeground(Color.BLACK);
            but5.setBorderPainted(true);
            but4.setBackground(Color.CYAN);
            but4.setOpaque(true);
            but4.setForeground(Color.GRAY);
            but4.setBorderPainted(false);

        });

        but5.addActionListener((ActionEvent e) -> { // complete check out button

            but1.setEnabled(false);
            but4.setEnabled(false);
            but5.setEnabled(false);

            NewWindow5 myWindow = new NewWindow5();

            text5.setEditable(false);
            text6.setEditable(false);

            but5.setBackground(Color.CYAN);
            but5.setOpaque(true);
            but5.setForeground(Color.GRAY);
            but5.setBorderPainted(false);

            but1.setBackground(Color.CYAN);
            but1.setOpaque(true);
            but1.setForeground(Color.GRAY);
            but1.setBorderPainted(false);

            but4.setBackground(Color.CYAN);
            but4.setOpaque(true);
            but4.setForeground(Color.GRAY);
            but4.setBorderPainted(false);
        });

        but6.addActionListener((ActionEvent e) -> {
            dispose();
        });

    }

    public String[] findItem() throws FileNotFoundException, IOException {
        String files = "inventory.txt";
        try ( BufferedReader br = new BufferedReader(new FileReader(files))) {
            String line;
            while ((line = br.readLine()) != null) {
                dirc = line.split(", ");
                if (dirc[0].equals(ID)) {
                    return dirc;
                }
            }
        }
        return null;
    }

    public class NewWindow1 {

        JFrame frame = new JFrame();
        JLabel label = new JLabel("item ID " + ID + " not in file");
        JLabel label1 = new JLabel();
        JButton but = new JButton("OK");
        Dimension size1 = label.getPreferredSize();
        Dimension size2 = but.getPreferredSize();

        NewWindow1() {

            label1.setIcon(new ImageIcon(new ImageIcon("javaErrorPic.jpeg").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
            Dimension size3 = label1.getPreferredSize();

            frame.setTitle("Nile Dot Com - ERROR");

            label.setBounds(230, 60, size1.width, size1.height);
            label1.setBounds(40, 30, size3.width + 5, size3.height + 5);
            but.setBounds(280, 130, 80, size2.height);

            frame.add(but);
            frame.add(label);
            frame.add(label1);
            frame.setSize(420, 220);
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
        JLabel label = new JLabel("Sorry... that item is out of stock, please try another item");
        JLabel label1 = new JLabel();
        JButton but = new JButton("OK");
        Dimension size1 = label.getPreferredSize();
        Dimension size2 = but.getPreferredSize();

        NewWindow2() {

            label1.setIcon(new ImageIcon(new ImageIcon("javaErrorPic2.jpeg").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
            Dimension size3 = label1.getPreferredSize();

            frame.setTitle("Nile Dot Com - ERROR");

            label.setBounds(200, 60, size1.width, size1.height);
            label1.setBounds(40, 30, size3.width + 5, size3.height + 5);
            but.setBounds(500, 130, 80, size2.height);

            frame.add(but);
            frame.add(label);
            frame.add(label1);
            frame.setSize(600, 220);
            frame.setLayout(null);
            frame.setVisible(true);

            but.addActionListener((ActionEvent e) -> {
                frame.dispose();
            });
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    public class NewWindow3 {

        int i = itemsPurch - 1;
        JFrame frame = new JFrame();
        JLabel label = new JLabel("Item #" + i + " accepted. Added to your cart.");
        JLabel label1 = new JLabel();
        JButton but = new JButton("OK");
        Dimension size1 = label.getPreferredSize();
        Dimension size2 = but.getPreferredSize();

        NewWindow3() {

            label1.setIcon(new ImageIcon(new ImageIcon("congrats.jpeg").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
            Dimension size3 = label1.getPreferredSize();

            frame.setTitle("Nile Dot Com - Item Confirmed");

            label.setBounds(160, 60, size1.width, size1.height);
            label1.setBounds(40, 30, size3.width + 5, size3.height + 5);
            but.setBounds(280, 130, 80, size2.height);

            frame.add(but);
            frame.add(label);
            frame.add(label1);
            frame.setSize(420, 220);
            frame.setLayout(null);
            frame.setVisible(true);

            but.addActionListener((ActionEvent e) -> {
                frame.dispose();
            });
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    public class NewWindow4 {

        int z = itemsPurch - 1;
        JFrame frame = new JFrame();
        JLabel label = new JLabel();
        JButton but = new JButton("OK");

        Dimension size2 = but.getPreferredSize();

        NewWindow4() {
            label.setIcon(new ImageIcon(new ImageIcon("info.jpeg").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
            Dimension size = label.getPreferredSize();
            label.setBounds(20, 0, size.width, size.height);
            frame.add(label);

            JLabel[] labels = new JLabel[z];
            int x = 0;
            for (int o = 0; o < z; o++) {
                labels[o] = new JLabel(o + 1 + ". " + String.join(" ", purchasedItems[o]));
                labels[o].setBounds(100, x, 550, 29);
                frame.add(labels[o]);
                x += 40;
            }

            frame.setTitle("Nile Dot Com - Current Shopping Cart Status");

            but.setBounds(300, 30 + x, 80, size2.height);

            frame.add(but);

            frame.setSize(650, 100 + x);
            frame.setLayout(null);
            frame.setVisible(true);

            but.addActionListener((ActionEvent e) -> {
                frame.dispose();
            });
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    public class NewWindow5 {

        int z = itemsPurch - 1;
        JFrame frame = new JFrame();
        JLabel label = new JLabel();
        JButton but = new JButton("OK");
        JLabel label3 = new JLabel("Item# / ID / Price / Qty / Disc% / Subtotal:");
        JLabel label5 = new JLabel("Tax rate:       6%");
        JLabel label8 = new JLabel("Thanks for shopping at Nile Dot Com!");

        Dimension size9 = but.getPreferredSize();

        NewWindow5() {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy, HH:mm:ss");
            DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("ddMMyyyyHHmm");

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime now1 = LocalDateTime.now();

            JLabel label1 = new JLabel("Date: " + dtf.format(now) + " PM EST");

            label.setIcon(new ImageIcon(new ImageIcon("java.jpeg").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
            Dimension size = label.getPreferredSize();
            label.setBounds(20, 0, size.width, size.height);
            frame.add(label);

            JLabel[] labels = new JLabel[z];
            int x = 0;
            int o = 0;
            double total = 0;
            for (o = 0; o < z; o++) {
                labels[o] = new JLabel(o + 1 + ". " + String.join(" ", purchasedItems[o]));
                labels[o].setBounds(100, 120 + x, 550, 29);
                frame.add(labels[o]);
                total = Double.parseDouble(purchasedItems[o][5].substring(1)) + total;
                x += 40;

                try {
                    FileWriter fstream = new FileWriter("transactions.txt", true);
                    BufferedWriter out = new BufferedWriter(fstream);
                    out.write(dtf1.format(now1) + ", " + String.join(", ", purchasedItems[o]) + ", " + dtf.format(now) + " PM EST" + "\n");
                    out.close();
                } catch (Exception e) {
                    System.err.println("Error while writing to file: " + e.getMessage());
                }
            }
            DecimalFormat dfZero = new DecimalFormat("0.00");
            JLabel label2 = new JLabel("Number of line items: " + o);
            JLabel label4 = new JLabel("Order subtotal:     $" + dfZero.format(total));
            JLabel label6 = new JLabel("Tax amount:       $" + dfZero.format(total * .06));
            JLabel label7 = new JLabel("ORDER TOTAL:        $" + dfZero.format(total + (total * .06)));

            Dimension size1 = label1.getPreferredSize();
            Dimension size2 = label2.getPreferredSize();
            Dimension size3 = label3.getPreferredSize();
            Dimension size4 = label4.getPreferredSize();
            Dimension size5 = label5.getPreferredSize();
            Dimension size6 = label6.getPreferredSize();
            Dimension size7 = label7.getPreferredSize();
            Dimension size8 = label8.getPreferredSize();

            label1.setBounds(100, 0, size1.width, size1.height);
            label2.setBounds(100, 40, size2.width, size2.height);
            label3.setBounds(100, 80, size3.width, size3.height);

            label4.setBounds(100, 160 + x, size1.width, size1.height);
            label5.setBounds(100, 200 + x, size2.width, size2.height);
            label6.setBounds(100, 240 + x, size3.width, size3.height);
            label7.setBounds(100, 280 + x, size3.width, size3.height);

            frame.setTitle("Nile Dot Com - FINAL INVOICE");

            but.setBounds(500, 300 + x, 80, size9.height);

            frame.add(but);
            frame.add(label1);
            frame.add(label2);
            frame.add(label3);
            frame.add(label4);
            frame.add(label5);
            frame.add(label6);
            frame.add(label7);
            frame.add(label8);

            frame.setSize(650, 400 + x);
            frame.setLayout(null);
            frame.setVisible(true);

            but.addActionListener((ActionEvent e) -> {
                frame.dispose();
            });
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    public static void main(String[] args) {
        NileDotCom com = new NileDotCom();
    }
}
