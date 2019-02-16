package org.projects.polyglot.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PropertiesController {

    @FXML
    private TableView<?> propertyTableView;

    @FXML
    private TableColumn<?, ?> keyTableColumn;

    @FXML
    private TableColumn<?, ?> valueTableColumn;

    @FXML
    private TableColumn<?, ?> actionTableColumn;

}
