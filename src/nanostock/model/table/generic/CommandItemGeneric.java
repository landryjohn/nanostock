package nanostock.model.table.generic;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CommandItemGeneric {

    protected SimpleIntegerProperty commandQuantity ;

    protected SimpleDoubleProperty discountValue ;

/////
    protected SimpleIntegerProperty commandNum ;

    protected SimpleStringProperty commandTimestamp;

    protected SimpleStringProperty clientName;

    protected SimpleBooleanProperty payStatus;

    protected SimpleStringProperty payStatusShow ;

    protected SimpleBooleanProperty deliveryStatus;

    protected SimpleStringProperty deliveryStatusShow ;

    protected SimpleStringProperty deliveryAddress;

    protected SimpleStringProperty commandDate ;

    protected SimpleStringProperty commandTime ;

    /////

    /////

    protected SimpleIntegerProperty itemCode;

    protected SimpleDoubleProperty unitPrice;

    protected SimpleStringProperty itemDes;

    protected SimpleStringProperty itemTimestamp;

    protected SimpleDoubleProperty enterPrice;

    protected SimpleDoubleProperty wholesalePrice;

    protected SimpleStringProperty itemReference ;

    protected SimpleDoubleProperty totalPrice ;

    /////


    public double getTotalPrice() {
        return totalPrice.get();
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice.set(totalPrice);
    }

    public int getCommandQuantity(){ return this.commandQuantity.get() ; }

    public double getDiscountValue(){ return this.discountValue.get() ; }

    public void setCommandQuantity( int commandQuantity ){ this.commandQuantity.set( commandQuantity);}

    public void setDiscountValue( double discountValue ){ this.discountValue.set( discountValue) ; }

    /////
    public int getCommandNum() {
        return commandNum.get();
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
    protected void setStatus(){
        this.payStatusShow = this.payStatus.get() ?
                new SimpleStringProperty( StatusCollection.PAIDSTATUS.toString() )
                :   new SimpleStringProperty( StatusCollection.UNPAIDSTATUS.toString() ) ;
        this.deliveryStatusShow = this.deliveryStatus.get() ?
                new SimpleStringProperty( StatusCollection.DELEVERED.toString() )
                :   new SimpleStringProperty( StatusCollection.UNDELIVERED.toString() ) ;
    }

    protected String getDate( String datetime){
        String[] datetimeSplited = datetime.split(" ");
        return datetimeSplited[0] ;
    }

    protected String getTime( String datetime){
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

    ///////

    public int getItemCode(){ return this.itemCode.get(); }

    public double getUnitPrice(){ return this.unitPrice.get(); }

    public String getItemDes(){ return this.itemDes.get() ;}

    public String getItemTimestamp(){ return this.itemTimestamp.get() ; }

    public void setItemTimestamp( String itemTimestamp ){  this.itemTimestamp.set( itemTimestamp ) ; }

    public double getEnterPrice(){ return this.enterPrice.get(); }

    public double getWholesalePrice(){ return this.wholesalePrice.get(); }

    public String getItemReference(){ return this.itemReference.get() ; }

    public void setItemDes(String itemDes) {
        this.itemDes.set(itemDes);
    }

    public void setEnterPrice(double enterPrice) {
        this.enterPrice.set(enterPrice);
    }

    public void setWholesalePrice(double wholesalePrice) {
        this.wholesalePrice.set(wholesalePrice);
    }

    public void setItemReference(String itemReference) {
        this.itemReference.set(itemReference);
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice.set(unitPrice);
    }

    ///////
}
