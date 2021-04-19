package nanostock.model.table.generic;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CommandInventory {

    private SimpleIntegerProperty commandNum ;
    private SimpleStringProperty commandDate ;
    private SimpleStringProperty commandTime ;
    private SimpleStringProperty clientName ;
    private SimpleStringProperty deliveryAddress  ;
    private SimpleDoubleProperty commandTotalCost ;
    private SimpleIntegerProperty commandItemQuantity ;
    private SimpleStringProperty commandSeller ;

    public CommandInventory(){}

    public CommandInventory(int commandNum , String commandDate, String commandTime ,
                            String clientName , String deliveryAddress ,
                            Double commandTotalCost, int commandItemQuantity,
                            String commandSeller ){
        this.commandNum = new SimpleIntegerProperty( commandNum ) ;
        this.commandDate = new SimpleStringProperty( commandDate ) ;
        this.commandTime = new SimpleStringProperty( commandTime ) ;
        this.clientName = new SimpleStringProperty( clientName ) ;
        this.deliveryAddress = new SimpleStringProperty( deliveryAddress ) ;
        this.commandTotalCost = new SimpleDoubleProperty( commandTotalCost ) ;
        this.commandItemQuantity = new SimpleIntegerProperty( commandItemQuantity );
        this.commandSeller = new SimpleStringProperty( commandSeller ) ;

    }

    public int getCommandNum() {
        return commandNum.get();
    }

    public void setCommandNum(int commandNum) {
        this.commandNum.set(commandNum);
    }

    public String getCommandDate() {
        return commandDate.get();
    }

    public void setCommandDate(String commandDate) {
        this.commandDate.set(commandDate);
    }

    public String getCommandTime() {
        return commandTime.get();
    }

    public void setCommandTime(String commandTime) {
        this.commandTime.set(commandTime);
    }

    public String getClientName() {
        return clientName.get();
    }

    public void setClientName(String clientName) {
        this.clientName.set(clientName);
    }

    public String getDeliveryAddress() {
        return deliveryAddress.get();
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress.set(deliveryAddress);
    }

    public double getCommandTotalCost() {
        return commandTotalCost.get();
    }

    public void setCommandTotalCost(double commandTotalCost) {
        this.commandTotalCost.set(commandTotalCost);
    }

    public int getCommandItemQuantity() {
        return commandItemQuantity.get();
    }

    public void setCommandItemQuantity(int commandItemQuantity) {
        this.commandItemQuantity.set(commandItemQuantity);
    }

    public String getCommandSeller() {
        return commandSeller.get();
    }

    public void setCommandSeller(String commandSeller) {
        this.commandSeller.set(commandSeller);
    }
}
