/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.model.table;

/**
 *
 * @author landry john
 */
public class ItemCategory {
    
    private int categoryId;
    
    private String categoryName;
    
    private String categoryDes;
    
    public ItemCategory( int categoryId, String categoryName, String categoryDes ){
        
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDes = categoryDes;
                
    }
    
    public ItemCategory(){}
    
    public int getCategoryId(){ return this.categoryId; }
    
    public String getCategoryName(){ return this.categoryName; }
    
    public void setCategoryName( String categoryName ){ this.categoryName = categoryName ; }
    
    public String getCategoryDes(){ return this.categoryDes ; }
    
    public void setCategoryDes( String categoryDes ){ this.categoryDes = categoryDes ; }
    
}
