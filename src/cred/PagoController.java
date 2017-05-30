package cred;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
	
	public PagoController() {
//		pagos.addAll(this.credito.getListaPagos());
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
//		clienteField.setText(this.getCliente());
//	      
		fechaPagoField.setValue(pago.getFecha());
		
		pagosTable.setItems(pagos);
		ingresarPagoButton.setOnAction((event) -> {
		    // Button was clicked, do something...
		    ingresarPago();
		});		
	}

	private void initColumns() {
		fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));		
        montoPagoColumn.setCellValueFactory(new PropertyValueFactory<>("montoPago"));	
	}

	private void ingresarPago() {		
		this.pago.setMontoPago(Float.valueOf(montoPagadoField.getText()));
		this.pago.setFecha(fechaPagoField.getValue());
		this.credito.agregarPago(this.pago);
//		pagos.add(this.credito.getListaPagos());
		pagos.add(this.pago);
		this.pago = new PagoModel();
	}

	public void setCredito(CreditoModel credito) {
		this.credito = credito;
		clienteField.setText(this.credito.getCliente());
		pagos.addAll(this.credito.getListaPagos());
//		pagosTable.setItems(pagos);
	}

}