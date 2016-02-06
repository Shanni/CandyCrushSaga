/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.data;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author shan
 */
public class ZombieCrushRecord {
    private HashMap<String, ZombieCrushLevelRecord> levelRecords;
    
    /**
     * Default constructor, it simply creates the hash table for
     * storing all the records stored by level.
     */
    public ZombieCrushRecord()
    {
        levelRecords = new HashMap();
    }

    // GET METHODS
public HashMap<String, ZombieCrushLevelRecord> getLevelRecord(){
    return levelRecords;
}
    /**
     * Adds the record for a level
     * 
     * @param levelName
     * 
     * @param rec 
     */
    public void addMahjongLevelRecord(String levelName, ZombieCrushLevelRecord rec)
    {
        levelRecords.put(levelName, rec);
    }
    
    /**
     * This method adds a win to the current player's record according
     * to the level being played.
     * 
     * @param levelName The level being played that the player won.
     * 
     */
    public void addGame(String levelName, int move, int score, int goal)
    {
        // GET THE RECORD FOR levelName
        ZombieCrushLevelRecord rec;
        rec = levelRecords.get(levelName);
        
        // IF THE PLAYER HAS NEVER PLAYED A GAME ON levelName
        if (rec == null)
        {
            // MAKE A NEW RECORD FOR THIS LEVEL, SINCE THIS IS
            // THE FIRST TIME WE'VE PLAYED IT
            rec = new ZombieCrushLevelRecord();
            rec.moves = move;
            rec.score = score;
            rec.highestScore=0;
           if(move>rec.threeStarScore){
               rec.stars=3;
           }else if(move>rec.twoStarScore){
               rec.stars=2;
           }else if(move>rec.oneStarScore){
               rec.stars=1;
           }
            if(rec.stars!=0){
                rec.highestScore=score;
            }
            levelRecords.put(levelName, rec);
        }
        else{
          
            rec.moves = move;
            rec.highestScore=score;
           if(move>rec.threeStarScore){
               rec.stars=3;
           }else if(move>rec.twoStarScore){
               rec.stars=2;
           }else if(move>rec.oneStarScore){
               rec.stars=1;
           }
            if(rec.stars!=0){
                
                  if (score > rec.highestScore)
                rec.highestScore = score;
            }
            
        }
           
            
        
    }
    
    public int getGameMove(String levelName) 
    {
        ZombieCrushLevelRecord rec = levelRecords.get(levelName);

        // IF levelName ISN'T IN THE RECORD OBJECT
        // THEN SIMPLY RETURN 0
        if (rec == null)
            return 0;
        // OTHERWISE RETURN THE GAMES PLAYED
        else
            return rec.moves; 
    }
    
//    public int getLevelGoalScore(String levelName){
//          ZombieCrushLevelRecord rec = levelRecords.get(levelName);
//        
//        // IF levelName ISN'T IN THE RECORD OBJECT
//        // THEN SIMPLY RETURN 0        
//        if (rec == null)
//            return 0;
//        // OTHERWISE RETURN THE WINS
//        else
//            return rec.goal; 
//    } 
    public int getStars(String levelName)
    {
       ZombieCrushLevelRecord rec = levelRecords.get(levelName);
        
        // IF levelName ISN'T IN THE RECORD OBJECT
        // THEN SIMPLY RETURN 0        
        if (rec == null)
            return 0;
        // OTHERWISE RETURN THE WINS
        else
            return rec.stars; 
    }
    
    /**
     * This method gets the losses for a given level.
     * 
     * @param levelName Level for the request.
     * 
     * @return The losses the player has earned for the levelName level.
     */      
//    public int getOneStarScore(String levelName)
//    {
//        ZombieCrushLevelRecord rec = levelRecords.get(levelName);
//
//        // IF levelName ISN'T IN THE RECORD OBJECT
//        // THEN SIMPLY RETURN 0
//        
//        if (rec == null)
//            return 0;
//        // OTHERWISE RETURN THE LOSSES
//        else
//            return rec.oneStarScore; 
//    }
    
    /**
     * This method gets the fastest time for a given level.
     * 
     * @param levelName Level for the request.
     * 
     * @return The fastest time the player has earned for the levelName level.
     */       
    public int getHighestScore(String levelName)
    {
        ZombieCrushLevelRecord rec = levelRecords.get(levelName);
        
        // IF THE PLAYER HAS NEVER PLAYED THAT LEVEL, RETURN
        // THE MAX AS A  FLAG
        if (rec == null)
            return 0;
        // OTHERWISE RETURN THE FASTEST TIME
        else
            return rec.highestScore; 
    }

     public int getScore(String levelName)
    {
        ZombieCrushLevelRecord rec = levelRecords.get(levelName);
      
        if (rec == null)
            return 0;
        // OTHERWISE RETURN THE FASTEST TIME
        else
            return rec.score; 
    }
    public byte[] toByteArray() throws IOException
    {
        Iterator<String> keysIt = levelRecords.keySet().iterator();
        int numLevels = levelRecords.keySet().size();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(numLevels);
        while(keysIt.hasNext())
        {
            String key = keysIt.next();
            dos.writeUTF(key);
            ZombieCrushLevelRecord rec = levelRecords.get(key);
            dos.writeInt(rec.moves);
            dos.writeInt(rec.score);
            dos.writeInt(rec.highestScore);
            dos.writeInt(rec.stars);
             dos.writeInt(rec.oneStarScore);
            dos.writeInt(rec.twoStarScore);
             dos.writeInt(rec.threeStarScore);
        }
        // AND THEN RETURN IT
        return baos.toByteArray();
    }
}
