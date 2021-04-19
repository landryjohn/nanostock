/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock;

/**
 *
 * @author Landry John
 */
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;

public class PrintReport extends JFrame {

    private static final long serialVersionUID = 1L;

    public void showReport() throws JRException, ClassNotFoundException, SQLException {

        String reportSrcFile = "data/bill.jrxml";

// First, compile jrxml file.
        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
// Fields for report
        HashMap<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("company", "MAROTHIA TECHS");
        parameters.put("receipt_no", "RE101");
        parameters.put("name", "Khushboo");
        parameters.put("amount", "10000");
        parameters.put("receipt_for", "EMI Payment");
        parameters.put("date", "20-12-2016");
        parameters.put("contact", "98763178");
        parameters.put("testField", "Allo despuii");

        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object> >();
        list.add(parameters);

        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, null, beanColDataSource);
        JRViewer viewer = new JRViewer(print);
        viewer.setOpaque(true);
        viewer.setVisible(true);
        this.add(viewer);
        this.setSize(700, 500);
        this.setVisible(true);
        System.out.print("Done!");

    }

}
