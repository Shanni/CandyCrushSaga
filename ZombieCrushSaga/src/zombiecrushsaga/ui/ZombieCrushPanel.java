/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiecrushsaga.ui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JPanel;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import zombiecrushsaga.ZombieCrushConstant;
import static zombiecrushsaga.ZombieCrushConstant.*;
import zombiecrushsaga.data.ZombieCrushDataModel;

/**
 *
 * @author shanliu
 */
public class ZombieCrushPanel extends JPanel {

    // THIS IS ACTUALLY OUR Mahjong Solitaire APP, WE NEED THIS
    // BECAUSE IT HAS THE GUI STUFF THAT WE NEED TO RENDER
    private MiniGame game;

    // AND HERE IS ALL THE GAME DATA THAT WE NEED TO RENDER
    private ZombieCrushDataModel data;

    // WE'LL USE THIS TO FORMAT SOME TEXT FOR DISPLAY PURPOSES
    private NumberFormat numberFormatter;

    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING UNSELECTED TILES
    private BufferedImage blankTileImage;

    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING SELECTED TILES
    private BufferedImage blankTileSelectedImage;

    /**
     * This constructor stores the game and data references, which we'll need
     * for rendering.
     *
     * @param initGame the Mahjong Solitaire game that is using this panel for
     * rendering.
     *
     * @param initData the Mahjong Solitaire game data.
     */
    public ZombieCrushPanel(MiniGame initGame, ZombieCrushDataModel initData) {
        game = initGame;
        data = initData;
        numberFormatter = NumberFormat.getNumberInstance();
        numberFormatter.setMinimumFractionDigits(3);
        numberFormatter.setMaximumFractionDigits(3);
    }

    // MUTATOR METHODS
    // -setBlankTileImage
    // -setBlankTileSelectedImage
    /**
     * This mutator method sets the base image to use for rendering tiles.
     *
     * @param initBlankTileImage The image to use as the base for rendering
     * tiles.
     */
    public void setBlankTileImage(BufferedImage initBlankTileImage) {
        blankTileImage = initBlankTileImage;
    }

    /**
     * This mutator method sets the base image to use for rendering selected
     * tiles.
     *
     * @param initBlankTileSelectedImage The image to use as the base for
     * rendering selected tiles.
     */
    public void setBlankTileSelectedImage(BufferedImage initBlankTileSelectedImage) {
        blankTileSelectedImage = initBlankTileSelectedImage;
    }

    // MUTATOR METHODS
    // -setBlankTileImage
    // -setBlankTileSelectedImage
    /**
     * This is where rendering starts. This method is called each frame, and the
     * entire game application is rendered here with the help of a number of
     * helper methods.
     *
     * @param g The Graphics context for this panel.
     */
    @Override
    public void paintComponent(Graphics g) {
        try {
            // MAKE SURE WE HAVE EXCLUSIVE ACCESS TO THE GAME DATA
            game.beginUsingData();

            // CLEAR THE PANEL
            super.paintComponent(g);

            // RENDER THE BACKGROUND, WHICHEVER SCREEN WE'RE ON
            renderBackground(g);

        
// AND THE BUTTONS AND DECOR
            renderGUIControls(g);
           
            // AND THE TILES
             renderTiles(g);

            // AND THE DIALOGS, IF THERE ARE ANY
            renderDialogs(g);

            // AND THE TIME AND TILES STATS
            renderStats(g);

            renderScore(g);

            // RENDERING THE GRID WHERE ALL THE TILES GO CAN BE HELPFUL
            // DURING DEBUGGIN TO BETTER UNDERSTAND HOW THEY RE LAID OUT
            renderGrid(g);

            // AND FINALLY, TEXT FOR DEBUGGING
            renderDebuggingText(g);
        } finally {
            // RELEASE THE LOCK
            game.endUsingData();
        }
    }

