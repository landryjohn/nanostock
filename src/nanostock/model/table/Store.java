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
public class Store {
    
    protected int storeNum;
    
    protected String localization;
    
    protected String storeDescript;
    
    public Store( int storeNum, String localization, String storeDescript ){
        
        this.storeNum = storeNum;
        this.localization = localization;
        this.storeDescript = storeDescript;
        
    }
    
    public Store(){}
    
    public int getStoreNum(){ return this.storeNum ; }
    
    public String getLocalization(){ return this.localization; }
    
    public void setLocalozation( String localization ){ this.localization = localization ; }
    
    public String getStoreDescript(){ return this.storeDescript ; }
    
    public void setStoreDescript( String storeDescript ){ this.storeDescript = storeDescript; }
    
    
}
