/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.model.table.DAO;

import nanostock.app.logic.App;
import nanostock.model.table.Command;
import nanostock.model.table.CommandItem;
import nanostock.model.table.ItemStore;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author landry john
 */
public class CommandItemDAO{

    public void create( CommandItem commandItem ) throws SQLException {
        ArrayList parameters = new ArrayList(Arrays.asList(
                commandItem.getItemCode(),
                commandItem.getCommandNum() ,
                commandItem.getCommandQuantity() ,
                commandItem.getDiscountValue() ) ) ;
        App.prepare("INSERT INTO CommandItem values( ? , ? , ? , ? )", parameters , 0);
    }

    public void update( CommandItem commandItem ) throws SQLException {
        ArrayList parameters = new ArrayList(Arrays.asList(
                commandItem.getCommandQuantity() ,
                commandItem.getDiscountValue() ,
                commandItem.getItemCode(),
                commandItem.getCommandNum() )  );
        App.prepare("UPDATE CommandItem SET commandQuantity = ? ,  discountValue = ?  " +
                        " WHERE itemCode = ? and CommandNum = ?" ,
                parameters , 0 ) ;
    }

    public void delete( CommandItem commandItem ) throws SQLException {
        ArrayList parameters = new ArrayList( Arrays.asList( commandItem.getItemCode() ,
                                                commandItem.getCommandNum() ) ) ;
        ItemStore itemStore = FactoryDAO.getItemStoreDAO().findByItemCode( commandItem.getItemCode() ) ;
        itemStore.setStockQuantity( itemStore.getSockQuantity() + commandItem.getCommandQuantity() ) ;
        FactoryDAO.getItemStoreDAO().update( itemStore ) ;
        App.prepare("DELETE FROM CommandItem WHERE itemCode = ? and commandNum = ? " ,
                parameters ,  0 )  ;
    }

    public void deleteItemSold( CommandItem commandItem ) throws SQLException {
        ArrayList parameters = new ArrayList( Arrays.asList( commandItem.getItemCode() ,
                commandItem.getCommandNum() )) ;
        App.prepare("DELETE FROM CommandItem WHERE itemCode = ? and commandNum = ? " ,
                parameters ,  0 )  ;

    }

    public void deleteAllCommandItemByCommand( Command command ) throws SQLException {
        ArrayList<CommandItem> commandItemList = this.findByCommand( command ) ;
        for( CommandItem commandItem : commandItemList ){
            this.delete( commandItem );
        }
    }

    public ArrayList<CommandItem> findByCommand( Command command ) throws SQLException {

        ArrayList parameters = new ArrayList( Arrays.asList( command.getCommandNum() ) ) ;
        ArrayList<CommandItem> commandItemList = new ArrayList<>();
        ResultSet result = App.prepare(
                "SELECT * FROM CommandItem " +
                        "INNER JOIN Command ON Command.commandNum = CommandItem.commandNum " +
                        "INNER JOIN Item ON Item.itemCode = CommandItem.itemCode " +
                        "WHERE CommandItem.commandNum = ? " , parameters ) ;


        while ( result.next() ){

            commandItemList.add( new CommandItem( FactoryDAO.getCommandDAO().find( result.getInt("commandNum") ),
                    FactoryDAO.getItemDAO().find( result.getInt("itemCode") ) ,  result.getInt("commandQuantity"),
                    result.getDouble("discountValue") ) ) ;

        }

        return commandItemList ;

    }


}
