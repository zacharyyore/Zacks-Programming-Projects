
package userInterface;

import game.Game;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.*;


public class ScraggleUi 
{
    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu gameMenu;
    private JMenuItem exit;
    private JMenuItem newGame;
    
    //////////////// Scraggle board
    private JPanel scragglePanel;
    private JButton[][] diceButtons;

    //////////////// Enter found words
    private JPanel wordsPanel;
    private JScrollPane scrollPane;
    private JTextPane wordsArea;
    
    ///////////////// time label 
    private JLabel timeLabel;
    private JButton shakeDice;

    ///////////////// Enter current word
    private JPanel currentPanel;
    private JLabel currentLabel;
    private JButton currentSubmit;
        
    /////////////////// player's score
    private JLabel scoreLabel;
    private int score;

    private Game game;
    
    /////////////////// Timer
    private Timer timer;
    private int minutes = 2;
    private int seconds = 0;
    
    private ArrayList<String> foundWords = new ArrayList<String>();
            
    private final int GRID = 4;
    
    private final static int MAX_INDEX = 3;
    private final static int MIN_INDEX = 0;
    private final static String PLUS = "+";
    private final static String MINUS = "-";
    
    private ResetGameListener resetGameListener;
    
    public ScraggleUi(Game inGame)
    {
        game = inGame;
        
        initObjects();
        initComponents();
    }
    private void initObjects()
    {
    resetGameListener = new ResetGameListener();
    }
    
    private void setupTimer()
    {
     timer = new Timer(1000, new TimerListener());
     timer.start();
    }
    
