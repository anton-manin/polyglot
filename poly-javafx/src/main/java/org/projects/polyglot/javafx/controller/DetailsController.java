package org.projects.polyglot.javafx.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import org.projects.polyglot.core.domain.Languages;
import org.projects.polyglot.core.domain.Word;
import org.projects.polyglot.core.service.LanguageService;
import org.projects.polyglot.core.service.WordTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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
    private TableColumn<?, ?> actionTableColumn;

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

        languageTableColumn.setEditable(true);
        translationTableColumn.setEditable(true);

        translationTableView.setItems(translations);
    }

    private void addDummyTestDataToWordsList() {
        Word word = new Word();
        word.setWord("do");
        word.setLanguage(Languages.ENGLISH);

        Word translation = new Word();
        translation.setWord("machen");
        translation.setLanguage(Languages.GERMAN);

        word.setTranslations(Arrays.asList(translation));

        wordTextField.setText(word.getWord());
        languageChoiceBox.setValue(word.getLanguage().toString());
        translations.add(translation);
    }

}
