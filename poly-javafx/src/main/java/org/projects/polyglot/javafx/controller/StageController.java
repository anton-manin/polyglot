package org.projects.polyglot.javafx.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class StageController {

    private static FXMLLoader fxmlLoader;

    private static Stage primaryStage;

    public static void initFxmlLoader(ConfigurableApplicationContext springContext, Stage primaryStage) {
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(springContext::getBean);

        StageController.primaryStage = primaryStage;
    }

    public static void showMainWindow() throws IOException {
        fxmlLoader.setLocation(StageController.class.getClassLoader().getResource("fxml/MainWindow.fxml"));
        final AnchorPane mainWindowPane = fxmlLoader.load();

        final Scene scene = new Scene(mainWindowPane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Polyglot");

        primaryStage.show();
    }
}
