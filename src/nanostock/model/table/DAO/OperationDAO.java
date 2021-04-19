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
import nanostock.model.table.Operation;
import nanostock.model.table.User;

/**
 *
 * @author landry john
 */
public class OperationDAO extends DAO<Operation>{

    @Override
    public boolean create(Operation operation) {
        boolean operationCheck = false ;
        User user = FactoryDAO.getUserDAO().find( operation.getOperationUserId() ) ;
        operation.setOperationWording( operation.getOperationWording() );
        ArrayList parameters = new ArrayList( Arrays.asList( operation.getOperationUserId(),
                                                            operation.getOperationWording() ) );

        try {
             App.prepare("INSERT INTO Operation(userId,operationWording,operationTimestamp)"
                    + " values(?,?, DATETIME('now','localtime') )", parameters , 0);
             System.out.println( "OK" );
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return operationCheck;
    }

    @Override
    public Operation find(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean update(Operation object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(int operationId ) {

        boolean operationCheck = false ;
        try {
            ArrayList parameters = new ArrayList( Arrays.asList( operationId ) ) ;
            App.prepare("DELETE FROM Operation WHERE operationId = ? ", parameters , 0) ;
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemCategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return operationCheck ;    
        
    }

    @Override
    public ArrayList<Operation> getAll() {

        ArrayList<Operation> operationList = new ArrayList();
        try {
            ResultSet result = App.query("SELECT * FROM Operation WHERE archiveStatus != 1 ");
            while( result.next() ){
                operationList.add( new Operation( result.getInt("operationId"), 
                                            result.getInt("userId") , 
                                            result.getString("operationWording") ,
                                            result.getString("operationTimestamp"),
                                            result.getBoolean("archiveStatus") ) ) ;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return operationList ;
        
    }

    @Override
    public Operation getLast() {

        Operation operation = new Operation();
        try {
            ResultSet result = App.query("SELECT * FROM Operation WHERE operationNum = "
                                        + "( SELECT max(operationNum) FROM Operation )");
            while( result.next() ){
                operation = new Operation( result.getInt("operationId"), 
                                            result.getInt("userId") , 
                                            result.getString("operationWording") ,
                                            result.getString("operationTimestamp"),
                                            result.getBoolean("archiveStatus")) ;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return operation ;
        
    }

    @Override
    public boolean deleteAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Operation> getAll(int offset, int limit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public boolean archiveAll(){
        
        boolean operationCheck = false ;
        try {
            ResultSet result = App.query("UPDATE Operation SET archiveStatus = 1") ;
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemCategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return operationCheck ; 
        
    }

    public void archive( int operationId ){
        try {
            ArrayList parameters = new ArrayList( Arrays.asList( operationId ) ) ;
            App.prepare("UPDATE Operation SET archiveStatus = 1 WHERE operationId = ?" ,
                    parameters , 0) ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemCategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
