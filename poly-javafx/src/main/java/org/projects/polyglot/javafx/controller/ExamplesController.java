package org.projects.polyglot.javafx.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import org.projects.polyglot.core.domain.Example;
import org.projects.polyglot.core.domain.Word;
import org.projects.polyglot.core.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.projects.polyglot.core.util.Util.emptyIfNull;

@Component
public class ExamplesController {

    @FXML
    private TableView<Example> exampleTableView;

    @FXML
    private TableColumn<Example, String> exampleTableColumn;

    @FXML
    private TableColumn<String, String> actionTableColumn;

    private final ObservableList<Example> examples = FXCollections.observableArrayList();

    Word currentWord;

    @Autowired
    WordService wordService;

    DetailsController detailsController;

    public void init() {
        exampleTableView.setEditable(true);

        exampleTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Example, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Example, String> param)
            {
                return new ReadOnlyStringWrapper(param.getValue().getExample());
            }
        });
        exampleTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        exampleTableColumn.setOnEditCommit((TableColumn.CellEditEvent<Example, String> t) ->
        {
            Example selectedTranslation = ((Example) t.getTableView().getItems().get(t.getTablePosition().getRow()));
            selectedTranslation.setExample(t.getNewValue());

            if (selectedTranslation.getId() != null) {
                wordService.update(selectedTranslation.getWord());
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
                        Example example = examples.get(this.getIndex());

                        if (example != null &&
                                example.getExample() != null && !example.getExample().equals("Enter example")) {
                            example.setWord(currentWord);
                            System.out.println("Add Example");
                            currentWord = wordService.addExample(currentWord, example);

                            examples.add(getEmptyExample());
                            detailsController.loadWord(currentWord);
                        }
                    });

                    deleteTranslationButton.setOnAction(event -> {
                        System.out.println("========== Delete Translation Button clicked");
                        Example translation = examples.get(this.getIndex());
                        System.out.println("Delete Example");
                        currentWord = wordService.deleteExample(currentWord, translation);

                        examples.remove(translation);
                        detailsController.loadWord(currentWord);
                    });

                    System.out.println(this.getIndex() + " == " + examples.size());
                    if (this.getIndex() == examples.size() - 1) {
                        System.out.println("Equal");
                        setGraphic(saveTranslationButton);
                    } else {
                        setGraphic(deleteTranslationButton);
                    }

                    setText(null);
                }
            }
        });

        exampleTableColumn.setEditable(true);

        exampleTableView.setItems(examples);
    }

    private Example getEmptyExample() {
        Example emptyTranslation = new Example();
        emptyTranslation.setExample("Enter example");

        return emptyTranslation;
    }

    public void setCurrentWord(Word currentWord) {
        this.currentWord = currentWord;
        this.examples.clear();
        this.examples.addAll(emptyIfNull(this.currentWord.getExamples()));
        this.examples.add(getEmptyExample());
    }

    public void setDetailsController(DetailsController detailsController) {
        this.detailsController = detailsController;
    }
}
