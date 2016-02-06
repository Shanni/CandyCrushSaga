package zombiecrushsaga.event;



import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import zombiecrushsaga.data.ZombieCrushDataModel;

import zombiecrushsaga.ui.ZombieCrushMiniGame;

/**
 * This event handler lets us provide additional custom responses
 * to key presses while  is running.
 * 
 * @author Shan Liu
 */
public class KeyEventHandler extends KeyAdapter
{
    // THE MAHJONG GAME ON WHICH WE'LL RESPOND
    private ZombieCrushMiniGame game;

    /**
     * This constructor simply inits the object by 
     * keeping the game for later.
     * 
     * @param initGame The Mahjong game that contains
     * the back button.
     */    
    public KeyEventHandler(ZombieCrushMiniGame initGame)
    {
        game = initGame;
    }
    
    /**
     * This method provides a custom game response to when the user
     * presses a keyboard key.
     * 
     * @param ke Event object containing information about the event,
     * like which key was pressed.
     */
    @Override
    public void keyPressed(KeyEvent ke)
    {
        // CHEAT BY ONE MOVE. NOTE THAT IF WE HOLD THE C
        // KEY DOWN IT WILL CONTINUALLY CHEAT
        if (ke.getKeyChar() == KeyEvent.VK_1)
        {
           ZombieCrushDataModel data = (ZombieCrushDataModel)game.getDataModel();
            // FIND A MOVE IF THERE IS ONE
          
            data.moveAllTilesToStack1();
            data.hardcode4();
         
          
      
        }
        if (ke.getKeyCode() == KeyEvent.VK_U)
        {
     
        }
    }
}