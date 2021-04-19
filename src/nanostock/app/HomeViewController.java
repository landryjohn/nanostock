/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import nanostock.app.logic.Switcher;
import nanostock.model.table.DAO.FactoryDAO;
import nanostock.model.table.Operation;
import nanostock.model.table.User;
import nanostock.model.table.generic.WatchDogApp;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author landry john
 */
public class HomeViewController extends Switcher implements Initializable {

    @FXML private Text userLoggedText ;
    @FXML private Text userTypeText ;
    @FXML private AnchorPane userManageAnch ;
    @FXML private AnchorPane historyAnch ;
    @FXML private Pane userManagePane ;
    @FXML private Pane historyPane ;
    @FXML private Pane employeePane ;
    @FXML private AnchorPane itemManageAnch  ;
    @FXML private Pane itemManagePane  ;
    @FXML private Label alertSize ;
    @FXML private ImageView notificationButton ;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // nothing yet
//        System.out.println( WatchDogApp.getUserConnected().getUserType() );
    }    
    
    public void initializeUserData( User user)  {

        WatchDogApp.setUserConnected( user );
        User userConnected = WatchDogApp.getUserConnected() ;
        this.userLoggedText.setText( userConnected.getUserName() );
        this.userTypeText.setText( userConnected.getUserType().equals("admin") ? "administrateur" : "caissier" ) ;
            System.out.println( userConnected.getUserType() );
        if( userConnected.getUserType().equals("cashier") ){
            this.userManageAnch.setVisible( false );
            this.historyAnch.setVisible( false );
            this.userManagePane.setVisible( false );
            this.historyPane.setVisible( false );
            this.itemManageAnch.setVisible( false );
            this.itemManagePane.setVisible( false );
            this.alertSize.setVisible( false );
            this.notificationButton.setVisible( false );
            this.employeePane.setVisible( false ) ;
        }
        try {
            this.alertSize.setText( FactoryDAO.getItemStoreDAO().getAlertSize() + "" );
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
    /**
     * switch to item managing home view
     * @param event
     * @throws IOException 
     */
    public void goToItemHomeView( MouseEvent event) throws IOException {
        
        this.switchTo("view/itemHomeView.fxml" , event ) ;
        
    }

    public void goToAlertPanelView( MouseEvent event ) throws IOException {

        this.switchTo("view/alertPanelView.fxml" , event ) ;

    }
    
    /**
     * switch to selling panel home view 
     * @param event
     * @throws IOException 
     */
    public void goToCommandPanelView( MouseEvent event ) throws IOException {
    
        this.switchTo("view/commandPanelView.fxml", event);
        
    }
    
    /**
     * witch to user managing home view
     * @param event
     * @throws IOException 
     */
    public void goToUserPanelView( MouseEvent event ) throws IOException {
    
        this.switchTo("view/userPanelView.fxml" , event );
        
    }
    
    /**
     * switch to setting home view
     * @param event
     * @throws IOException 
     */
    public void goToSettingView( MouseEvent event ) throws IOException {
    
        this.switchTo("view/settingHome.fxml", event);
        
    }
    
    public void goToLoginView( MouseEvent event ) throws IOException{

        FactoryDAO.getOperationDAO().create(new Operation( WatchDogApp.getUserConnected().getUserId() ,
                "déconnexion de l'utilisateur/=/0") ) ;
        this.switchTo("view/loginView.fxml", event);
        
    }
    
    /**
     * switch to history view 
     * @param event
     * @throws IOException 
     */
    public void goToHistoryView( MouseEvent event ) throws IOException {
    
        this.switchTo("view/historyPanelView.fxml", event);
        
    }

    public void goToEmployeeView( MouseEvent event ) throws IOException {
        this.switchTo("view/employeePanelView.fxml" , event );
    }
    
    public void exitApp( MouseEvent event ){
        Alert confirmExitAlert = new Alert( Alert.AlertType.CONFIRMATION );
        confirmExitAlert.setTitle("Confirmation de l'opération");
        confirmExitAlert.setHeaderText(null);
        confirmExitAlert.setContentText("Voulez vous vraiment quiter l'application ? ");
        
        Optional<ButtonType> result = confirmExitAlert.showAndWait() ;
        if ( result.get() == ButtonType.OK )
            System.exit(0) ;
        
        
    }
    
    public void lockSession( MouseEvent event ) throws IOException{
       Alert confirmExitAlert = new Alert( Alert.AlertType.CONFIRMATION );
        confirmExitAlert.setTitle("Confirmation de l'opération");
        confirmExitAlert.setHeaderText(null);
        confirmExitAlert.setContentText("Voulez vous vraiment fermer la session ? ");
        
        Optional<ButtonType> result = confirmExitAlert.showAndWait() ;
        if( result.isPresent() ){
            if ( result.get() == ButtonType.OK )
                this.goToLoginView(event);
        }
    }


}
