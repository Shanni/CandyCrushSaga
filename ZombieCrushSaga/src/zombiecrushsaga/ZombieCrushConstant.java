/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zombiecrushsaga;

import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author shanliu
 */
public class ZombieCrushConstant {
     // TILE SPRITE TYPES
    public static final String REGULAR_TYPE = "REGULAR_TYPE";
    public static final String S4H_TYPE = "S4H_TYPE";
public static final String S4V_TYPE = "S4V_TYPE";
public static final String S5_TYPE = "S5_TYPE";
    public static final String T_TYPE = "T_TYPE";
   public static final String L_TYPE = "L_TYPE";
    public static final String TILE_SPRITE_TYPE_POSTFIX = "z";
    public static final String TILE_SPRITE_STRIPE_TYPE_POSTFIX = "zs";
    
    // EACH SCREEN HAS ITS OWN BACKGROUND TYPE
    public static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";
    
    // THIS REPRESENTS THE BUTTONS ON THE SPLASH SCREEN FOR LEVEL SELECTION
    public static final String LEVEL_SELECT_BUTTON_TYPE = "LEVEL_SELECT_BUTTON_TYPE";

    // IN-GAME UI CONTROL TYPES SPLASH SCREEN
    public static final String PLAY_GAME_BUTTON_TYPE = "PLAY_GAME_BUTTON_TYPE";
    public static final String RESET_BUTTON_TYPE = "RESET_BUTTON_TYPE";
    public static final String QUIT_BUTTON_TYPE = "QUIT_BUTTON_TYPE";
   
    public static final String SCROLLUP_BUTTON_TYPE = "SCROLLUP_BUTTON_TYPE";
    public static final String SCROLLDOWN_BUTTON_TYPE = "SCROLLDOWN_BUTTON_TYPE";
   // public static final String BACK_BUTTON_TYPE = "BACK_BUTTON_TYPE";
    public static final String QUITSAGA_BUTTON_TYPE = "QUITSAGA_BUTTON_TYPE";
    public static final String PLAYSAGA1_BUTTON_TYPE = "PLAYSAGA1_BUTTON_TYPE";
   
    public static final String BACK_LEVEL_BUTTON_TYPE = "BACK_LEVEL_BUTTON_TYPE";       
    public static final String PLAYLEVELSCORE_BUTTON_TYPE = "PLAYLEVELSCORE_BUTTON_TYPE";
    public static final String QUITLEVELSCORE_BUTTON_TYPE = "QUITLEVELSCORE_BUTTON_TYPE";
    
    public static final String BACKGAME_BUTTON_TYPE = "BACKGAME_BUTTON_TYPE";
    public static final String SMASH_BUTTON_TYPE ="SMASH_BUTTON_TYPE";
     public static final String TRY_AGAIN_BUTTON_TYPE="TRY_AGAIN_BUTTON_TYPE";
    // DIALOG TYPES
    public static final String STATS_STAR_TYPE = "STATS_STAR_TYPE";
    public static final String WIN_DIALOG_TYPE = "WIN_DIALOG_TYPE";
    public static final String LOSS_DIALOG_TYPE = "LOSS_DIALOG_TYPE";

    public static final String STATS_STAR1_TYPE = "STATS_STAR1_TYPE";
    public static final String STATS_STAR2_TYPE = "STATS_STAR2_TYPE";
    public static final String STATS_STAR3_TYPE = "STATS_STAR3_TYPE";

   public static final String METER_TYPE="METER_TYPE";
   public static final String STAR_TYPE="STAR_TYPE";
    
    // WE'LL USE THESE STATES TO CONTROL SWITCHING BETWEEN THE four
    public static final String SPLASH_SCREEN_STATE = "SPLASH_SCREEN_STATE";
    public static final String SAGA_SCREEN_STATE = "SAGA_SCREEN_STATE";
    public static final String GAME_SCREEN_STATE = "GAME_SCREEN_STATE";
    public static final String RECORD_SCREEN_STATE = "RECORD_SCREEN_STATE";
    

