/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nanostock.app.logic.Switcher;
import nanostock.model.table.Category;
import nanostock.model.table.DAO.FactoryDAO;
import nanostock.model.table.Item;
import nanostock.model.table.ItemStore;
import nanostock.model.table.Store;
import nanostock.model.table.generic.WatchDogApp;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author landry john
 */
public class ItemUpdateViewController extends Switcher implements Initializable {

    @FXML private JFXTextField itemRefField ;
    @FXML private JFXTextArea itemDesField ;
    @FXML private JFXTextField enterPriceField ;
    @FXML private JFXTextField unitPriceField ;
    @FXML private JFXTextField wholesalePriceField ;
    @FXML private JFXTextField qtField ;
    @FXML private JFXTextField qtAlertField ;
    @FXML private ChoiceBox categoryChoiceBox ;
    @FXML private JFXButton validationButton ;
    @FXML private Label itemImageName ;
    @FXML private ImageView itemImage ;
    private File imageSelected;
    private ItemStore itemStoreToUpdate;
    private String firstItemRefValue = "";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ArrayList<Category> categoryList = FactoryDAO.getCategoryDAO().getAll() ;
        // categoryChoiceBox.getItems().add("") ;
        for( Category category : categoryList ){
            categoryChoiceBox.getItems().add( category.getCategoryName() ) ;
        }


    }

    public void initializeData(ItemStore itemToUpdate) throws Exception
    {
        this.itemStoreToUpdate = itemToUpdate;
        this.itemRefField.setText( itemToUpdate.getItemReference() );
        this.firstItemRefValue = this.itemRefField.getText();
        this.itemDesField.setText( itemToUpdate.getItemDes() );
        this.enterPriceField.setText( (int)itemToUpdate.getEnterPrice()+"");
        this.unitPriceField.setText( (int)itemToUpdate.getUnitPrice()+"");
        this.wholesalePriceField.setText( (int)itemToUpdate.getWholesalePrice()+"");
        this.qtField.setText(itemToUpdate.getSockQuantity()+"");
        this.qtAlertField.setText(itemToUpdate.getAlertQuantity()+"");

        this.itemImageName.setText(itemToUpdate.getItemImageName());

        //System.out.println(this.itemRefField.getText());

        String  itemImagePath = System.getProperty("user.dir")+"\\data\\images\\" + this.itemImageName.getText();
        System.out.println(itemImagePath);
        imageSelected = new File( itemImagePath );
        Image imageContent = new Image( Paths.get(itemImagePath).toUri().toURL().toString() );
        itemImage.setImage( imageContent );

        categoryChoiceBox.setValue(FactoryDAO.getCategoryDAO().find(itemToUpdate.getCategoryId()).getCategoryName());



        System.out.println(itemToUpdate.getCategoryId());
    }


    public void submit( MouseEvent event ) throws SQLException, IOException , Exception {

        boolean checkError = this.itemRefField.getText().equals("") ||
                this.itemDesField.getText().equals("") ||
                this.enterPriceField.getText().equals("") ||
                this.unitPriceField.getText().equals("") ||
                this.wholesalePriceField.getText().equals("") ||
                this.categoryChoiceBox.getValue().toString().equals("") ||
                this.qtField.getText().equals("") ||
                this.qtAlertField.getText().equals("") ||
                this.itemImageName.getText().equals("");

        if( checkError ){

            this.alert("Format invalide" , null ,
                    "Un ou plusieurs champ(s) sont vide !");

        }else{

            DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

            LocalDateTime now = LocalDateTime.now();

            this.itemImageName.setText( date.format( now )+getExtension( imageSelected ) );

            ArrayList<Item> itemStoreList = FactoryDAO.getItemDAO().getAll(  ) ;

            System.out.println(itemStoreList.size());
            // delete old image

            boolean itemFound = false ;
            for( Item item : itemStoreList ){
                if(item.getItemReference().equals(this.itemRefField.getText().toUpperCase()))
                {
                    itemFound = true;
                    break;
                }
            }
            System.out.println( itemFound );


            if( !itemFound || this.firstItemRefValue.equals(this.itemRefField.getText())){

                itemStoreToUpdate.setItemReference( this.itemRefField.getText().toUpperCase() );
                itemStoreToUpdate.setItemDes( this.itemDesField.getText() );
                itemStoreToUpdate.setEnterPrice( Integer.valueOf(this.enterPriceField.getText()).doubleValue()  );
                itemStoreToUpdate.setUnitPrice( Integer.valueOf(this.unitPriceField.getText()).doubleValue() );
                itemStoreToUpdate.setWholesalePrice( Integer.valueOf(this.wholesalePriceField.getText()).doubleValue() );
                itemStoreToUpdate.setStockQuantity( Integer.valueOf(this.qtField.getText()).intValue() );
                itemStoreToUpdate.setAlertQuantity( Integer.valueOf(this.qtAlertField.getText()).intValue() );
                itemStoreToUpdate.setItemImageName( this.itemImageName.getText() );

                FactoryDAO.getItemStoreDAO().update( itemStoreToUpdate );

                Item item = new Item(itemStoreToUpdate.getItemCode(),
                                    itemStoreToUpdate.getUnitPrice(),
                                    itemStoreToUpdate.getItemDes(),
                                    itemStoreToUpdate.getEnterPrice(),
                                    itemStoreToUpdate.getWholesalePrice(),
                                    itemStoreToUpdate.getItemReference(),
                                    itemStoreToUpdate.getItemImageName());
                FactoryDAO.getItemDAO().updateItem( item );

                File srcFile = new File( this.imageSelected.getAbsolutePath() ) ;
                Path desFile = Paths.get(System.getProperty("user.dir").toString()+"\\data\\images\\" + this.itemImageName.getText());

                System.out.println( desFile);


                Files.copy( srcFile.toPath() , desFile  , StandardCopyOption.REPLACE_EXISTING) ;

                System.out.println(srcFile.toPath());

                this.switchTo("view/itemHomeView.fxml" , event );

            }else {
                this.alert("Erreur d'ajout" , null ,
                        "Le nom de l'article " + this.itemRefField.getText() +
                                " existe déjà") ;
            }

        }

    }


    public void importItemImage() throws Exception
    {
        //String lastId = FactoryDAO.getUserDAO().getLast().getUserId();
        JFileChooser imageChoice = new JFileChooser();
        imageChoice.setDialogTitle("Choisir une image pour l'article");

        imageChoice.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {

                if (f.isDirectory()) {
                    return true;
                }

                String extension = getExtension(f);
                if (extension != null) {
                    if (extension.equals(".jpeg") ||
                            extension.equals(".jpg") ||
                            extension.equals(".png")) {
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "Images uniquement";
            }
        });


        int result = imageChoice.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION)
        {
            this.imageSelected = imageChoice.getSelectedFile();
            this.itemImageName.setText(this.imageSelected.getName());
            /*DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println(date.format( now ));*/
            //System.out.println(imageSelected.getName());

            Image imageContent = new Image( this.imageSelected.toURI().toURL().toString() );
            itemImage.setImage( imageContent );

        }
    }
    private String getExtension( File filepath )
    {
        return  filepath.getName().substring( filepath.getName().lastIndexOf(".") );
    }

    /**
     *
     * @param event
     * @throws SQLException
     */


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
        Scene homeViewScene = new Scene( homeViewParent ) ;
        HomeViewController homeViewController = (HomeViewController)loader.getController();
        homeViewController.initializeUserData( WatchDogApp.getUserConnected() );
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene( homeViewScene );
        window.show();
    }

}
