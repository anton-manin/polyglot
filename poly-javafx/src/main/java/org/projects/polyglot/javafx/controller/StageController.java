package org.projects.polyglot.javafx.controller;

import com.google.inject.Provider;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class StageController {

    @Inject
    private Provider<FXMLLoader> fxmlLoaderProvider;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showMainWindow() {
        try
        {
            FXMLLoader fxmlLoader = fxmlLoaderProvider.get();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/MainWindow.fxml"));
            final AnchorPane mainWindowPane = fxmlLoader.load();

            final Scene scene = new Scene(mainWindowPane);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Polyglot");

            primaryStage.show();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
