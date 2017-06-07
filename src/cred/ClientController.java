package cred;

import java.awt.event.ActionEvent;
import java.time.LocalDate;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class ClientController {

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
	private TableView<ClientModel> clientesTable;	
	@FXML
	private TableColumn<ClientModel, String> clienteNombreColumn;
	@FXML
	private TableColumn<ClientModel, String> clienteApellidoColumn;
	@FXML
	private TableColumn<ClientModel, String> clienteDniColumn;
	@FXML
	private TableColumn<ClientModel, String> clienteDireccionColumn;
	@FXML
	private TableColumn<ClientModel, String> clienteTelefonoColumn;
	
    ObservableList<ClientModel> clientes = FXCollections.observableArrayList();	
	
	public ClientController () {
		
	}
	
	@FXML
	private void initialize() {

		initColumns();		
		
//		fechaPagoField.setValue(pago.getFecha());
//		
//		pagosTable.setItems(pagos);
//
//		montoPagadoField.setOnKeyReleased( (event) -> {			
//			actualizarCuotasPagadasField();
//		});
//		
//		cuotasPagadasField.setOnKeyReleased( (event) -> {
//			actualizarMontoPagadoField();
//		});
		
		clienteGuardarButton.setOnAction((event) -> {
		    guardarCliente();
		});		
	}

	private void guardarCliente() {
    	addItemToList(new ClientModel(clienteNombreField.getText(), clienteApellidoField.getText(), 
				  clienteDireccionField.getText(), clienteTelefonoField.getText(), 
				  clienteDniField.getText()));
	}

    private void addItemToList(ClientModel cliente) {
    	clientes.add(cliente);
    	clientesTable.setItems(getClientes());
    }	

	private ObservableList<ClientModel> getClientes() {
		return clientes;
	}
	
	private void initColumns() {
		clienteNombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));		
        clienteApellidoColumn.setCellValueFactory(new PropertyValueFactory<>("apellido"));	
	}

}