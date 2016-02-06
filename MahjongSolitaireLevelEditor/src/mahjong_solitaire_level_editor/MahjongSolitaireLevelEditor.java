package mahjong_solitaire_level_editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

/**
 * This program serves as a level editor for the Zomjong game. It is
 * capable of making and saving new .zom levels, as well as opening,
 * editing, and saving existing ones. Note that a .zom level does not 
 * arrange tiles, it just specifies where tiles may be arranged. Tile
 * arrangement should be done semi-randomly.
 * 
 * Note that we have designed this level editor such that the entire
 * program is defined inside this one class using inner classes for 
 * all event handlers and the renderer.
 * 
 * @author Richard McKenna
 */
public class MahjongSolitaireLevelEditor
{
    // INITIALIZATION CONSTANTS
    
    // THESE CONSTANTS ARE FOR CUSTOMIZATION OF THE GRID
    private final int INIT_GRID_DIM = 10;
    private final int MIN_GRID_DIM = 1;
    private final int MAX_GRID_ROWS = 10;
    private final int MAX_GRID_COLUMNS = 20;
    private final int GRID_STEP = 1;
    private final int TOTAL_TILES = 144;

    // TEXTUAL CONSTANTS
    private final String WINDOW_ICON_FILE_NAME = "../MahjongSolitaire/img/zomjong/ZombieBasic.png";
    private final String APP_TITLE = "Zomjong Level Editor";
    private final String OPEN_BUTTON_TEXT = "Open";
    private final String SAVE_AS_BUTTON_TEXT = "Save As";
    private final String COLUMNS_LABEL_TEXT = "Columns: ";
    private final String ROWS_LABEL_TEXT = "Rows: ";
    private final String TILES_REMAINING_LABEL_TEXT = "Tiles Remaining: ";
    private final String ZOMJONG_DATA_DIR = "../MahjongSolitaire/data/zomjong/";
    private final String ZOM_FILE_EXTENSION = ".zom";
    private final String OPEN_FILE_ERROR_FEEDBACK_TEXT = "File not loaded: .mazl files only";
    private final String SAVE_AS_ERROR_FEEDBACK_TEXT = "File not saved: must use .zom file extension";
    private final String FILE_LOADING_SUCCESS_TEXT = " loaded successfully";
    private final String FILE_READING_ERROR_TEXT = "Error reading from ";
    private final String FILE_WRITING_ERROR_TEXT = "Error writing to ";
    
    // CONSTANTS FOR FORMATTING THE GRID
    private final Font   GRID_FONT = new Font("monospaced", Font.BOLD, 36);
    private final Color  GRID_BACKGROUND_COLOR = new Color(200, 200, 120);

    // INSTANCE VARIABLES
    
    // HERE ARE THE UI COMPONENTS
    private JFrame      window;
    private JPanel      westPanel;
    private JButton     openButton;
    private JButton     saveAsButton;
    private JLabel      columnsLabel;
    private JSpinner    columnsSpinner;
    private JLabel      rowsLabel;
    private JSpinner    rowsSpinner;
    private JLabel      tilesRemainingLabel;

    // WE'LL RENDER THE GRID IN THIS COMPONENT
    private GridRenderer gridRenderer;

    // AND HERE IS THE GRID WE'RE MAKING
    private int gridColumns;
    private int gridRows;
    private int grid[][];
    
    // THIS KEEPS TRACK OF THE NUMBER OF TILES
    // WE STILL HAVE TO PLACE
    private int tilesRemaining;
    
    // THIS WILL LET THE USER SELECT THE FILES TO READ AND WRITE
    private JFileChooser fileChooser;
    
    // THIS WILL HELP US LIMIT THE USER FILE SELECTION CHOICES
    private FileFilter zomFileFilter;
    
    /**
     * This method initializes the level editor application, setting
     * up all data an UI components for use.
     */
    private void init()
    {
        // INIT THE EDITOR APP'S CONTAINER
        initWindow();
        
        // INITIALIZES THE GRID DATA
        initData();

        // LAYOUT THE INITIAL CONTROLS
        initGUIControls();
        
        // HOOK UP THE EVENT HANDLERS
        initHandlers();
        
        // INITIALIZE
        initFileControls();
    }
 
