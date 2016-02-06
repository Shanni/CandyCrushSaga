/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import properties_manager.PropertiesManager;
import zombiecrushsaga.ZombieCrushConstant;
import static zombiecrushsaga.ZombieCrushConstant.LEVELFILE;
import static zombiecrushsaga.ZombieCrushConstant.RECORD_SCREEN_STATE;
import static zombiecrushsaga.ZombieCrushConstant.*;
import zombiecrushsaga.data.ZombieCrushDataModel;
import zombiecrushsaga.file.ZombieCrushFileManager;
import zombiecrushsaga.ui.ZombieCrushMiniGame;

/**
 *
 * @author shan
 */
public class LevelScoreHandler implements ActionListener{
  private  ZombieCrushMiniGame game;   
  String levelfile;
   PropertiesManager props = PropertiesManager.getPropertiesManager();
    public LevelScoreHandler(ZombieCrushMiniGame miniGame, String levelFile){
        game = miniGame;
        levelfile=levelFile;
    }
    @Override
    public void actionPerformed(ActionEvent ae) {
          ZombieCrushConstant.LEVELFILE=levelfile;
       if (ZombieCrushConstant.LEVELFILE!=null){
            String Level = ZombieCrushConstant.LEVELFILE;
            String[] level =Level.split("/");
            String LEVEL = level[5];
            String level1 = LEVEL.substring(0,LEVEL.length()-4);
            ZombieCrushConstant.LEVELFILE=level1+".zom";
            
        if (game.isCurrentScreenState(SAGA_SCREEN_STATE)) {
            ZombieCrushDataModel data = (ZombieCrushDataModel) game.getDataModel();
            ZombieCrushFileManager fileManager = game.getFileManager();
            if (ZombieCrushConstant.LEVELFILE != null) {
                String LLevel = LEVELFILE;
                
              //  if (Level.equals("LEVEL2.zom")) {
                    //    fileManager.loadLevel(levelFile);
                    fileManager.loadLevel("./data/zomcrush/"+LLevel);
                    fileManager.loadLevelInfo("./data/"+level1+".txt");

                    
           //     }
           
          game.switchToLevelScoreScreen();
        
            }
        }
    
     
       }
      
    }
    
}
