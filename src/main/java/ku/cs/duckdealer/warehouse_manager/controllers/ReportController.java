package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.print.Paper;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ku.cs.duckdealer.models.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class ReportController {


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
    private TableColumn<ReportData, String> idColumn, nameColumn, quantityColumn, worthColumn, dateColumn,reasonColumn;
    @FXML
    private Button printButton;
    @FXML
    private AnchorPane displayTableTab;

    private MainController mainCtrl;
    private Pane mainPane;
    private ObservableList<PieChart.Data> pieChartData;
    private Chart chart;
    private ArrayList<Sales> allSales;
    private ArrayList<StockedProduct> allStockedProducts;
    private HashMap<String, Double> allItemPrice;
    private HashMap<String, Integer> allItemQuantity;
    private HashMap<String, String> idMapping;
    private HashMap<String, Integer> allMovementCount;
    private ArrayList<ProductMovement> allProductMovement;

    private ToggleGroup groupA;
    private ToggleGroup groupB;
    CategoryAxis xAxis;
    NumberAxis yAxis;

    @FXML
    public void initialize() {

        dateColumn = new TableColumn<ReportData, String>() ;
        reasonColumn = new TableColumn<ReportData, String>() ;

        reportTable.setVisible(false);
        printButton.setVisible(false);

        Image printer = new Image(getClass().getResourceAsStream("/assets/printer.png"));
        printButton.setGraphic(new ImageView(printer));

        ObservableList<String> chartType = FXCollections.observableArrayList("Bar chart"
        );


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


        gridDateOption.getChildren().clear();


        allProductMovement = new ArrayList<>();
        allMovementCount = new HashMap<>();


        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();

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
        for (Sales sale : allSales) {
            if (dateFrom.before(sale.getDate()) && dateTo.after(sale.getDate())) {
                loadSalesDataHelper(sale);
            }
        }
    }

    private void loadSalesDataHelper(Sales sale) {
        for (SalesItem item : sale.getItems()
                ) {
            if (idMapping.containsKey(item.getID())) {
                allItemPrice.put(item.getID(), allItemPrice.get(item.getID()) + item.getPrice() * item.getQuantity());
                allItemQuantity.put(item.getID(), allItemQuantity.get(item.getID()) + item.getQuantity());

            } else {
                idMapping.put(item.getID(), item.getName());
                allItemPrice.put(item.getID(), item.getPrice());
                allItemQuantity.put(item.getID(), item.getQuantity());
            }
        }
    }

    private void loadPieSalesData() {
        pieChartData = FXCollections.observableArrayList();
        chart.setTitle("INCOME FROM SOLD ITEM");
        chart.setLegendSide(Side.RIGHT);
        double sum = 0;
        for (String price:allItemPrice.keySet()){
            sum+=allItemPrice.get(price);
        }

        for (String id : allItemPrice.keySet()
                ) {
            pieChartData.add(new PieChart.Data(idMapping.get(id), Double.valueOf(String.format("%.2f", allItemPrice.get(id) * 100 / sum))));

        }
    }

    private void loadSalesDataToTable() {

        ObservableList<ReportData> temp = FXCollections.observableArrayList();
        ArrayList<ReportData> temp2 = new ArrayList<>();
        for (String id : idMapping.keySet()) {
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

    private void loadMovementCount(){
        for (ProductMovement movement : allProductMovement) {
            if (allMovementCount.containsKey(movement.getReason())) {
                allMovementCount.put(movement.getReason(), allMovementCount.get(movement.getReason()) + 1);
            } else {
                allMovementCount.put(movement.getReason(), 1);
            }
        }
    }


    private void loadMovementData() {
        loadMovementCount();
    }

    private void loadMovementData(Calendar fromDate, Calendar toDate){
        ArrayList<ProductMovement> temp = new ArrayList<>();
        for (ProductMovement movement : allProductMovement) {
            if (fromDate.before(movement.getDate()) && toDate.after(movement.getDate())) {
                temp.add(movement);
            }
        }
        allProductMovement = temp ;
        loadMovementCount();
    }

    private void loadMovementData(Calendar date, String condition) {
        ArrayList<ProductMovement> temp = new ArrayList<>();
        for (ProductMovement movement : allProductMovement) {
            if (date.get(Calendar.YEAR) == movement.getDate().get(Calendar.YEAR) && date.get(Calendar.MONTH) == movement.getDate().get(Calendar.MONTH)) {
                if (condition.equals("month")) {
                    temp.add(movement);
                } else if (date.get(Calendar.DATE) == movement.getDate().get(Calendar.DATE)) {
                    temp.add(movement);
                }
            }
        }
        allProductMovement = temp ;
        loadMovementCount();
    }


    private Chart loadBarMovementData() {
//        pieChartData.clear();

        NumberAxis xAxis = new NumberAxis();
        CategoryAxis yAxis = new CategoryAxis();
        BarChart barChart = new BarChart<Number, String>(xAxis, yAxis);
        XYChart.Series series1 = new XYChart.Series();
        for (String key : allMovementCount.keySet()) {
            series1.getData().add(new XYChart.Data(allMovementCount.get(key), key));

        }
        series1.setName("Reasons");
        barChart.getData().add(series1);
        barChart.setTitle("Stock Movement Summary");
        barChart.setVisible(true);
        return barChart;
    }


    private void loadMovementDataToTable() {

        dateColumn.setText("Date");
        reasonColumn.setText("Reason");

        TableColumn<ReportData, String> dateColumn = new TableColumn<>(); dateColumn.setText("Date");dateColumn.setPrefWidth(150);
        TableColumn<ReportData, String> idColumn = new TableColumn<>(); idColumn.setText("ID");idColumn.setPrefWidth(100);
        TableColumn<ReportData, String> nameColumn = new TableColumn<>(); nameColumn.setText("Name");nameColumn.setPrefWidth(150);
        TableColumn<ReportData, String> quantityColumn = new TableColumn<>(); quantityColumn.setText("Quantity");quantityColumn.setPrefWidth(100);
        TableColumn<ReportData, String> reasonColumn = new TableColumn<>(); reasonColumn.setText("Reason");reasonColumn.setPrefWidth(150);

        reportTable.getColumns().addAll(dateColumn, idColumn, nameColumn, quantityColumn, reasonColumn);

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("productID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));

        ObservableList<ReportData> temp = FXCollections.observableArrayList();
        ArrayList<ReportData> temp2 = new ArrayList<>();
        for (ProductMovement movement : allProductMovement) {
            temp2.add(new ReportData(convertCalendatToString(movement.getDate()),movement.getProduct().getID(), movement.getProduct().getName(), movement.getQuantity(), movement.getReason()));
        }
        temp.setAll(temp2);
        reportTable.setEditable(false);
        reportTable.setItems(temp);
        reportTable.setVisible(true);
    }

    private String convertCalendatToString(Calendar date){
        String temp = "";
        temp += date.get(Calendar.DATE)+"/"+ date.get(Calendar.MONTH)+"/"+ date.get(Calendar.YEAR)+" ";
        temp += date.get(Calendar.HOUR) + ":" +date.get(Calendar.MINUTE);
        return temp ;
    }


    public void showData() {
        reportRangeLabel.setVisible(false);
        reportTable.setVisible(false);
        printButton.setVisible(true);

        allItemPrice.clear();
        allItemQuantity.clear();
        idMapping.clear();

        reportTable.getColumns().clear();

        if (radioStock.isSelected()) {

            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            chart = new BarChart<String, Number>(xAxis, yAxis);

            chart.setTitle("DUCK DEALER'S STOCK REPORT");
            chart.setLegendSide(Side.RIGHT);

            allStockedProducts = mainCtrl.getProductService().getAll();
            allProductMovement = mainCtrl.getProductMovementService().getAll();

            if (radioCustom.isSelected()) {

                Calendar temp1 = new GregorianCalendar();
                Calendar temp2 = new GregorianCalendar();
                String[] temp3 = fromPicker.getValue().toString().split("-");
                temp1.set(Integer.valueOf(temp3[0]), Integer.valueOf(temp3[1]) - 1, Integer.valueOf(temp3[2]));
                temp3 = toPicker.getValue().toString().split("-");
                temp2.set(Integer.valueOf(temp3[0]), Integer.valueOf(temp3[1]) - 1, Integer.valueOf(temp3[2]));

                loadMovementData(temp1, temp2);

                reportRangeLabel.setText("Stock report from " + temp1.get(Calendar.DATE) + "/" + temp1.get(Calendar.MONTH) + "/" + temp1.get(Calendar.YEAR)
                        + " to " + temp2.get(Calendar.DATE) + "/" + temp2.get(Calendar.MONTH) + "/" + temp2.get(Calendar.YEAR));
            } else {
                Calendar temp1 = new GregorianCalendar();
                String[] temp3 = fromPicker.getValue().toString().split("-");
                temp1.set(Integer.valueOf(temp3[0]), Integer.valueOf(temp3[1]) - 1, Integer.valueOf(temp3[2]));
                String condition;
                String reportRange = "";
                if (radioDay.isSelected()) {
                    reportRange = "Stock report on " + temp1.get(Calendar.DATE) + "/" + temp1.get(Calendar.MONTH) + "/" + temp1.get(Calendar.YEAR);

                    condition = "day";
                } else {
                    reportRange = "Stock report on " + new SimpleDateFormat("MMMM").format(temp1.getTime()) + " in " + temp1.get(Calendar.YEAR);
                    condition = "month";
                }
                loadMovementData(temp1, condition);

                reportRangeLabel.setText(reportRange);
            }

//            loadMovementData();
            loadMovementDataToTable();
            displayChartTab.setContent(loadBarMovementData());

        } else if (radioSales.isSelected() && !groupB.getSelectedToggle().equals(null)) {

            TableColumn<ReportData, String> idColumn = new TableColumn<>();
            TableColumn<ReportData, String> nameColumn = new TableColumn<>();
            TableColumn<ReportData, String> quantityColumn = new TableColumn<>();
            TableColumn<ReportData, String> worthColumn = new TableColumn<>();

            idColumn.setText("Product's ID");
            idColumn.setPrefWidth(128);
            nameColumn.setText("Product's Name");
            nameColumn.setPrefWidth(280);
            quantityColumn.setText("Quantity");
            quantityColumn.setPrefWidth(108);
            worthColumn.setText("Price");
            worthColumn.setPrefWidth(147);

            reportTable.getColumns().addAll(idColumn, nameColumn, quantityColumn, worthColumn);

            idColumn.setCellValueFactory(new PropertyValueFactory<>("productID"));
            idColumn.setStyle( "-fx-alignment: CENTER;");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            quantityColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");
            worthColumn.setCellValueFactory(new PropertyValueFactory<>("worth"));
            worthColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");
            chart = new PieChart();
            ((PieChart) chart).setStartAngle(90);

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

            ((PieChart) chart).setData(pieChartData);

            final Label caption = new Label("");
            caption.setTextFill(Color.BLACK);
            caption.setStyle("-fx-font: 24 arial;");

            for (final PieChart.Data data : ((PieChart) chart).getData()) {
                data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED,
                        new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent e) {
                                caption.setTranslateX(e.getSceneX() - 260);
                                caption.setTranslateY(e.getSceneY() - 40);
                                caption.setText(String.valueOf(data.getPieValue()) + "%");
                            }
                        });
            }
            AnchorPane tempPane = new AnchorPane();
            chart.setPrefSize(744, 600);
            tempPane.getChildren().addAll(chart, caption);
            displayChartTab.setContent(tempPane);

        }
    }

    @FXML
    private void print() {
        ObservableList<ReportData> list = reportTable.getItems();
        int start = 0;
        while (start < list.size()) {
            ObservableList<ReportData> newList = FXCollections.observableArrayList();
            if (list.size() - start >= 20) {
                newList.addAll(list.subList(start, start+20));
                start = start+20;
            } else {
                newList.addAll(list.subList(start, list.size()));
                start = list.size();
            }
            Node node = createReport(newList);
            mainCtrl.getPrintService().print(node, Paper.A4);

        }
    }

    private Node createReport(ObservableList<ReportData> data) {
        GridPane report = new GridPane();
        report.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        report.setVgap(20);
        report.setPadding(new Insets(50, 50, 50, 50));
        report.setAlignment(Pos.CENTER);
        Label title = new Label("DUCK DEALER's REPORT");
        title.setPrefWidth(450);
        title.setAlignment(Pos.CENTER);
        title.setFont(Font.font(24));
        report.add(title, 0, 0);
        Label subtitle = new Label(reportRangeLabel.getText());
        subtitle.setPrefWidth(450);
        subtitle.setAlignment(Pos.CENTER);
        report.add(subtitle, 0, 1);
        TableView<ReportData> tableView = new TableView<>();
        tableView.setEditable(false);
        tableView.setItems(data);
        tableView.getColumns().addAll(reportTable.getColumns());
        tableView.setFixedCellSize(30);
        tableView.prefHeightProperty().bind(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(32));
        report.add(tableView, 0, 2);

        Stage stage = new Stage();
        Scene scene = new Scene(report);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initOwner(this.displayTableTab.getScene().getWindow());
        stage.show();

        title.setPrefWidth(tableView.getWidth());
        subtitle.setPrefWidth(tableView.getWidth());
        stage.hide();

        return report;
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

        private SimpleStringProperty reason;
        private SimpleStringProperty date;

        public ReportData(String id, String name, int q, double w) {
            productID = new SimpleStringProperty(id);
            productName = new SimpleStringProperty(name);
            quantity = new SimpleStringProperty(q + "");
            worth = new SimpleStringProperty(w + "");
        }

        public ReportData(String date,String id, String name, int q, String reas) {
            this.date = new SimpleStringProperty(date);
            productID = new SimpleStringProperty(id);
            productName = new SimpleStringProperty(name);
            quantity = new SimpleStringProperty(q + "");
            reason = new SimpleStringProperty(reas);
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

        public String getReason() {
            return reason.get();
        }

        public String getDate() { return date.get(); }

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