    /**
     * Initializes the window.
     */
    private void initWindow()
    {
        // MAKE THE WINDOW AND SET THE WINDOW TITLE
        window = new JFrame(APP_TITLE);

        // THEN LOAD THE IMAGE
        try
        {
            File imageFile = new File(WINDOW_ICON_FILE_NAME);
            Image windowImage = ImageIO.read(imageFile);
            MediaTracker mt = new MediaTracker(window);
            mt.addImage(windowImage, 0);
            mt.waitForAll();
            window.setIconImage(windowImage);
        }
        catch (Exception e)
        {
            // WE CAN LIVE WITHOUT THE ICON IMAGE IN CASE AN ERROR HAPPENS,
            // SO WE'LL JUST SQUELCH THIS EXCEPTION
        }
        
        // MAKE IT FULL SCREEN
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // JUST CLOSE WHEN SOMEONE HITS X
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Initializes the app data.
     */
    private void initData()
    {
        // START OUT OUR GRID WITH DEFAULT DIMENSIONS
        gridColumns = INIT_GRID_DIM;
        gridRows = INIT_GRID_DIM;
        
        // NOW MAKE THE INITIALLY EMPTY GRID
        grid = new int[gridColumns][gridRows];
        for (int i = 0; i < gridColumns; i++)
        {
            for (int j = 0; j < gridRows; j++)
            {
                grid[i][j] = 0;
            }
        }
        
        // WE HAVEN'T PLACED ANY TILE LOCATIONS YET
        tilesRemaining = TOTAL_TILES;
    }

    /**
     * Constructs and lays out all UI controls.
     */
    private void initGUIControls()
    {
        // ALL THE GRID DIMENSIONS CONTROLS GO IN THE WEST
        westPanel = new JPanel();
        westPanel.setLayout(new GridBagLayout());

        // WE HAVE 2 SPINNERS FOR UPDATING THE GRID DIMENSIONS, THESE
        // MODELS SPECIFY HOW THEY GET INITIALIZED AND THEIR VALUE BOUNDARIES
        SpinnerModel columnsSpinnerModel = new SpinnerNumberModel(  INIT_GRID_DIM, 
                                                                    MIN_GRID_DIM,
                                                                    MAX_GRID_COLUMNS,
                                                                    GRID_STEP);
        SpinnerModel rowsSpinnerModel = new SpinnerNumberModel( INIT_GRID_DIM,
                                                                MIN_GRID_DIM,
                                                                MAX_GRID_ROWS,
                                                                GRID_STEP);

        // CONSTRUCT ALL THE WEST TOOLBAR COMPONENTS
        openButton = new JButton(OPEN_BUTTON_TEXT);
        saveAsButton = new JButton(SAVE_AS_BUTTON_TEXT);
        saveAsButton.setEnabled(false);
        columnsLabel = new JLabel(COLUMNS_LABEL_TEXT);
        columnsSpinner = new JSpinner(columnsSpinnerModel);
        rowsLabel = new JLabel(ROWS_LABEL_TEXT);
        rowsSpinner = new JSpinner(rowsSpinnerModel);
        tilesRemainingLabel = new JLabel();
        
        // MAKE SURE THIS LABEL HAS THE CORRECT TEXT
        updateTilesRemainingLabel();

        // NOW PUT ALL THE CONTROLS IN THE WEST TOOLBAR
        addToWestPanel(openButton,          0, 0, 1, 1);
        addToWestPanel(saveAsButton,        1, 0, 1, 1);
        addToWestPanel(columnsLabel,        0, 1, 1, 1);
        addToWestPanel(columnsSpinner,      1, 1, 1, 1);
        addToWestPanel(rowsLabel,           0, 2, 1, 1);
        addToWestPanel(rowsSpinner,         1, 2, 1, 1);
        addToWestPanel(tilesRemainingLabel, 0, 3, 2, 1);

        // THIS GUY RENDERS OUR GRID
        gridRenderer = new GridRenderer();
        
        // PUT EVERYTHING IN THE FRAME
        window.add(westPanel, BorderLayout.WEST);
        window.add(gridRenderer, BorderLayout.CENTER);
    }

    /**
     * This method updates the display of the label in the west that
     * displays how many tiles are left to be located. This needs to
     * be called after every time the grid dimensions change or when
     * a new .zom file is loaded.
     */
    private void updateTilesRemainingLabel()
    {
        tilesRemainingLabel.setText(TILES_REMAINING_LABEL_TEXT + tilesRemaining);
    }
    
    /**
     * This helper method assists in using Java's GridBagLayout to arrange
     * components inside the west panel.
     */
    private void addToWestPanel(JComponent comp, int initGridX, int initGridY, int initGridWidth, int initGridHeight)
    {
        // GridBagLayout IS A JAVA LayoutManager THAT CAN BE USED TO
        // ARRANGE COMPONENTS IN A MULTI-DIMENSIONAL GRID WITH COMPONENTS
        // SPANNING MULTIPLE CELLS. FIRST WE INIT THE SETTINGS
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = initGridX;
        gbc.gridy = initGridY;
        gbc.gridwidth = initGridWidth;
        gbc.gridheight = initGridHeight;
        gbc.insets = new Insets(10, 10, 5, 5);
        
        // THEN USING THOSE SETTINGS WE PUT THE COMPONENT IN THE PANEL
        westPanel.add(comp, gbc);
    }

    /**
     * This method initializes all the event handlers needed by 
     * this application.
     */
    private void initHandlers()
    {
        // WE'LL RESPOND TO WHEN THE USER CHANGES THE
        // GRID DIMENSIONS USING THE SPINNERS
        GridSizeHandler gsh = new GridSizeHandler();
        columnsSpinner.addChangeListener(gsh);
        rowsSpinner.addChangeListener(gsh);

        // WE'LL UPDATE THE CELL-TILE COUNTS WHEN THE
        // USER CLICKS THE MOUSE ON THE RENDERING PANEL
        GridClickHandler gch = new GridClickHandler();
        gridRenderer.addMouseListener(gch);

        // WE'LL LET THE USER SELECT AND THEN WE'LL OPEN
        // A .ZOM FILE WHEN THE USER CLICKS ON THE OPEN BUTTON
        OpenLevelHandler olh = new OpenLevelHandler();
        openButton.addActionListener(olh);
        
        // AND WE'LL SAVE THE CURRENT LEVEL WHEN THE USER
        // PRESSES THE SAVE AS BUTTON
        SaveAsLevelHandler salh = new SaveAsLevelHandler();
        saveAsButton.addActionListener(salh);
    }

    /**
     * This method initializes the file chooser and the file filter
     * for that control so that the user may select files for saving
     * and loading.
     */
    public void initFileControls()
    {
        // INIT THE FILE CHOOSER CONTROL
        fileChooser = new JFileChooser(ZOMJONG_DATA_DIR);
        
        // AND THE FILE FILTER, WE'LL DEFINE A SIMPLE
        // ANONYMOUS TYPE FOR THIS
        zomFileFilter = new FileFilter() 
        {
            /**
             * This method limits the types the file chooser
             * can see to .zom files.
             */
            @Override
            public boolean accept(File file) 
            {
                return file.getName().endsWith(ZOM_FILE_EXTENSION);
            }

            /**
             * Describes the types of files we'll accept.
             */
            @Override
            public String getDescription() { return ZOM_FILE_EXTENSION; }
        };
        
        // AND MAKE SURE THE FILE CHOOSER USES THE FILTER
        fileChooser.setFileFilter(zomFileFilter);
    }

    /**
     * This event handler responds to when the user mouse clicks
     * on the rendering panel. The result is we update the tile
     * assignments on the cell that was clicked.
     */
    class GridClickHandler extends MouseAdapter
    {
        /**
         * This is the method where we respond to mouse clicks. Note
         * that the me argument knows the x,y coordinates of the
         * mouse click on the panel and we can translate that into
         * a click on a cell.
         */
        @Override
        public void mousePressed(MouseEvent me)
        {
            // FIGURE OUT THE CORRESPONDING COLUMN & ROW
            int w = gridRenderer.getWidth()/gridColumns;
            int col = me.getX()/w;
            int h = gridRenderer.getHeight()/gridRows;
            int row = me.getY()/h;
            
            // GET THE VALUE IN THAT CELL
            int value = grid[col][row];
            
            // IF IT'S A LEFT MOUSE CLICK WE INC
            if (me.getButton() == MouseEvent.BUTTON1)
            {
                // ONLY IF WE HAVE MORE TILES TO ASSIGN
                if (tilesRemaining > 0)
                {
                    value++;
                    tilesRemaining--;
                }
                // ONCE WE HAVE ASSIGNED ALL THE
                // TILES WE CAN SAVE A .ZOM FILE
                if (tilesRemaining == 0)
                {
                    saveAsButton.setEnabled(true);
                }
            }
            // IF IT'S A RIGHT MOUSE CLICK WE DEC
            else if (me.getButton() == MouseEvent.BUTTON3)
            {
                // BUT ONLY IF WE CAN
                if (value > 0)
                {
                    value--;
                    tilesRemaining++;
                    
                    // IF WE SUCCESSFULLY DEC'D WE DEFINITELY HAVEN'T 
                    // ASSIGNED ALL THE TILES
                    saveAsButton.setEnabled(false);
                }
            }
            // NOW ASSIGN THE NEW VALUE
            grid[col][row] = value;
            
            // UPDATE THE TILES REMAINING DISPLAY
            updateTilesRemainingLabel();
            
            // AND REDRAW THE GRID
            gridRenderer.repaint();
        }
    }
    
    /**
     * This class serves as the event handler for the two
     * spinners, which allow the user to change the
     * grid dimensions.
     */
    class GridSizeHandler implements ChangeListener
    {
        /**
         * Called when the user changes the value in one of the two spinners,
         * this method rebuilds the grid using the most recent
         * dimension settings, it also tries to copy data from the previous
         * sized grid into the new one. Note that as a grid is made larger
         * we keep all the data, but as a grid is made smaller, we will
         * lose some data.
         */        
        @Override
        public void stateChanged(ChangeEvent e)
        {
            // GET THE NEW GRID DIMENSIONS
            int newGridColumns = Integer.parseInt(columnsSpinner.getValue().toString());
            int newGridRows = Integer.parseInt(rowsSpinner.getValue().toString());
            
            //  MAKE A NEW GRID
            int[][] newGrid = new int[newGridColumns][newGridRows];
            int totalCellsCopied = 0;
            
            // COPY THE OLD DATA TO THE NEW GRID
            for (int i = 0; i < gridColumns; i++)
            {
                for (int j = 0; j < gridRows; j++)
                {
                    if ((i < newGridColumns) && (j < newGridRows))
                    {
                        newGrid[i][j] = grid[i][j];
                        totalCellsCopied += grid[i][j];
                    }
                }
            }
            
            // NOW UPDATE THE GRID
            gridColumns = newGridColumns;
            gridRows = newGridRows;
            grid = newGrid;
            
            // IF WE SHRUNK THE GRID, WE MAY HAVE LOST SOME DATA, SO
            // RETURN THOSE TILES
            int lostTiles = TOTAL_TILES - tilesRemaining - totalCellsCopied;
            tilesRemaining += lostTiles;
            
            // UPDATE THE REMAINING TILES DISPLAY
            updateTilesRemainingLabel();
            
            // AND RE-RENDER THE GRID
            gridRenderer.repaint();        
        }        
    }
    
    /**
     * This class serves as the event handler for the open
     * level button.
     */
    class OpenLevelHandler implements ActionListener
    {
        /**
         * This method responds to a click on the open level button. It
         * prompts the user for a file to open and then proceeds to
         * load it.
         */
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            // FIRST PROMPT THE USER FOR A FILE NAME
            int buttonSelection = fileChooser.showOpenDialog(openButton);
            
            // MAKE SURE THE USER WANTS TO CONTINUE AND DIDN'T
            // PRESS THE CANCEL OPTION
            if (buttonSelection == JFileChooser.APPROVE_OPTION)
            {   
                // GET THE FILE THE USER SELECTED
                File fileToOpen = fileChooser.getSelectedFile();
                String fileName = fileToOpen.getPath();
                
                // MAKE SURE IT'S A .ZOM FILE
                if (!fileName.endsWith(ZOM_FILE_EXTENSION))
                {
                    JOptionPane.showMessageDialog(saveAsButton, OPEN_FILE_ERROR_FEEDBACK_TEXT);
                    return;
                }

                // NOW LOAD THE RAW DATA SO WE CAN USE IT
                // OUR LEVEL FILES WILL HAVE THE DIMENSIONS FIRST,
                // FOLLOWED BY THE GRID VALUES
                try
                {
                    // LET'S USE A FAST LOADING TECHNIQUE. WE'LL LOAD ALL OF THE
                    // BYTES AT ONCE INTO A BYTE ARRAY, AND THEN PICK THAT APART.
                    // THIS IS FAST BECAUSE IT ONLY HAS TO DO FILE READING ONCE
                    byte[] bytes = new byte[Long.valueOf(fileToOpen.length()).intValue()];
                    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                    FileInputStream fis = new FileInputStream(fileToOpen);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    
                    // HERE IT IS, THE ONLY READY REQUEST WE NEED
                    bis.read(bytes);
                    bis.close();

                    // NOW WE NEED TO LOAD THE DATA FROM THE BYTE ARRAY
                    DataInputStream dis = new DataInputStream(bais);

                    // NOTE THAT WE NEED TO LOAD THE DATA IN THE SAME
                    // ORDER AND FORMAT AS WE SAVED IT

                    // FIRST READ THE GRID DIMENSIONS
                    int initGridColumns = dis.readInt();
                    int initGridRows = dis.readInt();
                    int[][] newGrid = new int[initGridColumns][initGridRows];
                    
                    // AND NOW ALL THE CELL VALUES
                    for (int i = 0; i < initGridColumns; i++)
                    {
                        for (int j = 0; j < initGridRows; j++)
                        {
                            newGrid[i][j] = dis.readInt();
                        }
                    }

                    // EVERYTHING WENT AS PLANNED SO LET'S MAKE IT PERMANENT
                    columnsSpinner.setValue(initGridColumns);
                    rowsSpinner.setValue(initGridRows);
                    grid = newGrid;
                    gridColumns = initGridColumns;
                    gridRows = initGridRows;                    
                    tilesRemaining = 0;
                    saveAsButton.setEnabled(true);

                    // UPDATE THE DISPLAY
                    updateTilesRemainingLabel();
                    gridRenderer.repaint();
                    
                    // FINALLY TELL THE USER THE LEVEL SUCCESSFULLY LOADED
                    JOptionPane.showMessageDialog(window, fileToOpen.getName() + FILE_LOADING_SUCCESS_TEXT);
                }
                catch(IOException ioe)
                {
                    // AN ERROR HAPPENED, LET THE USER KNOW.
                    JOptionPane.showMessageDialog(saveAsButton, FILE_READING_ERROR_TEXT + fileName, FILE_READING_ERROR_TEXT + fileName, JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * This class serves as the event handler for the 
     * Save As button.
     */
    class SaveAsLevelHandler implements ActionListener
    {
        /**
         * This method responds to when the user clicks the save
         * as button. It prompts the user for a file name and then
         * saves the level to that file.
         */
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            // FIRST PROMPT THE USER FOR A FILE NAME
            int buttonSelection = fileChooser.showSaveDialog(saveAsButton);
            
            // MAKE SURE THE USER WANTS TO CONTINUE AND DIDN'T SELECT
            // THE CANCEL BUTTON
            if (buttonSelection == JFileChooser.APPROVE_OPTION)
            {   
                // GET THE FILE THE USER SELECTED
                File fileToSave = fileChooser.getSelectedFile();
                String fileName = fileToSave.getPath();
                
                // MAKE SURE IT'S THE CORRECT FILE TYPE
                if (!fileName.endsWith(ZOM_FILE_EXTENSION))
                {
                    JOptionPane.showMessageDialog(saveAsButton, SAVE_AS_ERROR_FEEDBACK_TEXT);
                    return;
                }
                
                // OUR LEVEL FILES WILL HAVE THE DIMENSIONS FIRST,
                // FOLLOWED BY THE GRID VALUES
                try
                {
                    // WE'LL WRITE EVERYTHING IN BINARY. NOTE THAT WE
                    // NEED TO MAKE SURE WE SAVE THE DATA IN THE SAME
                    // FORMAT AND ORDER WITH WHICH WE READ IT LATER
                    FileOutputStream fos = new FileOutputStream(fileName);
                    DataOutputStream dos = new DataOutputStream(fos);
                    
                    // FIRST WRITE THE DIMENSIONS
                    dos.writeInt(gridColumns);
                    dos.writeInt(gridRows);
                    
                    // AND NOW ALL THE CELL VALUES
                    for (int i = 0; i < gridColumns; i++)
                    {
                        for (int j = 0; j < gridRows; j++)
                        {
                            dos.writeInt(grid[i][j]);
                        }
                    }
                }
                catch(IOException ioe)
                {
                    // AN ERROR HAS HAPPENED, LET THE USER KNOW
                    JOptionPane.showMessageDialog(saveAsButton, FILE_WRITING_ERROR_TEXT + fileName, FILE_WRITING_ERROR_TEXT + fileName, JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * This class renders the grid for us. Note that we also listen for
     * mouse clicks on it.
     */
    class GridRenderer extends JPanel
    {
        // PIXEL DIMENSIONS OF EACH CELL
        int cellWidth;
        int cellHeight;
        
        // WE'LL USE THIS TO MEASURE THE TEXT WE RENDER
        // SO THAT WE CAN PROPERLY CENTER IT
        FontMetrics fm;

        /**
         * Default constructor, it will initialize the background
         * color for the grid.
         */
        public GridRenderer()
        {
            setBackground(GRID_BACKGROUND_COLOR);
        }

        /**
         * This function is called each time the panel is rendered. It
         * will draw the grid, including all the cells and the numeric
         * values in each cell. 
         */
        @Override
        public void paintComponent(Graphics g)
        {
            // CLEAR THE PANEL
            super.paintComponent(g);
            
            // MAKE SURE WE'RE USING THE CORRECT FONT
            g.setFont(GRID_FONT);
            
            // MEASURE THE FONT 
            fm = g.getFontMetrics(GRID_FONT);
            
            // THIS IS THE HEIGHT OF OUR CHARACTERS
            int charHeight = fm.getHeight();

            // CALCULATE THE GRID CELL DIMENSIONS
            int w = this.getWidth()/MahjongSolitaireLevelEditor.this.gridColumns;
            int h = this.getHeight()/MahjongSolitaireLevelEditor.this.gridRows;

            // NOW RENDER EACH CELL
            int x = 0, y = 0;
            for (int i = 0; i < gridColumns; i++)
            {
                y = 0;
                for (int j = 0; j < gridRows; j++)
                {
                    // DRAW THE CELL
                    g.drawRoundRect(x, y, w, h, 10, 10);
                    
                    // THEN RENDER THE TEXT
                    String numToDraw = "" + MahjongSolitaireLevelEditor.this.grid[i][j];
                    int charWidth = fm.stringWidth(numToDraw);
                    int xInc = (w/2) - (charWidth/2);
                    int yInc = (h/2) + (charHeight/4);
                    x += xInc;
                    y += yInc;
                    g.drawString(numToDraw, x, y);
                    x -= xInc;
                    y -= yInc;
                    
                    // ON TO THE NEXT ROW
                    y += h;
                }
                // ON TO THE NEXT COLUMN
                x += w;
            }
        }
    }
    
    /**
     * This is where execution of the level editor begins.
     */
    public static void main(String[] args)
    {
        // MAKE THE EDITOR
        MahjongSolitaireLevelEditor app = new MahjongSolitaireLevelEditor();
        
        // INITIALIZE THE APP
        app.init();
        
        // AND OPEN THE WINDOW
        app.window.setVisible(true);
    }
}
