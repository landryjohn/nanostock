package nanostock.app;

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
import nanostock.model.table.DAO.FactoryDAO;
import nanostock.model.table.generic.Alert;
import nanostock.model.table.generic.WatchDogApp;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AlertPanelViewController extends Switcher implements Initializable {

    @FXML TableView<Alert> alertTableView ;
    @FXML TableColumn<Alert , Integer> alertNumCol ;
    @FXML TableColumn<Alert , String> alertItemRefCol  ;
    @FXML TableColumn<Alert , String> alertMessageCol ;
    @FXML TableColumn<Alert , Integer> alertQuantityCol ;
    @FXML TableColumn<Alert , Integer> stockQuantityCol  ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        alertNumCol.setCellValueFactory( new PropertyValueFactory<Alert, Integer>("alertNum"));
        alertItemRefCol.setCellValueFactory( new PropertyValueFactory<Alert, String>("alertItemRef"));
        alertMessageCol.setCellValueFactory( new PropertyValueFactory<Alert, String>("alertMessage"));
        alertQuantityCol.setCellValueFactory( new PropertyValueFactory<Alert, Integer>("alertQuantity"));
        stockQuantityCol.setCellValueFactory( new PropertyValueFactory<Alert, Integer>("stockQuantity"));

        try {
            this.alertTableView.setItems( getAll() );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<Alert> getAll() throws SQLException {
        ObservableList<Alert> alertObservableList = FXCollections.observableArrayList() ;
        alertObservableList.addAll(FactoryDAO.getItemStoreDAO().buildAlert() ) ;
        return alertObservableList ;
    }

    public void goToHomeView( MouseEvent event ) throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation( getClass().getResource("view/homeView.fxml") ) ;
        Parent homeViewParent = loader.load();
        this.initDefaultSize();
        Scene homeViewScene = new Scene( homeViewParent, this.getWidth() , this.getHeight() ) ;
        HomeViewController homeViewController = (HomeViewController)loader.getController();
        homeViewController.initializeUserData( WatchDogApp.getUserConnected() );
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene( homeViewScene );
        window.setMaximized(true);
        window.show();

    }




}
