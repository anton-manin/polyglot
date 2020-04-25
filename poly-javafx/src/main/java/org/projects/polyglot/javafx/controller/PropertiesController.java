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
import org.projects.polyglot.core.domain.Property;
import org.projects.polyglot.core.domain.Word;
import org.projects.polyglot.core.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.projects.polyglot.core.util.Util.emptyIfNull;

@Component
public class PropertiesController {

    @FXML
    private TableView<Property> propertyTableView;

    @FXML
    private TableColumn<Property, String> keyTableColumn;

    @FXML
    private TableColumn<Property, String> valueTableColumn;

    @FXML
    private TableColumn<String, String> actionTableColumn;

    private final ObservableList<Property> properties = FXCollections.observableArrayList();

    Word currentWord;

    @Autowired
    WordService wordService;

    DetailsController detailsController;

    public void init() {
        propertyTableView.setEditable(true);


        keyTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Property, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Property, String> param)
            {
                return new ReadOnlyStringWrapper(param.getValue().getKey());
            }
        });
        keyTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        keyTableColumn.setOnEditCommit((TableColumn.CellEditEvent<Property, String> t) ->
        {
            Property selectedProperty = t.getTableView().getItems().get(t.getTablePosition().getRow());
            selectedProperty.setKey(t.getNewValue());

            if (selectedProperty.getId() != null) {
                wordService.update(selectedProperty.getWord());
            }

            t.getTableView().refresh();
        });


        valueTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Property, String>, ObservableValue<String>>()
        {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Property, String> param)
            {
                return new ReadOnlyStringWrapper(param.getValue().getValue());
            }
        });
        valueTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        valueTableColumn.setOnEditCommit((TableColumn.CellEditEvent<Property, String> t) ->
        {
            Property selectedProperty = t.getTableView().getItems().get(t.getTablePosition().getRow());
            selectedProperty.setValue(t.getNewValue());

            if (selectedProperty.getId() != null) {
                wordService.update(selectedProperty.getWord());
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
                        System.out.println("========== Save Property Button clicked");
                        Property property = properties.get(this.getIndex());

                        if (property != null &&
                                property.getKey() != null && !property.getKey().equals("Enter key") &&
                                property.getValue() != null && !property.getValue().equals("Enter value")) {
                            property.setWord(currentWord);
                            System.out.println("Add Property");
                            currentWord = wordService.addProperty(currentWord, property);

                            properties.add(getEmptyProperty());
                            detailsController.loadWord(currentWord);
                        }
                    });

                    deleteTranslationButton.setOnAction(event -> {
                        System.out.println("========== Delete Property Button clicked");
                        Property property = properties.get(this.getIndex());
                        System.out.println("Delete Property");
                        currentWord = wordService.deleteProperty(currentWord, property);

                        properties.remove(property);
                        detailsController.loadWord(currentWord);
                    });

                    if (this.getIndex() == properties.size() - 1) {
                        setGraphic(saveTranslationButton);
                    } else {
                        setGraphic(deleteTranslationButton);
                    }

                    setText(null);
                }
            }
        });

        keyTableColumn.setEditable(true);
        valueTableColumn.setEditable(true);

        propertyTableView.setItems(properties);

    }

    private Property getEmptyProperty() {
        Property emptyProperty = new Property();

        emptyProperty.setKey("Enter key");
        emptyProperty.setValue("Enter value");

        return emptyProperty;
    }

    public void setCurrentWord(Word currentWord) {
        this.currentWord = currentWord;
        this.properties.clear();
        this.properties.addAll(emptyIfNull(this.currentWord.getProperties()));
        this.properties.add(getEmptyProperty());
    }

    public void setDetailsController(DetailsController detailsController) {
        this.detailsController = detailsController;
    }

}
