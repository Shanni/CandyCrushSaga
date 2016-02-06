/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga;

import javax.swing.JFrame;
import zombiecrushsaga.ui.ZombieCrushMiniGame;
import xml_utilities.InvalidXMLFileFormatException;
import properties_manager.PropertiesManager;
import zombiecrushsaga.ui.ZombieCrushErrorHandler;

/**
 * Zombie Crush is a game application that's ready to be customized to play
 * different flavors of the game. It has been setup using art from Plants vs.
 * Zombies (i.e. Zomcrush)
 *
 * @author Shan Liu
 */
public class ZombieCrushSaga {

    // THIS HAS THE FULL USER INTERFACE AND ONCE IN EVENT
    // HANDLING MODE, BASICALLY IT BECOMES THE FOCAL
    // POINT, RUNNING THE UI AND EVERYTHING ELSE
    static ZombieCrushMiniGame miniGame = new ZombieCrushMiniGame();

    // WE'LL LOAD ALL THE UI AND ART PROPERTIES FROM FILES,
    // BUT WE'LL NEED THESE VALUES TO START THE PROCESS
    static String PROPERTY_TYPES_LIST = "property_types.txt";
    static String UI_PROPERTIES_FILE_NAME = "properties.xml";
    static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";
    static String DATA_PATH = "./data/";

    /**
     * This is where the Zombie Crush game application starts execution. We'll
     * load the application properties and then use them to build our user
     * interface and start the window in event handling mode. Once in that mode,
     * all code execution will happen in response to a user request.
     */
    public static void main(String[] args) {
        new JFrame();
        try {
            // LOAD THE SETTINGS FOR STARTING THE APP
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(ZombieCrushPropertyType.UI_PROPERTIES_FILE_NAME, UI_PROPERTIES_FILE_NAME);
            props.addProperty(ZombieCrushPropertyType.PROPERTIES_SCHEMA_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME);
            props.addProperty(ZombieCrushPropertyType.DATA_PATH.toString(), DATA_PATH);
            props.loadProperties(UI_PROPERTIES_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME);

            // THEN WE'LL LOAD THE MAHJONG FLAVOR AS SPECIFIED BY THE PROPERTIES FILE
            String gameFlavorFile = props.getProperty(ZombieCrushPropertyType.GAME_FLAVOR_FILE_NAME);
            props.loadProperties(gameFlavorFile, PROPERTIES_SCHEMA_FILE_NAME);

            // NOW WE CAN LOAD THE UI, WHICH WILL USE ALL THE FLAVORED CONTENT
            String appTitle = props.getProperty(ZombieCrushPropertyType.GAME_TITLE_TEXT);
            int fps = Integer.parseInt(props.getProperty(ZombieCrushPropertyType.FPS));
            miniGame.initMiniGame(appTitle, fps);
            miniGame.startGame();
        } // THERE WAS A PROBLEM LOADING THE PROPERTIES FILE
        catch (InvalidXMLFileFormatException ixmlffe) {
            // LET THE ERROR HANDLER PROVIDE THE RESPONSE
            ZombieCrushErrorHandler errorHandler = miniGame.getErrorHandler();
            errorHandler.processError(ZombieCrushPropertyType.INVALID_XML_FILE_ERROR_TEXT);
        }
    }

    /**
     * ZombieCrushPropertyType represents the types of data that will need to be
     * extracted from XML files.
     */
    public enum ZombieCrushPropertyType {

        S4H_TYPE,
        S4V_TYPE,
        S5_TYPE,
        T_TYPE,
        TILE_C_TYPE,
        
        S4H_TYPE_TILE,
        S4V_TYPE_TILE,
        S5_TYPE_TILE,
        T_TYPE_TILE,
       
        TYPE_C_TILES,
        
