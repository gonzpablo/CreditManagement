package cred;

import java.io.IOException;
import java.util.function.Predicate;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainController extends Stage {
	
	@FXML
	private TextField cobradorFilterField;
	@FXML
	private TableView<CreditoModel> creditosTable;
	@FXML
	private TableColumn<CreditoModel, String> clienteColumn;
	@FXML	
    private TableColumn<CreditoModel, Float> montoCuotaColumn;	
	@FXML	
    private TableColumn<CreditoModel, Float> gciaXDiaColumn;	
	@FXML	
    private TableColumn<CreditoModel, Float> saldoCapitalColumn;   
	@FXML	
    private TableColumn<CreditoModel, Float> montoCreditoColumn;	    
	@FXML	
    private TableColumn<CreditoModel, Integer> cuotasPagasColumn;    
	@FXML	
    private TableColumn<CreditoModel, Integer> cantDiasColumn;    
	@FXML	
    private TableColumn<CreditoModel, String> cobradorColumn;
	@FXML	
    private TableColumn<CreditoModel, String> rutaColumn;	
	@FXML
	private ComboBox<String> rutaFilterCombo;
	@FXML
	private Button btnCleanFilters;

	@FXML
	private TextField cuotaPuraField;
	@FXML
	private TextField gciaDiaField;
	
	private ObservableList<CreditoModel> creditos = FXCollections.observableArrayList();
	
	private FilteredList<CreditoModel> filteredItems = new FilteredList<>(creditos, p -> true);

	
	/**
	 * Just add some sample data in the constructor.
	 */
	public MainController() {
		initCreditos();
	}	
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 * 
	 * Initializes the table columns and sets up sorting and filtering.
	 */
	
	@FXML
	private void initialize() {

        rutaFilterCombo.setOnAction(e -> { 	calc();  });
//      cobradorFilterField.setOnAction(e -> { calc(); });
//      cobradorFilterField.setOnInputMethodTextChanged(e -> { calc(); });
        cobradorFilterField.setOnKeyPressed(e -> { calc(); });		
		cobradorFilterField.setOnAction(e -> { calc(); });
		initColumns();
		initComboRuta();
		
        ObjectProperty<Predicate<CreditoModel>> cobradorFilter = new SimpleObjectProperty<>();
        ObjectProperty<Predicate<CreditoModel>> rutaFilter = new SimpleObjectProperty<>();		
	
		cobradorFilter.bind(Bindings.createObjectBinding(() -> 
        credito -> credito.getCobrador().toLowerCase().contains(cobradorFilterField.getText().toLowerCase()), 
        cobradorFilterField.textProperty()));
		
        rutaFilter.bind(Bindings.createObjectBinding(() ->
        credito -> rutaFilterCombo.getValue() == null || rutaFilterCombo.getValue() == credito.getRuta(), 
        rutaFilterCombo.valueProperty()));		
		
//        FilteredList<CreditoModel> filteredItems = new FilteredList<>(creditos, p -> true);
        creditosTable.setItems(filteredItems);    
        
//		Esto hace que el filtro sea acumulativo (de los dos filtros) (ver el .and)        
        filteredItems.predicateProperty().bind(Bindings.createObjectBinding(
                () -> cobradorFilter.get().and(rutaFilter.get()), 
                cobradorFilter, rutaFilter));

        calc();
        
        btnCleanFilters.setOnAction(e -> {
            rutaFilterCombo.setValue(null);
            cobradorFilterField.clear();           
        });        
    
       
//		Doble-click        
        creditosTable.setRowFactory( tv -> {
        	
            TableRow<CreditoModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
            	
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    CreditoModel rowData = row.getItem();
//                    System.out.println(rowData.getCliente());
                    
                    pago(rowData, row);
                }
            });
            return row;
        });
        
                
	}

	private void pago(CreditoModel rowData, TableRow<CreditoModel> row) {

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("Pago.fxml"));
            GridPane page = (GridPane) loader.load();
            PagoController controller = loader.<PagoController>getController();
//            rowData.agregarPago(new PagoModel());
            controller.setCredito(rowData);
//            System.out.println(rowData.getCliente());
//            rowData.agregarPago(new PagoModel());
            Scene scene = new Scene(page);
            setScene(scene);
            show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	private void calc() {
		float sumaCuotaPura = 0,
			  sumaGciaXDia = 0;

		for ( CreditoModel cred : filteredItems ) {			
			sumaCuotaPura+=cred.getMontoCuota();
			sumaGciaXDia+=cred.getGciaXDia();
        }		
		
    	cuotaPuraField.setText(String.valueOf(sumaCuotaPura));
    	gciaDiaField.setText(String.valueOf(sumaGciaXDia));		
	}
	
    private void initComboRuta() {
		rutaFilterCombo.setItems(FXCollections.observableArrayList("1","2","3","4"));
	}

	private void initColumns() {
		clienteColumn.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        montoCuotaColumn.setCellValueFactory(new PropertyValueFactory<>("montoCuota"));
        gciaXDiaColumn.setCellValueFactory(new PropertyValueFactory<>("gciaXDia"));                
        saldoCapitalColumn.setCellValueFactory(new PropertyValueFactory<>("saldoCapital"));
        montoCreditoColumn.setCellValueFactory(new PropertyValueFactory<>("montoCredito"));               
        cuotasPagasColumn.setCellValueFactory(new PropertyValueFactory<>("cuotasPagas"));       
        cantDiasColumn.setCellValueFactory(new PropertyValueFactory<>("cantDias"));        
        cobradorColumn.setCellValueFactory(new PropertyValueFactory<>("cobrador"));
        rutaColumn.setCellValueFactory(new PropertyValueFactory<>("ruta"));
	}

    public ObservableList<CreditoModel> initCreditos(){
    	creditos.add(new CreditoModel("Carlos", 29, 45, 3000, 150, 45, 3000, "Luis", "1"));
    	creditos.add(new CreditoModel("Juan", 15, 45, 2000, 150, 45, 2000, "Miguel", "2"));
    	creditos.add(new CreditoModel("Jorge", 29, 45, 3000, 250, 45, 3000, "Luis", "1"));
    	creditos.add(new CreditoModel("Bernardo", 15, 45, 2000, 350, 45, 2000, "Ezequiel", "2"));

        return creditos;
    }

    public ObservableList<CreditoModel> getCreditos() {
        return creditos;
      }
}