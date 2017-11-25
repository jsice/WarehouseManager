package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import ku.cs.duckdealer.models.Product;
import ku.cs.duckdealer.models.Sales;
import ku.cs.duckdealer.models.StockedProduct;

import java.util.ArrayList;

public class ReportController {

    @FXML
    private ComboBox reportPicker;
    @FXML
    private GridPane reportInnerPane, gridDateOption;
    @FXML
    private Tab displayChartTab;
    @FXML
    private RadioButton radioDay, radioMonth, radioCustom;
    @FXML
    private Label selectDateLabel, fromLabel, toLabel;
    @FXML
    private DatePicker fromPicker, toPicker;

    private MainController mainCtrl;
    private Pane mainPane;
    private ObservableList<PieChart.Data> pieChartData;
    private Chart chart;
    private ArrayList<Sales> allSales;

    @FXML
    public void initialize() {

        ObservableList<String> reportType = FXCollections.observableArrayList("Pie chart",
                "Bar chart",
                "Line chart"
        );

        reportPicker.setItems(reportType);

        reportPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectChart(newValue.toString());
        });

        ToggleGroup group = new ToggleGroup();
        radioDay.setToggleGroup(group);
        radioMonth.setToggleGroup(group);
        radioCustom.setToggleGroup(group);
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            setDateSelectingVisible(newValue);
        });
        gridDateOption.getChildren().clear();
    }

    private void selectChart(String type) {

        switch (type) {
            case "Pie chart":
                loadPieData();
                chart = new PieChart(pieChartData);
                ((PieChart) chart).setStartAngle(90);
                break;
            case "Line chart":
                break;
            case "Bar chart":
                CategoryAxis xAxis = new CategoryAxis();
                NumberAxis yAxis = new NumberAxis();
                chart = new BarChart<String, Number>(xAxis, yAxis);
                break;
        }
    }

    private void loadPieData(){
        pieChartData = FXCollections.observableArrayList(new PieChart.Data("Test1", 50),
                new PieChart.Data("Test2", 30), new PieChart.Data("Test3", 40));
    }

    private void setDateSelectingVisible(Toggle newValue) {
        RadioButton radio = (RadioButton) newValue;
        gridDateOption.getChildren().clear();

        if (radio.getId().equals("radioDay") || radio.getId().equals("radioMonth")) {
            FlowPane temp = new FlowPane();
            temp.getChildren().addAll(selectDateLabel, fromPicker);

            gridDateOption.add(temp, 0, 0);

        } else if (radio.getId().equals("radioCustom")) {
            FlowPane temp = new FlowPane();
            temp.getChildren().addAll(fromLabel, fromPicker);

            FlowPane temp2 = new FlowPane();
            temp2.getChildren().addAll(toLabel, toPicker);

            gridDateOption.add(temp, 0, 0);
            gridDateOption.add(temp2, 0, 1);
        }
    }

    public void showData() {
        loadPieData();
        displayChartTab.setContent(chart);
    }

    public Pane getMainPane() {
        return mainPane;
    }

    public void setMainPane(Pane mainPane) {
        this.mainPane = mainPane;
    }

    public void setMainCtrl(MainController mainCtrl) {
        this.mainCtrl = mainCtrl;
        allSales = mainCtrl.getSalesService().getAll();
        for (Sales sale:allSales
             ) {
            System.out.println(sale);
        }
    }

}
