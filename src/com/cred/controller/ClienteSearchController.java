package com.cred.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import com.cred.model.ClienteModel;

public class ClienteSearchController {

	@FXML
	private TextField nombreFilterField;
	@FXML
	private Button limpiarFiltrosButton;
		
	@FXML
	private TableColumn<ClienteModel, String> nombreColumn;
	@FXML
	private TableColumn<ClienteModel, String> apellidoColumn;
	@FXML
	private TableColumn<ClienteModel, String> direccionColumn;
	@FXML
	private TableColumn<ClienteModel, String> telefonoColumn;
	@FXML
	private TableColumn<ClienteModel, String> dniColumn;
	@FXML
	private TableView<ClienteModel> clientesTable;
	
	private CreditoController creditoController;
		
	private ObservableList<ClienteModel> clientes;
	
	
	public ClienteSearchController() {	}
	
	@FXML
	private void initialize() {	
		initColumns();
		
//		Doble-click        
        clientesTable.setRowFactory( tv -> {

            TableRow<ClienteModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {

                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    ClienteModel rowData = row.getItem();
                    setCliente(rowData, row);
                }
            });
            return row;
        });
	}

	private void setCliente(ClienteModel rowData, TableRow<ClienteModel> row) {
		creditoController.setCliente(rowData);		

	    Stage stage = (Stage) row.getScene().getWindow();

	    stage.close();
	}

	private void initColumns() {
		nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));		
		apellidoColumn.setCellValueFactory(new PropertyValueFactory<>("apellido"));
		direccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));
		telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
		dniColumn.setCellValueFactory(new PropertyValueFactory<>("dni"));
	}

	public void setClientes(ObservableList<ClienteModel> clientes) {
		this.clientes = clientes;
		clientesTable.setItems(this.clientes);
	}

	public void setMainController(CreditoController creditoController) {
		this.creditoController = creditoController;		
	}
}