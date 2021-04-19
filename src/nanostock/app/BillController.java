package nanostock.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import nanostock.app.logic.Switcher;
import nanostock.model.table.Bill;
import nanostock.model.table.Command;
import nanostock.model.table.CommandItem;
import nanostock.model.table.DAO.FactoryDAO;
import nanostock.model.table.Operation;
import nanostock.model.table.generic.HistoryBuilder;
import nanostock.model.table.generic.NumberToLetter;
import nanostock.model.table.generic.WatchDogApp;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;

public class BillController extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    String  sellerManText ;
    String  clientNameText ;
    String  billTotalPrice ;
    String  billTotalPriceInLetter ;
    String  deliveryAddressText ;
    String  locationDateBillText ;
    String  billPrintedTimeText ;
    String  billNumberText ;
    String billDatetime ;
    ArrayList<CommandItem> commandItem ; 
    
    private Command command = new Command() ;
    
//    MouseEvent event ; 
    
    public void showReport( Command command ) throws JRException, ClassNotFoundException, SQLException {
        
        this.command = command ; 
        this.commandItem = getAll() ; 
//        this.event = event ; 
        
        this.sellerManText =  "Vendeur : " + WatchDogApp.getUserConnected().getUserName() ;
        this.clientNameText =  "Client : " + command.getClientName() ;
        this.billTotalPrice =  this.getBillTotalPrice() + " FCFA" ;
        NumberToLetter c = new NumberToLetter();
        c.setMontant("" + this.getBillTotalPrice() + ".00");
        c.calculer_glob();
        this.billTotalPriceInLetter =  c.getMontantLettre() + " FCFA"  ;
        this.deliveryAddressText =  "Adresse de livraison : " + command.getDeliveryAddress() ;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd") ;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm") ;
        LocalDateTime nowPrintingDatetime = LocalDateTime.now() ;
        this.billPrintedTimeText =  timeFormatter.format( nowPrintingDatetime  ) ;
        this.locationDateBillText = "Yaoundé le " + dateFormatter.format( nowPrintingDatetime )  ;
        this.billDatetime = dateFormatter.format( nowPrintingDatetime ) + " " +
                timeFormatter.format( nowPrintingDatetime  ) ;

        try {
            ArrayList<Bill> billList = FactoryDAO.getBillDAO().getAll() ;
            if( billList.size() == 0 ){
                this.billNumberText = "FACTURE N°" + 1  ;
            }else{
                this.billNumberText = "FACTURE N°" + ( billList.get( billList.size() - 1 ).getBillCode() + 1 )  ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        String reportSrcFile = "data/bill.jrxml";
        
        // First, compile jrxml file.
        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
        // Fields for report
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        
        parameters.put("locationDateBillText" , "1") ;
        parameters.put("billPrintedTimeText" , "2" ) ;
        parameters.put("sellerManText" , "3" );
        parameters.put("clientNameText" , "4");
        parameters.put("deliveryAddressText" , "5");
        
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
        
        // After the printing process as done successfully 
        
        this.validAllOnPrinting();


        try {
            FactoryDAO.getBillDAO().create( new Bill( this.billDatetime , this.command.getCommandNum() ,
                    WatchDogApp.getUserConnected().getUserId() ) );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.saveOperation();
        
        
        
        
    }
//
//    @FXML TableView<CommandItem> billTableView  ;
//    @FXML TableColumn<CommandItem , Integer> quantityColumn ;
//    @FXML TableColumn<CommandItem , String> designationColumn ;
//    @FXML TableColumn<CommandItem , Double> priceItemColumn ;
//    @FXML TableColumn<CommandItem , Double> priceTotalItemColumn ;
//    @FXML Text sellerManText ;
//    @FXML Text clientNameText ;
//    @FXML Label billTotalPrice ;
//    @FXML Label billTotalPriceInLetter ;
//    @FXML Text deliveryAddressText ;
//    @FXML Text locationDateBillText ;
//    @FXML Text billPrintedTimeText ;
//    @FXML Button printButton ;
//    @FXML AnchorPane root ;
//
//    @FXML Text billNumberText ;
//    @FXML ImageView goBackButton ;
    
//
//    public void initialization( Command command ){
//
//        this.command = command  ;
//        quantityColumn.setCellValueFactory( new PropertyValueFactory<CommandItem , Integer>("commandQuantity"));
//        designationColumn.setCellValueFactory( new PropertyValueFactory<CommandItem , String>("itemReference"));
//        priceItemColumn.setCellValueFactory( new PropertyValueFactory<CommandItem , Double>("discountValue"));
//        priceTotalItemColumn.setCellValueFactory( new PropertyValueFactory<CommandItem , Double>("totalPrice"));

//        try {
//            this.billTableView.setItems( this.getAll() ) ;
//            for (  CommandItem commandItem : billTableView.getItems() ) {
//                System.out.println("valeur : " + commandItem.getTotalPrice() ) ;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        this.sellerManText.setText( "Vendeur : " + WatchDogApp.getUserConnected().getUserName() );
//        this.clientNameText.setText( "Client : " + this.command.getClientName() );
//        this.billTotalPrice.setText( this.getBillTotalPrice() + " FCFA");
//        NumberToLetter c = new NumberToLetter();
//        c.setMontant("" + this.getBillTotalPrice() + ".00");
//        c.calculer_glob();
//        this.billTotalPriceInLetter.setText( c.getMontantLettre() + " FCFA") ;
//        this.billTotalPriceInLetter.setPrefSize(
//                this.billTotalPriceInLetter.getText().length()*8 , 25);
//        this.deliveryAddressText.setText( "Adresse de livraison : " + this.command.getDeliveryAddress() ) ;
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd") ;
//        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm") ;
//        LocalDateTime nowPrintingDatetime = LocalDateTime.now() ;
//        this.billPrintedTimeText.setText( timeFormatter.format( nowPrintingDatetime  ) );
//        this.locationDateBillText.setText("Yaoundé le " + dateFormatter.format( nowPrintingDatetime ) ) ;
//        this.billDatetime = dateFormatter.format( nowPrintingDatetime ) + " " +
//                timeFormatter.format( nowPrintingDatetime  ) ;
//
//        try {
//            ArrayList<Bill> billList = FactoryDAO.getBillDAO().getAll() ;
//            if( billList.size() == 0 ){
//                this.billNumberText.setText("FACTURE N°" + 1) ;
//            }else{
//                this.billNumberText.setText("FACTURE N°" + ( billList.get( billList.size() - 1 ).getBillCode() + 1 ) ) ;
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }


    private void saveOperation() throws SQLException {
        ArrayList<CommandItem> commandItemList = FactoryDAO.getCommandItemDAO().
                findByCommand( this.command ) ;
        for( CommandItem commandItem : commandItemList){
            FactoryDAO.getOperationDAO().create( new Operation( WatchDogApp.getUserConnected().getUserId() ,
                    HistoryBuilder.buildSellHistoryData("Vente de l'article " + commandItem.getItemReference() ,
                            4 , commandItem.getItemCode() , commandItem.getCommandQuantity() ,
                            commandItem.getDiscountValue() , commandItem.getCommandNum() ,
                            commandItem.getClientName() , command.getDeliveryStatus() ? 1 : 0 ,
                            command.getPayStatus() ? 1 : 0 , command.getDeliveryAddress())));
        }
    }

//    public void printBill( MouseEvent event ) throws SQLException {
//
//        this.printButton.setVisible( false );
//        this.goBackButton.setVisible( false ) ;
//
//        // Create the Printer Job
//        PrinterJob printerJob = PrinterJob.createPrinterJob();
//
//        // Get The Printer Job Settings
//        JobSettings jobSettings = printerJob.getJobSettings();
//
//        // Get the Page Layout
//        PageLayout pageLayout = jobSettings.getPageLayout();
//
//        // Get The Printer
//        Printer printer = printerJob.getPrinter();
//        // Create the Page Layout of the Printer
//        pageLayout = printer.createPageLayout( Paper.A4 ,
//                PageOrientation.PORTRAIT , 0 , 0 , 0 , 0  );
//
//
//        jobSettings.setPageLayout(pageLayout);

//        // Show the page setup dialog
//        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
////        boolean proceed = printerJob.showPageSetupDialog( window );
//
//            print( printerJob , this.root, pageLayout , window );
//
//            this.validAllOnPrinting();
//
//            this.printButton.setVisible( true ) ;
//            this.goBackButton.setVisible( true ) ;
//
//        try {
//            FactoryDAO.getBillDAO().create( new Bill( this.billDatetime , this.command.getCommandNum() ,
//                    WatchDogApp.getUserConnected().getUserId() ) );
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        this.saveOperation();
//        
////        try {
////            this.goToSetCommandBill( event );
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//    }

    public void validAllOnPrinting(){
        this.command.setPayStatus( true );
        this.command.setDeliveryStatus( true );
        FactoryDAO.getCommandDAO().update( command ) ;
    }

//    public void goToSetCommandBill( MouseEvent event ) throws IOException,SQLException {
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation( getClass().getResource("view/setCommandBillView.fxml") ) ;
//        Parent setCommandBillViewParent = loader.load();
//        this.initDefaultSize();
//        Scene setCommandBillViewScene = new Scene( setCommandBillViewParent, this.getWidth() , this.getHeight() ) ;
//        SetCommandBillController setCommandBillController = (SetCommandBillController)loader.getController();
//        setCommandBillController.initializeCommand( this.command  );
//        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//        window.setScene( setCommandBillViewScene );
//        window.setMaximized(true);
//        window.show();
//    }

//    private void print(PrinterJob job, Node node , PageLayout pageLayout, Window window )
//    {
//        // Print the node
//        boolean printed = job.printPage(pageLayout , node );
//
//        if (printed)
//        {
//            job.showPrintDialog( window ) ;
//            job.endJob();
//        }
//    }

//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        // noting to do fir the moment
//    }

    public ArrayList<CommandItem> getAll() throws SQLException {
        Command com = new Command( this.command.getCommandNum()  ) ;
        ArrayList<CommandItem> arrayList = new ArrayList() ; 
        arrayList.addAll(FactoryDAO.getCommandItemDAO().findByCommand( com )) ;
        return arrayList ;
    }

    public double getBillTotalPrice(){
        double billTotalPrice =  0   ;
        for( CommandItem commanditem : this.commandItem  ){
            billTotalPrice += commanditem.getTotalPrice() ;
        }
        return billTotalPrice ;
    }


}
