package zombiecrushsaga.data;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;
import static zombiecrushsaga.ZombieCrushConstant.*;
import zombiecrushsaga.ZombieCrushSaga.ZombieCrushPropertyType;
import zombiecrushsaga.ui.ZombieCrushMiniGame;
import zombiecrushsaga.ui.ZombieCrushPanel;
import zombiecrushsaga.ui.ZombieCrushTile;

public class ZombieCrushDataModel extends MiniGameDataModel {
    // THIS CLASS HAS A REFERERENCE TO THE MINI GAME SO THAT IT
    // CAN NOTIFY IT TO UPDATE THE DISPLAY WHEN THE DATA MODEL CHANGES

    private MiniGame miniGame;
    // THE LEVEL GRID REFERS TO THE LAYOUT FOR A GIVEN LEVEL, MEANING
    // HOW MANY TILES FIT INTO EACH CELL WHEN FIRST STARTING A LEVEL
    private int[][] levelGrid;
    // LEVEL GRID DIMENSIONS
    private int gridColumns;
    private int gridRows;
    int[] bottom = new int[gridColumns];
    // THIS STORES THE TILES ON THE GRID DURING THE GAME
    private ArrayList<ZombieCrushTile>[][] tileGrid;
    // THESE ARE THE TILES THE PLAYER HAS MATCHED
    private ArrayList<ZombieCrushTile> stackTiles;
    // THESE ARE THE TILES THAT ARE MOVING AROUND, AND SO WE HAVE TO UPDATE
    private ArrayList<ZombieCrushTile> movingTiles;
    // THIS IS A SELECTED TILE, MEANING THE FIRST OF A PAIR THE PLAYER
    // IS TRYING TO MATCH. THERE CAN ONLY BE ONE OF THESE AT ANY TIME
    private ZombieCrushTile selectedTile;
    // THE INITIAL LOCATION OF TILES BEFORE BEING PLACED IN THE GRID
    private int unassignedTilesX;
    private int unassignedTilesY;
    // THESE ARE USED FOR TIMING THE GAME
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;
    // THE REFERENCE TO THE FILE BEING PLAYED
    private String currentLevel;
    private int playTillLevel;
    ArrayList<ZombieCrushTile> stack1;
    ArrayList<ZombieCrushTile> stack2;

    private int moveCount;
    private int moveResetCount;
    private int score;

    public int moveType;
    public int targetScore;
    private int star2Score;
    private int star3Score;
    private int highestScore;
    private int earnSocre;

    public int getEearnedScore() {
        return earnSocre;
    }

    public void setTargetScore(int i) {
        targetScore = i;
    }

    public int getTargetScore() {
        return targetScore;
    }

    public void setMoveCount(int i) {
        moveCount = i;
        moveResetCount = moveCount;
    }

    public void resetGameData() {
        moveCount = moveResetCount;
        score = 0;
    }

    public void setStar2Score(int i) {
        star2Score = i;
    }

    public int getStar2Score() {
        return star2Score;
    }

    public void setStar3Score(int i) {
        star3Score = i;
    }

    public int getStar3Score() {
        return star3Score;
    }

    public void setHighestScore(int i) {
        highestScore = i;
    }

    /**
     * Constructor for initializing this data model, it will create the data
     * structures for storing tiles, but not the tile grid itself, that is
     * dependent of file loading, and so should be subsequently initialized.
     *
     * @param initMiniGame The Mahjong game UI.
     */
    public ZombieCrushDataModel(MiniGame initMiniGame) {
        // KEEP THE GAME FOR LATER
        miniGame = initMiniGame;
        moveCount = 3;
        score = 0;
        // INIT THESE FOR HOLDING MATCHED AND MOVING TILES
        stackTiles = new ArrayList();
        movingTiles = new ArrayList();
        hasSpecialType = 0;

        targetScore = 780;
        playTillLevel = 1;

    }

   
  

    public int getMove() {
        return moveCount;
    }

    public int getScore() {
        return score;
    }

    public int getCurrentLevelCouldPLay() {
        return playTillLevel;
    }
    // INIT METHODS - AFTER CONSTRUCTION, THESE METHODS SETUP A GAME FOR USE
    // - initTiles
    // - initTile
    // - initLevelGrid
    // - initSpriteType
    /**
     * This method loads the tiles, creating an individual sprite for each. Note
     * that tiles may be of various types, which is important during the tile
     * matching tests.
     */
    int spriteTypeID = 0;
    int[] R_id = new int[6];

    public void initTiles() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(ZombieCrushPropertyType.IMG_PATH);

        SpriteType sT;

//        // WE'LL RENDER ALL THE TILES ON TOP OF THE BLANK TILE
//        String blankTileFileName = props.getProperty(ZombieCrushPropertyType.BLANK_TILE_IMAGE_NAME);
//        BufferedImage blankTileImage = miniGame.loadImageWithColorKey(imgPath + blankTileFileName, COLOR_KEY);
//        ((ZombieCrushPanel) (miniGame.getCanvas())).setBlankTileImage(blankTileImage);
//
//        // THIS IS A HIGHLIGHTED BLANK TILE FOR WHEN THE PLAYER SELECTS ONE
//        String blankTileSelectedFileName = props.getProperty(ZombieCrushPropertyType.BLANK_TILE_SELECTED_IMAGE_NAME);
//        BufferedImage blankTileSelectedImage = miniGame.loadImageWithColorKey(imgPath + blankTileSelectedFileName, COLOR_KEY);
//        ((ZombieCrushPanel) (miniGame.getCanvas())).setBlankTileSelectedImage(blankTileSelectedImage);
        // AND THEN TYPE C, FOR WHICH THERE ARE 4 OF EACH 
        // THIS IS ANALOGOUS TO THE CHARACTER AND NUMBER TILES IN FLAVORLESS MAHJONG
        ArrayList<String> typeCTiles = props.getPropertyOptionsList(ZombieCrushPropertyType.TYPE_C_TILES);
        for (int i = 0; i < typeCTiles.size(); i++) {
            String imgFile = imgPath + typeCTiles.get(i);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + String.valueOf(spriteTypeID));
            for (int j = 0; j < 8; j++) {
                initTile(sT, TILE_C_TYPE);
            }
            R_id[i] = spriteTypeID;
            spriteTypeID++;
        }

        ArrayList<String> typeS4HTiles = props.getPropertyOptionsList(ZombieCrushPropertyType.S4H_TYPE_TILE);
        for (int i = 0; i < typeS4HTiles.size(); i++) {
            S4H_id[i] = spriteTypeID;
            spriteTypeID++;
        }
        ArrayList<String> typeS4VTiles = props.getPropertyOptionsList(ZombieCrushPropertyType.S4V_TYPE_TILE);
        for (int i = 0; i < typeS4VTiles.size(); i++) {
            String imgFile = imgPath + typeCTiles.get(i);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + spriteTypeID);
            S4V_id[i] = spriteTypeID;
            spriteTypeID++;
        }
        ArrayList<String> typeS5Tiles = props.getPropertyOptionsList(ZombieCrushPropertyType.S5_TYPE_TILE);
        for (int i = 0; i < typeS5Tiles.size(); i++) {
            String imgFile = imgPath + typeCTiles.get(i);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + spriteTypeID);
            S5_id = spriteTypeID;
            spriteTypeID++;
        }
        ArrayList<String> typeTLTiles = props.getPropertyOptionsList(ZombieCrushPropertyType.T_TYPE_TILE);

        for (int i = 0; i < typeTLTiles.size(); i++) {
            String imgFile = imgPath + typeCTiles.get(i);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + spriteTypeID);
            TL_id = spriteTypeID;
            spriteTypeID++;
        }

    }

    int[] S4V_id = new int[6];
    int[] S4H_id = new int[6];
    int S5_id;
    int TL_id;

    public void initSpecialTile1(int a, int x, int y, ArrayList<ZombieCrushTile> tile, int id) {

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(ZombieCrushPropertyType.IMG_PATH);

        SpriteType sT;

        if (a == 40) {
            ArrayList<String> typeCTiles = props.getPropertyOptionsList(ZombieCrushPropertyType.S4H_TYPE_TILE);
            String imgFile = imgPath + typeCTiles.get(id);

            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + String.valueOf(S4H_id[id]));
            ZombieCrushTile newTile = new ZombieCrushTile(sT, this.calculateTileXInGrid(x, 0), this.calculateTileYInGrid(y, 0), 0, 0, VISIBLE_STATE, S4H_TYPE);
            tile.add(newTile);
            newTile.setGridCell(x, y);
            hasSpecialType++;
            return;
        }
        if (a == 41) {
            ArrayList<String> typeCTiles = props.getPropertyOptionsList(ZombieCrushPropertyType.S4V_TYPE_TILE);

            String imgFile = imgPath + typeCTiles.get(id);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + String.valueOf(S4V_id[id]));
            ZombieCrushTile newTile = new ZombieCrushTile(sT, this.calculateTileXInGrid(x, 0), this.calculateTileYInGrid(y, 0), 0, 0, VISIBLE_STATE, S4V_TYPE);

            tile.add(newTile);
            newTile.setGridCell(x, y);

            hasSpecialType++;
            return;
        }
        if (a == 5) {
            ArrayList<String> typeCTiles = props.getPropertyOptionsList(ZombieCrushPropertyType.S5_TYPE_TILE);
            String imgFile = imgPath + typeCTiles.get(0);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + String.valueOf(S5_id));
            ZombieCrushTile newTile = new ZombieCrushTile(sT, this.calculateTileXInGrid(x, 0), this.calculateTileYInGrid(y, 0), 0, 0, VISIBLE_STATE, S5_TYPE);
            tile.add(newTile);
            newTile.setGridCell(x, y);
            hasSpecialType++;
            return;
        }
        if (a == 3) {
            ArrayList<String> typeCTiles = props.getPropertyOptionsList(ZombieCrushPropertyType.T_TYPE_TILE);
            String imgFile = imgPath + typeCTiles.get(0);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + String.valueOf(TL_id));
            ZombieCrushTile newTile = new ZombieCrushTile(sT, this.calculateTileXInGrid(x, 0), this.calculateTileYInGrid(y, 0), 0, 0, VISIBLE_STATE, T_TYPE);
            tile.add(newTile);
            newTile.setGridCell(x, y);
            hasSpecialType++;
            return;
        }
    }

    public void initTile1() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(ZombieCrushPropertyType.IMG_PATH);

        SpriteType sT;

        // WE'LL RENDER ALL THE TILES ON TOP OF THE BLANK TILE
//        String blankTileFileName = props.getProperty(ZombieCrushPropertyType.BLANK_TILE_IMAGE_NAME);
//        BufferedImage blankTileImage = miniGame.loadImageWithColorKey(imgPath + blankTileFileName, COLOR_KEY);
//        ((ZombieCrushPanel) (miniGame.getCanvas())).setBlankTileImage(blankTileImage);
        ArrayList<String> typeCTiles = props.getPropertyOptionsList(ZombieCrushPropertyType.TYPE_C_TILES);
