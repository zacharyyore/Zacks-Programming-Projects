package game;

import dictionary.Alphabet;
import dictionary.Dictionary;
import model.GridPoint;
import model.GridUnit;

public class Game 
{
    private final GridUnit[][] grid;
    private final Dictionary dictionary;

    public Game(Dictionary dictionary) 
    {
        this.dictionary = dictionary;
        this.grid = new GridUnit[4][4];
        this.populateGrid();
    }

    public GridUnit[][] getGrid() 
    {
        return grid;
    }

    public GridUnit getGridUnit(GridPoint point) 
    {
        return grid[point.x][point.y];
    }

    public void populateGrid() 
    {
        for (int i = 0; i < 4; ++i) 
        {
            for (int j = 0; j < 4; ++j) 
            {
                grid[i][j] = new GridUnit(Alphabet.newRandom(), new GridPoint(i, j));
            }
        }
    }
    
    public void displayGrid()
    {
        System.out.println("-------------------------");
        for (int i = 0; i < 4; ++i) 
        {
            System.out.print("|  ");
            
            for (int j = 0; j < 4; ++j) 
            {
                System.out.print(grid[i][j].getLetter());
                System.out.print("  |  ");
            }
            
            System.out.println("\n-------------------------");
        }        
    }

    /**
     * @return the dictionary
     */
    public Dictionary getDictionary() {
        return dictionary;
    }
}
