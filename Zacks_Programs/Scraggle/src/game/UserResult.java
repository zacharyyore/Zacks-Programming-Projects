package game;

import model.WordResult;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserResult 
{
    private final Map<String, WordResult> wordToResultMap = new LinkedHashMap<>();
    private int totalScore = 0;

    public void add(String word, WordResult result) 
    {
        this.wordToResultMap.put(word, result);
        this.totalScore += result.getScore();
    }

    public WordResult get(String word) 
    {
        return this.wordToResultMap.get(word);
    }

    public Map<String, WordResult> all() 
    {
        return this.wordToResultMap;
    }

    public boolean exists(String word) 
    {
        return this.wordToResultMap.containsKey(word);
    }

    public int getTotalScore() 
    {
        return this.totalScore;
    }

    // shadowing when the parameter is the same name as a member
    // variable, use the keyword this to differentiate between 
    // the two variables
    public void setTotalScore(int totalScore) 
    {
        this.totalScore = totalScore;
    }
    
    public int getWordCount() 
    {
        return this.wordToResultMap.size();
    }
}
