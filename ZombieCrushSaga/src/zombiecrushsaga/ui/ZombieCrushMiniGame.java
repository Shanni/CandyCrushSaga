/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.ui;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import static javax.lang.model.type.TypeKind.NULL;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;
import static zombiecrushsaga.ZombieCrushConstant.*;
import zombiecrushsaga.ZombieCrushSaga;
import zombiecrushsaga.ZombieCrushSaga.ZombieCrushPropertyType;
import zombiecrushsaga.data.ZombieCrushDataModel;
import zombiecrushsaga.data.ZombieCrushLevelRecord;
import zombiecrushsaga.data.ZombieCrushRecord;
import zombiecrushsaga.event.BackGameHandler;
import zombiecrushsaga.event.BackLevelScoreHandler;
import zombiecrushsaga.event.ExitLevelScoreHandler;
import zombiecrushsaga.event.KeyEventHandler;

import zombiecrushsaga.event.LevelScoreHandler;
import zombiecrushsaga.event.LossAndTryAgainHandler;
import zombiecrushsaga.event.PlayGameHandler;
import zombiecrushsaga.event.PlayLevelScoreHandler;
import zombiecrushsaga.event.QuitGameHandler;
import zombiecrushsaga.event.QuitSagaHandler;
import zombiecrushsaga.event.ResetGameHandler;
import zombiecrushsaga.event.ScrollDownHandler;
import zombiecrushsaga.event.ScrollUpHandler;
import zombiecrushsaga.event.SmashHandler;
import zombiecrushsaga.file.ZombieCrushFileManager;

/**
 *
 * @author shan
 */
public class ZombieCrushMiniGame extends MiniGame {
    // THE PLAYER RECORD FOR EACH LEVEL, WHICH LIVES BEYOND ONE SESSION

    private ZombieCrushRecord record;
    // HANDLES ERROR CONDITIONS
    private ZombieCrushErrorHandler errorHandler;
    // MANAGES LOADING OF LEVELS AND THE PLAYER RECORDS FILES
    private ZombieCrushFileManager fileManager;
    // THE SCREEN CURRENTLY BEING PLAYED
    private String currentScreenState;

    public String getCurrentState() {
        return currentScreenState;
    }

   
    @Override
    public void initAudioContent() {
    }

