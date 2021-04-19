/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.model.table;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import nanostock.model.table.generic.ItemStoreGeneric ;

/**
 *
 * @author landry john
 */
public class ItemStore extends ItemStoreGeneric {

    public ItemStore(){
        this.itemImageName = new SimpleStringProperty("") ;
    }


    
    public ItemStore( Item item, Store store , int stockQuantity , 
                    String exitSpecification, int alertQuantity  ){
    
        this.itemCode = new SimpleIntegerProperty( item.getItemCode() ) ;
        this.unitPrice = new SimpleDoubleProperty( item.getUnitPrice() ) ;
        this.itemDes = new SimpleStringProperty( item.getItemDes() );
        this.itemTimestamp = item.getItemTimestamp()  ;
        this.enterPrice = new SimpleDoubleProperty( item.getEnterPrice()  ) ;
        this.wholesalePrice = new SimpleDoubleProperty( item.getWholesalePrice() );
        this.itemReference = new SimpleStringProperty( item.getItemReference() );
        this.itemImageName = new SimpleStringProperty( item.getItemImageName() );
        this.storeNum = new SimpleIntegerProperty( store.getStoreNum()  ) ;
        this.localization = new SimpleStringProperty( store.getLocalization() ) ;
        this.storeDescript = new SimpleStringProperty( store.getStoreDescript() );
        this.stockQuantity = new SimpleIntegerProperty( stockQuantity ) ;
        this.exitSpecification = new SimpleStringProperty( exitSpecification ) ;
        this.alertQuantity = new SimpleIntegerProperty( alertQuantity ) ;
        this.stockQuantity = new SimpleIntegerProperty( stockQuantity ) ;
        this.categoryId = new SimpleIntegerProperty( item.getCategoryId() ) ;

    }

    public ItemStore( int stockQuantity , String exitSpecification, int alertQuantity ) {
        this.stockQuantity = new SimpleIntegerProperty(stockQuantity) ;
        this.exitSpecification = new SimpleStringProperty( exitSpecification );
        this.alertQuantity = new SimpleIntegerProperty( alertQuantity );
    }

    public int getSockQuantity(){ return this.stockQuantity.get() ; }
    
    public String getExitSpecification(){ return this.exitSpecification.get() ; }
    
    public int getAlertQuantity(){ return this.alertQuantity.get() ; }
    
    public void setAlertQuantity( int alertQuantity ){ this.alertQuantity.set( alertQuantity ) ; }
    
}
