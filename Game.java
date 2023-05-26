package FinalProject;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.Timeline;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Game{
    Scene scene;
    Board board;
    
    Timeline timeline;
    int seconds = 0;
    String formattedTime;

    Label timer_label = new Label("00:00:00");
    Label mine_label = new Label("0");


    public Game(Stage primaryStage){
        VBox layout = new VBox(20);
        Button try_again = new Button();

        // create combo box where user can choose between easy, medium, and hard mode.
        ComboBox<String> comboBox1 = new ComboBox<String>();
        comboBox1.getItems().addAll("Easy", "Medium", "Hard");
        comboBox1.setOnAction(e -> {
            stopTimer();
            int total_width = 18;
            int total_height = 14;
            int total_mines = 40;
            String response = comboBox1.getValue();
            if (response.equals("Easy")) {
                total_width = 10;
                total_height = 8;
                total_mines = 10;
            }
            else if (response.equals("Hard")) {
                total_width = 24;
                total_height = 20;
                total_mines = 99;
            }
            board = new Board(this, total_width, total_height, total_mines);
            layout.getChildren().remove(2);
            layout.getChildren().add(2, board.getGrid());
            timer_label.setText("00:00:00");
            setTimer();
        });
        
        //try again button which restarts the game and the timer
        try_again.setText("Try Again");
        try_again.setOnAction(e -> {
            stopTimer();
            board.tileButtons = new GridPane();
            String new_response = comboBox1.getValue();
            int total_width = 18;
            int total_height = 14;
            int total_mines = 40;
            if (new_response.equals("Easy")) {
                total_width = 10;
                total_height = 8;
                total_mines = 10;
            }
            else if (new_response.equals("Hard")) {
                total_width = 24;
                total_height = 20;
                total_mines = 99;
            }
            board = new Board(this, total_width, total_height, total_mines);
            layout.getChildren().remove(2);
            layout.getChildren().add(2, board.getGrid());
            timer_label.setText("00:00:00");
            setTimer();
        });

        //flag and timer images
        Image flag = new Image("./FinalProject/Numbers/flag.png");
        Image timer = new Image("./FinalProject/Numbers/timer.png");

        // Create ImageViews
        ImageView imageView1 = new ImageView(flag);
        ImageView imageView2 = new ImageView(timer);

        imageView1.setFitWidth(50);
        imageView1.setFitHeight(50);
        imageView2.setFitWidth(50);
        imageView2.setFitHeight(50);
        
        setTimer(); // Repeat indefinitely

        //set font for timer and mine
        timer_label.setFont(new Font("Cambria", 24));
        mine_label.setFont(new Font("Cambria", 24));

        //flag count and timer count formatting
        HBox flag_timer_count = new HBox(imageView1, mine_label, imageView2, timer_label);
        flag_timer_count.setAlignment(Pos.CENTER);
        flag_timer_count.setSpacing(20);

        //formatting
        layout.getStyleClass().add("vbox");
        layout.getChildren().addAll(comboBox1, flag_timer_count, new GridPane(), try_again);
        layout.setAlignment(Pos.CENTER);

        //Scene scene = new Scene(layout, primaryStage.getWidth(), primaryStage.getHeight());
        scene = new Scene(layout, primaryStage.getWidth(), primaryStage.getHeight());
    }

    //starts timer
    public void startTimer(){
        timeline.play();
    }

    //stops timer
    public void stopTimer(){
        timeline.stop();
    }

    //changes number of mines left using flags
    public void updateMines(int mineCount){
        mine_label.setText(String.valueOf(mineCount));
    }
    
    //resets timer to 0 
    public void setTimer(){
        seconds = 0;
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            seconds++;
            int hours = seconds / 3600;
            int minutes = (seconds % 3600) / 60;
            int secs = seconds % 60;
            formattedTime = String.format("%02d:%02d:%02d", hours, minutes, secs);
            timer_label.setText(formattedTime);
            
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

}