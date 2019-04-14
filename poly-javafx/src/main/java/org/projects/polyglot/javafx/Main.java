package org.projects.polyglot.javafx;

import javafx.application.Application;
import javafx.stage.Stage;
import org.projects.polyglot.javafx.controller.StageController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(Main.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        StageController.initFxmlLoader(springContext, stage);
        StageController.showMainWindow();
    }

    public static void main(String[] args) {
        launch();
    }
}