    /**
     * Renders the background image, which is different depending on the screen.
     *
     * @param g the Graphics context of this panel.
     */
    public void renderBackground(Graphics g) {
        // THERE IS ONLY ONE CURRENTLY SET
        Sprite bg = game.getGUIDecor().get(BACKGROUND_TYPE);
        SpriteType bgST = bg.getSpriteType();
        Image img = bgST.getStateImage(bg.getState());

        if (bg.getState().equals(SAGA_SCREEN_STATE)) {
            bg.setY(OFFSET);
            g.drawImage(img, 0, 0, getWidth(), getHeight(),
                    (int) bg.getX(), (int) bg.getY(),
                    (int) bg.getX() + (int) getWidth(), (int) bg.getY() + getHeight(), null);
        } else {
            bg.setY(0);
            g.drawImage(img, (int) bg.getX(), (int) bg.getY(), bgST.getWidth(), bgST.getHeight(), null);
        }
    }

    /**
     * Renders the s Sprite into the Graphics context g. Note that each Sprite
     * knows its own x,y coordinate location.
     *
     * @param g the Graphics context of this panel
     *
     * @param s the Sprite to be rendered
     */
    public void renderSprite(Graphics g, Sprite s) {
        // ONLY RENDER THE VISIBLE ONES
        if (!s.getState().equals(INVISIBLE_STATE)) {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int) s.getX(), (int) s.getY(), bgST.getWidth(), bgST.getHeight(), null);
        }
    }

    /**
     * Renders all the GUI decor and buttons.
     *
     * @param g this panel's rendering context.
     */
    public void renderGUIControls(Graphics g) {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> decorSprites = game.getGUIDecor().values();
        for (Sprite s : decorSprites) {
            renderSprite(g, s);
        }

        // AND NOW RENDER THE BUTTONS
        Collection<Sprite> buttonSprites = game.getGUIButtons().values();
        for (Sprite s : buttonSprites) {
            renderSprite(g, s);
        }
    }

    public void renderScore(Graphics g) {
        if (((ZombieCrushMiniGame) game).isCurrentScreenState(GAME_SCREEN_STATE)
                && (data.inProgress() || data.isPaused()||data.lost()||data.won())) {

            g.setFont(TEXT_DISPLAY_FONT);
            Sprite bg;
    
            // if(game.getGUIDialogs().get(STATS_DIALOG_TYPE).getState().equals(VISIBLE_STATE)){
            String name = data.getCurrentLevel();
            String name1[] = name.split("/");
            int score = data.getScore();
            int i=data.getEearnedScore();
            String currentscore = String.valueOf(i);
    //        String games = "Stars: " + ((ZombieCrushMiniGame) game).getPlayerRecord().getStars(name);
    String games = "Stars: " + star;

            String Score = "Current Score: " + score;

            int moveCount = data.getMove();
            String t = "Remaining Move: " + Integer.toString(moveCount);

            int xs = 850;
            int ys = 310 + STATS_OFFSET_Y;
            g.setFont(STATS_FONT);
            g.drawString(name1[name1.length - 1].substring(0, name1[name1.length - 1].length() - 4), xs, ys);
            ys += STATS_OFFSET_Y;
            g.drawString(games, xs, ys);
            ys += STATS_OFFSET_Y;

            g.drawString(t, xs, ys);
            ys += STATS_OFFSET_Y;
            g.drawString(Score, xs, ys);
//             ys+=STATS_OFFSET_Y;
//             g.drawString(w, xs, ys);
            //DRAW METER BAR
            bg = game.getGUIDecor().get(METER_TYPE);
            bg.setState(VISIBLE_STATE);
            int x, y, x1, y1;
            x = (int) game.getGUIDecor().get(METER_TYPE).getX() + 4;
            y = (int) game.getGUIDecor().get(METER_TYPE).getY() + 4;
            x1 = (score * 400) / 1000;
            y1 = 23;
            g.setColor(STATS_COLOR);
            g.fillRect(x, y, x1, y1);
            //DRAW STAR ON THE METER
            bg = game.getGUIDecor().get(STAR_TYPE);
            bg.setState(VISIBLE_STATE);
            
            //FLOAT POINT
            if (data.getMovingTiles().hasNext()) {
                if (data.getTheTileX() != 0) {
                    g.setColor(COLOR_KEY);
                    g.drawString(currentscore, (int) data.getTheTileX(), (int) data.getTheTileY());
                }
            }
        }

    }
