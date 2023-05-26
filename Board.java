package FinalProject;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import java.util.Random;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class Board {
    private Game game;
    private int width; //width and height of board depending on mode
    private int height;
    private int numMines; //Actual number of mines on the board
    private int mineCount; //How many mines are left that the player hasn't counted/marked
    private int tileCount; //How many tiles are left that the player hasn't counted

    private int seconds = 0;

    Tile[][] tiles;
    GridPane tileButtons;

    //constructor
    public Board(Game game, int width, int height, int numMines){
        this.game = game;
        this.width = width;
        this.height = height;
        this.numMines = numMines;
        mineCount = numMines;
        this.tileButtons = new GridPane();
        this.tiles = new Tile[width][height];
        tileCount = (width * height) - mineCount;

        tileButtons.setAlignment(Pos.CENTER);
        
        game.updateMines(mineCount); 

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++){
                final int a = x;
                final int b = y;
                Tile tile = new Tile(this, x, y);
                HBox hbox = new HBox();
                hbox.getChildren().addAll(tile.getButton(), tile.getNumber());
                tile.getButton().setPrefSize(30, 30); //Set size of buttons
                tile.getButton().setOnAction(e -> { //Sets each function to start the game when clicked on
                    generate(a, b);
                    tile.uncover();
                    setButtons();
                });

                tileButtons.add(hbox, x, y); //Add the hbox of tile button and tile image
                tiles[x][y] = tile; 
            }
        }

    }

    //checks if coordinate is within the boundaries
    public boolean isValid(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) 
            return false;
        return true;
    }

    //generates entire board including tiles and mines
    public void generate(int first_width, int first_height){
        for (int x = 0; x < numMines; x++){
            Random rand = new Random();
            int rand_width = rand.nextInt(width);
            int rand_height = rand.nextInt(height);
            boolean not_new = true;
            while (not_new) {
                not_new = false;
                //System.out.println(rand_width);
                if (tiles[rand_width][rand_height].getIsMine() ||
                    (rand_width == first_width - 1 && rand_height == first_height -1) || //lower left corner
                    (rand_width == first_width - 1 && rand_height == first_height) || //left middle
                    (rand_width == first_width - 1 && rand_height == first_height + 1) || // left top corner
                    (rand_width == first_width && rand_height == first_height -1) || //bottom middle
                    (rand_width == first_width && rand_height == first_height) || //middle middle
                    (rand_width == first_width && rand_height == first_height +1) || //top middle
                    (rand_width == first_width + 1 && rand_height == first_height -1) || //right lower corner
                    (rand_width == first_width + 1 && rand_height == first_height) || //right middle
                    (rand_width == first_width + 1 && rand_height == first_height +1) //right top corner
                    ) {
                    not_new = true;
                    rand_width = rand.nextInt(width);
                    rand_height = rand.nextInt(height);
                }

            }
            
            tiles[rand_width][rand_height].setIsMine(true);
            //already_width.add(rand_width);
            //already_height.add(rand_height);
        }

        
        //put tiles with no mines inside and calculate adjacency
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (tiles[x][y].getIsMine() == false) {
                    //check lower left corner
                    if (isValid(x-1, y-1) && tiles[x-1][y-1].getIsMine() == true)
                        tiles[x][y].setValue(tiles[x][y].getValue() + 1);
                    //check left middle
                    if (isValid(x-1, y) && tiles[x-1][y].getIsMine() == true)
                        tiles[x][y].setValue(tiles[x][y].getValue() + 1);
                    //check left top corner
                    if (isValid(x-1, y + 1) && tiles[x-1][y + 1].getIsMine() == true)
                        tiles[x][y].setValue(tiles[x][y].getValue() + 1);
                    //check middle bottom
                    if (isValid(x, y-1) && tiles[x][y-1].getIsMine() == true)
                        tiles[x][y].setValue(tiles[x][y].getValue() + 1);
                    //check middle top
                    if (isValid(x, y+1) && tiles[x][y+1].getIsMine() == true)
                        tiles[x][y].setValue(tiles[x][y].getValue() + 1);
                    //check right bottom corner
                    if (isValid(x + 1, y - 1) && tiles[x + 1][y - 1].getIsMine() == true)
                        tiles[x][y].setValue(tiles[x][y].getValue() + 1);
                    //check right middle
                    if (isValid(x + 1, y) && tiles[x + 1][y].getIsMine() == true)
                        tiles[x][y].setValue(tiles[x][y].getValue() + 1);
                    //check right top corner
                    if (isValid(x + 1, y + 1) && tiles[x + 1][y + 1].getIsMine() == true)
                        tiles[x][y].setValue(tiles[x][y].getValue() + 1);
                }
            }
        }
        
        game.startTimer();
        
    }

    //sets tiles to become buttons
    public void setButtons(){
        int i = 0;
        for(Node node: tileButtons.getChildren()){
            if (node instanceof HBox){
                HBox hbox = (HBox) node;
                Button button = (Button) hbox.getChildren().get(0);
                final Tile tile = tiles[i%width][i/width];
                button.setOnAction(null);
                tile.getButton().setOnMouseClicked((MouseEvent event) -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        tile.uncover();
                    }else if (event.getButton() == MouseButton.SECONDARY) {
                        tile.flag();
                    }
                });
                ++i;
            }
        }
    }

    //freezes board and doesn't allow user to click another button because they either won or hit a mine
    public void freeze(){
        int i = 0;
        for(Node node: tileButtons.getChildren()){
            if (node instanceof HBox){
                HBox hbox = (HBox) node;
                Button button = (Button) hbox.getChildren().get(0);
                final Tile tile = tiles[i%width][i/width];
                button.setOnAction(null);
                tile.getButton().setOnMouseClicked(null);
                ++i;
            }
        }
        game.stopTimer();
    }

    //decreases tile count
    public void decrementTileCount(){
        tileCount--;
        if(tileCount == 0){
            freeze();

        }
    }

    //decreases mines
    public void decrementMines(){
        mineCount--;
        game.updateMines(mineCount);
    }

    //increases mines
    public void incrementMines(){
        mineCount++;
        game.updateMines(mineCount);
    }

    public GridPane getGrid(){
        return tileButtons;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }
}
