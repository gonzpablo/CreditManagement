package com.cred.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import com.cred.model.ClienteModel;

public class ClienteSearchController {

	@FXML
	private TextField nombreFilterField;
		
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
		
	private ObservableList<ClienteModel> clientes = FXCollections.observableArrayList();;

	
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
		nombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());		
		apellidoColumn.setCellValueFactory(cellData -> cellData.getValue().apellidoProperty());
		direccionColumn.setCellValueFactory(cellData -> cellData.getValue().direccionProperty());
		telefonoColumn.setCellValueFactory(cellData -> cellData.getValue().telefonoProperty());
		dniColumn.setCellValueFactory(cellData -> cellData.getValue().dniProperty());
	}

	public void setClientes(ObservableList<ClienteModel> clientes) {
		this.clientes = clientes;
		clientesTable.setItems(this.clientes);
		
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

	public void setMainController(CreditoController creditoController) {
		this.creditoController = creditoController;		
	}
}