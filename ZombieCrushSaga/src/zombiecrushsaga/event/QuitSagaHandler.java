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
public class QuitSagaHandler implements ActionListener {
  private  ZombieCrushMiniGame game;   
public QuitSagaHandler(ZombieCrushMiniGame initMiniGame)
    {
       game = initMiniGame;
    }
    @Override
    public void actionPerformed(ActionEvent ae) {
        System.exit(0);
    }
}
