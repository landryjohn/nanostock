/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.model.table;

import java.sql.Timestamp;

/**
 *
 * @author landry john
 */
public class Bill {
    
    private int billCode;
    
    private String billTimestamp;

    private int commandNum ;

    private int userId ;
    
    public Bill( int billCode, String billTimestamp ){
        
        this.billCode = billCode;
        this.billTimestamp = billTimestamp; 
        
    }

    public Bill( int billCode , String billTimestamp , int commandNum , int userId  ){
        this.billCode = billCode ;
        this.commandNum = commandNum ;
        this.userId = userId ;
        this.billTimestamp = billTimestamp ;
    }

    public Bill( String billTimestamp , int commandNum , int userId  ){
        this.commandNum = commandNum ;
        this.userId = userId ;
        this.billTimestamp = billTimestamp ;
    }
    
    public Bill(){}
    
    public int getBillCode(){ return this.billCode; }
    
    public String getBillTimestamp(){ return this.billTimestamp; }
    
    public void setBillTimestamp( String billTimestamp){ this.billTimestamp = billTimestamp; }


    public void setBillCode(int billCode) {
        this.billCode = billCode;
    }

    public int getCommandNum() {
        return commandNum;
    }

    public void setCommandNum(int commandNum) {
        this.commandNum = commandNum;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
