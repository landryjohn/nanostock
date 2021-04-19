/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.model.table.DAO;

import nanostock.model.table.Category;
import nanostock.model.table.Item;
import nanostock.model.table.DAO.DAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import nanostock.app.logic.App;
import nanostock.model.table.ItemStore;

/**
 *
 * @author landry john
 */
public class ItemDAO extends DAO<Item> {

    @Override
    public boolean create(Item item) {
        boolean operationCheck = false ;
        ArrayList parameters = new ArrayList( Arrays.asList( item.getUnitPrice(), 
                                                            item.getItemDes(),
                                                            item.getEnterPrice(),
                                                            item.getWholesalePrice(),
                                                            item.getItemReference()));
        try {
            App.prepare("INSERT INTO Item(unitPrice, "
                    + "itemDes, itemTimestamp, enterPrice, wholesalePrice, itemReference) " +
                    "values(?,?, DATETIME('now','localtime') ,?,?,?)", parameters, 0);
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return operationCheck;
    }

    public boolean create(Item item , Category category ) {
        boolean operationCheck = false ;
        ArrayList parameters = new ArrayList( Arrays.asList( category.getCategoryId() ,
                                                            item.getUnitPrice(),
                                                            item.getItemDes(),
                                                            item.getEnterPrice(),
                                                            item.getWholesalePrice(),
                                                            item.getItemReference(),
                                                            item.getItemImageName()));
        try {
             App.prepare("INSERT INTO Item(  categoryId , unitPrice, "
                    + "itemDes, itemTimestamp, enterPrice, wholesalePrice, itemReference,itemImageName) " +
                    "values( ? , ?, ?, DATETIME('now','localtime'),?,?,?,?)", parameters , 0 );
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return operationCheck;
    }

    @Override
    public Item find(int itemCode ) {
        Item item = new Item();
        ArrayList parameters = new ArrayList( Arrays.asList( itemCode ) );
        try {
            ResultSet result = App.prepare("SELECT * FROM Item WHERE itemCode = ? ", parameters);
            item = new Item(result.getInt("itemCode"), 
                            result.getDouble("unitPrice"),
                            result.getString("itemDes"),
                            result.getString("itemTimestamp"),
                            result.getDouble("enterPrice"),
                            result.getDouble("wholesalePrice"),
                            result.getString("itemReference"),
                            result.getInt("categoryId"),
                            result.getString("itemImageName")  ) ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return item;
    }

    @Override
    public boolean update(Item item) {
        boolean operationCheck = false ;
        ArrayList parameters = new ArrayList( Arrays.asList( item.getUnitPrice(),
                                                            item.getItemDes(),
                                                            item.getEnterPrice(),
                                                            item.getWholesalePrice(),
                                                            item.getItemReference(),
                                                            item.getItemCode() ) );
        System.out.println(" value : " + item.getItemCode() );
        try {
            App.prepare("UPDATE Item set unitPrice = ?, "
                    + "itemDes = ?, enterPrice = ?,  wholesalePrice = ? "
                    + " , itemReference = ? WHERE itemCode = ? ", parameters , 0 ) ;
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return operationCheck;
    }

    public boolean updateItem(Item item) {
        boolean operationCheck = false ;
        ArrayList parameters = new ArrayList( Arrays.asList( item.getUnitPrice(),
                item.getItemDes(),
                item.getEnterPrice(),
                item.getWholesalePrice(),
                item.getItemReference(),
                item.getItemImageName(),
                item.getItemCode() ) );
        System.out.println(" value : " + item.getItemCode() );
        try {
            App.prepare("UPDATE Item set unitPrice = ?, "
                    + "itemDes = ?, enterPrice = ?,  wholesalePrice = ? "
                    + " , itemReference = ? , itemImageName = ? WHERE itemCode = ? ", parameters , 0 ) ;
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return operationCheck;
    }
    @Override
    public boolean delete(int itemCode) {

        boolean operationCheck = false ;
        
        ArrayList parameters = new ArrayList( Arrays.asList( itemCode ) );
        try {
            App.prepare("DELETE FROM Item WHERE itemCode = ? ", parameters, 0 );
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return operationCheck;

    }
    
    @Override
    public ArrayList<Item> getAll(){
    
        ArrayList<Item> itemList = new ArrayList();
        
        try {
            ResultSet result = App.query("SELECT * FROM Item");
            while( result.next() ){
                itemList.add( new Item(result.getInt("itemCode"),
                                        result.getDouble("unitPrice"), 
                                        result.getString("itemDes"), 
                                        result.getString("itemTimestamp"),
                                        result.getDouble("enterPrice"),
                                        result.getDouble("wholesalePrice"),
                                        result.getString("itemReference") ,
                                        result.getInt("categoryId"),
                                        result.getString("itemImageName") )) ;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return itemList ;
    }

    @Override
    public Item getLast(){
        Item item = new Item();
        try {
            ResultSet result = App.query("SELECT * FROM Item WHERE itemCode = "
                                        + "( SELECT max(itemCode) FROM Item )");
            while( result.next() ){
                item = new Item(result.getInt("itemCode"),
                                        result.getDouble("unitPrice"), 
                                        result.getString("itemDes"), 
                                        result.getString("itemTimestamp"),
                                        result.getDouble("enterPrice"),
                                        result.getDouble("wholesalePrice"),
                                        result.getString("itemReference"),
                                        result.getInt("categoryId"),
                                        result.getString("itemImageName") ) ;

                System.out.println("valeur : " + result.getString("itemTimestamp") );

            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return item ;
    }

    @Override
    public boolean deleteAll() {

        boolean operationCheck = false ;
        
        try {
            ResultSet result = App.query("DELETE FROM Item");
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return operationCheck;
        
    }

    @Override
    public ArrayList<Item> getAll(int offset, int limit) {
           
        ArrayList<Item> itemList ;
        ArrayList<Item> itemListSort = new ArrayList();
        itemList = this.getAll();
        int i = offset , j = limit ;
        while ( i <= j && j < itemList.size() ){
            itemListSort.add( itemList.get( i ) ) ;
            i++ ;
        }
        
        return itemListSort ;
    }
    
    public ArrayList<Item> getAllItemByCategory( String categoryName ) throws SQLException{

        ArrayList itemList = new ArrayList() ;
        ArrayList parameters = new ArrayList( Arrays.asList( categoryName ) ) ;
        ResultSet result = App.prepare("SELECT * FROM Item INNER JOIN ItemCategory "
                + " WHERE Item.categoryId = ItemCategory.categoryId", parameters);
        while( result.next() ){
            itemList.add( new Item(result.getInt("itemCode"),
                        result.getDouble("unitPrice"), 
                        result.getString("itemDes"), 
        //TODO
                        result.getTimestamp("itemTimestamp").toString(),
                        result.getDouble("enterPrice"),
                        result.getDouble("wholesalePrice"), 
                        result.getString("itemReference") ,
                        result.getInt("categoryId") ,
                        result.getString("itemImageName") ) ) ;
        }
        
        return itemList ;
    }

    public boolean searchItem( String itemReference ) throws SQLException {
        Item item = this.find( itemReference ) ;
        return item != null;
    }

    public Item find( String itemReference ) throws SQLException {
        ArrayList parameters = new ArrayList( Arrays.asList( itemReference ) );
        ResultSet result ;
        Item item = null ;
        result = App.prepare("SELECT * FROM Item WHERE itemReference = ? " , parameters ) ;

        while( result.next() ){
            item = new Item(result.getInt("itemCode"),
                    result.getDouble("unitPrice"),
                    result.getString("itemDes"),
                    result.getString("itemTimestamp"),
                    result.getDouble("enterPrice"),
                    result.getDouble("wholesalePrice"),
                    result.getString("itemReference"),
                    result.getInt("categoryId") ,
                    result.getString("itemImageName") ) ;
        }

        return item ;
    }
    
    
}
