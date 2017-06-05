package cred;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
		    // Button was clicked, do something...
		    ingresarPago();
		});		
	}

	private void actualizarMontoPagadoField() {
		montoPagadoField.setText(String.valueOf(credito.calcularMontoSegunCuota(Integer.valueOf(cuotasPagadasField.textProperty().getValue()))));		
	}
	
	private void actualizarCuotasPagadasField() {
		
		if (montoPagadoField.textProperty().getValue().length() == 0)
			return;
	
		credito.setMontoCuota(Float.valueOf(montoPagadoField.textProperty().getValue()));
			
//		cuotasPagadasField.setText(montoPagadoField.textProperty().getValue());
		cuotasPagadasField.setText(String.valueOf(credito.calcularCuotasSegunMonto()));
	};	
	
	private void initColumns() {
		fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));		
//		fechaColumn.setCellValueFactory( 
//			pago -> { 
//				SimpleStringProperty property = new SimpleStringProperty();
//				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//				property.setValue(dateFormat.format(pago.getValue().getFecha()));
////				return ObservableValue<>.toString(property);
//				return property;
//				
//	});
        montoPagoColumn.setCellValueFactory(new PropertyValueFactory<>("montoPago"));	
	}

	private void initFields() {
//		fechaPagoField
		montoPagadoField.clear();
		this.montoPagadoField.setText(Float.toString(this.credito.getValorCuota()));		
	}
	
	private void ingresarPago() {		
		this.pago.setMontoPago(Float.valueOf(montoPagadoField.getText()));
		this.pago.setFecha(fechaPagoField.getValue());
		this.credito.agregarPago(this.pago);

		pagos.add(this.pago);
		this.pago = new PagoModel();
		initFields();
		credito.calcularMontoAcumulado();
		credito.calcularCuotasPagas();
		credito.calcularSaldoCapital();
		this.mainController.calcPagos();
	}

	public void setCredito(CreditoModel credito, ObservableList<CreditoModel> creditosTab) {
		this.credito = credito;
		clienteField.setText(this.credito.getCliente());
		pagos.addAll(this.credito.getListaPagos());

		this.montoPagadoField.setText(Float.toString(this.credito.getValorCuota()));
		
/*		
		StringProperty monto = new SimpleStringProperty();		
		
		System.out.println(montoPagadoField.textProperty().getValue().length());
		
		if ( montoPagadoField.textProperty().getValue().length() == 0 ) {
		
		} else {		
 
			monto.bind(montoPagadoField.textProperty());
			monto.setValue();
			monto.setValue(String.valueOf(this.credito.getValorCuota() * 10));
				
			System.out.println(monto.getValue());
		}
//		montoPagadoField.textProperty().get
		cuotasPagadasField.textProperty().bind(Bindings.multiply(5, montoPagadoField.textProperty().getValue())));		
		
//		private IntegerProperty totalCents = new SimpleIntegerProperty();

//		and then you can let your display bind its text to a formatted version of that property:

//		display.textProperty().bind(totalCents.divide(100.0).asString("$ %.2f"));		
		
*/				
	}

	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}
}