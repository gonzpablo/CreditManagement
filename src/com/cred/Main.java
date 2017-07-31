package com.cred;

import java.io.IOException;

import com.cred.util.Log;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestión de Créditos");

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Main.fxml"));
            VBox page = (VBox) loader.load();
            
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image("file:icons/calculator.png"));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}