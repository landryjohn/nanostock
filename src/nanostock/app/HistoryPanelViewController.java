package nanostock.app;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nanostock.app.logic.Switcher;
import nanostock.model.table.DAO.FactoryDAO;
import nanostock.model.table.Operation;
import nanostock.model.table.User;
import nanostock.model.table.generic.HistoryBuilder;
import nanostock.model.table.generic.WatchDogApp;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class HistoryPanelViewController extends Switcher implements Initializable {

    @FXML TableView<HistoryBuilder> userOperationTableView ;
    @FXML TableColumn<HistoryBuilder , String> usernameCol ;
    @FXML TableColumn<HistoryBuilder , String> operationDescriptionCol ;
    @FXML TableColumn<HistoryBuilder , String> operationDateCol ;
    @FXML TableColumn<HistoryBuilder , String> operationTimeCol ;

    @FXML JFXTimePicker startTimeField ;
    @FXML JFXTimePicker endTimeField ;
    @FXML JFXDatePicker startDateField ;
    @FXML JFXDatePicker endDateField ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.startTimeField.setIs24HourView( true );
        this.endTimeField.setIs24HourView( true );
        this.usernameCol.setCellValueFactory( new PropertyValueFactory<HistoryBuilder, String>("username")) ;
        this.operationDescriptionCol.setCellValueFactory( new PropertyValueFactory<HistoryBuilder, String>("operationDescription")) ;
        this.operationDateCol.setCellValueFactory( new PropertyValueFactory<HistoryBuilder, String>("operationDate")) ;
        this.operationTimeCol.setCellValueFactory( new PropertyValueFactory<HistoryBuilder, String>("operationTime")) ;

        this.userOperationTableView.setItems( getAll() ) ;

        this.userOperationTableView.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE ) ;

    }

    private ObservableList<HistoryBuilder> getAll(){
        ObservableList<HistoryBuilder> userOperationObservableList = FXCollections.observableArrayList() ;
        ArrayList<Operation> operationList = FactoryDAO.getOperationDAO().getAll() ;
        HistoryBuilder historyBuilder ;
        for( Operation operation : operationList ){

            historyBuilder = new HistoryBuilder( operation ) ;

            if( historyBuilder.getOperationType() == 0 )
                userOperationObservableList.add( new HistoryBuilder( operation ) ) ;
        }

        return userOperationObservableList ;
    }

    public void reinitializeTable(MouseEvent event ){
        this.userOperationTableView.setItems( this.getAll() );
        this.userOperationTableView.refresh();
    }

    private ArrayList<HistoryBuilder> getAllHistory(){
        ArrayList<HistoryBuilder> userOperationList = new ArrayList<>() ;
        ArrayList<Operation> operationList = FactoryDAO.getOperationDAO().getAll() ;
        HistoryBuilder historyBuilder ;
        for( Operation operation : operationList ){

            historyBuilder = new HistoryBuilder( operation ) ;

            if( historyBuilder.getOperationType() == 0 )
                userOperationList.add( new HistoryBuilder( operation ) ) ;
        }

        return userOperationList ;
    }

    public void deleteOperation( MouseEvent event ){
        ArrayList<HistoryBuilder> selectedUserOperation =
                new ArrayList<>(this.userOperationTableView.getSelectionModel().getSelectedItems());
        if( selectedUserOperation.size() != 0 ){
            String message = selectedUserOperation.size() == 1 ? "l'opération ?" : "les opérations ?" ;
            Optional<ButtonType> choice = this.confirm("Archivage operation" , null ,
                    "Voulez vous vraiment archiver " + message  ) ;
            if( choice.isPresent() ){
                if( choice.get() == ButtonType.OK ){
                    for( HistoryBuilder history : selectedUserOperation ){
                        FactoryDAO.getOperationDAO().delete( history.getOperationId() );
                    }
                    this.reinitializeTable( event );
                }
            }
        }
    }

    public void sortOperationTableView(MouseEvent _event ){

        ObservableList<HistoryBuilder> historyObservableList = FXCollections.observableArrayList() ;

        ArrayList<HistoryBuilder> historySortedByTimeList =  HistoryBuilder.sortByTimePeriod( this.getAllHistory()  ,
                    this.startTimeField.getValue() + ":00" ,
                    this.endTimeField.getValue() + ":00" ) ;

        ArrayList<HistoryBuilder> historySortedByDateList =  HistoryBuilder.sortByDatePeriod( this.getAllHistory()  ,
                    this.startDateField.getValue() + "" ,
                    this.endDateField.getValue()  + "") ;

        ArrayList<HistoryBuilder> historySortedByDateTimeList =  HistoryBuilder.sortByDatePeriod( historySortedByTimeList  ,
                this.startDateField.getValue() + "" ,
                this.endDateField.getValue()  + "") ;


        if( this.startTimeField.getValue() != null && this.endTimeField.getValue() != null ){
            historyObservableList.addAll( historySortedByTimeList ) ;
        }else if( this.startDateField.getValue() != null && this.endDateField.getValue() != null ){
            historyObservableList.addAll( historySortedByDateList ) ;
        }

        if( ( this.startTimeField.getValue() != null && this.endTimeField.getValue() != null ) &&
                ( this.startDateField.getValue() != null && this.endDateField.getValue() != null ) ){
            historyObservableList.clear();
            historyObservableList.addAll( historySortedByDateTimeList ) ;
        }

        if( ( this.startTimeField.getValue() != null && this.endTimeField.getValue() != null ) ||
                ( this.startDateField.getValue() != null && this.endDateField.getValue() != null ) ){
            this.userOperationTableView.setItems( historyObservableList );
            this.userOperationTableView.refresh();
        }


    }

    public void goToInOutItemHistoryView( MouseEvent event ,  int operationType ) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation( getClass().getResource("view/inOutItemHistoryView.fxml") ) ;
        Parent homeViewParent = loader.load();
        this.initDefaultSize();
        Scene homeViewScene = new Scene( homeViewParent, this.getWidth(), this.getHeight() ) ;
        InOutItemHistoryViewController inOutItemHistoryViewController =
                (InOutItemHistoryViewController)loader.getController();
        inOutItemHistoryViewController.initializeData( operationType );
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene( homeViewScene );
        window.setMaximized(true);
        window.show();
    }

    public void goToInItemHistory( MouseEvent event ) throws IOException {
        this.goToInOutItemHistoryView( event , 2);
    }

    public void goToOutItemHistory( MouseEvent event ) throws IOException {
        this.goToInOutItemHistoryView( event , 3);
    }

    public void goToHistoryPanel( MouseEvent event ) throws IOException {
        this.switchTo("view/historyPanelView.fxml" , event );
    }

    public void goToSellHistory(MouseEvent event ) throws IOException {
        this.switchTo("view/sellHistoryView.fxml" , event );
    }

    public void goToHomeView( MouseEvent event ) throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation( getClass().getResource("view/homeView.fxml") ) ;
        Parent homeViewParent = loader.load();
        this.initDefaultSize();
        Scene homeViewScene = new Scene( homeViewParent, this.getWidth() , this.getHeight()) ;
        HomeViewController homeViewController = (HomeViewController)loader.getController();
        homeViewController.initializeUserData( WatchDogApp.getUserConnected() );
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene( homeViewScene );
        window.setMaximized(true);
        window.show();

    }

}
