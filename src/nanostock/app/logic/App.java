/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.app.logic;

import java.sql.* ;
import java.util.ArrayList;
/**
 *
 * @author landry johm
 */
public class App {

    /**
     * execute a non prepared query
     *
     * @param request
     * @return ResultSet object
     * @throws SQLException
     */
    public static ResultSet query(String request) throws SQLException {

        ResultSet result;
        Statement statement = Database.getDBInstance().createStatement();
        result = statement.executeQuery(request);
        return result;

    }

    public static boolean query(String request , int returnResult ) throws SQLException {

        Statement statement = Database.getDBInstance().createStatement();
        statement.executeUpdate(request);
        return true ;

    }

    /**
     * execute a non prepared query
     * it actually support parameters type such as integer, float, double, Date, String
     *
     * @param request
     * @param parameters
     * @return
     * @throws SQLException
     */
    public static ResultSet prepare(String request, ArrayList parameters) throws SQLException {

        PreparedStatement preparedStatement = Database.getDBInstance().prepareStatement(request);

        for (int i = 1, j = 0; i <= parameters.size(); i++) {
            if (parameters.get(j) instanceof String) {
                preparedStatement.setString(i, (String) parameters.get(j));
            }
            if (parameters.get(j) instanceof Integer) {
                preparedStatement.setInt(i, (int) parameters.get(j));
            }
            if (parameters.get(j) instanceof Double) {
                preparedStatement.setDouble(i, (double) parameters.get(j));
            }
            if (parameters.get(j) instanceof Float) {
                preparedStatement.setFloat(i, (float) parameters.get(j));
            }
            if (parameters.get(j) instanceof Date) {
                preparedStatement.setDate(i, (Date) parameters.get(j));
            }

            j++;

        }

        return preparedStatement.executeQuery();

    }

    public static boolean prepare(String request, ArrayList parameters, int returnResult) throws SQLException {

        PreparedStatement preparedStatement = Database.getDBInstance().prepareStatement(request);

        for (int i = 1, j = 0; i <= parameters.size(); i++) {
            if (parameters.get(j) instanceof String) {
                preparedStatement.setString(i, (String) parameters.get(j));
            }
            if (parameters.get(j) instanceof Integer) {
                preparedStatement.setInt(i, (int) parameters.get(j));
            }
            if (parameters.get(j) instanceof Double) {
                preparedStatement.setDouble(i, (double) parameters.get(j));
            }
            if (parameters.get(j) instanceof Float) {
                preparedStatement.setFloat(i, (float) parameters.get(j));
            }
            if (parameters.get(j) instanceof Date) {
                preparedStatement.setDate(i, (Date) parameters.get(j));
            }

            j++;

        }

        preparedStatement.executeUpdate() ;

        return true ;

    }

}
