package nanostock.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nanostock.app.logic.Switcher;
import nanostock.model.table.DAO.FactoryDAO;
import nanostock.model.table.ItemStore;
import nanostock.model.table.Operation;
import nanostock.model.table.generic.CommandInventory;
import nanostock.model.table.generic.HistoryBuilder;
import nanostock.model.table.generic.WatchDogApp;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InOutStockViewController extends Switcher implements Initializable  {

    @FXML private TableView<ItemStore> tableViewItemStoreList ;
    @FXML private TableColumn< ItemStore , Integer > itemCodeCol ;
    @FXML private TableColumn< ItemStore , String > itemReferenceCol ;
    @FXML private TableColumn< ItemStore , Integer > quantityCol ;
    @FXML private TableColumn< ItemStore , Double > unitPriceCol ;
    @FXML private TableColumn< ItemStore , Double > wholesalePriceCol ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.itemCodeCol.setCellValueFactory(new PropertyValueFactory<ItemStore,Integer>("itemCode") );
        this.itemReferenceCol.setCellValueFactory(new PropertyValueFactory<ItemStore,String>("itemReference") );
        this.quantityCol.setCellValueFactory(new PropertyValueFactory<ItemStore,Integer>("stockQuantity") );
        this.unitPriceCol.setCellValueFactory(new PropertyValueFactory<ItemStore,Double>("unitPrice") );
        this.wholesalePriceCol.setCellValueFactory(new PropertyValueFactory<ItemStore,Double>("wholesalePrice") );

        //this will allow the table to select multiple rows at once
        this.tableViewItemStoreList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        try {
            this.tableViewItemStoreList.setItems( this.getItemStore() ) ;
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method return an observable list of item object
     * @return
     */
    public ObservableList<ItemStore> getItemStore() throws SQLException{

        ObservableList<ItemStore> itemStoreObservableList = FXCollections.observableArrayList() ;

        ArrayList<ItemStore> itemStoreList = new ArrayList<>() ;

        itemStoreList = FactoryDAO.getItemStoreDAO().getAllItemStore() ;

        //            System.out.println( "value : " + itemStoreList.get(i).getSockQuantity() );
        itemStoreObservableList.addAll(itemStoreList);

        return itemStoreObservableList ;


    }

    public void addItem( MouseEvent event ) throws SQLException {

        ObservableList<ItemStore> selectedItemStore  ;

        selectedItemStore = this.tableViewItemStoreList.getSelectionModel().getSelectedItems() ;

        if( selectedItemStore.size() > 0 ){

            for( ItemStore itemStore : selectedItemStore ){

                Optional<String> result = this.textInputField( "Entrée en stock" ,
                    "Augmentation de la quantité de : " + itemStore.getItemReference() ,
                    "Saisir la quantité à ajouter : " ) ;

                if( result.isPresent() ){
                    if( Integer.valueOf(result.get()) > 0 ){
                        itemStore.setStockQuantity( itemStore.getSockQuantity() + Integer.valueOf(result.get()) );
                        FactoryDAO.getItemStoreDAO().update( itemStore );

                        FactoryDAO.getOperationDAO().create( new Operation( WatchDogApp.getUserConnected().getUserId() ,
                                HistoryBuilder.buildInOutHistoryData("Entrée en stock de l'artcile " ,
                                        2 , itemStore.getItemCode() , Integer.valueOf( result.get()) ) ) ) ;

                    }else{
                        this.alert( "Erreur de quantité" ,
                                null ,
                                "Vous devez entrer une quantité positive");

                    }
                }

            }

            this.tableViewItemStoreList.setItems( this.getItemStore() ) ;
            this.tableViewItemStoreList.refresh();

        }


    }


    public void removeItem( MouseEvent event ) throws SQLException {

        ObservableList<ItemStore> selectedItemStore  ;

        selectedItemStore = this.tableViewItemStoreList.getSelectionModel().getSelectedItems() ;

        if( selectedItemStore.size() > 0 ){

            for( ItemStore itemStore : selectedItemStore ){

                Optional<String> result = this.textInputField( "Sortie en stock" ,
                        "Réduire la quantité de : " + itemStore.getItemReference() ,
                        "Saisir la quantité à reduire : " ) ;

                if( result.isPresent() ){
                    if( Integer.valueOf(result.get()) > 0 ){
                        if( Integer.valueOf(result.get()) <= itemStore.getSockQuantity() ){
                            itemStore.setStockQuantity( itemStore.getSockQuantity() - Integer.valueOf(result.get()) );
                            FactoryDAO.getItemStoreDAO().update( itemStore );

                            FactoryDAO.getOperationDAO().create( new Operation( WatchDogApp.getUserConnected().getUserId() ,
                                    HistoryBuilder.buildInOutHistoryData("Sortie en stock de l'artcile " ,
                                             3 , itemStore.getItemCode() , Integer.valueOf( result.get()) ) ) ) ;
                        }
                        else {
                            this.alert( "Erreur de quantité" ,
                                    null ,
                                    "La quantité entrée est superieure à la quantite total");

                        }

                    }else{
                        this.alert( "Erreur de quantité" ,
                                null ,
                                "Vous devez entrer une quantité positive");

                    }
                }

            }

            this.tableViewItemStoreList.setItems( this.getItemStore() ) ;
            this.tableViewItemStoreList.refresh();

        }


    }


    public void goToItemAddView( MouseEvent event ) throws IOException {

        this.switchTo("view/itemAddView.fxml", event);

    }

    public void goToItemHomeView( MouseEvent event ) throws IOException {

        this.switchTo("view/itemHomeView.fxml", event);

    }

    public void goToCategoryManageView( MouseEvent event ) throws IOException {

        this.switchTo("view/categoryManageView.fxml", event);

    }

    public void goToInOutStockView( MouseEvent event ) throws IOException {

        this.switchTo("view/inOutStockView.fxml", event);

    }

    public void goToHomeView( MouseEvent event ) throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation( getClass().getResource("view/homeView.fxml") ) ;
        Parent homeViewParent = loader.load();
        this.initDefaultSize();
        Scene homeViewScene = new Scene( homeViewParent, this.getWidth(), this.getHeight()) ;
        HomeViewController homeViewController = (HomeViewController)loader.getController();
        homeViewController.initializeUserData( WatchDogApp.getUserConnected() );
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene( homeViewScene );
        window.setMaximized(true);
        window.show();

    }

}
