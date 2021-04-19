/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.app;

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

/**
 * FXML Controller class
 *
 * @author landry john
 */
public class ItemAddViewController extends Switcher implements Initializable {

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



    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // nothing yet
        ArrayList<Category> categoryList = FactoryDAO.getCategoryDAO().getAll() ;
        // categoryChoiceBox.getItems().add("") ;
        for( Category category : categoryList ){
            categoryChoiceBox.getItems().add( category.getCategoryName() ) ;
        }
        categoryChoiceBox.setValue("");
    }

    /**
     *
     * @param event
     * @throws SQLException
     */
    public void submit( MouseEvent event ) throws SQLException, IOException , Exception {
        boolean checkError = this.itemRefField.getText().equals("") ||
                        this.itemDesField.getText().equals("") ||
                        this.enterPriceField.getText().equals("") ||
                        this.unitPriceField.getText().equals("") ||
                        this.wholesalePriceField.getText().equals("") ||
                        this.categoryChoiceBox.getValue().toString().equals("") ||
                        this.qtField.getText().equals("") ||
                        this.qtAlertField.getText().equals("");

        if( checkError ){

            this.alert("Format invalide" , null ,
                    "Un ou plusieurs champ(s) sont vide !");

        }else{
            if( !this.itemImageName.getText().equals(""))
            {
                    
                DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

                LocalDateTime now = LocalDateTime.now();

                this.itemImageName.setText( date.format( now )+getExtension( imageSelected ) );
            }
            else
            {
                    
                this.itemImageName.setText("defaultImage.png");
            }

            


            ArrayList<Item> itemStoreList = FactoryDAO.getItemDAO().getAll() ;

            boolean itemFound = false ;

            for( Item item : itemStoreList ){
                if(item.getItemReference().equals(this.itemRefField.getText().toUpperCase()))
                {
                    itemFound = true;
                    break;
                }
            }


            if( !itemFound ){
                
                FactoryDAO.getItemStoreDAO().create(
                        new Item( 0 , Integer.valueOf(this.unitPriceField.getText()).intValue() ,
                                this.itemDesField.getText(),
                                Integer.valueOf(this.enterPriceField.getText()).intValue() ,
                                Integer.valueOf(this.wholesalePriceField.getText()).intValue() ,
                                this.itemRefField.getText().toUpperCase(),this.itemImageName.getText()) ,

                        new Store( 1 , null , null  ) ,
                        new ItemStore( Integer.valueOf(this.qtField.getText()).intValue(), "" ,
                                Integer.valueOf(this.qtAlertField.getText()).intValue()
                        ),
                        FactoryDAO.getCategoryDAO()
                                .findCategoryByName( this.categoryChoiceBox.getValue().toString() )
                ) ;
                
                if(!this.itemImageName.getText().equals("defaultImage.png"))
                {
                    File srcFile = new File( this.imageSelected.getAbsolutePath() ) ;
                    Path desFile = Paths.get(System.getProperty("user.dir").toString()+"\\data\\images\\" + this.itemImageName.getText());

                    //System.out.println( desFile);

                    Files.copy( srcFile.toPath() , desFile  , StandardCopyOption.REPLACE_EXISTING) ;

                    //System.out.println(srcFile.toPath());
                }
            
                this.switchTo("view/itemAddView.fxml" , event );

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

    //this methode help to resize the image with the same size of a label

   /* public ImageIcon resizeImage( String imagePath)
    {
        ImageIcon maImage = new ImageIcon(imagePath);
    }*/
    private String getExtension( File filepath )
    {
        return  filepath.getName().substring( filepath.getName().lastIndexOf(".") );
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
        Scene homeViewScene = new Scene( homeViewParent ) ;
        HomeViewController homeViewController = (HomeViewController)loader.getController();
        homeViewController.initializeUserData( WatchDogApp.getUserConnected() );
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene( homeViewScene );
        window.show();
    }

}
