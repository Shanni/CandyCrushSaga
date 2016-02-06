/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import zombiecrushsaga.ui.ZombieCrushMiniGame;

/**
 *
 * @author shan
 */
public class BackGameHandler implements ActionListener{
 private  ZombieCrushMiniGame game;
    public BackGameHandler(ZombieCrushMiniGame aThis) {
    game=aThis;    
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if(game.getDataModel().inProgress()){
            game.getDataModel().pause();
        }
        game.switchToLevelScoreScreen();
    }
    
}
