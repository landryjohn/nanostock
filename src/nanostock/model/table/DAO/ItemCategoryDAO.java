/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.model.table.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import nanostock.app.logic.App;
import nanostock.model.table.ItemCategory;

/**
 *
 * @author landry john
 */
public class ItemCategoryDAO extends DAO<ItemCategory> {

    @Override
    public boolean create(ItemCategory itemCategory) {
        boolean operationCheck = false ;
        
        ArrayList parameters = new ArrayList( Arrays.asList(
                                                itemCategory.getCategoryName() ,
                                                itemCategory.getCategoryDes()
                                            ) ) ;        
        try {
      
            ResultSet result = App.prepare("INSERT INTO ItemCategory(categoryName , categoryDes ) "
                    + "values ( ? , ? ) ", parameters) ;
            operationCheck = true ;
                    
        } catch (SQLException ex) {
            Logger.getLogger(ItemCategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return operationCheck ;
        
    }

    @Override
    public ItemCategory find(int id) {
        ItemCategory itemCategory = new ItemCategory() ;
        ArrayList parameters = new ArrayList( Arrays.asList( id )) ;
        try {
            ResultSet result = App.prepare("SELECT * FROM ItemCategory WHERE categoryId = ? " , parameters ) ;
            if( result.next() ){
                itemCategory = new ItemCategory(result.getInt("categoryId"), 
                                                result.getString("categoryName") , 
                                                result.getString("categoryDes") ) ;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemCategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return itemCategory ;

    }

    @Override
    public boolean update(ItemCategory itemCategory) {

        boolean operationCheck = false ;
        ArrayList parameters = new ArrayList( Arrays.asList( itemCategory.getCategoryName(),
                                                                itemCategory.getCategoryDes() ) ) ;
        try {
            ResultSet result = App.prepare("UPDATE ItemCategory "
                    + "SET categoryName = ? , categoryDes = ? ", parameters);
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemCategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return operationCheck ;
        
    }

    @Override
    public boolean delete(int id) {
        boolean operationCheck = false ;
        try {
            ArrayList parameters = new ArrayList( Arrays.asList( id ) ) ;
            ResultSet result = App.prepare("DELETE FROM ItemCategory WHERE categoryId = ? ", parameters) ;
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemCategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return operationCheck ;
    }

    @Override
    public ArrayList<ItemCategory> getAll() {
        ArrayList<ItemCategory> itemCategoryList = new ArrayList() ;
        ItemCategory itemCategory = new ItemCategory();
        try {
            ResultSet result = App.query("SELECT * ItemCategory");
            while( result.next() ){
                itemCategory = new ItemCategory( result.getInt("categoryId") , 
                                                    result.getString("categoryName"), 
                                                    result.getString("categoryDes") ) ;
                itemCategoryList.add( itemCategory ) ;
            
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemCategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return itemCategoryList ;
    }
    
    @Override
    public ItemCategory getLast(){
    
        ItemCategory itemCategory = new ItemCategory();
        try {
            ResultSet result = App.query("SELECT * FROM ItemCategory WHERE categoryId = "
                                        + "( SELECT max(categoryId) FROM Item )");
            while( result.next() ){
                itemCategory = new ItemCategory(result.getInt("categoryId"),
                                        result.getString("categoryName"),
                                        result.getString("categoryDes") ) ;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return itemCategory ;
    }

    @Override
    public boolean deleteAll() {
        
        boolean operationCheck = false ;
        try {
            ResultSet result = App.query("DELETE FROM ItemCategory ") ;
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemCategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return operationCheck ;
        
    }

    @Override
    public ArrayList<ItemCategory> getAll(int offset, int limit) {

        ArrayList<ItemCategory> itemCategoryList ;
        ArrayList<ItemCategory> itemCategoryListSort = new ArrayList();
        itemCategoryList = this.getAll();
        int i = offset , j = limit ;
        while ( i <= j && j < itemCategoryList.size() ){
            itemCategoryListSort.add( itemCategoryList.get( i ) ) ;
            i++ ;
        }
        
        return itemCategoryListSort ;
        
    }
    
    
}
