package cred;

import java.math.BigDecimal;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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
	private ComboBox<String> unidadCuotasCombo;	
	@FXML
	private ComboBox<String> cobradorCombo;
	@FXML
	private ComboBox<String> rutaCombo;			
	@FXML
	private Button crearButton;
	@FXML
	private Button cancelarButton;
	
	
//	private PagoModel pago = new PagoModel();
//
//	private CreditoModel credito;
//
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
		cobradorCombo.setItems(FXCollections.observableArrayList("Luis","Miguel","Ezequiel"));		
	}
	
	private void initComboRuta() {
		rutaCombo.setItems(FXCollections.observableArrayList("1","2","3","4"));		
	}
	
	private void simular() {

		if ( montoCreditoField.getText().length() == 0 ||
			 cantCuotasField.getText().length() == 0 ||
			 tasaInteresField.getText().length() == 0 ) {
		
			initFields();
			return;
		}

		BigDecimal cuotaCapital = CreditoModel.obtenerCuotaCapital(montoCreditoField.getText(), cantCuotasField.getText());		
		BigDecimal montoTotalCredito = CreditoModel.obtenerMontoTotalCredito(montoCreditoField.getText(), tasaInteresField.getText());
		BigDecimal montoCuota = CreditoModel.obtenerMontoCuota(montoTotalCredito, cantCuotasField.getText());

		montoCuotaField.setText(String.valueOf(montoCuota));
		gciaXDiaField.setText(String.valueOf(montoCuota.subtract(cuotaCapital)));		
	}	

	private void initFields() {
		montoCuotaField.clear();
		gciaXDiaField.clear();		
	}

	private boolean validar() {
		
//		if (clienteField.getText().equals("")) {
//			Alert alert = new Alert(AlertType.INFORMATION);
//			alert.setTitle("Atención");
//			alert.setHeaderText("Error");
//			alert.setContentText("Por favor ingrese el cliente");
//			alert.showAndWait();			
//			return false;
//		} 

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
}