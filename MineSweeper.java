// Mine Sweeper
// Tanner McLeod and Eric Guan
// May 23, 2023
// The classic Minesweeper game recreated. Each tile when uncovered displays the 
//number of mines adjacent to the tile itself. When clicked on a bomb, the game is over. The user can also 
//mark bombs using flags in order to keep track of which tiles are bombs. There is a timer and flag count also implemented in our code.
//There are three difficulties in our game: Easy, Medium, and Hard. The Easy mode has 80 tiles and 10 bombs. 
//The Medium mode has 252 tiles and 40 mines. The Hard mode has 480 tiles and 99 mines.
// Honor Code Pledge: We pledge to abide by the CS Academic Honesty Agreement

package FinalProject;
import javafx.application.Application;
import javafx.stage.Stage;

public class MineSweeper extends Application{
    
    public void start(Stage primaryStage){
        Game game = new Game(primaryStage); //starts new game

        primaryStage.setTitle("Minesweeper"); //sets title to minesweeper
        primaryStage.setScene(game.scene); //sets scene to game.scene
        primaryStage.setFullScreen(true); //sets full screen
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
