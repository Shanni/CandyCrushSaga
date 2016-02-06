/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import zombiecrushsaga.data.ZombieCrushLevelRecord;
import zombiecrushsaga.ui.ZombieCrushMiniGame;

/**
 * * This class manages when the user clicks the window X to
 * kill the application.
 * 
 * @author shan
 */
public class QuitGameHandler extends WindowAdapter implements ActionListener
{
    private ZombieCrushMiniGame miniGame;
    
    public QuitGameHandler(ZombieCrushMiniGame initMiniGame)
    {
        miniGame = initMiniGame;
    }
    
    /**
     * This method is called when the user clicks the window'w X. We 
     * respond by giving the player a loss if the game is still going on.
     * 
     * @param we Window event object.
     */
    @Override
    public void windowClosing(WindowEvent we)
    {
        // IF THE GAME IS STILL GOING ON, END IT AS A LOSS
        if (miniGame.getDataModel().inProgress())
            
        {
          //  HashMap<String, ZombieCrushLevelRecord levelrecord=miniGame.getPlayerRecord().getLevelRecord().get(we);
            miniGame.getDataModel().endGameAsLoss();
            miniGame.savePlayerRecordb();
        }
        // AND CLOSE THE ALL
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        System.exit(0);
    }
}

