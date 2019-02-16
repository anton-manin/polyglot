package org.projects.polyglot.javafx.context;

import javax.inject.Singleton;

@Singleton
public class LanguageContext {

    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
