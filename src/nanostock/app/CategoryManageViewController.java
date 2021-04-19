/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import nanostock.app.logic.Switcher;
import nanostock.model.table.Category;
import nanostock.model.table.DAO.FactoryDAO;
import nanostock.model.table.generic.WatchDogApp;

/**
 * FXML Controller class
 *
 * @author landry john
 */
public class CategoryManageViewController extends Switcher implements Initializable {

    @FXML JFXTextField categoryNameField ;
    @FXML JFXTextArea categoryDesField ;
    @FXML JFXTextField alertValueField ;
    @FXML JFXButton validationButton ;
    @FXML TableView<Category> tableViewCategoryList ;

    @FXML TableColumn<Category, Integer> categoryIdCol ;
    @FXML TableColumn<Category, String> categoryNameCol ;
    @FXML TableColumn<Category, String> categoryDesCol ;
    @FXML TableColumn<Category, Integer> categoryAlertValueCol ;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.categoryIdCol.setCellValueFactory( new PropertyValueFactory<Category, Integer>("categoryId"));
        this.categoryNameCol.setCellValueFactory( new PropertyValueFactory<Category, String>("categoryName"));
        this.categoryDesCol.setCellValueFactory( new PropertyValueFactory<Category, String>("categoryDes"));
        this.categoryAlertValueCol.setCellValueFactory( new PropertyValueFactory<Category, Integer>("alertValue"));
        this.tableViewCategoryList.setItems( this.getCategoryObservableList() );

        //make category name column to be editable
        this.tableViewCategoryList.setEditable(true) ;
        this.categoryNameCol.setCellFactory(TextFieldTableCell.forTableColumn() );
        this.categoryDesCol.setCellFactory(TextFieldTableCell.forTableColumn() );
        this.categoryAlertValueCol.setCellFactory(
                TextFieldTableCell.forTableColumn( new IntegerStringConverter()));

        //this will allow the table to select multiple rows at once
        this.tableViewCategoryList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * This method delete selected category from the tableView
     */
    public void deleteCategory(){

        ObservableList<Category> selectedCategory , allCategory ;
        allCategory = this.tableViewCategoryList.getItems() ;

        //permit to return selected category
        selectedCategory = this.tableViewCategoryList.getSelectionModel().getSelectedItems() ;

        if( selectedCategory.size() > 0 ){

            Optional<ButtonType> choice =
                    this.confirm( "Confirmation" , null ,
                            "Voulez-vous valider la suppresion ?") ;

            if( choice.get() == ButtonType.OK ){

                for( Category category : selectedCategory ){
                    System.out.println( category.getCategoryId() ) ;
                    FactoryDAO.getCategoryDAO().delete( category.getCategoryId() ) ;
                    allCategory.remove( category );
                }

            }

        }

    }

    /**
     * This method is used to add an category in the database after button "add" pressed
     * @param e
     * @throws SQLException
     */
    @FXML public void submit( ActionEvent e) throws SQLException {

        String categoryName = this.categoryNameField.getText() ;
        String categoryDes = this.categoryDesField.getText() ;
        int alertValue = Integer.valueOf(this.alertValueField.getText()).intValue() ;

        if( !FactoryDAO.getCategoryDAO().searchCategory( categoryName ) ){

            boolean result = FactoryDAO.getCategoryDAO().create( new Category(0 , categoryName,
                    categoryDes, alertValue) );
            this.categoryNameField.setText("") ;
            this.categoryDesField.setText("") ;
            this.alertValueField.setText("") ;
            this.tableViewCategoryList.setItems( this.getCategoryObservableList() );
            tableViewCategoryList.refresh();

        }else{

            this.alert("Erreur", null, "impossible d'ajouter la category :  "
                    + categoryName + "\nElle exite déjà" );

        }

    }

    /**
     * return an observableList that content a list of item category
     * @return
     */
    public ObservableList<Category> getCategoryObservableList(){
        ArrayList<Category> categoryList = new ArrayList<>() ;
        ObservableList<Category> categoryObservableList = FXCollections.observableArrayList();
        categoryList = FactoryDAO.getCategoryDAO().getAll() ;
        categoryObservableList.addAll( categoryList ) ;
        return categoryObservableList ;
    }

    public void onclickDetailsButon(ActionEvent event  ) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation( getClass().getResource("view/categoryView.fxml") ) ;

        Parent parent = loader.load();
        Scene categoryViewScene = new Scene( parent ) ;
        CategoryViewController categoryViewController = (CategoryViewController)loader.getController();
        // TODO
//        CategoryViewController.initData("Landry");
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene( categoryViewScene );
        window.setMaximized(true);
        window.show();
    }

    public void setCategoryName(TableColumn.CellEditEvent edditedCell ) throws SQLException {
        Category category = tableViewCategoryList.getSelectionModel().getSelectedItem() ;
        System.out.println("categoryId : " + category.getCategoryId() );
        if( !FactoryDAO.getCategoryDAO().searchCategory( edditedCell.getNewValue().toString() ) ){
            category.setCategoryName( edditedCell.getNewValue().toString() );
            FactoryDAO.getCategoryDAO().update(category) ;
        }else{

            this.tableViewCategoryList.setItems( this.getCategoryObservableList() );
            tableViewCategoryList.refresh();
            this.alert("Erreur", null, "impossible d'ajouter la category :  "
                    + edditedCell.getNewValue().toString() + "\nElle exite déjà" );



        }

    }

    public void setCategoryDes(TableColumn.CellEditEvent edditedCell )  {
        Category category = tableViewCategoryList.getSelectionModel().getSelectedItem() ;
            category.setCategoryDes ( edditedCell.getNewValue().toString() );
            FactoryDAO.getCategoryDAO().update(category) ;

            this.tableViewCategoryList.setItems( this.getCategoryObservableList() );
            tableViewCategoryList.refresh();
    }

    public void setCategoryAlertValue(TableColumn.CellEditEvent edditedCell ) {
        Category category = tableViewCategoryList.getSelectionModel().getSelectedItem() ;
        category.setAlertValue ( Integer.valueOf( edditedCell.getNewValue().toString() ) );
        FactoryDAO.getCategoryDAO().update(category) ;

        this.tableViewCategoryList.setItems( this.getCategoryObservableList() );
        tableViewCategoryList.refresh();
    }

    public void showCategoryDetails( MouseEvent event ){
        Category selectedCategory = null ;
        selectedCategory = this.tableViewCategoryList.getSelectionModel().getSelectedItem() ;
        if( selectedCategory != null ){
            this.alertInfo( "Details De Catégorie" , null ,
                    "Details de la catégorie :" +
                    "\n\nNom : " + selectedCategory.getCategoryName() +
                    "\n\nDescription : " + selectedCategory.getAlertValue() +
                    "\n\nValeur d'alerte : " + selectedCategory.getAlertValue() );
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

    public void goToHomeView( MouseEvent event ) throws IOException {

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
