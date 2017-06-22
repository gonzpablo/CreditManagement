package cred;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PagoController {

	@FXML
	private TextField clienteField;
	@FXML
	private TextField montoPagadoField;
	@FXML
	private TextField cuotasPagadasField;		
	@FXML
	private DatePicker fechaPagoField;
	@FXML
	private CheckBox cerrarCreditoCheckBox;
		
	@FXML
	private Button ingresarPagoButton;
	@FXML
	private Button cancelarButton;
	@FXML
	private Button borrarPagoButton;
	
	@FXML
	private TableColumn<PagoModel, LocalDate> fechaColumn;
	@FXML	
    private TableColumn<PagoModel, Float> montoPagoColumn;	
	@FXML
	private TableView<PagoModel> pagosTable;
	
	private PagoModel pago = new PagoModel();

	private CreditoModel credito;

	private ObservableList<PagoModel> pagos = FXCollections.observableArrayList();	

	private MainController mainController;


	public PagoController() {

	}

	public static final LocalDate LOCAL_DATE (String dateString){
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    LocalDate localDate = LocalDate.parse(dateString, formatter);
	    return localDate;
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */

	@FXML
	private void initialize() {

		initColumns();		
		
		fechaPagoField.setValue(pago.getFecha());
		
		pagosTable.setItems(pagos);

		montoPagadoField.setOnKeyReleased( (event) -> {			
			actualizarCuotasPagadasField();
		});
		
		cuotasPagadasField.setOnKeyReleased( (event) -> {
			actualizarMontoPagadoField();
		});
		
		ingresarPagoButton.setOnAction((event) -> {
		    ingresarPago();
		});		
		
		cancelarButton.setOnAction( (event) -> {
			cancelarPago();
		});
		
		borrarPagoButton.setOnAction( (event) -> {
			borrarPago();
		});
		
		cerrarCreditoCheckBox.setOnAction( (event) -> { cerrarCredito(); } );
	}

	private void borrarPago() {
		
		PagoModel pago = pagosTable.getSelectionModel().getSelectedItem();
		
		if (pago == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error al borrar un pago");
			alert.setContentText("Por favor seleccione un pago a borrar");
			alert.showAndWait();									
			return;
		}
			
		pagos.remove(pago);
		credito.borrarPago(pago);
		credito.calcular();
		this.mainController.calcPagos();
	}

	private void disableFields(Boolean value) {
		
		montoPagadoField.setDisable(value);		
		cuotasPagadasField.setDisable(value);
		ingresarPagoButton.setDisable(value);
		borrarPagoButton.setDisable(value);
		fechaPagoField.setDisable(value);
	}
	
	private void actualizarMontoPagadoField() {
		montoPagadoField.setText(String.valueOf(credito.calcularMontoSegunCuota(Integer.valueOf(cuotasPagadasField.textProperty().getValue()))));		
	}
	
	private void actualizarCuotasPagadasField() {
		
		if (montoPagadoField.textProperty().getValue().length() == 0)
			return;
	
		credito.setMontoCuota(new BigDecimal(montoPagadoField.textProperty().getValue()));
			
		cuotasPagadasField.setText(String.valueOf(credito.calcularCuotasAPagarSegunMonto()));
	};	
	
	private void initColumns() {
		fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));		
        montoPagoColumn.setCellValueFactory(new PropertyValueFactory<>("montoPago"));	
	}

	private void initFields() {

		montoPagadoField.clear();
		montoPagadoField.setText(credito.getValorCuotaInterno().toString());
		credito.setMontoCuota(credito.getValorCuotaInterno());

		cuotasPagadasField.setText(String.valueOf(credito.calcularCuotasAPagarSegunMonto()));
	}
	
	private void cerrarCredito() {
		if (cerrarCreditoCheckBox.isSelected() == true) {
			credito.setCerrado(true);
			disableFields(true);
		}
		
		else {
			credito.setCerrado(false);
			if (this.credito.getCuotasPagas() == this.credito.getCantCuotas())
				disableFields(true);
			else
				disableFields(false);	
		}
	}
	
	private void ingresarPago() {
		
		if (!(credito.validarMontoAPagar(new BigDecimal(montoPagadoField.textProperty().getValue())))) {
//			
			credito.setCerrado(true);
			credito.calcular();
			this.mainController.refrescar();
			
			credito.calcular();
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error al ingresar pago");
			alert.setContentText("El monto a pagar excede el monto total del crédito");
			alert.showAndWait();				

			return;
		}
			
		this.pago.setMontoPago(montoPagadoField.getText());
		this.pago.setFecha(fechaPagoField.getValue());
		this.credito.agregarPago(this.pago);

		pagos.add(this.pago);
		this.pago = new PagoModel();
		initFields();
		credito.calcular();
		this.mainController.calcPagos();
	}

//	public void setCredito(CreditoModel credito, ObservableList<CreditoModel> creditosTab) {
	public void setCredito(CreditoModel credito) {		
		this.credito = credito;
		clienteField.setText(this.credito.getCliente());
		pagos.addAll(this.credito.getListaPagos());

		this.montoPagadoField.setText(this.credito.getValorCuotaInterno().toString());
		credito.setMontoCuota(this.credito.getValorCuotaInterno());
		cuotasPagadasField.setText(String.valueOf(credito.calcularCuotasAPagarSegunMonto()));
		
		if (this.credito.getCuotasPagas() == this.credito.getCantCuotas()) { 

			disableFields(true);
			cerrarCreditoCheckBox.setDisable(true);
//			this.credito.setCerrado(true);
		}
	
		if (this.credito.isCerrado() == true)  {
			disableFields(true);
		}
		
		if (this.credito.isCerrado() == true)
			cerrarCreditoCheckBox.setSelected(true);;
	}

	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}
	
	public void cancelarPago() {
	    // get a handle to the stage
	    Stage stage = (Stage) cancelarButton.getScene().getWindow();
	    // do what you have to do
	    stage.close();		
	}
}