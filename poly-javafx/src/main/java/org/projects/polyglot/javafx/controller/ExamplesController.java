package org.projects.polyglot.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Component;

@Component
public class ExamplesController {

    @FXML
    private TableView<?> exampleTableView;

    @FXML
    private TableColumn<?, ?> exampleTableColumn;

    @FXML
    private TableColumn<?, ?> actionTableColumn;
}
