/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import properties_manager.PropertiesManager;
import static zombiecrushsaga.ZombieCrushConstant.INVISIBLE_STATE;
import static zombiecrushsaga.ZombieCrushConstant.LOSS_DIALOG_TYPE;
import static zombiecrushsaga.ZombieCrushConstant.VISIBLE_STATE;
import zombiecrushsaga.data.ZombieCrushDataModel;
import zombiecrushsaga.ui.ZombieCrushMiniGame;

/**
 *
 * @author shan
 */
public class LossAndTryAgainHandler implements ActionListener{
  private  ZombieCrushMiniGame game;   
 
   PropertiesManager props = PropertiesManager.getPropertiesManager();
    public LossAndTryAgainHandler(ZombieCrushMiniGame miniGame){
        game = miniGame;
    
    }
    @Override
    public void actionPerformed(ActionEvent ae) {
         ZombieCrushDataModel data = (ZombieCrushDataModel) game.getDataModel();
        if (!data.inProgress()) {
            ((ZombieCrushDataModel) data).enableTiles(true);
            data.reset(game);
              game.getGUIDialogs().get(LOSS_DIALOG_TYPE).setState(INVISIBLE_STATE);
              data.resetGameData();
        }
game.switchToGameScreen();
      
    }
    
}
