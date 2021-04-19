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
 * @author landry john
 */
public class Item {
    
    private SimpleIntegerProperty itemCode;

    private SimpleDoubleProperty unitPrice;

    private SimpleStringProperty itemDes;

    private String itemTimestamp;
    
    private SimpleDoubleProperty enterPrice;
    
    private SimpleDoubleProperty wholesalePrice;

    private SimpleStringProperty itemReference ;

    private SimpleStringProperty itemImageName ;

    private SimpleIntegerProperty categoryId ;

    /**
     * wdw
     * @param itemCode
     * @param unitPrice
     * @param itemDes
     * @param itemTimestamp
     * @param enterPrice
     * @param wholesalePrice
     * @param itemReference
     */
    public Item( int itemCode, double unitPrice, String itemDes,
                 String itemTimestamp, double enterPrice, double wholesalePrice ,
                    String itemReference , int categoryId, String itemImageName){
    
        this.itemCode = new SimpleIntegerProperty(itemCode);
        this.unitPrice = new SimpleDoubleProperty( unitPrice );
        this.itemDes = new SimpleStringProperty( itemDes ) ;
        this.itemTimestamp = itemTimestamp ;
        this.enterPrice = new SimpleDoubleProperty( enterPrice ) ;
        this.wholesalePrice = new SimpleDoubleProperty( wholesalePrice )  ;
        this.itemReference = new SimpleStringProperty( itemReference ) ;
        this.categoryId = new SimpleIntegerProperty( categoryId );
        this.itemImageName = new SimpleStringProperty( itemImageName ) ;

    }

//    public Item( int itemCode, double unitPrice, String itemDes,
//                 String itemTimestamp, double enterPrice, double wholesalePrice ,
//                 String itemReference , int categoryId){
//
//        this.itemCode = new SimpleIntegerProperty(itemCode);
//        this.unitPrice = new SimpleDoubleProperty( unitPrice );
//        this.itemDes = new SimpleStringProperty( itemDes ) ;
//        this.itemTimestamp = itemTimestamp ;
//        this.enterPrice = new SimpleDoubleProperty( enterPrice ) ;
//        this.wholesalePrice = new SimpleDoubleProperty( wholesalePrice )  ;
//        this.itemReference = new SimpleStringProperty( itemReference ) ;
//        this.categoryId = new SimpleIntegerProperty( categoryId );
//
//    }

//    public Item( int itemCode, double unitPrice, String itemDes,
//                 double enterPrice, double wholesalePrice ,
//                 String itemReference ){
//
//        this.itemCode = new SimpleIntegerProperty(itemCode);
//        this.unitPrice = new SimpleDoubleProperty( unitPrice );
//        this.itemDes = new SimpleStringProperty( itemDes ) ;
//        this.enterPrice = new SimpleDoubleProperty( enterPrice ) ;
//        this.wholesalePrice = new SimpleDoubleProperty( wholesalePrice )  ;
//        this.itemReference = new SimpleStringProperty( itemReference ) ;
//
//    }

    public Item( int itemCode, double unitPrice, String itemDes,
                 double enterPrice, double wholesalePrice ,
                 String itemReference , String itemImageName){

        this.itemCode = new SimpleIntegerProperty(itemCode);
        this.unitPrice = new SimpleDoubleProperty( unitPrice );
        this.itemDes = new SimpleStringProperty( itemDes ) ;
        this.enterPrice = new SimpleDoubleProperty( enterPrice ) ;
        this.wholesalePrice = new SimpleDoubleProperty( wholesalePrice )  ;
        this.itemReference = new SimpleStringProperty( itemReference ) ;
        this.itemImageName = new SimpleStringProperty( itemImageName ) ;

    }
    
    public Item(){
        this.itemReference = new SimpleStringProperty( "" ) ;
    }
    
    public int getItemCode(){ return this.itemCode.get(); }

    public int getCategoryId(){ return this.categoryId.get() ; }

    public void setCategoryId( int categoryId ){ this.categoryId.set( categoryId ) ;}
    
    public double getUnitPrice(){ return this.unitPrice.get(); }
    
    public void setUnitPrice( double unitPrice ){ this.unitPrice.set( unitPrice ) ; }
    
    public String getItemDes(){ return this.itemDes.get() ;}
    
    public void setItemDes( String itemDes ){ this.itemDes.set( itemDes ) ; }
    
    public String getItemTimestamp(){ return this.itemTimestamp ; }
    
    public void setItemTimestamp( String itemTimestamp){ this.itemTimestamp = itemTimestamp; }
    
    public double getEnterPrice(){ return this.enterPrice.get(); }
    
    public void setEnterPrice( double enterPrice){ this.enterPrice.set( enterPrice) ; }
    
    public double getWholesalePrice(){ return this.wholesalePrice.get(); }
    
    public void setWholesalePrice( double wholesalePrice ){ this.wholesalePrice.set( wholesalePrice ) ; }
    
    public String getItemReference(){ return this.itemReference.get() ; } 

    public void setItemReference( String itemReference ){ this.itemReference.set(itemReference); }

    public String getItemImageName(){ return this.itemImageName.get() ; }

    public void setItemImageName( String itemImageName ){ this.itemImageName.set(itemImageName); }

}
