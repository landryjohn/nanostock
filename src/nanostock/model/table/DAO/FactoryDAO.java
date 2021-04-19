/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.model.table.DAO;

/**
 *
 * @author landry john
 */
public class FactoryDAO {

    public static EmployeeDAO getEmployeeDAO(){ return new EmployeeDAO(); }

    public static ChargesDAO getChargesDAO(){ return new ChargesDAO(); }
    
    public static UserDAO getUserDAO(){
        return new UserDAO();
    }
    
    public static ItemDAO getItemDAO(){
        return new ItemDAO();
    }
    
    public static ItemStoreDAO getItemStoreDAO(){
        return new ItemStoreDAO();
    }
    
    public static CategoryDAO getCategoryDAO(){
        return new CategoryDAO();
    }

    public static CommandDAO getCommandDAO() { return new CommandDAO(); }

    public static CommandItemDAO getCommandItemDAO() { return new CommandItemDAO() ; }

    public static OperationDAO getOperationDAO() { return new OperationDAO() ; }

    public static BillDAO getBillDAO() { return new BillDAO() ; }
    
//    public static 
    
}
