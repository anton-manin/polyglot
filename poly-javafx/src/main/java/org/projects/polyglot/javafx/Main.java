package org.projects.polyglot.javafx;

import javafx.application.Application;
import javafx.stage.Stage;
import org.projects.polyglot.javafx.controller.StageController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"org.projects.polyglot"})
@EnableJpaRepositories("org.projects.polyglot.core.repository")
@EntityScan("org.projects.polyglot.core.domain")
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
