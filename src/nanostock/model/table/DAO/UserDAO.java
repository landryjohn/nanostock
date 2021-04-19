/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.model.table.DAO;
import nanostock.model.table.DAO.DAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import nanostock.app.logic.App;
import nanostock.model.table.User;
/**
 *
 * @author landry john
 */
public class UserDAO extends DAO<User> {


    @Override
    public User find(int userId){
        User user = new User();
        ArrayList parametres = new ArrayList( Arrays.asList( userId ) );

        try {

            ResultSet result = App.prepare("SELECT * FROM User WHERE userId = ? ", parametres) ;
            if( result.next() )
                user = new User( result.getInt("userId"),
                        result.getString("userName"),
                        result.getString("userPassword"),
                        result.getString("userLogin"),
                        result.getInt("userPhone") );

        } catch (SQLException ex) {

            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);

        }

        return user;
    }

    @Override
    public boolean create(User user){

        boolean operationCheck = false ;
        ArrayList parameters = new ArrayList( Arrays.asList( user.getUserName(),
                user.getUserPassword(),
                user.getUserLogin(),
                user.getUserPhone(),
                user.getUserType()
        ) );
        try {
            App.prepare("INSERT INTO User(userName, userPassword, userLogin, userPhone, userType)"+
                    "values(?,?,?,?,?)", parameters,0);
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return operationCheck;
    }

    public void delete(User user) throws SQLException {
        ArrayList parameters = new ArrayList( Arrays.asList( user.getUserId() ) ) ;
        App.prepare( "DELETE FROM User WHERE userId = ? " , parameters , 0 ) ;
        FactoryDAO.getItemDAO().delete( user.getUserId() ) ;
    }

    @Override
    public boolean delete(int userId){

        boolean operationCheck = false ;
        ArrayList parameters = new ArrayList( Arrays.asList( userId ) );

        try {
            ResultSet result = App.prepare("DELETE FROM User WHERE userId = ?", parameters);
            operationCheck = true;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return operationCheck;
    }

    @Override
    public boolean update(User user){
        boolean operationCheck = false;
        ArrayList parameters = new ArrayList( Arrays.asList( user.getUserName(),
                user.getUserPassword(),
                user.getUserLogin(),
                user.getUSerPhone(),
                user.getUserId() ) ) ;
        System.out.println(" value : " + user.getUserId() );

        try {
            App.prepare("UPDATE User SET userName = ?, userPassword = ?, userLogin = ? "
                    + ", userPhone = ? WHERE userId = ? ", parameters,0) ;
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return operationCheck;
    }

    @Override
    public ArrayList<User> getAll(){

        ArrayList<User> userList = new ArrayList();

        try {
            ResultSet result = App.query("SELECT * FROM User");
            while( result.next() ){

                userList.add( new User( result.getInt("userId"),
                        result.getString("userName"),
                        result.getString("userPassword"),
                        result.getString("userLogin"),
                        result.getString("userType"),
                        result.getInt("userPhone"))) ;

            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return userList ;
    }

    public ArrayList<User> getAll(String stringValue){

        ArrayList<User> userList = new ArrayList();

        try {
            ResultSet result = App.query("SELECT * FROM User where userName like '%"+stringValue+"%' or userLogin like '%"+stringValue+"%' or userType like '%"+stringValue+"%'");
            while( result.next() ){

                userList.add( new User( result.getInt("userId"),
                        result.getString("userName"),
                        result.getString("userPassword"),
                        result.getString("userLogin"),
                        result.getString("userType"),
                        result.getInt("userPhone"))) ;

            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return userList ;
    }

    public boolean existLogin(String userLogin)
    {
        User user = new User() ;
        boolean resultSearch = false;
        ArrayList parameters = new ArrayList( Arrays.asList( userLogin) );
        try {
            ResultSet result = App.prepare("SELECT * FROM User WHERE userLogin = ? ",
                    parameters) ;
            resultSearch = result.next();

        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultSearch ;

    }

    public User searchUser( String userLogin, String userPassword ){

        User user = new User() ;
        ArrayList parameters = new ArrayList( Arrays.asList( userLogin , userPassword ) );
        try {
            ResultSet result = App.prepare("SELECT * FROM User WHERE userLogin = ? and userPassword = ?  ",
                    parameters) ;
            if( result.next() ){
            
                user = new User( result.getInt("userId"),
                        result.getString("userName"),
                        result.getString("userPassword"),
                        result.getString("userLogin"),
                        result.getString("userType"),
                        result.getInt("userPhone")) ;
            }


        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return user ;
    }

    public boolean isUserExist( String userLogin, String userPassword ){

        return this.searchUser( userLogin , userPassword).getUserId() != 0 ; 

    }

    @Override
    public User getLast(){

        User user = new User();
        try {
            ResultSet result = App.query("SELECT * FROM User WHERE userId = "
                    + "( SELECT max(userId) FROM User )");
            while( result.next() ){
                user = new User(result.getInt("userId"),
                        result.getString("userName"),
                        result.getString("userPassword"),
                        result.getString("userLogin"),
                        result.getInt("userPhone") ) ;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return user ;
    }

    @Override
    public boolean deleteAll() {

        boolean operationCheck = false ;
        try {
            ResultSet result = App.query("DELETE FROM User" );
            operationCheck = true;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return operationCheck;
    }

    @Override
    public ArrayList<User> getAll(int offset, int limit) {

        ArrayList<User> userList ;
        ArrayList<User> userListSort = new ArrayList();
        userList = this.getAll();
        int i = offset , j = limit ;
        while ( i <= j && j < userList.size() ){
            userListSort.add( userList.get( i ) ) ;
            i++ ;
        }

        return userListSort ;
    }

}