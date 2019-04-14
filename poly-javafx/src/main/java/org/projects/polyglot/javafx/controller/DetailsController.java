package org.projects.polyglot.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

}
