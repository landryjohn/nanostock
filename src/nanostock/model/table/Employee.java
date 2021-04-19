/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.model.table;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


/**
 *author
 * @author ngoune alban
 */
public class Employee {

    private SimpleIntegerProperty idEmployee;

    private SimpleDoubleProperty employeeSalary;

    private SimpleDoubleProperty employeeCharges;

    private SimpleStringProperty employeeName;

    private SimpleStringProperty employeeDescription;

    private SimpleStringProperty employeeDatePaiement;




    public Employee(int idEmployee, double employeeSalary, double employeeCharges,
                    String employeeName,String employeeDescription ,String employeeDatePaiement){

        this.idEmployee = new SimpleIntegerProperty(idEmployee);
        this.employeeSalary = new SimpleDoubleProperty( employeeSalary );
        this.employeeCharges = new SimpleDoubleProperty( employeeCharges ) ;
        this.employeeName = new SimpleStringProperty( employeeName ) ;
        this.employeeDescription = new SimpleStringProperty( employeeDescription ) ;
        this.employeeDatePaiement = new SimpleStringProperty( employeeDatePaiement ) ;
    }

    public Employee(){}

    public int getIdEmployee() {
        return idEmployee.get();
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee.set(idEmployee);
    }

    public double getEmployeeSalary() {
        return employeeSalary.get();
    }

    public void setEmployeeSalary(double employeeSalary) {
        this.employeeSalary.set(employeeSalary);
    }

    public double getEmployeeCharges() {
        return employeeCharges.get();
    }

    public void setEmployeeCharges(double employeeCharges) {
        this.employeeCharges.set(employeeCharges);
    }

    public String getEmployeeName() {
        return employeeName.get();
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName.set(employeeName);
    }

    public String getEmployeeDescription() {
        return employeeDescription.get();
    }

    public void setEmployeeDescription(String employeeDescription) {
        this.employeeDescription.set(employeeDescription);
    }

    public String getEmployeeDatePaiement() {
        return employeeDatePaiement.get();
    }

    public void setEmployeeDatePaiement(String employeeDatePaiement) {
        this.employeeDatePaiement.set(employeeDatePaiement);
    }

}
