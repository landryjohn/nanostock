package nanostock;

import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author landry john
 */
public class Nanostock extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("app/view/loginView.fxml"));
        StageStyle stageStyle = StageStyle.DECORATED ;

        Scene scene = new Scene( root );
        stage.setTitle("LENINE CONSTRUCTION");
        stage.initStyle(stageStyle);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException {
        
        launch(args);
    }
    
}


/**
 * Test section 
 */
//
//import java.sql.SQLException; 
//
//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//import nanostock.PrintReport;
//import net.sf.jasperreports.engine.JRException;
//
//public class Nanostock extends Application {
//
//Stage window;
//Scene scene;
//Button button;
//
//public static void main(String[] args) {
//    launch(args);
//}
//
//@Override
//public void start(Stage primaryStage) throws Exception {
//    window = primaryStage;
//    window.setTitle("Jasper Report Tutorial");
//    button = new Button("Show Receipt");
//    button.setOnAction(e -> {
//    try {
//        // --- Show Jasper Report on click-----
//        new PrintReport().showReport();
//    }catch( ClassNotFoundException | JRException | SQLException e1) {
//        e1.printStackTrace();
//       }
//        });
//
//    VBox layout = new VBox(10);
//    layout.setPadding(new Insets(20, 20, 20, 20));
//    layout.getChildren().addAll(button);
//    scene = new Scene(layout, 300, 250);
//    window.setScene(scene);
//    window.show();
//    }
//
//}