package FinalProject;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

//80 tiles 10 minesa
//252 tiles 40 mines
//480 tiles 99 mines

//static final String images = "FinalProject/Numbers/%d.png"

public class Tile {
    static final private String flag = "FinalProject/Numbers/flag.png";
    static final private String mine = "FinalProject/Numbers/mine.png";
    private Board board;
    private boolean isMine; //used to see if tile is mine
    private int value;
    private ImageView number; //
    private Button button;
    private int x;
    private int y;

    private boolean isFlagged;
    private boolean isUncovered;

    public Tile(Board board, int x, int y)
    {
        this.board = board;
        isMine = false;
        value = 0;
        button = new Button();
        button.setPrefSize(20, 20);

        number = new ImageView();
        number.setImage(new Image("FinalProject/Numbers/1.png", 20, 20, true, true));
        
        number.setVisible(false);
        number.setManaged(false);

        this.x = x;
        this.y = y;
    }

    public boolean getIsMine(){
        return isMine;
    }

    public void setIsMine(boolean mine) {
        isMine = mine;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int adj) {
        value = adj;
    }

    public Button getButton(){
        return button;
    }

    public ImageView getNumber(){
        return number;
    }

    //Uncover() shows each tile by hiding the button and making the number image appear. If it's a mine, it freezes the game until the user hits try again.
    public void uncover(){
        if(isMine){
            button.setGraphic(new ImageView(new Image(mine, 10, 20, true, true)));
            board.freeze();
        }
        else if(isFlagged || isUncovered){
            return;
        }
        else{
            board.decrementTileCount();
            button.setVisible(false);
            number.setImage(new Image(String.format("FinalProject/Numbers/%d.png", value), 20, 20, true, true));
            number.setVisible(true);
            isUncovered = true;

            if(value == 0){
                value = -1;
                for(int x = -1; x < 2; x++)
                    for(int y = -1; y < 2; y++){
                        if(!((x == 0 && y == 0) || this.x + x < 0 || this.x + x >= board.getWidth() || this.y + y < 0 || this.y + y >= board.getHeight())){
                            board.tiles[this.x + x][this.y + y].uncover();
                        }
                            
            
                    }
            }
        }
    }

    //marks a tile as flagged and either increments or decrements the number of mines left
    public void flag(){
        if(isFlagged){
            button.setGraphic(null);
            isFlagged = false;
            board.incrementMines();
        }
        else{
            ImageView flagImage = new ImageView(new Image(flag, 10, 20, true, true));
            button.setGraphic(flagImage);
            isFlagged = true;
            board.decrementMines();
        }
    }

}
