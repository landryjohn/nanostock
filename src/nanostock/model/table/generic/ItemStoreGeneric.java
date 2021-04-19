/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.model.table.generic;

import java.sql.Timestamp;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author landry john
 */
public class ItemStoreGeneric {
    
    protected SimpleIntegerProperty stockQuantity ;
    
    protected SimpleStringProperty exitSpecification ; 
    
    protected SimpleIntegerProperty alertQuantity ;
    
    protected SimpleIntegerProperty itemCode;
    
    protected SimpleDoubleProperty unitPrice;
    
    protected SimpleStringProperty itemDes;
    
    protected String itemTimestamp;
    
    protected SimpleDoubleProperty enterPrice;
    
    protected SimpleDoubleProperty wholesalePrice;
    
    protected SimpleStringProperty itemReference ;

    protected SimpleStringProperty itemImageName ;

    protected SimpleIntegerProperty storeNum;
    
    protected SimpleStringProperty localization;
    
    protected SimpleStringProperty storeDescript;

    protected SimpleIntegerProperty categoryId ;
    
    public int getItemCode(){ return this.itemCode.get(); }
    
    public double getUnitPrice(){ return this.unitPrice.get(); }
    
    public String getItemDes(){ return this.itemDes.get() ; }

    public int getCategoryId(){ return this.categoryId.get() ; }

    public void setcategoryId( int categoryId ){ this.categoryId.set( categoryId ); }
    
    public String getItemTimestamp(){ return this.itemTimestamp ; }
    
    public double getEnterPrice(){ return this.enterPrice.get(); }
    
    public double getWholesalePrice(){ return this.wholesalePrice.get(); }
    
    public String getItemReference(){ return this.itemReference.get() ; }

    public String getItemImageName(){ return this.itemImageName.get() ; }

    public int getStoreNum(){ return this.storeNum.get() ; }
    
    public String getLocalization(){ return this.localization.get() ; }
        
    public String getStoreDescript(){ return this.storeDescript.get() ; }

    public int getStockQuantity() { return this.stockQuantity.get(); }

    public void setAlertQuantity(int alertQuantity) {
        this.alertQuantity.set(alertQuantity);
    }

    public void setItemDes(String itemDes) {
        this.itemDes.set(itemDes);
    }

    public void setEnterPrice(double enterPrice) {
        this.enterPrice.set(enterPrice);
    }

    public void setWholesalePrice(double wholesalePrice) {
        this.wholesalePrice.set(wholesalePrice);
    }

    public void setItemImageName(String itemImageName) {
        this.itemImageName.set(itemImageName);
    }

    public void setItemReference(String itemReference) { this.itemReference.set(itemReference); }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice.set(unitPrice);
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity.set(stockQuantity);
    }
}
