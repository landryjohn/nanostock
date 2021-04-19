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
import java.util.logging.Level;
import java.util.logging.Logger;
import nanostock.app.logic.App;
import nanostock.model.table.Command;

/**
 *
 * @author landry john
 */
public class CommandDAO extends DAO<Command>{

    @Override
    public boolean create(Command command) {
        boolean operationCheck = false ;
        ArrayList parameters = new ArrayList( Arrays.asList( command.getClientName(),
                                                            command.getPayStatus() ? 1 : 0,
                                                            command.getDeliveryStatus() ? 1 : 0,
                                                            command.getDeliveryAddress() ) );
        try {
            App.prepare("INSERT INTO Command(commandTimestamp , clientName , "
                                            + " payStatus , deliveryStatus, deliveryAddress )"
                                            + " values( DATETIME('now','localtime') ,?,?,?,?)", parameters, 0);
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return operationCheck;
    }

    @Override
    public Command find(int commandNum ) {
        Command command = new Command();
        ArrayList parameters = new ArrayList( Arrays.asList( commandNum ) );
        try {
            ResultSet result = App.prepare("SELECT * FROM Command WHERE commandNum = ? ", parameters);
            command = new Command(result.getInt("commandNum"), 
                            result.getString("commandTimestamp"),
                            result.getString("clientName"),
                            result.getBoolean("payStatus"),
                            result.getBoolean("deliveryStatus"),
                            result.getString("deliveryAddress")
                        );
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return command;
    }

    @Override
    public boolean update(Command command) {
        
        boolean operationCheck = false ;
        ArrayList parameters = new ArrayList( Arrays.asList( command.getCommandTimestamp() ,
                                                            command.getClientName(),
                                                            command.getPayStatus() ? 1 : 0 ,
                                                            command.getDeliveryStatus() ? 1 : 0 ,
                                                            command.getDeliveryAddress() ,
                                                            command.getCommandValidity() ? 1 : 0 ,
                                                            command.getCommandNum() ));
        try {
             App.prepare("UPDATE Command SET commandTimestamp = ? ,"
                                            + "clientName = ? , payStatus = ? , "
                                            + "deliveryStatus = ?, deliveryAddress = ? ,  " +
                                            " commandValidity = ? " +
                                            "WHERE commandNum = ?  " , parameters , 0) ;
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return operationCheck;
        
    }

    @Override
    public boolean delete(int commandNum ) {
        
        boolean operationCheck = false ;
        
        ArrayList parameters = new ArrayList( Arrays.asList( commandNum ) );
        try {
             App.prepare("DELETE FROM Command WHERE commandNum = ?  ", parameters , 0);
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return operationCheck;
        
    }

    @Override
    public ArrayList<Command> getAll() {

        ArrayList<Command> commandList = new ArrayList();
        
        try {
            ResultSet result = App.query("SELECT * FROM Command");
            while( result.next() ){
                commandList.add( new Command(   result.getInt("commandNum"), 
                                                result.getString("commandTimestamp") ,
                                                result.getString("clientName") ,
                                                result.getBoolean("payStatus"),
                                                result.getBoolean("deliveryStatus") ,
                                                result.getString("deliveryAddress") ) ) ;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return commandList ;
        
    }

    public ArrayList<Command> getAll(String stringValue) {

        ArrayList<Command> commandList = new ArrayList();

        try {
            ResultSet result = App.query("SELECT * FROM Command where clientName like '%"+stringValue+"%' or payStatus like '%"+stringValue+"%' or deliveryStatus like '%"+stringValue+"%' or deliveryAddress like '%"+stringValue+"%'");
            while( result.next() ){
                commandList.add( new Command(   result.getInt("commandNum"),
                        result.getString("commandTimestamp") ,
                        result.getString("clientName") ,
                        result.getBoolean("payStatus"),
                        result.getBoolean("deliveryStatus") ,
                        result.getString("deliveryAddress") ) ) ;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return commandList ;

    }

    @Override
    public Command getLast() {

        Command command = new Command();
        try {
            ResultSet result = App.query("SELECT * FROM Command WHERE commandNum = "
                                        + "( SELECT max(commandNum) FROM Command )");
            while( result.next() ){
                command = new Command(    result.getInt("commandNum"), 
                                                result.getString("commandTimestamp") ,
                                                result.getString("clientName") ,
                                                result.getBoolean("payStatus"),
                                                result.getBoolean("deliveryStatus") ,
                                                result.getString("deliveryAddress") ) ;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return command ;

    }

    @Override
    protected boolean deleteAll() {

        boolean operationCheck = false ;
        
        try {
            ResultSet result = App.query("DELETE FROM Command");
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return operationCheck;
    }

    @Override
    protected ArrayList<Command> getAll(int offset, int limit) {

        ArrayList<Command> commandList ;
        ArrayList<Command> commandListSort = new ArrayList();
        commandList = this.getAll();
        int i = offset , j = limit ;
        while ( i <= j && j < commandList.size() ){
            commandListSort.add( commandList.get( i ) ) ;
            i++ ;
        }
        
        return commandListSort ;
    }

    public void activeCommandValidity( Command command ) throws SQLException {
        ArrayList parameters = new ArrayList( Arrays.asList( command.getCommandNum() ) ) ;
        App.prepare("UPDATE Command SET commandValidity = 1" , parameters , 0 ) ;

    }

    public void deleteInvalidCommand() throws SQLException{
        ArrayList<Command>invalidCommandList =  this.getAllCommandByValidity( false ) ;
        for (  Command invalidCommand  : invalidCommandList ) {
            FactoryDAO.getCommandItemDAO().deleteAllCommandItemByCommand( invalidCommand );
        }
        App.query("DELETE FROM Command WHERE commandValidity = 0 " , 0 ) ;
    }

    public ArrayList<Command> getAllCommandByValidity( boolean validity ) throws SQLException {
        ArrayList parameters = new ArrayList( Arrays.asList( validity ? 1 : 0 ) ) ;
        ArrayList<Command> commandList = new ArrayList<>() ;
        ResultSet result = App.prepare("SELECT * FROM Command WHERE commandNum = ? " ,
                parameters ) ;

        while( result.next() ){
            commandList.add( new Command(    result.getInt("commandNum"),
                    result.getString("commandTimestamp") ,
                    result.getString("clientName") ,
                    result.getBoolean("payStatus"),
                    result.getBoolean("deliveryStatus") ,
                    result.getString("deliveryAddress") ) );
        }

        return commandList ;

    }
    
}
