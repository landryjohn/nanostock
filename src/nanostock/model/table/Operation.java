/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.model.table;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author landry john
 */
public class Operation {
    
    protected SimpleIntegerProperty operationId ;
    
    protected SimpleIntegerProperty operationUserId ;
    
    protected SimpleStringProperty operationWording;
    
    protected SimpleStringProperty operationTimestamp;

    protected SimpleStringProperty operationDate ;

    protected SimpleStringProperty operationTime ;
    
    protected SimpleBooleanProperty archiveStatus;
    
    public Operation(){}

    
    public Operation( int operationId, int operationUserId, 
            String operationWording, String operationTimestamp, boolean archiveStatus ){
        this.operationId = new SimpleIntegerProperty( operationId );
        this.operationUserId = new SimpleIntegerProperty( operationUserId );
        this.operationWording = new SimpleStringProperty( operationWording );
        this.operationTimestamp = new SimpleStringProperty( operationTimestamp );
        this.archiveStatus = new SimpleBooleanProperty( archiveStatus );
        this.operationDate = new SimpleStringProperty( this.getDate() ) ;
        this.operationTime = new SimpleStringProperty( this.getTime() ) ;
    }

    public Operation( int operationUserId,
                      String operationWording ){
        this.operationUserId = new SimpleIntegerProperty( operationUserId );
        this.operationWording = new SimpleStringProperty( operationWording );
    }

    public String getDate(){
        String[] dateFormat = this.operationTimestamp.get().split(" ");
        return dateFormat[0] ;
    }

     public String getTime(){
        String[] dateFormat = this.operationTimestamp.get().split(" ") ;
        return dateFormat[1] ;
    }

    public int getOperationId() {
        return operationId.get();
    }

    public void setOperationId(int operationId) {
        this.operationId.set(operationId);
    }

    public int getOperationUserId() {
        return operationUserId.get();
    }

    public void setOperationUserId(int operationUserId) {
        this.operationUserId.set(operationUserId);
    }

    public String getOperationWording() {
        return operationWording.get();
    }

    public void setOperationWording(String operationWording) {
        this.operationWording.set(operationWording);
    }

    public String getOperationTimestamp() {
        return operationTimestamp.get();
    }

    public void setOperationTimestamp(String operationTimestamp) {
        this.operationTimestamp.set(operationTimestamp);
    }

    public boolean isArchiveStatus() {
        return archiveStatus.get();
    }

    public void setArchiveStatus(boolean archiveStatus) {
        this.archiveStatus.set(archiveStatus);
    }

    public String getOperationDate() {
        return operationDate.get();
    }

    public void setOperationDate(String operationDate) {
        this.operationDate.set(operationDate);
    }

    public String getOperationTime() {
        return operationTime.get();
    }

    public void setOperationTime(String operationTime) {
        this.operationTime.set(operationTime);
    }

    public boolean getArchiveStatus(){
        return this.archiveStatus.get() ;
    }
}
