package nanostock.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import nanostock.app.logic.Switcher;
import nanostock.model.table.Charges;
import nanostock.model.table.DAO.FactoryDAO;
import nanostock.model.table.Employee;
import nanostock.model.table.generic.WatchDogApp;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EmployeePanelViewController extends Switcher implements Initializable {

    @FXML
    private TableView<Employee> employeeListTableView ;
    @FXML private TableColumn<Employee , Integer> idEmployee ;
    @FXML private TableColumn<Employee, String> employeeName ;
    @FXML private TableColumn<Employee, Double> employeeCharges ;
    @FXML private TableColumn<Employee, String> employeeDatePaiement ;
    @FXML private TableColumn<Employee, Integer> employeeSalary ;
    @FXML private TextField searchButton;
    //private Double employeeCharges;

    @Override

    public void initialize(URL location, ResourceBundle resources) {
//        cheekInactivity();

        //this.employeeCharges = getEmployeeChgarges(idEmployee);
        this.idEmployee.setCellValueFactory( new PropertyValueFactory<Employee, Integer>("idEmployee") );
        this.employeeName.setCellValueFactory( new PropertyValueFactory<Employee, String>("employeeName") );
        this.employeeDatePaiement.setCellValueFactory( new PropertyValueFactory<Employee, String>("employeeDatePaiement") );
        this.employeeCharges.setCellValueFactory( new PropertyValueFactory<Employee, Double>("employeeCharges") );
        this.employeeSalary.setCellValueFactory( new PropertyValueFactory<Employee, Integer>("employeeSalary") );

        this.employeeListTableView.setItems( this.getEmployee() );

        //make category name column to be editable
        this.employeeListTableView. setEditable(true) ;
        this.employeeName.setCellFactory(TextFieldTableCell.forTableColumn());
        this.employeeDatePaiement.setCellFactory(TextFieldTableCell.forTableColumn());


        //this will allow the table to select multiple rows at once
        this.employeeListTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    private ObservableList<Employee> getEmployee(){
        ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList() ;
        ArrayList<Employee> employeeList = FactoryDAO.getEmployeeDAO().getAll();
        employeeObservableList.addAll( employeeList ) ;
        return employeeObservableList ;
    }

    public void searchEmployee() {
        String searchBarValue = this.searchButton.getText().trim() ;
        ObservableList<Employee> employeeList  = FXCollections.observableArrayList() ;
        employeeList.addAll( FactoryDAO.getEmployeeDAO().getAll( searchBarValue ) ) ;
        this.employeeListTableView.setItems( employeeList );
    }

    public void addEmployeeCharges()
    {
        Employee employee = employeeListTableView.getSelectionModel().getSelectedItem() ;
        if(employee!=null){

        Optional<String> chargesName = this.textInputField( "Ajout d'une charge" ,
                "DEFINITION DU NOM DE LA CHARGE "  ,
                "Entrée le nom de la charge : " ) ;

        Optional<String> chargesValue = this.textInputField( "Ajout d'une charge" ,
                "DEFINITION DE LA VALEUR DE LA CHARGE "  ,
                "Entrée la valeur de la charge : " ) ;

        Optional<String> employeeDate = this.textInputField( "Ajout d'une charge" ,
                "DEFINIR LA DATE"  ,
                "Definir la date suivant le format jj-mm-aaaa : " ) ;
            while(!dateFormatCheeker( employeeDate.get() ))
            {
                employeeDate = this.textInputField( "Ajout d'une charge" ,
                        "DEFINIR LA DATE"  ,
                        "Definir la date suivant le format jj-mm-aaaa : " ) ;
            }

        if( chargesName.isPresent() && chargesName.isPresent() && employeeDate.isPresent() )
        {
            //ajout d'une charge
            DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            Charges charges = new Charges( 0, employee.getIdEmployee(), Double.valueOf( chargesValue.get() ) , chargesName.get() , employeeDate.get() );
            FactoryDAO.getChargesDAO().create(charges);

            //mise a jour de la valeur des charges de l'utilisateur

            this.updateEmployeeCharges(employee.getIdEmployee(), Double.valueOf( chargesValue.get() ) );

            // mise a jour de la valeur du salaire


        }else{
            this.alert("Erreur d'ajout d'une charge " , null ,
                    "Tous les paramètres n'ont pas été renseigné");
        }

        this.employeeListTableView.setItems( this.getEmployee() ) ;
        }
    }

    public boolean updateEmployeeCharges(int idEmployee, Double chargeValue)
    {
        Employee employee = FactoryDAO.getEmployeeDAO().find(idEmployee);
        employee.setEmployeeCharges(employee.getEmployeeCharges()+chargeValue);
        FactoryDAO.getEmployeeDAO().update( employee);
        return true;
    }

    public void deleteEmployee() {
        Employee employee = this.employeeListTableView.getSelectionModel().getSelectedItem();
        if (employee != null) {

            FactoryDAO.getEmployeeDAO().delete(employee.getIdEmployee());
            FactoryDAO.getChargesDAO().deleteEmployeeCharges(employee.getIdEmployee());
            this.employeeListTableView.setItems( this.getEmployee() ) ;

        }

    }

    public void showItemDetails( ) {

        Employee selectedEmployee = null ;
        selectedEmployee = this.employeeListTableView.getSelectionModel().getSelectedItem();


        if( selectedEmployee != null ){
            Alert itemAlert = new Alert( Alert.AlertType.INFORMATION );
            TextArea textArea = new TextArea();
            textArea.setEditable( false );
            textArea.setWrapText(true);

            itemAlert.setTitle("Détail Charges");
            itemAlert.setHeaderText(" Details des charges de l'employé : "+ selectedEmployee.getEmployeeName());

            ArrayList<Charges> employeeChargesList = FactoryDAO.getChargesDAO().getAllChargesEmployee(selectedEmployee.getIdEmployee());

            int compter = 0;
            String chargesContent = "";
            for(Charges charges : employeeChargesList)
            {
                System.out.println(employeeChargesList.size());
                compter++;
                chargesContent = chargesContent + "Charge numero : "+ compter +
                        "\n   Intitulé de la charge: " + charges.getChargesLib()+
                        "\n   Date : " + charges.getDateCharges() +
                        "\n   Valeur : " + this.currency(charges.getChargesValue())+"\n\n";

            }
            if (chargesContent == ""){
                chargesContent = " Aucune charge";
            }
            textArea.insertText(0,chargesContent);
            itemAlert.getDialogPane().setExpandableContent(textArea);
            itemAlert.show();
        }

    }

    public void addEmployee()
    {

        Optional<String> employeeName = this.textInputField( "Ajout d'un employé" ,
                "CHOIX DU NOM"  ,
                "Entrée le nom de l'employé : " ) ;

        Optional<String> employeeSalary = this.textInputField( "Ajout d'un employé" ,
                "DEFINIR LE SALAIRE"  ,
                "Entrée la valeur du salaire : " ) ;

        Optional<String> employeeDescription = this.textInputField( "Ajout d'un employé" ,
                "DESCRIPTION"  ,
                "Faite une description de votre employé: " ) ;

        Optional<String> employeeDatePaiement = this.textInputField( "Ajout d'un employé" ,
                "DATE DE PAIEMENT"  ,
                "Definir la date de paiement de votre employé jj-mm-aaaa : " ) ;
        while(!dateFormatCheeker( employeeDatePaiement.get() ))
        {
             employeeDatePaiement = this.textInputField( "Ajout d'un employé" ,
                    "DATE DE PAIEMENT"  ,
                    "Definir la date de paiement de votre employé jj-mm-aaaa : " ) ;
        }

        if( employeeName.isPresent() && employeeSalary.isPresent() && employeeDescription.isPresent() &&
                employeeDatePaiement.isPresent())
        {
            Employee employee = new Employee(0,Integer.valueOf(employeeSalary.get()),0,employeeName.get(),employeeDescription.get(),employeeDatePaiement.get());
            FactoryDAO.getEmployeeDAO().create(employee);
        }else{
            this.alert("Error d'ajout d'employé" , null ,
                    "Tous les paramètres n'ont pas été renseigné");
        }


        this.employeeListTableView.setItems( this.getEmployee() ) ;
    }


    public void payEmployeeSalary()
    {
        Employee selectedEmployee = null ;
        selectedEmployee = this.employeeListTableView.getSelectionModel().getSelectedItem();


        if( selectedEmployee != null ){
            Alert itemAlert = new Alert( Alert.AlertType.CONFIRMATION );

            itemAlert.setTitle("Détail");
            itemAlert.setHeaderText(" Details du salaire de l'employé : "+ selectedEmployee.getEmployeeName());
            String chargesContent = "Salaire de base : "+this.currency(selectedEmployee.getEmployeeSalary())+
                                    "\nTotal des charges : "+this.currency(selectedEmployee.getEmployeeCharges())+
                                    "\nNet a payer : "+this.currency(selectedEmployee.getEmployeeSalary()- selectedEmployee.getEmployeeCharges());
            itemAlert.setContentText(chargesContent);
            itemAlert.showAndWait();
            if(itemAlert.getResult().getText().equals("OK"))
            {
                //deleting of all the old charges
                FactoryDAO.getChargesDAO().deleteEmployeeCharges(selectedEmployee.getIdEmployee());
                //modifiying the date of th next date of payement
                selectedEmployee.setEmployeeCharges(0);
                selectedEmployee.setEmployeeDatePaiement( nextMonth(selectedEmployee.getEmployeeDatePaiement()));
                FactoryDAO.getEmployeeDAO().update(selectedEmployee);
                this.employeeListTableView.setItems( this.getEmployee() ) ;

            }
        }

    }

    public void editEmployeeName(TableColumn.CellEditEvent editedCell ) throws SQLException {
        Employee employee = employeeListTableView.getSelectionModel().getSelectedItem();
        employee.setEmployeeName(editedCell.getNewValue().toString());

        FactoryDAO.getEmployeeDAO().update( new Employee(

                employee.getIdEmployee(),employee.getEmployeeSalary(),employee.getEmployeeCharges(),
                employee.getEmployeeName(),employee.getEmployeeDescription(),employee.getEmployeeDatePaiement())
        );

        this.employeeListTableView.setItems(this.getEmployee());
        employeeListTableView.refresh();
    }
    public boolean dateFormatCheeker(String date)
    {

        boolean result = false;
        //return true if the date format is correct
        if(date.split("-").length == 3){
            result = true;
        }
       return result;
    }
    public void editEmployeeDateP(TableColumn.CellEditEvent editedCell ) throws SQLException {
        Employee employee = employeeListTableView.getSelectionModel().getSelectedItem();

        if(dateFormatCheeker(editedCell.getNewValue().toString()))
        {
            employee.setEmployeeDatePaiement(editedCell.getNewValue().toString());

            FactoryDAO.getEmployeeDAO().update( new Employee(

                    employee.getIdEmployee(),employee.getEmployeeSalary(),employee.getEmployeeCharges(),
                    employee.getEmployeeName(),employee.getEmployeeDescription(),employee.getEmployeeDatePaiement())
            );

            this.employeeListTableView.setItems(this.getEmployee());
            employeeListTableView.refresh();
        }
        else
        {

            this.employeeListTableView.setItems(this.getEmployee());
            employeeListTableView.refresh();

            this.alert("Erreur", null, "Erreur de format :  "
                    + editedCell.getNewValue().toString() + "\nFormat invalide" );
        }


    }


    public  String nextMonth(String oldMonth)
    {
        String newMonth = oldMonth.split("-")[0];
        if ( oldMonth.split("-")[1].equals("12"))
        {
            newMonth = newMonth + "-01-"+(Integer.valueOf(oldMonth.split("-")[2])+1) ;
        }
        else
        {
            int month = (Integer.valueOf(oldMonth.split("-")[1])+1);
            if (month<10)
            {
                newMonth = newMonth + "-0" + month  + "-"+oldMonth.split("-")[2];
            }
            else
            {
                newMonth = newMonth + "-" + month  + "-"+oldMonth.split("-")[2];
            }
        }
        return  newMonth;
    }


    public class Threads extends Thread{

        private  int numberOfMinute = 1;

        public Threads(String name)
        {
            super(name);
        }
        // help to convert string format time to integer ex(the time 01:12 to 72)
        public int StrintTimeToInt(String time){
            return  Integer.valueOf(time.split(":")[0])*3600 + Integer.valueOf(time.split(":")[1])*60 +  Integer.valueOf(time.split(":")[2]);
        }

        public int different(String currentDate, String startedDate)
        {
            return (StrintTimeToInt(currentDate) - StrintTimeToInt(startedDate));
        }


        public void run()
        {
            //String strDateFormatter = "hh-mm:ss a";//02-02:27 AM
            String strDateFormater = "hh:mm:ss";
            DateFormat dateFormat = new SimpleDateFormat(strDateFormater);
            String startedDate = dateFormat.format(new Date());
            String currentDate = dateFormat.format(new Date());
            System.out.println(currentDate);


            while( this.different(currentDate,startedDate)<60*numberOfMinute )
            {
                boolean result = false;
                if(1==0)
                {
                    result = true;
                }

                if(result)
                {
                    startedDate = dateFormat.format(new Date());
                }
                currentDate = dateFormat.format(new Date());
               // System.out.println( this.different(currentDate,startedDate) );

            }
            System.out.println("We are just out of the while");
        }
    }

    public void cheekInactivity()
    {

        Threads t1 = new Threads("");
        t1.start();
    }

    public void goToHome( MouseEvent event ) throws IOException {

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
