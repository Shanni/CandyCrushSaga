/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.event;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import zombiecrushsaga.ZombieCrushConstant;
import static zombiecrushsaga.ZombieCrushConstant.RECORD_SCREEN_STATE;
import zombiecrushsaga.data.ZombieCrushDataModel;
import zombiecrushsaga.file.ZombieCrushFileManager;
import zombiecrushsaga.ui.ZombieCrushMiniGame;

/**
 *
 * @author shan
 */
public class PlayLevelScoreHandler implements ActionListener {

    private ZombieCrushMiniGame game;
    private String levelFile;

    public PlayLevelScoreHandler(ZombieCrushMiniGame aThis, String initLevelFile) {
        game = aThis;
        levelFile = initLevelFile;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
          if(game.getDataModel().inProgress()){
            game.getDataModel().unpause();
        }

        if (game.isCurrentScreenState(RECORD_SCREEN_STATE)) {
//            ZombieCrushDataModel data = (ZombieCrushDataModel) game.getDataModel();
//            ZombieCrushFileManager fileManager = game.getFileManager();
//            if (ZombieCrushConstant.LEVELFILE != null) {
//                String Level = ZombieCrushConstant.LEVELFILE;
//         
//              //  if (Level.equals("LEVEL2.zom")) {
//                    //    fileManager.loadLevel(levelFile);
//                    fileManager.loadLevel("./data/zomcrush/"+Level);

                    game.switchToGameScreen();
           //     }
      //      }

        }
    }
}
