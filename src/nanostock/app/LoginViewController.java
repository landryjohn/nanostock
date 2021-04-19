/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import nanostock.app.logic.Switcher;
import nanostock.model.table.DAO.FactoryDAO;
import nanostock.model.table.Operation;
import nanostock.model.table.User;

/**
 *
 * @author landry john
 */
public class LoginViewController implements Initializable {
    
    @FXML
    private JFXTextField userLogin;
    
    @FXML
    private JFXPasswordField userPassword;
    
    @FXML
    private JFXButton loginButton;
    
    @FXML
    private Label userExistLabel;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // nothing yet
    }
    
    @FXML
    public void login( ActionEvent event ) throws IOException, SQLException {
        boolean userExist = FactoryDAO.getUserDAO().isUserExist( this.userLogin.getText(), this.userPassword.getText()) ;
        System.out.println( userExist );
        if( userExist ){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation( getClass().getResource("view/homeView.fxml") ) ;
            User user = FactoryDAO.getUserDAO().searchUser( this.userLogin.getText() ,
                    this.userPassword.getText() ) ;
            Parent homeViewParent = loader.load();
            Scene homeViewScene = new Scene( homeViewParent ) ;
            HomeViewController homeViewController = (HomeViewController)loader.getController();
            FactoryDAO.getOperationDAO().create( new Operation( user.getUserId() ,
                    "Connexion de l'utilisateur/=/0")) ;
            homeViewController.initializeUserData( user );
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow(); 
            window.setScene( homeViewScene );
            window.setMaximized(true);
            window.show();
            
        }else{
                
            Alert loginAlert = new Alert( AlertType.ERROR );
            loginAlert.setTitle("Erreur de connexion");
            loginAlert.setHeaderText(null);
            loginAlert.setContentText("Le login ou le mot de passe est incorrect !");
            loginAlert.showAndWait();
            
        }
        
    }
    
    
}
