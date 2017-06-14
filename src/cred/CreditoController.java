package cred;

import java.io.IOException;
import java.math.BigDecimal;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreditoController {

	@FXML
	private TextField clienteField;
	@FXML
	private TextField montoCreditoField;
	@FXML
	private TextField tasaInteresField;		
	@FXML
	private TextField cantCuotasField;
	@FXML
	private TextField cobradorField;
	@FXML
	private TextField montoCuotaField;
	@FXML
	private TextField gciaXDiaField;
	@FXML
	private TextField montoTotalCreditoField;	
	@FXML
	private ComboBox<String> unidadCuotasCombo;	
	@FXML
	private ComboBox<String> cobradorCombo;
	@FXML
	private ComboBox<String> rutaCombo;			
	@FXML
	private Button crearButton;
	@FXML
	private Button cancelarButton;
	@FXML
	private Button buscarButton;
	
	
//	private PagoModel pago = new PagoModel();
//
//	private CreditoModel credito;
//
	private ClienteModel cliente;
	private MainController mainController;


	public CreditoController() {

	}

	@FXML
	private void initialize() {
		
		initComboCobrador();
		initComboUnidadCuotas();
		initComboRuta();
		
		montoCreditoField.setOnKeyReleased( (event) -> {			
			simular();
		});		
		
		tasaInteresField.setOnKeyReleased( (event) -> {			
			simular();
		});
		
		cantCuotasField.setOnKeyReleased( (event) -> {			
			simular();
		});		
			
		crearButton.setOnAction( (event) -> { 
			crear();
		});
		
		cancelarButton.setOnAction( (event) -> { 
			cancelar();
		});
		
		buscarButton.setOnAction( (event) -> {
			buscarCliente();
		});
		
	}

	private void initCliente() {
		if (cliente == null)
			return;
		
		this.clienteField.setText(cliente.getNombre() + ' ' + cliente.getApellido());		
	}

	private void buscarCliente() {
		
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("ClienteSearch.fxml"));
            VBox page = (VBox) loader.load();
            ClienteSearchController controller = loader.<ClienteSearchController>getController();

            controller.setClientes(mainController.getClientes());
//            controller.setCredito(rowData);            
            controller.setMainController(this);            
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Buscar cliente");
            stage.setResizable(false);
            
            Scene scene = new Scene(page);

            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }		
	}

	private void crear() {
		
	  	if (!validar())
	  		return;
	  
      	mainController.addItemToList(new CreditoModel(this.clienteField.getText(),
      											Integer.valueOf(this.cantCuotasField.getText()),
      											this.tasaInteresField.getText(),
      											montoCreditoField.getText(),	      											
      											cobradorCombo.getValue(),
      											rutaCombo.getValue()));

	    // get a handle to the stage
	    Stage stage = (Stage) crearButton.getScene().getWindow();
	    // do what you have to do
	    stage.close();      	
	}

	private void cancelar() {
	    // get a handle to the stage
	    Stage stage = (Stage) cancelarButton.getScene().getWindow();
	    // do what you have to do
	    stage.close();		
	}

	private void initComboUnidadCuotas() {
		unidadCuotasCombo.setItems(FXCollections.observableArrayList("Días", "Semanas"));
		unidadCuotasCombo.setValue("Días");
	}

	private void initComboCobrador() {
		cobradorCombo.setItems(FXCollections.observableArrayList("Luis","Miguel","Ezequiel","Ricardo","Rafael","Emanuel"));		
	}
	
	private void initComboRuta() {
		rutaCombo.setItems(FXCollections.observableArrayList("1","2","3","4","5","6","7"));		
	}
	
	private void simular() {

		if ( montoCreditoField.getText().length() == 0 ||
			 cantCuotasField.getText().length() == 0 ||
			 tasaInteresField.getText().length() == 0 ) {
		
			initFields();
			return;
		}

		BigDecimal cuotaCapital = CreditoModel.obtenerCuotaCapital(montoCreditoField.getText(), cantCuotasField.getText());		
		BigDecimal montoTotalCredito = CreditoModel.obtenerMontoTotalCredito(montoCreditoField.getText(), tasaInteresField.getText(), cantCuotasField.getText());
		BigDecimal montoCuota = CreditoModel.obtenerMontoCuota(montoTotalCredito, Integer.valueOf(cantCuotasField.getText()));

		montoCuotaField.setText(String.valueOf(montoCuota));
		gciaXDiaField.setText(String.valueOf(montoCuota.subtract(cuotaCapital)));
		montoTotalCreditoField.setText(String.valueOf(montoTotalCredito));
	}	

	private void initFields() {
		montoCuotaField.clear();
		gciaXDiaField.clear();	
	}

	private boolean validar() {
		
		if (clienteField.getText().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Error");
			alert.setContentText("Por favor ingrese el cliente");
			alert.showAndWait();			
			return false;
		} 

		if (montoCreditoField.getText().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Error");
			alert.setContentText("Ingrese el monto del crédito");
			alert.showAndWait();							
			return false;
		}						
		
		if (cantCuotasField.getText().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Error");
			alert.setContentText("Ingrese la cantidad de días");
			alert.showAndWait();							
			return false;
		}		

		if (tasaInteresField.getText().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Error");
			alert.setContentText("Ingrese la tasa de interés mensual");
			alert.showAndWait();							
			return false;
		}				
		
		if (cobradorCombo.getValue() == null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Error");
			alert.setContentText("Ingrese el Cobrador");
			alert.showAndWait();							
			return false;
		}					

		if (rutaCombo.getValue() == null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Error");
			alert.setContentText("Ingrese la ruta");
			alert.showAndWait();							
			return false;
		}					
		
		return true;
	}

	public void setMainController(MainController mainController) {
		this.mainController = mainController;		
	}

	public void setCliente(ClienteModel cliente) {
		this.cliente = cliente;
		initCliente();
	}
}