/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.model.table;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author landry john
 */
public class User {

    private SimpleIntegerProperty userId;

    private SimpleStringProperty userName;

    private SimpleStringProperty userPassword;

    private SimpleStringProperty userLogin;

    private SimpleStringProperty userType;

    private SimpleIntegerProperty userPhone;

    /**
     * Construct the User object
     *
     * @param userId
     * @param userName
     * @param userPassword
     * @param userLogin
     * @param userPhone
     */
    public User(int userId, String userName, String userPassword, String userLogin, int userPhone) {
        this.userId = new SimpleIntegerProperty(userId);
        this.userName = new SimpleStringProperty(userName);
        this.userPassword = new SimpleStringProperty(userPassword);
        this.userLogin = new SimpleStringProperty(userLogin);
        this.userPhone = new SimpleIntegerProperty(userPhone);

    }

    public User(int userId, String userName, String userPassword, String userLogin, String userType, int userPhone) {
        this.userId = new SimpleIntegerProperty(userId);
        this.userName = new SimpleStringProperty(userName);
        this.userPassword = new SimpleStringProperty(userPassword);
        this.userLogin = new SimpleStringProperty(userLogin);
        this.userType = new SimpleStringProperty(userType);
        this.userPhone = new SimpleIntegerProperty(userPhone);

    }

    public User() {
        this.userId = new SimpleIntegerProperty(0);
        this.userName = new SimpleStringProperty("") ;
    }

    public int getUserId() {
        return this.userId.get();
    }

    public String getUserName() {
        return this.userName.get();
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public String getUserType() {
        return this.userType.get();
    }

    public void setUserType(String userType) {
        this.userType.set(userType);
    }

    public int getUserPhone() {
        return this.userPhone.get();
    }


    public String getUserPassword() {
        return this.userPassword.get();
    }

    public void setUserPassword(String userPassword) {
        this.userPassword.set(userPassword);
    }

    public String getUserLogin() {
        return this.userLogin.get();
    }

    public void setUserLogin(String userLogin) {
        this.userLogin.set(userLogin);
    }

    public int getUSerPhone() {
        return this.userPhone.get();
    }

    public void setUserPhone(int userPhone) {
        this.userPhone.set(userPhone);
    }
}