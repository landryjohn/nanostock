/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.model.table.DAO;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import nanostock.app.logic.App;
import nanostock.model.table.Category;
import nanostock.model.table.Item;
import nanostock.model.table.ItemStore;
import nanostock.model.table.Store;
import nanostock.model.table.generic.Alert;

/**
 *
 * @author landry john
 */
public class ItemStoreDAO{

    public boolean create(Item item , Store store, ItemStore itemStore , Category category ) throws SQLException {

        FactoryDAO.getItemDAO().create( item , category ) ;

        Item lastItem = FactoryDAO.getItemDAO().getLast() ;

        ArrayList parameters = new ArrayList(Arrays.asList( store.getStoreNum() ,
                                                            lastItem.getItemCode(),
                                                            itemStore.getSockQuantity() ,
                                                            itemStore.getExitSpecification(),
                                                            itemStore.getAlertQuantity() ) ) ;

        App.prepare("INSERT INTO " +
                "ItemStore( storeNum, itemCode , quantity , exitSpecification , alertQuantity  )" +
                "VALUES( ? , ? , ? , ? , ?) " , parameters , 0 ) ;
        return true ;


    }

    public ArrayList<ItemStore> getAllItemStore(String stringValue) throws SQLException{
        ArrayList<ItemStore> itemStoreList = new ArrayList() ;
        ResultSet result = App.query("SELECT * FROM " +
                "ItemStore " +
                "INNER JOIN " +
                "Store ON Store.storeNum = ItemStore.storeNum " +
                "INNER JOIN " +
                "Item ON Item.itemCode = ItemStore.itemCode where itemReference like '%"+stringValue+"%' or wholeSalePrice like '%"+stringValue+"%' or unitPrice like '%"+stringValue+"%' or quantity like '%"+stringValue+"%' " +
                "ORDER BY Item.itemReference ASC ");
        Item item = null ;
        Store store = null ;

        while( result.next() ){

            item = new Item( result.getInt("itemCode"),
                    result.getDouble("unitPrice") ,
                    result.getString("itemDes") ,
                    result.getString("itemTimestamp") ,
                    result.getDouble("enterPrice"),
                    result.getDouble("wholesalePrice"),
                    result.getString("itemReference"),
                    result.getInt("categoryId"),
                    result.getString("itemImageName")) ;

            store = new Store( result.getInt("storeNum"),
                    result.getString("localization") ,
                    result.getString("storeDescript") ) ;

            itemStoreList.add( new ItemStore( item , store ,
                    result.getInt("quantity"),
                    result.getString("exitSpecification") ,
                    result.getInt("alertQuantity") ) ) ;

        }
        return itemStoreList ;
    }

