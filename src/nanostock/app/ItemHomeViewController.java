/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.app;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import nanostock.app.logic.Switcher;
import nanostock.model.table.Category;
import nanostock.model.table.Item;
import nanostock.model.table.ItemStore;
import nanostock.model.table.DAO.FactoryDAO ;
import nanostock.model.table.generic.WatchDogApp;

/**
 * FXML Controller class
 *
 * @author landry john
 */
public class ItemHomeViewController extends Switcher implements Initializable {

    
    @FXML
    private TableView<ItemStore> tableViewItemStoreList ;

    @FXML private TableColumn< ItemStore , Integer > itemCodeCol ; 
    @FXML private TableColumn< ItemStore , String > itemReferenceCol ; 
    @FXML private TableColumn< ItemStore , Integer > quantityCol ; 
    @FXML private TableColumn< ItemStore , Double > unitPriceCol ; 
    @FXML private TableColumn< ItemStore , Double > wholesalePriceCol ;
    @FXML private ComboBox<String> categoryChoiceBox ;

    @FXML private TextField searchBar ;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ArrayList<Category> categoryList = FactoryDAO.getCategoryDAO().getAll() ;

        this.categoryChoiceBox.getItems().add("Toutes");
        this.categoryChoiceBox.setValue("Toutes");

        for( Category category : categoryList ){
            this.categoryChoiceBox.getItems().add( category.getCategoryName() ) ;
        }


//        System.out.println(WatchDogApp.getUserConnected().getUserName());

        this.itemCodeCol.setCellValueFactory(new PropertyValueFactory<ItemStore,Integer>("itemCode") );
        this.itemReferenceCol.setCellValueFactory(new PropertyValueFactory<ItemStore,String>("itemReference") );
        this.quantityCol.setCellValueFactory(new PropertyValueFactory<ItemStore,Integer>("stockQuantity") );
        this.unitPriceCol.setCellValueFactory(new PropertyValueFactory<ItemStore,Double>("unitPrice") );
        this.wholesalePriceCol.setCellValueFactory(new PropertyValueFactory<ItemStore,Double>("wholesalePrice") );
        
