package org.projects.polyglot.javafx.config;

import com.google.inject.AbstractModule;
import org.projects.polyglot.javafx.controller.StageController;

public class GuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(StageController.class);
    }
}
