package nanostock.app;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import nanostock.app.logic.Switcher;
import nanostock.model.table.Category;

import java.net.URL;
import java.util.ResourceBundle;


public class CategoryViewController extends Switcher implements Initializable {

    @FXML private int categoryId ;
    @FXML private JFXTextField categoryNameField ;
    @FXML private JFXTextArea categoryDescriptionArea ;
    @FXML private JFXTextField categoryAlertValueField ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * initialize value of the view components
     * @param category
     */
    public void initData(Category category ){
        this.categoryId = category.getCategoryId() ;
    }

}
