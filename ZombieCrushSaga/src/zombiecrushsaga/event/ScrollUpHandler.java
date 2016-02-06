/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import properties_manager.PropertiesManager;
import zombiecrushsaga.ui.ZombieCrushMiniGame;
import static zombiecrushsaga.ZombieCrushConstant.*;
import zombiecrushsaga.ZombieCrushSaga;

/**
 *
 * @author shan
 */
public class ScrollUpHandler implements ActionListener {

    private ZombieCrushMiniGame game;
    PropertiesManager props = PropertiesManager.getPropertiesManager();

    public ScrollUpHandler(ZombieCrushMiniGame game) {
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
////       
//    if (game.getGUIDecor().get(BACKGROUND_TYPE).getY() - OFFSET <=SCROLL_OFFSET ) {
//       ArrayList<String> levelImageNames = props.getPropertyOptionsList(ZombieCrushSaga.ZombieCrushPropertyType.LEVEL_IMAGE_OPTIONS);
//       for (String temp : levelImageNames) {
//         if((game.getGUIButtons().get(temp).getY() + SCROLL_OFFSET)<=0)
//          game.getGUIButtons().get(temp).
//           System.out.println(game.getGUIButtons().get(temp).getY());
//       }
//          DY = game.getGUIDecor().get(SAGA_SCREEN_STATE).getY() - SCROLL_OFFSET;
//          game.getGUIDecor().get(SAGA_SCREEN_STATE).setY(DY);
//   
//       
//           // OFFSET -= SCROLL_OFFSET;
////         }
//    //     System.out.println(game.getGUIBackground().get(BACKGROUND_TYPE).getY() );
//       
//        
//       }
//         
//    }
//}
//





 //    if (game.getGUIDecor().get(BACKGROUND_TYPE).getY() - OFFSET <=SCROLL_OFFSET ) {

        if (OFFSET -SCROLL_OFFSET>0) {
//      
           
            OFFSET -= SCROLL_OFFSET;
            ArrayList<String> levelImageNames = props.getPropertyOptionsList(ZombieCrushSaga.ZombieCrushPropertyType.LEVEL_IMAGE_OPTIONS);
            for (String temp : levelImageNames) {
                
        
                game.getGUIButtons().get(temp).setY(game.getGUIButtons().get(temp).getY() + SCROLL_OFFSET);

//                if((game.getGUIButtons().get(temp).getY() -SCROLL_OFFSET)){
//                 game.getGUIButtons().get(temp).update(game);
//            if((game.getGUIButtons().get(temp).getY() + SCROLL_OFFSET)>=720)
                }
                 
            }
      }
       
}
       