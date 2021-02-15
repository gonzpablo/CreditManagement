package com.cred.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import com.cred.model.CobradorModel;
import com.cred.model.CreditoDAO;
import com.cred.model.CreditoModel;
import com.cred.model.RutaModel;

public class CreditoModController {

	@FXML
	private TextField clienteField;
	@FXML
	private TextField montoCreditoField;
	@FXML
	private TextField tasaInteresField;		
	@FXML
	private TextField cantCuotasField;
	@FXML
	private TextField montoCuotaField;
	@FXML
	private TextField gciaXDiaField;
	@FXML
	private TextField montoTotalCreditoField;	
	@FXML
	private ComboBox<String> unidadCuotasCombo;	
	@FXML
	private ComboBox<CobradorModel> cobradorCombo;
	@FXML
	private ComboBox<RutaModel> rutaCombo;			
	@FXML
	private Button guardarButton;
	@FXML
	private Button cancelarButton;
	
	private MainController mainController;

	private CreditoModel credito;

	
	public CreditoModController() {	}

	@FXML
	private void initialize() {
		
		initComboUnidadCuotas();
		
		montoCreditoField.setOnKeyReleased( (event) -> simular());
		montoCuotaField.setOnKeyReleased( (event) -> simular());
		cantCuotasField.setOnKeyReleased( (event) -> simular());

		guardarButton.setOnAction( (event) -> guardar());
		cancelarButton.setOnAction( (event) -> cancelar());
		
        rutaCombo.setConverter(new StringConverter<RutaModel>() {
            @Override
            public String toString(RutaModel ruta) {
                return ruta.getNombre() + " - " + ruta.getDescripcion();
            }

            @Override
            public RutaModel fromString(String string) {
                return null;            }
        });
        
        cobradorCombo.setConverter(new StringConverter<CobradorModel>() {
            @Override
            public String toString(CobradorModel cobrador) {
                return cobrador.getNombre() + " " + cobrador.getApellido();
            }

            @Override
            public CobradorModel fromString(String string) {
                return null;
            }
        });		
	}
	
	public void cargarDatos() {
		
		BigDecimal montoTotalCredito = CreditoModel.obtenerMontoTotalCredito(
										this.credito.getValorCuota().toString(), 
									String.valueOf(this.credito.getCantCuotas()));
		
		initCliente();
		
		this.montoCreditoField.setText(this.credito.getMontoCredito().toString());
		this.montoCuotaField.setText(this.credito.getValorCuota().toString());

		this.cantCuotasField.setText(String.valueOf(this.credito.getCantCuotas()));
		
		this.cobradorCombo.setValue(this.credito.getCobradorRef());
		this.rutaCombo.setValue(this.credito.getRutaRef());

		this.montoTotalCreditoField.setText(montoTotalCredito.toString());

		this.gciaXDiaField.setText(this.credito.getGciaXDia().toString());
		this.tasaInteresField.setText(CreditoModel.obtenerTasaInteres(
										this.credito.getMontoCredito().toString(), 
										montoTotalCredito, 
										String.valueOf(this.credito.getCantCuotas()), 
										this.credito.getUnidad()).toString());

		this.unidadCuotasCombo.setValue(this.credito.getUnidad());

		BigDecimal cuotaCapital = CreditoModel.obtenerCuotaCapital(
				this.montoCreditoField.getText(), cantCuotasField.getText());		

		gciaXDiaField.setText(String.valueOf(this.credito.getValorCuota().subtract(cuotaCapital).setScale(2, RoundingMode.HALF_UP)));
	}
	
	private void initCliente() {
		if (this.credito.getClienteRef() == null)
			return;
		
		this.clienteField.setText(this.credito.getClienteRef().getNombre() + ' ' + this.credito.getClienteRef().getApellido());		
	}

	private void guardar() {
		
	  	if (!validar())
	  		return;
  	
	  	this.credito.setCobrador(this.cobradorCombo.getValue());
	  	this.credito.setRuta(this.rutaCombo.getValue());
	  	
	  	try {
			CreditoDAO.modificarCredito(this.credito);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		this.mainController.refreshTableView();
	  	
	    Stage stage = (Stage) guardarButton.getScene().getWindow();
	    stage.close();      	
	}

	private void cancelar() {
	    Stage stage = (Stage) cancelarButton.getScene().getWindow();
	    stage.close();		
	}

	private void initComboUnidadCuotas() {
		unidadCuotasCombo.setItems(FXCollections.observableArrayList("Días", "Semanas"));
		unidadCuotasCombo.setValue("Días");
	}

	private void simular() {

		if ( ( montoCreditoField.getText().length() == 0 || 
			   cantCuotasField.getText().length() == 0 || 
			   montoCuotaField.getText().length() == 0 )
			|| ( Float.parseFloat(montoCreditoField.getText()) <= 0 ) ||
				( Integer.parseInt(cantCuotasField.getText()) <= 0 ) ||
				( Float.parseFloat(montoCuotaField.getText()) <= 0 ) ) {
		
			initFields();
			return;
		}
		
		BigDecimal montoCuota = new BigDecimal(montoCuotaField.getText());
		
		BigDecimal cuotaCapital = CreditoModel.obtenerCuotaCapital(
									montoCreditoField.getText(), cantCuotasField.getText());		
		BigDecimal montoTotalCredito = CreditoModel.obtenerMontoTotalCredito(
									montoCuotaField.getText(), cantCuotasField.getText());
		
		BigDecimal tasaInteres = CreditoModel.obtenerTasaInteres(
									montoCreditoField.getText(), montoTotalCredito, cantCuotasField.getText(), unidadCuotasCombo.getValue());

		tasaInteresField.setText(tasaInteres.toString());
		gciaXDiaField.setText(String.valueOf(montoCuota.subtract(cuotaCapital)));
		montoTotalCreditoField.setText(String.valueOf(montoTotalCredito));
	}	

	private void initFields() {
		tasaInteresField.clear();		
		gciaXDiaField.clear();
		montoTotalCreditoField.clear();
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

		if (tasaInteresField.getText().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Error");
			alert.setContentText("Ingrese el monto de cada cuota");
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
		
//		Se hace acá porque se necesitan los datos del Main Controller		
		initComboCobrador();
		initComboRuta();		
	}
	
	public void setCredito(CreditoModel credito) {
		this.credito = credito;
	}
	
	private void initComboCobrador() {		
		cobradorCombo.setItems(mainController.getListaCobradores());
	}
	
	private void initComboRuta() {
		rutaCombo.setItems(mainController.getListaRutas());
	}	
}