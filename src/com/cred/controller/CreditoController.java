package com.cred.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import com.cred.model.ClienteModel;
import com.cred.model.CobradorModel;
import com.cred.model.CreditoDAO;
import com.cred.model.CreditoModel;
import com.cred.model.RutaModel;
import com.cred.util.DBUtil;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

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
	private ComboBox<CobradorModel> cobradorCombo;
	@FXML
	private ComboBox<RutaModel> rutaCombo;			
	@FXML
	private Button crearButton;
	@FXML
	private Button cancelarButton;
	@FXML
	private Button buscarButton;
	
	private ClienteModel cliente;
	
	private MainController mainController;


	public CreditoController() {
	}

	@FXML
	private void initialize() {
		
		initComboUnidadCuotas();
		
		montoCreditoField.setOnKeyReleased( (event) -> { simular(); });		
		montoCuotaField.setOnKeyReleased( (event) -> { simular(); });
		cantCuotasField.setOnKeyReleased( (event) -> { simular(); });		

		crearButton.setOnAction( (event) -> { crear(); });
		cancelarButton.setOnAction( (event) -> { cancelar(); });
		buscarButton.setOnAction( (event) -> { buscarCliente(); });
		
        rutaCombo.setConverter(new StringConverter<RutaModel>() {
            @Override
            public String toString(RutaModel object) {
                return object.getDescripcion();
            }

            @Override
            public RutaModel fromString(String string) {
                return null;
            }
        });
        
        cobradorCombo.setConverter(new StringConverter<CobradorModel>() {
            @Override
            public String toString(CobradorModel object) {
                return object.getNombre();
            }

            @Override
            public CobradorModel fromString(String string) {
                return null;
            }
        });		
		
	}

	private void initCliente() {
		if (cliente == null)
			return;
		
		this.clienteField.setText(cliente.getNombre() + ' ' + cliente.getApellido());		
	}

	private void buscarCliente() {
		
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../view/ClienteSearch.fxml"));
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

//	  	mainController.addItemToList(new CreditoModel(this.clienteField.getText(),
//      											Integer.valueOf(this.cantCuotasField.getText()),
//      											unidadCuotasCombo.getValue(),
//      											montoCuotaField.getText(),
//      											montoCreditoField.getText(),	      											
//      											cobradorCombo.getValue(),
//      											rutaCombo.getValue()));

//		public CreditoModel(int idCliente, int cantCuotas, int idUnidad, int montoCuota, 
//				int montoCredito, int idCobrador, int idRuta) {
	  	
	  	
//		public CreditoModel(ClienteModel cliente, int cantCuotas, String unidad, String montoCuota,
//				String montoCredito, CobradorModel cobrador, RutaModel ruta)	  	
	  	CreditoModel nuevoCredito = new CreditoModel(
	  													this.cliente,
	  													Integer.valueOf(cantCuotasField.getText()),
	  													unidadCuotasCombo.getValue(),					
	  													montoCuotaField.getText(),
	  													montoCreditoField.getText(),	      											
	  													cobradorCombo.getValue(),
	  													rutaCombo.getValue());	  	
	  	
	  	nuevoCredito.setCliente(cliente);
	  	nuevoCredito.setCobrador(cobradorCombo.getValue());
	  	nuevoCredito.setRuta(rutaCombo.getValue());
	  	
	  	try {
			CreditoDAO.agregarCredito(nuevoCredito);
//			Obtener Id asignado al crédito por la base de datos						
			nuevoCredito.setId(DBUtil.getLastRowId("creditos"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  	
	  	mainController.addItemToList(nuevoCredito);
	  	
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
		cobradorCombo.setItems(mainController.getListaCobradores());
	}
	
	private void initComboRuta() {
//		rutaCombo.setItems(FXCollections.observableArrayList("1","2","3","4","5","6","7"));
		rutaCombo.setItems(mainController.getListaRutas());
	}
	
	private void simular() {

		if ( ( montoCreditoField.getText().length() == 0 || 
			   cantCuotasField.getText().length() == 0 || 
			   montoCuotaField.getText().length() == 0 )
			|| ( Float.valueOf(montoCreditoField.getText()) <= 0 ) ||
				( Integer.valueOf(cantCuotasField.getText()) <= 0 ) ||
				( Float.valueOf(montoCuotaField.getText()) <= 0 ) ) {
		
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
//		BigDecimal montoCuota = CreditoModel.obtenerMontoCuota(montoTotalCredito, Integer.valueOf(cantCuotasField.getText()));

//		montoCuotaField.setText(String.valueOf(montoCuota));
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

	public void setCliente(ClienteModel cliente) {
		this.cliente = cliente;
		initCliente();
	}
}