        this.itemReferenceCol.setSortType( TableColumn.SortType.ASCENDING );
        try {
            this.tableViewItemStoreList.setItems( this.getItemStore() ) ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemHomeViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    //make category name column to be editable
        this.tableViewItemStoreList. setEditable(true) ;
        this.itemReferenceCol.setCellFactory(TextFieldTableCell.forTableColumn());
        this.unitPriceCol.setCellFactory( TextFieldTableCell.forTableColumn( new DoubleStringConverter() ) );
        this.wholesalePriceCol.setCellFactory(
                TextFieldTableCell.forTableColumn( new DoubleStringConverter()));

    //this will allow the table to select multiple rows at once
        this.tableViewItemStoreList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void setItemStoreByCategory (ActionEvent event ){

        System.out.println( this.categoryChoiceBox.getValue() ) ;
        if( !this.categoryChoiceBox.getValue().equals("Toutes") ){
            this.tableViewItemStoreList.setItems(
                    this.getItemStoreByCategory( categoryChoiceBox.getValue() ) );
        }else{
            System.out.println("Hello ok");
            try {
                this.tableViewItemStoreList.setItems( this.getItemStore() );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        this.tableViewItemStoreList.refresh();

    }


    /**
     * This method return an observable list of item object
     * @return 
     */
    public ObservableList<ItemStore> getItemStore() throws SQLException{
        
        ObservableList<ItemStore> itemStoreObservableList = FXCollections.observableArrayList() ;
        
        ArrayList<ItemStore> itemStoreList = new ArrayList<>() ;
        
        itemStoreList = FactoryDAO.getItemStoreDAO().getAllItemStore()  ;
        
        for( int i = 0 ; i < itemStoreList.size() ; i++ ){
            itemStoreObservableList.add( itemStoreList.get(i) );
        }

        return itemStoreObservableList ;
    }

    private ObservableList<ItemStore> getItemStoreByCategory( String category ){
        ObservableList<ItemStore> itemStoreObservableList = FXCollections.observableArrayList() ;

        ArrayList<ItemStore> itemStoreList = new ArrayList<>() ;

        itemStoreList =
                FactoryDAO.getItemStoreDAO().getAllItemStoreByCategory( category );  ;

        for( int i = 0 ; i < itemStoreList.size() ; i++ ){

            itemStoreObservableList.add( itemStoreList.get(i) );

//            System.out.println( "value : " + itemStoreList.get(i).getSockQuantity() );
        }

        return itemStoreObservableList ;
    }

    //UPDATE an item
    public void editRefItemCell(TableColumn.CellEditEvent editedCell ) throws SQLException {
        ItemStore itemStore = tableViewItemStoreList.getSelectionModel().getSelectedItem() ;
        itemStore.setItemReference( editedCell.getNewValue().toString() );
        if( !FactoryDAO.getItemDAO().searchItem( itemStore.getItemReference() ) ){
            FactoryDAO.getItemDAO().update( new Item(
                itemStore.getItemCode(), itemStore.getUnitPrice(), itemStore.getItemDes(),
                itemStore.getEnterPrice(), itemStore.getWholesalePrice(),
                itemStore.getItemReference() , itemStore.getItemImageName() ) ) ;
        }else{

            this.tableViewItemStoreList.setItems( this.getItemStore() );
            tableViewItemStoreList.refresh();
            this.alert("Erreur", null, "Erreur de reference :  "
                    + editedCell.getNewValue().toString() + "\nelle exite déjà" );

        }

    }

    public void searchItem() throws SQLException {
        String searchBarValue = this.searchBar.getText().trim() ;
        ObservableList<ItemStore> itemStoreList  = FXCollections.observableArrayList() ;
        itemStoreList.addAll( FactoryDAO.getItemStoreDAO().getAllItemStore( searchBarValue ) ) ;
        this.tableViewItemStoreList.setItems( itemStoreList );
    }

    public void editUnitPriceItemCell(TableColumn.CellEditEvent edditedCell ) throws SQLException {
        ItemStore itemStore = tableViewItemStoreList.getSelectionModel().getSelectedItem() ;
        System.out.println(  Double.valueOf( edditedCell.getNewValue().toString() ).intValue() );
        double unitPrice = Double.valueOf( edditedCell.getNewValue().toString() ).intValue();
        itemStore.setUnitPrice ( unitPrice );
        if(unitPrice > 0){
            FactoryDAO.getItemDAO().update( new Item(
                    itemStore.getItemCode(), itemStore.getUnitPrice(), itemStore.getItemDes(),
                    itemStore.getEnterPrice(), itemStore.getWholesalePrice(),
                    itemStore.getItemReference() , itemStore.getItemImageName() ) ) ;
        }else{

            this.tableViewItemStoreList.setItems( this.getItemStore() );
            tableViewItemStoreList.refresh();
            this.alert("Erreur", null, "Erreur de prix :  "
                    + edditedCell.getNewValue().toString() + "\nprix de vente négatif" );
        }
    }

    public void editWholesalePriceItemCell(TableColumn.CellEditEvent edditedCell ) throws SQLException {
        ItemStore itemStore = tableViewItemStoreList.getSelectionModel().getSelectedItem() ;
        double wholesalePrice = Double.valueOf( edditedCell.getNewValue().toString() ).intValue();
        itemStore.setWholesalePrice( wholesalePrice);
        if(wholesalePrice > 0){
            FactoryDAO.getItemDAO().update( new Item(
                    itemStore.getItemCode(), itemStore.getUnitPrice(), itemStore.getItemDes(),
                    itemStore.getEnterPrice(), itemStore.getWholesalePrice(),
                    itemStore.getItemReference() , itemStore.getItemImageName() ) ) ;
        }else{

            this.tableViewItemStoreList.setItems( this.getItemStore() );
            tableViewItemStoreList.refresh();
            this.alert("Erreur", null, "Erreur de prix :  "
                    + edditedCell.getNewValue().toString() + "\nprix de gros négatif" );
        }
    }

    public void deleteItem() throws SQLException, Exception {
        ObservableList<ItemStore> selectedItem , allItem ;
        allItem = this.tableViewItemStoreList.getItems() ;

        //permit to return selected item
        selectedItem = this.tableViewItemStoreList.getSelectionModel().getSelectedItems() ;

        if( selectedItem.size() > 0 ){

            Optional<ButtonType> choice =
                    this.confirm( "Confirmation" , null ,
                            "Voulez-vous valider la suppresion ?") ;

            if( choice.get() == ButtonType.OK ){

                for( ItemStore itemStore : selectedItem ){
                    System.out.println( itemStore.getItemCode() ) ;
                    String imageName = FactoryDAO.getItemStoreDAO().findByItemCode( itemStore.getItemCode()).getItemImageName();
                    FactoryDAO.getItemStoreDAO().delete( itemStore) ;
                    allItem.remove( itemStore );
                    // suppresion de l'image

                    Path desFile = Paths.get(System.getProperty("user.dir")+"\\data\\images\\" + imageName);
                    System.out.println(desFile);
                    File imagePath = new File(desFile.toUri().toURL().toString());
                    System.out.println(imagePath);

                    if( imagePath.delete())
                    {
                        System.out.println("bonjour tous le monde");
                    }

                    imagePath.delete();

                }

            }

        }
    }


//    public void showItemDetails( MouseEvent event ){
//        ItemStore selectedItemStore = null ;
//        selectedItemStore = this.tableViewItemStoreList.getSelectionModel().getSelectedItem() ;
//        if( selectedItemStore != null ){
//            this.alertInfo("Détail Article" , null ,
//                    "Détails de l'article : " +
//                    "\n\nNom : " + selectedItemStore.getItemReference() +
//                    "\n\nCategory : " + FactoryDAO.getCategoryDAO().
//                            find( selectedItemStore.getCategoryId() ).getCategoryName() +
//                    "\n\nQuantité en stock : " + selectedItemStore.getSockQuantity() +
//                    "\n\nPrix Unitaire : " + selectedItemStore.getUnitPrice() + " FCFA "+
//                    "\n\nPrix De Gros : " + selectedItemStore.getWholesalePrice() + " FCFA "+
//                    "\n\nDescription : " + selectedItemStore.getItemDes() +
//                    "\n\nQuantité d'Alerte : " + selectedItemStore.getAlertQuantity() );
//        }
//    }


    public void showItemDetails( MouseEvent event )throws Exception {


        Alert  itemAlert = new Alert( Alert.AlertType.INFORMATION );
        itemAlert.setTitle("Détail Article");

        ItemStore selectedItemStore = null ;
        selectedItemStore = this.tableViewItemStoreList.getSelectionModel().getSelectedItem() ;
        if( selectedItemStore != null ){

            itemAlert.setHeaderText("Image : ");

            String  itemImagePath = System.getProperty("user.dir").toString()+"\\data\\images\\" + selectedItemStore.getItemImageName();
            System.out.println(Paths.get(itemImagePath).toUri().toURL().toString());
            ImageView itemImageView = new ImageView( Paths.get(itemImagePath).toUri().toURL().toString() ) ;
            Image itemImageIco = new Image( Paths.get(itemImagePath).toUri().toURL().toString()) ;
            itemAlert.setGraphic(itemImageView);
            itemImageView.setFitHeight( 300 );
            itemImageView.setFitWidth( 300 );


            Stage stage = (Stage)itemAlert.getDialogPane().getScene().getWindow();
            stage.getIcons().add( itemImageIco );

            itemAlert.setContentText(
                            "\n\nNom : " + selectedItemStore.getItemReference()+
                            "\n\nCategorie : " + FactoryDAO.getCategoryDAO().
                            find( selectedItemStore.getCategoryId() ).getCategoryName() +
                            "\n\nQuantité en stock : " + selectedItemStore.getSockQuantity() +
                            "\n\nPrix Unitaire : " + selectedItemStore.getUnitPrice() + " FCFA "+
                            "\n\nPrix De Gros : " + selectedItemStore.getWholesalePrice() + " FCFA "+
                            "\n\nDescription : " + selectedItemStore.getItemDes() +
                            "\n\nQuantité d'Alerte : " + selectedItemStore.getAlertQuantity());

        }

        itemAlert.show();
    }


    public void goToUpdateItem(MouseEvent event) throws Exception
    {
        ItemStore selectedItemStore = null ;
        selectedItemStore = this.tableViewItemStoreList.getSelectionModel().getSelectedItem() ;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation( getClass().getResource("view/itemUpdateView.fxml") ) ;
        Parent itemUpdateViewParent = loader.load();
        Scene homeViewScene = new Scene( itemUpdateViewParent ) ;
        ItemUpdateViewController itemUpdateViewController = (ItemUpdateViewController)loader.getController();
        itemUpdateViewController.initializeData( selectedItemStore );
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene( homeViewScene );
        window.show();
    }

    public void goToItemAddView( MouseEvent event ) throws IOException {
    
        this.switchTo("view/itemAddView.fxml", event);
        
    }

    public void setItemAlertQuantity()
    {

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

    public void goToHomeView( MouseEvent event ) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation( getClass().getResource("view/homeView.fxml") ) ;
        Parent homeViewParent = loader.load();
        Scene homeViewScene = new Scene( homeViewParent ) ;
        HomeViewController homeViewController = (HomeViewController)loader.getController();
        homeViewController.initializeUserData( WatchDogApp.getUserConnected() );
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene( homeViewScene );
        window.show();

    }

    public void setCategory( MouseEvent event ) throws SQLException {
        ItemStore selectedItemStore = null ;
        selectedItemStore = this.tableViewItemStoreList.getSelectionModel().getSelectedItem() ;
        if( selectedItemStore != null ){
            List<String> choiceOption = new ArrayList<>();
            ArrayList<Category> categoryList = FactoryDAO.getCategoryDAO().getAll() ;
            for ( Category category : categoryList ) {
                choiceOption.add( category.getCategoryName() ) ;
            }

            ChoiceDialog<String> dialog = new ChoiceDialog<>(
                    FactoryDAO.getCategoryDAO().find( selectedItemStore.getCategoryId() ).getCategoryName()
                    , choiceOption);
            dialog.setTitle( "Modification de la categorie" );
            dialog.setHeaderText( null );
            dialog.setContentText( "Choisir la nouvelle category" );

            Optional<String> result = dialog.showAndWait();
            if( result.isPresent() ){
                System.out.println( "value : " + result.get());
                Category category = FactoryDAO.getCategoryDAO().findCategoryByName( result.get() ) ;
//                selectedItemStore.setcategoryId(
//                        .getCategoryId() );
                System.out.println( category.getCategoryName() );
                FactoryDAO.getItemStoreDAO().update( selectedItemStore );
                this.tableViewItemStoreList.setItems( this.getItemStore() );
                tableViewItemStoreList.refresh();
            }
        }
    }

    
}
