package nanostock.app;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.converter.DoubleStringConverter;
import nanostock.app.logic.Switcher;
import nanostock.model.table.Command;
import nanostock.model.table.CommandItem;
import nanostock.model.table.DAO.FactoryDAO;
import nanostock.model.table.ItemStore;
import nanostock.model.table.Operation;
import nanostock.model.table.generic.WatchDogApp;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class SetCommandItemController extends Switcher implements Initializable {

    @FXML private TableView<ItemStore> itemListTableView ;
    @FXML private TableColumn<ItemStore, Integer> itemCodeCol ;
    @FXML private TableColumn<ItemStore , String> itemReferenceCol ;
    @FXML private TableColumn<ItemStore , Integer> itemQuantityCol ;
    @FXML private TableColumn<ItemStore , Double > itemUnitPriceCol ;
    @FXML private TableColumn<ItemStore , Double > itemWholesalePriceCol ;

    @FXML private TableView<CommandItem> commandItemTableView ;
    @FXML private TableColumn<CommandItem , String > commandItemRefCol ;
    @FXML private TableColumn<CommandItem , Integer > commandItemQtCol ;
    @FXML private TableColumn<CommandItem , Double > commandItemPriceCol ;

    @FXML JFXButton validCommandButton ;
    @FXML JFXButton manageCommandButton ;
    @FXML private TextField searchBar ;

    private Command command ;

//    @FXML private Text commandProperty ;

    private ObservableList<CommandItem> commandItemObservableList ;

    private String clientType = "client simple";

    private int modificationOperationCheck = 0 ;

    private boolean itIsNewCommand ;

    public void initCommandInformation( Command command , boolean isItNewCommand ) throws SQLException {

        this.command = command ;
        this.itIsNewCommand = isItNewCommand ;
        // set validation command button invisible
            this.validCommandButton.setVisible( isItNewCommand );


        if( this.itIsNewCommand ){
            this.manageCommandButton.setDisable( true );
            ArrayList<String> clientData = getClientInformation() ;
            System.out.println("nom : " + clientData.get(0) + "\n address : " + clientData.get(1) );

            FactoryDAO.getCommandDAO().create(
                    new Command( 0 , "now now" ,
                            clientData.get(0) , false , false ,
                            clientData.get(1) ) ) ;

            this.command = FactoryDAO.getCommandDAO().getLast() ;

            Optional<String> result = this.choiceDialog("Type de client" , null ,
                    "Choisir le type de client" , "client simple" ,
                    "client de gros") ;

            if( result.isPresent() ){
                this.clientType = result.get() ;
            }

        }else {
            this.manageCommandButton.setDisable( false );

        }

        this.itemCodeCol.setCellValueFactory( new PropertyValueFactory< ItemStore , Integer >("itemCode") );
        this.itemReferenceCol.setCellValueFactory( new PropertyValueFactory< ItemStore , String >("itemReference") );
        this.itemQuantityCol.setCellValueFactory( new PropertyValueFactory< ItemStore , Integer >("stockQuantity") );
        this.itemUnitPriceCol.setCellValueFactory( new PropertyValueFactory< ItemStore , Double >("unitPrice") );
        this.itemWholesalePriceCol.setCellValueFactory( new PropertyValueFactory< ItemStore , Double >("wholesalePrice") );

        try {
            this.itemListTableView.setItems( getAllItem() );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.commandItemRefCol.setCellValueFactory( new PropertyValueFactory<CommandItem , String >("itemReference") ) ;
        this.commandItemQtCol.setCellValueFactory( new PropertyValueFactory<CommandItem , Integer >("commandQuantity") ) ;
        this.commandItemPriceCol.setCellValueFactory( new PropertyValueFactory<CommandItem , Double >("discountValue") ) ;

        try {
            this.commandItemTableView.setItems( getAllCommandItem() ) ;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.commandItemTableView.setEditable(true) ;
        this.commandItemPriceCol.setCellFactory( TextFieldTableCell.forTableColumn( new DoubleStringConverter() ) );

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // nothing tot do for the moment
    }

    private ObservableList<ItemStore> getAllItem() throws SQLException {
        ObservableList<ItemStore> itemObservableList = FXCollections.observableArrayList() ;
        itemObservableList.addAll(FactoryDAO.getItemStoreDAO().getAllItemStore());
        return itemObservableList ;
    }

    public ObservableList<ItemStore> getAllItem(String target) throws SQLException {
        ObservableList<ItemStore> itemObservableList = FXCollections.observableArrayList() ;
        itemObservableList.addAll(FactoryDAO.getItemStoreDAO().getAllItemStore(target));
        return itemObservableList ;
    }

    private ObservableList<CommandItem> getAllCommandItem() throws SQLException {
        ObservableList<CommandItem> observableList = FXCollections.observableArrayList() ;
        ArrayList<CommandItem> l =  FactoryDAO.getCommandItemDAO().findByCommand( this.command ) ;
        observableList.addAll( l )   ;
        return observableList ;
    }

    public double getTotalCommandPrice() throws SQLException {
        ObservableList<CommandItem>  commandItemList = FXCollections.observableArrayList() ;
        commandItemList = this.getAllCommandItem() ;
        double totalCommandPrice = 0 ;
        for( CommandItem commandItem : commandItemList ){
            totalCommandPrice += commandItem.getCommandQuantity() * commandItem.getDiscountValue() ;
        }
        return totalCommandPrice ;
    }

    public void editDiscountValue(TableColumn.CellEditEvent editedCell ) throws SQLException {
        CommandItem commandItemEdited = this.commandItemTableView.getSelectionModel().getSelectedItem() ;
        commandItemEdited.setDiscountValue( Double.valueOf( editedCell.getNewValue().toString() ) );
        if( Double.valueOf( editedCell.getNewValue().toString() ) > 0 ){
            FactoryDAO.getCommandItemDAO().update( commandItemEdited );
        }else{

            this.commandItemTableView.setItems( this.getAllCommandItem() );
            commandItemTableView.refresh();
            this.alert("Erreur", null, "Erreur de prix :  "
                    + editedCell.getNewValue().toString() + "\nLe prix entré est incorrect" );
        }
    }

    public void itemDetail(MouseEvent event ) throws MalformedURLException {


        Alert  itemAlert = new Alert( Alert.AlertType.INFORMATION );
        itemAlert.setTitle("Détail Article");
        ItemStore selectedItemStore = null ;
        selectedItemStore = this.itemListTableView.getSelectionModel().getSelectedItem() ;

        if( selectedItemStore != null ){

            itemAlert.setHeaderText("Image : ");

            String  itemImagePath = System.getProperty("user.dir").toString()+"\\data\\images\\" + selectedItemStore.getItemImageName();
            System.out.println(Paths.get(itemImagePath).toUri().toURL().toString());
            ImageView itemImageView = new ImageView( Paths.get(itemImagePath).toUri().toURL().toString() ) ;
            javafx.scene.image.Image itemImageIco = new Image( Paths.get(itemImagePath).toUri().toURL().toString()) ;
            itemAlert.setGraphic(itemImageView);
            itemImageView.setFitHeight( 300 );
            itemImageView.setFitWidth( 300 );


            Stage stage = (Stage)itemAlert.getDialogPane().getScene().getWindow();
            stage.getIcons().add( itemImageIco );

            itemAlert.setContentText(
                    "Détails de l'article : " +
                            "\n\nNom : " + selectedItemStore.getItemReference()+
                            "\n\nCategory : " + FactoryDAO.getCategoryDAO().
                            find( selectedItemStore.getCategoryId() ).getCategoryName() +
                            "\n\nQuantité en stock : " + selectedItemStore.getSockQuantity() +
                            "\n\nPrix Unitaire : " + selectedItemStore.getUnitPrice() + " FCFA "+
                            "\n\nPrix De Gros : " + selectedItemStore.getWholesalePrice() + " FCFA "+
                            "\n\nDescription : " + selectedItemStore.getItemDes() +
                            "\n\nQuantité d'Alerte : " + selectedItemStore.getAlertQuantity());

        }

        itemAlert.show();
    }

    private ArrayList<String> getClientInformation(){
        // get client informations
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Information du client");
        dialog.setHeaderText("Saisir les informations du client");
        ButtonType submitButtonType = new ButtonType("valider", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        // Create the clientName and deliveryAddress labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField clientName = new TextField();
        clientName.setPromptText("Nom du client");
        TextField deliveryAddress = new TextField("BOUTIQUE");
        deliveryAddress.setPromptText("Lieu de livraison");

        grid.add(new Label("Client"), 0, 0);
        grid.add( clientName , 1, 0);
        grid.add(new Label("Addresse de livraison"), 0, 1);
        grid.add( deliveryAddress , 1, 1);

        // Enable/Disable login button depending on whether a clientName was entered.
        Node submitButton = dialog.getDialogPane().lookupButton(submitButtonType);
        submitButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        clientName.textProperty().addListener((observable, oldValue, newValue) -> {
            submitButton.setDisable(newValue.trim().isEmpty());
        });

        // Request focus on the username field by default.
        Platform.runLater(() -> clientName.requestFocus());

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a clientName-deliveryAddress-pair when the ok button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                return new Pair<>(clientName.getText(), deliveryAddress.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        ArrayList<String> clientData = new ArrayList<>() ;
        result.ifPresent( clientInformation -> {
            System.out.println("clientName = " + clientInformation.getKey() + ", deleveryAddress = " + clientInformation.getValue());
            clientData.add( clientInformation.getKey() ) ;
            clientData.add( clientInformation.getValue() ) ;
        });

        return clientData ;
    }

    public void commandDetailShow( MouseEvent event ) throws SQLException {
        this.alertInfo("DETAIL DE LA COMMANDE" , null ,
                "\nNom du client : " + this.command.getClientName() +
                        "\n\nType de client : " + this.clientType +
                        "\n\nAdrèsse de livraison : " + this.command.getDeliveryAddress() +
                        "\n\nMontant de la commande : " + this.getTotalCommandPrice() + " FCFA"
                );
    }

    public void addItemToCommandByTab(MouseEvent event ){
        if( event.getClickCount() == 2 ){
            try {
                this.addItemToCommand( event );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addItemToCommand(MouseEvent event) throws SQLException {
        ItemStore selectedItem = null ;
        selectedItem = this.itemListTableView.getSelectionModel().getSelectedItem() ;
        if( selectedItem != null ) {
            Optional<String> result;

            do {
                result = this.textInputField("Saisir la quantité", null,
                        "Saisir la quantité de l'article : ");
            } while (result.isPresent() && Integer.valueOf(result.get()) <= 0);

            if (result.isPresent()) {
                ObservableList<CommandItem>  commandItemList = FXCollections.observableArrayList() ;
                commandItemList = this.getAllCommandItem() ;
                if (selectedItem.getSockQuantity() - Integer.valueOf(result.get()) >= 0) {
                    if (itemPresenceInCommand(commandItemList, selectedItem)) {
                        addItemQuantity(selectedItem, Math.abs(Integer.valueOf(result.get())));
                        FactoryDAO.getCommandItemDAO().update(
                                new CommandItem(selectedItem.getItemCode(),
                                        this.command.getCommandNum(),
                                        Math.abs(Integer.valueOf(result.get())) + oldQuantityValue(selectedItem),
                                        this.clientType.equals("client simple") ?
                                                selectedItem.getUnitPrice() :
                                                selectedItem.getWholesalePrice()
                                ));
                        selectedItem.setStockQuantity(
                                selectedItem.getSockQuantity() - Math.abs(Integer.valueOf(result.get())));
                        FactoryDAO.getItemStoreDAO().update(selectedItem);
                        this.itemListTableView.refresh();
                        this.commandItemTableView.refresh();
                    } else {


                        FactoryDAO.getCommandItemDAO().create(
                                new CommandItem(selectedItem.getItemCode(),
                                        this.command.getCommandNum(),
                                        Math.abs(Integer.valueOf(result.get())),
                                        this.clientType.equals("client simple") ?
                                                selectedItem.getUnitPrice() :
                                                selectedItem.getWholesalePrice()
                                ));
                        this.commandItemTableView.setItems(this.getAllCommandItem());
                        this.commandItemTableView.refresh();
                        selectedItem.setStockQuantity(
                                selectedItem.getSockQuantity() - Math.abs(Integer.valueOf(result.get())));
                        FactoryDAO.getItemStoreDAO().update(selectedItem);
                        this.itemListTableView.setItems(getAllItem());
                        this.modificationOperationCheck++;
                    }
                }
                else {
                    this.alert("Stock insufisant", null,
                            "La quantité en stock est : " + selectedItem.getStockQuantity() +
                                    "\nLa quantité demandée est : " + Math.abs(Integer.valueOf(result.get())) +
                                    "\nStock insufisant");
                }

            }
        }
    }

//    public void itemDetail(MouseEvent event ){
//        ItemStore selectedItem = null ;
//        selectedItem = this.itemListTableView.getSelectionModel().getSelectedItem() ;
//        if( selectedItem != null ){
//            this.alertInfo("Information Article" , null ,
//                    "Réference : " + selectedItem.getItemReference() +
//                    "\n\nDescription : " + selectedItem.getItemDes() +
//                    "\n\nQuantité en stock : " + selectedItem.getSockQuantity() +
//                    "\n\nPrix de Gros : " + selectedItem.getWholesalePrice() +
//                    "\n\nPrix Unitaire : " + selectedItem.getUnitPrice() );
//        }
//    }

    //cheek the presence of an item in the CommandItemTableView
    private boolean itemPresenceInCommand(ObservableList<CommandItem> command, ItemStore item)
    {
        boolean result = false;
        for (CommandItem indice : command)
        {
            if(item.getItemCode() == indice.getItemCode() )
            {
                result = true;
            }
        }
        return  result;
    }

    // modified the quantity of an item in the CommandItemTableView

    private void addItemQuantity(ItemStore item, int quantity)throws SQLException
    {
        ObservableList<CommandItem>  commandItemList = FXCollections.observableArrayList() ;
        commandItemList = this.getAllCommandItem() ;

        for (CommandItem indice : commandItemList)
        {
            if(item.getItemCode() == indice.getItemCode() )
            {
                indice.setCommandQuantity(quantity+indice.getCommandQuantity());
            }
        }
        this.commandItemTableView.setItems( commandItemList ) ;
    }
    private int oldQuantityValue(ItemStore item)throws SQLException
    {
        int result = 0;
        ObservableList<CommandItem>  commandItemList = FXCollections.observableArrayList() ;
        commandItemList = this.getAllCommandItem() ;

        for (CommandItem commandItem : commandItemList)
        {
            if(item.getItemCode() == commandItem.getItemCode() )
            {
                result = commandItem.getCommandQuantity();
            }
        }
        return result;
    }


    public void removeItem(MouseEvent event ) throws SQLException {
        CommandItem selectedCommandItem = null ;
        selectedCommandItem = this.commandItemTableView.getSelectionModel().getSelectedItem() ;
        if( selectedCommandItem != null ){
            FactoryDAO.getCommandItemDAO().delete( selectedCommandItem );
            this.itemListTableView.setItems( getAllItem() );
            this.itemListTableView.refresh();
            this.commandItemTableView.setItems( getAllCommandItem() );
            this.commandItemTableView.refresh();
        }
    }

    // search implementation

    public void searchKeyReleased() throws SQLException {

        String buttonBarValue = searchBar.getText().trim();
        this.itemListTableView.setItems( getAllItem(buttonBarValue) );
    }







    public void goToCommandPanelView( MouseEvent event ) throws IOException {
        if( this.modificationOperationCheck > 0 && this.itIsNewCommand &&
                ! this.command.getCommandValidity() ){

            Optional<ButtonType> result = this.confirm("Validation des modifications" , null ,
                    "Les modifications ne seront pas pris en compte. " +
                            "\nVoulez vous vraiment quitter ?") ;
            if( result.isPresent() ){
                if( result.get() == ButtonType.OK ){
                    this.switchTo( "view/commandPanelView.fxml" , event  );
                }
            }

        }else{
            this.switchTo( "view/commandPanelView.fxml" , event  );
        }

    }

    public void validNewCommand( MouseEvent event ){
        this.command.setCommandValidity( true ) ;
        FactoryDAO.getCommandDAO().update( this.command ) ;
        this.manageCommandButton.setDisable( false );
                FactoryDAO.getOperationDAO().create(new Operation( WatchDogApp.getUserConnected().getUserId() ,
                "Ajout de la commande du client " + this.command.getClientName() +
                " | statut : non payée/=/1") ) ;

    }

    public void goToSetCommandBill( MouseEvent event ) throws IOException,SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation( getClass().getResource("view/setCommandBillView.fxml") ) ;
        Parent setCommandBillViewParent = loader.load();
        this.initDefaultSize();
        Scene setCommandBillViewScene = new Scene( setCommandBillViewParent, this.getWidth(), this.getHeight()) ;
//        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//        double width = gd.getDisplayMode().getWidth() - 50;
//        double height = gd.getDisplayMode().getHeight() - 80;
        SetCommandBillController setCommandBillController = (SetCommandBillController)loader.getController();
        setCommandBillController.initializeCommand( this.command  );
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene( setCommandBillViewScene );
        window.setMaximized(true);
        window.show();
    }



}
