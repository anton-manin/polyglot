package org.projects.polyglot.javafx.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
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
import org.projects.polyglot.core.service.PriorityService;
import org.projects.polyglot.core.service.WordService;
import org.projects.polyglot.core.service.WordTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.projects.polyglot.core.util.Util.emptyIfNull;

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

    @FXML
    private ChoiceBox<Integer> priorityChoiceBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button addButton;

    @Autowired
    LanguageService languageService;

    @Autowired
    WordTypeService wordTypeService;

    @Autowired
    PriorityService priorityService;

    private final ObservableList<String> languages = FXCollections.observableArrayList();

    private final ObservableList<String> wordTypes = FXCollections.observableArrayList();

    private final ObservableList<Word> translations = FXCollections.observableArrayList();

    private final ObservableList<Integer> priorities = FXCollections.observableArrayList();

    @Autowired
    WordService wordService;

    Word currentWord;

    @Autowired
    WordRepository wordRepository;
    private ExamplesController wordsExamplesController;
    private PropertiesController wordsPropertiesController;
    private Tab examplesTab;
    private Tab propertiesTab;

    public void init() {
        translationTableView.setDisable(true);

        deleteButton.setVisible(false);
        saveButton.setVisible(false);
        cancelButton.setVisible(false);
        addButton.setVisible(true);

        // Load languages in languageChoiceBox
        languages.setAll(languageService.getAllLanguages());
        languageChoiceBox.setItems(languages);

        wordTypes.setAll(wordTypeService.getAllWordTypes());
        wordTypeChoiceBox.setItems(wordTypes);

        priorities.setAll(priorityService.getListOfPriorities());
        priorityChoiceBox.setItems(priorities);

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

            if (selectedTranslation.getId() != null) {
                wordService.update(selectedTranslation);
            }

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

            if (selectedTranslation.getId() != null) {
                wordService.update(selectedTranslation);
            }

            t.getTableView().refresh();
        });

        actionTableColumn.setCellFactory(param -> new TableCell<String,String>() {
            final Button deleteTranslationButton = new Button("Delete");
            final Button saveTranslationButton = new Button("Save");

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    saveTranslationButton.setOnAction(event -> {
                        System.out.println("========== Save Translation Button clicked");
                        Word translation = translations.get(this.getIndex());

                        if (translation != null &&
                        translation.getLanguage() != null && !translation.getWord().equals("Enter word")) {
                            translation.setWordType(currentWord.getWordType());
                            wordService.addTranslation(currentWord, translation);

                            translations.add(getEmptyWord());
                        }
                    });

                    deleteTranslationButton.setOnAction(event -> {
                        System.out.println("========== Delete Translation Button clicked");
                        Word translation = translations.get(this.getIndex());
                        wordService.deleteTranslation(currentWord, translation);
                        translations.remove(translation);
                    });

                    System.out.println(this.getIndex() + " == " + translations.size());
                    if (this.getIndex() == translations.size() - 1) {
                        System.out.println("Equal");
                        setGraphic(saveTranslationButton);
                    } else {
                        setGraphic(deleteTranslationButton);
                    }

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
            if (wordTextField.getText() != null &&
                    wordTypeChoiceBox.getValue() != null &&
                    languageChoiceBox.getValue() != null &&
                    priorityChoiceBox.getValue() != null) {
                currentWord.setWord(wordTextField.getText());
                currentWord.setWordType(WordType.valueOf(wordTypeChoiceBox.getValue()));
                currentWord.setLanguage(Languages.valueOf(languageChoiceBox.getValue()));
                currentWord.setPriority(priorityChoiceBox.getValue());
            } else throw new RuntimeException("Provide all the needed information to word");

            if (this.currentWord.getId() == null) {
                wordService.save(currentWord);
            } else {
                this.currentWord = wordService.update(currentWord);
            }

            this.wordsExamplesController.setCurrentWord(this.currentWord);
            this.wordsPropertiesController.setCurrentWord(this.currentWord);

            examplesTab.setDisable(false);
            propertiesTab.setDisable(false);

            translationTableView.setDisable(false);

            deleteButton.setVisible(true);
            cancelButton.setVisible(false);
            addButton.setVisible(true);

            System.out.println("===== SAVED");
        } catch (UniqueWordException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The word you trying to save already exists!", ButtonType.OK);
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
            // here should be logging
            Alert alert = new Alert(Alert.AlertType.ERROR, "Word could not be saved, check your log files", ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    public void deleteButtonOnAction() {
        wordService.delete(currentWord);

        translations.clear();
        translationTableView.setDisable(true);

        wordTextField.setText("");
        wordTypeChoiceBox.setValue(null);
        languageChoiceBox.setValue(null);
        priorityChoiceBox.setValue(null);

        resetCurrentWord();
        deleteButton.setVisible(false);
        saveButton.setVisible(false);
        cancelButton.setVisible(false);
        addButton.setVisible(true);

        examplesTab.setDisable(true);
        propertiesTab.setDisable(true);

        System.out.println("===== DELETED");
    }

    @FXML
    public void addButtonOnAction() {
        loadWord(new Word());

        saveButton.setVisible(true);
        cancelButton.setVisible(true);
        addButton.setVisible(false);
    }

    @FXML
    void cancelButtonOnAction() {
        saveButton.setVisible(false);
        cancelButton.setVisible(false);
        addButton.setVisible(true);
    }

    private void resetCurrentWord() {
        currentWord = new Word();
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

        Word translation2 = new Word();
        translation2.setWord("tun");
        translation2.setLanguage(Languages.GERMAN);

        wordTextField.setText(currentWord.getWord());
        wordTypeChoiceBox.setValue(currentWord.getWordType().toString());
        languageChoiceBox.setValue(currentWord.getLanguage().toString());

        translations.add(getEmptyWord());

        addButton.setVisible(false);
    }

    private Word getEmptyWord() {
        Word emptyTranslation = new Word();
        emptyTranslation.setWord("Enter word");

        return emptyTranslation;
    }

    public void loadWord(Word word) {
        System.out.println("LOADED WORD!");
        this.currentWord = word;

        wordTextField.setText(currentWord.getWord() != null ? currentWord.getWord() : "");
        wordTypeChoiceBox.setValue(currentWord.getWordType() != null ? currentWord.getWordType().toString() : null);
        languageChoiceBox.setValue(currentWord.getLanguage() != null ? currentWord.getLanguage().toString() : null);
        priorityChoiceBox.setValue(currentWord.getPriority());

        translations.clear();
        translations.addAll(emptyIfNull(word.getTranslations()));
        translations.add(getEmptyWord());

        this.wordsExamplesController.setCurrentWord(this.currentWord);
        this.wordsPropertiesController.setCurrentWord(this.currentWord);

        if (word.getId() == null) {
            deleteButton.setVisible(false);
            this.translationTableView.setDisable(true);
            this.examplesTab.setDisable(true);
            this.propertiesTab.setDisable(true);
        } else {
            deleteButton.setVisible(true);
            this.translationTableView.setDisable(false);
            this.examplesTab.setDisable(false);
            this.propertiesTab.setDisable(false);
        }
    }

    public void setExamplesController(ExamplesController wordsExamplesController) {
        this.wordsExamplesController = wordsExamplesController;
        this.wordsExamplesController.setDetailsController(this);
    }

    public void setExamplesTab(Tab examplesTab) {
        this.examplesTab = examplesTab;
    }

    public void setPropertiesController(PropertiesController wordsPropertiesController) {
        this.wordsPropertiesController = wordsPropertiesController;
        this.wordsPropertiesController.setDetailsController(this);
    }

    public void setPropertiesTab(Tab propertiesTab) {
        this.propertiesTab = propertiesTab;
    }
}