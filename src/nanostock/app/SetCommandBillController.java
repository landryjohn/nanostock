package nanostock.app;

import com.jfoenix.controls.JFXRadioButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nanostock.app.logic.Switcher;
import nanostock.model.table.Command;
import nanostock.model.table.CommandItem;
import nanostock.model.table.DAO.FactoryDAO;
import nanostock.model.table.Operation;
import nanostock.model.table.generic.HistoryBuilder;
import nanostock.model.table.generic.WatchDogApp;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import net.sf.jasperreports.engine.JRException;

public class SetCommandBillController extends Switcher implements Initializable {

    private Command command = new Command();

    @FXML
    TableView<CommandItem> commandItemTableView;
    
    @FXML
    private TableColumn<CommandItem, String> commandItemRefCol;
    @FXML
    private TableColumn<CommandItem, Integer> commandItemQtCol;
    @FXML
    private TableColumn<CommandItem, Double> commandItemPriceCol;

    @FXML
    private JFXRadioButton payStatus;
    @FXML
    private JFXRadioButton deliveryStatus;

    @FXML
    private Label clientName;
    @FXML
    private Label deliveryAddress;
    @FXML
    private Label totalCommand;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initializeCommand(Command command) throws SQLException {

        this.command = command;
        System.out.println("Valeur : " + command.getCommandNum());

        this.commandItemRefCol.setCellValueFactory(new PropertyValueFactory<CommandItem, String>("itemReference"));
        this.commandItemQtCol.setCellValueFactory(new PropertyValueFactory<CommandItem, Integer>("commandQuantity"));
        this.commandItemPriceCol.setCellValueFactory(new PropertyValueFactory<CommandItem, Double>("discountValue"));

        clientName.setText(this.command.getClientName());
        deliveryAddress.setText(this.command.getDeliveryAddress());
        totalCommand.setText(this.getTotalCommandPrice() + " FCFA ");

        this.payStatus.setSelected(this.command.getPayStatus());
        this.deliveryStatus.setSelected(this.command.getDeliveryStatus());

        try {
            this.commandItemTableView.setItems(getAllCommandItem());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<CommandItem> getAllCommandItem() throws SQLException {
        ObservableList<CommandItem> observableList = FXCollections.observableArrayList();
        ArrayList<CommandItem> l = FactoryDAO.getCommandItemDAO().findByCommand(this.command);
        observableList.addAll(l);
        return observableList;
    }

    public void setCommand(MouseEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/setCommandItemView.fxml"));
        Parent setCommandItemViewParent = loader.load();
        this.initDefaultSize();
        Scene setCommandItemViewScene = new Scene(setCommandItemViewParent, this.getWidth(), this.getHeight());
//        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//        double width = gd.getDisplayMode().getWidth() - 50;
//        double height = gd.getDisplayMode().getHeight() - 80;
        SetCommandItemController setCommandItemController = (SetCommandItemController) loader.getController();
        setCommandItemController.initCommandInformation(this.command, false);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(setCommandItemViewScene);
        window.setMaximized(true);
        window.show();
    }

    public void generateBill(MouseEvent event) throws IOException, SQLException {
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation( getClass().getResource("view/bill.fxml") ) ;
//        Parent billViewParent = loader.load();
//        this.initDefaultSize();
//        Scene billViewScene = new Scene( billViewParent, this.getWidth(), this.getHeight()) ;
//        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//        double width = gd.getDisplayMode().getWidth() ;
//        double height = gd.getDisplayMode().getHeight() - 50;
//        BillController billController = (BillController)loader.getController();
//        billController.initialization( this.command );
//        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//        window.setScene( billViewScene );
//        window.setMaximized(true);
//        window.show();

        try {
            // --- Show Jasper Report on click-----
            new BillController().showReport(this.command );
        } catch (ClassNotFoundException | JRException | SQLException e1) {
            e1.printStackTrace();
        }
    }

    public double getTotalCommandPrice() throws SQLException {
        ObservableList<CommandItem> commandItemList = FXCollections.observableArrayList();
        commandItemList = this.getAllCommandItem();
        double totalCommandPrice = 0;
        for (CommandItem commandItem : commandItemList) {
            totalCommandPrice += commandItem.getCommandQuantity() * commandItem.getDiscountValue();
        }
        return totalCommandPrice;
    }

    public void updateDeliveryStatus(MouseEvent event) {
        this.command.setDeliveryStatus(this.deliveryStatus.isSelected());

        if (this.deliveryStatus.isSelected()) {
            FactoryDAO.getOperationDAO().create(new Operation(WatchDogApp.getUserConnected().getUserId(),
                    "Livraison de la commande " + this.command.getCommandNum()
                    + " du client " + this.command.getClientName() + " Lieu : "
                    + this.command.getDeliveryAddress() + "/=/1"));
        } else {
            FactoryDAO.getOperationDAO().create(new Operation(WatchDogApp.getUserConnected().getUserId(),
                    "Annulation de la livraison de la commande " + this.command.getCommandNum()
                    + " du client " + this.command.getClientName() + "/=/1"));
        }

        this.command.setCommandValidity(true);
        FactoryDAO.getCommandDAO().update(this.command);

    }

    public void updatePayStatus(MouseEvent event) {

        this.command.setPayStatus(payStatus.isSelected());

        if (payStatus.isSelected()) {
            FactoryDAO.getOperationDAO().create(new Operation(WatchDogApp.getUserConnected().getUserId(),
                    "Paiement de la commande " + this.command.getCommandNum()
                    + " du client " + this.command.getClientName() + "/=/1"));
        } else {
            FactoryDAO.getOperationDAO().create(new Operation(WatchDogApp.getUserConnected().getUserId(),
                    "Annulation du paiement de la commande " + this.command.getCommandNum()
                    + " du client " + this.command.getClientName() + "/=/1"));
        }

        this.command.setCommandValidity(true);
        FactoryDAO.getCommandDAO().update(this.command);

    }

    public void goToCommandPanelView(MouseEvent event) throws IOException {

        this.switchTo("view/commandPanelView.fxml", event);

    }
}