    public ArrayList<ItemStore> getAllItemStoreByCategory( String category ){
        ArrayList<ItemStore> itemStoreList = new ArrayList<>() ;
        ArrayList<ItemStore> itemStoreListSorted = new ArrayList<>() ;
        try {
            itemStoreList = this.getAllItemStore();
            for( int i = 0 ; i < itemStoreList.size() ; i++ ){
                if( FactoryDAO.getCategoryDAO().find(itemStoreList.get(i).
                        getCategoryId()).getCategoryName().equals(category) ) {

                    itemStoreListSorted.add( itemStoreList.get(i) ) ;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemStoreListSorted ;
    }

    public ArrayList<ItemStore> getAllItemStore() throws SQLException{
        ArrayList<ItemStore> itemStoreList = new ArrayList() ;
        ResultSet result = App.query("SELECT * FROM " +
                                        "ItemStore " +
                                        "INNER JOIN " +
                                        "Store ON Store.storeNum = ItemStore.storeNum " +
                                        "INNER JOIN " +
                                        "Item ON Item.itemCode = ItemStore.itemCode " +
                                        "ORDER BY Item.itemReference ASC ");
        Item item = null ;
        Store store = null ;
        
        while( result.next() ){
        
            item = new Item( result.getInt("itemCode"), 
                                result.getDouble("unitPrice") , 
                                result.getString("itemDes") , 
                                result.getString("itemTimestamp") ,
                                result.getDouble("enterPrice"), 
                                result.getDouble("wholesalePrice"), 
                                result.getString("itemReference"),
                                result.getInt("categoryId"),
                                result.getString("itemImageName")) ;

            store = new Store( result.getInt("storeNum"), 
                                result.getString("localization") , 
                                result.getString("storeDescript") ) ;
            
            itemStoreList.add( new ItemStore( item , store ,
                    result.getInt("quantity"),
                    result.getString("exitSpecification") ,
                    result.getInt("alertQuantity") ) ) ;
            
        }
        return itemStoreList ;
    }

    /**
     *
     * @param itemStore
     * @throws SQLException
     */
    public void delete(ItemStore itemStore) throws SQLException {
        ArrayList parameters = new ArrayList( Arrays.asList( itemStore.getItemCode() ) ) ;
        App.prepare( "DELETE FROM ItemStore WHERE itemCode = ? " , parameters , 0 ) ;
        FactoryDAO.getItemDAO().delete( itemStore.getItemCode() ) ;
    }

    public void update( ItemStore itemStore ) throws SQLException {
        ArrayList parameters = new ArrayList(Arrays.asList(itemStore.getSockQuantity() ,
                                                            itemStore.getAlertQuantity(),
                                                            itemStore.getItemCode()) ) ;
        App.prepare( "UPDATE ItemStore SET quantity = ?, alertQuantity = ? WHERE itemCode = ?" , parameters , 0 ) ;
    }

    public ItemStore findByItemCode( int itemCode ) throws SQLException {
        ArrayList parameters = new ArrayList( Arrays.asList( itemCode ) ) ;
        ResultSet result = App.prepare("SELECT * FROM " +
                "ItemStore " +
                "INNER JOIN " +
                "Store ON Store.storeNum = ItemStore.storeNum " +
                "INNER JOIN " +
                "Item ON Item.itemCode = ItemStore.itemCode " +
                "WHERE ItemStore.itemCode = ? " , parameters );
        Item item = null ;
        Store store = null ;
        ItemStore itemStore = new ItemStore();

        while( result.next() ){

            item = new Item( result.getInt("itemCode"),
                    result.getDouble("unitPrice") ,
                    result.getString("itemDes") ,
                    result.getString("itemTimestamp") ,
                    result.getDouble("enterPrice"),
                    result.getDouble("wholesalePrice"),
                    result.getString("itemReference"),
                    result.getInt("categoryId"),
                    result.getString("itemImageName") ) ;

            store = new Store( result.getInt("storeNum"),
                    result.getString("localization") ,
                    result.getString("storeDescript") ) ;

            itemStore = new ItemStore( item , store ,
                    result.getInt("quantity"),
                    result.getString("exitSpecification") ,
                    result.getInt("alertQuantity") )  ;

        }
        return itemStore ;


    }

    public ArrayList<ItemStore> getAllAlertItemStore() throws SQLException {
        ArrayList<ItemStore> itemStoreList = this.getAllItemStore() ;
        ArrayList<ItemStore> alertItemStoreList = new ArrayList<>() ;
        for( ItemStore itemStore : itemStoreList ){
            if( itemStore.getSockQuantity() < itemStore.getAlertQuantity() ){
                alertItemStoreList.add( itemStore ) ;
            }
        }
        return alertItemStoreList ;
    }

    public ArrayList<Alert> buildAlert() throws SQLException {
        ArrayList<ItemStore> alertItemStoreList = this.getAllAlertItemStore() ;
        ArrayList<Alert> alertList = new ArrayList<>() ;
        int count = 1  ;
        for( ItemStore itemStore : alertItemStoreList ){
            alertList.add( new Alert(count ,
                    "Le stock est inférieur à la quantité d'alert" ,
                    itemStore.getAlertQuantity() ,
                    itemStore.getSockQuantity(), itemStore.getItemReference() ) ) ;
        }
        return alertList ;
    }

    public int getAlertSize() throws SQLException {
        return this.getAllAlertItemStore().size() ;
    }

    public ArrayList<ItemStore> sortByItemReference(ArrayList<ItemStore> itemStoreList ){
        ItemStore itemStore ; System.out.println("ok");
        for( int i = 0 ; i < itemStoreList.size() ; i++ ){
            for( int j = i ; j < itemStoreList.size() ; j++ ){
                if(itemStoreList.get(j).getItemReference().toUpperCase().compareTo(
                        itemStoreList.get(i).getItemReference().toUpperCase()) > 0 ){
                    itemStore = itemStoreList.get( i ) ;
                    itemStoreList.set( i , itemStoreList.get( j ) ) ;
                    itemStoreList.set( j , itemStore ) ;
                }
            }
        }
        return itemStoreList ;

    }


}
