package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import ku.cs.duckdealer.models.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javafx.scene.chart.XYChart;

public class ReportController {

    @FXML
    private ComboBox reportPicker;
    @FXML
    private GridPane reportInnerPane, gridDateOption;
    @FXML
    private Tab displayChartTab;
    @FXML
    private RadioButton radioStock, radioSales, radioDay, radioMonth, radioCustom;
    @FXML
    private Label selectDateLabel, fromLabel, toLabel, reportRangeLabel;
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
    private ArrayList<StockedProduct> allStockedProducts;
    private HashMap<String, Double> allItemPrice;
    private HashMap<String, Integer> allItemQuantity;
    private HashMap<String, String> idMapping;
    private ArrayList<ProductMovement> allProductMovement;
    private HashMap<String, Integer> allMovementCount;

    private ToggleGroup groupA;
    private ToggleGroup groupB;
    CategoryAxis xAxis ;
    NumberAxis yAxis ;
    @FXML
    public void initialize() {

        ObservableList<String> chartType = FXCollections.observableArrayList("Pie chart",
                "Bar chart"
        );
        reportPicker.setItems(chartType);
        reportPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectChart(newValue.toString());
        });

        groupA = new ToggleGroup();
        radioStock.setToggleGroup(groupA);
        radioSales.setToggleGroup(groupA);

        groupB = new ToggleGroup();
        radioDay.setToggleGroup(groupB);
        radioMonth.setToggleGroup(groupB);
        radioCustom.setToggleGroup(groupB);
        groupB.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            setDateSelectingVisible(newValue);
        });

        allItemPrice = new HashMap<>();
        allItemQuantity = new HashMap<>();
        idMapping = new HashMap<>();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("productID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        worthColumn.setCellValueFactory(new PropertyValueFactory<>("worth"));

        gridDateOption.getChildren().clear();
        pieChartData = FXCollections.observableArrayList();

       allProductMovement = new ArrayList<>() ;
       allMovementCount = new HashMap<>();


        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();

    }

    private void selectChart(String type) {

        switch (type) {
            case "Pie chart":
                pieChartData = FXCollections.observableArrayList();
                chart = new PieChart(pieChartData);
                ((PieChart) chart).setStartAngle(90);
                chart.setScaleX(0.9);
                chart.setScaleY(0.9);
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

    private void loadSalesData(Calendar date, String condition) {
        for (Sales sale : allSales
                ) {
            if (date.get(Calendar.YEAR) == sale.getDate().get(Calendar.YEAR) && date.get(Calendar.MONTH) == sale.getDate().get(Calendar.MONTH)) {
                if (condition.equals("month")) {
                    loadSalesDataHelper(sale);
                } else if (date.get(Calendar.DATE) == sale.getDate().get(Calendar.DATE)) {
                    loadSalesDataHelper(sale);
                }
            }
        }
    }


    private void loadSalesData(Calendar dateFrom, Calendar dateTo) {
        for (Sales sale : allSales
                ) {
            if (dateFrom.before(sale.getDate()) && dateTo.after(sale.getDate())) {
                loadSalesDataHelper(sale);
            }
        }
    }

    private void loadSalesDataHelper(Sales sale) {
        for (SalesItem item : sale.getItems()
                ) {
            if (idMapping.containsKey(item.getID())) {
                allItemPrice.put(item.getID(), allItemPrice.get(item.getID()) + item.getPrice()*item.getQuantity());
                allItemQuantity.put(item.getID(), allItemQuantity.get(item.getID()) + item.getQuantity());

            } else {
                idMapping.put(item.getID(), item.getName());
                allItemPrice.put(item.getID(), item.getPrice());
                allItemQuantity.put(item.getID(), item.getQuantity());
            }
        }
    }

    private void loadPieSalesData() {
        pieChartData.clear();
        chart.setTitle("NUMBER OF SALES ITEM");
        chart.setLegendSide(Side.RIGHT);

        for (String id : allItemPrice.keySet()
                ) {
            pieChartData.add(new PieChart.Data(idMapping.get(id), allItemPrice.get(id)));
        }
    }

    private void loadSalesDataToTable() {
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

    private void loadStockData() {
        for (StockedProduct prod : allStockedProducts) {
            Product product = prod.getProduct();
            if (idMapping.containsKey(product.getID())) {
                allItemPrice.put(product.getID(), allItemPrice.get(product.getID()) + product.getPrice());
                allItemQuantity.put(product.getID(), allItemQuantity.get(product.getID()) + prod.getQuantity());
                System.out.println(product.getName() + " --------- " + allItemPrice.get(product.getID()));

            } else {
                idMapping.put(product.getID(), product.getName());
                allItemPrice.put(product.getID(), product.getPrice());
                allItemQuantity.put(product.getID(), prod.getQuantity());
            }
        }
    }

    private void loadStockDataToTable() {
        ObservableList<ReportData> temp = FXCollections.observableArrayList();
        ArrayList<ReportData> temp2 = new ArrayList<>();
        for (String id : idMapping.keySet()) {
//            System.out.println(id + " " + idMapping.get(id) + " " + allItemQuantity.get(id) + " " + allItemPrice.get(id));
            temp2.add(new ReportData(id, idMapping.get(id), allItemQuantity.get(id), allItemPrice.get(id)));
        }
        temp.setAll(temp2);
        reportTable.setEditable(false);
        reportTable.setItems(temp);
        reportTable.setVisible(true);
    }

    private void loadPieStockData() {
        pieChartData.clear();
        for (String id : allItemPrice.keySet()) {
            pieChartData.add(new PieChart.Data(idMapping.get(id), allItemQuantity.get(id)));
        }
    }

    private Chart loadBarStockData() {
        pieChartData.clear();
        chart.setTitle("Bar chart");

        NumberAxis xAxis = new NumberAxis();
        CategoryAxis yAxis = new CategoryAxis();
        BarChart barChart = new BarChart<Number,String>(xAxis,yAxis);
        XYChart.Series series1 = new XYChart.Series();
        for (String id : allItemPrice.keySet()) {
            series1.getData().add(new XYChart.Data(allItemPrice.get(id),idMapping.get(id) ));
        }
//        series1.setName("");
        barChart.getData().add(series1);
        barChart.setTitle("DUCK DEALER'S STOCK BAR CHART REPORT");
        return barChart ;
    }

    private void loadMovementData(){

        for (ProductMovement movement : allProductMovement) {

            if (idMapping.containsKey(movement.getReason())) {
                allMovementCount.put(movement.getReason() , allMovementCount.get(movement.getReason() + 1 ));
            } else {
                allMovementCount.put(movement.getReason() , 1);
                  }
            System.out.println(movement.getReason()+"-----"+movement.getQuantity());


        }
    }

    private Chart loadBarMovementData(){
        pieChartData.clear();
        chart.setTitle("Bar chart");

        NumberAxis xAxis = new NumberAxis();
        CategoryAxis yAxis = new CategoryAxis();
        BarChart barChart = new BarChart<Number,String>(xAxis,yAxis);
        XYChart.Series series1 = new XYChart.Series();
        for (String key : allItemPrice.keySet()) {
            series1.getData().add(new XYChart.Data(allMovementCount.get(key),key ));
        }
        series1.setName("Reasons");
        barChart.getData().add(series1);
        barChart.setTitle("Stock Movement Summary");
        return barChart ;
    }


    private void loadMovementDataToTable(){
        ObservableList<ReportData> temp = FXCollections.observableArrayList();
        ArrayList<ReportData> temp2 = new ArrayList<>();
        for (String id : idMapping.keySet()) {
//            System.out.println(id + " " + idMapping.get(id) + " " + allItemQuantity.get(id) + " " + allItemPrice.get(id));
            temp2.add(new ReportData(id, idMapping.get(id), allItemQuantity.get(id), allItemPrice.get(id)));
        }
        temp.setAll(temp2);
        reportTable.setEditable(false);
        reportTable.setItems(temp);
        reportTable.setVisible(true);
    }




    public void showData() {
        reportRangeLabel.setVisible(false);
        reportTable.setVisible(false);
        allItemPrice.clear();
        allItemQuantity.clear();
        idMapping.clear();

        if (radioStock.isSelected()) {

            chart.setTitle("DUCK DEALER'S STOCK REPORT");
            chart.setLegendSide(Side.RIGHT);

            allStockedProducts = mainCtrl.getProductService().getAll();
            allProductMovement = mainCtrl.getProductMovementService().getAll();


            if ("Bar chart".equals(reportPicker.getValue())){
                loadMovementData();
//                loadBarStockData();
//                loadMovementDataToTable();
                displayChartTab.setContent(loadBarMovementData()) ;}
            else if ("Pie chart".equals(reportPicker.getValue())){
                loadStockData();
                loadPieStockData();
                loadStockDataToTable();
                displayChartTab.setContent(chart);}


        } else if (radioSales.isSelected() && !groupB.getSelectedToggle().equals(null)) {
            allSales = mainCtrl.getSalesService().getAll();

            reportRangeLabel.setVisible(true);
            reportTable.setVisible(true);

            if (radioCustom.isSelected()) {

                Calendar temp1 = new GregorianCalendar();
                Calendar temp2 = new GregorianCalendar();
                String[] temp3 = fromPicker.getValue().toString().split("-");
                temp1.set(Integer.valueOf(temp3[0]), Integer.valueOf(temp3[1]) - 1, Integer.valueOf(temp3[2]));
                temp3 = toPicker.getValue().toString().split("-");
                temp2.set(Integer.valueOf(temp3[0]), Integer.valueOf(temp3[1]) - 1, Integer.valueOf(temp3[2]));

                loadSalesData(temp1, temp2);

                reportRangeLabel.setText("sales report from " + temp1.get(Calendar.DATE) + "/" + temp1.get(Calendar.MONTH) + "/" + temp1.get(Calendar.YEAR)
                + " to " + temp2.get(Calendar.DATE) + "/" + temp2.get(Calendar.MONTH) + "/" + temp2.get(Calendar.YEAR));
            } else {
                Calendar temp1 = new GregorianCalendar();
                String[] temp3 = fromPicker.getValue().toString().split("-");
                temp1.set(Integer.valueOf(temp3[0]), Integer.valueOf(temp3[1]) - 1, Integer.valueOf(temp3[2]));
                String condition;
                String reportRange = "";
                if (radioDay.isSelected()) {
                    reportRange = "sales report on " + temp1.get(Calendar.DATE) + "/" + temp1.get(Calendar.MONTH) + "/" + temp1.get(Calendar.YEAR);

                    condition = "day";
                } else {
                    reportRange = "sales report on " + new SimpleDateFormat("MMMM").format(temp1.getTime()) + " in " + temp1.get(Calendar.YEAR);
                    condition = "month";
                }
                loadSalesData(temp1, condition);

                reportRangeLabel.setText(reportRange);
            }
            loadSalesDataToTable();
            loadPieSalesData();

            displayChartTab.setContent(chart);
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

    public class ReportData {
        private SimpleStringProperty productID;
        private SimpleStringProperty productName;
        private SimpleStringProperty worth;
        private SimpleStringProperty quantity;

        public ReportData(String id, String name, int q, double w) {
            productID = new SimpleStringProperty(id);
            productName = new SimpleStringProperty(name);
            quantity = new SimpleStringProperty(q + "");
            worth = new SimpleStringProperty(w + "");
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
