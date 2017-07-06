package com.cred.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.cred.model.ClienteDAO;
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
	private Button clienteNuevoButton;
	@FXML
	private Button clienteBorrarButton;		
	
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
		
	private ObservableList<ClienteModel> clientes;
	
//	Referencia al cliente que se está editando (Nuevo o modificación)
	private ClienteModel cliente = null;
	
	
	public ClienteController() {}
	
	@FXML
	private void initialize() {

		initColumns();		
			
		clienteGuardarButton.setOnAction( event -> {
		    guardarCliente();
		    initFields();
        	grisarCampos(true);
		});		
		
		clienteNuevoButton.setOnAction( event -> { 
			nuevoCliente();

		});
		
		clienteBorrarButton.setOnAction( event -> { borrarCliente(); });
		
//		Doble-click        
        clientesTable.setRowFactory( tv -> {

            TableRow<ClienteModel> row = new TableRow<>();
            
            row.setOnMouseClicked( event -> {

            	if (event.getClickCount() > 2 )
            		return;

                if (row.isEmpty())
                	return;

                ClienteModel rowData = row.getItem();

                switch (event.getClickCount()) {

	                case 1:
	                	grisarCampos(true);	                	
	                	cargarCliente(rowData);
	                	break;
	                case 2:
//	                	clienteView(rowData);
	                	grisarCampos(false);
	                	cargarCliente(rowData);
	                	break;
                }                       
            });
            return row;
        });		
	}

	private void nuevoCliente() {
		grisarCampos(false);
		initFields();
		this.cliente = null;		
	}

	private void borrarCliente() {
		// TODO Auto-generated method stub
		
	}

	private void cargarCliente(ClienteModel cliente) {
		
		this.cliente = cliente;
		clienteNombreField.setText(cliente.getNombre());		
		clienteApellidoField.setText(cliente.getApellido());
		clienteDniField.setText(cliente.getDni());
		clienteDireccionField.setText(cliente.getDireccion());
		clienteTelefonoField.setText(cliente.getTelefono());
	}

	private void grisarCampos(Boolean value) {
		clienteNombreField.setDisable(value);		
		clienteApellidoField.setDisable(value);
		clienteDniField.setDisable(value);
		clienteDireccionField.setDisable(value);
		clienteTelefonoField.setDisable(value);		
	}	
	
	private void clienteView(ClienteModel rowData) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../view/ClienteCreditos.fxml"));
            GridPane page = (GridPane) loader.load();
            ClienteCreditosController controller = loader.<ClienteCreditosController>getController();

            controller.setCliente(rowData);       
            
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
		
		if (this.cliente == null) {
			ClienteModel clienteNew = new ClienteModel(
					  clienteNombreField.getText(), clienteApellidoField.getText(), 
					  clienteDireccionField.getText(), clienteTelefonoField.getText(), 
					  clienteDniField.getText()); 
		
			addItemToList(clienteNew);
			
	    	try {
				ClienteDAO.agregarCliente(clienteNew);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}    	
			
		} else {
			try {
				
				this.cliente.setNombre(clienteNombreField.getText());
				this.cliente.setApellido(clienteApellidoField.getText());
				this.cliente.setDireccion(clienteDireccionField.getText());
				this.cliente.setTelefono(clienteTelefonoField.getText());
				this.cliente.setDni(clienteDniField.getText());
				
				ClienteDAO.modificarCliente(cliente);			
				
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}			
		}  	
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