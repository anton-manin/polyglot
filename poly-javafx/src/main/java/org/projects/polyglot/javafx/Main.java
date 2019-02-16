package org.projects.polyglot.javafx;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;
import org.projects.polyglot.javafx.config.GuiceModule;
import org.projects.polyglot.javafx.controller.StageController;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Injector injector = Guice.createInjector(new GuiceModule());

        StageController stageController = injector.getInstance(StageController.class);

        stageController.setPrimaryStage(stage);
        stageController.showMainWindow();
    }
}
