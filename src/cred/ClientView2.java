package cred;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ClientView2 extends Application implements EventHandler<ActionEvent> {
	Button btnCredito;
    Stage window;
    TableView<CreditoModel> table;
    Scene scene;
    Stage secondStage;
    ObservableList<CreditoModel> creditos = FXCollections.observableArrayList();
    
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Gestión de créditos");

        //Name column
        TableColumn<CreditoModel, String> clienteColumn = new TableColumn<>("Cliente");
        clienteColumn.setMinWidth(200);
        clienteColumn.setCellValueFactory(new PropertyValueFactory<>("cliente"));

        //Price column
        TableColumn<CreditoModel, Float> montoCuota = new TableColumn<>("Monto Cuota");
        montoCuota.setMinWidth(100);
        montoCuota.setCellValueFactory(new PropertyValueFactory<>("montoCuota"));

        //Quantity column
        TableColumn<CreditoModel, Float> tasaInt = new TableColumn<>("Tasa");
        tasaInt.setMinWidth(100);
        tasaInt.setCellValueFactory(new PropertyValueFactory<>("tasaInt"));

        table = new TableView<>();
        table.setItems(initCreditos());
        table.getColumns().addAll(clienteColumn, montoCuota, tasaInt);

        VBox vBox = new VBox();
        btnCredito = new Button("Cargar Crédito");
        vBox.getChildren().addAll(table, btnCredito);
        
        btnCredito.setOnAction(this);      
        
        scene = new Scene(vBox);
        window.setScene(scene);
        window.setHeight(600);window.setWidth(600);
        window.show();
    }
    
    //When button is clicked, handle() gets called
    //Button click is an ActionEvent (also MouseEvents, TouchEvents, etc...)
    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == btnCredito) { 
        	CreditView credit = new CreditView(this);      	
        } else {
//        	secondStage.close();
        }        
    }
    
    //Get all of the products
    public ObservableList<CreditoModel> initCreditos(){
//        ObservableList<Product> products = FXCollections.observableArrayList();
//        products.add(new Product("Laptop", 859.00, 20));
//        products.add(new Product("Bouncy Ball", 2.49, 198));
//        products.add(new Product("Toilet", 99.00, 74));
//        products.add(new Product("The Notebook DVD", 19.99, 12));
//        products.add(new Product("Corn", 1.49, 856));
//    	public CreditoModel(String cliente, int cantDias, float tasaInt, float montoCredito, float montoCuota, float gciaXDia) {
    	creditos.add(new CreditoModel("Carlos", 29, 45, 3000, 150, 45));
    	creditos.add(new CreditoModel("Juan", 29, 45, 3000, 150, 45));
        return creditos;
    }

    public ObservableList<CreditoModel> getCreditos(){
//      ObservableList<Product> products = FXCollections.observableArrayList();
//      products.add(new Product("Laptop", 859.00, 20));
//      products.add(new Product("Bouncy Ball", 2.49, 198));
//      products.add(new Product("Toilet", 99.00, 74));
//      products.add(new Product("The Notebook DVD", 19.99, 12));
//      products.add(new Product("Corn", 1.49, 856));
  	
//  	creditos.add(new CreditoModel("Pepe", 10, 10, 10, 10, 10));
      return creditos;
  }

    
    public void addItemToList(CreditoModel cred) {
    	creditos.add(cred);
    	table.setItems(getCreditos());
    }
}
