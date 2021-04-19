/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanostock.app.logic;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 *
 * @author landry john
 */
public class Switcher {
    
    private double width ;

    private double heigth ;

    private ArrayList userSettings ;
    
    /**
     * switch to another scene view
     * @param viewPath
     * @param event
     * @throws IOException 
     */
    public void switchTo( String viewPath, Event event ) throws IOException{
        this.initDefaultSize();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation( getClass().getResource( viewPath ) ) ;
        Parent parent = loader.load();
        this.initDefaultSize();
        Scene scene = new Scene( parent, this.width , this.heigth ) ;
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow(); 
        window.setScene( scene );
        window.setMaximized(true);
        window.show();
        
    }

    public void initSize( double width , double height ){
        this.width = width ;
        this.heigth = height ;
    }

    public double getWidth(){ return this.width ; }

    public double getHeight(){ return this.heigth ; }

    public void initDefaultSize(){

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        this.width = gd.getDisplayMode().getWidth() ;
        this.heigth = gd.getDisplayMode().getHeight() - 60;
    }

//    public void switchTo( String viewPath, Event event, String controllerClassName ,
//                          String initMethod,
//                          Object initParameters  ) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
//        boolean findMethodResult = false ;
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation( getClass().getResource( viewPath ) ) ;
//        Parent parent = loader.load();
//        Scene scene = new Scene( parent ) ;
//        Class classe = Class.forName( controllerClassName ) ;
//        Object viewController = classe.newInstance() ;
//        ArrayList<Method> methodList = new ArrayList(Arrays.asList( classe.getMethods() ) ) ;
//        for( Method method : methodList ){
//            if( method.getName().equals(initMethod) ){
//                findMethodResult = true ;
//            }
//        }
//        if( findMethodResult ){
//        }
//        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//        window.setScene( scene );
//        window.show();
//    }

    public void addUserSettings( String setting){
    
        this.userSettings.add( setting ) ;
        
    }

    /**
     * Generate a simple Alert component
     * @param title
     * @param headerText
     * @param contentText
     */
    public void alert( String title, String headerText, String contentText ){
        Alert loginAlert = new Alert( Alert.AlertType.ERROR );
        loginAlert.setTitle(title);
        loginAlert.setHeaderText(headerText);
        loginAlert.setContentText(contentText);
        loginAlert.showAndWait();
    }

    public void alert( String title, String headerText, String contentText, String textLabel,
                       String textInformation ){
        Alert alert = new Alert( Alert.AlertType.INFORMATION ) ;
        alert.setTitle( title );
        alert.setHeaderText( headerText );
        alert.setContentText( contentText );
        javafx.scene.control.Label label = new javafx.scene.control.Label( textLabel ) ;
        javafx.scene.control.TextArea textArea = new javafx.scene.control.TextArea( textInformation ) ;
        textArea.setEditable( false );
        textArea.setWrapText( true );

        textArea.setMaxWidth( Double.MAX_VALUE);
        textArea.setMaxHeight( Double.MAX_VALUE );
        GridPane.setVgrow( textArea , Priority.ALWAYS);
        GridPane.setHgrow( textArea , Priority.ALWAYS);

        GridPane informationContent = new GridPane() ;
        informationContent.setMaxWidth( Double.MAX_VALUE );
        informationContent.add( label , 0 , 0 ) ;
        informationContent.add( textArea , 0 , 1);
        //set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent( informationContent );

        alert.showAndWait() ;

    }

    /**
     * Generate a simple confirmation Alert component
     * @param title
     * @param headerText
     * @param contentText
     * @return
     */
    public Optional<ButtonType> confirm( String title, String headerText, String contentText ){

        Alert confirmExitAlert = new Alert( Alert.AlertType.CONFIRMATION );
        confirmExitAlert.setTitle(title);
        confirmExitAlert.setHeaderText(headerText);
        confirmExitAlert.setContentText(contentText);

        return confirmExitAlert.showAndWait();

    }

    public Optional<String> textInputField( String title, String headerText, String contentText ){
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle( title );
        dialog.setHeaderText( headerText );
        dialog.setContentText( contentText );
        Optional<String> result = dialog.showAndWait();

        return result ;
    }

    public void alertInfo( String title, String headerText, String contentText ){
        Alert loginAlert = new Alert( Alert.AlertType.INFORMATION );
        loginAlert.setTitle(title);
        loginAlert.setHeaderText(headerText);
        loginAlert.setContentText(contentText);
        loginAlert.showAndWait();
    }

    public Optional<String> choiceDialog( String title , String headerText , String contentText ,
                              String firstChoice , String secondChoice ){
        List<String> choiceOption = new ArrayList<>();
        choiceOption.add( firstChoice );
        choiceOption.add( secondChoice );

        ChoiceDialog<String> dialog = new ChoiceDialog<>(firstChoice , choiceOption);
        dialog.setTitle( title );
        dialog.setHeaderText( headerText );
        dialog.setContentText( contentText );

        Optional<String> result = dialog.showAndWait();

        return result ;

    }

    public String currency( Double currencyDoubleValue ){
        return String.format("%,.0f",currencyDoubleValue);
    }
    
}
