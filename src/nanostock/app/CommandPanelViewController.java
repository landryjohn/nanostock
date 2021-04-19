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
import nanostock.app.logic.Switcher;
import nanostock.model.table.Command;
import nanostock.model.table.CommandItem;
import nanostock.model.table.DAO.FactoryDAO;
import nanostock.model.table.Operation;
import nanostock.model.table.generic.WatchDogApp;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class CommandPanelViewController extends Switcher implements Initializable {

    @FXML private TableView<Command> commandListTableView ;

    @FXML private TableColumn< Command , Integer > commandNumCol ;
    @FXML private TableColumn< Command , String > commandTimeCol ;
    @FXML private TableColumn< Command , String > clientNameCol ;
    @FXML private TableColumn< Command , String > deliveryAddressCol ;
    @FXML private TableColumn< Command , String > payStatusCol ;
    @FXML private TableColumn< Command , String > deliveryStatusCol ;
    @FXML private TableColumn< Command , String > commandDateCol ;
    @FXML private TextField searchBar ;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //purge invalid command
        try {
            FactoryDAO.getCommandDAO().deleteInvalidCommand();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        this.commandNumCol.setCellValueFactory(  new PropertyValueFactory<Command, Integer>("commandNum") );
        this.commandTimeCol.setCellValueFactory(  new PropertyValueFactory<Command, String>("commandTime") );
        this.clientNameCol.setCellValueFactory( new PropertyValueFactory<Command, String>("clientName") );
        this.deliveryAddressCol.setCellValueFactory( new PropertyValueFactory<Command, String>("deliveryAddress") );
        this.payStatusCol.setCellValueFactory( new PropertyValueFactory<Command, String>("payStatusShow") );
        this.deliveryStatusCol.setCellValueFactory( new PropertyValueFactory<Command, String>("deliveryStatusShow") );
        this.commandDateCol.setCellValueFactory( new PropertyValueFactory<Command, String>("commandDate"));

        // allow the tableViewCommand to be editable
        this.commandListTableView.setEditable( true );
        this.clientNameCol.setCellFactory( TextFieldTableCell.forTableColumn() );
        this.deliveryAddressCol.setCellFactory( TextFieldTableCell.forTableColumn() );
        this.commandListTableView.setItems( this.getCommand() );



    }

    private ObservableList<Command> getCommand(){

        ObservableList<Command> commandObservableList = FXCollections.observableArrayList() ;
        ArrayList<Command> commandList = FactoryDAO.getCommandDAO().getAll()  ;
        commandObservableList.addAll ( commandList ) ;

//        for ( Command command : commandList ) {
//
//            System.out.println( command.getCommandTime() + " " +
//                command.getCommandDate() + " " + command.getPayStatusShow() + " " +
//                command.getDeleveryStatusShow() + " " + command.getPayStatus()
//                    + " " + command.getDeliveryStatus());
//        }

        return commandObservableList ;
    }

    public void addCommand( MouseEvent event ) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation( getClass().getResource("view/setCommandItemView.fxml") ) ;
        Parent setCommandItemViewParent = loader.load();
        this.initDefaultSize();
        Scene setCommandItemViewScene = new Scene( setCommandItemViewParent, this.getWidth() , this.getHeight() ) ;
        SetCommandItemController setCommandItemController = loader.getController();
        setCommandItemController.initCommandInformation( null , true  );
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene( setCommandItemViewScene );
        window.setMaximized(true);
        window.show();
    }

