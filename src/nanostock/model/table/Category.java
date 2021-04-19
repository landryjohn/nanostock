/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.model.table;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author landry john
 */
public class Category {
    
    private SimpleIntegerProperty categoryId ;
    
    private SimpleStringProperty categoryName ;
    
    private SimpleStringProperty categoryDes ;

    private SimpleIntegerProperty alertValue ;
    
    public int getCategoryId() {
        return categoryId.get();
    }

    public String getCategoryName() {
        return categoryName.get();
    }

    public String getCategoryDes() {
        return categoryDes.get();
    }
    
    public int getAlertValue(){
        return this.alertValue.get();
    }

    public Category( int  categoryId, String categoryName, String categoryDes, int alertValue ) {
        this.categoryId = new SimpleIntegerProperty ( categoryId ) ;
        this.categoryName = new SimpleStringProperty ( categoryName ) ;
        this.categoryDes = new SimpleStringProperty ( categoryDes ) ;
        this.alertValue = new SimpleIntegerProperty ( alertValue ) ;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName.set(categoryName);
    }

    public void setCategoryDes(String categoryDes) {
        this.categoryDes.set(categoryDes);
    }

    public void setAlertValue(int alertValue ){
        this.alertValue.set( alertValue ) ;
    }




}
