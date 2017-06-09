package cred;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

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
	private ComboBox<String> unidadCombo;	
	@FXML
	private ComboBox<String> cobradorCombo;			
	@FXML
	private Button crearButton;
	@FXML
	private Button cancelarButton;
	
	
//	private PagoModel pago = new PagoModel();
//
//	private CreditoModel credito;
//
//	private MainController mainController;


	public CreditoController() {

	}

	@FXML
	private void initialize() {
		
		initComboCobrador();
		initComboUnidad();

	}

	private void initComboUnidad() {
		unidadCombo.setItems(FXCollections.observableArrayList("Días", "Semanas"));
		
	}

	private void initComboCobrador() {
		cobradorCombo.setItems(FXCollections.observableArrayList("Luis","Miguel","Ezequiel"));		
	}

	private void simular() {

		float montoCuota;
		float cuotaCapital;
		
	
		if (!validar())
			return;
		
		cuotaCapital = Float.valueOf(fldMontoCred.getText()) / Float.valueOf(fldCantDias.getText());
		
		montoCuota = cuotaCapital + ( cuotaCapital * ( Float.valueOf(fldTasaInt.getText()) / 100 )); //Float.valueOf("100") )); 

		fldMontoCuota.setText(String.valueOf(montoCuota));

		fldGciaXDia.setText(String.valueOf(montoCuota - cuotaCapital));
		
	}	

	private boolean validar() {
		
		if (fldCliente.getText().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Error");
			alert.setContentText("Por favor ingrese el cliente");
			alert.showAndWait();			
			return false;
		} 

		if (fldMontoCred.getText().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Error");
			alert.setContentText("Ingrese el monto del crédito");
			alert.showAndWait();							
			return false;
		}						
		
		if (fldCantDias.getText().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Error");
			alert.setContentText("Ingrese la cantidad de días");
			alert.showAndWait();							
			return false;
		}		

		if (fldTasaInt.getText().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Error");
			alert.setContentText("Ingrese la tasa de interés mensual");
			alert.showAndWait();							
			return false;
		}				
	
		if (fldCobrador.getText().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Error");
			alert.setContentText("Ingrese el Cobrador");
			alert.showAndWait();							
			return false;
		}					
		
		return true;
	}
		
	
}