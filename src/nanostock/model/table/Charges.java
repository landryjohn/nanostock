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
public class Charges {

    private SimpleIntegerProperty idCharges;

    private SimpleIntegerProperty idEmployee;

    private SimpleDoubleProperty chargesValue;

    private SimpleStringProperty chargesLib;

    private SimpleStringProperty dateCharges;




    public Charges(int idCharges, int idEmployee, double chargesValue, String chargesLib, String dateCharges){

        this.idCharges = new SimpleIntegerProperty(idCharges);
        this.idEmployee = new SimpleIntegerProperty(idEmployee);
        this.chargesValue = new SimpleDoubleProperty( chargesValue );
        this.chargesLib = new SimpleStringProperty( chargesLib ) ;
        this.dateCharges = new SimpleStringProperty( dateCharges ) ;
    }

    public Charges(){}

    public int getIdCharges() {
        return idCharges.get();
    }

    public void setIdCharges(int idCharges) {
        this.idCharges.set(idCharges);
    }

    public int getIdEmployee() {
        return idEmployee.get();
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee.set(idEmployee);
    }

    public double getChargesValue() {
        return chargesValue.get();
    }

    public void setChargesValue(double chargesValue) {
        this.chargesValue.set(chargesValue);
    }

    public String getChargesLib() {
        return chargesLib.get();
    }

    public void setChargesLib(String chargesLib) {
        this.chargesLib.set(chargesLib);
    }

    public String getDateCharges() {
        return dateCharges.get();
    }

    public void setDateCharges(String dateCharges) {
        this.dateCharges.set(dateCharges);
    }
}
