package com.nju.zxr;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    final Controller controller  = new Controller();

    public void start(Stage primaryStage){
        primaryStage.setScene(new Scene(controller.initializeGUI()));
        controller.initializeGame();
        //primaryStage.setResizable(false);
        primaryStage.setTitle("CalabashBros VS Monsters");
        primaryStage.show();
        controller.start(primaryStage);
    }

    public static void main(String[] args){
        launch(args);
    }
}