//        for (int i = 0; i < typeCTiles.size(); i++)
//        {
        int i = (int) (Math.random() * 5);
        String imgFile = imgPath + typeCTiles.get(i);
        sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + R_id[i]);
        ZombieCrushTile newTile = new ZombieCrushTile(sT, unassignedTilesX, unassignedTilesY, 0, 0, VISIBLE_STATE, TILE_C_TYPE);

        stackTiles.add(newTile);

    }

    /**
     * Helper method for loading the tiles, it constructs the prescribed tile
     * type using the provided sprite type.
     *
     * @param sT The sprite type to use to represent this tile during rendering.
     *
     * @param tileType The type of tile. Note that there are 3 broad categories.
     */
    private void initTile(SpriteType sT, String tileType) {
        // CONSTRUCT THE TILE
        ZombieCrushTile newTile = new ZombieCrushTile(sT, unassignedTilesX, unassignedTilesY, 0, 0, INVISIBLE_STATE, tileType);

        // AND ADD IT TO THE STACK
        stackTiles.add(newTile);
    }

    /**
     * Called after a level has been selected, it initializes the grid so that
     * it is the proper dimensions.
     *
     * @param initGrid The grid distribution of tiles, where each cell specifies
     * the number of tiles to be stacked in that cell.
     *
     * @param initGridColumns The columns in the grid for the level selected.
     *
     * @param initGridRows The rows in the grid for the level selected.
     */
    public void initLevelGrid(int[][] initGrid, int initGridColumns, int initGridRows) {
        // KEEP ALL THE GRID INFO
        levelGrid = initGrid;
        gridColumns = initGridColumns;
        gridRows = initGridRows;

        // AND BUILD THE TILE GRID FOR STORING THE TILES
        // SINCE WE NOW KNOW ITS DIMENSIONS
        tileGrid = new ArrayList[gridColumns][gridRows];
        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < gridRows; j++) {
                // EACH CELL HAS A STACK OF TILES, WE'LL USE
                // AN ARRAY LIST FOR THE STACK
                tileGrid[i][j] = new ArrayList();
            }
        }
        // MAKE ALL THE TILES VISIBLE
        enableTiles(true);
    }

    /**
     * This helper method initializes a sprite type for a tile or set of similar
     * tiles to be created.
     */
    private SpriteType initTileSpriteType(String imgFile, String spriteTypeID) {
        // WE'LL MAKE A NEW SPRITE TYPE FOR EACH GROUP OF SIMILAR LOOKING TILES
        SpriteType sT = new SpriteType(spriteTypeID);
        addSpriteType(sT);

        // LOAD THE ART
        BufferedImage img = miniGame.loadImageWithColorKey(imgFile, COLOR_KEY);
        Image tempImage = img.getScaledInstance(TILE_IMAGE_WIDTH, TILE_IMAGE_HEIGHT, BufferedImage.SCALE_SMOOTH);
        img = new BufferedImage(TILE_IMAGE_WIDTH, TILE_IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        img.getGraphics().drawImage(tempImage, 0, 0, null);

        // WE'LL USE THE SAME IMAGE FOR ALL STATES
        sT.addState(INVISIBLE_STATE, img);
        sT.addState(VISIBLE_STATE, img);
        sT.addState(SELECTED_STATE, img);
        sT.addState(SMASH_STATE, img);
        return sT;
    }

    // ACCESSOR METHODS
    /**
     * Accessor method for getting the level currently being played.
     *
     * @return The level name used currently for the game screen.
     */
    public String getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Accessor method for getting the number of tile columns in the game grid.
     *
     * @return The number of columns (left to right) in the grid for the level
     * currently loaded.
     */
    public int getGridColumns() {
        return gridColumns;
    }

    /**
     * Accessor method for getting the number of tile rows in the game grid.
     *
     * @return The number of rows (top to bottom) in the grid for the level
     * currently loaded.
     */
    public int getGridRows() {
        return gridRows;
    }

    /**
     * Accessor method for getting the tile grid, which has all the tiles the
     * user may select from.
     *
     * @return The main 2D grid of tiles the user selects tiles from.
     */
    public ArrayList<ZombieCrushTile>[][] getTileGrid() {
        return tileGrid;
    }

    /**
     * Accessor method for getting the stack tiles.
     *
     * @return The stack tiles, which are the tiles the matched tiles are placed
     * in.
     */
    public ArrayList<ZombieCrushTile> getStackTiles() {
        return stackTiles;
    }

    /**
     * Accessor method for getting the moving tiles.
     *
     * @return The moving tiles, which are the tiles currently being animated as
     * they move around the game.
     */
    public Iterator<ZombieCrushTile> getMovingTiles() {
        return movingTiles.iterator();
    }

    /**
     * Mutator method for setting the currently loaded level.
     *
     * @param initCurrentLevel The level name currently being used to play the
     * game.
     */
    public void setCurrentLevel(String initCurrentLevel) {
        currentLevel = initCurrentLevel;
    }

    /**
     * Used to calculate the x-axis pixel location in the game grid for a tile
     * placed at column with stack position z.
     *
     * @param column The column in the grid the tile is located.
     *
     * @param z The level of the tile in the stack at the given grid location.
     *
     * @return The x-axis pixel location of the tile
     */
    public int calculateTileXInGrid(int column, int z) {
        int cellWidth = TILE_IMAGE_WIDTH;
        float leftEdge = miniGame.getBoundaryLeft();
        return (int) (leftEdge + (cellWidth * column) - (Z_TILE_OFFSET * z));
    }

    /**
     * Used to calculate the y-axis pixel location in the game grid for a tile
     * placed at row with stack position z.
     *
     * @param row The row in the grid the tile is located.
     *
     * @param z The level of the tile in the stack at the given grid location.
     *
     * @return The y-axis pixel location of the tile
     */
    public int calculateTileYInGrid(int row, int z) {
        int cellHeight = TILE_IMAGE_HEIGHT;
        float topEdge = miniGame.getBoundaryTop();
        return (int) (topEdge + (cellHeight * row) - (Z_TILE_OFFSET * z));
    }

    /**
     * Used to calculate the grid column for the x-axis pixel location.
     *
     * @param x The x-axis pixel location for the request.
     *
     * @return The column that corresponds to the x-axis location x.
     */
    public int calculateGridCellColumn(int x) {
        float leftEdge = miniGame.getBoundaryLeft();
        x = (int) (x - leftEdge);
        return x / TILE_IMAGE_WIDTH;
    }

    /**
     * Used to calculate the grid row for the y-axis pixel location.
     *
     * @param y The y-axis pixel location for the request.
     *
     * @return The row that corresponds to the y-axis location y.
     */
    public int calculateGridCellRow(int y) {
        float topEdge = miniGame.getBoundaryTop();
        y = (int) (y - topEdge);
        return y / TILE_IMAGE_HEIGHT;
    }

    // TIME TEXT METHODS
    // - timeToText
    // - gameTimeToText
    /**
     * This method creates and returns a textual description of the timeInMillis
     * argument as a time duration in the format of (H:MM:SS).
     *
     * @param timeInMillis The time to be represented textually.
     *
     * @return A textual representation of timeInMillis.
     */
    public String timeToText(long timeInMillis) {
        // FIRST CALCULATE THE NUMBER OF HOURS,
        // SECONDS, AND MINUTES
        long hours = timeInMillis / MILLIS_IN_AN_HOUR;
        timeInMillis -= hours * MILLIS_IN_AN_HOUR;
        long minutes = timeInMillis / MILLIS_IN_A_MINUTE;
        timeInMillis -= minutes * MILLIS_IN_A_MINUTE;
        long seconds = timeInMillis / MILLIS_IN_A_SECOND;

        // THEN ADD THE TIME OF GAME SUMMARIZED IN PARENTHESES
        String minutesText = "" + minutes;
        if (minutes < 10) {
            minutesText = "0" + minutesText;
        }
        String secondsText = "" + seconds;
        if (seconds < 10) {
            secondsText = "0" + secondsText;
        }
        return hours + ":" + minutesText + ":" + secondsText;
    }

    /**
     * This method builds and returns a textual representation of the game time.
     * Note that the game may still be in progress.
     *
     * @return The duration of the current game represented textually.
     */
    public String gameTimeToText() {
        // CALCULATE GAME TIME USING HOURS : MINUTES : SECONDS
        if ((startTime == null) || (endTime == null)) {
            return "";
        }
        long timeInMillis = endTime.getTimeInMillis() - startTime.getTimeInMillis();
        return timeToText(timeInMillis);
    }

    public void smashTile() {
        tileGrid[this.getCurrentSelectedTileX()][this.getCurrentSelectedTileY()].remove(0);
        selectedTile = null;

    }
    // GAME DATA SERVICE METHODS
    // -enableTiles
    // -findMove
    // -moveAllTilesToStack
    // -moveTiles
    // -playWinAnimation
    // -processMove
    // -selectTile
    // -undoLastMove
    /**
     * This method can be used to make all of the tiles either visible (true) or
     * invisible (false). This should be used when switching between the splash
     * and game screens.
     *
     * @param enable Specifies whether the tiles should be made visible or not.
     */
    public void enableTiles(boolean enable) {
        // PUT ALL THE TILES IN ONE PLACE WHERE WE CAN PROCESS THEM TOGETHER
        moveAllTilesToStack();

        // GO THROUGH ALL OF THEM 
        for (ZombieCrushTile tile : stackTiles) {
            // AND SET THEM PROPERLY
            if (enable) {
                tile.setState(VISIBLE_STATE);
            } else {
                tile.setState(INVISIBLE_STATE);
            }
        }
    }

    public int[] checkbottom() {

        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < gridRows; j++) {
                if (tileGrid[i][j].size() > 0) {
                    if (tileGrid[i][j + 1].size() == 0) {
                        bottom[i] = j;
                    }
                }
            }
        }
        return bottom;
    }

    /**
     * This method examines the current game grid and finds and returns a valid
     * move that is available.
     *
     * @return A move that can be made, or null if none exist.
     */
    public ZombieCrushMove findInitialMove() {
        // MAKE A MOVE TO FILL IN 
        ZombieCrushMove move = new ZombieCrushMove();

        // GO THROUGH THE ENTIRE GRID TO FIND A MATCH BETWEEN AVAILABLE TILES
        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < gridRows; j++) {
                if (tileGrid[i][j].size() > 0) {
                    if (!((ZombieCrushTile) tileGrid[i][j].get(0)).getSpriteType().equals(TILE_C_TYPE)) {
                        j++;
                    }
                    //match T 1
                    if (i < (gridColumns - 2) && j < gridRows - 2) {

                        if (tileGrid[i][j].size() > 0 && tileGrid[i + 1][j].size() > 0 && tileGrid[i + 2][j].size() > 0
                                && tileGrid[i + 1][j + 1].size() > 0 && tileGrid[i + 1][j + 2].size() > 0) {
                            if (tileGrid[i][j].get(0).match(tileGrid[i + 1][j].get(0))
                                    && tileGrid[i + 1][j].get(0).match(tileGrid[i + 2][j].get(0))
                                    && tileGrid[i + 1][j].get(0).match(tileGrid[i + 1][j + 1].get(0))
                                    && tileGrid[i + 1][j + 1].get(0).match(tileGrid[i + 1][j + 2].get(0))) {
                                move.col1 = i;
                                move.row1 = j;
                                move.col2 = i + 1;
                                move.row2 = j;
                                move.col3 = i + 2;
                                move.row3 = j;
                                move.col4 = i + 1;
                                move.row4 = j + 1;
                                move.col5 = i + 1;
                                move.row5 = j + 2;
                                score += 20;
                                return move;
                            }
                        }

                        //match T 2
                        if (tileGrid[i][j].size() > 0 && tileGrid[i + 1][j].size() > 0 && tileGrid[i + 2][j].size() > 0
                                && tileGrid[i + 1][j + 1].size() > 0 && tileGrid[i + 1][j + 2].size() > 0) {
                            if (tileGrid[i][j].get(0).match(tileGrid[i + 1][j].get(0))
                                    && tileGrid[i + 1][j].get(0).match(tileGrid[i + 2][j].get(0))
                                    && tileGrid[i + 1][j].get(0).match(tileGrid[i + 1][j + 1].get(0))
                                    && tileGrid[i + 1][j + 1].get(0).match(tileGrid[i + 1][j + 2].get(0))) {
                                move.col1 = i;
                                move.row1 = j;
                                move.col2 = i + 1;
                                move.row2 = j;
                                move.col3 = i + 2;
                                move.row3 = j;
                                move.col4 = i + 1;
                                move.row4 = j + 1;
                                move.col5 = i + 1;
                                move.row5 = j + 2;
                                score += 20;
                                return move;
                            }
                        }

                        //match T 3
                        if (tileGrid[i][j].size() > 0 && tileGrid[i + 1][j + 1].size() > 0 && tileGrid[i + 2][j + 1].size() > 0
                                && tileGrid[i][j + 1].size() > 0 && tileGrid[i][j + 2].size() > 0) {
                            if (tileGrid[i][j].get(0).match(tileGrid[i + 1][j + 1].get(0))
                                    && tileGrid[i + 1][j + 1].get(0).match(tileGrid[i + 2][j + 1].get(0))
                                    && tileGrid[i][j].get(0).match(tileGrid[i][j + 1].get(0))
                                    && tileGrid[i][j + 1].get(0).match(tileGrid[i][j + 2].get(0))) {
                                move.col1 = i;
                                move.row1 = j;
                                move.col2 = i + 1;
                                move.row2 = j + 1;
                                move.col3 = i + 2;
                                move.row3 = j + 1;
                                move.col4 = i;
                                move.row4 = j + 1;
                                move.col5 = i;
                                move.row5 = j + 2;
                                score += 20;
                                return move;
                            }
                        }

                        //match T 4
                        if (tileGrid[i][j + 1].size() > 0 && tileGrid[i + 1][j + 1].size() > 0 && tileGrid[i + 2][j + 1].size() > 0
                                && tileGrid[i + 2][j].size() > 0 && tileGrid[i + 2][j + 2].size() > 0) {
                            if (tileGrid[i][j + 1].get(0).match(tileGrid[i + 1][j + 1].get(0))
                                    && tileGrid[i + 1][j + 1].get(0).match(tileGrid[i + 2][j + 1].get(0))
                                    && tileGrid[i + 2][j + 1].get(0).match(tileGrid[i + 2][j].get(0))
                                    && tileGrid[i + 2][j].get(0).match(tileGrid[i + 2][j + 2].get(0))) {
                                move.col1 = i;
                                move.row1 = j + 1;
                                move.col2 = i + 1;
                                move.row2 = j + 1;
                                move.col3 = i + 2;
                                move.row3 = j;
                                move.col4 = i + 2;
                                move.row4 = j + 2;
                                move.col5 = i + 2;
                                move.row5 = j + 1;
                                score += 20;
                                return move;
                            }
                        }

                        //match L 1
                        if (tileGrid[i][j + 2].size() > 0 && tileGrid[i + 1][j].size() > 0 && tileGrid[i + 2][j + 2].size() > 0
                                && tileGrid[i + 1][j + 1].size() > 0 && tileGrid[i + 1][j + 2].size() > 0) {
                            if (tileGrid[i][j + 2].get(0).match(tileGrid[i + 1][j].get(0))
                                    && tileGrid[i + 1][j].get(0).match(tileGrid[i + 2][j + 2].get(0))
                                    && tileGrid[i + 1][j].get(0).match(tileGrid[i + 1][j + 1].get(0))
                                    && tileGrid[i + 1][j + 1].get(0).match(tileGrid[i + 1][j + 2].get(0))) {
                                move.col1 = i;
                                move.row1 = j + 2;
                                move.col2 = i + 1;
                                move.row2 = j;
                                move.col3 = i + 2;
                                move.row3 = j + 2;
                                move.col4 = i + 1;
                                move.row4 = j + 1;
                                move.col5 = i + 1;
                                move.row5 = j + 2;
                                score += 20;
                                return move;
                            }
                        }

                        //match L 2
                        if (tileGrid[i][j].size() > 0 && tileGrid[i + 1][j].size() > 0 && tileGrid[i + 2][j].size() > 0
                                && tileGrid[i][j + 1].size() > 0 && tileGrid[i][j + 2].size() > 0) {
                            if (tileGrid[i][j].get(0).match(tileGrid[i + 1][j].get(0))
                                    && tileGrid[i + 1][j].get(0).match(tileGrid[i + 2][j].get(0))
                                    && tileGrid[i][j].get(0).match(tileGrid[i][j + 1].get(0))
                                    && tileGrid[i][j + 1].get(0).match(tileGrid[i][j + 2].get(0))) {
                                move.col1 = i;
                                move.row1 = j;
                                move.col2 = i + 1;
                                move.row2 = j;
                                move.col3 = i + 2;
                                move.row3 = j;
                                move.col4 = i;
                                move.row4 = j + 1;
                                move.col5 = i;
                                move.row5 = j + 2;
                                score += 20;
                                return move;
                            }
                        }

                        //match L 3
                        if (tileGrid[i][j].size() > 0 && tileGrid[i + 1][j + 2].size() > 0 && tileGrid[i + 2][j + 2].size() > 0
                                && tileGrid[i][j + 1].size() > 0 && tileGrid[i][j + 2].size() > 0) {
                            if (tileGrid[i][j + 2].get(0).match(tileGrid[i + 1][j + 2].get(0))
                                    && tileGrid[i + 1][j + 2].get(0).match(tileGrid[i + 2][j + 2].get(0))
                                    && tileGrid[i][j].get(0).match(tileGrid[i][j + 1].get(0))
                                    && tileGrid[i][j + 1].get(0).match(tileGrid[i][j + 2].get(0))) {
                                move.col1 = i;
                                move.row1 = j;
                                move.col2 = i + 1;
                                move.row2 = j + 2;
                                move.col3 = i + 2;
                                move.row3 = j + 2;
                                move.col4 = i;
                                move.row4 = j + 1;
                                move.col5 = i;
                                move.row5 = j + 2;
                                score += 20;
                                return move;
                            }
                        }

                        //match L 4
                        if (tileGrid[i + 2][j].size() > 0 && tileGrid[i + 1][j + 2].size() > 0 && tileGrid[i + 2][j + 2].size() > 0
                                && tileGrid[i + 2][j + 1].size() > 0 && tileGrid[i][j + 2].size() > 0) {
                            if (tileGrid[i][j + 2].get(0).match(tileGrid[i + 1][j + 2].get(0))
                                    && tileGrid[i + 1][j + 2].get(0).match(tileGrid[i + 2][j + 2].get(0))
                                    && tileGrid[i + 2][j].get(0).match(tileGrid[i + 2][j + 1].get(0))
                                    && tileGrid[i + 2][j + 1].get(0).match(tileGrid[i][j + 2].get(0))) {
                                move.col1 = i + 2;
                                move.row1 = j;
                                move.col2 = i + 1;
                                move.row2 = j + 2;
                                move.col3 = i + 2;
                                move.row3 = j + 2;
                                move.col4 = i + 2;
                                move.row4 = j + 1;
                                move.col5 = i;
                                move.row5 = j + 2;
                                score += 20;
                                return move;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < gridRows; j++) {
                if (tileGrid[i][j].size() > 0) {
                    //match 5
                    if (i < (gridColumns - 4)) {
                        if (tileGrid[i][j].size() > 0 && tileGrid[i + 1][j].size() > 0 && tileGrid[i + 2][j].size() > 0
                                && tileGrid[i + 3][j].size() > 0 && tileGrid[i + 4][j].size() > 0) {
                            if (tileGrid[i][j].get(0).match(tileGrid[i + 1][j].get(0))
                                    && tileGrid[i + 1][j].get(0).match(tileGrid[i + 2][j].get(0))
                                    && tileGrid[i + 2][j].get(0).match(tileGrid[i + 3][j].get(0))
                                    && tileGrid[i + 3][j].get(0).match(tileGrid[i + 4][j].get(0))) {
                                move.col1 = i;
                                move.row1 = j;
                                move.col2 = i + 1;
                                move.row2 = j;
                                move.col3 = i + 2;
                                move.row3 = j;
                                move.col4 = i + 3;
                                move.row4 = j;
                                move.col5 = i + 4;
                                move.row5 = j;
                                score += 20;
                                return move;
                            }
                        }
                    }
                    if (j < gridRows - 4) {
                        if (tileGrid[i][j].size() > 0 && tileGrid[i][j + 1].size() > 0 && tileGrid[i][j + 2].size() > 0
                                && tileGrid[i][j + 3].size() > 0 && tileGrid[i][j + 4].size() > 0) {
                            if (tileGrid[i][j].get(0).match(tileGrid[i][j + 1].get(0))
                                    && tileGrid[i][j + 1].get(0).match(tileGrid[i][j + 2].get(0))
                                    && tileGrid[i][j + 2].get(0).match(tileGrid[i][j + 3].get(0))
                                    && tileGrid[i][j + 3].get(0).match(tileGrid[i][j + 4].get(0))) {
                                move.col1 = i;
                                move.row1 = j;
                                move.col2 = i;
                                move.row2 = j + 1;
                                move.col3 = i;
                                move.row3 = j + 2;
                                move.col4 = i;
                                move.row4 = j + 3;
                                move.col5 = i;
                                move.row5 = j + 4;
                                score += 20;
                                return move;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < gridRows; j++) {
                if (tileGrid[i][j].size() > 0) {
                    //match 4
                    if (i < (gridColumns - 3)) {
                        if (tileGrid[i][j].size() > 0 && tileGrid[i + 1][j].size() > 0 && tileGrid[i + 2][j].size() > 0 && tileGrid[i + 3][j].size() > 0) {
                            if (tileGrid[i][j].get(0).match(tileGrid[i + 1][j].get(0))
                                    && tileGrid[i + 1][j].get(0).match(tileGrid[i + 2][j].get(0))
                                    && tileGrid[i + 2][j].get(0).match(tileGrid[i + 3][j].get(0))) {
                                move.col1 = i;
                                move.row1 = j;
                                move.col2 = i + 1;
                                move.row2 = j;
                                move.col3 = i + 2;
                                move.row3 = j;
                                move.col4 = i + 3;
                                move.row4 = j;

                                score += 20;
                                return move;
                            }
                        }
                    }
                    if (j < gridRows - 3) {
                        if (tileGrid[i][j].size() > 0 && tileGrid[i][j + 1].size() > 0 && tileGrid[i][j + 2].size() > 0 && tileGrid[i][j + 3].size() > 0) {
                            if (tileGrid[i][j].get(0).match(tileGrid[i][j + 1].get(0))
                                    && tileGrid[i][j + 1].get(0).match(tileGrid[i][j + 2].get(0))
                                    && tileGrid[i][j + 2].get(0).match(tileGrid[i][j + 3].get(0))) {
                                move.col1 = i;
                                move.row1 = j;
                                move.col2 = i;
                                move.row2 = j + 1;
                                move.col3 = i;
                                move.row3 = j + 2;
                                move.col4 = i;
                                move.row4 = j + 3;

                                score += 20;
                                return move;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < gridRows; j++) {
                if (tileGrid[i][j].size() > 0) {
                    //match 3
                    if (i < (gridColumns - 2)) {
                        if (tileGrid[i][j].size() > 0 && tileGrid[i + 1][j].size() > 0 && tileGrid[i + 2][j].size() > 0) {
                            if (tileGrid[i][j].get(0).match(tileGrid[i + 1][j].get(0))
                                    && tileGrid[i + 1][j].get(0).match(tileGrid[i + 2][j].get(0))) {
                                move.col1 = i;
                                move.row1 = j;
                                move.col2 = i + 1;
                                move.row2 = j;
                                move.col3 = i + 2;
                                move.row3 = j;
                                score += 10;
                                return move;
                            }
                        }
                    }
                    if (j < gridRows - 2) {
                        if (tileGrid[i][j].size() > 0 && tileGrid[i][j + 1].size() > 0 && tileGrid[i][j + 2].size() > 0) {
                            if (tileGrid[i][j].get(0).match(tileGrid[i][j + 1].get(0))
                                    && tileGrid[i][j + 1].get(0).match(tileGrid[i][j + 2].get(0))) {
                                move.col1 = i;
                                move.row1 = j;
                                move.col2 = i;
                                move.row2 = j + 1;
                                move.col3 = i;
                                move.row3 = j + 2;
                                score += 10;
                                return move;
                            }
                        }
                    }
                    // DO THEY MATCH

                }
            }
        }
        // WE'VE SEARCHED THE ENTIRE GRID AND THERE
        // ARE NO POSSIBLE MOVES REMAINING
        return null;
    }

    public void fixGridMove() {

        int k;
        // GO THROUGH THE ENTIRE GRID TO FIND A MATCH BETWEEN AVAILABLE TILES
        for (int i = 0; i < gridColumns; i++) {
            for (int j = gridRows - 2; j >= 0; j--) {
                if (tileGrid[i][j].size() == 1) {
                    k = 0;
                    while (k + 1 + j < gridRows && tileGrid[i][j + k + 1].isEmpty() && ((j + k + 1) < gridRows)) {
                        k++;
                    }
                    if (k != 0) {
                        ZombieCrushTile tile = tileGrid[i][j].remove(0);
                        tile.setTarget(tile.getX(), tile.getY() + k * 55);
                        movingTiles.add(tile);
                        tile.startMovingToTarget(MAX_TILE_VELOCITY);
                        tile.setGridCell(i, j + k);

                        tileGrid[i][j + k].add(tile);
                    }

                }
            }

        }
        for (int i = 0; i < gridColumns; i++) {
            int j = 0;
            k = 0;
            while ((j + k + 1) < gridRows && tileGrid[i][j + k].size() == 0) {
                initTile1();
                k++;
            }

            while (k > 0) {
                ZombieCrushTile tile = stackTiles.remove(stackTiles.size() - 1);
                tile.setTarget(this.calculateTileXInGrid(i, 0), this.calculateTileYInGrid(j + k - 1, 0));
                movingTiles.add(tile);
                tile.setGridCell(i, j + k - 1);
                tile.startMovingToTarget(MAX_TILE_VELOCITY);
                tileGrid[i][j + k - 1].add(tile);
                k--;
            }

        }
    }

    /**
     * This method moves all the tiles not currently in the stack to the stack.
     */
    public void moveAllTilesToStack() {
        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < gridRows; j++) {
                ArrayList<ZombieCrushTile> cellStack = tileGrid[i][j];
                moveTiles(cellStack, stackTiles);
            }
        }
    }

    public void moveAllTilesToStack1() {
        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < gridRows; j++) {
                while (tileGrid[i][j].size() != 0) {
                    tileGrid[i][j].remove(0);
                }

            }
        }
    }

    /**
     * This method removes all the tiles in from argument and moves them to
     * argument.
     *
     * @param from The source data structure of tiles.
     *
     * @param to The destination data structure of tiles.
     */
    private void moveTiles(ArrayList<ZombieCrushTile> from, ArrayList<ZombieCrushTile> to) {
        // GO THROUGH ALL THE TILES, TOP TO BOTTOM
        for (int i = from.size() - 1; i >= 0; i--) {
            ZombieCrushTile tile = from.remove(i);

            // ONLY ADD IT IF IT'S NOT THERE ALREADY
            if (!to.contains(tile)) {
                to.add(tile);
            }
        }
    }

    /**
     * This method sets up and starts the animation shown after a game is won.
     */
    public void playWinAnimation() {
        // MAKE A NEW PATH
        ArrayList<Integer> winPath = new ArrayList();

        // THIS HAS THE APPROXIMATE PATH NODES, WHICH WE'LL SLIGHTLY
        // RANDOMIZE FOR EACH TILE FOLLOWING THE PATH.
        winPath.add(getGameWidth() - WIN_PATH_COORD);
        winPath.add(WIN_PATH_COORD);
        winPath.add(WIN_PATH_COORD);
        winPath.add(WIN_PATH_COORD);
        winPath.add(WIN_PATH_COORD);
        winPath.add(getGameHeight() - WIN_PATH_COORD);
        winPath.add(getGameWidth() - WIN_PATH_COORD);
        winPath.add(getGameHeight() - WIN_PATH_COORD);

        // START THE ANIMATION FOR ALL THE TILES
        for (int i = 0; i < stackTiles.size(); i++) {
            // GET EACH TILE
            ZombieCrushTile tile = stackTiles.get(i);

            // MAKE SURE IT'S MOVED EACH FRAME
            movingTiles.add(tile);

            // AND GET IT ON A PATH
            tile.initWinPath(winPath);
        }
    }

    /**
     * This method updates all the necessary state information to process the
     * move argument.
     *
     * @param move The move to make. Note that a move specifies the cell
     * locations for a match.
     */
    public void processMove(ZombieCrushMove move) {
        boolean special;
        int x = 100, y = 100, id = 0;

        ZombieCrushTile tile1 = tileGrid[move.col1][move.row1].remove(0);
        ZombieCrushTile tile2 = tileGrid[move.col2][move.row2].remove(0);
        ZombieCrushTile tile3 = tileGrid[move.col3][move.row3].remove(0);

        // MAKE SURE BOTH ARE UNSELECTED
        tile1.setState(VISIBLE_STATE);
        tile2.setState(VISIBLE_STATE);
        tile3.setState(VISIBLE_STATE);

        // SEND THEM TO THE STACK
        tile1.setTarget(TILE_STACK_X + TILE_STACK_OFFSET_X, TILE_STACK_Y + TILE_STACK_OFFSET_Y);
        tile1.startMovingToTarget(MAX_TILE_VELOCITY);
        tile2.setTarget(TILE_STACK_X + TILE_STACK_2_OFFSET_X, TILE_STACK_Y + TILE_STACK_OFFSET_Y);
        tile2.startMovingToTarget(MAX_TILE_VELOCITY);
        tile3.setTarget(TILE_STACK_X + TILE_STACK_2_OFFSET_X, TILE_STACK_Y + TILE_STACK_OFFSET_Y);
        tile3.startMovingToTarget(MAX_TILE_VELOCITY);

        // MAKE SURE THEY MOVE
        movingTiles.add(tile1);
        movingTiles.add(tile2);
        movingTiles.add(tile3);

        if (move.col4 != 100) {

            ZombieCrushTile tile4 = tileGrid[move.col4][move.row4].remove(0);

            tile4.setState(VISIBLE_STATE);
            tile4.setTarget(TILE_STACK_X + TILE_STACK_2_OFFSET_X, TILE_STACK_Y + TILE_STACK_OFFSET_Y);
            movingTiles.add(tile4);
            tile4.startMovingToTarget(MAX_TILE_VELOCITY);

        }
        if (move.col5 != 100) {

            ZombieCrushTile tile5 = tileGrid[move.col5][move.row5].remove(0);

            tile5.setState(VISIBLE_STATE);
            tile5.setTarget(TILE_STACK_X + TILE_STACK_2_OFFSET_X, TILE_STACK_Y + TILE_STACK_OFFSET_Y);
            movingTiles.add(tile5);
            tile5.startMovingToTarget(MAX_TILE_VELOCITY);

        }

        selectedTile = null;
        // NOW CHECK TO SEE IF THE GAME HAS EITHER BEEN WON OR LOST
        // HAS THE PLAYER WON?
        while (stackTiles.size() < 10) {
            initTile1();
        }

        if (score >= this.getStar3Score()||(score>=this.getTargetScore()&&moveCount<1)) {
            this.endGameAsWin();

        }
        if (moveCount < 1 && score < this.getTargetScore()) {
            this.endGameAsLoss();
        }
    }

    public ZombieCrushMove findTileMove(ArrayList<ZombieCrushTile> stack) {

        if (stack != null) {
            ZombieCrushMove move = new ZombieCrushMove();
            int i = stack.get(0).getGridColumn();
            int j = stack.get(0).getGridRow();
            if (tileGrid[i][j].size() > 0) {
//                if (hasSpecialType) {
//                    if (!tileGrid[i][j].get(0).getSpriteType().equals(TILE_C_TYPE)) {
//                        if (tileGrid[i][j].get(0).matchSpecial()) {
//
//                        }
//                    }
//                }

                //match L 2
                if (i < (gridColumns - 2) && j < gridRows - 2) {
                    if (tileGrid[i][j].size() > 0 && tileGrid[i + 1][j].size() > 0 && tileGrid[i + 2][j].size() > 0
                            && tileGrid[i][j + 1].size() > 0 && tileGrid[i][j + 2].size() > 0) {
                        if (tileGrid[i][j].get(0).match(tileGrid[i + 1][j].get(0))
                                && tileGrid[i + 1][j].get(0).match(tileGrid[i + 2][j].get(0))
                                && tileGrid[i][j].get(0).match(tileGrid[i][j + 1].get(0))
                                && tileGrid[i][j + 1].get(0).match(tileGrid[i][j + 2].get(0))) {
                            move.col1 = i;
                            move.row1 = j;
                            move.col2 = i + 1;
                            move.row2 = j;
                            move.col3 = i + 2;
                            move.row3 = j;
                            move.col4 = i;
                            move.row4 = j + 1;
                            move.col5 = i;
                            move.row5 = j + 2;
                            score += 300;
                            earnSocre = 300;
                            moveType = 3;
                            return move;
                        }
                    }
                }

                if (i > 0 && i < (gridColumns - 1) && j < (gridRows - 2)) {
                    //match T 1

                    if (tileGrid[i][j].size() > 0 && tileGrid[i + 1][j].size() > 0 && tileGrid[i - 1][j].size() > 0
                            && tileGrid[i][j + 1].size() > 0 && tileGrid[i][j + 2].size() > 0) {
                        if (tileGrid[i][j].get(0).match(tileGrid[i + 1][j].get(0))
                                && tileGrid[i + 1][j].get(0).match(tileGrid[i - 1][j].get(0))
                                && tileGrid[i][j].get(0).match(tileGrid[i][j + 1].get(0))
                                && tileGrid[i][j + 1].get(0).match(tileGrid[i][j + 2].get(0))) {
                            move.col1 = i;
                            move.row1 = j;
                            move.col2 = i + 1;
                            move.row2 = j;
                            move.col3 = i - 1;
                            move.row3 = j;
                            move.col4 = i;
                            move.row4 = j + 1;
                            move.col5 = i;
                            move.row5 = j + 2;
                            score += 300;
                            earnSocre = 300;
                            moveType = 3;
                            return move;
                        }
                    }
                }
                //match T 2
                if (i > 0 && i < (gridColumns - 1) && j > 1) {
                    if (tileGrid[i][j].size() > 0 && tileGrid[i + 1][j].size() > 0 && tileGrid[i - 1][j].size() > 0
                            && tileGrid[i][j - 1].size() > 0 && tileGrid[i][j - 2].size() > 0) {
                        if (tileGrid[i][j].get(0).match(tileGrid[i + 1][j].get(0))
                                && tileGrid[i][j].get(0).match(tileGrid[i - 1][j].get(0))
                                && tileGrid[i][j].get(0).match(tileGrid[i][j - 1].get(0))
                                && tileGrid[i][j - 1].get(0).match(tileGrid[i][j - 2].get(0))) {
                            move.col1 = i;
                            move.row1 = j;
                            move.col2 = i + 1;
                            move.row2 = j;
                            move.col3 = i - 1;
                            move.row3 = j;
                            move.col4 = i;
                            move.row4 = j - 1;
                            move.col5 = i;
                            move.row5 = j - 2;
                            score += 300;
                            earnSocre = 300;
                            moveType = 3;
                            return move;
                        }
                    }
                }
                //match T 3
                if (i < (gridColumns - 2) && j < (gridRows - 1) && j > 0) {
                    if (tileGrid[i][j].size() > 0 && tileGrid[i + 1][j].size() > 0 && tileGrid[i + 2][j].size() > 0
                            && tileGrid[i][j + 1].size() > 0 && tileGrid[i][j - 1].size() > 0) {
                        if (tileGrid[i][j].get(0).match(tileGrid[i + 1][j].get(0))
                                && tileGrid[i + 1][j].get(0).match(tileGrid[i + 2][j].get(0))
                                && tileGrid[i][j].get(0).match(tileGrid[i][j + 1].get(0))
                                && tileGrid[i][j + 1].get(0).match(tileGrid[i][j - 1].get(0))) {
                            move.col1 = i;
                            move.row1 = j;
                            move.col2 = i + 1;
                            move.row2 = j;
                            move.col3 = i + 2;
                            move.row3 = j;
                            move.col4 = i;
                            move.row4 = j + 1;
                            move.col5 = i;
                            move.row5 = j - 1;
                            score += 300;
                            earnSocre = 300;
                            moveType = 3;
                            return move;
                        }
                    }
                }

                //match T 4
                if (i > 1 && j < (gridRows - 1) && j > 0) {
                    if (tileGrid[i][j + 1].size() > 0 && tileGrid[i][j - 1].size() > 0 && tileGrid[i - 2][j].size() > 0
                            && tileGrid[i][j].size() > 0 && tileGrid[i - 1][j].size() > 0) {
                        if (tileGrid[i][j + 1].get(0).match(tileGrid[i][j].get(0))
                                && tileGrid[i][j + 1].get(0).match(tileGrid[i][j - 1].get(0))
                                && tileGrid[i][j].get(0).match(tileGrid[i - 2][j].get(0))
                                && tileGrid[i - 2][j].get(0).match(tileGrid[i - 1][j].get(0))) {
                            move.col1 = i;
                            move.row1 = j + 1;
                            move.col2 = i;
                            move.row2 = j - 1;
                            move.col3 = i;
                            move.row3 = j;
                            move.col4 = i - 2;
                            move.row4 = j;
                            move.col5 = i;
                            move.row5 = j + 1;
                            score += 300;
                            earnSocre = 300;
                            moveType = 3;
                            return move;
                        }
                    }
                }
                //match L 1
                if (i > 1 && j < (gridRows - 2)) {
                    if (tileGrid[i][j].size() > 0 && tileGrid[i - 1][j].size() > 0 && tileGrid[i - 2][j].size() > 0
                            && tileGrid[i][j + 1].size() > 0 && tileGrid[i][j + 2].size() > 0) {
                        if (tileGrid[i][j].get(0).match(tileGrid[i - 1][j].get(0))
                                && tileGrid[i - 1][j].get(0).match(tileGrid[i - 2][j].get(0))
                                && tileGrid[i][j].get(0).match(tileGrid[i][j + 1].get(0))
                                && tileGrid[i][j + 1].get(0).match(tileGrid[i][j + 2].get(0))) {
                            move.col1 = i;
                            move.row1 = j + 2;
                            move.col2 = i;
                            move.row2 = j;
                            move.col3 = i;
                            move.row3 = j + 1;
                            move.col4 = i - 1;
                            move.row4 = j;
                            move.col5 = i - 2;
                            move.row5 = j;
                            score += 300;
                            earnSocre = 300;
                            moveType = 3;
                            return move;
                        }
                    }

                }
                //match L 3
                if (i < (gridColumns - 2) && j > 1) {
                    if (tileGrid[i][j].size() > 0 && tileGrid[i + 1][j].size() > 0 && tileGrid[i + 2][j].size() > 0
                            && tileGrid[i][j - 1].size() > 0 && tileGrid[i][j - 2].size() > 0) {
                        if (tileGrid[i][j].get(0).match(tileGrid[i + 1][j].get(0))
                                && tileGrid[i + 1][j].get(0).match(tileGrid[i + 2][j].get(0))
                                && tileGrid[i][j].get(0).match(tileGrid[i][j - 1].get(0))
                                && tileGrid[i][j - 1].get(0).match(tileGrid[i][j - 2].get(0))) {
                            move.col1 = i;
                            move.row1 = j;
                            move.col2 = i + 1;
                            move.row2 = j;
                            move.col3 = i + 2;
                            move.row3 = j;
                            move.col4 = i;
                            move.row4 = j - 1;
                            move.col5 = i;
                            move.row5 = j - 2;
                            score += 300;
                            earnSocre = 300;
                            moveType = 3;
                            return move;
                        }
                    }
                }
                //match L 4
                if (i > 1 && j > 1) {
                    if (tileGrid[i - 2][j].size() > 0 && tileGrid[i - 1][j].size() > 0 && tileGrid[i][j].size() > 0
                            && tileGrid[i][j - 1].size() > 0 && tileGrid[i][j - 2].size() > 0) {
                        if (tileGrid[i - 2][j].get(0).match(tileGrid[i - 1][j].get(0))
                                && tileGrid[i - 1][j].get(0).match(tileGrid[i][j].get(0))
                                && tileGrid[i][j].get(0).match(tileGrid[i][j - 1].get(0))
                                && tileGrid[i][j - 1].get(0).match(tileGrid[i][j - 2].get(0))) {
                            move.col1 = i - 2;
                            move.row1 = j;
                            move.col2 = i - 1;
                            move.row2 = j;
                            move.col3 = i;
                            move.row3 = j - 2;
                            move.col4 = i;
                            move.row4 = j - 1;
                            move.col5 = i;
                            move.row5 = j;
                            score += 300;
                            earnSocre = 300;
                            moveType = 3;
                            return move;
                        }
                    }
                }
                //match 5 i
                if ((i < (gridColumns - 2)) && i > 1) {
                    if (tileGrid[i][j].size() > 0 && tileGrid[i + 1][j].size() > 0 && tileGrid[i + 2][j].size() > 0
                            && tileGrid[i - 1][j].size() > 0 && tileGrid[i - 2][j].size() > 0) {
                        if (tileGrid[i][j].get(0).match(tileGrid[i + 1][j].get(0))
                                && tileGrid[i + 1][j].get(0).match(tileGrid[i + 2][j].get(0))
                                && tileGrid[i + 2][j].get(0).match(tileGrid[i - 1][j].get(0))
                                && tileGrid[i - 1][j].get(0).match(tileGrid[i - 2][j].get(0))) {
                            move.col1 = i;
                            move.row1 = j;
                            move.col2 = i + 1;
                            move.row2 = j;
                            move.col3 = i + 2;
                            move.row3 = j;
                            move.col4 = i - 1;
                            move.row4 = j;
                            move.col5 = i - 2;
                            move.row5 = j;

                            earnSocre = 300;
                            moveType = 5;
                            return move;
                        }
                    }
                }
                //match 5 j
                if (j < gridRows - 2 && j > 1) {
                    if (tileGrid[i][j].size() > 0 && tileGrid[i][j + 1].size() > 0 && tileGrid[i][j + 2].size() > 0
                            && tileGrid[i][j - 1].size() > 0 && tileGrid[i][j - 2].size() > 0) {
                        if (tileGrid[i][j].get(0).match(tileGrid[i][j + 1].get(0))
                                && tileGrid[i][j + 1].get(0).match(tileGrid[i][j + 2].get(0))
                                && tileGrid[i][j + 2].get(0).match(tileGrid[i][j - 1].get(0))
                                && tileGrid[i][j - 1].get(0).match(tileGrid[i][j - 2].get(0))) {
                            move.col1 = i;
                            move.row1 = j;
                            move.col2 = i;
                            move.row2 = j + 1;
                            move.col3 = i;
                            move.row3 = j + 2;
                            move.col4 = i;
                            move.row4 = j - 1;
                            move.col5 = i;
                            move.row5 = j - 2;
                            score += 300;
                            earnSocre = 300;
                            moveType = 5;
                            return move;
                        }
                    }
                }

                //match 4 herizontal 3
                if (i < (gridColumns - 1) && i > 1) {
                    if (tileGrid[i][j].size() > 0 && tileGrid[i + 1][j].size() > 0 && tileGrid[i - 2][j].size() > 0 && tileGrid[i - 1][j].size() > 0) {
                        if (tileGrid[i][j].get(0).match(tileGrid[i + 1][j].get(0))
                                && tileGrid[i + 1][j].get(0).match(tileGrid[i - 2][j].get(0))
                                && tileGrid[i - 2][j].get(0).match(tileGrid[i - 1][j].get(0))) {
                            move.col1 = i;
                            move.row1 = j;
                            move.col2 = i + 1;
                            move.row2 = j;
                            move.col3 = i - 2;
                            move.row3 = j;
                            move.col4 = i - 1;
                            move.row4 = j;
                            score += 120;
                            earnSocre = 120;
                            moveType = 40;
                            return move;
                        }
                    }
                }

                //match 4 herizontal 2
                if (i < (gridColumns - 2) && i > 0) {
                    if (tileGrid[i][j].size() > 0 && tileGrid[i + 1][j].size() > 0 && tileGrid[i + 2][j].size() > 0 && tileGrid[i - 1][j].size() > 0) {
                        if (tileGrid[i][j].get(0).match(tileGrid[i + 1][j].get(0))
                                && tileGrid[i + 1][j].get(0).match(tileGrid[i + 2][j].get(0))
                                && tileGrid[i + 2][j].get(0).match(tileGrid[i - 1][j].get(0))) {
                            move.col1 = i;
                            move.row1 = j;
                            move.col2 = i + 1;
                            move.row2 = j;
                            move.col3 = i + 2;
                            move.row3 = j;
                            move.col4 = i - 1;
                            move.row4 = j;
                            score += 120;
                            earnSocre = 120;
                            moveType = 40;
                            return move;
                        }
                    }
                }
                //match 4 vertical 2
                if (j < gridRows - 2 && j > 0) {
                    if (tileGrid[i][j].size() > 0 && tileGrid[i][j + 1].size() > 0 && tileGrid[i][j + 2].size() > 0 && tileGrid[i][j - 1].size() > 0) {
                        if (tileGrid[i][j].get(0).match(tileGrid[i][j + 1].get(0))
                                && tileGrid[i][j + 1].get(0).match(tileGrid[i][j + 2].get(0))
                                && tileGrid[i][j + 2].get(0).match(tileGrid[i][j - 1].get(0))) {
                            move.col1 = i;
                            move.row1 = j;
                            move.col2 = i;
                            move.row2 = j + 1;
                            move.col3 = i;
                            move.row3 = j + 2;
                            move.col4 = i;
                            move.row4 = j - 1;
                            score += 120;
                            earnSocre = 120;
                            moveType = 41;
                            return move;
                        }
                    }
                }
                //match 4 vertical 3

                if (j < gridRows - 1 && j > 1) {
                    if (tileGrid[i][j].size() > 0 && tileGrid[i][j + 1].size() > 0 && tileGrid[i][j - 2].size() > 0 && tileGrid[i][j - 1].size() > 0) {
                        if (tileGrid[i][j].get(0).match(tileGrid[i][j + 1].get(0))
                                && tileGrid[i][j + 1].get(0).match(tileGrid[i][j - 2].get(0))
                                && tileGrid[i][j - 2].get(0).match(tileGrid[i][j - 1].get(0))) {
                            move.col1 = i;
                            move.row1 = j;
                            move.col2 = i;
                            move.row2 = j + 1;
                            move.col3 = i;
                            move.row3 = j - 2;
                            move.col4 = i;
                            move.row4 = j - 1;
                            score += 120;
                            earnSocre = 120;
                            moveType = 41;
                            return move;
                        }
                    }
                }

                if (i > 1) {
                    //match 3-horizontal 3
                    if (tileGrid[i][j].size() > 0 && tileGrid[i - 1][j].size() > 0 && tileGrid[i - 2][j].size() > 0) {
                        if (tileGrid[i][j].get(0).match(tileGrid[i - 1][j].get(0))
                                && tileGrid[i - 1][j].get(0).match(tileGrid[i - 2][j].get(0))) {
                            move.col1 = i;
                            move.row1 = j;
                            move.col2 = i - 1;
                            move.row2 = j;
                            move.col3 = i - 2;
                            move.row3 = j;
                            score += 60;
                            earnSocre = 60;
                            return move;
                        }
                    }
                }

                if (i < (gridColumns - 1) && i > 0) {
                    //match 3-horizontal 2
                    if (tileGrid[i][j].size() > 0 && tileGrid[i + 1][j].size() > 0 && tileGrid[i - 1][j].size() > 0) {
                        if (tileGrid[i][j].get(0).match(tileGrid[i + 1][j].get(0))
                                && tileGrid[i + 1][j].get(0).match(tileGrid[i - 1][j].get(0))) {
                            move.col1 = i;
                            move.row1 = j;
                            move.col2 = i + 1;
                            move.row2 = j;
                            move.col3 = i - 1;
                            move.row3 = j;
                            score += 60;
                            earnSocre = 60;
                            return move;
                        }
                    }
                }

                if (i < (gridColumns - 2)) {
                    //match 3-horizontal 1
                    if (tileGrid[i][j].size() > 0 && tileGrid[i + 1][j].size() > 0 && tileGrid[i + 2][j].size() > 0) {
                        if (tileGrid[i][j].get(0).match(tileGrid[i + 1][j].get(0))
                                && tileGrid[i + 1][j].get(0).match(tileGrid[i + 2][j].get(0))) {
                            move.col1 = i;
                            move.row1 = j;
                            move.col2 = i + 1;
                            move.row2 = j;
                            move.col3 = i + 2;
                            move.row3 = j;
                            score += 60;
                            earnSocre = 60;
                            return move;
                        }
                    }
                }
                //match 3 vertical 1
                if (j < gridRows - 2) {
                    if (tileGrid[i][j].size() > 0 && tileGrid[i][j + 1].size() > 0 && tileGrid[i][j + 2].size() > 0) {
                        if (tileGrid[i][j].get(0).match(tileGrid[i][j + 1].get(0))
                                && tileGrid[i][j + 1].get(0).match(tileGrid[i][j + 2].get(0))) {
                            move.col1 = i;
                            move.row1 = j;
                            move.col2 = i;
                            move.row2 = j + 1;
                            move.col3 = i;
                            move.row3 = j + 2;
                            score += 60;
                            earnSocre = 60;
                            return move;
                        }
                    }
                }
                //match 3 vertical 2

                if (j < gridRows - 1 && j > 0) {
                    if (tileGrid[i][j].size() > 0 && tileGrid[i][j + 1].size() > 0 && tileGrid[i][j - 1].size() > 0) {
                        if (tileGrid[i][j].get(0).match(tileGrid[i][j + 1].get(0))
                                && tileGrid[i][j + 1].get(0).match(tileGrid[i][j - 1].get(0))) {
                            move.col1 = i;
                            move.row1 = j;
                            move.col2 = i;
                            move.row2 = j + 1;
                            move.col3 = i;
                            move.row3 = j - 1;
                            score += 60;
                            earnSocre = 60;
                            return move;
                        }
                    }
                }
                //match 3 vertical 3

                if (j > 1) {
                    if (tileGrid[i][j].size() > 0 && tileGrid[i][j - 2].size() > 0 && tileGrid[i][j - 1].size() > 0) {
                        if (tileGrid[i][j].get(0).match(tileGrid[i][j - 2].get(0))
                                && tileGrid[i][j - 2].get(0).match(tileGrid[i][j - 1].get(0))) {
                            move.col1 = i;
                            move.row1 = j;
                            move.col2 = i;
                            move.row2 = j - 2;
                            move.col3 = i;
                            move.row3 = j - 1;
                            score += 60;
                            earnSocre = 60;
                            return move;
                        }
                    }
                }
            }
            return null;

        }
        return null;
    }

    public void processSwitchTwoTiles(ZombieCrushMove move) {

        // REMOVE THE MOVE TILES FROM THE GRID
        stack1 = tileGrid[move.col1][move.row1];
        stack2 = tileGrid[move.col2][move.row2];

        ZombieCrushTile tile1 = stack1.remove(0);
        ZombieCrushTile tile2 = stack2.remove(0);

        // MAKE SURE BOTH ARE UNSELECTED
        tile1.setState(VISIBLE_STATE);
        tile2.setState(VISIBLE_STATE);

        // SEND THEM TO THE STACK
//        tile1.setTarget(this.calculateTileXInGrid(tile2.getGridColumn(), 0) ,this.calculateTileYInGrid(tile2.getGridRow(), 0));
//        tile2.setTarget(this.calculateTileXInGrid(tile1.getGridColumn(), 0) ,this.calculateTileYInGrid(tile1.getGridRow(), 0));
        tile1.setTarget(tile2.getX(), tile2.getY());
        tile2.setTarget(tile1.getX(), tile1.getY());

        movingTiles.add(tile1);
        movingTiles.add(tile2);

        tile1.startMovingToTarget(MIN_TILE_VELOCITY);
        tile2.startMovingToTarget(MIN_TILE_VELOCITY);

        tile1.setGridCell(move.col2, move.row2);
        tile2.setGridCell(move.col1, move.row1);
        stack2.add(tile1);
        stack1.add(tile2);

//         ZombieCrushMove move = data.processInitialMove();
//            if (move != null)
//                data.processMove(move);
//                          fixGridMove();
    }

    int hasSpecialType = 0;

    public void specialTileActivate(String n, int i, int j) {

        if (n.equals(S4H_TYPE)) {
            for (int k = 0; k < this.gridColumns; k++) {
                if (!tileGrid[k][j].isEmpty()) {
                    tileGrid[k][j].remove(0);
                }
            }
        }
        if (n.equals(S4V_TYPE)) {
            for (int k = 0; k < this.gridRows; k++) {
                if (!tileGrid[i][k].isEmpty()) {
                    tileGrid[i][k].remove(0);
                }
            }
        }

        if (n.equals(T_TYPE)) {
            if (i > 1 && i < this.gridColumns - 1 && j > 1 && j < this.gridRows - 1) {
                if (!tileGrid[i][j].isEmpty()) {
                    tileGrid[i][j].remove(0);
                }
                if (!tileGrid[i + 1][j].isEmpty()) {
                    tileGrid[i + 1][j].remove(0);
                }
                if (!tileGrid[i - 1][j].isEmpty()) {
                    tileGrid[i - 1][j].remove(0);
                }
                if (!tileGrid[i][j + 1].isEmpty()) {
                    tileGrid[i][j + 1].remove(0);
                }
                if (!tileGrid[i][j - 1].isEmpty()) {
                    tileGrid[i][j - 1].remove(0);
                }
                if (!tileGrid[i + 1][j + 1].isEmpty()) {
                    tileGrid[i + 1][j + 1].remove(0);
                }
                if (!tileGrid[i - 1][j + 1].isEmpty()) {
                    tileGrid[i - 1][j + 1].remove(0);
                }
                if (!tileGrid[i + 1][j - 1].isEmpty()) {
                    tileGrid[i + 1][j - 1].remove(0);
                }
                if (!tileGrid[i - 1][j - 1].isEmpty()) {
                    tileGrid[i - 1][j - 1].remove(0);
                }
            }
        }

    }

    public void special5Activate(ZombieCrushTile stack) {
//        String a = stack.getSpriteType().getSpriteTypeID();
//        String c = (a.substring(5, a.length() - 1));
//        int id = Integer.valueOf(c) - 48;

        for (int n = 0; n < this.gridColumns; n++) {
            for (int m = 0; m < this.gridRows; m++) {
                String strid = stack.getSpriteType().getSpriteTypeID();
                if (!tileGrid[n][m].isEmpty()) {
                    if (tileGrid[n][m].get(0).getSpriteType().getSpriteTypeID().equals(strid)) {

                        tileGrid[n][m].remove(0);
                    }

                }
            }
        }
    }

    public ZombieCrushTile getTheTile() {
        return theTile;
    }
    public float theTileX;

    public float getTheTileX() {
        return theTileX;
    }
    public float theTileY;

    public float getTheTileY() {
        return theTileY;
    }
    public boolean mouseSelect;
    boolean helperMove = false;
    ZombieCrushTile theTile;

    public boolean findMoveHelper() {
        //CHECKING TILE IF IT IS MATCH
        if (stack1 != null && stack2 != null) {

            //DEAL WITH SPECIAL CASE 
            if (hasSpecialType > 0) {
                ZombieCrushMove moves;
                if (!stack1.get(0).getTileType().equals(TILE_C_TYPE)
                        || !stack2.get(0).getTileType().equals(TILE_C_TYPE)) {

                    int theX = 0, theY = 0, theX1 = 0, theY1 = 0;
                    ZombieCrushTile tile1 = stack1.get(0);
                    ZombieCrushTile tile2 = stack2.get(0);
                    String type1 = null;
                    String type2 = null;
                    if (!tile1.getTileType().equals(TILE_C_TYPE)) {
                        type1 = tile1.getTileType();

                        theX = stack1.get(0).getGridColumn();
                        theY = stack1.get(0).getGridRow();
                        theTile = stack1.get(0);

                    }

                    if (!tile2.getTileType().equals(TILE_C_TYPE)) {
                        type2 = tile2.getTileType();

                        theX1 = stack2.get(0).getGridColumn();
                        theY1 = stack2.get(0).getGridRow();
                        theTile = stack2.get(0);

                    }

                    stack1.remove(0);
                    stack2.remove(0);
                    tile1.setState(VISIBLE_STATE);
                    tile2.setState(VISIBLE_STATE);

                    // SEND THEM TO THE STACK
                    tile1.setTarget(TILE_STACK_X + TILE_STACK_OFFSET_X, TILE_STACK_Y + TILE_STACK_OFFSET_Y);
                    tile1.startMovingToTarget(MAX_TILE_VELOCITY);
                    tile2.setTarget(TILE_STACK_X + TILE_STACK_2_OFFSET_X, TILE_STACK_Y + TILE_STACK_OFFSET_Y);
                    tile2.startMovingToTarget(MAX_TILE_VELOCITY);

                    // MAKE SURE THEY MOVE
                    movingTiles.add(tile1);
                    movingTiles.add(tile2);

                    moves = null;
                    helperMove = true;
                    stack1 = null;
                    stack2 = null;
                    moveType = 0;
                    this.moveCount--;

                    //ACTIVE SPECIAL
                    if (type1 != null) {
                        if (type1.equals(S5_TYPE)) {
                            this.special5Activate(tile2);
                        } else {
                            this.specialTileActivate(type1, theX, theY);

                        }
                        theTileX = this.calculateTileXInGrid(theX, 0);
                        theTileY = this.calculateTileYInGrid(theY, 0);

                        type1 = null;
                        hasSpecialType--;
                    }
                    if (type2 != null) {
                        if (type2.equals(S5_TYPE)) {
                            this.special5Activate(tile1);
                        } else {
                            this.specialTileActivate(type2, theX1, theY1);

                        }
                        theTileX = this.calculateTileXInGrid(theX1, 0);
                        theTileY = this.calculateTileYInGrid(theY1, 0);

                        type2 = null;
                        hasSpecialType--;
                    }
                    fixGridMove();
                    return true;
                }
            } else {
                //REGUALER CASE
                ZombieCrushMove move2, move3;
                move2 = findTileMove(stack1);

                if (move2 != null) {
                    //DEAL WITH SPECIAL CASE
                    int x2 = stack1.get(0).getGridColumn();
                    int y2 = stack1.get(0).getGridRow();
                    //TO PRINT OUT THE FLOAT POINT

                    char c = (stack1.get(0).getSpriteType().getSpriteTypeID().charAt(5));
                    int id = Integer.valueOf(c) - 48;
                    theTile = stack1.get(0);

                    this.processMove(move2);
                    theTileX = this.calculateTileXInGrid(x2, 0);
                    theTileY = this.calculateTileYInGrid(y2, 0);
                    if (moveType != 0) {
                        initSpecialTile1(moveType, x2, y2, stack1, id);
                    }

                    fixGridMove();
                    move2 = null;
                    helperMove = true;
                    stack1 = null;
                    moveType = 0;
                    this.moveCount--;

                    return true;
                }
                move3 = findTileMove(stack2);
                if (move3 != null) {
                    int x3 = stack2.get(0).getGridColumn();
                    int y3 = stack2.get(0).getGridRow();
                    //TO PRINT OUT THE FLOAT POINT

                    char c = (stack2.get(0).getSpriteType().getSpriteTypeID().charAt(5));
                    int id = Integer.valueOf(c) - 48;
                    theTile = stack2.get(0);

                    this.processMove(move3);

                    theTileX = this.calculateTileXInGrid(x3, 0);
                    theTileY = this.calculateTileYInGrid(y3, 0);
                    if (moveType != 0) {
                        initSpecialTile1(moveType, x3, y3, stack2, id);
                    }
                    fixGridMove();
                    move3 = null;
                    helperMove = true;
                    stack2 = null;
                    moveType = 0;
                    this.moveCount--;
                    return true;
                }

                if (move3 == null && move2 == null) {
                    //  this.undoLastMove();
                    stack1 = null;
                    stack2 = null;

                }

            }
        }
        return false;
    }

    /**
     * This method attempts to select the selectTile argument. Note that this
     * may be the first or second selected tile. If a tile is already selected,
     * it will attempt to process a match/move.
     *
     * @param selectTile The tile to select.
     */
    public void selectTile(ZombieCrushTile selectTile) {

        // IF IT'S ALREADY THE SELECTED TILE, DESELECT IT
        if (selectTile == selectedTile) {
            selectedTile = null;
            selectTile.setState(VISIBLE_STATE);
            return;
        }
        if (selectedTile != null) {
            selectedTile.setState(VISIBLE_STATE);

            ZombieCrushMove move = new ZombieCrushMove();
            move.col1 = selectedTile.getGridColumn();
            move.row1 = selectedTile.getGridRow();
            move.col2 = selectTile.getGridColumn();
            move.row2 = selectTile.getGridRow();

            processSwitchTwoTiles(move);

            selectedTile = null;
            return;

        }
        // IF THE TILE IS NOT AT THE TOP OF ITS STACK, DO NOTHING
//        int col = selectTile.getGridColumn();
//        int row = selectTile.getGridRow();

//        // IF THE TILE IS NOT FREE, DO NOTHING, BUT MAKE SURE WE GIVE FEEDBACK
//        if ((col > 0) && (col < (gridColumns - 1)))
//        {
//            int leftZ = tileGrid[col-1][row].size();
//            int z = tileGrid[col][row].size();
//            int rightZ = tileGrid[col+1][row].size();
//            if ((z <= leftZ) && (z <= rightZ))
//            {
//                // IF IT'S ALREADY INCORRECTLY SELECTED, DEACTIVATE THE FEEDBACK
//                if (selectTile.getState().equals(INCORRECTLY_SELECTED_STATE))
//                {
//                    selectTile.setState(VISIBLE_STATE);
//                    return;
//                }
//            }
//        }
        // IT'S FREE
        if (selectedTile == null) {
            selectedTile = selectTile;
            selectedTile.setState(SELECTED_STATE);
        }
    }

    /**
     * This method undoes the previous move, sending the two tiles on top of the
     * tile stack back to the game grid.
     */
    public void undoLastMove() {
        if (stack1 != null && stack2 != null) {
            if (inProgress() && stack1.size() > 0 && stack2.size() > 0) {
                // TAKE THE TOP 2 TILES
                ZombieCrushTile topTile = stack1.remove(0);
                ZombieCrushTile nextToTopTile = stack2.remove(0);

                // SET THEIR DESTINATIONS
                float boundaryLeft = miniGame.getBoundaryLeft();
                float boundaryTop = miniGame.getBoundaryTop();

                // FIRST TILE 1
                int col = topTile.getGridColumn();
                int row = topTile.getGridRow();
                int col2 = nextToTopTile.getGridColumn();
                int row2 = nextToTopTile.getGridRow();

                int z = tileGrid[col][row].size();
                float targetX = this.calculateTileXInGrid(col, z);
                float targetY = this.calculateTileYInGrid(row, z);
                nextToTopTile.setTarget(targetX, targetY);
                movingTiles.add(nextToTopTile);
                nextToTopTile.startMovingToTarget(MAX_TILE_VELOCITY);
                tileGrid[col][row].add(nextToTopTile);
                nextToTopTile.setGridCell(col, row);

                z = tileGrid[col2][row2].size();
                targetX = this.calculateTileXInGrid(col2, z);
                targetY = this.calculateTileYInGrid(row2, z);
                topTile.setTarget(targetX, targetY);
                movingTiles.add(topTile);
                topTile.startMovingToTarget(MAX_TILE_VELOCITY);
                tileGrid[col2][row2].add(topTile);
                topTile.setGridCell(col2, row2);

            }
        }
    }
    // OVERRIDDEN METHODS
    // - checkMousePressOnSprites
    // - endGameAsWin
    // - endGameAsLoss
    // - reset
    // - updateAll
    // - updateDebugText
    /**
     * This method provides a custom game response for handling mouse clicks on
     * the game screen. We'll use this to close game dialogs as well as to
     * listen for mouse clicks on grid cells.
     *
     * @param game The Mahjong game.
     *
     * @param x The x-axis pixel location of the mouse click.
     *
     * @param y The y-axis pixel location of the mouse click.
     */

    public void checkMousePressOnSprites(MiniGame game, int x, int y) {
        if (x <= gameWidth && x >= 0 && y <= gameHeight && y >= 0) {
            int col = calculateGridCellColumn(x);
            int row = calculateGridCellRow(y);

            // CHECK THE TOP OF THE STACK AT col, row
            ArrayList<ZombieCrushTile> tileStack = tileGrid[col][row];

            if (tileStack.size() > 0) {
                // GET AND TRY TO SELECT THE TOP TILE IN THAT CELL, IF THERE IS ONE
                ZombieCrushTile testTile = tileStack.get(0);

                if (testTile.containsPoint(x, y)) {
                    selectTile(testTile);
                    // if(moved) 
//                       fixGridMove();
                }
            }
        }
    }
    int col11, row11;

    public int getCurrentSelectedTileX() {
        return col11;
    }

    public int getCurrentSelectedTileY() {
        return row11;
    }

    @Override
    public void checkMousePressOnSprites(MiniGame game, int x, int y, int x1, int y1) {
        // FIGURE OUT THE CELL IN THE GRID
        if (x <= gameWidth && x >= 0 && y <= gameHeight && y >= 0) {
            int col = calculateGridCellColumn(x);
            int row = calculateGridCellRow(y);
            col11 = col;
            row11 = row;
            int col1 = calculateGridCellColumn(x1);
            int row1 = calculateGridCellRow(y1);
            if (col < this.gridColumns && col1 < this.gridColumns
                    && row < this.gridRows && row1 < this.gridRows
                    && col >= 0 && col1 >= 0 && row >= 0 & row1 >= 0) {
                if (col1 == col && row == row1) {
                    // CHECK THE TOP OF THE STACK AT col, row
                    ArrayList<ZombieCrushTile> tileStack = tileGrid[col][row];

                    if (tileStack.size() > 0) {
                        // GET AND TRY TO SELECT THE TOP TILE IN THAT CELL, IF THERE IS ONE
                        ZombieCrushTile testTile = tileStack.get(0);

                        if (testTile.containsPoint(x, y)) {
//                            if(Smash){
//                                testTile.setState(SMASH_STATE);
//                              //  movingTiles.add(testTile);
//                                tileGrid[col][row].remove(0);
//                                Smash=false;
//                            }
//                            else
                            if (selectedTile != null) {
                                if (((selectedTile.getGridColumn() == (testTile.getGridColumn() - 1)
                                        || selectedTile.getGridColumn() == (testTile.getGridColumn() + 1))
                                        && selectedTile.getGridRow() == (testTile.getGridRow()))
                                        || ((selectedTile.getGridRow() == (testTile.getGridRow() - 1)
                                        || selectedTile.getGridRow() == (testTile.getGridRow() + 1))
                                        && selectedTile.getGridColumn() == (testTile.getGridColumn()))) {
                                    selectTile(testTile);
                                }
                            } else {
                                selectTile(testTile);
                            }
//                   if(moved) 
//                       fixGridMove();
                        }
                    }
                } else {
                    // CHECK THE TOP OF THE STACK AT col, row
                    ArrayList<ZombieCrushTile> tileStack = tileGrid[col][row];
                    ArrayList<ZombieCrushTile> tileStack1 = tileGrid[col1][row1];

                    if (tileStack.size() > 0 && tileStack1.size() > 0) {
                        // GET AND TRY TO SELECT THE TOP TILE IN THAT CELL, IF THERE IS ONE
                        ZombieCrushTile testTile = tileStack.get(0);
                        ZombieCrushTile testTile1 = tileStack1.get(0);
                        if (testTile.containsPoint(x, y) && testTile1.containsPoint(x1, y1)) {
                            selectTile(testTile);
                            selectTile(testTile1);
//                   if(moved) 
//                       fixGridMove();
                        }
                    }
                }
            }
        }

    }

    /**
     * Called when the game is won, it will record the ending game time, update
     * the player record, display the win dialog, and play the win animation.
     */
    @Override
    public void endGameAsWin() {
        // UPDATE THE GAME STATE USING THE INHERITED FUNCTIONALITY
        super.endGameAsWin();

        // RECORD IT AS A WIN
        //     ((ZombieCrushMiniGame) miniGame).getPlayerRecord().addWin(currentLevel, gameTime);
        ((ZombieCrushMiniGame) miniGame).savePlayerRecordb();
        // DISPLAY THE WIN DIALOG
        miniGame.getGUIDialogs().get(WIN_DIALOG_TYPE).setState(VISIBLE_STATE);
 
        // AND PLAY THE WIN ANIMATION
        //  playWinAnimation();
        playTillLevel += 1;
        miniGame.endUsingData();
    }

    @Override
    public void endGameAsLoss() {
        // UPDATE THE GAME STATE USING THE INHERITED FUNCTIONALITY
        super.endGameAsLoss();

        // RECORD IT AS A WIN
        //     ((ZombieCrushMiniGame) miniGame).getPlayerRecord().addWin(currentLevel, gameTime);
        ((ZombieCrushMiniGame) miniGame).savePlayerRecordb();
        // DISPLAY THE WIN DIALOG
        miniGame.getGUIDialogs().get(LOSS_DIALOG_TYPE).setState(VISIBLE_STATE);

        miniGame.getGUIButtons().get(TRY_AGAIN_BUTTON_TYPE).setState(VISIBLE_STATE);
        miniGame.getGUIButtons().get(TRY_AGAIN_BUTTON_TYPE).setEnabled(true);

    }
    /**
     * Called when a game is started, the game grid is reset.
     *
     * @param game
     */
    int finishedReset = 0;
    boolean isRunning = false;
    public boolean cheat = false;

    @Override
    public void reset(MiniGame game) {
        // PUT ALL THE TILES IN ONE PLACE AND MAKE THEM VISIBLE
        moveAllTilesToStack();
        for (ZombieCrushTile tile : stackTiles) {
            tile.setX(TILE_STACK_X);
            tile.setY(TILE_STACK_Y);
            tile.setState(VISIBLE_STATE);
        }

        // RANDOMLY ORDER THEM
        Collections.shuffle(stackTiles);

        // START THE CLOCK
        startTime = new GregorianCalendar();

        // NOW LET'S REMOVE THEM FROM THE STACK
        // AND PUT THE TILES IN THE GRID        
        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < gridRows; j++) {
                for (int k = 0; k < levelGrid[i][j]; k++) {
                    // TAKE THE TILE OUT OF THE STACK
                    ZombieCrushTile tile = stackTiles.remove(stackTiles.size() - 1);

                    // PUT IT IN THE GRID
                    tileGrid[i][j].add(tile);
                    tile.setGridCell(i, j);

                    // WE'LL ANIMATE IT GOING TO THE GRID, SO FIGURE
                    // OUT WHERE IT'S GOING AND GET IT MOVING
                    float x = calculateTileXInGrid(i, k);
                    float y = calculateTileYInGrid(j, k);
                    tile.setTarget(x, y);
                    movingTiles.add(tile);
                    tile.startMovingToTarget(MAX_TILE_VELOCITY);
                }
            }
        }

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (!isRunning) {
                    isRunning = true;
                    if (movingTiles.isEmpty()) {
                        findMoveHelper();
                    }
                    if (movingTiles.isEmpty()) {
                        initTileFixWhenStart();
                        fixGridMove();
                    }
                    isRunning = false;
                }
            }

        }, 0, 300);

        beginGame();

        // CLEAR ANY WIN OR LOSS DISPLAY
        miniGame.getGUIDialogs().get(WIN_DIALOG_TYPE).setState(INVISIBLE_STATE);
        miniGame.getGUIDialogs().get(LOSS_DIALOG_TYPE).setState(INVISIBLE_STATE);
//        miniGame.getGUIDialogs().get(STATS_DIALOG_TYPE).setState(INVISIBLE_STATE);
    }
    boolean hasMove;

    public void initTileFixWhenStart() {
        if (this.inProgress()) {

            ZombieCrushMove move;
            hasMove = false;
            do {
                move = findInitialMove();
                if (move != null) {
                    processMove(move);
                    hasMove = true;
                }
            } while (move != null);

            //   fixGridMove();
        }
    }

    /**
     * Called each frame, this method updates all the game objects.
     *
     * @param game The Mahjong game to be updated.
     */
    @Override
    public void updateAll(MiniGame game) {

        try {

            game.beginUsingData();

            // WE ONLY NEED TO UPDATE AND MOVE THE MOVING TILES
            for (int i = 0; i < movingTiles.size(); i++) {
                // GET THE NEXT TILE
                ZombieCrushTile tile = movingTiles.get(i);

                // THIS WILL UPDATE IT'S POSITION USING ITS VELOCITY
                tile.update(game);

                // IF IT'S REACHED ITS DESTINATION, REMOVE IT
                // FROM THE LIST OF MOVING TILES
                if (!tile.isMovingToTarget()) {
                    movingTiles.remove(tile);
                }
                if (movingTiles.isEmpty()) {
                    this.theTileX = 0;
                    this.theTileY = 0;
                }

//
//            if (movingTiles.isEmpty()) {
//                findMoveHelper();
//            }
//            if (movingTiles.isEmpty()) {
//                this.initTileFixWhenStart();
//                 fixGridMove();
//            }
                // if (movingTiles.isEmpty()) {
                //}
//  
                // IF THE GAME IS STILL ON, THE TIMER SHOULD CONTINUE
                if (inProgress()) {
                    // KEEP THE GAME TIMER GOING IF THE GAME STILL IS
                    endTime = new GregorianCalendar();
                }
            }
        } finally {
            // MAKE SURE WE RELEASE THE LOCK WHETHER THERE IS
            // AN EXCEPTION THROWN OR NOT
            game.endUsingData();
        }
    }

    /**
     * This method is for updating any debug text to present to the screen. In a
     * graphical application like this it's sometimes useful to display data in
     * the GUI.
     *
     * @param game The Mahjong game about which to display info.
     */
    @Override
    public void updateDebugText(MiniGame game) {
    }

    private void moveTiletoFix(ArrayList<ZombieCrushTile> from, ArrayList<ZombieCrushTile> to, int k) {
        ZombieCrushTile tile = from.remove(0);
        // MAKE SURE BOTH ARE UNSELECTED
        tile.setState(VISIBLE_STATE);

        tile.setTarget(tile.getX(), tile.getY() + k * 55);
        tile.startMovingToTarget(MAX_TILE_VELOCITY);

        to.add(tile);

    }

    public void hardcode4() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(ZombieCrushPropertyType.IMG_PATH);
        ArrayList<String> typeCTiles = props.getPropertyOptionsList(ZombieCrushPropertyType.TYPE_C_TILES);
        String imgFile = imgPath + typeCTiles.get(0);
        String imgFile1 = imgPath + typeCTiles.get(1);
        SpriteType sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + "TILE_0");
        SpriteType sT1 = initTileSpriteType(imgFile1, TILE_SPRITE_TYPE_PREFIX + "TILE_1");

        for (int i = 0; i < this.gridColumns; i++) {
            for (int j = 0; j < this.gridRows; j++) {
                if (i % 2 == 0) {
                    ZombieCrushTile tile = new ZombieCrushTile(sT,
                            calculateTileXInGrid(i, 0), calculateTileYInGrid(j, 0),
                            0, 0, VISIBLE_STATE, TILE_C_TYPE);
                    tileGrid[i][j].add(tile);
                    tile.setGridCell(i, j);
                }
                if (i % 2 == 1) {
                    ZombieCrushTile tile1 = new ZombieCrushTile(sT1,
                            calculateTileXInGrid(i, 0), calculateTileYInGrid(j, 0),
                            0, 0, VISIBLE_STATE, TILE_C_TYPE);

                    tileGrid[i][j].add(tile1);
                    tile1.setGridCell(i, j);
                }
            }
        }
    }
}
