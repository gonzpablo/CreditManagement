package com.cred.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.Temporal;
import java.util.Comparator;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import com.cred.model.CreditoModel;
import com.cred.model.NumeroUtil;
import com.cred.model.PagoDAO;
import com.cred.model.PagoModel;
import com.cred.util.DBUtil;
import com.cred.util.TextFieldValidator;
import com.cred.util.TextFieldValidator.ValidationModus;

public class PagoController {

	@FXML
	private TextField clienteField;
	@FXML
	private TextField montoPagadoField;
	@FXML
	private Spinner<Integer> cuotasPagadasField;	
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


	public PagoController() {}

	@FXML
	private void initialize() {
		
		initColumns();		
		
		fechaPagoField.setValue(pago.getFecha().getValue());
		
		pagosTable.setItems(pagos);
		
		montoPagadoField.setOnKeyReleased( (event) -> {			
			actualizarCuotasPagadasField();
		});
	
		cuotasPagadasField.getEditor().setTextFormatter(new TextFieldValidator(ValidationModus.MAX_INTEGERS, 9).getFormatter());

		cuotasPagadasField.setOnMouseReleased((event) -> {
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

		montoPagadoField.setTextFormatter(new TextFieldValidator(ValidationModus.MAX_FRACTION_DIGITS, 2).getFormatter());
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
		pago.borrarPago();
		credito.calcular();
		mainController.calcularTotales();
	}

	private void disableFields(Boolean value) {
		
		montoPagadoField.setDisable(value);		
		cuotasPagadasField.setDisable(value);
		ingresarPagoButton.setDisable(value);
		borrarPagoButton.setDisable(value);
		fechaPagoField.setDisable(value);
	}
	
	private void actualizarMontoPagadoField() {		
		montoPagadoField.setText(String.valueOf(credito.calcularMontoSegunCuota(cuotasPagadasField.getValue())));		
	}
	
	private void actualizarCuotasPagadasField() {
		
		if (montoPagadoField.textProperty().getValue().length() == 0)
			return;
	
		credito.setMontoCuota(new BigDecimal(montoPagadoField.textProperty().getValue()));
			
		cuotasPagadasField.getValueFactory().setValue(credito.calcularCuotasAPagarSegunMonto());
	};	
	
	private void initColumns() {
		fechaColumn.setCellValueFactory(cellData -> cellData.getValue().getFecha());
		
        montoPagoColumn.setCellValueFactory(new PropertyValueFactory<>("montoPago"));
        
        DateTimeFormatter format = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
        fechaColumn.setCellFactory (getDateCell(format));        
	}

	public static <ROW,T extends Temporal> Callback<TableColumn<ROW, T>, TableCell<ROW, T>> getDateCell (DateTimeFormatter format) {		
		  return column -> {
		    return new TableCell<ROW, T> () {
		      @Override
		      protected void updateItem (T item, boolean empty) {
		        super.updateItem (item, empty);
		        if (item == null || empty) {
		          setText (null);
		        }
		        else {
		          setText (format.format (item));
		        }
		      }
		    };
		  };
		}
	
	private void initFields() {

		montoPagadoField.clear();
		montoPagadoField.setText(credito.getValorCuota().toString());
		
		credito.setMontoCuota(credito.getValorCuota());

		cuotasPagadasField.getValueFactory().setValue(credito.calcularCuotasAPagarSegunMonto());
	}
	
	private void cerrarCredito() {

		credito.setCerrado(cerrarCreditoCheckBox.isSelected());
		disableFields(cerrarCreditoCheckBox.isSelected());
		credito.cerrar(cerrarCreditoCheckBox.isSelected());
	}
	
	private void ingresarPago() {
		
		if (!(credito.validarMontoAPagar(new BigDecimal(montoPagadoField.textProperty().getValue())))) {

			credito.calcular();
			this.mainController.refrescar();
			
//			credito.calcular();
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error al ingresar pago");
			alert.setContentText("El monto a pagar excede el monto total del crédito");
			alert.showAndWait();				

			return;
		}
			
		this.pago.setMontoPago(montoPagadoField.getText());
		this.pago.setFecha(new SimpleObjectProperty<LocalDate>(fechaPagoField.getValue()));
		this.credito.agregarPago(this.pago);

		pagos.add(this.pago);
		
		Comparator<PagoModel> comparator = new Comparator<PagoModel>() {
		    @Override
		    public int compare(final PagoModel o1, final PagoModel o2) {
		        if (o1.getFechaValue()== null || o2.getFechaValue() == null)
		            return 0;
		        return o2.getFechaValue().compareTo(o1.getFechaValue());
		    }
		};	

		FXCollections.sort(pagos, comparator);
		
		try {
			PagoDAO.agregarPago(credito.getId(), this.pago);
//			Obtener Id asignado por la base de datos						
			this.pago.setId(DBUtil.getLastRowId("pagos"));			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		credito.calcularMontoAcumulado();
		
		if (credito.alcanzoMontoFinal()) {
			credito.cerrar(true);
			credito.setCerrado(true);
		}
		
		this.pago = new PagoModel();
		initFields();
		credito.calcular();
		mainController.calcularTotales();
	}

	private int calcularCuotasAPagarSegunMonto(String monto, BigDecimal valorCuota) {

		BigDecimal montoAPagar = new BigDecimal(monto);
		
		return montoAPagar.divide(
				valorCuota, NumeroUtil.EXCEL_MAX_DIGITS, RoundingMode.HALF_UP).intValue();					
	}
	
	public void setCredito(CreditoModel credito) {		
		this.credito = credito;
		clienteField.setText(this.credito.getCliente());
		pagos.addAll(this.credito.getListaPagos());		
		
		this.montoPagadoField.setText(this.credito.getValorCuota().toString());		
		
		cuotasPagadasField.getValueFactory().setValue(calcularCuotasAPagarSegunMonto(this.montoPagadoField.getText(), credito.getValorCuota()));
		
		if (this.credito.getCuotasPagas() == this.credito.getCantCuotas()) { 
			disableFields(true);
			cerrarCreditoCheckBox.setDisable(true);
		}
	
		if (this.credito.isCerrado() == true)
			disableFields(true);
		
		if (this.credito.isCerrado() == true)
			cerrarCreditoCheckBox.setSelected(true);;
	}

	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}
	
	public void cancelarPago() {
	    Stage stage = (Stage) cancelarButton.getScene().getWindow();
	    stage.close();		
	}
	
	public static final LocalDate LOCAL_DATE (String dateString) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    LocalDate localDate = LocalDate.parse(dateString, formatter);
	    return localDate;
	}	
}