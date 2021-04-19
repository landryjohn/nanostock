/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package nanostock.model.table.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.Arrays;
import java.util.logging.Level;
import nanostock.app.logic.App;
import nanostock.model.table.Item;
import nanostock.model.table.Store;

/**
 *
 * @author landry john
 */
public class StoreDAO extends DAO<Store>{

    @Override
    public boolean create(Store store) {
        boolean operationCheck = false ;
        ArrayList parameters = new ArrayList( Arrays.asList( store.getLocalization() , store.getStoreDescript() ) ); 
        
        try{
            
            ResultSet result = App.prepare("INSERT INTO Store( localization, storeDescript ) "
                                            + "values( ?, ?)", parameters);
            operationCheck = true ;
        }catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return operationCheck ;
    }

    @Override
    public Store find(int storeNum ) {
        Store store = new Store();
        ArrayList parameters = new ArrayList( Arrays.asList( storeNum ) ) ;
        try {
            ResultSet result = App.prepare("SELECT * FROM Store WHERE storeNum = ? " , parameters ) ;
            if( result.next() ){
                store = new Store( result.getInt("storeNum"), 
                                   result.getString("localization"), 
                                   result.getString("storeDescript" ) ) ;
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoreDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return store ;
    }

    @Override
    public boolean update(Store store) {
        boolean operationCheck = false ;
        ArrayList parameters = new ArrayList( Arrays.asList( store.getLocalization() , 
                                                             store.getStoreDescript()));
        try {
            ResultSet result = App.prepare("UPDATE Item set localization = ?, "
                    + " storeDescription", parameters ) ;
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return operationCheck;
    }

    @Override
    public boolean delete(int storeNum ) {
        boolean operationCheck = false ;
        
        ArrayList parameters = new ArrayList( Arrays.asList( storeNum ) );
        try {
            ResultSet result = App.prepare("DELETE FROM Store WHERE storeNum = ? ", parameters);
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return operationCheck;

    }

    @Override
    public ArrayList<Store> getAll() {
        
        ArrayList<Store> storeList = new ArrayList();
        
        try {
            ResultSet result = App.query("SELECT * FROM Item");
            while( result.next() ){
                storeList.add( new Store(result.getInt("storeNum"),
                                        result.getString("localization"), 
                                        result.getString("storeDescript") ) );
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return storeList ;
    }
    
    @Override
    public Store getLast(){
        
        Store store = new Store();
        try {
            ResultSet result = App.query("SELECT * FROM Store WHERE storeNum = "
                                        + "( SELECT max(storeNum) FROM Store )");
            while( result.next() ){
                store = new Store(result.getInt("storeNum"),
                                        result.getString("localization"), 
                                        result.getString("storeDescript") )  ;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return store ;
    }
    
    @Override
    protected ArrayList<Store> getAll(int offset, int limit) {
           
        ArrayList<Store> storeList ;
        ArrayList<Store> storeListSort = new ArrayList();
        storeList = this.getAll();
        int i = offset , j = limit ;
        while ( i <= j && j < storeList.size() ){
            storeListSort.add( storeList.get( i ) ) ;
            i++ ;
        }
        
        return storeListSort ;
    }

    @Override
    protected boolean deleteAll() {

        boolean operationCheck = false ;
        
        try {
            ResultSet result = App.query("DELETE FROM Store");
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return operationCheck;
        
    }
    
    
}

