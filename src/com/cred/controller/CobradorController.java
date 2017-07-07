package com.cred.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.cred.model.ClienteDAO;
import com.cred.model.ClienteModel;
import com.cred.model.CobradorModel;
import com.cred.model.CreditoModel;
import com.cred.util.DBUtil;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class CobradorController {

	@FXML
	private TextField cobradorNombreField;
	
	@FXML
	private TextField cobradorApellidoField;
	
	@FXML
	private TableView<CobradorModel> cobradoresTable;	
	@FXML
	private TableColumn<CobradorModel, String> cobradorNombreColumn;
	@FXML
	private TableColumn<CobradorModel, String> cobradorApellidoColumn;
		
	@FXML
	private Button cobradorGuardarButton;
	@FXML
	private Button cobradorNuevoButton;
	@FXML
	private Button cobradorBorrarButton;		
	
	
	
	private ObservableList<CobradorModel> cobradores;
	
//	Referencia al cliente que se está editando (Nuevo o modificación)
	private CobradorModel cobrador = null;
	
	
	public CobradorController() {}
	
	@FXML
	private void initialize() {

		initColumns();		

		cobradorGuardarButton.setOnAction( event -> {
		    guardarCobrador();
		    initFields();
        	grisarCampos(true);
		});		
		
		cobradorNuevoButton.setOnAction( event -> { 
			nuevoCobrador();
		});
		
		cobradorBorrarButton.setOnAction( event -> { borrarCobrador(); });
		
//		Doble-click        
        cobradoresTable.setRowFactory( tv -> {

            TableRow<CobradorModel> row = new TableRow<>();
            
            row.setOnMouseClicked( event -> {

            	if (event.getClickCount() > 2 )
            		return;

                if (row.isEmpty())
                	return;

                cobradorBorrarButton.setDisable(false);
                
                CobradorModel rowData = row.getItem();

                switch (event.getClickCount()) {

	                case 1:
	                	grisarCampos(true);
	                	cargarCobrador(rowData);
	                	break;
	                case 2:
//	                	clienteView(rowData);
	                	grisarCampos(false);
	                	cargarCobrador(rowData);
	                	break;
                }                       
            });
            return row;
        });
        
        if (cobradoresTable.getSelectionModel().getSelectedItem() == null)
        	cobradorBorrarButton.setDisable(true);	
        
	}

	private void nuevoCobrador() {
		grisarCampos(false);
		initFields();
		this.cobrador = null;		
	}

	private void cargarCobrador(CobradorModel cobrador) {
		
		this.cobrador = cobrador;
		cobradorNombreField.setText(cobrador.getNombre());		
		cobradorApellidoField.setText(cobrador.getApellido());
	}

	private void grisarCampos(Boolean value) {
		cobradorNombreField.setDisable(value);		
		cobradorApellidoField.setDisable(value);
		
		cobradorGuardarButton.setDisable(value);
	}		

	private void guardarCobrador() {
		
		if (this.cobrador == null) {
			CobradorModel cobradorNew = new CobradorModel(
					  cobradorNombreField.getText(), cobradorApellidoField.getText()); 
			
	    	try {
				CobradorDAO.agregarCliente(clienteNew);
				cobradorNew.setId(DBUtil.getLastRowId("clientes"));
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
		cobradorNombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());		
        cobradorApellidoColumn.setCellValueFactory(cellData -> cellData.getValue().apellidoProperty());
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
//		credito.calcular();
		cliente.borrar();
		initFields();		
		
//        TableRow<ClienteModel> row = new TableRow<>();
//        
//        row.setOnMouseClicked( event -> {
//
//        	if (event.getClickCount() > 2 )
//        		return;
//
//            if (row.isEmpty())
//            	return;
//
//            clienteBorrarButton.setDisable(false);
//            
//            ClienteModel rowData = row.getItem();
//
//            switch (event.getClickCount()) {
//
//                case 1:
//                	grisarCampos(true);
		cliente = clientesTable.getSelectionModel().getSelectedItem();
		if (cliente == null)
			return;
                	cargarCliente(cliente);
//                	break;		
		
	}
}