        /* SETUP FILE NAMES */
        UI_PROPERTIES_FILE_NAME,
        PROPERTIES_SCHEMA_FILE_NAME,
        GAME_FLAVOR_FILE_NAME,
        RECORD_FILE_NAME,
        RECORD_FILE_NAMEb,
        /* DIRECTORIES FOR FILE LOADING */
        AUDIO_PATH,
        DATA_PATH,
        IMG_PATH,
        /* WINDOW DIMENSIONS & FRAME RATE */
        WINDOW_WIDTH,
        WINDOW_HEIGHT,
        FPS,
        GAME_WIDTH,
        GAME_HEIGHT,
        GAME_LEFT_OFFSET,
        GAME_TOP_OFFSET,
        /* GAME TEXT */
        GAME_TITLE_TEXT,
        EXIT_REQUEST_TEXT,
        INVALID_XML_FILE_ERROR_TEXT,
        ERROR_DIALOG_TITLE_TEXT,
        /* ERROR TYPES */
        AUDIO_FILE_ERROR,
        LOAD_LEVEL_ERROR,
        RECORD_SAVE_ERROR,
        /*
         * SELECT TILE
         */
        BLANK_TILE_IMAGE_NAME,
        BLANK_TILE_SELECTED_IMAGE_NAME,
        /* IMAGE FILE NAMES */
        WINDOW_ICON,
        SAGA_SCREEN_IMAGE_NAME,
        SPLASH_SCREEN_IMAGE_NAME,
        GAME_BACKGROUND_IMAGE_NAME,
        RECORD_SCREEN_IMAGE_NAME,
        /*
         *ON SPLASH SCREEN
         */
        PLAY_GAME_BUTTON_IMAGE_NAME,
        PLAY_GAME_BUTTON_MOUSE_OVER_IMAGE_NAME,
        RESET_BUTTON_IMAGE_NAME,
        RESET_BUTTON_MOUSE_OVER_IMAGE_NAME,
        QUIT_BUTTON_IMAGE_NAME,
        QUIT_BUTTON_MOUSE_OVER_IMAGE_NAME,
        /*
         * ON SAGA SCREEN
         */
        EXTI_BUTTON_IMAGE_NAME,
        EXTI_BUTTON_IMAGE_NAME_MOUSE_OVER,
        SCROLLUP_BUTTON_IMAGE_NAME,
        SCROLLDOWN_BUTTON_IMAGE_NAME,
        SCROLLUP_BUTTON_MOUSE_OVER_IMAGE_NAME,
        SCROLLDOWN_BUTTON_MOUSE_OVER_IMAGE_NAME,
        /*
         *  ON LEVELSCORE SCREEN
         */
        PLAY_ZOMCRUSH_IMAGE_NAME,
        PLAY_ZOMCRUSH_IMAGE_NAME_MOUSE_OVER,
        BACK_LEVELSCORE_BUTTON_IMAGE_NAME,
        BACK_LEVELSCORE_BUTTON_IMAGE_NAME_MOUSE_OVER,
        EXIT_LEVEL_BUTTON_IMAGE_NAME,
        EXIT_lEVEL_BUTTON_IMAGE_NAME_MOUSE_OVER,
        /*
         * ON GAME SCREEN
         */
        BACK_GAME_BUTTON_IMAGE_NAME,
        BACK_GAME_BUTTON_IMAGE_NAME_MOUSE_OVER,
        SMASH_BUTTON_IMAGE_NAME,
        SMASH_BUTTON_IMAGE_NAME_MOUSE_OVER,
        TRY_AGAIN_IMAGE_NAME,
        /*
         * LEVEL SCORE BUTTON
         */
        LEVEL_OPTIONS,
        LEVEL_IMAGE_OPTIONS,
        LEVEL_MOUSE_OVER_IMAGE_OPTIONS,
        LEVEL_LOCKED_IMAGE_OPTIONS,
        STATS_BUTTON_IMAGE_NAME,
        STATS_BUTTON_MOUSE_OVER_IMAGE_NAME,
        UNDO_BUTTON_IMAGE_NAME,
        UNDO_BUTTON_MOUSE_OVER_IMAGE_NAME,
        TILE_STACK_IMAGE_NAME,
        // AND THE DIALOGS
        STATS_STAR1_IMAGE_NAME,
        STATS_STAR0_IMAGE_NAME,
        STATS_STAR2_IMAGE_NAME,
        STATS_STAR3_IMAGE_NAME,
        METER_IMAGE_NAME,
        METERBAR_IMAGE_NAME,
        STAR_IMAGE_NAME,
        WIN_DIALOG_IMAGE_NAME,
        LOSS_DIALOG_IMAGE_NAME,
        /* TILE LOADING STUFF */
        REGULAR_TILES,
        STRIPE_TILES,
        T_TILES,
        L_TILES

    }

}