//    public void setCommand( MouseEvent event ){
//
//        Command selectedCommand = null ;
//        selectedCommand =  this.commandListTableView.getSelectionModel().getSelectedItem() ;
//
//        if( selectedCommand != null ){
//
//
//
//        }
//
//    }

    public void editClientName( TableColumn.CellEditEvent editedCell ){
        Command command = null ;
        command = this.commandListTableView.getSelectionModel().getSelectedItem() ;

        if( command != null ){
            if( ! command.getPayStatus() ){

                command.setClientName( editedCell.getNewValue().toString() );

                if( command.getClientName().length() > 0 ){

                    if( commit() ){
                        FactoryDAO.getCommandDAO().update( command ) ;

                    }

                }else{

                    this.commandListTableView.setItems( getCommand() );
                    this.commandListTableView.refresh();
                    this.alert("Erreur de nom de client" , null ,
                            "Le nom " + editedCell.getNewValue().toString() + "saisi pour le client est incorrect");
                }

            }else{
                this.alert( "Modification  de la commande" , null ,
                        "La commande est déjà payée. Vous ne pouves la modifier");
            }
        }

    }

    public void editDeliveryAddress( TableColumn.CellEditEvent editedCell){
        Command command = null ;
        command = this.commandListTableView.getSelectionModel().getSelectedItem() ;
        if( command != null ){

            if( ! command.getDeliveryStatus() ){

                if( commit() ){
                    FactoryDAO.getCommandDAO().update( command ) ;
                }

            }else{

                this.alert( "Modification  de la commande" , null ,
                        "La commande est déjà livrée. Vous ne pouves la modifier");

            }

        }else{
            this.commandListTableView.setItems( getCommand() );
            this.commandListTableView.refresh();
            this.alert("Erreur de lieu de livraison" , null ,
                    "Le lieu" + editedCell.getNewValue().toString() + "saisi pour est incorrect");
        }
    }

    public boolean commit(){
        Optional<ButtonType> choice =  this.confirm("Validation de l'operation" , null
                , "Voulez vous valider les modification ?") ;
        return choice.get() == ButtonType.OK ;
    }

    public void editCommandItem( MouseEvent event ) throws IOException, SQLException {
        Command selectedCommand = null ;
        selectedCommand =  this.commandListTableView.getSelectionModel().getSelectedItem() ;

        if( selectedCommand != null ){
            System.out.println("valeur " + selectedCommand.getClientName() );
            if( ! selectedCommand.getPayStatus() ){

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation( getClass().getResource("view/setCommandItemView.fxml") ) ;
                Parent setCommandItemViewParent = loader.load();
                this.initDefaultSize();
                Scene setCommandItemViewScene = new Scene( setCommandItemViewParent, this.getWidth() , this.getHeight()) ;
                SetCommandItemController setCommandItemController = (SetCommandItemController)loader.getController();
                setCommandItemController.initCommandInformation( selectedCommand , false  );
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.setScene( setCommandItemViewScene );
                window.setMaximized(true);
                window.show();

            }else{
                this.alert( "Modification  de la commande" , null ,
                        "La commande est déjà payée. Vous ne pouves la modifier");
            }

        }

    }

    public void deleteCommand() throws SQLException {
        Command selectedCommand = null ;
        selectedCommand = this.commandListTableView.getSelectionModel().getSelectedItem() ;
        if( selectedCommand != null ){
            Optional<ButtonType> result = this.confirm("SUPPRESION DE COMMANDE" ,  null ,
                    "Voulez vous vraiment supprimer la commande No " +
                     selectedCommand.getCommandNum() + " du client : " +
                            selectedCommand.getClientName() ) ;
            if( result.isPresent() ){
                if( result.get() == ButtonType.OK ){
                    ArrayList<CommandItem> commandItemList = FactoryDAO.getCommandItemDAO().findByCommand( selectedCommand ) ;
                    for( CommandItem commandItem : commandItemList ){
                        if( selectedCommand.getPayStatus() )
                            FactoryDAO.getCommandItemDAO().deleteItemSold( commandItem );
                        else
                            FactoryDAO.getCommandItemDAO().delete( commandItem );
                    }
                    FactoryDAO.getCommandDAO().delete( selectedCommand.getCommandNum()  ) ;
                    FactoryDAO.getOperationDAO().create(new Operation( WatchDogApp.getUserConnected().getUserId() ,
                            "Supression de la commande " +
                            selectedCommand.getCommandNum() + " du client " + selectedCommand.getClientName() +"/0") ) ;
                    this.commandListTableView.setItems( getCommand() );
                    this.commandListTableView.refresh();
                }
            }
        }

    }

    public void goToSetCommandBillView( MouseEvent event ) throws IOException,SQLException {
        Command selectedCommand = null ;
        selectedCommand = this.commandListTableView.getSelectionModel().getSelectedItem() ;
        if( selectedCommand != null ){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation( getClass().getResource("view/setCommandBillView.fxml") ) ;
            Parent setCommandBillViewParent = loader.load();
            this.initDefaultSize();
            Scene setCommandBillViewScene = new Scene( setCommandBillViewParent ,
                    this.getWidth() , this.getHeight()) ;
            SetCommandBillController setCommandBillController = (SetCommandBillController)loader.getController();
            setCommandBillController.initializeCommand( selectedCommand  );
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene( setCommandBillViewScene );
            window.setMaximized(true);
            window.show();
        }


    }

    // search implementation

    public void searchKeyReleased()
    {
        String buttonBarValue = searchBar.getText().trim();

        this.commandListTableView.setItems( this.updateCommandListTableView(buttonBarValue) );
    }

    private ObservableList<Command> updateCommandListTableView(String stringValue){
        ObservableList<Command> commandObservableList = FXCollections.observableArrayList() ;
        ArrayList<Command> commandList = FactoryDAO.getCommandDAO().getAll(stringValue)  ;
        commandObservableList.addAll( commandList ) ;
        return commandObservableList ;
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