    /**
     * This method switches the application to the splash screen, making all the
     * appropriate UI controls visible & invisible.
     */
    public void switchToSplashScreen() {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(SPLASH_SCREEN_STATE);
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levelImageNames = props.getPropertyOptionsList(ZombieCrushPropertyType.LEVEL_IMAGE_OPTIONS);
        for (String temp : levelImageNames) {
            guiButtons.get(temp).setState(INVISIBLE_STATE);
            guiButtons.get(temp).setEnabled(false);
        }
        // DEACTIVATE THE TOOLBAR CONTROLS
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(RESET_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(QUIT_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(QUIT_BUTTON_TYPE).setEnabled(true);

        guiButtons.get(SCROLLUP_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SCROLLUP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLLDOWN_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SCROLLDOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(QUITSAGA_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(QUITSAGA_BUTTON_TYPE).setEnabled(false);

        guiButtons.get(BACK_LEVEL_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(BACK_LEVEL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(QUITLEVELSCORE_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(QUITLEVELSCORE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PLAYLEVELSCORE_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(PLAYLEVELSCORE_BUTTON_TYPE).setEnabled(false);

        guiButtons.get(BACKGAME_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(BACKGAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SMASH_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SMASH_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setEnabled(false);
        
        guiDecor.get(STATS_STAR_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(STATS_STAR1_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(STATS_STAR2_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(STATS_STAR3_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(METER_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(STAR_TYPE).setState(INVISIBLE_STATE);

        guiDialogs.get(WIN_DIALOG_TYPE).setState(INVISIBLE_STATE);
        guiDialogs.get(LOSS_DIALOG_TYPE).setState(INVISIBLE_STATE);
        // HIDE THE TILES
        ((ZombieCrushDataModel) data).enableTiles(false);

        // MAKE THE CURRENT SCREEN THE SPLASH SCREEN
        currentScreenState = SPLASH_SCREEN_STATE;

    }

    public void switchToGameScreen() {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(GAME_SCREEN_STATE);

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levelImageNames = props.getPropertyOptionsList(ZombieCrushPropertyType.LEVEL_IMAGE_OPTIONS);
        for (String temp : levelImageNames) {
            guiButtons.get(temp).setState(INVISIBLE_STATE);
            guiButtons.get(temp).setEnabled(false);
        }
        // DEACTIVATE THE TOOLBAR CONTROLS
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(QUIT_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(QUIT_BUTTON_TYPE).setEnabled(false);

        guiButtons.get(SCROLLUP_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SCROLLUP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLLDOWN_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SCROLLDOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(QUITSAGA_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(QUITSAGA_BUTTON_TYPE).setEnabled(false);

        guiButtons.get(QUITLEVELSCORE_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(QUITLEVELSCORE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PLAYLEVELSCORE_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(PLAYLEVELSCORE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(BACK_LEVEL_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(BACK_LEVEL_BUTTON_TYPE).setEnabled(false);

        guiButtons.get(BACKGAME_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(BACKGAME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SMASH_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(SMASH_BUTTON_TYPE).setEnabled(true);
         guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setEnabled(false);

        guiDecor.get(STATS_STAR_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(STATS_STAR1_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(STATS_STAR2_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(STATS_STAR3_TYPE).setState(INVISIBLE_STATE);

        guiDecor.get(METER_TYPE).setState(VISIBLE_STATE);
        guiDecor.get(STAR_TYPE).setState(VISIBLE_STATE);
        if (!data.inProgress()) {
            ((ZombieCrushDataModel) data).enableTiles(true);
            data.reset(this);
        }

        if (data.inProgress()) {
            this.getDataModel().unpause();
        }
        // MAKE THE CURRENT SCREEN THE SPLASH SCREEN
        currentScreenState = GAME_SCREEN_STATE;

    }

    public void switchToSagaScreen() {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(SAGA_SCREEN_STATE);
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levelImageNames = props.getPropertyOptionsList(ZombieCrushPropertyType.LEVEL_IMAGE_OPTIONS);
       int i= ((ZombieCrushDataModel)this.getDataModel()).getCurrentLevelCouldPLay();
        
        for (String temp : levelImageNames) {
            if(i>0){
            guiButtons.get(temp).setState(VISIBLE_STATE);
            guiButtons.get(temp).setEnabled(true);
            }
            if(i<=0){
                guiButtons.get(temp).setState(LOCK_STATE);
            guiButtons.get(temp).setEnabled(false);
            }
            i--;
        }

        // DEACTIVATE THE TOOLBAR CONTROLS
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(QUIT_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(QUIT_BUTTON_TYPE).setEnabled(false);

        guiButtons.get(SCROLLUP_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(SCROLLUP_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SCROLLDOWN_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(SCROLLDOWN_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(QUITSAGA_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(QUITSAGA_BUTTON_TYPE).setEnabled(true);

        /* 
         PropertiesManager props = PropertiesManager.getPropertiesManager();
         ArrayList<String> levels = props.getPropertyOptionsList(ZombieCrushPropertyType.LEVEL_OPTIONS);
         for (String level : levels)
         {
         guiButtons.get(level).setState(VISIBLE_STATE);
         guiButtons.get(level).setEnabled(true);
         }    
         */
        guiButtons.get(QUITLEVELSCORE_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(QUITLEVELSCORE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PLAYLEVELSCORE_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(PLAYLEVELSCORE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(BACK_LEVEL_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(BACK_LEVEL_BUTTON_TYPE).setEnabled(false);

        guiButtons.get(BACKGAME_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(BACKGAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SMASH_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SMASH_BUTTON_TYPE).setEnabled(false);
      guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setEnabled(false);
        
        guiDecor.get(STATS_STAR_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(STATS_STAR1_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(STATS_STAR2_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(STATS_STAR3_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(METER_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(STAR_TYPE).setState(INVISIBLE_STATE);

        guiDialogs.get(WIN_DIALOG_TYPE).setState(INVISIBLE_STATE);

        guiDialogs.get(LOSS_DIALOG_TYPE).setState(INVISIBLE_STATE);
        // HIDE THE TILES
        ((ZombieCrushDataModel) data).enableTiles(false);

        // MAKE THE CURRENT SCREEN THE SPLASH SCREEN
        currentScreenState = SAGA_SCREEN_STATE;

    }

    public void switchToLevelScoreScreen() {
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(RECORD_SCREEN_STATE);
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levelImageNames = props.getPropertyOptionsList(ZombieCrushPropertyType.LEVEL_IMAGE_OPTIONS);
        for (String temp : levelImageNames) {
            guiButtons.get(temp).setState(INVISIBLE_STATE);
            guiButtons.get(temp).setEnabled(false);
        }

        // DEACTIVATE THE TOOLBAR CONTROLS
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(QUIT_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(QUIT_BUTTON_TYPE).setEnabled(false);

        guiButtons.get(SCROLLUP_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SCROLLUP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLLDOWN_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SCROLLDOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(QUITSAGA_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(QUITSAGA_BUTTON_TYPE).setEnabled(false);
        //guiButtons.get(BACK_LEVEL_BUTTON_TYPE).setState(INVISIBLE_STATE);
        //guiButtons.get(BACK_LEVEL_BUTTON_TYPE).setEnabled(false);

        guiButtons.get(QUITLEVELSCORE_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(QUITLEVELSCORE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(PLAYLEVELSCORE_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(PLAYLEVELSCORE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(BACK_LEVEL_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(BACK_LEVEL_BUTTON_TYPE).setEnabled(true);

        guiButtons.get(BACKGAME_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(BACKGAME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SMASH_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SMASH_BUTTON_TYPE).setEnabled(false);
      guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setEnabled(false);
       
//        guiDecor.get(STATS_STAR1_TYPE).setState(VISIBLE_STATE);
//        guiDecor.get(STATS_STAR_TYPE).setState(VISIBLE_STATE);
//        
//        guiDecor.get(STATS_STAR2_TYPE).setState(VISIBLE_STATE);
//        guiDecor.get(STATS_STAR3_TYPE).setState(VISIBLE_STATE);
        guiDecor.get(METER_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(STAR_TYPE).setState(INVISIBLE_STATE);

        guiDialogs.get(WIN_DIALOG_TYPE).setState(INVISIBLE_STATE);

        guiDialogs.get(LOSS_DIALOG_TYPE).setState(INVISIBLE_STATE);
        // HIDE THE TILES
        ((ZombieCrushDataModel) data).enableTiles(false);
        if (data.inProgress()) {
           // this.getDataModel().pause();
        }
        // MAKE THE CURRENT SCREEN THE SPLASH SCREEN
        currentScreenState = RECORD_SCREEN_STATE;

    }

    /**
     * This method updates the game grid boundaries, which will depend on the //
     * * level loaded.
     */
    // METHODS OVERRIDDEN FROM MiniGame
    // - initAudioContent
    // - initData
    // - initGUIControls
    // - initGUIHandlers
    // - reset
    // - updateGUI
    /**
     * Initializes the game data used by the application. Note that it is this
     * method's obligation to construct and set this Game's custom GameDataModel
     * object as well as any other needed game objects.
     */
    @Override
    public void initData() {
        // INIT OUR ERROR HANDLER
        errorHandler = new ZombieCrushErrorHandler(window);

        // INIT OUR FILE MANAGER
        fileManager = new ZombieCrushFileManager(this);

        // LOAD THE PLAYER'S RECORD FROM A FILE
        record = fileManager.loadRecordb();

        // INIT OUR DATA MANAGER
        data = new ZombieCrushDataModel(this);
        // LOAD THE GAME DIMENSIONS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        int gameWidth = Integer.parseInt(props.getProperty(ZombieCrushPropertyType.GAME_WIDTH.toString()));
        int gameHeight = Integer.parseInt(props.getProperty(ZombieCrushPropertyType.GAME_HEIGHT.toString()));
        data.setGameDimensions(gameWidth, gameHeight);

        // THIS WILL CHANGE WHEN WE LOAD A LEVEL
        boundaryLeft = Integer.parseInt(props.getProperty(ZombieCrushPropertyType.GAME_LEFT_OFFSET.toString()));
        boundaryTop = Integer.parseInt(props.getProperty(ZombieCrushPropertyType.GAME_TOP_OFFSET.toString()));
        boundaryRight = gameWidth - boundaryLeft;
        boundaryBottom = gameHeight;
    }

    /**
     * Initializes the game controls, like buttons, used by the game
     * application. Note that this includes the tiles, which serve as buttons of
     * sorts.
     */
    @Override
    public void initGUIControls() {
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        float x, y;
        SpriteType sT;
        Sprite s;

        // FIRST PUT THE ICON IN THE WINDOW
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(ZombieCrushPropertyType.IMG_PATH);
        String windowIconFile = props.getProperty(ZombieCrushPropertyType.WINDOW_ICON);
        img = loadImage(imgPath + windowIconFile);
        window.setIconImage(img);

        // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING
        canvas = new ZombieCrushPanel(this, (ZombieCrushDataModel) data);

        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        currentScreenState = SPLASH_SCREEN_STATE;
        sT = new SpriteType(BACKGROUND_TYPE);
        img = loadImage(imgPath + props.getProperty(ZombieCrushPropertyType.SPLASH_SCREEN_IMAGE_NAME));
        sT.addState(SPLASH_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(ZombieCrushPropertyType.SAGA_SCREEN_IMAGE_NAME));
        sT.addState(SAGA_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(ZombieCrushPropertyType.GAME_BACKGROUND_IMAGE_NAME));
        sT.addState(GAME_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(ZombieCrushPropertyType.RECORD_SCREEN_IMAGE_NAME));
        sT.addState(RECORD_SCREEN_STATE, img);

        /*
         FOR LATER USE @SHAN
         img = loadImage(imgPath + props.getProperty(ZombieCrushPropertyType.RECORD_SCREEN_IMAGE_NAME));
         sT.addState(RECORD_SCREEN_STATE, img);
         */
        s = new Sprite(sT, 0, 0, 0, 0, SPLASH_SCREEN_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);

        /*  
        
         ADDING NEW GAME LEVEL HERE!!! @SHAN
         */
        // ADD A BUTTON FOR EACH LEVEL AVAILABLE
        // ArrayList<String> levels = props.getPropertyOptionsList(ZombieCrushPropertyType.LEVEL_OPTIONS);
        ArrayList<String> levelImageNames = props.getPropertyOptionsList(ZombieCrushPropertyType.LEVEL_IMAGE_OPTIONS);
        ArrayList<String> levelMouseOverImageNames = props.getPropertyOptionsList(ZombieCrushPropertyType.LEVEL_MOUSE_OVER_IMAGE_OPTIONS);
        ArrayList<String> levelLockedImageNames = props.getPropertyOptionsList(ZombieCrushPropertyType.LEVEL_LOCKED_IMAGE_OPTIONS);

//  float totalWidth = levels.size() * (LEVEL_BUTTON_WIDTH + LEVEL_BUTTON_MARGIN) - LEVEL_BUTTON_MARGIN;
        // float gameWidth = Integer.parseInt(props.getProperty(ZombieCrushPropertyType.GAME_WIDTH));
        x = 20;
        y = 620;
        int a = 0;
        for (int i = 0; i < levelImageNames.size(); i++) {
            sT = new SpriteType(LEVEL_SELECT_BUTTON_TYPE);
            img = loadImageWithColorKey(imgPath + levelImageNames.get(i), COLOR_KEY);
            sT.addState(VISIBLE_STATE, img);
            img = loadImageWithColorKey(imgPath + levelMouseOverImageNames.get(i), COLOR_KEY);
            sT.addState(MOUSE_OVER_STATE, img);
            img = loadImageWithColorKey(imgPath + levelLockedImageNames.get(i), COLOR_KEY);
            sT.addState(LOCK_STATE, img);
            s = new Sprite(sT, x, y, 0, 0, INVISIBLE_STATE);
            guiButtons.put(levelImageNames.get(i), s);
            if (i % 20 < 10) {
                if (a == 1) {
                    y = y - 40;
                }
                x += LEVEL_BUTTON_X_OFFSET;
                a = 0;
            } else {
                if (a == 0) {
                    y -= 40;
                }
                x -= LEVEL_BUTTON_X_OFFSET;
                a = 1;
            }
            y -= LEVEL_BUTTON_Y_OFFSET;
        }

        // ADD THE CONTROLS ALONG THE NORTH OF THE GAME SCREEN
        // THEN THE NEW BUTTON
        String newButton = props.getProperty(ZombieCrushPropertyType.PLAY_GAME_BUTTON_IMAGE_NAME);
        sT = new SpriteType(PLAY_GAME_BUTTON_TYPE);
        img = loadImage(imgPath + newButton);
        sT.addState(VISIBLE_STATE, img);
        String newMouseOverButton = props.getProperty(ZombieCrushPropertyType.PLAY_GAME_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImage(imgPath + newMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, PLAY_BUTTON_X, PLAY_BUTTON_Y, 0, 0, VISIBLE_STATE);
        guiButtons.put(PLAY_GAME_BUTTON_TYPE, s);

        //RESET Button
        String newButton1 = props.getProperty(ZombieCrushPropertyType.RESET_BUTTON_IMAGE_NAME);
        sT = new SpriteType(RESET_BUTTON_TYPE);
        img = loadImage(imgPath + newButton1);
        sT.addState(VISIBLE_STATE, img);
        String newMouseOverButton1 = props.getProperty(ZombieCrushPropertyType.RESET_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImage(imgPath + newMouseOverButton1);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, RESET_BUTTON_X, RESET_BUTTON_Y, 0, 0, VISIBLE_STATE);
        guiButtons.put(RESET_BUTTON_TYPE, s);

        //QUIT Button
        String newButton2 = props.getProperty(ZombieCrushPropertyType.QUIT_BUTTON_IMAGE_NAME);
        sT = new SpriteType(QUIT_BUTTON_TYPE);
        img = loadImage(imgPath + newButton2);
        sT.addState(VISIBLE_STATE, img);
        String newMouseOverButton2 = props.getProperty(ZombieCrushPropertyType.QUIT_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImage(imgPath + newMouseOverButton2);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, QUIT_BUTTON_X, QUIT_BUTTON_Y, 0, 0, VISIBLE_STATE);
        guiButtons.put(QUIT_BUTTON_TYPE, s);

        String newButton3 = props.getProperty(ZombieCrushPropertyType.SCROLLUP_BUTTON_IMAGE_NAME);
        sT = new SpriteType(SCROLLUP_BUTTON_TYPE);
        img = loadImage(imgPath + newButton3);
        sT.addState(VISIBLE_STATE, img);
        String newMouseOverButton3 = props.getProperty(ZombieCrushPropertyType.SCROLLUP_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImage(imgPath + newMouseOverButton3);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, SCROLLUP_BUTTON_X, SCROLLUP_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(SCROLLUP_BUTTON_TYPE, s);

        String newButton4 = props.getProperty(ZombieCrushPropertyType.SCROLLDOWN_BUTTON_IMAGE_NAME);
        sT = new SpriteType(SCROLLDOWN_BUTTON_TYPE);
        img = loadImage(imgPath + newButton4);
        sT.addState(VISIBLE_STATE, img);
        String newMouseOverButton4 = props.getProperty(ZombieCrushPropertyType.SCROLLDOWN_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImage(imgPath + newMouseOverButton4);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, SCROLLDOWN_BUTTON_X, SCROLLDOWN_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(SCROLLDOWN_BUTTON_TYPE, s);

        String newButton5 = props.getProperty(ZombieCrushPropertyType.EXTI_BUTTON_IMAGE_NAME);
        sT = new SpriteType(QUITSAGA_BUTTON_TYPE);
        img = loadImage(imgPath + newButton5);
        sT.addState(VISIBLE_STATE, img);
        String newMouseOverButton5 = props.getProperty(ZombieCrushPropertyType.EXTI_BUTTON_IMAGE_NAME_MOUSE_OVER);
        img = loadImage(imgPath + newMouseOverButton5);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, EXITSAGA_BUTTON_X, EXITSAGA_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(QUITSAGA_BUTTON_TYPE, s);

        String newButton6 = props.getProperty(ZombieCrushPropertyType.PLAY_ZOMCRUSH_IMAGE_NAME);
        sT = new SpriteType(PLAYLEVELSCORE_BUTTON_TYPE);
        img = loadImage(imgPath + newButton6);
        sT.addState(VISIBLE_STATE, img);
        String newMouseOverButton6 = props.getProperty(ZombieCrushPropertyType.PLAY_ZOMCRUSH_IMAGE_NAME_MOUSE_OVER);
        img = loadImage(imgPath + newMouseOverButton6);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, PLAYSAGA_BUTTON_X, PLAYSAGA_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(PLAYLEVELSCORE_BUTTON_TYPE, s);

        String newButton66 = props.getProperty(ZombieCrushPropertyType.EXIT_LEVEL_BUTTON_IMAGE_NAME);
        sT = new SpriteType(QUITLEVELSCORE_BUTTON_TYPE);
        img = loadImage(imgPath + newButton66);
        sT.addState(VISIBLE_STATE, img);
        String newMouseOverButton66 = props.getProperty(ZombieCrushPropertyType.EXIT_lEVEL_BUTTON_IMAGE_NAME_MOUSE_OVER);
        img = loadImage(imgPath + newMouseOverButton66);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, QUITLEVELSCORE_BUTTON_X, QUITLEVELSCORE_BUTTON_y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(QUITLEVELSCORE_BUTTON_TYPE, s);

        String newButton7 = props.getProperty(ZombieCrushPropertyType.BACK_LEVELSCORE_BUTTON_IMAGE_NAME);
        sT = new SpriteType(BACK_LEVEL_BUTTON_TYPE);
        img = loadImage(imgPath + newButton7);
        sT.addState(VISIBLE_STATE, img);
        String newMouseOverButton7 = props.getProperty(ZombieCrushPropertyType.BACK_LEVELSCORE_BUTTON_IMAGE_NAME_MOUSE_OVER);
        img = loadImage(imgPath + newMouseOverButton7);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, BACKLEVEL_BUTTON_X, BACKLEVEL_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(BACK_LEVEL_BUTTON_TYPE, s);

        String newButton8 = props.getProperty(ZombieCrushPropertyType.BACK_GAME_BUTTON_IMAGE_NAME);
        sT = new SpriteType(BACKGAME_BUTTON_TYPE);
        img = loadImage(imgPath + newButton8);
        sT.addState(VISIBLE_STATE, img);
        String newMouseOverButton8 = props.getProperty(ZombieCrushPropertyType.BACK_GAME_BUTTON_IMAGE_NAME_MOUSE_OVER);
        img = loadImage(imgPath + newMouseOverButton8);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, BACKGAME_BUTTON_X, BACKGAME_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(BACKGAME_BUTTON_TYPE, s);

        String newButton9 = props.getProperty(ZombieCrushPropertyType.SMASH_BUTTON_IMAGE_NAME);
        sT = new SpriteType(SMASH_BUTTON_TYPE);
        img = loadImage(imgPath + newButton9);
        sT.addState(VISIBLE_STATE, img);
        String newMouseOverButton9 = props.getProperty(ZombieCrushPropertyType.SMASH_BUTTON_IMAGE_NAME_MOUSE_OVER);
        img = loadImage(imgPath + newMouseOverButton9);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, 100, 300, 0, 0, INVISIBLE_STATE);
        guiButtons.put(SMASH_BUTTON_TYPE, s);

       
        
         // AND THE TRY AGAIN BUTTON
         String statsButton = props.getProperty(ZombieCrushPropertyType.TRY_AGAIN_IMAGE_NAME);
         sT = new SpriteType(TRY_AGAIN_BUTTON_TYPE);
         img = loadImage(imgPath + statsButton);
         sT.addState(VISIBLE_STATE, img);
         String newMouseOverButton10 = props.getProperty(ZombieCrushPropertyType.TRY_AGAIN_IMAGE_NAME);
        img = loadImage(imgPath + newMouseOverButton10);
        sT.addState(MOUSE_OVER_STATE, img);
         s = new Sprite(sT,500, 200, 0, 0, INVISIBLE_STATE);
         guiButtons.put(TRY_AGAIN_BUTTON_TYPE, s);
           
        
      
        // NOW ADD THE DIALOGS
        // AND THE STATS STAR DISPLAY
        String statsStar0 = props.getProperty(ZombieCrushPropertyType.STATS_STAR0_IMAGE_NAME);
        sT = new SpriteType(STATS_STAR_TYPE);
        img = loadImageWithColorKey(imgPath + statsStar0, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        x = (data.getGameWidth() / 2) - (img.getWidth(null) / 2);
        y = (data.getGameHeight() / 2) - (img.getHeight(null) / 2) - 100;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE_STATE);
        guiDecor.put(STATS_STAR_TYPE, s);

        String statsStar1 = props.getProperty(ZombieCrushPropertyType.STATS_STAR1_IMAGE_NAME);
        sT = new SpriteType(STATS_STAR1_TYPE);
        img = loadImageWithColorKey(imgPath + statsStar1, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        x = (data.getGameWidth() / 2) - (img.getWidth(null) / 2);
        y = (data.getGameHeight() / 2) - (img.getHeight(null) / 2) - 100;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE_STATE);
        guiDecor.put(STATS_STAR1_TYPE, s);

        String statsStar2 = props.getProperty(ZombieCrushPropertyType.STATS_STAR2_IMAGE_NAME);
        sT = new SpriteType(STATS_STAR2_TYPE);
        img = loadImageWithColorKey(imgPath + statsStar2, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        x = (data.getGameWidth() / 2) - (img.getWidth(null) / 2);
        y = (data.getGameHeight() / 2) - (img.getHeight(null) / 2) - 100;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE_STATE);
        guiDecor.put(STATS_STAR2_TYPE, s);

        String statsStar3 = props.getProperty(ZombieCrushPropertyType.STATS_STAR3_IMAGE_NAME);
        sT = new SpriteType(STATS_STAR3_TYPE);
        img = loadImageWithColorKey(imgPath + statsStar3, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        x = (data.getGameWidth() / 2) - (img.getWidth(null) / 2);
        y = (data.getGameHeight() / 2) - (img.getHeight(null) / 2) - 100;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE_STATE);
        guiDecor.put(STATS_STAR3_TYPE, s);

        String Meter = props.getProperty(ZombieCrushPropertyType.METER_IMAGE_NAME);
        sT = new SpriteType(METER_TYPE);
        img = loadImageWithColorKey(imgPath + Meter, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        s = new Sprite(sT, 600, 650, 0, 0, INVISIBLE_STATE);
        guiDecor.put(METER_TYPE, s);

        String STAR = props.getProperty(ZombieCrushPropertyType.STAR_IMAGE_NAME);
        sT = new SpriteType(STAR_TYPE);
        img = loadImageWithColorKey(imgPath + STAR, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        int ystar = ((ZombieCrushDataModel) data).getTargetScore();
        ystar = (ystar * 400) / 1000;
        s = new Sprite(sT, 600 + ystar, 630, 0, 0, INVISIBLE_STATE);
        guiDecor.put(STAR_TYPE, s);

        // AND THE WIN CONDITION DISPLAY
        String winDisplay = props.getProperty(ZombieCrushPropertyType.WIN_DIALOG_IMAGE_NAME);
        sT = new SpriteType(WIN_DIALOG_TYPE);
        img = loadImageWithColorKey(imgPath + winDisplay, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        x = (data.getGameWidth() / 2) - (img.getWidth(null) / 2);
        y = (data.getGameHeight() / 2) - (img.getHeight(null) / 2);
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE_STATE);
        guiDialogs.put(WIN_DIALOG_TYPE, s);

        //loss condition
        String lossDisplay = props.getProperty(ZombieCrushPropertyType.LOSS_DIALOG_IMAGE_NAME);
        sT = new SpriteType(LOSS_DIALOG_TYPE);
        img = loadImageWithColorKey(imgPath + lossDisplay, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        x = (data.getGameWidth() / 2) - (img.getWidth(null) / 2);
        y = (data.getGameHeight() / 2) - (img.getHeight(null) / 2);
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE_STATE);
        guiDialogs.put(LOSS_DIALOG_TYPE, s);

        // THEN THE TILES STACKED TO THE TOP LEFT
        ((ZombieCrushDataModel) data).initTiles();
    }

    /**
     * Initializes the game event handlers for things like game gui buttons.
     */
    @Override
    public void initGUIHandlers() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String dataPath = props.getProperty(ZombieCrushPropertyType.DATA_PATH);

//        // WE'LL HAVE A CUSTOM RESPONSE FOR WHEN THE USER CLOSES THE WINDOW
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // QuitGameHandler eh = new QuitGameHandler(this);
        // window.addWindowListener(eh);

        // LEVEL BUTTON EVENT HANDLERS
        ArrayList<String> levelimg = props.getPropertyOptionsList(ZombieCrushPropertyType.LEVEL_IMAGE_OPTIONS);
        for (String levelFile : levelimg) {
            LevelScoreHandler slh = new LevelScoreHandler(this, dataPath + levelFile);
            guiButtons.get(levelFile).setActionListener(slh);
            guiButtons.get(levelFile).setEnabled(false);
        }

        //   ArrayList<String> levels = props.getPropertyOptionsList(ZombieCrushPropertyType.LEVEL_OPTIONS);
        //    for (String levelFile : levels) {
        String levelFile = LEVELFILE;
        PlayLevelScoreHandler pls = new PlayLevelScoreHandler(this, dataPath + "zomcrush/" + levelFile);
        guiButtons.get(PLAYLEVELSCORE_BUTTON_TYPE).setActionListener(pls);
//            guiButtons.get(levelFile).setEnabled(false);

        //      }
        // NEW GAME EVENT HANDLER
        PlayGameHandler pgh = new PlayGameHandler(this);
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setActionListener(pgh);
        ResetGameHandler rgh = new ResetGameHandler(this);
        guiButtons.get(RESET_BUTTON_TYPE).setActionListener(rgh);
        QuitGameHandler qgh = new QuitGameHandler(this);
        guiButtons.get(QUIT_BUTTON_TYPE).setActionListener(qgh);

        /*
         * IN LEVELSCORE SCREEN
         */
        BackLevelScoreHandler bls = new BackLevelScoreHandler(this);
        guiButtons.get(BACK_LEVEL_BUTTON_TYPE).setActionListener(bls);
        ExitLevelScoreHandler elh = new ExitLevelScoreHandler(this);
        guiButtons.get(QUITLEVELSCORE_BUTTON_TYPE).setActionListener(elh);

        ScrollUpHandler suh = new ScrollUpHandler(this);
        guiButtons.get(SCROLLUP_BUTTON_TYPE).setActionListener(suh);
        ScrollDownHandler sdh = new ScrollDownHandler(this);
        guiButtons.get(SCROLLDOWN_BUTTON_TYPE).setActionListener(sdh);
        QuitSagaHandler qs = new QuitSagaHandler(this);
        guiButtons.get(QUITSAGA_BUTTON_TYPE).setActionListener(qs);

        BackGameHandler bgh = new BackGameHandler(this);
        guiButtons.get(BACKGAME_BUTTON_TYPE).setActionListener(bgh);
        SmashHandler sh = new SmashHandler(this);
        guiButtons.get(SMASH_BUTTON_TYPE).setActionListener(sh);
        LossAndTryAgainHandler tr=new LossAndTryAgainHandler(this);
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setActionListener(tr);
        
        // KEY LISTENER - LET'S US PROVIDE CUSTOM RESPONSES
        KeyEventHandler mkh = new KeyEventHandler(this);
        this.setKeyListener(mkh);
        /*
         // STATS BUTTON EVENT HANDLER
         StatsHandler sh = new StatsHandler(this);
         guiButtons.get(STATS_BUTTON_TYPE).setActionListener(sh);
        
         UndoButtonHandler ubh= new UndoButtonHandler(this);
         guiButtons.get(UNDO_BUTTON_TYPE).setActionListener(ubh);
        
         */
    }
 public void savePlayerRecord(ZombieCrushRecord recordToSave) throws FileNotFoundException, IOException {
        // THIS CURRENTLY DOES NOTHING, INSTEAD, IT MUST SAVE ALL THE
        // PLAYER RECORDS IN THE SAME FORMAT IT IS BEING LOADED
          PropertiesManager props = PropertiesManager.getPropertiesManager();
         String dataPath = props.getProperty(ZombieCrushPropertyType.DATA_PATH);
         String recordPath = dataPath + props.getProperty(ZombieCrushPropertyType.RECORD_FILE_NAME);
         FileOutputStream s=new FileOutputStream(recordPath);
         File fileToWrite= new File(recordPath);
         File fileToWrite1= new File(recordPath);
         FileWriter fw=new FileWriter(fileToWrite1);
         BufferedWriter b=new BufferedWriter(fw);
         
         if(fileToWrite.exists()){
             fileToWrite.delete();
             fileToWrite1.createNewFile();
         }
         HashMap<String, ZombieCrushLevelRecord> levelRecords=recordToSave.getLevelRecord();
         ArrayList<String> levels= props.getPropertyOptionsList(ZombieCrushPropertyType.LEVEL_OPTIONS);
         b.write(String.valueOf(levelRecords.size()));
         b.newLine();
         for(int i=0; i<levelRecords.size();i++){
             b.write(levels.get(i));
             b.newLine();
             b.write(String.valueOf(recordToSave.getScore(levels.get(i))));
             b.newLine();
             b.write(String.valueOf(recordToSave.getStars(levels.get(i))));
             b.newLine();
         }
         b.close();
        
      /*
         try{
         PropertiesManager props = PropertiesManager.getPropertiesManager();
         String dataPath = props.getProperty(ZombieCrushPropertyType.DATA_PATH);
         String recordPath = dataPath + props.getProperty(ZombieCrushPropertyType.RECORD_FILE_NAME);
         FileOutputStream s=new FileOutputStream(recordPath);
         byte[] b= this.getPlayerRecord().toByteArray();
         s.write(b);
         }catch(Exception e){
         System.out.println("cannot save record");
        
         
    }*/
    }
 
 public void savePlayerRecordb()
    {
        // THIS CURRENTLY DOES NOTHING, INSTEAD, IT MUST SAVE ALL THE
        // PLAYER RECORDS IN THE SAME FORMAT IT IS BEING LOADED
      
        try{
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String dataPath = props.getProperty(ZombieCrushPropertyType.DATA_PATH);
            String recordPath = dataPath + props.getProperty(ZombieCrushPropertyType.RECORD_FILE_NAMEb);
           FileOutputStream s=new FileOutputStream(recordPath);
           byte[] b= this.getPlayerRecord().toByteArray();
           s.write(b);
           }catch(Exception e){
        System.out.println("cannot save record");
    }
    }
    /**
     * Invoked when a new game is started, it resets all relevant game data and
     * gui control states.
     */
    @Override
    public void reset() {
        data.reset(this);

        // LET THE USER START THE GAME ON DEMAND
//        data.setGameState(MiniGameState.NOT_STARTED);
    }

    /**
     * Updates the state of all gui controls according to the current game
     * conditions.
     */
    @Override
    public void updateGUI() {

        // if ( guiBackground.get(BACKGROUND_TYPE).getY()> DY ){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> levelImageNames = props.getPropertyOptionsList(ZombieCrushSaga.ZombieCrushPropertyType.LEVEL_IMAGE_OPTIONS);
        for (String temp : levelImageNames) {
            //   if((game.getGUIButtons().get(temp).getY() + SCROLL_OFFSET)<=0)
            guiButtons.get(temp).update(this);
        }
        guiDecor.get(BACKGROUND_TYPE).update(this);
        guiDecor.get(STATS_STAR1_TYPE).update(this);
        //   }
        // GO THROUGH THE VISIBLE BUTTONS TO TRIGGER MOUSE OVERS
        Iterator<Sprite> buttonsIt = guiButtons.values().iterator();
        while (buttonsIt.hasNext()) {
            Sprite button = buttonsIt.next();

            // ARE WE ENTERING A BUTTON?
            if (button.getState().equals(VISIBLE_STATE)) {
                if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                    button.setState(MOUSE_OVER_STATE);
                }
            } // ARE WE EXITING A BUTTON?
            else if (button.getState().equals(MOUSE_OVER_STATE)) {
                if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                    button.setState(VISIBLE_STATE);
                }
            }
        }
    }

    public ZombieCrushErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public ZombieCrushRecord getPlayerRecord() {
        return record;
    }

//    public ZombieCrushErrorHandler getErrorHandler()
//    {
//        return errorHandler;
//    }
    public ZombieCrushFileManager getFileManager() {
        return fileManager;
    }

    /**
     * Used for testing to see if the current screen state matches the
     * testScreenState argument. If it mates, true is returned, else false.
     *
     * @param testScreenState Screen state to test against the current state.
     *
     * @return true if the current state is testScreenState, false otherwise.
     */
    public boolean isCurrentScreenState(String testScreenState) {
        return testScreenState.equals(currentScreenState);
    }

    // SERVICE METHODS
    // - displayStats
    // - savePlayerRecord
    // - switchToGameScreen
    // - switchToSplashScreen
    // - updateBoundaries
    /**
     * This method displays makes the stats dialog display visible, which
     * includes the text inside.
     */
    public void displayStats() {
        // MAKE SURE ONLY THE PROPER DIALOG IS VISIBLE
        guiDialogs.get(WIN_DIALOG_TYPE).setState(INVISIBLE_STATE);
        guiDialogs.get(STATS_STAR_TYPE).setState(VISIBLE_STATE);
    }

    public void updateBoundaries() {
        // NOTE THAT THE ONLY ONES WE CARE ABOUT ARE THE LEFT & TOP BOUNDARIES
        float totalWidth = ((ZombieCrushDataModel) data).getGridColumns() * TILE_IMAGE_WIDTH;
        float halfTotalWidth = totalWidth / 2.0f;
        float halfViewportWidth = data.getGameWidth() / 2.0f;
        boundaryLeft = halfViewportWidth - halfTotalWidth;

        // THE LEFT & TOP BOUNDARIES ARE WHERE WE START RENDERING TILES IN THE GRID
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        float topOffset = Integer.parseInt(props.getProperty(ZombieCrushPropertyType.GAME_TOP_OFFSET.toString()));
        float totalHeight = ((ZombieCrushDataModel) data).getGridRows() * TILE_IMAGE_HEIGHT;
        float halfTotalHeight = totalHeight / 2.0f;
        float halfViewportHeight = (data.getGameHeight() - topOffset) / 2.0f;
        boundaryTop = topOffset + halfViewportHeight - halfTotalHeight;
    }
}
