/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.app.logic;

import java.sql.* ; 

/**
 *
 * @author landry john
 */
public class Database {
    
    private static final String DATABASEPATH = "data/nanostock.db" ; 
    private static Connection connector = null ;
    private Statement statement = null ;
    private static boolean connectionStatus = false ;
    
    /**
     * use to initialize the object by singleton pattern
     */
    private Database() {
        try{
                
            connector = DriverManager.getConnection( "jdbc:sqlite:" + DATABASEPATH ) ;
            connectionStatus = true ; 
            
        }catch( SQLException e){
            
            System.err.println( e.getClass().getName() + " : " + e.getMessage() );
//            System.exit(0);
        
        }
        
    } 
    
    /**
     * return an unique instance of the database class
     * @return Connector
     */
    public static Connection getDBInstance(){
        
        if(  connector == null ){
            new Database() ;
        }
            
        return connector  ; 
    }
    
    /**
     * check the status of the connection
     * @return 
     */
    public static boolean getConnectionStatus() {
        return connector != null;
    }
    
    /**
     * initialize first the database 
     */
    public void initializeDatabase(){
        
        try{
                
            statement = connector.createStatement() ;
            
            String req= "BEGIN TRANSACTION;\n" +
                        "CREATE TABLE IF NOT EXISTS 'Command' ('commandNum' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'commandTimestamp' DATETIME, 'clientName' TEXT, 'payStatus' BOOLEAN DEFAULT 0 , 'deliveryStatus' BOOLEAN DEFAULT 0 , 'deliveryAddress' TEXT);\n" +
                        "\n" +
                        "CREATE TABLE IF NOT EXISTS 'Item' ('itemCode' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'categoryId' INTEGER, 'itemDes' TEXT, 'itemTimestamp' DATETIME, 'enterPrice' REAL, 'unitPrice' REAL, 'wholesalePrice' REAL, 'itemReference' TEXT);\n" +
                        "\n" +
                        "CREATE TABLE IF NOT EXISTS 'ItemCategory' ('categoryId' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'categoryName' TEXT, 'categoryDes' TEXT, 'alertValue' INTEGER);\n" +
                        "\n" +
                        "CREATE TABLE IF NOT EXISTS 'Store' ('storeNum' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'localization' TEXT, 'storeDescript' TEXT);\n" +
                        "\n" +
                        "CREATE TABLE IF NOT EXISTS 'Commanditem' ('itemCode' INTEGER NOT NULL, 'commandNum' INTEGER NOT NULL, 'commandQauntity' INTEGER, 'discountValue' INTEGER DEFAULT 0 );\n" +
                        "\n" +
                        "CREATE TABLE IF NOT EXISTS 'Bill' ('billCode' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'commandNum' INTEGER, 'billTimestamp' DATETIME, 'userId' INTEGER);\n" +
                        "\n" +
                        "CREATE TABLE IF NOT EXISTS 'User' ('userId' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'userName' TEXT, 'userType' TEXT, 'userPassword' TEXT, 'userPhone' INTEGER, 'userLogin' TEXT);\n" +
                        "\n" +
                        "CREATE TABLE IF NOT EXISTS 'Operation' ('operationId' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'userId' INTEGER, 'operationWording' TEXT, 'operationTimestamp' DATETIME);\n" +
                        "\n" +
                        "CREATE TABLE IF NOT EXISTS 'ItemStore' ('storeNum' INTEGER NOT NULL, 'itemCode' INTEGER NOT NULL, 'stockQuantity' INTEGER, 'exitSpecification' TEXT,'alertQuantity' INTEGER DEFAULT 0 );\n" +
                        "\n" +
                        "COMMIT;";
            
            statement.executeUpdate(req) ; 
                
        }catch(Exception e){
        
            System.err.println( e.getClass().getName() + " : " + e.getMessage() );
        
        }
        
    }
    
    /**
     * destroy the bridge of the communication with the database
     * @throws SQLException 
     */
    public static void endDatabaseSession() throws SQLException{
    
        if( getConnectionStatus() )
            connector.close() ;

    }

    
}

