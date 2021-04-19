/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.model.table.DAO;

import nanostock.app.logic.App;
import nanostock.model.table.Bill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author landry john
 */
public class BillDAO {

    public void create(Bill bill) throws SQLException {

        ArrayList parameters = new ArrayList(Arrays.asList(
                bill.getBillTimestamp() ,
                bill.getCommandNum() ,
                bill.getUserId() ) ) ;
        App.prepare("INSERT INTO Bill( commandNum , billTimestamp , userId ) " +
                "VALUES( ? , ? , ? )" , parameters ,  0 )  ;

    }

    public ArrayList<Bill> getAll() throws SQLException {
        ArrayList<Bill> billList = new ArrayList<>() ;
        ResultSet result = App.query("SELECT * FROM Bill" ) ;
        while( result.next() ) {
            billList.add( new Bill( result.getInt("billCode" ) ,
                    result.getString("billTimestamp") ,
                    result.getInt("commandNum") ,
                    result.getInt("userId")) ) ;
        }

        return billList ;
    }

}