int star;
    public void renderStats(Graphics g) {
        // RENDER THE GAME TIME
        if (((ZombieCrushMiniGame) game).isCurrentScreenState(RECORD_SCREEN_STATE)) {
            // RENDER THE TIME
            String time = data.gameTimeToText();
            int x = TIME_X + TIME_OFFSET;
            int y = TIME_Y + TIME_TEXT_OFFSET;
            g.setFont(TEXT_DISPLAY_FONT);
            g.drawString(time, x, y);

            // if(game.getGUIDialogs().get(STATS_DIALOG_TYPE).getState().equals(VISIBLE_STATE)){
            String name = data.getCurrentLevel();
            String name1[] = name.split("/");
            
            int gstar = ((ZombieCrushMiniGame) game).getPlayerRecord().getStars(name);
            int score = data.getScore();
            
            if(score>=data.getStar3Score())
                gstar=3;
            else if(score>=data.getStar2Score())
                gstar=2;
            else if(score>=data.getTargetScore())
                gstar=1;
            else gstar=0;
            star=gstar;
            //String games = "Stars: " + ((ZombieCrushMiniGame) game).getPlayerRecord().getStars(name);
           // String win = "Moves: " + ((ZombieCrushMiniGame) game).getPlayerRecord().getGameMove(name);
         String games = "Stars: " + gstar;
            //String HighScore = "Highest Score: " + ((ZombieCrushMiniGame) game).getPlayerRecord().getHighestScore(name);
            String Score = "Current Score: " + score;
            String LevelGoal = "Level Goal: " + ((ZombieCrushDataModel)((ZombieCrushMiniGame) game).getDataModel()).getTargetScore();
           
// String LevelGoal = "Level Goal: " + ((ZombieCrushMiniGame) game).getPlayerRecord().getLevelGoalScore(name);
            int moveCount = data.getMove();
            String t = "Remaining game move: " + Integer.toString(moveCount);

            Sprite bg;
            if (gstar == 1) {
               
            
                bg = game.getGUIDecor().get(STATS_STAR1_TYPE);
                bg.setState(VISIBLE_STATE);
                   bg = game.getGUIDecor().get(STATS_STAR_TYPE);
                bg.setState(INVISIBLE_STATE);
            }
            if (gstar == 2) {
                 bg = game.getGUIDecor().get(STATS_STAR_TYPE);
                bg.setState(INVISIBLE_STATE);
                bg = game.getGUIDecor().get(STATS_STAR1_TYPE);
                bg.setState(INVISIBLE_STATE);
                bg = game.getGUIDecor().get(STATS_STAR2_TYPE);
                bg.setState(VISIBLE_STATE);
            }
            if (gstar == 3) {
                 bg = game.getGUIDecor().get(STATS_STAR_TYPE);
                bg.setState(INVISIBLE_STATE);
                bg = game.getGUIDecor().get(STATS_STAR1_TYPE);
                bg.setState(INVISIBLE_STATE);
                bg = game.getGUIDecor().get(STATS_STAR2_TYPE);
                bg.setState(INVISIBLE_STATE);
                bg = game.getGUIDecor().get(STATS_STAR3_TYPE);
                bg.setState(VISIBLE_STATE);
            } else {
                bg = game.getGUIDecor().get(STATS_STAR_TYPE);
                bg.setState(VISIBLE_STATE);
            }

            int xs = 465;
            int ys = 310 + STATS_OFFSET_Y;
            g.setFont(STATS_FONT);
            g.drawString(name1[name1.length - 1].substring(0, name1[name1.length - 1].length() - 4), xs, ys);
            ys += STATS_OFFSET_Y;
            g.drawString(games, xs, ys);
            ys += STATS_OFFSET_Y;
            g.drawString(LevelGoal, xs, ys);
            ys += STATS_OFFSET_Y;
            g.drawString(t, xs, ys);
            ys += STATS_OFFSET_Y;
//            g.drawString(win, xs, ys);
//            ys += STATS_OFFSET_Y;
//            g.drawString(HighScore, xs, ys);
//            ys += STATS_OFFSET_Y;
            g.drawString(Score, xs, ys);
//             ys+=STATS_OFFSET_Y;
//             g.drawString(w, xs, ys);

        }
    }

    /**
     * Helper method for rendering the tiles that are currently moving.
     *
     * @param g Rendering context for this panel.
     *
     * @param tileToRender Tile to render to this panel.
     */
    /**
     * Renders all the game tiles, doing so carefully such that they are
     * rendered in the proper order.
     *
     * @param g the Graphics context of this panel.
     */
    public void renderTiles(Graphics g) {
        // DRAW THE TOP TILES ON THE STACK
        if (!data.won()) {
            // WE DRAW ONLY THE TOP 4 (OR 2 IF THERE ARE ONLY 2). THE REASON
            // WE DRAW 4 IS THAT WHILE WE MOVE MATCHES TO THE STACK WE WANT
            // TO SEE THE STACK
            ArrayList<ZombieCrushTile> stackTiles = data.getStackTiles();
            if (stackTiles.size() > 3) {
                renderTile(g, stackTiles.get(stackTiles.size() - 3));
                renderTile(g, stackTiles.get(stackTiles.size() - 4));
            }
            if (stackTiles.size() > 1) {
                renderTile(g, stackTiles.get(stackTiles.size() - 1));
                renderTile(g, stackTiles.get(stackTiles.size() - 2));
            }
        }

        // THEN DRAW THE GRID TILES BOTTOM TO TOP USING
        // THE TILE'S Z TO STAGGER THEM AND GIVE THE ILLUSION
        // OF DEPTH
        ArrayList<ZombieCrushTile>[][] tileGrid = data.getTileGrid();
        boolean noneOnLevel = false;
        int zIndex = 0;
        while (!noneOnLevel) {
            int levelCounter = 0;
            for (int i = 0; i < data.getGridColumns(); i++) {
                for (int j = 0; j < data.getGridRows(); j++) {
                    if (tileGrid[i][j].size() > zIndex) {
                        ZombieCrushTile tile = tileGrid[i][j].get(zIndex);
                        renderTile(g, tile);
                        levelCounter++;
                    }
                }
            }
            if (levelCounter == 0) {
                noneOnLevel = true;
            }
            zIndex++;
        }

        // THEN DRAW ALL THE MOVING TILES
        Iterator<ZombieCrushTile> movingTiles = data.getMovingTiles();
        while (movingTiles.hasNext()) {
            ZombieCrushTile tile = movingTiles.next();
            renderTile(g, tile);
        }
    }

    /**
     * Renders all the game tiles, doing so carefully such that they are
     * rendered in the proper order.
     *
     * @param g the Graphics context of this panel.
     */
    public void renderTile(Graphics g, ZombieCrushTile tileToRender) {
        // ONLY RENDER VISIBLE TILES
        if (!tileToRender.getState().equals(INVISIBLE_STATE)) {
            // FIRST DRAW THE BLANK TILE IMAGE
            if (tileToRender.getState().equals(SELECTED_STATE)) {
                g.drawImage(blankTileSelectedImage, (int) tileToRender.getX(), (int) tileToRender.getY(), null);
            } else if (tileToRender.getState().equals(VISIBLE_STATE)) {
                g.drawImage(blankTileImage, (int) tileToRender.getX(), (int) tileToRender.getY(), null);
            }

            // THEN THE TILE IMAGE
            SpriteType bgST = tileToRender.getSpriteType();
            Image img = bgST.getStateImage(tileToRender.getState());
            g.drawImage(img, (int) tileToRender.getX() + TILE_IMAGE_OFFSET, (int) tileToRender.getY() + TILE_IMAGE_OFFSET, bgST.getWidth(), bgST.getHeight(), null);

            // IF THE TILE IS SELECTED, HIGHLIGHT IT
            if (tileToRender.getState().equals(SELECTED_STATE)) {
                g.setColor(SELECTED_TILE_COLOR);
                g.fillRoundRect((int) tileToRender.getX(), (int) tileToRender.getY(), bgST.getWidth(), bgST.getHeight(), 5, 5);
            } else if (tileToRender.getState().equals(SMASH_STATE)) {
                g.setColor(INCORRECTLY_SELECTED_TILE_COLOR);
                g.fillRoundRect((int) tileToRender.getX(), (int) tileToRender.getY(), bgST.getWidth(), bgST.getHeight(), 5, 5);
            }
        }
    }

    /**
     * Renders the game dialog boxes.
     *
     * @param g This panel's graphics context.
     */
    public void renderDialogs(Graphics g) {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> dialogSprites = game.getGUIDialogs().values();
        for (Sprite s : dialogSprites) {
            // RENDER THE DIALOG, NOTE IT WILL ONLY DO IT IF IT'S VISIBLE
            renderSprite(g, s);
        }
    }

    private void renderLevelName(Graphics g) {

        if (((ZombieCrushMiniGame) game).getCurrentState() == RECORD_SCREEN_STATE) {
            if (ZombieCrushConstant.LEVELFILE != null) {
                String Level = ZombieCrushConstant.LEVELFILE;
                String[] level = Level.split("/");
                String LEVEL = level[5];
                String level1 = LEVEL.substring(0, LEVEL.length() - 4);
                int x = 100;
                int y = 150;
                Font font = new Font("Dialog", Font.BOLD, 30);
                g.setFont(font);
                g.drawString(level1, x, y);
                /*
                 * INSTRUCTION OF LEVEL1
                 */
                if (level1.equals("LEVEL1")) {
                    String in = "To pass this level, you should aim for at least 300 points in 6 moves. ";

                    Font font2 = new Font("Dialog", Font.ITALIC, 24);
                    g.setFont(font2);
                    g.drawString(in, 100, 250);
                    g.drawString("When the player has no more moves left, all special candies will be activated to give extra points", 100, 270);
                    g.drawString("and the depending on the number of moves left, striped candies will be created", 100, 290);
                }
            }
        }

    }

    /**
     * This method renders grid lines in the game tile grid to help during
     * debugging.
     *
     * @param g Graphics context for this panel.
     */
    public void renderGrid(Graphics g) {
        // ONLY RENDER THE GRID IF WE'RE DEBUGGING
        if (data.isDebugTextRenderingActive()) {
            for (int i = 0; i < data.getGridColumns(); i++) {
                for (int j = 0; j < data.getGridRows(); j++) {
                    int x = data.calculateTileXInGrid(i, 0);
                    int y = data.calculateTileYInGrid(j, 0);
                    g.drawRect(x, y, TILE_IMAGE_WIDTH, TILE_IMAGE_HEIGHT);
                }
            }
        }
    }

    public void renderDebuggingText(Graphics g) {
        // IF IT'S ACTIVATED
        if (data.isDebugTextRenderingActive()) {
            // ENABLE PROPER RENDER SETTINGS
            g.setFont(DEBUG_TEXT_FONT);
            g.setColor(DEBUG_TEXT_COLOR);

            // GO THROUGH ALL THE DEBUG TEXT
            Iterator<String> it = data.getDebugText().iterator();
            int x = data.getDebugTextX();
            int y = data.getDebugTextY();
            while (it.hasNext()) {
                // RENDER THE TEXT
                String text = it.next();
                g.drawString(text, x, y);
                y += 20;
            }
        }
    }
}
