package nanostock.model.table.DAO;

import nanostock.app.logic.App;
import nanostock.model.table.Charges;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChargesDAO extends DAO<Charges> {

    @Override
    public boolean create(Charges charges) {
        boolean operationCheck = false ;
        ArrayList parameters = new ArrayList( Arrays.asList(
                charges.getIdEmployee(),
                charges.getChargesValue(),
                charges.getChargesLib(),
                charges.getDateCharges()));
        try {
            App.prepare("INSERT INTO Charges ("
                    + "idEmployee, chargesValue, chargesLib, dateCharges) " +
                    "values(?,?,?,?)", parameters, 0);
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ChargesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return operationCheck;
    }

    public ArrayList<Charges> getAllChargesEmployee(int idEmployee)
    {
        ArrayList<Charges> EmployeeChargesList = new ArrayList();
        try {
            ResultSet result = App.query("SELECT * FROM Charges where idEmployee = "+idEmployee);
            while( result.next() ){
                EmployeeChargesList.add( new Charges(result.getInt("idCharges"),
                        result.getInt("idEmployee"),
                        result.getDouble("chargesValue"),
                        result.getString("chargesLib"),
                        result.getString("dateCharges") )) ;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return EmployeeChargesList ;
    }

    @Override
    protected Charges find(int id) {
        return null;
    }

    @Override
    protected boolean update(Charges object) {
        return false;
    }

    @Override
    public boolean delete(int idCharges) {

        boolean operationCheck = false ;

        ArrayList parameters = new ArrayList( Arrays.asList( idCharges ) );
        try {
            App.prepare("DELETE FROM Charges WHERE idCharges = ? ", parameters, 0 );
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return operationCheck;
    }

    public boolean deleteEmployeeCharges(int idEmployee) {

        boolean operationCheck = false ;

        ArrayList parameters = new ArrayList( Arrays.asList( idEmployee ) );
        try {
            App.prepare("DELETE FROM Charges WHERE idEmployee = ? ", parameters, 0 );
            operationCheck = true ;
        } catch (SQLException ex) {
            Logger.getLogger(ItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return operationCheck;
    }

    @Override
    protected ArrayList<Charges> getAll() {
        return null;
    }

    @Override
    protected Charges getLast() {
        return null;
    }

    @Override
    protected boolean deleteAll() {
        return false;
    }

    @Override
    protected ArrayList<Charges> getAll(int offset, int limit) {
        return null;
    }
}
