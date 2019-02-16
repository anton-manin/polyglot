package org.projects.polyglot.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
    private SplitPane splitPane;

    @FXML
    private TabPane tabsPane;

    @FXML
    private AnchorPane leftPane;

    @FXML
    private AnchorPane rightPane;

    @FXML
    private Tab detailsTab;

    @FXML
    private Tab examplesTab;

    @FXML
    private Tab propertiesTab;

    @FXML
    public void initialize() {
        // prevent moving of split pane sliders
        leftPane.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.2));
        rightPane.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.8));
        leftPane.minWidthProperty().bind(splitPane.widthProperty().multiply(0.2));
        rightPane.minWidthProperty().bind(splitPane.widthProperty().multiply(0.8));


        AnchorPane detailsContent = (AnchorPane) detailsTab.getContent();
        detailsContent.prefWidthProperty().bind(rightPane.widthProperty());

        AnchorPane examplesContent = (AnchorPane) examplesTab.getContent();
        examplesContent.prefWidthProperty().bind(rightPane.widthProperty());

        AnchorPane propertiesContent = (AnchorPane) propertiesTab.getContent();
        propertiesContent.prefWidthProperty().bind(rightPane.widthProperty());

    }
}

