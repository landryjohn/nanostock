/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.model.table;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import nanostock.model.table.generic.CommandItemGeneric;

/**
 *
 * @author landry john
 */
public class CommandItem extends CommandItemGeneric {
    
    public CommandItem( Command command , Item item , int commandQuantity , double discountValue ){

        this.itemCode = new SimpleIntegerProperty( item.getItemCode() ) ;
        this.unitPrice = new SimpleDoubleProperty( item.getUnitPrice() ) ;
        this.itemDes = new SimpleStringProperty( item.getItemDes() );
        this.itemTimestamp = new SimpleStringProperty( item.getItemTimestamp()  );
        this.enterPrice = new SimpleDoubleProperty( item.getEnterPrice()  ) ;
        this.wholesalePrice = new SimpleDoubleProperty( item.getWholesalePrice() );
        this.itemReference = new SimpleStringProperty( item.getItemReference() );

        this.commandNum = new SimpleIntegerProperty( command.getCommandNum() ) ;
        this.commandTimestamp = new SimpleStringProperty( command.getCommandTimestamp() ) ;
        this.clientName =  new SimpleStringProperty( command.getClientName() ) ;
        this.payStatus = new SimpleBooleanProperty( command.getPayStatus() ) ;
        this.deliveryStatus = new SimpleBooleanProperty( command.getDeliveryStatus() ) ;
        this.deliveryAddress = new SimpleStringProperty( command.getDeliveryAddress() ) ;
        this.setStatus();
        this.commandDate = new SimpleStringProperty( this.getDate( this.commandTimestamp.get() ) ) ;
        this.commandTime = new SimpleStringProperty( this.getTime( this.commandTimestamp.get() ) ) ;

        this.commandQuantity = new SimpleIntegerProperty( commandQuantity ) ;
        this.discountValue = new SimpleDoubleProperty( discountValue ) ;
        this.totalPrice = new SimpleDoubleProperty( discountValue * commandQuantity ) ;

    }

    public CommandItem( int commandQuantity , int discountValue ){
        this.commandQuantity = new SimpleIntegerProperty( commandQuantity ) ;
        this.discountValue = new SimpleDoubleProperty( discountValue ) ;
    }

    public CommandItem( int itemCode , int commandNum , int commandQuantity , double discountValue ){
        this.itemCode = new SimpleIntegerProperty( itemCode ) ;
        this.commandNum = new SimpleIntegerProperty( commandNum ) ;
        this.commandQuantity = new SimpleIntegerProperty( commandQuantity ) ;
        this.discountValue = new SimpleDoubleProperty( discountValue ) ;
    }


    
}
