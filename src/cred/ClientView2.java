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
	Button buttonOK;
    Stage window;
    TableView<Product> table;
    Scene scene;
    Stage secondStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Gestión de créditos");

        //Name column
        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        //Price column
        TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Quantity column
        TableColumn<Product, String> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setMinWidth(100);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        table = new TableView<>();
        table.setItems(getProduct());
        table.getColumns().addAll(nameColumn, priceColumn, quantityColumn);

        VBox vBox = new VBox();
        buttonOK = new Button("OK");
        vBox.getChildren().addAll(table, buttonOK);
        
        buttonOK.setOnAction(this);      
        
        scene = new Scene(vBox);
        window.setScene(scene);
        window.setHeight(600);window.setWidth(600);
        window.show();
    }
    
    //When button is clicked, handle() gets called
    //Button click is an ActionEvent (also MouseEvents, TouchEvents, etc...)
    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == buttonOK) { 
        	CreditView credit = new CreditView(this);      	
        } else {
//        	secondStage.close();
        }        
    }
    
    //Get all of the products
    public ObservableList<Product> getProduct(){
        ObservableList<Product> products = FXCollections.observableArrayList();
        products.add(new Product("Laptop", 859.00, 20));
        products.add(new Product("Bouncy Ball", 2.49, 198));
        products.add(new Product("Toilet", 99.00, 74));
        products.add(new Product("The Notebook DVD", 19.99, 12));
        products.add(new Product("Corn", 1.49, 856));
        return products;
    }
    
    public void addItemToList(Product prod) {
    	
    }
}
