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
public class BackLevelScoreHandler implements ActionListener {
 private  ZombieCrushMiniGame game;
    public BackLevelScoreHandler(ZombieCrushMiniGame aThis) {
        game=aThis;  
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        game.switchToSagaScreen();
    }
    
}
