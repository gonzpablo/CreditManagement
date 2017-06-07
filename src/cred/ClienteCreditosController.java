package cred;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClienteCreditosController {

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
	private Button crearCreditoButton;
	
	@FXML
	private TableView<CreditoModel> creditosTable;	
	@FXML
	private TableColumn<CreditoModel, String> montoCreditoColumn;
	@FXML
	private TableColumn<CreditoModel, String> valorCuotaColumn;
	@FXML
	private TableColumn<CreditoModel, String> montoAcumuladoColumn;
	@FXML
	private TableColumn<CreditoModel, String> saldoCapitalColumn;
	@FXML
	private TableColumn<CreditoModel, String> cuotasPagasColumn;
	@FXML
	private TableColumn<CreditoModel, String> cantCuotasColumn;
	
    ObservableList<CreditoModel> creditos = FXCollections.observableArrayList();	
	
	public ClienteCreditosController() {
		creditos.add(new CreditoModel("Carlos", 29, "45", "3000", "Luis", "1"));
	}
	
	@FXML
	private void initialize() {

		initColumns();
		
		crearCreditoButton.setOnAction((event) -> {
		    crearCredito();
//		    initFields();
		});				
		
		
	}

	private void crearCredito() {
		
		
	}

	private void initColumns() {       
    	montoCreditoColumn.setCellValueFactory(new PropertyValueFactory<>("montoCredito"));    	
    	valorCuotaColumn.setCellValueFactory(new PropertyValueFactory<>("valorCuota"));
    	montoAcumuladoColumn.setCellValueFactory(new PropertyValueFactory<>("montoCuotaAcumulado"));
    	saldoCapitalColumn.setCellValueFactory(new PropertyValueFactory<>("saldoCapital"));
    	cuotasPagasColumn.setCellValueFactory(new PropertyValueFactory<>("cuotasPagas"));
    	cantCuotasColumn.setCellValueFactory(new PropertyValueFactory<>("cantDias"));        
	}
	
	public void setCliente(ClientModel cliente) {
		
	}
}