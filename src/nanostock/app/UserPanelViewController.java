package nanostock.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import nanostock.app.logic.Switcher;
import nanostock.model.table.DAO.FactoryDAO;
import nanostock.model.table.DAO.UserDAO;
import nanostock.model.table.User;
import nanostock.model.table.generic.WatchDogApp;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserPanelViewController extends Switcher implements Initializable {

    @FXML private TableView<User> userListTableView ;
    @FXML private TableColumn<User , Integer> userNumCol ;
    @FXML private TableColumn<User, String> userNameCol ;
    @FXML private TableColumn<User , String> userTypeCol ;
    @FXML private TableColumn<User, Integer> userPhoneCol ;
    @FXML private TableColumn<User, String> userLoginCol ;
    @FXML private TextField searchButton;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.userNumCol.setCellValueFactory( new PropertyValueFactory<User, Integer>("userId") );
        this.userNameCol.setCellValueFactory( new PropertyValueFactory<User, String>("userName") );
        this.userTypeCol.setCellValueFactory( new PropertyValueFactory<User, String>("userType") );
        this.userPhoneCol.setCellValueFactory( new PropertyValueFactory<User, Integer>("userPhone") );
        this.userLoginCol.setCellValueFactory( new PropertyValueFactory<User, String>("userLogin") );

        this.userListTableView.setItems( this.getUser() );

        //make category name column to be editable
        this.userListTableView. setEditable(true) ;
        this.userNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        this.userLoginCol.setCellFactory(TextFieldTableCell.forTableColumn());
        this.userPhoneCol.setCellFactory(TextFieldTableCell.forTableColumn( new IntegerStringConverter()) );


        //this will allow the table to select multiple rows at once
        this.userListTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private ObservableList<User> getUser(){
        ObservableList<User> userObservableList = FXCollections.observableArrayList() ;
        ArrayList<User> userList = FactoryDAO.getUserDAO().getAll()  ;
        userObservableList.addAll( userList ) ;
        return userObservableList ;
    }

    //delete user

    public void deleteUser() throws SQLException {
        ObservableList<User> selectedItem , allItem ;
        allItem = this.userListTableView.getItems() ;

        //permit to return selected item
        selectedItem = this.userListTableView.getSelectionModel().getSelectedItems() ;

        if( selectedItem.size() > 0 ){

            Optional<ButtonType> choice =
                    this.confirm( "Confirmation" , null ,
                            "Voulez-vous valider la suppresion ?") ;

            if( choice.get() == ButtonType.OK ){

                for( User user : selectedItem ){
                    System.out.println( user.getUserId() ) ;
                    FactoryDAO.getUserDAO().delete( user ) ;
                    allItem.remove( user );
                }

            }

        }
    }

    //search button bar implementation

    public void searchKeyReleased()
    {
        String buttonBarValue = searchButton.getText().trim();

        this.userListTableView.setItems( this.updateUserView(buttonBarValue) );
    }

    private ObservableList<User> updateUserView(String stringValue){
        ObservableList<User> userObservableList = FXCollections.observableArrayList() ;
        ArrayList<User> userList = FactoryDAO.getUserDAO().getAll(stringValue)  ;
        userObservableList.addAll( userList ) ;
        return userObservableList ;
    }



    //Add button implementation

    public void addUser() throws IOException {

           /*User userValue = new User( 1, "Ngoune", "123", "alban", "admin",256 );
            userValue.setUserPhone(15);
            System.out.println(userValue.getUSerPhone());*/

            Optional<String> userName = this.textInputField( "Ajout d'un utilisateur" ,
                        "CHOIX DU NOM"  ,
                        "Entrée le nom de l'utilisateur : " ) ;
        Optional<String> userLogin = this.textInputField( "Ajout d'un utilisateur" ,
                "CHOIX DU LOGIN"  ,
                "Entrée le login de l'utilisateur : " ) ;
        System.out.println(existLogin( userLogin.get() ));
        while ( existLogin( userLogin.get() ) == true )
        {
            userLogin = this.textInputField( "Ajout d'un utilisateur" ,
                    "CHOIX DU LOGIN : le login entreé existe déjà"  ,
                    "Entrée le login de l'utilisateur : " ) ;
        }

        Optional<String> userPassword = this.textInputField( "Ajout d'un utilisateur" ,
                "CHOIX DU MOT DE PASSE "  ,
                "Entrée le mot de passe de l'utilisateur : " ) ;
        Optional<String> userPhone = this.textInputField( "Ajout d'un utilisateur" ,
                "CHOIX DU NUMERO DE TELEPHONE"  ,
                "Entrée le numero de l'utilisateur : " ) ;

        Optional<String> result = this.choiceDialog( "Ajout d'un utilisateur" ,
                "CHOIX DU TYPE "  ,
                "Choisir le type d'utilisateur :","Caissier","Administrateur") ;
        String userType = "" ;
        if( result.isPresent() ){
            userType = result.get() == "Caissier" ? "cashier" : "admin" ;
        }

            if( userName.isPresent() && userLogin.isPresent() && userPassword.isPresent() &&
                    userPhone.isPresent() && result.isPresent() )
            {
                User userValue = new User( 0, userName.get(), userPassword.get(), userLogin.get(), userType ,Integer.valueOf( userPhone.get()) );
                FactoryDAO.getUserDAO().create( userValue );
            }else{
                this.alert("Error d'ajout d'utilisateur" , null ,
                        "Tous les paramètres n'ont pas été renseigné");
            }

            this.userListTableView.setItems( this.getUser() ) ;
            this.userListTableView.refresh();

    }



    private boolean existLogin(String userLogin)
    {
        return FactoryDAO.getUserDAO().existLogin(userLogin);
    }



    // update button implementation

    public void editUserPhoneNumberCell(TableColumn.CellEditEvent edditedCell ) throws SQLException {
        User user = userListTableView.getSelectionModel().getSelectedItem() ;
        int phoneNumber = Integer.valueOf( edditedCell.getNewValue().toString() ).intValue();
        user.setUserPhone( phoneNumber);
            FactoryDAO.getUserDAO().update( new User(
                    user.getUserId(), user.getUserName(), user.getUserPassword(),user.getUserLogin(),
                    user.getUserType(), user.getUserPhone()) ) ;
            this.userListTableView.setItems( this.getUser() );
            userListTableView.refresh();
    }
    public void editUserNameCell(TableColumn.CellEditEvent editedCell ) throws SQLException {
        User user = userListTableView.getSelectionModel().getSelectedItem() ;
        user.setUserName( editedCell.getNewValue().toString() );
            FactoryDAO.getUserDAO().update( new User(
                    user.getUserId(), user.getUserName(), user.getUserPassword(),user.getUserLogin(),
                    user.getUserType(), user.getUserPhone()) ) ;
            this.userListTableView.setItems( this.getUser() );
            userListTableView.refresh();

    }
    public void editUserLoginCell(TableColumn.CellEditEvent editedCell ) throws SQLException {
        User user = userListTableView.getSelectionModel().getSelectedItem() ;
        user.setUserLogin( editedCell.getNewValue().toString() );
        if( !FactoryDAO.getUserDAO().existLogin( user.getUserLogin() ) ){
            FactoryDAO.getUserDAO().update( new User(
                    user.getUserId(), user.getUserName(), user.getUserPassword(),user.getUserLogin(),
                    user.getUserType(), user.getUserPhone()) ) ;
        }else{

            this.userListTableView.setItems( this.getUser() );
            userListTableView.refresh();
            this.alert("Erreur", null, "Erreur de login :  "
                    + editedCell.getNewValue().toString() + "\nil exite déjà" );

        }

    }

    public void goToCommandPanelView( MouseEvent event ) throws IOException, SQLException {

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
