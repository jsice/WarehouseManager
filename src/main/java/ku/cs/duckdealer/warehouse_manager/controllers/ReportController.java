package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.Chart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ReportController {

    @FXML
    private ComboBox reportPicker;
    @FXML
    private GridPane reportInnerPane;
    @FXML
    private Tab displayChartTab;

    private MainController mainCtrl;
    private Pane mainPane;
    private ObservableList<PieChart.Data> pieChartData;
    private Chart chart ;

    @FXML
    public void initialize() {
        ObservableList<String> reportType = FXCollections.observableArrayList("Pie chart",
                "Bar chart",
                "Area chart",
                "Line chart",
                "Scatter chart"
        );

        reportPicker.setItems(reportType);

        reportPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectChart(newValue.toString());
        });

        pieChartData = FXCollections.observableArrayList(new PieChart.Data("Test1", 50),
                new PieChart.Data("Test2", 30), new PieChart.Data("Test3", 40));
        chart = new PieChart(pieChartData);
            displayChartTab.setContent(chart);
//        pie.setData(pieChartData);
//        pie.setTitle("Test Chart");
//        pie.setStartAngle(90);
    }

    public void selectChart(String type) {

        switch (type) {
            case "Pie chart":
                chart = new PieChart(pieChartData);
                reportInnerPane.add(chart, 1, 0);
                break;
        }
    }

    public Pane getMainPane() {
        return mainPane;
    }

    public void setMainPane(Pane mainPane) {
        this.mainPane = mainPane;
    }

    public void setMainCtrl(MainController mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

}
