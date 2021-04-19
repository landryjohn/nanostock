/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.model.table.DAO;

import java.util.ArrayList;

/**
 * 
 * @author landry john
 * @param <T> 
 */
public abstract class DAO<T> {
    /**
     * insert the object in the database
     * @param object
     * @return 
     */
    protected abstract boolean create( T object ) ;
    
    /**
     * find the data that match the object in database by id
     * @param id
     * @return 
     */
    protected abstract T find( int id ) ;
    
    /**
     * update value of entry that match the object in database
     * @param object
     * @return 
     */
    protected abstract boolean update(T object);
    
    /**
     * delete data of entry that match the object
     * @param id
     * @return 
     */
    protected abstract boolean delete(int id );
    
    /**
     * Get all occurrence those match the Class
     * @return 
     */
    protected abstract ArrayList<T> getAll();
    
    /**
     * Get the last occurrence who match the class
     * @return 
     */
    protected abstract T getLast();
    
    /**
     * Delete all occurrence of table that match the class
     * @return 
     */
    protected abstract boolean deleteAll();
    
    protected abstract ArrayList<T> getAll(int offset , int limit ); 
    
}