    private void initComponents()
    {
        /////////////// Initialize the JFrame
        frame = new JFrame("Scraggle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(660, 500);
        
        ///////////////// Initialize the JMenuBar and add to the JFrame
        createMenu();   

        //////////////// Initialize the JPane for the current word
        setupCurrentPanel();
        
        ////////////////// Initialize the JPanel for the word entry
        setupWordPanel();
        
        ////////////////// Initialize the JPanel for the Scraggle dice
        setupScragglePanel(); 
        
        setupTimer();
        
        // Add everything to the JFrame
        frame.setJMenuBar(menuBar);
        frame.add(scragglePanel, BorderLayout.WEST);
        frame.add(wordsPanel, BorderLayout.CENTER);
        frame.add(currentPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    
    private void createMenu()
    {
        menuBar = new JMenuBar();
        gameMenu = new JMenu("Scraggle");
        gameMenu.setMnemonic('S');
        
        newGame = new JMenuItem("New Game");
        newGame.setMnemonic('N');
        newGame.addActionListener(new ResetGameListener()); //b.Register ActionListener class ResetGameListener to JMenuItem “New Game” (i.e. newGame)

        exit = new JMenuItem("Exit");
        exit.setMnemonic('E');
        exit.addActionListener(new ExitListener()); //c.Register ActionListener class ExitListener to JMenuItem “Exit” (i.e. exit)
        
        gameMenu.add(newGame);    
        gameMenu.add(exit);    
        menuBar.add(gameMenu);
    }

    private void setupCurrentPanel()
    {
        currentPanel = new JPanel();
        currentPanel.setBorder(BorderFactory.createTitledBorder("Current Word"));

        currentLabel = new JLabel();
        currentLabel.setBorder(BorderFactory.createTitledBorder("Current Word"));
        currentLabel.setMinimumSize(new Dimension(300, 50));
        currentLabel.setPreferredSize(new Dimension(300,50));
        currentLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        currentSubmit = new JButton("Submit Word");
        currentSubmit.setMinimumSize(new Dimension(200, 100));
        currentSubmit.setPreferredSize(new Dimension(200, 50));
        currentSubmit.addActionListener(new SubmitListener()); //d.Register ActionListener class SubmitListener to JButton “Submit Word” (i.e. currentSubmit)
        
        scoreLabel = new JLabel();
        scoreLabel.setBorder(BorderFactory.createTitledBorder("Score"));
        scoreLabel.setMinimumSize(new Dimension(100, 50));
        scoreLabel.setPreferredSize(new Dimension(100,50));
        
        currentPanel.add(currentLabel);
        currentPanel.add(currentSubmit);
        currentPanel.add(scoreLabel);
    }

    private void setupWordPanel()
    {
        wordsPanel = new JPanel();
        wordsPanel.setLayout(new BoxLayout(wordsPanel, BoxLayout.Y_AXIS));
        wordsPanel.setBorder(BorderFactory.createTitledBorder("Enter Words Found"));
        
        wordsArea = new JTextPane();
        scrollPane = new JScrollPane(wordsArea);
        scrollPane.setPreferredSize(new Dimension(180, 330));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        timeLabel = new JLabel("3:00");
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setFont(new Font("Serif", Font.PLAIN, 48));
        timeLabel.setPreferredSize(new Dimension(240, 100));
        timeLabel.setMinimumSize(new Dimension(240, 100));
        timeLabel.setMaximumSize(new Dimension(240, 100));
        timeLabel.setBorder(BorderFactory.createTitledBorder("Time Left"));
        
        shakeDice = new JButton("Shake Dice");
        shakeDice.setPreferredSize(new Dimension(240, 100));
        shakeDice.setMinimumSize(new Dimension(240, 100));
        shakeDice.setMaximumSize(new Dimension(240, 100));
        shakeDice.addActionListener(resetGameListener); //e.Register ActionListener class ResetGameListener to JButton “Shake Dice” (i.e. shakeDice)
        
        wordsPanel.add(scrollPane);
        wordsPanel.add(timeLabel);
        wordsPanel.add(shakeDice);
    }

    private void setupScragglePanel()
    {   
        scragglePanel = new JPanel();
        scragglePanel.setLayout(new GridLayout(4, 4));
        scragglePanel.setMinimumSize(new Dimension(400, 400));
        scragglePanel.setPreferredSize(new Dimension(400, 400));
        scragglePanel.setBorder(BorderFactory.createTitledBorder("Scraggle Board"));
        
        diceButtons = new JButton[GRID][GRID];
        
        for(int row = 0; row < GRID; row++)
            for(int col = 0; col < GRID; col++)
        {
            URL imgURL = getClass().getResource(game.getGrid()[row][col].getImgPath());
            ImageIcon icon = new ImageIcon(imgURL);
            icon = imageResize(icon);
             
            // add the image to the Jbutton
            diceButtons[row][col] = new JButton(icon);
            diceButtons[row][col].setMinimumSize(new Dimension(400, 400));
            diceButtons[row][col].setMaximumSize(new Dimension(240, 100));        
            
            // add client properties
            diceButtons[row][col].putClientProperty("row", row);
            diceButtons[row][col].putClientProperty("col", col);
            diceButtons[row][col].putClientProperty("letter",game.getGrid()[row][col].getLetter() ); //check
            
            //instantiate the event handlers
            TileListener tileListener = new TileListener();
            LetterListener letterListener = new LetterListener();
            
            diceButtons[row][col].addActionListener(letterListener);
            diceButtons[row][col].addActionListener(tileListener);
            
            scragglePanel.add(diceButtons[row][col]);
        }
    }
    
    private ImageIcon imageResize(ImageIcon icon)
    {
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(95, 95, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImage);
        return icon;
    }  

    // inner class for envent handlers 4
    private class ExitListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent ae)
        {
          int response = JOptionPane.showConfirmDialog(frame, "confirm to exit Scraggle?",
                  "Exit", JOptionPane.YES_NO_OPTION);
          
          if (response == JOptionPane.YES_OPTION)
              System.exit(0);
        }
    }
    
  //5  
private class ResetGameListener implements ActionListener
{

        @Override
        public void actionPerformed(ActionEvent ae) 
        {
          score = 0;
          game.populateGrid();
          
          frame.remove(scragglePanel);
          scragglePanel.removeAll();
          setupScragglePanel();
          scragglePanel.revalidate();
          scragglePanel.repaint();
          frame.add(scragglePanel, BorderLayout.WEST);
          wordsArea.setText("");
          scoreLabel.setText("0");
          currentLabel.setText("");
          timeLabel.setText("3:00");
          foundWords.removeAll(foundWords); 
          timer.stop();
          
          minutes = 3;
          seconds = 0;
          timer.start();
        }


}
// inner class 6
private class SubmitListener implements ActionListener
{
        @Override
        public void actionPerformed(ActionEvent ae) 
        {
           int wordScore = game.getDictionary().search(currentLabel.getText().toLowerCase());
           
           if(foundWords.contains(currentLabel.getText().toLowerCase()))
           {
           JOptionPane.showMessageDialog(frame, "Word found already");
           }
           else
           {
           if(wordScore > 0)
           {
           wordsArea.setText(wordsArea.getText()+ "\n" + currentLabel.getText());
           wordsArea.setCaretPosition(wordsArea.getDocument().getLength());
           
           foundWords.add(currentLabel.getText().toLowerCase());
           score += wordScore;
           scoreLabel.setText(String.valueOf(score));
           }
           else if(wordScore == 0)
           {
           JOptionPane.showMessageDialog(frame, "Word is not valid");
           }
           
           }
    
           currentLabel.setText("");
           
           for(int row = 0; row < GRID; row++)
           {
           for(int col = 0; col < GRID; col++)
           {
           diceButtons[row][col].setEnabled(true);
           }
           
           }
        }
}
        // inner class 7
     private class LetterListener implements ActionListener 
     {

        @Override
        public void actionPerformed(ActionEvent ae) 
        {
            JButton button = (JButton)ae.getSource();
            String letter = (String)button.getClientProperty("letter");
            currentLabel.setText(currentLabel.getText() + letter);
            
        }
     
     }
     
     // inner class 8
       private class TileListener implements ActionListener 
       {

        @Override
        public void actionPerformed(ActionEvent ae)
        {
            //based on the row and the col of the JButton clicked 
            //enable one row up, one down, one left, one col right
            if(ae.getSource() instanceof JButton)
            {
            JButton button = (JButton)ae.getSource();
            
            int row = (int)button.getClientProperty("row");
            int col = (int)button.getClientProperty("col");
            
            
            // cannot -1 for row
            
            //diable all the buttons
            for(int i = 0; i < 4; i++)
            {
            for(int j = 0; j < 4; j++)
            {
            diceButtons[i][j].setEnabled(false);
            }
            }
            //enable availabel jbuttons based on the location location 0,0
            if(row == 0 && col == 0)
            {
                //row plus 1 and current colum
                //position 0/0
            diceButtons[row][col + 1].setEnabled(true);
            diceButtons[row + 1][col + 1].setEnabled(true);
            diceButtons[row + 1 ][col].setEnabled(true);
            }
            // location 3,3 
            else if(row == 3 && col == 3)
            {
                //row plus 1 and current colum
                //position 3/3
            diceButtons[row ][col - 1].setEnabled(true);
            diceButtons[row - 1][col - 1].setEnabled(true);//
            diceButtons[row - 1 ][col].setEnabled(true);
            }
             // location 3,0
            else if(row == 3 && col == 0)
             {
             diceButtons[row - 1][col + 1].setEnabled(true);
            diceButtons[row - 1][col].setEnabled(true);
            diceButtons[row ][col + 1].setEnabled(true); 
            }
            else if(row == 0 && col == 3)
             {
             diceButtons[row + 1][col - 1].setEnabled(true);
            diceButtons[row ][col - 1].setEnabled(true);
            diceButtons[row + 1 ][col].setEnabled(true);
             }
             
            
             
          
            
            else if(row == 0 && col > 0)
            {
            diceButtons[row][col + 1].setEnabled(true);
            diceButtons[row + 1][col + 1].setEnabled(true);
            diceButtons[row ][col].setEnabled(true);  
            diceButtons[row + 1][col - 1].setEnabled(true);
            diceButtons[row ][col - 1].setEnabled(true);
            }
            else if (row > 0 && col == 0)
            {
            diceButtons[row - 1][col + 1].setEnabled(true);
            diceButtons[row ][col + 1].setEnabled(true);
            diceButtons[row + 1 ][col + 1].setEnabled(true);  
            diceButtons[row + 1][col].setEnabled(true);
            diceButtons[row - 1][col].setEnabled(true);
            }
            //locations 1,3 and 2,3
            else if(row > 0 && col == 3)
            {
            diceButtons[row - 1][col].setEnabled(true);
            diceButtons[row + 1][col].setEnabled(true);
            diceButtons[row + 1 ][col - 1].setEnabled(true);  
            diceButtons[row ][col - 1].setEnabled(true);
            diceButtons[row - 1 ][col - 1].setEnabled(true);
            }
            // at locations 3,1 and 3,2 //check botton not working
            else if (row == 3 && col > 0)
            {
            diceButtons[row][col + 1].setEnabled(true);
            diceButtons[row - 1][col + 1].setEnabled(true);
            diceButtons[row - 1 ][col].setEnabled(true);  
            diceButtons[row - 1][col - 1].setEnabled(true);
            diceButtons[row ][col - 1].setEnabled(true);
            }
            // this is only available for row 1, 2 and col 1,2 ( 4 center dices)
            else if( row > 0 && col > 0)
            {
                //top row
            diceButtons[row - 1][col - 1].setEnabled(true);
            diceButtons[row - 1][col].setEnabled(true);
            diceButtons[row - 1 ][col + 1].setEnabled(true);
             //botton row
            diceButtons[row + 1][col - 1].setEnabled(true);
            diceButtons[row + 1][col].setEnabled(true);
            diceButtons[row + 1 ][col + 1].setEnabled(true);
            
            //current row
            diceButtons[row][col - 1].setEnabled(true);
            diceButtons[row][col + 1].setEnabled(true);
            }
            }
        }
        }              
    // inner class 9
    private class TimerListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            if(seconds == 0 && minutes == 0)
            {              
                timer.stop();
                JOptionPane.showMessageDialog(frame, "time is up! Game over!");
            }
            else
            {
                if(seconds == 0)
                {
                    seconds = 59;
                    minutes--;
                }
                else
                {
                    seconds--;
                }
            }

            if(seconds < 10)
            {
                String strSeconds = "0" + String.valueOf(seconds);
                timeLabel.setText(String.valueOf(minutes) + ":" + strSeconds);
            }
            else
            {
                timeLabel.setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
            }
           
        }
    }
}