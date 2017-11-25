package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import ku.cs.duckdealer.models.Sales;
import ku.cs.duckdealer.models.SalesItem;

import java.util.ArrayList;
import java.util.HashMap;

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
    @FXML
    private TableView<ReportData> reportTable;
    @FXML
    private TableColumn<ReportData, String> idColumn, nameColumn, quantityColumn, worthColumn;

    private MainController mainCtrl;
    private Pane mainPane;
    private ObservableList<PieChart.Data> pieChartData;
    private Chart chart;
    private ArrayList<Sales> allSales;
    private HashMap<String, Double> allItemPrice;
    private HashMap<String, Integer> allItemQuantity;
    private HashMap<String, String> idMapping;

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

        allItemPrice = new HashMap<>();
        allItemQuantity = new HashMap<>();
        idMapping = new HashMap<>();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("productID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("worth"));
        worthColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        gridDateOption.getChildren().clear();
    }

    private void selectChart(String type) {

        switch (type) {
            case "Pie chart":
                pieChartData = FXCollections.observableArrayList();
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

    private void loadData() {
        for (Sales sale : allSales
                ) {
            for (SalesItem item : sale.getItems()
                    ) {
                if (idMapping.containsKey(item.getID())) {
                    allItemPrice.put(item.getID(), allItemPrice.get(item.getID()) + item.getPrice());
                    allItemQuantity.put(item.getID(), allItemQuantity.get(item.getID()) + item.getQuantity());

                } else {
                    idMapping.put(item.getID(), item.getName());
                    allItemPrice.put(item.getID(), item.getPrice());
                    allItemQuantity.put(item.getID(), item.getQuantity());
                }
            }
        }
    }

    private void loadPieData() {
        for (String id : allItemPrice.keySet()
                ) {
            pieChartData.add(new PieChart.Data(idMapping.get(id), allItemPrice.get(id)));
        }
    }

    private void loadDataToTable() {
        ObservableList<ReportData> temp = FXCollections.observableArrayList();
        ArrayList<ReportData> temp2 = new ArrayList<>();
        for (String id : idMapping.keySet()
                ) {
            temp2.add(new ReportData(id, idMapping.get(id), allItemQuantity.get(id), allItemPrice.get(id)));
        }
        temp.setAll(temp2);
        reportTable.setEditable(false);
        reportTable.setItems(temp);
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
        allItemPrice.clear();
        allItemQuantity.clear();
        idMapping.clear();

        chart.setTitle("DUCK DEALER'S REPORT");
        chart.setLegendSide(Side.RIGHT);

        allSales = mainCtrl.getSalesService().getAll();
        loadData();
        loadDataToTable();
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
    }

    public class ReportData {
        private SimpleStringProperty productID;
        private SimpleStringProperty productName;
        private SimpleStringProperty worth;
        private SimpleStringProperty quantity;

        public ReportData(String id, String name, int q, double w) {
            productID = new SimpleStringProperty(id);
            productName = new SimpleStringProperty(name);
            quantity = new SimpleStringProperty(q+"");
            worth = new SimpleStringProperty(w+"");
        }

        public String getProductID() {
            return productID.get();
        }

        public SimpleStringProperty productIDProperty() {
            return productID;
        }

        public String getProductName() {
            return productName.get();
        }

        public SimpleStringProperty productNameProperty() {
            return productName;
        }

        public String getWorth() {
            return worth.get();
        }

        public SimpleStringProperty worthProperty() {
            return worth;
        }

        public String getQuantity() {
            return quantity.get();
        }

        public SimpleStringProperty quantityProperty() {
            return quantity;
        }
    }
}
