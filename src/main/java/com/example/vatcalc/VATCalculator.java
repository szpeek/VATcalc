package com.example.vatcalc;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VATCalculator extends Application {

    private TextField baseValueField;
    private ComboBox<String> vatRateComboBox;
    private Label resultNetLabel;
    private Label resultVATLabel;
    private Label resultGrossLabel;
    private ToggleGroup calculationMethodGroup;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Kalkulator VAT netto-brutto");

        calculationMethodGroup = new ToggleGroup();
        RadioButton nettoToBrutto = new RadioButton("Od netto do brutto");
        nettoToBrutto.setToggleGroup(calculationMethodGroup);

        RadioButton bruttoToNetto = new RadioButton("Od brutto do netto");
        bruttoToNetto.setToggleGroup(calculationMethodGroup);

        RadioButton vatAdjustment = new RadioButton("Dopasuj do kwoty VAT");
        vatAdjustment.setToggleGroup(calculationMethodGroup);

        VBox calculationMethodBox = new VBox(5, nettoToBrutto, bruttoToNetto, vatAdjustment);
        calculationMethodBox.setPadding(new Insets(10));
        calculationMethodBox.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 5px;");
        calculationMethodBox.setPrefWidth(300);

        Label methodLabel = new Label("Metoda obliczeń:");
        VBox methodBox = new VBox(5, methodLabel, calculationMethodBox);

        baseValueField = new TextField();
        baseValueField.setPromptText("0.00");

        vatRateComboBox = new ComboBox<>();
        vatRateComboBox.getItems().addAll("23%", "8%", "5%", "0%");
        vatRateComboBox.setValue("23%");

        GridPane dataPane = new GridPane();
        dataPane.setVgap(10);
        dataPane.setHgap(10);
        dataPane.setPadding(new Insets(10));
        dataPane.add(new Label("Wartość bazowa:"), 0, 0);
        dataPane.add(baseValueField, 1, 0);
        dataPane.add(new Label("Stawka VAT:"), 0, 1);
        dataPane.add(vatRateComboBox, 1, 1);
        dataPane.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 5px;");
        dataPane.setPrefWidth(300);

        Label dataLabel = new Label("Dane:");
        VBox dataBox = new VBox(5, dataLabel, dataPane);

        resultNetLabel = new Label("Netto: ");
        resultVATLabel = new Label("VAT: ");
        resultGrossLabel = new Label("Brutto: ");
        VBox resultBox = new VBox(5, resultNetLabel, resultVATLabel, resultGrossLabel);
        resultBox.setPadding(new Insets(10));
        resultBox.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 5px;");
        resultBox.setPrefWidth(300);

        Label resultLabel = new Label("Wyniki:");
        VBox resultContainer = new VBox(5, resultLabel, resultBox);

        Button calculateButton = new Button("OBLICZ");
        calculateButton.setOnAction(e -> calculate());

        Button closeButton = new Button("Zamknij");
        closeButton.setOnAction(e -> stage.close());

        HBox buttonBox = new HBox(10, calculateButton, closeButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setSpacing(120);

        VBox mainBox = new VBox(10, methodBox, dataBox, buttonBox, resultContainer);
        mainBox.setPadding(new Insets(10));
        mainBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainBox, 350, 450);
        stage.setScene(scene);
        stage.show();
    }

    private void calculate() {
        try {
            double baseValue = Double.parseDouble(baseValueField.getText());
            double vatRate = getSelectedVatRate();

            String selectedMethod = ((RadioButton) calculationMethodGroup.getSelectedToggle()).getText();
            double netValue = 0;
            double vatValue = 0;
            double grossValue = 0;

            switch (selectedMethod) {
                case "Od netto do brutto":
                    netValue = baseValue;
                    vatValue = netValue * (vatRate / 100);
                    grossValue = netValue + vatValue;
                    break;

                case "Od brutto do netto":
                    grossValue = baseValue;
                    netValue = grossValue / (1 + (vatRate / 100));
                    vatValue = grossValue - netValue;
                    break;

                case "Dopasuj do kwoty VAT":
                    vatValue = baseValue;
                    netValue = vatValue / (vatRate / 100);
                    grossValue = netValue + vatValue;
                    break;
            }

            resultNetLabel.setText(String.format("Netto: %.2f", netValue));
            resultVATLabel.setText(String.format("VAT: %.2f @ %.0f%%", vatValue, vatRate));
            resultGrossLabel.setText(String.format("Brutto: %.2f", grossValue));
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Wprowadź prawidłową wartość liczbową.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private double getSelectedVatRate() {
        String selectedRate = vatRateComboBox.getValue().replace("%", "");
        return Double.parseDouble(selectedRate);
    }
}
