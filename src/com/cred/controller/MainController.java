package com.cred.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import com.cred.model.ClienteDAO;
import com.cred.model.ClienteModel;
import com.cred.model.CobradorDAO;
import com.cred.model.CobradorModel;
import com.cred.model.CreditoDAO;
import com.cred.model.CreditoModel;
import com.cred.model.NumeroUtil;
import com.cred.model.PagoDAO;
import com.cred.model.PagoModel;
import com.cred.model.Reporte;
import com.cred.model.RutaDAO;
import com.cred.model.RutaModel;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

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
	private TableColumn<CreditoModel, String> unidadColumn;
	@FXML	
	private TableColumn<CreditoModel, String> cobradorColumn;
	@FXML	
	private TableColumn<CreditoModel, String> rutaColumn;	
//  -------------------------------------------------------------------
//  Filtros	
//  -------------------------------------------------------------------	
	@FXML
	private ComboBox<RutaModel> rutaFilterCombo;
	@FXML
	private ComboBox<CobradorModel> cobradorFilterCombo;	
	@FXML
	private CheckBox cerradoFilterCheckBox;
	@FXML
	private DatePicker fechaFilterField;
//  -------------------------------------------------------------------	
	@FXML
	private Button btnCleanFilters;
	@FXML
	private Button btnBorrarCreditos;

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
	private MenuItem cobradorMenuGestionar;
	@FXML
	private MenuItem rutasMenuGestionar;		
	@FXML
	private MenuItem reporteMenu;
	
