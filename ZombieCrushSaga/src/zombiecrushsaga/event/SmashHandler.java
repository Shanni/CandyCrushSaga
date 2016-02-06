/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import zombiecrushsaga.data.ZombieCrushDataModel;
import zombiecrushsaga.ui.ZombieCrushMiniGame;

/**
 * This event handler responds to when the user requests to
 * start a new game. 
 * @author shan
 */
public class SmashHandler implements ActionListener{
  // HERE'S THE GAME WE'LL UPDATE
    private  ZombieCrushMiniGame game;
    
    /**
     * This constructor just stores the game for later.
     * 
     * @param initGame the game to update
     */
    public SmashHandler( ZombieCrushMiniGame initGame)
    {
        game = initGame;
    }
    
    /**
     * Here is the event response. This code is executed when
     * the user clicks on the button for starting a new game,
     * which can be done when the application starts up, during
     * a game, or after a game has been played. Note that the game 
     * data is already locked for this thread before it is called, 
     * and that it will be unlocked after it returns.
     * 
     * @param ae the event object for the button press
     */
        public void actionPerformed(ActionEvent ae)
    {
        ZombieCrushDataModel data = (ZombieCrushDataModel)game.getDataModel();
        data.smashTile();
       
    }    
}
