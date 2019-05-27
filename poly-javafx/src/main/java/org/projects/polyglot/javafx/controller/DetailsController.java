package org.projects.polyglot.javafx.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import org.projects.polyglot.core.domain.Languages;
import org.projects.polyglot.core.domain.Word;
import org.projects.polyglot.core.domain.WordType;
import org.projects.polyglot.core.errorhandling.UniqueWordException;
import org.projects.polyglot.core.repository.WordRepository;
import org.projects.polyglot.core.service.LanguageService;
import org.projects.polyglot.core.service.WordService;
import org.projects.polyglot.core.service.WordTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DetailsController {

    @FXML
    private TextField wordTextField;

    @FXML
    private TableView<Word> translationTableView;

    @FXML
    private TableColumn<Word, String> languageTableColumn;

    @FXML
    private TableColumn<Word, String> translationTableColumn;

    @FXML
    private TableColumn<String, String> actionTableColumn;

    @FXML
    private ChoiceBox<String> languageChoiceBox;

    @FXML
    private ChoiceBox<String> wordTypeChoiceBox;

    @Autowired
    LanguageService languageService;

    private final ObservableList<String> languages = FXCollections.observableArrayList();

    @Autowired
    WordTypeService wordTypeService;

    private final ObservableList<String> wordTypes = FXCollections.observableArrayList();

    private final ObservableList<Word> translations = FXCollections.observableArrayList();

    @Autowired
    WordService wordService;

    Word currentWord;

    SimpleStringProperty wordTextProperty;
    SimpleStringProperty wordTypeProperty;
    SimpleStringProperty languageProperty;

    // For test
    @Autowired
    WordRepository wordRepository;

    @FXML
    public void initialize() {
        addDummyTestDataToWordsList();

        // Load languages in languageChoiceBox
        languages.setAll(languageService.getAllLanguages());
        languageChoiceBox.setItems(languages);

        wordTypes.setAll(wordTypeService.getAllWordTypes());
        wordTypeChoiceBox.setItems(wordTypes);

        initializeWordsTableView();
    }

    private void initializeWordsTableView() {
        translationTableView.setEditable(true);
        languageTableColumn.setCellValueFactory(new PropertyValueFactory<Word, String>("language"));
        languageTableColumn.setCellFactory(ComboBoxTableCell.forTableColumn(languages));

        languageTableColumn.setOnEditCommit((TableColumn.CellEditEvent<Word, String> t) ->
        {
            Word selectedTranslation = ((Word) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            selectedTranslation.setLanguage(Languages.valueOf(t.getNewValue()));

            t.getTableView().refresh();
        });

        translationTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Word, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Word, String> param)
            {
                return new ReadOnlyStringWrapper(param.getValue().getWord());
            }
        });
        translationTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        translationTableColumn.setOnEditCommit((TableColumn.CellEditEvent<Word, String> t) ->
        {
            Word selectedTranslation = ((Word) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            selectedTranslation.setWord(t.getNewValue());

            t.getTableView().refresh();
        });

        actionTableColumn.setCellFactory(param -> new TableCell<String,String>() {
            final Button btn = new Button("Delete");

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    btn.setOnAction(event -> {
                        System.out.println("========== Button clicked");
                    });
                    setGraphic(btn);
                    setText(null);
                }
            }
        });

        languageTableColumn.setEditable(true);
        translationTableColumn.setEditable(true);

        translationTableView.setItems(translations);
    }

    @FXML
    public void saveButtonOnAction() {
        try {
            wordService.save(currentWord);
        } catch (UniqueWordException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The word you trying to save already exists!", ButtonType.OK);
            alert.show();
        } catch (Exception e) {
            // here should be logging
            Alert alert = new Alert(Alert.AlertType.ERROR, "Word could not be saved, check your log files", ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    public void deleteButtonOnAction() {

    }

    private void addDummyTestDataToWordsList() {
        wordRepository.deleteAll();

        currentWord = new Word();
        currentWord.setWord("do");
        currentWord.setLanguage(Languages.ENGLISH);
        currentWord.setWordType(WordType.VERB);

        Word translation = new Word();
        translation.setWord("machen");
        translation.setLanguage(Languages.GERMAN);

        //currentWord.setTranslations(Arrays.asList(translation));

        wordTextProperty = new SimpleStringProperty(currentWord.getWord());
        wordTextProperty.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) ->
                currentWord.setWord(newValue));
        wordTextField.textProperty().bindBidirectional(wordTextProperty);

        wordTypeProperty = new SimpleStringProperty(currentWord.getWordType().toString());
        wordTypeProperty.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) ->
                currentWord.setWordType(WordType.valueOf(newValue)));
        wordTypeChoiceBox.valueProperty().bindBidirectional(wordTypeProperty);

        languageProperty = new SimpleStringProperty(currentWord.getLanguage().toString());
        languageProperty.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) ->
            currentWord.setLanguage(Languages.valueOf(newValue)));
        languageChoiceBox.valueProperty().bindBidirectional(languageProperty);

        translations.add(translation);
    }

}