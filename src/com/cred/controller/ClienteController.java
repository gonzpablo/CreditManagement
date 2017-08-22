package com.cred.controller;

import java.sql.SQLException;

import com.cred.model.ClienteDAO;
import com.cred.model.ClienteModel;
import com.cred.util.DBUtil;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;

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
	private TextField nombreFilterField;	
	
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

                clienteBorrarButton.setDisable(false);
                
                ClienteModel rowData = row.getItem();

                switch (event.getClickCount()) {

	                case 1:
	                	grisarCampos(true);
	                	cargarCliente(rowData);
	                	break;
	                case 2:
	                	grisarCampos(false);
	                	cargarCliente(rowData);
	                	break;
                }                       
            });
            return row;
        });
        
        if (clientesTable.getSelectionModel().getSelectedItem() == null)
        	clienteBorrarButton.setDisable(true);
	}

	private void nuevoCliente() {
		grisarCampos(false);
		initFields();
		this.cliente = null;		
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
		clienteGuardarButton.setDisable(value);
	}

	private void guardarCliente() {

		if (!validar())
			return;

		if (this.cliente == null) {
			ClienteModel clienteNew = new ClienteModel(
					  clienteNombreField.getText(), clienteApellidoField.getText(),
					  clienteDireccionField.getText(), clienteTelefonoField.getText(),
					  clienteDniField.getText());

	    	try {
				ClienteDAO.agregarCliente(clienteNew);
				clienteNew.setId(DBUtil.getLastRowId("clientes"));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}    	
			
			addItemToList(clienteNew);
			
		} else {
			try {
				
				this.cliente.setNombre(clienteNombreField.getText());
				this.cliente.setApellido(clienteApellidoField.getText());
				this.cliente.setDireccion(clienteDireccionField.getText());
				this.cliente.setTelefono(clienteTelefonoField.getText());
				this.cliente.setDni(clienteDniField.getText());
				
				ClienteDAO.modificarCliente(cliente);			
				this.cliente = null;
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}			
		}  	
	}

    private void addItemToList(ClienteModel cliente) {
    	clientes.add(cliente);
    }	
	
	private void initColumns() {
		clienteNombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());		
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

	private void borrarCliente() {
		
		ClienteModel cliente = clientesTable.getSelectionModel().getSelectedItem();
	
		if (cliente == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error al borrar un cliente");
			alert.setContentText("Por favor seleccione un cliente a borrar");
			alert.showAndWait();									
			return;
		}
			
		if (cliente.tieneCreditos()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error al borrar un cliente");
			alert.setContentText("Por favor elimine los creditos del cliente");
			alert.showAndWait();									
			return;
		}
		
		clientes.remove(cliente);
		cliente.borrar();
		initFields();		
		
		cliente = clientesTable.getSelectionModel().getSelectedItem();
		if (cliente == null)
			return;
		
        cargarCliente(cliente);
	}
	
	private boolean validar() {
		
		if (clienteNombreField.getText().equals("") && clienteApellidoField.getText().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Error");
			alert.setContentText("Por favor ingrese Nombre y Apellido");
			alert.showAndWait();			
			return false;
		} 		
	
		return true;
	}
	
	public void setClientes(ObservableList<ClienteModel> clientes) {
		this.clientes = clientes;
		clientesTable.setItems(clientes);
		
		FilteredList<ClienteModel> filteredData = new FilteredList<>(clientes, p -> true);
		
		nombreFilterField.textProperty().addListener((observable, oldValue, newValue) -> {
		
            filteredData.setPredicate(cliente -> {
                // Si el filtro está vacío, mostrar todos los clientes
                if (newValue == null || newValue.isEmpty())
                    return true;

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (cliente.getNombre().toLowerCase().contains(lowerCaseFilter)	||
                	cliente.getApellido().toLowerCase().contains(lowerCaseFilter))
                	
                    return true; // Filter matches first name.
                else
                	return false; // Does not match.
            });
        });		
		
        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<ClienteModel> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(clientesTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        clientesTable.setItems(sortedData);			
	}
}