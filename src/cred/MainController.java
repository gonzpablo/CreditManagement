package cred;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {

	@FXML
	private TableView<CreditoModel> creditosTable;
//  -------------------------------------------------------------------
//  Columnas tabla
//  -------------------------------------------------------------------	
	@FXML
	private TableColumn<CreditoModel, String> clienteColumn;
	@FXML	
	private TableColumn<CreditoModel, Float> valorCuotaColumn;	
	@FXML	
	private TableColumn<CreditoModel, Float> montoCuotaColumn;
	@FXML	
	private TableColumn<CreditoModel, Float> montoCuotaAcumuladoColumn;		
	@FXML	
	private TableColumn<CreditoModel, Float> gciaXDiaColumn;
	@FXML	
	private TableColumn<CreditoModel, Float> saldoCapitalColumn;
	@FXML	
	private TableColumn<CreditoModel, Float> montoCreditoColumn;
	@FXML	
	private TableColumn<CreditoModel, Integer> cuotasPagasColumn;
	@FXML	
	private TableColumn<CreditoModel, Integer> cantCuotasColumn;
	@FXML	
	private TableColumn<CreditoModel, String> cobradorColumn;
	@FXML	
	private TableColumn<CreditoModel, String> rutaColumn;
//  -------------------------------------------------------------------
//  Filtros	
//  -------------------------------------------------------------------	
	@FXML
	private ComboBox<String> rutaFilterCombo;
	@FXML
	private ComboBox<String> cobradorFilterCombo;
	@FXML
	private CheckBox cerradoFilterCheckBox;
	@FXML
	private DatePicker fechaFilterField;
//  -------------------------------------------------------------------	
	@FXML
	private Button btnCleanFilters;

//  -------------------------------------------------------------------
//  Sumarizadores
//  -------------------------------------------------------------------
	@FXML
	private TextField cuotaPuraField;
	@FXML
	private TextField gciaDiaField;
//  -------------------------------------------------------------------
	@FXML
	private MenuItem crearCreditoMenu;
	@FXML
	private MenuItem clienteMenuGestionar;
	@FXML
	private MenuItem reporteMenu;
	
	
//	Lista de créditos	
	private ObservableList<CreditoModel> creditos = FXCollections.observableArrayList();

	private FilteredList<CreditoModel> filteredItems = new FilteredList<>(creditos, p -> true);

//	Lista de clientes	
//	private List<ClientModel> clientes;
	private ObservableList<ClienteModel> clientes = FXCollections.observableArrayList();
	
	
	
	/**
	 * Just add some sample data in the constructor.
	 */
	public MainController() {
		initCreditos();
//		String nombre, String apellido, String direccion, String telefono, String dni		
		clientes.add(new ClienteModel("Patricia","Aguirre","Belgrano 1547","4987-5780","21487141"));
		clientes.add(new ClienteModel("Matias","Barbieri","Cuzco 9371","4547-3456","87214774"));
		clientes.add(new ClienteModel("Carla","Diaz","Alsina 837","2454-3852","17874877"));
		clientes.add(new ClienteModel("Miguel","Carrera","Paraguay 897","3878-1877","25787369"));		
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
        cobradorFilterCombo.setOnAction(e -> { calc(); });
        fechaFilterField.setOnAction(e -> { calcPagos(); } );
        clienteMenuGestionar.setOnAction( e -> { gestClientes(); } );
        crearCreditoMenu.setOnAction( e -> { crearCredito(); } );
        reporteMenu.setOnAction( e -> { reporte(); } );
        
		initColumns();
		initComboCobrador();
		initComboRuta();
				
        ObjectProperty<Predicate<CreditoModel>> cobradorFilter = new SimpleObjectProperty<>();
        ObjectProperty<Predicate<CreditoModel>> rutaFilter = new SimpleObjectProperty<>();			
        ObjectProperty<Predicate<CreditoModel>> cerradoFilter = new SimpleObjectProperty<>();
        
        cobradorFilter.bind(Bindings.createObjectBinding(() ->        
        								credito -> cobradorFilterCombo.getValue() == null ||
        								cobradorFilterCombo.getValue() == credito.getCobrador(),
        								cobradorFilterCombo.valueProperty()));				
		
        rutaFilter.bind(Bindings.createObjectBinding(() ->
        								credito -> rutaFilterCombo.getValue() == null ||
        								rutaFilterCombo.getValue() == credito.getRuta(),
        								rutaFilterCombo.valueProperty()));		

        cerradoFilter.bind(Bindings.createObjectBinding(() ->
										credito -> cerradoFilterCheckBox.isSelected() == credito.isCerrado(),
												cerradoFilterCheckBox.visibleProperty()));	

        
        
//        FilteredList<CreditoModel> filteredItems = new FilteredList<>(creditos, p -> true);
        creditosTable.setItems(filteredItems);    
        
//		Esto hace que el filtro sea acumulativo (de los dos filtros) (ver el .and)        
//        filteredItems.predicateProperty().bind(Bindings.createObjectBinding(() -> 
//        										cobradorFilter.get().and(
//        												rutaFilter.get()), cobradorFilter, rutaFilter));
        filteredItems.predicateProperty().bind(Bindings.createObjectBinding(() -> 
												cobradorFilter.get().and(rutaFilter.get()), cobradorFilter, rutaFilter));
        
        
        calc();
        
        btnCleanFilters.setOnAction(e -> {
            rutaFilterCombo.setValue(null);
            cobradorFilterCombo.setValue(null);
            fechaFilterField.setValue(null);
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

	private void reporte() {
		try {
			Reporte.reporteRuta(filteredItems);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void crearCredito() {
		   try {
	            FXMLLoader loader = new FXMLLoader(Main.class.getResource("Credito.fxml"));
	            GridPane page = (GridPane) loader.load();
	            CreditoController controller = loader.<CreditoController>getController();

	            controller.setMainController(this);            
	            
	            Stage stage = new Stage();
	            stage.initModality(Modality.APPLICATION_MODAL);
	            stage.setTitle("Crear Crédito");
	          
	            Scene scene = new Scene(page);

	            stage.setScene(scene);
	            stage.setResizable(false);
	            stage.show();

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}

	private void gestClientes() {
		   try {
	            FXMLLoader loader = new FXMLLoader(Main.class.getResource("Cliente.fxml"));
	            GridPane page = (GridPane) loader.load();
	            ClienteController controller = loader.<ClienteController>getController();

	            controller.setClientes(clientes);
//	            controller.setMainController(this);            
	            
	            Stage stage = new Stage();
	            stage.initModality(Modality.APPLICATION_MODAL);
	            stage.setTitle("Gestionar Clientes");
	          
	            Scene scene = new Scene(page);

	            stage.setScene(scene);
	            stage.show();
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }		
	}

	public void calcPagos() {
			
//		float sumaCuotaPura = 0,
//			  sumaGciaXDia = 0;
		BigDecimal sumaCuotaPura = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP),
					sumaGciaXDia = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP);
		
//		System.out.println("calcPago()");		
			for ( CreditoModel cred : filteredItems ) {


//				sumaCuotaPura+=cred.getMontoCuota(fechaFilterField.getValue());
				sumaCuotaPura = sumaCuotaPura.add(cred.getMontoCuota(fechaFilterField.getValue()));
//				System.out.println(cred.getMontoCuota(fechaFilterField.getValue()));
//				sumaGciaXDia+=cred.getGciaXDia(fechaFilterField.getValue());
				sumaGciaXDia = sumaGciaXDia.add(cred.getGciaXDia(fechaFilterField.getValue()));
			}

//		sumaCuotaPura.setScale(2, RoundingMode.HALF_UP);
//		sumaGciaXDia.setScale(2, RoundingMode.HALF_UP);
//		System.out.println(sumaGciaXDia);
//		System.out.println(sumaCuotaPura);
    	cuotaPuraField.setText(String.valueOf(sumaCuotaPura.setScale(2, RoundingMode.HALF_UP)));
    	gciaDiaField.setText(String.valueOf(sumaGciaXDia.setScale(2, RoundingMode.HALF_UP)));				
//    	cuotaPuraField.setText(String.valueOf(sumaCuotaPura));
//    	gciaDiaField.setText(String.valueOf(sumaGciaXDia));
    	
		creditosTable.refresh();
	}

	private void pago(CreditoModel rowData, TableRow<CreditoModel> row) {

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("Pago.fxml"));
            GridPane page = (GridPane) loader.load();
            PagoController controller = loader.<PagoController>getController();

//            controller.setCredito(rowData, creditos);
            controller.setCredito(rowData);            
            controller.setMainController(this);            
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Gestionar Pagos");
            stage.setResizable(false);
            
            Scene scene = new Scene(page);

            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	private void calc() {

//		float sumaCuotaPura = 0,
//				  sumaGciaXDia = 0;
			
		BigDecimal sumaCuotaPura = NumeroUtil.crearBigDecimal("0").setScale(2, RoundingMode.HALF_UP),
				sumaGciaXDia = NumeroUtil.crearBigDecimal("0").setScale(2, RoundingMode.HALF_UP);
		
//		for ( CreditoModel cred : filteredItems ) {
////			sumaCuotaPura+=cred.getMontoCuota(fechaFilterField.getValue());
//			sumaCuotaPura.add(cred.getMontoCuota(fechaFilterField.getValue()));
//			
////			sumaGciaXDia+=cred.getGciaXDia(fechaFilterField.getValue());
//			sumaGciaXDia.add(cred.getGciaXDia(fechaFilterField.getValue()));	

		for ( CreditoModel cred : filteredItems ) {			
//			sumaCuotaPura+=cred.getMontoCuota();
			sumaCuotaPura = sumaCuotaPura.add(cred.getMontoCuota());
//			sumaGciaXDia+=cred.getGciaXDia();
			sumaGciaXDia = sumaGciaXDia.add(cred.getGciaXDia());
        }		
		
		sumaCuotaPura.setScale(2, RoundingMode.HALF_UP);
		sumaGciaXDia.setScale(2, RoundingMode.HALF_UP);
    	cuotaPuraField.setText(String.valueOf(sumaCuotaPura));
    	gciaDiaField.setText(String.valueOf(sumaGciaXDia));		
	}
	
    private void initComboRuta() {
		rutaFilterCombo.setItems(FXCollections.observableArrayList("1","2","3","4","5","6","7"));
	}

    private void initComboCobrador() {
		cobradorFilterCombo.setItems(FXCollections.observableArrayList("Luis","Miguel","Ezequiel","Ricardo","Rafael","Emanuel"));
	}

	private void initColumns() {
		clienteColumn.setCellValueFactory(new PropertyValueFactory<>("cliente"));
		
		
		valorCuotaColumn.setCellValueFactory(new PropertyValueFactory<>("valorCuota"));
        montoCuotaColumn.setCellValueFactory(new PropertyValueFactory<>("montoCuota"));
        montoCuotaAcumuladoColumn.setCellValueFactory(new PropertyValueFactory<>("montoCuotaAcumulado"));
//        montoCuotaColumn.setCellValueFactory(cellData -> cellData.getValue().getMontoCuota().asObject());                
        gciaXDiaColumn.setCellValueFactory(new PropertyValueFactory<>("gciaXDia"));                
        saldoCapitalColumn.setCellValueFactory(new PropertyValueFactory<>("saldoCapital"));
        montoCreditoColumn.setCellValueFactory(new PropertyValueFactory<>("montoCredito"));               
        cuotasPagasColumn.setCellValueFactory(new PropertyValueFactory<>("cuotasPagas"));       
        cantCuotasColumn.setCellValueFactory(new PropertyValueFactory<>("cantCuotas"));        
        cobradorColumn.setCellValueFactory(new PropertyValueFactory<>("cobrador"));
        rutaColumn.setCellValueFactory(new PropertyValueFactory<>("ruta"));
	}

    public ObservableList<CreditoModel> initCreditos() {
    	
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));

    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	creditos.add(new CreditoModel("Patricia Aguirre", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Matias Barbieri", 15, "145", "2000", "Miguel", "2"));
    	creditos.add(new CreditoModel("Carla Diaz", 29, "145", "3000", "Luis", "1"));
    	creditos.add(new CreditoModel("Miguel Carrera", 15, "145", "2000", "Ezequiel", "2"));
    	
        return creditos;
    }

    public ObservableList<CreditoModel> getCreditos() {
        return creditos;
    }
    
    public void refreshTableView() {
    	creditosTable.refresh();
    }
    
    public void addItemToList(CreditoModel cred) {
    	creditos.add(cred);
//    	table.setItems(getCreditos());
// refresh??    	
    }

	public ObservableList<ClienteModel> getClientes() {
		return clientes;
	}            
}