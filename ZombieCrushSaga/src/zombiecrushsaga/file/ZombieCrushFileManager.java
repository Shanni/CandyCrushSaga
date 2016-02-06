/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import properties_manager.PropertiesManager;
import zombiecrushsaga.ZombieCrushSaga.ZombieCrushPropertyType;
import zombiecrushsaga.data.ZombieCrushDataModel;
import zombiecrushsaga.data.ZombieCrushLevelRecord;
import zombiecrushsaga.data.ZombieCrushRecord;
import zombiecrushsaga.ui.ZombieCrushMiniGame;

/**This class provides services for efficiently loading and saving
 * binary files for the ZombieCrush Saga game application.
 * @author shan
 */
public class ZombieCrushFileManager {
    private ZombieCrushMiniGame miniGame;
     /**
     * Constructor for initializing this file manager, it simply keeps
     * the game for later.
     * 
     * @param initMiniGame The game for which this class loads data.
     */
    public ZombieCrushFileManager(ZombieCrushMiniGame initMiniGame)
    {
        // KEEP IT FOR LATER
        miniGame = initMiniGame;
    }
    /**
     * This method loads the contents of the levelFile argument so that
     * the player may then play that level. 
     * 
     * @param levelFile Level to load.
     */
    public void loadLevel(String levelFile)//file address 
    {
        // LOAD THE RAW DATA SO WE CAN USE IT
        // OUR LEVEL FILES WILL HAVE THE DIMENSIONS FIRST,
        // FOLLOWED BY THE GRID VALUES
        try
        {
            
            File fileToOpen = new File(levelFile);

            // LET'S USE A FAST LOADING TECHNIQUE. WE'LL LOAD ALL OF THE
            // BYTES AT ONCE INTO A BYTE ARRAY, AND THEN PICK THAT APART.
            // THIS IS FAST BECAUSE IT ONLY HAS TO DO FILE READING ONCE
            byte[] bytes = new byte[Long.valueOf(fileToOpen.length()).intValue()];
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            FileInputStream fis = new FileInputStream(fileToOpen);
            BufferedInputStream bis = new BufferedInputStream(fis);
            
            // HERE IT IS, THE ONLY READY REQUEST WE NEED
            bis.read(bytes);
            bis.close();
            
            // NOW WE NEED TO LOAD THE DATA FROM THE BYTE ARRAY
            DataInputStream dis = new DataInputStream(bais);
            
            // NOTE THAT WE NEED TO LOAD THE DATA IN THE SAME
            // ORDER AND FORMAT AS WE SAVED IT
           
            // FIRST READ THE GRID DIMENSIONS
            int initGridColumns = dis.readInt();
            int initGridRows = dis.readInt();
            int[][] newGrid = new int[initGridColumns][initGridRows];
            
            // AND NOW ALL THE CELL VALUES
            for (int i = 0; i < initGridColumns; i++)
            {                        
                for (int j = 0; j < initGridRows; j++)
                {
                    newGrid[i][j] = dis.readInt();
                }
            }
           
            // EVERYTHING WENT AS PLANNED SO LET'S MAKE IT PERMANENT
            ZombieCrushDataModel dataModel = (ZombieCrushDataModel)miniGame.getDataModel();
            dataModel.initLevelGrid(newGrid, initGridColumns, initGridRows);
            dataModel.setCurrentLevel(levelFile);
            
            miniGame.updateBoundaries();
            
        }
        catch(Exception e)
        {
            // LEVEL LOADING ERROR
            miniGame.getErrorHandler().processError(ZombieCrushPropertyType.LOAD_LEVEL_ERROR);
        }
    }   
    
