package org.projects.polyglot.javafx.config;

import com.google.inject.AbstractModule;
import org.projects.polyglot.javafx.context.LanguageContext;
import org.projects.polyglot.javafx.controller.StageController;

public class JavaFXGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(StageController.class);
        bind(LanguageContext.class);
    }
}