//	Lista de créditos	
	private ObservableList<CreditoModel> creditos = FXCollections.observableArrayList();
	private ObservableList<CreditoModel> creditosCerrados = FXCollections.observableArrayList();
	
	private FilteredList<CreditoModel> filteredItems; 

	private ObservableList<ClienteModel> listaClientes = FXCollections.observableArrayList();
	private ObservableList<RutaModel> listaRutas = FXCollections.observableArrayList();	
	private ObservableList<CobradorModel> listaCobradores = FXCollections.observableArrayList();	
	private ObservableList<PagoModel> listaPagos = FXCollections.observableArrayList();
	
	private Map<Integer, ClienteModel> hashClientes = new HashMap<Integer, ClienteModel>(); 
	private Map<Integer, RutaModel> hashRutas = new HashMap<Integer, RutaModel>();
	private Map<Integer, CobradorModel> hashCobradores = new HashMap<Integer, CobradorModel>();
	private Map<Integer, CreditoModel> hashCreditos = new HashMap<Integer, CreditoModel>();
	

	public MainController() {	
		initClientes();
		initRutas();
		initCobradores();
		initCreditos();
		initPagos();		
		
//		Completar en los créditos, las referencias a clientes, cobradores y rutas		
		completarCreditos();
	}	
		
	@FXML
	private void initialize() {

		initFiltros();
		
		cerradoFilterCheckBox.setOnAction( e -> { buscarCredCerrados(); });
		
        rutaFilterCombo.setOnAction(e -> { 	calc();  });
        
        rutaFilterCombo.setConverter(new StringConverter<RutaModel>() {
            @Override
            public String toString(RutaModel object) {
                return object.getDescripcion();
            }

            @Override
            public RutaModel fromString(String string) {
                return null;
            }
        });
        
        cobradorFilterCombo.setOnAction(e -> { calc(); });        
        
        cobradorFilterCombo.setConverter(new StringConverter<CobradorModel>() {
            @Override
            public String toString(CobradorModel object) {
                return object.getNombre();
            }

            @Override
            public CobradorModel fromString(String string) {
                return null;
            }
        });
        
        fechaFilterField.setOnAction(e -> { calcPagos(); } );
        clienteMenuGestionar.setOnAction( e -> { gestClientes(); } );
        
        cobradorMenuGestionar.setOnAction( e -> { gestCobradores(); });
        rutasMenuGestionar.setOnAction( e -> { gestRutas();} );
        
        crearCreditoMenu.setOnAction( e -> { crearCredito(); } );
        reporteMenu.setOnAction( e -> { reporte(); } );
		btnBorrarCreditos.setOnAction( (event) -> {	borrarCredito(); });
        btnCleanFilters.setOnAction(e -> {
            rutaFilterCombo.setValue(null);
            cobradorFilterCombo.setValue(null);
            fechaFilterField.setValue(null);
            cerradoFilterCheckBox.setSelected(false);           
        });        
        
		initColumns();
		initComboCobrador();
		initComboRuta();
				
        ObjectProperty<Predicate<CreditoModel>> cobradorFilter = new SimpleObjectProperty<>();
        ObjectProperty<Predicate<CreditoModel>> rutaFilter = new SimpleObjectProperty<>();			
        ObjectProperty<Predicate<CreditoModel>> cerradoFilter = new SimpleObjectProperty<>();
        
        cobradorFilter.bind(Bindings.createObjectBinding(() ->        
		credito ->
			cobradorFilterCombo.getValue() == null || cobradorFilterCombo.getValue().getId() == credito.getIdCobrador(),
			cobradorFilterCombo.valueProperty()));              
        
        rutaFilter.bind(Bindings.createObjectBinding(() ->
		credito -> 
			rutaFilterCombo.getValue() == null || rutaFilterCombo.getValue().getId() == credito.getIdRuta(),
			rutaFilterCombo.valueProperty()));		
        
        
        cerradoFilter.bind(Bindings.createObjectBinding(() ->
							credito -> 
							    cerradoFilterCheckBox.isSelected() == credito.isCerrado(),
								cerradoFilterCheckBox.selectedProperty()));	        

        filteredItems.predicateProperty().bind(Bindings.createObjectBinding(() -> 
				cobradorFilter.get().and(rutaFilter.get().and(cerradoFilter.get())), cobradorFilter, rutaFilter, cerradoFilter));
        
        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<CreditoModel> sortedData = new SortedList<>(filteredItems);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(creditosTable.comparatorProperty());
        
        // 5. Add sorted (and filtered) data to the table.
        creditosTable.setItems(sortedData);
        
        calc();       

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
        
        calcPagos();
	}	
	
	private void buscarCredCerrados() {

//		solo debería buscar una vez los créditos cerrados
		if (creditosCerrados.size() > 0)
			return;
		
		try {		
			creditosCerrados = CreditoDAO.buscarCreditos(1);
			creditos.addAll(creditosCerrados);
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
    	hashCreditos();
    	
//		Completar en los créditos, las referencias a clientes, cobradores y rutas		
		completarCreditos();    	

	}

	private void hashClientes() {

		for (ClienteModel cliente : listaClientes)			
			hashClientes.put(cliente.getId(), cliente);		
	}

	private void hashRutas() {
		
		for (RutaModel ruta : listaRutas)
			hashRutas.put(ruta.getId(), ruta);
	}

	private void hashCobradores() {
		
		for (CobradorModel cobrador : listaCobradores )
			hashCobradores.put(cobrador.getId(), cobrador);
	}
	
	private void completarCreditos() {

		for ( CreditoModel credito : creditos ) {

			//	Referencia al Cliente
			credito.setCliente(hashClientes.get(credito.getIdCliente()));
			//	Referencia al Cobrador			
			credito.setCobrador(hashCobradores.get(credito.getIdCobrador()));
			//	Referencia a la Ruta
			credito.setRuta(hashRutas.get(credito.getIdRuta()));			
		}
		
		filteredItems = new FilteredList<>(creditos, p -> true);
	}

	private void reporte() {

		Stage stage = new Stage();
		Reporte rep = new Reporte(stage);
		
		try {
			rep.reporteRutaBeta(filteredItems);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void borrarCredito() {
		CreditoModel credito = creditosTable.getSelectionModel().getSelectedItem();
		
		if (credito == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error al borrar un crédito");
			alert.setContentText("Por favor seleccione un crédito a borrar");
			alert.showAndWait();									
			return;
		}
			
		creditos.remove(credito);
		credito.calcular();
		credito.borrarCredito();
		this.calcPagos();		
	}

	private void crearCredito() {
	   try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../view/Credito.fxml"));
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
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../view/Cliente.fxml"));
            GridPane page = (GridPane) loader.load();
            ClienteController controller = loader.<ClienteController>getController();

            controller.setClientes(listaClientes);  
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Gestionar Clientes");
          
            Scene scene = new Scene(page);

            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }		
	}

	private void gestCobradores() {
	   try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../view/Cobrador.fxml"));
            GridPane page = (GridPane) loader.load();
            CobradorController controller = loader.<CobradorController>getController();

            controller.setCobradores(listaCobradores);
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Gestionar Cobradores");
          
            Scene scene = new Scene(page);

            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }		
	}	
	
	private void gestRutas() {
		   try {
	            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../view/Ruta.fxml"));
	            GridPane page = (GridPane) loader.load();
	            RutaController controller = loader.<RutaController>getController();

	            controller.setRutas(listaRutas);
	            
	            Stage stage = new Stage();
	            stage.initModality(Modality.APPLICATION_MODAL);
	            stage.setTitle("Gestionar Rutas");
	          
	            Scene scene = new Scene(page);

	            stage.setResizable(false);
	            stage.setScene(scene);
	            stage.show();
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }		
		}	
	
	
	public void calcPagos() {
			
		BigDecimal sumaCuotaPura = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP),
				   sumaGciaXDia = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP);
		
			for ( CreditoModel cred : filteredItems ) {
				sumaCuotaPura = sumaCuotaPura.add(cred.getMontoCuota(fechaFilterField.getValue()));
				sumaGciaXDia = sumaGciaXDia.add(cred.getGciaXDia(fechaFilterField.getValue()));
			}

    	cuotaPuraField.setText(String.valueOf(sumaCuotaPura.setScale(2, RoundingMode.HALF_UP)));
    	gciaDiaField.setText(String.valueOf(sumaGciaXDia.setScale(2, RoundingMode.HALF_UP)));				
    	
		creditosTable.refresh();
	}
	
	public void refrescar() {
		creditosTable.refresh();
	}
	
	private void pago(CreditoModel rowData, TableRow<CreditoModel> row) {

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../view/Pago.fxml"));
            GridPane page = (GridPane) loader.load();
            PagoController controller = loader.<PagoController>getController();

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

		BigDecimal sumaCuotaPura = NumeroUtil.crearBigDecimal("0").setScale(2, RoundingMode.HALF_UP),
				sumaGciaXDia = NumeroUtil.crearBigDecimal("0").setScale(2, RoundingMode.HALF_UP);

		for ( CreditoModel cred : filteredItems ) {			
			sumaCuotaPura = sumaCuotaPura.add(cred.getMontoCuota());
			sumaGciaXDia = sumaGciaXDia.add(cred.getGciaXDia());
        }		
		
		sumaCuotaPura.setScale(2, RoundingMode.HALF_UP);
		sumaGciaXDia.setScale(2, RoundingMode.HALF_UP);
    	cuotaPuraField.setText(String.valueOf(sumaCuotaPura));
    	gciaDiaField.setText(String.valueOf(sumaGciaXDia));		
	}
	
    private void initComboRuta() {
		rutaFilterCombo.setItems(listaRutas);
	}

    private void initComboCobrador() {
    	cobradorFilterCombo.setItems(listaCobradores);
	}

	private void initColumns() {
		clienteColumn.setCellValueFactory(new PropertyValueFactory<>("cliente"));		
		valorCuotaColumn.setCellValueFactory(new PropertyValueFactory<>("valorCuota"));
        montoCuotaColumn.setCellValueFactory(new PropertyValueFactory<>("montoCuota"));
        montoCuotaAcumuladoColumn.setCellValueFactory(new PropertyValueFactory<>("montoCuotaAcumulado"));               
        gciaXDiaColumn.setCellValueFactory(new PropertyValueFactory<>("gciaXDia"));                
        saldoCapitalColumn.setCellValueFactory(new PropertyValueFactory<>("saldoCapital"));
        montoCreditoColumn.setCellValueFactory(new PropertyValueFactory<>("montoCredito"));               
        cuotasPagasColumn.setCellValueFactory(new PropertyValueFactory<>("cuotasPagas"));       
        cantCuotasColumn.setCellValueFactory(new PropertyValueFactory<>("cantCuotas"));        
        cobradorColumn.setCellValueFactory(new PropertyValueFactory<>("cobrador"));
        rutaColumn.setCellValueFactory(new PropertyValueFactory<>("ruta"));
        unidadColumn.setCellValueFactory(new PropertyValueFactory<>("unidad"));
	}

    public ObservableList<CreditoModel> initCreditos() {
    	
    	try {
			creditos = CreditoDAO.buscarCreditos(0);
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
    	    	
    	hashCreditos();
    	
    	return creditos;
    }

    private void hashCreditos() {
    	
    	for ( CreditoModel credito : creditos )
    		hashCreditos.put(credito.getId(), credito);
  			
	}

	public ObservableList<PagoModel> initPagos() {
    	
    	try {
			listaPagos = PagoDAO.buscarPagos();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
    	
    	for ( PagoModel pago : listaPagos ) {
    		CreditoModel credito;    		
    		credito = hashCreditos.get(pago.getIdCredito());

    		if (credito == null)
      			continue;
    		
    		credito.agregarPago(pago);
    		credito.calcularMontoAcumulado();
    		credito.calcularCuotasPagas();
    		credito.calcularSaldoCapital();
    	}
    	
    	return listaPagos;
    }    
  
	public ObservableList<ClienteModel> initClientes() {
    	
		try {
			listaClientes = ClienteDAO.buscarClientes();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}		
    	
    	hashClientes();
    	
    	return listaClientes;
    }
    
    public ObservableList<RutaModel> initRutas() {    	
    	
    	try {
			listaRutas = RutaDAO.buscarRutas();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
    	
    	hashRutas();
    	
    	return listaRutas;
    }    
    
    public ObservableList<CobradorModel> initCobradores() {    	
    	
    	try {
			listaCobradores = CobradorDAO.buscarCobradores();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
    	
    	hashCobradores();
    	
    	return listaCobradores;
    }        
    
    public ObservableList<CreditoModel> getCreditos() {
        return creditos;
    }
    
    public void refreshTableView() {
    	creditosTable.refresh();
    }
    
    public void addItemToList(CreditoModel cred) {
    	creditos.add(cred);
    }

	public ObservableList<ClienteModel> getClientes() {
		return listaClientes;
	}

	public ObservableList<ClienteModel> getListaClientes() {
		return listaClientes;
	}

	public ObservableList<RutaModel> getListaRutas() {
		return listaRutas;
	}

	public ObservableList<CobradorModel> getListaCobradores() {
		return listaCobradores;
	}
	
	private void initFiltros() {
		
	    LocalDateTime fechaHoraActual = LocalDateTime.now();	    
//		LocalDate fecha = fechaHoraActual.toLocalDate();
//	    SimpleObjectProperty<LocalDate> fecha = new SimpleObjectProperty<LocalDate>( fechaHoraActual.toLocalDate() );
//		LocalTime hora = fechaHoraActual.toLocalTime();
	    fechaFilterField.setValue(fechaHoraActual.toLocalDate());					
		
	}	
}