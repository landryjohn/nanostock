package nanostock.model.table.generic;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import nanostock.app.HistoryPanelViewController;
import nanostock.model.table.DAO.FactoryDAO;
import nanostock.model.table.Operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class HistoryBuilder extends Operation {

    private SimpleStringProperty operationDescription ;

    private SimpleIntegerProperty operationType ;

    private SimpleIntegerProperty itemCode ;

    private SimpleIntegerProperty quantity ;

    private SimpleDoubleProperty soldPrice ;

    private SimpleIntegerProperty commandNum ;

    private SimpleStringProperty clientName ;

    private SimpleIntegerProperty deliveryStatus ;

    private SimpleIntegerProperty payStatus ;

    private SimpleStringProperty deliveryAddress ;

    private SimpleStringProperty username ;

    private SimpleStringProperty itemReference ;

    public HistoryBuilder(  Operation operation ){

        super( operation.getOperationId() , operation.getOperationUserId(),
                operation.getOperationWording(), operation.getOperationTimestamp(),
                operation.getArchiveStatus() ) ;
        this.username = new SimpleStringProperty(
                FactoryDAO.getUserDAO().find( operation.getOperationUserId()  ).getUserName() ) ;

        ArrayList<String> historyDataFormat =
                new ArrayList<>(Arrays.asList( operation.getOperationWording().split("/=/") ) )   ;

        this.operationDescription  = new SimpleStringProperty( historyDataFormat.get(0) ) ;
        this.operationType  = new SimpleIntegerProperty( Integer.valueOf( historyDataFormat.get(1)  ) ) ;

        if( this.operationType.get() > 1 ){
            this.itemCode  = new SimpleIntegerProperty( Integer.valueOf( historyDataFormat.get(2) ) ) ;
            this.quantity  = new SimpleIntegerProperty( Integer.valueOf( historyDataFormat.get(3) ) );

            this.itemReference = new SimpleStringProperty(
                    FactoryDAO.getItemDAO().find( this.itemCode.get() ).getItemReference() ) ;
        }

        if( this.operationType.get() == 4 ){

            this.soldPrice  = new SimpleDoubleProperty( Double.valueOf( historyDataFormat.get(4) ) ) ;
            this.commandNum  = new SimpleIntegerProperty( Integer.valueOf( historyDataFormat.get(5) ) );
            this.clientName  = new SimpleStringProperty( historyDataFormat.get(6) );
            this.deliveryStatus  = new SimpleIntegerProperty( Integer.valueOf( historyDataFormat.get(7) ))  ;
            this.payStatus  = new SimpleIntegerProperty( Integer.valueOf( historyDataFormat.get(8) ) ) ;
            this.deliveryAddress  = new SimpleStringProperty( historyDataFormat.get(9) ) ;

        }

    }

    public static String buildSellHistoryData(String  operationDescription , int operationType ,
                        int itemCode , int quantity , double soldPrice , int commandNum ,
                        String clientName , int deliveryStatus ,int payStatus ,
                                       String deliveryAddress  ){
        return operationDescription + "/=/" + operationType + "/=/" + itemCode + "/=/" +
                quantity + "/=/" + soldPrice + "/=/" + commandNum + "/=/" + clientName +
                "/=/" + deliveryStatus + "/=/" + payStatus + "/=/" + deliveryAddress ;

    }

    public static String buildInOutHistoryData(String  operationDescription , int operationType ,
                                       int itemCode , int quantity   ){
        return operationDescription + "/=/" + operationType + "/=/" + itemCode + "/=/" +
                quantity ;

    }

    public static ArrayList<HistoryBuilder> getAllHistoryByCommandNum(
            ArrayList<HistoryBuilder> historyList  , int commandNum  ){
        ArrayList<HistoryBuilder> historyCommandList = new ArrayList<>() ;
        for(HistoryBuilder history : historyList  ){
            if( history.getCommandNum() == commandNum ){
                historyCommandList.add( history ) ;
            }
        }
        return historyCommandList ;
    }

    public static ArrayList<HistoryBuilder>  sortByDatePeriod( ArrayList<HistoryBuilder>  historyList ,
                                                  String startDate , String endDate ){
        ArrayList<HistoryBuilder> sortedList = new ArrayList<>();
        for( HistoryBuilder historyBuilder : historyList ){
            if( ( historyBuilder.getDate().compareTo( startDate ) >= 0  ) &&
                    ( historyBuilder.getDate().compareTo( endDate ) <= 0  ) ){
                sortedList.add( historyBuilder ) ;
            }
        }
        return sortedList ;
    }

    public static ArrayList<CommandInventory>  sortCommandInventoryByDatePeriod( ArrayList<CommandInventory>  historyList ,
                                                               String startDate , String endDate ){
        ArrayList<CommandInventory> sortedList = new ArrayList<>();
        for( CommandInventory history : historyList ){
            if( ( history.getCommandDate().compareTo( startDate ) >= 0  ) &&
                    ( history.getCommandDate().compareTo( endDate ) <= 0  ) ){
                sortedList.add( history ) ;
            }
        }
        return sortedList ;
    }

    public static CommandInventory buildCommandInventory(ArrayList<HistoryBuilder> historyList,
            int commandNum ){
        ArrayList<HistoryBuilder> historyCommandList = new ArrayList<>() ;
        int commandQuantity = 0 ;
        double commandTotalCoast = 0 ;
        for( HistoryBuilder history : historyList ){
            if( history.getCommandNum() == commandNum ){
                historyCommandList.add( history ) ;
                commandQuantity++ ;
                commandTotalCoast += history.getSoldPrice()  ;
            }
        }
        CommandInventory commandInventory = new CommandInventory() ;

        if( historyCommandList.size() != 0 ){
            HistoryBuilder firstHistory = historyCommandList.get(0) ;
            commandInventory = new CommandInventory( commandNum ,
                    firstHistory.getDate() , firstHistory.getTime() , firstHistory.getClientName() ,
                    firstHistory.getDeliveryAddress() , commandTotalCoast, commandQuantity,
                    firstHistory.getUsername() ) ;
        }

        return commandInventory ;
    }

    public static ArrayList<Integer> getAllCommandNum(ArrayList<HistoryBuilder> historyList){
        ArrayList<Integer> commandNumList = new ArrayList<>() ;
        boolean check = false ;
        for( HistoryBuilder history : historyList ){
            for (Integer integer : commandNumList) {
                if ( history.getCommandNum() == integer ) {
                    check = true;
                    break;
                }
            }
            if( !check   ){ commandNumList.add( history.getCommandNum() ) ;  }
            check = false ;

        }
        return commandNumList ;
    }

    public static ArrayList<HistoryBuilder>  sortByTimePeriod( ArrayList<HistoryBuilder>  historyList ,
                                                               String startTime , String endTime ){
        ArrayList<HistoryBuilder> sortedList = new ArrayList<>();
        for( HistoryBuilder historyBuilder : historyList ){
            if( ( historyBuilder.getTime().compareTo( startTime ) >= 0 ) &&
                    ( historyBuilder.getTime().compareTo( endTime ) <= 0 ) ){
                sortedList.add( historyBuilder ) ;
            }
        }
        return sortedList ;
    }

    public static ArrayList<CommandInventory>  sortCommandInventoryByTimePeriod( ArrayList<CommandInventory>  historyList ,
                                                               String startTime , String endTime ){
        ArrayList<CommandInventory> sortedList = new ArrayList<>();
        for( CommandInventory history : historyList ){
            if( ( history.getCommandTime().compareTo( startTime ) >= 0 ) &&
                    ( history.getCommandTime().compareTo( endTime ) <= 0 ) ){
                sortedList.add( history ) ;
            }
        }
        return sortedList ;
    }


    public String getOperationDescription() {
        return operationDescription.get();
    }

    public void setOperationDescription(String operationDescription) {
        this.operationDescription.set(operationDescription);
    }

    public int getOperationType() {
        return operationType.get();
    }

    public void setOperationType(int operationType) {
        this.operationType.set(operationType);
    }

    public int getItemCode() {
        return itemCode.get();
    }

    public void setItemCode(int itemCode) {
        this.itemCode.set(itemCode);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public double getSoldPrice() {
        return soldPrice.get();
    }

    public void setSoldPrice(double soldPrice) {
        this.soldPrice.set(soldPrice);
    }

    public int getCommandNum() {
        return commandNum.get();
    }

    public void setCommandNum(int commandNum) {
        this.commandNum.set(commandNum);
    }

    public String getClientName() {
        return clientName.get();
    }

    public void setClientName(String clientName) {
        this.clientName.set(clientName);
    }

    public int getDeliveryStatus() {
        return deliveryStatus.get();
    }

    public void setDeliveryStatus(int deliveryStatus) {
        this.deliveryStatus.set(deliveryStatus);
    }

    public int getPayStatus() {
        return payStatus.get();
    }

    public void setPayStatus(int payStatus) {
        this.payStatus.set(payStatus);
    }

    public String getDeliveryAddress() {
        return deliveryAddress.get();
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress.set(deliveryAddress);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getItemReference() {
        return itemReference.get();
    }

    public void setItemReference(String itemReference) {
        this.itemReference.set(itemReference);
    }
}
