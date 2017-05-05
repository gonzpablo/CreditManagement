package cred;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
//import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class CreditListView extends Application implements EventHandler<ActionEvent> {
	Button btnCredito;
	Button btnCliente;
	
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
//        setUserAgentStylesheet(STYLESHEET_CASPIAN);

        // Cliente
        TableColumn<CreditoModel, String> clienteColumn = new TableColumn<>("Cliente");
        clienteColumn.setMinWidth(100);
        clienteColumn.setCellValueFactory(new PropertyValueFactory<>("cliente"));

        // Monto Cuota
        TableColumn<CreditoModel, Float> montoCuota = new TableColumn<>("Monto Cuota");
        montoCuota.setMinWidth(80);
        montoCuota.setCellValueFactory(new PropertyValueFactory<>("montoCuota"));

        // Ganancia x día
        TableColumn<CreditoModel, Float> gciaXDia = new TableColumn<>("Gcia. X día");
        gciaXDia.setMinWidth(60);
        gciaXDia.setCellValueFactory(new PropertyValueFactory<>("gciaXDia"));
                
        // Saldo de capital
        TableColumn<CreditoModel, Float> saldoCapital = new TableColumn<>("Saldo Capital");
        saldoCapital.setMinWidth(60);
        saldoCapital.setCellValueFactory(new PropertyValueFactory<>("saldoCapital"));

        // Monto Crédito
        TableColumn<CreditoModel, Float> montoCredito = new TableColumn<>("Monto Crédito");
        montoCredito.setMinWidth(100);
        montoCredito.setCellValueFactory(new PropertyValueFactory<>("montoCredito"));        
        
        // Cuotas pagas
        TableColumn<CreditoModel, Integer> cuotasPagas = new TableColumn<>("Cuotas Pagas");
        cuotasPagas.setMinWidth(40);
        cuotasPagas.setCellValueFactory(new PropertyValueFactory<>("cuotasPagas"));
        
        // Cantidad de días
        TableColumn<CreditoModel, Integer> cantDias = new TableColumn<>("Cant. Días");
        cantDias.setMinWidth(40);
        cantDias.setCellValueFactory(new PropertyValueFactory<>("cantDias"));        
        
        // Cobrador
        TableColumn<CreditoModel, String> cobrador = new TableColumn<>("Cobrador");
        cobrador.setMinWidth(40);
        cobrador.setCellValueFactory(new PropertyValueFactory<>("cobrador"));        
        
        table = new TableView<>();
        table.setItems(initCreditos());
        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setRowFactory( tv -> {
        	
            TableRow<CreditoModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
            	
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    CreditoModel rowData = row.getItem();
//                    System.out.println(rowData.getCliente());
                    
                    visualizarCredito(rowData, row);
                }
            });
            return row;
        });        
                
        table.getColumns().addAll(clienteColumn, montoCuota,
        						  gciaXDia, 
        						  cuotasPagas, cantDias, saldoCapital, 
        						  montoCredito, cobrador);

        VBox vBox = new VBox();

//        vBox.setStyle("-fx-padding: 10;" +
////              "-fx-border-style: solid inside;" +
//              "-fx-border-width: 2;" +
//              "-fx-border-insets: 5;" +
//              "-fx-border-radius: 5;" );        
        vBox.setSpacing(10);
//        vBox.setPadding(new Insets(0, 5, 5, 5)); 
        
        btnCredito = new Button("Cargar Crédito");        
        btnCliente = new Button("Gestionar Clientes");
        
        btnCredito.setMaxWidth(150);
        btnCliente.setMaxWidth(150);
        
//        HBox hBox = new HBox();
//        hBox.getChildren().addAll(btnCredito, btnCliente);
        
        vBox.getChildren().addAll(table, btnCredito, btnCliente);        
      vBox.setAlignment(Pos.TOP_CENTER);        
        btnCredito.setOnAction(this);      
        btnCliente.setOnAction(this);

//        hBox.setHalignment(btnCredito, HPos.CENTER);        
//        GridPane.setHalignment(btnCliente, HPos.CENTER);        
        
        scene = new Scene(vBox);
//        vBox.setAlignment(Pos.CENTER);


//        vBox.setPadding(Insets.EMPTY);
        
//        primaryStage.getIcons().add(new Image("file:1493858779_Business.png"));
        primaryStage.getIcons().add(new Image("file:1493859896_Timetable.png"));        
        
//        @FXML private AnchorPane ap;
//        Stage stage = (Stage) ap.getScene().getWindow();

        window.setScene(scene);
        window.setHeight(520);window.setWidth(900);
        window.show();
    }
    
    private void visualizarCredito(CreditoModel rowData, TableRow<CreditoModel> row) {

    	CreditPayView credit = new CreditPayView(this);    		
    	credit.setCredito(rowData, row);
    	
    	credit.camposPropios();

	}

	//When button is clicked, handle() gets called
    //Button click is an ActionEvent (also MouseEvents, TouchEvents, etc...)
    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == btnCredito) { 
        	CreditCreateView credit = new CreditCreateView(this);      	
        } else if (event.getSource() == btnCliente) {
        	ClientView cliente = new ClientView();
        }        
    }
    
    //Get all of the products
    public ObservableList<CreditoModel> initCreditos(){

    	creditos.add(new CreditoModel("Carlos", 29, 45, 3000, 150, 45, 3000, "Luis"));
    	creditos.add(new CreditoModel("Juan", 15, 45, 2000, 150, 45, 2000, "Miguel"));
    	
        return creditos;
    }

    public ObservableList<CreditoModel> getCreditos(){
      return creditos;
    }
    
    public void addItemToList(CreditoModel cred) {
    	creditos.add(cred);
    	table.setItems(getCreditos());
    }
}