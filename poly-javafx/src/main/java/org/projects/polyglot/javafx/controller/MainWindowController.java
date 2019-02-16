package org.projects.polyglot.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class MainWindowController {

    @FXML
    private ListView<?> searchListView;

    @FXML
    private TextField searchTextField;

    @FXML
    private ImageView searchImageView;

    @FXML
    private ChoiceBox<?> languageChoiceBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TabPane tabsPane;

    @FXML
    private Tab detailsTab;

    @FXML
    private Tab examplesTab;

    @FXML
    private Tab propertiesTab;

}

