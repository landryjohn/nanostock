package nanostock.model.table.generic;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Alert {

    private SimpleIntegerProperty alertNum ;

    private SimpleStringProperty alertMessage ;

    private SimpleIntegerProperty alertQuantity ;

    private SimpleIntegerProperty stockQuantity ;

    private SimpleStringProperty alertItemRef ;

    public Alert(int alertNum, String alertMessage, int alertQuantity, int stockQuantity, String alertItemRef) {
        this.alertNum = new SimpleIntegerProperty(alertNum);
        this.alertMessage = new SimpleStringProperty(alertMessage);
        this.alertQuantity = new SimpleIntegerProperty(alertQuantity);
        this.stockQuantity = new SimpleIntegerProperty(stockQuantity);
        this.alertItemRef = new SimpleStringProperty( alertItemRef ) ;
    }

    public int getAlertNum() {
        return alertNum.get();
    }

    public void setAlertNum(int alertNum) {
        this.alertNum.set(alertNum);
    }

    public String getAlertMessage() {
        return alertMessage.get();
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage.set(alertMessage);
    }

    public int getAlertQuantity() {
        return alertQuantity.get();
    }

    public void setAlertQuantity(int alertQuantity) {
        this.alertQuantity.set(alertQuantity);
    }

    public int getStockQuantity() {
        return stockQuantity.get();
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity.set(stockQuantity);
    }

    public String getAlertItemRef() {
        return alertItemRef.get();
    }

    public void setAlertItemRef(String alertItemRef) {
        this.alertItemRef.set(alertItemRef);
    }
}