    // THE TILES MAY HAVE 4 STATES:
        // - INVISIBLE_STATE: USED WHEN ON THE SPLASH SCREEN, MEANS A TILE
            // IS NOT DRAWN AND CANNOT BE CLICKED
        // - VISIBLE_STATE: USED WHEN ON THE GAME SCREEN, MEANS A TILE
            // IS VISIBLE AND CAN BE CLICKED (TO SELECT IT), BUT IS NOT CURRENTLY SELECTED
        // - SELECTED_STATE: USED WHEN ON THE GAME SCREEN, MEANS A TILE
            // IS VISIBLE AND CAN BE CLICKED (TO UNSELECT IT), AND IS CURRENTLY SELECTED     
        // - NOT_AVAILABLE_STATE: USED FOR A TILE THE USER HAS CLICKED ON THAT
            // IS NOT FREE. THIS LET'S US GIVE THE USER SOME FEEDBACK
    public static final String INVISIBLE_STATE = "INVISIBLE_STATE";
    public static final String VISIBLE_STATE = "VISIBLE_STATE";
    public static final String SELECTED_STATE = "SELECTED_STATE";
    public static final String SMASH_STATE = "SMASH_STATE";
    public static final String MOUSE_OVER_STATE = "MOUSE_OVER_STATE";
    public static final String LOCK_STATE = "LOCK_STATE";

    public static String LEVELFILE="LEVEL1.zom";
    // THE BUTTONS MAY HAVE 2 STATES:
        // - INVISIBLE_STATE: MEANS A BUTTON IS NOT DRAWN AND CAN'T BE CLICKED
        // - VISIBLE_STATE: MEANS A BUTTON IS DRAWN AND CAN BE CLICKED
        // - MOUSE_OVER_STATE: MEANS A BUTTON IS DRAWN WITH SOME HIGHLIGHTING
            // BECAUSE THE MOUSE IS HOVERING OVER THE BUTTON

    // UI CONTROL SIZE AND POSITION SETTINGS
    
    // OR POSITIONING THE LEVEL SELECT BUTTONS
    public static final int LEVEL_BUTTON_WIDTH = 100;
    public static final int LEVEL_BUTTON_MARGIN = 5;
    public static final int LEVEL_BUTTON_Y = 570;

    // FOR STACKING TILES ON THE GRID
    public static final int NUM_TILES = 144;
    public static final int TILE_IMAGE_OFFSET = 1;
    public static final int TILE_IMAGE_WIDTH = 55;
    public static final int TILE_IMAGE_HEIGHT = 55;
    public static final int Z_TILE_OFFSET = 5;

    // FOR MOVING TILES AROUND
    public static final int MAX_TILE_VELOCITY = 15;
    public static final int MIN_TILE_VELOCITY = 10;
    
    // UI CONTROLS POSITIONS IN THE GAME SCREEN
    public static final int CLOSE_BUTTON_X = 900;
    public static final int CLOSE_BUTTON_Y =0;
    public static final int PLAY_BUTTON_X = 200;
    public static final int PLAY_BUTTON_Y = 600;
    public static final int RESET_BUTTON_X = 500;
    public static final int RESET_BUTTON_Y = 600;
    public static final int QUIT_BUTTON_X = 800;
    public static final int QUIT_BUTTON_Y = 600;
    public static int OFFSET = 3280; //4000-720=3280
    public static final int SCROLL_OFFSET = 300;
    public static float DY = 0;
    public static final int SCROLL_VELOCITY=20;
    
    public static final int LEVEL_BUTTON_Y_OFFSET = 30;
    public static final int LEVEL_BUTTON_X_OFFSET = 100;
    
    
    
  
   
    public static final int EXITSAGA_BUTTON_X = 1200;
    public static final int EXITSAGA_BUTTON_Y = 30;
    public static final int SCROLLUP_BUTTON_X = 1100;
    public static final int SCROLLUP_BUTTON_Y = 200;
     public static final int SCROLLDOWN_BUTTON_X = 1100;
    public static final int SCROLLDOWN_BUTTON_Y = 400;
    
     public static final int PLAYSAGA_BUTTON_X = 900;
    public static final int PLAYSAGA_BUTTON_Y = 600;
    public static final int BACKLEVEL_BUTTON_X = 1100;
    public static final int BACKLEVEL_BUTTON_Y = 600;
    public static final int QUITLEVELSCORE_BUTTON_X =1200;
    public static final int QUITLEVELSCORE_BUTTON_y =10;
    
