/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import properties_manager.PropertiesManager;
import static zombiecrushsaga.ZombieCrushConstant.*;
import zombiecrushsaga.ZombieCrushSaga;
import zombiecrushsaga.ui.ZombieCrushMiniGame;

/**
 *
 * @author shan
 */
public class ScrollDownHandler implements ActionListener {

    private ZombieCrushMiniGame game;
    PropertiesManager props = PropertiesManager.getPropertiesManager();

    public ScrollDownHandler(ZombieCrushMiniGame aThis) {
        game = aThis;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (OFFSET +SCROLL_OFFSET < 3280) {
//      
           
            OFFSET += SCROLL_OFFSET;
            ArrayList<String> levelImageNames = props.getPropertyOptionsList(ZombieCrushSaga.ZombieCrushPropertyType.LEVEL_IMAGE_OPTIONS);
            for (String temp : levelImageNames) {
                
        
                game.getGUIButtons().get(temp).setY(game.getGUIButtons().get(temp).getY() - SCROLL_OFFSET);

//                if((game.getGUIButtons().get(temp).getY() -SCROLL_OFFSET)){
//                 game.getGUIButtons().get(temp).update(game);
//            if((game.getGUIButtons().get(temp).getY() + SCROLL_OFFSET)>=720)
                }
                 
            }
      }
       
}
        
    

