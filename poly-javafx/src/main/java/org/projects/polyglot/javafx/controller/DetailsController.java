package org.projects.polyglot.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.projects.polyglot.core.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DetailsController {

    @FXML
    private TextField wordTextField;

    @FXML
    private TableView<?> translationTableView;

    @FXML
    private TableColumn<?, ?> languageTableColumn;

    @FXML
    private TableColumn<?, ?> translationTableColumn;

    @FXML
    private TableColumn<?, ?> actionTableColumn;

    @FXML
    private ChoiceBox<String> languageChoiceBox;

    @Autowired
    LanguageService languageService;

    private final ObservableList<String> languages = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Load languages in languageChoiceBox
        languages.setAll(languageService.getAllLanguages());
        languageChoiceBox.setItems(languages);
    }

}
