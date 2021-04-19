package nanostock.app;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nanostock.app.logic.Switcher;
import nanostock.model.table.Command;
import nanostock.model.table.DAO.FactoryDAO;
import nanostock.model.table.Operation;
import nanostock.model.table.generic.CommandInventory;
import nanostock.model.table.generic.HistoryBuilder;
import nanostock.model.table.generic.WatchDogApp;

import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SellHistoryViewController extends Switcher implements Initializable {

    private int operationType;
    private ArrayList<Integer> commandNumList;

    @FXML
    TableView<CommandInventory> sellOperationTableView;
    @FXML
    TableColumn<CommandInventory, Integer> commandNumCol;
    @FXML
    TableColumn<CommandInventory, Integer> commandQuantityCol;
    @FXML
    TableColumn<CommandInventory, Double> commandTotalCoastCol;
    @FXML
    TableColumn<CommandInventory, String> commandSellerCol;
    @FXML
    TableColumn<CommandInventory, String> commandDeliveryAddressCol;
    @FXML
    TableColumn<CommandInventory, String> commandClientNameCol;
    @FXML
    TableColumn<CommandInventory, String> commandDateCol;
    @FXML
    TableColumn<CommandInventory, String> commandTimeCol;

    @FXML
    JFXTimePicker startTimeField;
    @FXML
    JFXTimePicker endTimeField;
    @FXML
    JFXDatePicker startDateField;
    @FXML
    JFXDatePicker endDateField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.operationType = 4;
        this.commandNumCol.setCellValueFactory(new PropertyValueFactory<CommandInventory, Integer>("commandNum"));
        this.commandQuantityCol.setCellValueFactory(new PropertyValueFactory<CommandInventory, Integer>("commandItemQuantity"));
        this.commandTotalCoastCol.setCellValueFactory(new PropertyValueFactory<CommandInventory, Double>("commandTotalCost"));
        this.commandSellerCol.setCellValueFactory(new PropertyValueFactory<CommandInventory, String>("commandSeller"));
        this.commandDeliveryAddressCol.setCellValueFactory(new PropertyValueFactory<CommandInventory, String>("deliveryAddress"));
        this.commandClientNameCol.setCellValueFactory(new PropertyValueFactory<CommandInventory, String>("clientName"));
        this.commandDateCol.setCellValueFactory(new PropertyValueFactory<CommandInventory, String>("commandDate"));
        this.commandTimeCol.setCellValueFactory(new PropertyValueFactory<CommandInventory, String>("commandTime"));

        ArrayList<HistoryBuilder> historyList = this.getAll();
        this.commandNumList = HistoryBuilder.getAllCommandNum(historyList);

        this.sellOperationTableView.setItems(this.getAllCommandInventory());

    }

    private ObservableList<CommandInventory> getAllCommandInventory() {
        ArrayList<HistoryBuilder> historyList = this.getAll();
        ArrayList<Integer> commandNumList = HistoryBuilder.getAllCommandNum(historyList);
        ObservableList<CommandInventory> commandInventoryList = FXCollections.observableArrayList();
        for (Integer i : commandNumList) {
            commandInventoryList.add(HistoryBuilder.buildCommandInventory(historyList, i));
        }
        return commandInventoryList;
    }

    private ArrayList<CommandInventory> getAllCommandInventoryList() {
        ArrayList<HistoryBuilder> historyList = this.getAll();
        ArrayList<CommandInventory> commandInventoryList = new ArrayList<>();
        for (Integer i : this.commandNumList) {
            commandInventoryList.add(HistoryBuilder.buildCommandInventory(historyList, i));
        }
        return commandInventoryList;
    }

    private ArrayList<HistoryBuilder> getAll() {
        ArrayList<HistoryBuilder> sellOperationList = new ArrayList<>();
        ArrayList<Operation> operationList = FactoryDAO.getOperationDAO().getAll();
        HistoryBuilder historyBuilder;
        for (Operation operation : operationList) {

            historyBuilder = new HistoryBuilder(operation);

            if (historyBuilder.getOperationType() == this.operationType) {
                sellOperationList.add(historyBuilder);
            }
        }

        return sellOperationList;
    }

    private double getTotalCoast() {
        ObservableList<CommandInventory> commandInventoryList = FXCollections.observableArrayList();
        commandInventoryList = this.sellOperationTableView.getItems();
        double totalCoast = 0;
        for (CommandInventory commandInventory : commandInventoryList) {
            totalCoast += commandInventory.getCommandTotalCost();
        }
        return totalCoast;
    }

    public void getCommandDetails(MouseEvent event) {
        CommandInventory selectedCommandInventory = null;
        selectedCommandInventory = this.sellOperationTableView.getSelectionModel().getSelectedItem();
        if (selectedCommandInventory != null) {
            ArrayList<HistoryBuilder> historyList = this.getAll();
            ArrayList<HistoryBuilder> historyCommandList
                    = HistoryBuilder.getAllHistoryByCommandNum(historyList,
                            selectedCommandInventory.getCommandNum());
            StringBuilder textInformation = new StringBuilder();
            for (HistoryBuilder history : historyCommandList) {
                textInformation.append("-> Reference : ").append(history.getItemReference()).
                        append("\n\t Date et heure de vente : ").append(history.getDate()).append(history.getTime()).
                        append("\n\t Vendu à : ").append(history.getSoldPrice()).append(" FCFA").
                        append("\n\t Quantité vendue : ").append(history.getQuantity()).
                        append("\n\t Client : ").append(history.getClientName()).
                        append("\n\n");
            }
            this.alert("Info Commande", null,
                    "Liste des articles de la commande vendue", "Liste des articles",
                    textInformation.toString());
        }
    }

    public void getTotalSellCoast(MouseEvent event) {
        alertInfo("Vente de la période", null,
                "Les ventes totales de la période s'élèvent à : " + this.getTotalCoast()
                + " FCFA");
    }

    public void reinitializeTable(MouseEvent event) {
        this.sellOperationTableView.setItems(this.getAllCommandInventory());
        this.sellOperationTableView.refresh();
    }

    public void sortOperationTableView(MouseEvent _event) {

        ObservableList<CommandInventory> historyObservableList = FXCollections.observableArrayList();

        ArrayList<CommandInventory> historySortedByTimeList = HistoryBuilder.sortCommandInventoryByTimePeriod(this.getAllCommandInventoryList(),
                this.startTimeField.getValue() + ":00",
                this.endTimeField.getValue() + ":00");

        ArrayList<CommandInventory> historySortedByDateList = HistoryBuilder.sortCommandInventoryByDatePeriod(this.getAllCommandInventoryList(),
                this.startDateField.getValue() + "",
                this.endDateField.getValue() + "");

        ArrayList<CommandInventory> historySortedByDateTimeList = HistoryBuilder.sortCommandInventoryByDatePeriod(historySortedByTimeList,
                this.startDateField.getValue() + "",
                this.endDateField.getValue() + "");

        if (this.startTimeField.getValue() != null && this.endTimeField.getValue() != null) {
            historyObservableList.addAll(historySortedByTimeList);
        } else if (this.startDateField.getValue() != null && this.endDateField.getValue() != null) {
            historyObservableList.addAll(historySortedByDateList);
        }

        if ((this.startTimeField.getValue() != null && this.endTimeField.getValue() != null)
                && (this.startDateField.getValue() != null && this.endDateField.getValue() != null)) {
            historyObservableList.clear();
            historyObservableList.addAll(historySortedByDateTimeList);
        }

        if ((this.startTimeField.getValue() != null && this.endTimeField.getValue() != null)
                || (this.startDateField.getValue() != null && this.endDateField.getValue() != null)) {
            this.sellOperationTableView.setItems(historyObservableList);
            this.sellOperationTableView.refresh();
        }

    }

    public void goToInOutItemHistoryView(MouseEvent event, int operationType) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/inOutItemHistoryView.fxml"));
        Parent homeViewParent = loader.load();
        this.initDefaultSize();
        Scene homeViewScene = new Scene(homeViewParent, this.getWidth(), this.getHeight());
        InOutItemHistoryViewController inOutItemHistoryViewController
                = (InOutItemHistoryViewController) loader.getController();
        inOutItemHistoryViewController.initializeData(operationType);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(homeViewScene);
        window.setMaximized(true);
        window.show();
    }

    public void goToInItemHistory(MouseEvent event) throws IOException {
        this.goToInOutItemHistoryView(event, 2);
    }

    public void goToOutItemHistory(MouseEvent event) throws IOException {
        this.goToInOutItemHistoryView(event, 3);
    }

    public void goToHistoryPanel(MouseEvent event) throws IOException {
        this.switchTo("view/historyPanelView.fxml", event);
    }

    public void goToHomeView(MouseEvent event) throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/homeView.fxml"));
        Parent homeViewParent = loader.load();
        this.initDefaultSize();
        Scene homeViewScene = new Scene(homeViewParent, this.getWidth(), this.getHeight());
        HomeViewController homeViewController = (HomeViewController) loader.getController();
        homeViewController.initializeUserData(WatchDogApp.getUserConnected());
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(homeViewScene);
        window.setMaximized(true);
        window.show();

    }

}
