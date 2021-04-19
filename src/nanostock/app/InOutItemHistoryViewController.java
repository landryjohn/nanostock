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
import nanostock.model.table.generic.HistoryBuilder;
import nanostock.model.table.generic.WatchDogApp;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class InOutItemHistoryViewController extends Switcher implements Initializable {

    private int operationType ;

    @FXML TableView<HistoryBuilder> inOutItemOperationTableView ;
    @FXML TableColumn<HistoryBuilder , String> usernameCol ;
    @FXML TableColumn<HistoryBuilder , String> itemReferenceCol ;
    @FXML TableColumn<HistoryBuilder , Integer> quantityCol ;
    @FXML TableColumn<HistoryBuilder , String> operationDateCol ;
    @FXML TableColumn<HistoryBuilder , String> operationTimeCol ;

    @FXML JFXTimePicker startTimeField ;
    @FXML JFXTimePicker endTimeField ;
    @FXML JFXDatePicker startDateField ;
    @FXML JFXDatePicker endDateField ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeData( 3 );
    }

    public void initializeData( int operationType ){

        this.startTimeField.setIs24HourView( true );
        this.endTimeField.setIs24HourView( true );
        this.usernameCol.setCellValueFactory( new PropertyValueFactory<HistoryBuilder , String>("username"));
        this.itemReferenceCol.setCellValueFactory( new PropertyValueFactory<HistoryBuilder , String>("itemReference"));
        this.quantityCol.setCellValueFactory( new PropertyValueFactory<HistoryBuilder , Integer>("quantity"));
        this.operationDateCol.setCellValueFactory( new PropertyValueFactory<HistoryBuilder , String>("operationDate"));
        this.operationTimeCol.setCellValueFactory( new PropertyValueFactory<HistoryBuilder , String>("operationTime"));

        this.operationType = operationType ;

        this.usernameCol.setCellValueFactory( new PropertyValueFactory<HistoryBuilder , String>("username"));
        this.itemReferenceCol.setCellValueFactory( new PropertyValueFactory<HistoryBuilder , String>("itemReference"));
        this.quantityCol.setCellValueFactory( new PropertyValueFactory<HistoryBuilder , Integer>("quantity"));
        this.operationDateCol.setCellValueFactory( new PropertyValueFactory<HistoryBuilder , String>("operationDate"));
        this.operationTimeCol.setCellValueFactory( new PropertyValueFactory<HistoryBuilder , String>("operationTime"));

        this.inOutItemOperationTableView.setItems( this.getAll() );

        this.inOutItemOperationTableView.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE ) ;
    }

    public void reinitializeTable(MouseEvent event ){
        this.inOutItemOperationTableView.setItems( this.getAll() );
        this.inOutItemOperationTableView.refresh();
    }

    private ObservableList<HistoryBuilder> getAll(){
        ObservableList<HistoryBuilder> userOperationObservableList = FXCollections.observableArrayList() ;
        ArrayList<Operation> operationList = FactoryDAO.getOperationDAO().getAll() ;
        HistoryBuilder historyBuilder ;
        for( Operation operation : operationList ){

            historyBuilder = new HistoryBuilder( operation ) ;

            if( historyBuilder.getOperationType() == this.operationType )
                userOperationObservableList.add( new HistoryBuilder( operation ) ) ;
        }

        return userOperationObservableList ;
    }

    private ArrayList<HistoryBuilder> getAllHistory(){

        ArrayList<HistoryBuilder> itemOperationList = new ArrayList<>() ;
        ArrayList<Operation> operationList = FactoryDAO.getOperationDAO().getAll() ;
        HistoryBuilder historyBuilder ;
        for( Operation operation : operationList ){

            historyBuilder = new HistoryBuilder( operation ) ;

            if( historyBuilder.getOperationType() == this.operationType )
                itemOperationList.add( new HistoryBuilder( operation ) ) ;
        }

        return itemOperationList ;

    }

    public void archiveOperation( MouseEvent event ){
        ArrayList<HistoryBuilder> selectedIOOperation =
                new ArrayList<>(this.inOutItemOperationTableView.getSelectionModel().getSelectedItems());
        if( selectedIOOperation.size() != 0 ){
            String message = selectedIOOperation.size() == 1 ? "l'opération ?" : "les opérations ?" ;
            Optional<ButtonType> choice = this.confirm("Archivage operation" , null ,
                    "Voulez vous vraiment archiver " + message  ) ;
            if( choice.isPresent() ){
                if( choice.get() == ButtonType.OK ){
                    for( HistoryBuilder history : selectedIOOperation ){
                        FactoryDAO.getOperationDAO().archive( history.getOperationId() );
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
            this.inOutItemOperationTableView.setItems( historyObservableList );
            this.inOutItemOperationTableView.refresh();
        }


    }

    public void goToInOutItemHistoryView( MouseEvent event ,  int operationType ) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation( getClass().getResource("view/inOutItemHistoryView.fxml") ) ;
        Parent homeViewParent = loader.load();
        this.initDefaultSize();
        Scene homeViewScene = new Scene( homeViewParent, this.getWidth(), this.getHeight()) ;
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
        Scene homeViewScene = new Scene( homeViewParent, this.getWidth(), this.getHeight()) ;
        HomeViewController homeViewController = (HomeViewController)loader.getController();
        homeViewController.initializeUserData( WatchDogApp.getUserConnected() );
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene( homeViewScene );
        window.setMaximized(true);
        window.show();

    }

}
