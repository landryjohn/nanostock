package nanostock.model.table.DAO;

import nanostock.app.logic.App;
import nanostock.model.table.Employee;
import nanostock.model.table.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeDAO extends DAO<Employee> {
    @Override
    public boolean create(Employee employee) {

        boolean operationCheck = false ;
        ArrayList parameters = new ArrayList( Arrays.asList(employee.getEmployeeSalary(),
                employee.getEmployeeCharges(),
                employee.getEmployeeName(),
                employee.getEmployeeDescription(),
                employee.getEmployeeDatePaiement()));
        try {
            App.prepare("INSERT INTO Employee ("
                    + "employeeSalary, employeeCharges, employeeName, employeeDescription, employeeDatePaiement) " +
                    "values(?,?,?,?,?)", parameters, 0);
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return operationCheck;
    }

    @Override
    public Employee find(int idEmployee) {

        Employee employee = new Employee() ;

        ArrayList parameters = new ArrayList( Arrays.asList( idEmployee ) );



        try {

            ResultSet result = App.prepare("SELECT * FROM Employee WHERE idEmployee = ? " , parameters ) ;
            employee = new Employee(result.getInt("idEmployee"),
                    result.getDouble("employeeSalary"),
                    result.getDouble("employeeCharges"),
                    result.getString("employeeName"),
                    result.getString("employeeDescription"),
                    result.getString("employeeDatePaiement") ) ;

        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return employee ;
    }

    @Override
    public boolean update(Employee employee) {



            boolean operationCheck = false ;
        ArrayList parameters = new ArrayList( Arrays.asList( employee.getEmployeeDescription(),
                employee.getEmployeeSalary(),
                employee.getEmployeeCharges(),
                employee.getEmployeeName(),
                employee.getEmployeeDatePaiement(),
                employee.getIdEmployee() ) );
        try {
            App.prepare("UPDATE Employee set employeeDescription = ?, "
                    + "employeeSalary = ?, employeeCharges = ?,  employeeName = ? "
                    + " , employeeDatePaiement = ? WHERE idEmployee = ? ", parameters , 0 ) ;
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return operationCheck;
    }

    @Override
    public boolean delete(int idEmployee) {

        boolean operationCheck = false ;

        ArrayList parameters = new ArrayList( Arrays.asList( idEmployee ) );
        try {
            App.prepare("DELETE FROM Employee WHERE idEmployee = ? ", parameters, 0 );
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return operationCheck;
    }

    @Override
    public ArrayList<Employee> getAll(){

        ArrayList<Employee> EmployeeList = new ArrayList();


        try {
            ResultSet result = App.query("SELECT * FROM Employee");
            while( result.next() ){
                EmployeeList.add( new Employee(result.getInt("idEmployee"),
                        result.getDouble("employeeSalary"),
                        result.getDouble("employeeCharges"),
                        result.getString("employeeName"),
                        result.getString("employeeDescription"),
                        result.getString("employeeDatePaiement") )) ;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return EmployeeList ;
    }

    public ArrayList<Employee> getAll( String stringValue){

        ArrayList<Employee> EmployeeList = new ArrayList();


        try {
            ResultSet result = App.query("SELECT * FROM Employee where employeeName like '%"+stringValue+"%' or employeeCharges like '%"+stringValue+"%' or employeeDatePaiement like '%"+stringValue+"%' or employeeSalary like '%"+stringValue+"%'");
            while( result.next() ){
                EmployeeList.add( new Employee(result.getInt("idEmployee"),
                        result.getDouble("employeeSalary"),
                        result.getDouble("employeeCharges"),
                        result.getString("employeeName"),
                        result.getString("employeeDescription"),
                        result.getString("employeeDatePaiement") )) ;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return EmployeeList ;
    }

    @Override
    protected Employee getLast() {
        return null;
    }

    @Override
    protected boolean deleteAll() {
        return false;
    }

    @Override
    protected ArrayList<Employee> getAll(int offset, int limit) {
        return null;
    }


}