    public void loadLevelInfo(String levelFile) {
        try {
              File fileToOpen = new File(levelFile);
        
              
                FileReader read=new FileReader(fileToOpen);
            try (BufferedReader buffer = new BufferedReader(read)) {
                ZombieCrushDataModel data= (ZombieCrushDataModel) miniGame.getDataModel();
                
                data.setTargetScore(Integer.parseInt(buffer.readLine()));
                data.setStar2Score(Integer.parseInt(buffer.readLine()));
                data.setStar3Score(Integer.parseInt(buffer.readLine()));
                data.setMoveCount(Integer.parseInt(buffer.readLine()));
            }
                 
            } catch (Exception ex) {
        Logger.getLogger(ZombieCrushFileManager.class.getName()).log(Level.SEVERE, null, ex);

        }
           
  
                
      
    }
     /**
     * This method loads the player record from the records file
     * so that the user may view stats.
     * 
     * @return The fully loaded record from the player record file.
     */
    public ZombieCrushRecord loadRecord()
    {
        ZombieCrushRecord recordToLoad = new ZombieCrushRecord();
        
        // LOAD THE RAW DATA SO WE CAN USE IT
        // OUR LEVEL FILES WILL HAVE THE DIMENSIONS FIRST,
        // FOLLOWED BY THE GRID VALUES
        try
        {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String dataPath = props.getProperty(ZombieCrushPropertyType.DATA_PATH);
            String recordPath = dataPath + props.getProperty(ZombieCrushPropertyType.RECORD_FILE_NAME);
            File fileToOpen = new File(recordPath);
       
            if(fileToOpen.exists()){
                FileReader read=new FileReader(fileToOpen);
                BufferedReader buffer=new BufferedReader(read);
                String numOfLevels = buffer.readLine();
                int n = 0;
               if(numOfLevels!=null){
                   n = Integer.parseInt(numOfLevels);
               }
               for(int i = 0;i < n;i++){
                   String name= buffer.readLine();
                   ZombieCrushLevelRecord level= new ZombieCrushLevelRecord();
                   level.score=Integer.parseInt(buffer.readLine());
                   level.moves=Integer.parseInt(buffer.readLine());
                   level.stars=Integer.parseInt(buffer.readLine());
                   recordToLoad.addMahjongLevelRecord(name, level);
               }
               buffer.close();
            }
           

            
            // LET'S USE A FAST LOADING TECHNIQUE. WE'LL LOAD ALL OF THE
            // BYTES AT ONCE INTO A BYTE ARRAY, AND THEN PICK THAT APART.
            // THIS IS FAST BECAUSE IT ONLY HAS TO DO FILE READING ONCE
          //  byte[] bytes = new byte[Long.valueOf(fileToOpen.length()).intValue()];
          // ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
         //   FileInputStream fis = new FileInputStream(fileToOpen);
           // BufferedInputStream bis = new BufferedInputStream(fis);
            
            // HERE IT IS, THE ONLY READY REQUEST WE NEED
      //      bis.read(bytes);
          //  bis.read();
        //    bis.close();
            
            // NOW WE NEED TO LOAD THE DATA FROM THE BYTE ARRAY
      //      DataInputStream dis = new DataInputStream(bais);
          //    DataInputStream dis = new DataInputStream(fis);
            // NOTE THAT WE NEED TO LOAD THE DATA IN THE SAME
            // ORDER AND FORMAT AS WE SAVED IT
            // FIRST READ THE NUMBER OF LEVELS
    /*        int numLevels = dis.readInt();

            for (int i = 0; i < numLevels; i++)
            {
                String levelName = dis.readUTF();
                ZombieCrushLevelRecord rec = new ZombieCrushLevelRecord();
               
                // NEED TO EDIT LATER!!! @SHAN
                
                rec.goal = dis.readInt();
                rec.highestScore = dis.readInt();
              
                rec.oneStarScore = dis.readInt();
                rec.twoStarScore = dis.readInt();
                rec.threeStarScore= dis.readInt();
                recordToLoad.addMahjongLevelRecord(levelName, rec);
            }   */              
            
        }
        catch(Exception e)
        {
            // THERE WAS NO RECORD TO LOAD, SO WE'LL JUST RETURN AN
            // EMPTY ONE AND SQUELCH THIS EXCEPTION
        }        
        return recordToLoad;
    }
    


public ZombieCrushRecord loadRecordb(){
    ZombieCrushRecord recordToLoad = new ZombieCrushRecord();
        
        // LOAD THE RAW DATA SO WE CAN USE IT
        // OUR LEVEL FILES WILL HAVE THE DIMENSIONS FIRST,
        // FOLLOWED BY THE GRID VALUES
        try
        {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String dataPath = props.getProperty(ZombieCrushPropertyType.DATA_PATH);
            String recordPath = dataPath + props.getProperty(ZombieCrushPropertyType.RECORD_FILE_NAMEb);
            File fileToOpen = new File(recordPath);
   // LET'S USE A FAST LOADING TECHNIQUE. WE'LL LOAD ALL OF THE
            // BYTES AT ONCE INTO A BYTE ARRAY, AND THEN PICK THAT APART.
//             THIS IS FAST BECAUSE IT ONLY HAS TO DO FILE READING ONCE
            byte[] bytes = new byte[Long.valueOf(fileToOpen.length()).intValue()];
           ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            FileInputStream fis = new FileInputStream(fileToOpen);
            BufferedInputStream bis = new BufferedInputStream(fis);
            
            // HERE IT IS, THE ONLY READY REQUEST WE NEED
            bis.read(bytes);
            bis.close();
            
            // NOW WE NEED TO LOAD THE DATA FROM THE BYTE ARRAY
            DataInputStream dis = new DataInputStream(bais);
            
            // NOTE THAT WE NEED TO LOAD THE DATA IN THE SAME
            // ORDER AND FORMAT AS WE SAVED IT
            // FIRST READ THE NUMBER OF LEVELS
            int numLevels = dis.readInt();

            for (int i = 0; i < numLevels; i++)
            {
             
                String levelName = dis.readUTF();
                ZombieCrushLevelRecord rec = new ZombieCrushLevelRecord();
                rec.moves=dis.readInt();
                rec.score = dis.readInt();
                rec.highestScore = dis.readInt();
                rec.stars = dis.readInt();
                rec.oneStarScore=dis.readInt();
                rec.twoStarScore=dis.readInt();
                rec.threeStarScore=dis.readInt();
         
                recordToLoad.addMahjongLevelRecord(levelName, rec);
            }              
            
        }
        catch(Exception e)
        {
            // THERE WAS NO RECORD TO LOAD, SO WE'LL JUST RETURN AN
            // EMPTY ONE AND SQUELCH THIS EXCEPTION
        }        
        return recordToLoad;
    }
    
}