     public static final int BACKGAME_BUTTON_X = 1000;
    public static final int BACKGAME_BUTTON_Y = 600;
     
    
    public static final int CONTROLS_MARGIN = 0;
    public static final int NEW_BUTTON_X = 0;
    public static final int NEW_BUTTON_Y = 0;
    public static final int BACK_X = NEW_BUTTON_X+130+CONTROLS_MARGIN;
    public static final int BACK_Y = 0;
    public static final int TILE_OFFSET = 140;
    public static final int TILE_TEXT_OFFSET = 60;
    public static final int TILE_X = BACK_X + 130 + CONTROLS_MARGIN;
    public static final int TILE_Y = 0;
    public static final int TIME_X = TILE_X + 230 + CONTROLS_MARGIN;
    public static final int TIME_Y = 0;
    public static final int TIME_OFFSET = 130;
    public static final int TIME_TEXT_OFFSET = 55;
    public static final int STATS_X = TIME_X + 310 + CONTROLS_MARGIN;
    public static final int STATS_Y = 0;
    public static final int UNDO_X = STATS_X + 160 + CONTROLS_MARGIN;
    public static final int UNDO_Y = 0;
    public static final int TILE_STACK_X = UNDO_X + 130 + CONTROLS_MARGIN;
    public static final int TILE_STACK_Y = 0;
    public static final int TILE_STACK_OFFSET_X = 30;
    public static final int TILE_STACK_OFFSET_Y = 12;
    public static final int TILE_STACK_2_OFFSET_X = 105;
    public static final int STATS_OFFSET_Y = 40;
    
    // THESE ARE USED FOR FORMATTING THE TIME OF GAME
    public static final long MILLIS_IN_A_SECOND = 1000;
    public static final long MILLIS_IN_A_MINUTE = 1000 * 60;
    public static final long MILLIS_IN_AN_HOUR  = 1000 * 60 * 60;

    // USED FOR DOING OUR VICTORY ANIMATION
    public static final int WIN_PATH_NODES = 100;
    public static final int WIN_PATH_TOLERANCE = 100;
    public static final int WIN_PATH_COORD = 100;

    // COLORS USED FOR RENDERING VARIOUS THINGS, INCLUDING THE
    // COLOR KEY, WHICH REFERS TO THE COLOR TO IGNORE WHEN
    // LOADING ART.
    public static final Color COLOR_KEY = new Color(255, 174, 201);
    public static final Color DEBUG_TEXT_COLOR = Color.BLACK;
    public static final Color TEXT_DISPLAY_COLOR = new Color (10, 160, 10);
    public static final Color SELECTED_TILE_COLOR = new Color(255,255,0,100);
    public static final Color INCORRECTLY_SELECTED_TILE_COLOR = new Color(255, 50, 50, 100);
    public static final Color STATS_COLOR = new Color(0, 60, 0);

    // FONTS USED DURING FOR TEXTUAL GAME DISPLAYS
    public static final Font TEXT_DISPLAY_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 48);
    public static final Font DEBUG_TEXT_FONT = new Font(Font.MONOSPACED, Font.BOLD, 14);
    public static final Font STATS_FONT = new Font(Font.MONOSPACED, Font.BOLD, 24);
    
    // AND AUDIO STUFF
    public static final String SUCCESS_AUDIO_TYPE = "SUCCESS_AUDIO_TYPE";
    public static final String FAILURE_AUDIO_TYPE = "FAILURE_AUDIO_TYPE";
    public static final String THEME_SONG_TYPE = "THEME_SONG_TYPE";
    
    
      // WE ONLY HAVE A LIMITIED NUMBER OF UI COMPONENT TYPES IN THIS APP
    
    // TILE SPRITE TYPES
    public static final String TILE_A_TYPE = "TILE_A_TYPE";
    public static final String TILE_B_TYPE = "TILE_B_TYPE";
    public static final String TILE_C_TYPE = "TILE_C_TYPE";
    public static final String TILE_SPRITE_TYPE_PREFIX = "TILE_";
}
