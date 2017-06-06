package cred;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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
	private Button ingresarPagoButton;
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
	}

	private void disableFields() {
		
		montoPagadoField.setDisable(true);		
		cuotasPagadasField.setDisable(true);
//		ingresarPagoButton.setVisible(false);
		ingresarPagoButton.setDisable(true);		
		fechaPagoField.setDisable(true);
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
		montoPagadoField.setText(credito.getValorCuota().toString());
		credito.setMontoCuota(credito.getValorCuota());

		cuotasPagadasField.setText(String.valueOf(credito.calcularCuotasAPagarSegunMonto()));
	}
	
	private void ingresarPago() {
		
		if (!(credito.validarMontoAPagar(new BigDecimal(montoPagadoField.textProperty().getValue())))) {
			
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
		credito.calcularMontoAcumulado();		
		credito.calcularCuotasAPagarSegunMonto();
		credito.calcularSaldoCapital();
		credito.calcularCuotasPagas();
		this.mainController.calcPagos();
	}

	public void setCredito(CreditoModel credito, ObservableList<CreditoModel> creditosTab) {
		this.credito = credito;
		clienteField.setText(this.credito.getCliente());
		pagos.addAll(this.credito.getListaPagos());

		this.montoPagadoField.setText(this.credito.getValorCuota().toString());
		credito.setMontoCuota(this.credito.getValorCuota());
		cuotasPagadasField.setText(String.valueOf(credito.calcularCuotasAPagarSegunMonto()));
		
		if (this.credito.getCuotasPagas() == this.credito.getCantDias())		// cantidad de dias deberia cambiar a cantidad cuotas totales y en otro campo la unidad, dias o semanas
			disableFields();
				
		
	}

	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}
}