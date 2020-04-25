package org.projects.polyglot.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.projects.polyglot.core.domain.Word;
import org.projects.polyglot.core.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainWindowController {

    @FXML
    private ListView<Word> searchListView;

    @FXML
    private TextField searchTextField;

    @FXML
    private ImageView searchImageView;

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

    @Autowired
    private WordService wordService;

    @FXML
    DetailsController wordsDetailsController;

    @FXML
    ExamplesController wordsExamplesController;

    @FXML
    PropertiesController wordsPropertiesController;

    private final ObservableList<Word> words = FXCollections.observableArrayList();


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

        wordsDetailsController.setExamplesController(this.wordsExamplesController);
        wordsDetailsController.setExamplesTab(this.examplesTab);

        wordsDetailsController.setPropertiesController(this.wordsPropertiesController);
        wordsDetailsController.setPropertiesTab(this.propertiesTab);

        searchListView.setItems(words);
        searchListView.setCellFactory((ListView<Word> userListView) -> new ListCell<Word>()
        {
            @Override
            public void updateItem(Word word, boolean empty)
            {
                super.updateItem(word, empty);
                if (empty)
                {
                    setText(null);
                }
                else
                {
                    setText(word.getWord());
                }
            }
        });

        searchListView.onMouseClickedProperty().set(event -> {
            if (searchListView.getSelectionModel().getSelectedItem() != null) {
                this.wordsDetailsController.loadWord(
                        searchListView.getSelectionModel().getSelectedItem()
                );

                words.clear();
            }
        });
    }

    @FXML
    public void addButtonOnAction() {
        this.wordsDetailsController.loadWord(new Word());
    }

    @FXML
    public void searchWord(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            String wordToSearch = searchTextField.getText();

            words.clear();
            searchTextField.clear();

            wordService.find(wordToSearch).ifPresent(word -> {
                words.add(word);
            });
        }
    }
}

