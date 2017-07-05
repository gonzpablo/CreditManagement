package com.cred.controller;

import java.io.IOException;
import com.cred.model.ClienteModel;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class ClienteController {

	@FXML
	private TextField clienteNombreField;
	
	@FXML
	private TextField clienteApellidoField;
	
	@FXML
	private TextField clienteDniField;

	@FXML
	private TextField clienteDireccionField;
	
	@FXML
	private TextField clienteTelefonoField;

	@FXML
	private Button clienteGuardarButton;

	@FXML
	private TableView<ClienteModel> clientesTable;	
	@FXML
	private TableColumn<ClienteModel, String> clienteNombreColumn;
	@FXML
	private TableColumn<ClienteModel, String> clienteApellidoColumn;
	@FXML
	private TableColumn<ClienteModel, String> clienteDniColumn;
	@FXML
	private TableColumn<ClienteModel, String> clienteDireccionColumn;
	@FXML
	private TableColumn<ClienteModel, String> clienteTelefonoColumn;
	
//    ObservableList<ClientModel> clientes = FXCollections.observableArrayList();	
	ObservableList<ClienteModel> clientes;	
	
	public ClienteController() {
//		clientes.add(new ClientModel("aa","bb","cc","dd","ee"));
	}
	
	@FXML
	private void initialize() {

		initColumns();		
		
//		clientesTable.setItems(clientes);
			
		clienteGuardarButton.setOnAction((event) -> {
		    guardarCliente();
		    initFields();
		});		
		
		
//		Doble-click        
        clientesTable.setRowFactory( tv -> {

            TableRow<ClienteModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {

                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    ClienteModel rowData = row.getItem();
//                    System.out.println(rowData.getCliente());

                    clienteView(rowData, row);
                }
            });
            return row;
        });		
		
		
	}

	private void clienteView(ClienteModel rowData, TableRow<ClienteModel> row) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../view/ClienteCreditos.fxml"));
            GridPane page = (GridPane) loader.load();
            ClienteCreditosController controller = loader.<ClienteCreditosController>getController();

//            controller.setCredito(rowData, creditos);
            controller.setCliente(rowData);
//            controller.setMainController(this);            
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Creditos de Cliente");
          
            Scene scene = new Scene(page);

            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }		
		
	}

	private void guardarCliente() {
    	addItemToList(new ClienteModel(clienteNombreField.getText(), clienteApellidoField.getText(), 
				  clienteDireccionField.getText(), clienteTelefonoField.getText(), 
				  clienteDniField.getText()));
	}

    private void addItemToList(ClienteModel cliente) {
    	clientes.add(cliente);
//    	clientesTable.setItems(getClientes());
    }	

//	private ObservableList<ClienteModel> getClientes() {
//		return clientes;
//	}
	
	private void initColumns() {
		clienteNombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));		
        clienteApellidoColumn.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        clienteDniColumn.setCellValueFactory(new PropertyValueFactory<>("dni"));
        clienteDireccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        clienteTelefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));        
	}

	private void initFields() {
		clienteNombreField.clear();
		clienteApellidoField.clear();
		clienteDireccionField.clear();
		clienteDniField.clear();
		clienteTelefonoField.clear();
	}

	public void setClientes(ObservableList<ClienteModel> clientes) {
		this.clientes = clientes;
		clientesTable.setItems(clientes);
	}	
}