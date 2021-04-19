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
import nanostock.model.table.Category;

/**
 *
 * @author landry john
 */
public class CategoryDAO extends DAO<Category> {

    @Override
    public boolean create(Category category) {

        boolean operationCheck = false ;
        ArrayList parameters = new ArrayList( Arrays.asList( category.getCategoryName() , 
                                                                category.getCategoryDes() , 
                                                                category.getAlertValue() ) ) ;
        
        try {
            operationCheck = App.prepare("INSERT INTO ItemCategory( categoryName, categoryDes, alertValue )"
                    + "VALUES( ? , ? , ? ) " ,
                    parameters , 0) ;
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return operationCheck ;
    }

    public Category findCategoryByName(  String categoryName ) throws SQLException {
        ArrayList parametres = new ArrayList( Arrays.asList( categoryName ) );
        ResultSet result ;
        Category category = null ;
        result = App.prepare("SELECT * FROM ItemCategory WHERE categoryName = ? " , parametres ) ;

        while( result.next() ){
            category = new Category( result.getInt("categoryId") ,
                                        result.getString("categoryName"),
                                        result.getString("categoryDes"),
                                        result.getInt("alertValue") ) ;
        }

        return category ;

    }


    public boolean searchCategory( String categoryName ) throws SQLException {
        Category category = this.findCategoryByName( categoryName ) ;
        return category != null;
    }

    @Override
    public Category find(int categoryId ) {
        ArrayList<Category> categoryList = this.getAll() ;
        Category categoryFound = null ;
        for ( Category category : categoryList ) {
            if( category.getCategoryId() == categoryId ){
                categoryFound = category ;
                break ;
            }
        }

        return categoryFound ;
    }

    @Override
    public boolean update(Category category){
        boolean result = false ;
        ArrayList parameters = new ArrayList( Arrays.asList( category.getCategoryName() ,
                category.getCategoryDes() , category.getAlertValue(),
                category.getCategoryId())) ;
        try {
            result = App.prepare("UPDATE ItemCategory SET categoryName = ? " +
                    " , categoryDes = ? , alertValue = ? WHERE categoryId = ? " , parameters , 0 ) ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result ;
    }

    @Override
    public boolean delete(int categoryId ) {

        boolean operationCheck = false ;

        ArrayList parameters = new ArrayList( Arrays.asList( categoryId ) );
        try {
            App.prepare("DELETE FROM ItemCategory WHERE categoryId = ? ", parameters, 0 );
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return operationCheck;

    }

    @Override
    public ArrayList<Category> getAll() {
        ArrayList<Category> categoryList = new ArrayList();

        try {
            ResultSet result = App.query("SELECT * FROM ItemCategory");
            while( result.next() ){
                categoryList.add( new Category( result.getInt("categoryId"),
                                                result.getString("categoryName"),
                                                result.getString("categoryDes"),
                                                result.getInt("alertValue"))) ;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return categoryList ;
    }

    @Override
    public Category getLast() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Category> getAll(int offset, int limit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
