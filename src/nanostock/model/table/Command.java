/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.model.table;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import nanostock.model.table.generic.StatusCollection;

/**
 *
 * @author landry john
 */
public class Command {
    
    private SimpleIntegerProperty commandNum ;
    
    private SimpleStringProperty commandTimestamp;
    
    private SimpleStringProperty clientName;
    
    private SimpleBooleanProperty payStatus;

    private SimpleStringProperty payStatusShow ;
    
    private SimpleBooleanProperty deliveryStatus;

    private SimpleStringProperty deliveryStatusShow ;
    
    private SimpleStringProperty deliveryAddress;

    private SimpleStringProperty commandDate ;

    private SimpleStringProperty commandTime ;

    private SimpleBooleanProperty commandValidity ;

    public Command(){}

    public Command( int commandNum ){
        this.commandNum  = new SimpleIntegerProperty( commandNum ) ;
    }
    
    public Command( int commandNum , String commandTimestamp, String clientName,
                    boolean payStatus, boolean deliveryStatus , String deliveryAddress ){
        this.commandNum = new SimpleIntegerProperty( commandNum );
        this.commandTimestamp = new SimpleStringProperty( commandTimestamp ) ;
        this.clientName =  new SimpleStringProperty( clientName );
        this.payStatus = new SimpleBooleanProperty( payStatus );
        this.deliveryStatus = new SimpleBooleanProperty( deliveryStatus );
        this.deliveryAddress = new SimpleStringProperty( deliveryAddress ) ;
        this.setStatus();
        this.commandDate = new SimpleStringProperty( this.getDate( this.commandTimestamp.get() ) ) ;
        this.commandTime = new SimpleStringProperty( this.getTime( this.commandTimestamp.get() ) ) ;
        this.commandValidity = new SimpleBooleanProperty() ;
    }

    public int getCommandNum() {
        return commandNum.get();
    }

    public boolean getCommandValidity() {
        return commandValidity.get();
    }

    public void setCommandValidity(boolean commandValidity) {
        this.commandValidity.set(commandValidity);
    }

    public String getDeliveryStatusShow() {
        return this.deliveryStatusShow.get();
    }

    public String getPayStatusShow() {
        return payStatusShow.get();
    }

    public String getCommandDate() {
        return commandDate.get();
    }

    public String getCommandTime() {
        return commandTime.get() ;
    }

    public String getDeliveryAdress(){ return this.deliveryAddress.get() ; }

    //init set of attribute by using StatusCollection enumeration
    private void setStatus(){
        this.payStatusShow = this.payStatus.get() ?
                new SimpleStringProperty( StatusCollection.PAIDSTATUS.toString() )
                :   new SimpleStringProperty( StatusCollection.UNPAIDSTATUS.toString() ) ;
        this.deliveryStatusShow = this.deliveryStatus.get() ?
                new SimpleStringProperty( StatusCollection.DELEVERED.toString() )
                :   new SimpleStringProperty( StatusCollection.UNDELIVERED.toString() ) ;
    }

    private String getDate( String datetime){
        String[] datetimeSplited = datetime.split(" ");
        return datetimeSplited[0] ;
    }

    private String getTime( String datetime){
        String[] datetimeSplited = datetime.split(" ");
        return datetimeSplited[1] ;
    }

    public String getCommandTimestamp(){ return this.commandTimestamp.get() ; }
    
    public void setCommandTimestamp( String commandTimestamp ){
        this.commandTimestamp = new SimpleStringProperty(commandTimestamp ); }
    
    public String getClientName(){  return this.clientName.get() ; }
    
    public void setClientName( String clientName ){ this.clientName = new SimpleStringProperty(clientName ); }
    
    public boolean getPayStatus(){ return this.payStatus.get() ; }
    
    public void setPayStatus( boolean payStatus ){ this.payStatus = new SimpleBooleanProperty( payStatus ); }
    
    public boolean getDeliveryStatus(){ return this.deliveryStatus.get() ; }
    
    public void setDeliveryStatus( boolean deliveryStatus ){ this.deliveryStatus =
            new SimpleBooleanProperty( deliveryStatus ); }
    
    public String getDeliveryAddress(){ return this.deliveryAddress.get() ; }
    
    public void setDeliveryAddress( String deliveryAddress ){
        this.deliveryAddress = new SimpleStringProperty( deliveryAddress ) ; }
    
}
