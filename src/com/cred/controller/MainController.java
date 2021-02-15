package com.cred.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import com.cred.Main;
import com.cred.model.ClienteDAO;
import com.cred.model.ClienteModel;
import com.cred.model.CobradorDAO;
import com.cred.model.CobradorModel;
import com.cred.model.CreditoDAO;
import com.cred.model.CreditoModel;
import com.cred.model.PagoDAO;
import com.cred.model.PagoModel;
import com.cred.model.Reporte;
import com.cred.model.RutaDAO;
import com.cred.model.RutaModel;

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
	@FXML	
	private TableColumn<CreditoModel, LocalDate> fechaCreacionColumn;		
//  -------------------------------------------------------------------
//  Filtros	
//  -------------------------------------------------------------------	
	@FXML
	private ComboBox<RutaModel> rutaFilterCombo;
	@FXML
	private ComboBox<CobradorModel> cobradorFilterCombo;	
	@FXML
	private DatePicker fechaFilterField;
//  -------------------------------------------------------------------	
	@FXML
	private Button btnCleanFilters;
	@FXML
	private Button btnBorrarCreditos;
	@FXML
	private Button btnModificarCreditos;
//  -------------------------------------------------------------------
//  Sumarizadores
//  -------------------------------------------------------------------
	@FXML
	private TextField cuotaPuraField;
	@FXML
	private TextField gciaDiaField;
//  -------------------------------------------------------------------
//  Menú
//  -------------------------------------------------------------------	
	@FXML
	private MenuItem salirMenu;
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
//  -------------------------------------------------------------------	

	private ObservableList<CreditoModel> creditos = FXCollections.observableArrayList();
	
	private final FilteredList<CreditoModel> filteredItems;

	private ObservableList<ClienteModel> listaClientes = FXCollections.observableArrayList();
	private ObservableList<RutaModel> listaRutas = FXCollections.observableArrayList();	
	private ObservableList<CobradorModel> listaCobradores = FXCollections.observableArrayList();	
	private ObservableList<PagoModel> listaPagos = FXCollections.observableArrayList();
	
	private final Map<Integer, ClienteModel> hashClientes = new HashMap<>();
	private final Map<Integer, RutaModel> hashRutas = new HashMap<>();
	private final Map<Integer, CobradorModel> hashCobradores = new HashMap<>();
	private final Map<Integer, CreditoModel> hashCreditos = new HashMap<>();
	

	public MainController() {	

		buscarDatos();			
		
//		Completar en los créditos, las referencias a clientes, cobradores, rutas y pagos	
		completarCreditos(creditos);
		
		filteredItems = new FilteredList<>(creditos, p -> true);
	}	
		
	private void buscarDatos() {

		listaClientes = buscarClientes();		// Busca todos los clientes en BD y carga hashClientes
		listaRutas = buscarRutas();				// Busca las rutas y carga hashRutas
		listaCobradores = buscarCobradores();	// Busca los cobradores y carga hashCobradores
		listaPagos = buscarPagos(0);  			// Buscar pagos correspondientes a créditos abiertos (no cerrados)
		creditos = buscarCreditos(); 			// Buscar creditos no cerrados		
	}
	
	@FXML
	private void initialize() {

		initFiltros();			// Cargar fecha del día en filtro de fecha
		
		defineActions();		// Buttons onAction	
		
		initColumns();
		
		initCombos();       
				
		filtrosYDatosTabla();

        eventosTabla();			// Doble click en un registro
        
        calcularTotales();		// Actualizar los campos de abajo (sumaCuotaPura y sumaGciaXDia)
	}	
	
	private void eventosTabla() {
//		Doble-click        
        creditosTable.setRowFactory( tv -> {

            TableRow<CreditoModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {

                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    CreditoModel rowData = row.getItem();
                    pago(rowData, row);
                }
            });
            return row;
        });
	}

	private void filtrosYDatosTabla() {
		
        ObjectProperty<Predicate<CreditoModel>> cobradorFilter = new SimpleObjectProperty<>();
        ObjectProperty<Predicate<CreditoModel>> rutaFilter = new SimpleObjectProperty<>();			
        
        cobradorFilter.bind(Bindings.createObjectBinding(() ->        
		credito ->
			cobradorFilterCombo.getValue() == null || cobradorFilterCombo.getValue() == credito.getCobradorRef(),
			cobradorFilterCombo.valueProperty()));              

        rutaFilter.bind(Bindings.createObjectBinding(() ->
		credito -> 
			rutaFilterCombo.getValue() == null || rutaFilterCombo.getValue() == credito.getRutaRef(),
			rutaFilterCombo.valueProperty()));		

        filteredItems.predicateProperty().bind(Bindings.createObjectBinding(() -> 
				cobradorFilter.get().and(rutaFilter.get()), cobradorFilter, rutaFilter));
        
        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<CreditoModel> sortedData = new SortedList<>(filteredItems);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(creditosTable.comparatorProperty());
        
        // 5. Add sorted (and filtered) data to the table.
        creditosTable.setItems(sortedData);
	}

	private void initCombos() {
		initComboCobrador();
		initComboRuta();		
	}

	private void defineActions() {

//		Filtros
        cobradorFilterCombo.setOnAction(e -> calcularTotales());
        rutaFilterCombo.setOnAction(e -> calcularTotales());
        fechaFilterField.setOnAction(e -> calcularTotales());
        
        btnCleanFilters.setOnAction(e -> {
            rutaFilterCombo.setValue(null);
            cobradorFilterCombo.setValue(null);
            fechaFilterField.setValue(null);
            calcularTotales();
        });            

//		Menú
        salirMenu.setOnAction( e-> salir());
        crearCreditoMenu.setOnAction( e -> crearCredito());
        clienteMenuGestionar.setOnAction( e -> gestClientes());
        cobradorMenuGestionar.setOnAction( e -> gestCobradores());
        rutasMenuGestionar.setOnAction( e -> gestRutas());
        reporteMenu.setOnAction( e -> reporte());

//		Botones        
        btnModificarCreditos.setOnAction( (event) -> modificarCredito());
        btnBorrarCreditos.setOnAction( (event) -> borrarCredito());
	}

	private void salir() {
	    Stage stage = (Stage) btnBorrarCreditos.getScene().getWindow();
	    stage.close();			
	}

	private void hashClientes(ObservableList<ClienteModel> clientes) {

		for (ClienteModel cliente : clientes)			
			hashClientes.put(cliente.getId(), cliente);		
	}

	private void hashRutas(ObservableList<RutaModel> rutas) {
		
		for (RutaModel ruta : rutas)
			hashRutas.put(ruta.getId(), ruta);
	}

	private void hashCobradores(ObservableList<CobradorModel> cobradores) {
		
		for (CobradorModel cobrador : cobradores )
			hashCobradores.put(cobrador.getId(), cobrador);
	}

	private void completarCreditos(ObservableList<CreditoModel> listaCreditos) {
		
//		Agregar los pagos a los créditos
		for ( PagoModel pago : listaPagos ) {
			CreditoModel credito = hashCreditos.get(pago.getIdCredito());

			if (credito == null)
				continue;

			credito.agregarPago(pago);
		}		
		
//		Utilizar este método sólo para creditos cargados desde la base de datos
//		ya que utilizan los id (Cliente, cobrador y ruta) que no se están seteando
//		para los objetos creados en el momento (estos otros ya tienen la referencia
//		al objeto cargada.		
		for ( CreditoModel credito : listaCreditos ) {

			//	Referencia al Cliente
			credito.setCliente(hashClientes.get(credito.getIdCliente()));
			//	Referencia al Cobrador			
			credito.setCobrador(hashCobradores.get(credito.getIdCobrador()));
			//	Referencia a la Ruta
			credito.setRuta(hashRutas.get(credito.getIdRuta()));
			
			credito.calcularMontoAcumulado();
			credito.calcularCuotasPagas();
			credito.calcularSaldoCapital();			
		}
	}

	private void reporte() {

		Stage stage = new Stage();
		Reporte rep = new Reporte(stage);
		
		try {
			rep.reporteRuta(filteredItems);
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
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("¡ Atención !");
		alert.setHeaderText("Borrar crédito");
		alert.setContentText("¿Está seguro que desea borrar el crédito seleccionado?");

		Optional<ButtonType> result = alert.showAndWait();
	
		if (!(result.get() == ButtonType.OK))
		    return;
		
		hashCreditos.remove(credito.getId());
		creditos.remove(credito);
		credito.calcular();
		credito.borrarCredito();
		
		calcularTotales();		
	}

	private void crearCredito() {
	   try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Credito.fxml"));
            GridPane page = loader.load();
            CreditoController controller = loader.getController();

            controller.setMainController(this);
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Crear Crédito");
          
            Scene scene = new Scene(page);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(new Image("file:icons/calculator.png"));            
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	private void gestClientes() {
	   try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Cliente.fxml"));
            GridPane page = loader.load();
            ClienteController controller = loader.getController();

            controller.setClientes(listaClientes);  
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Gestionar Clientes");
          
            Scene scene = new Scene(page);

            stage.setResizable(false);
            stage.setScene(scene);
            stage.getIcons().add(new Image("file:icons/users.png"));            
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }		
	}

	private void gestCobradores() {
	   try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Cobrador.fxml"));
            GridPane page = loader.load();
            CobradorController controller = loader.getController();

            controller.setCobradores(listaCobradores);
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Gestionar Cobradores");
          
            Scene scene = new Scene(page);

            stage.setResizable(false);
            stage.setScene(scene);
            stage.getIcons().add(new Image("file:icons/contacts.png"));            
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }		
	}	
	
	private void modificarCredito() {
		
		CreditoModel credito = creditosTable.getSelectionModel().getSelectedItem();
		
		if (credito == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error al borrar un crédito");
			alert.setContentText("Por favor seleccione un crédito a modificar");
			alert.showAndWait();									
			return;
		}

		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/CreditoModificar.fxml"));
			GridPane page = loader.load();
			CreditoModController controller = loader.getController();

			controller.setCredito(credito);
			controller.setMainController(this);
			controller.cargarDatos();
			
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Modificar Crédito");

			Scene scene = new Scene(page);

			stage.setResizable(false);
			stage.setScene(scene);
            stage.getIcons().add(new Image("file:icons/Cred.png"));
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void gestRutas() {
	
	   try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Ruta.fxml"));
            GridPane page = loader.load();
            RutaController controller = loader.getController();

            controller.setRutas(listaRutas);
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Gestionar Rutas");
          
            Scene scene = new Scene(page);

            stage.setResizable(false);
            stage.setScene(scene);
            stage.getIcons().add(new Image("file:icons/route.png"));            
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }		
	}		
	
	public void refrescar() {
		creditosTable.refresh();
	}
	
	private void pago(CreditoModel rowData, TableRow<CreditoModel> row) {

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Pago.fxml"));
            GridPane page = loader.load();
            PagoController controller = loader.getController();

            controller.setCredito(rowData);            
            controller.setMainController(this);            
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Gestionar Pagos");
            stage.getIcons().add(new Image("file:icons/moneybox.png"));            
            stage.setResizable(false);
            
            Scene scene = new Scene(page);

            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public void calcularTotales() {
		
		if (fechaFilterField.getValue() == null) {

			cuotaPuraField.setAlignment(Pos.CENTER_LEFT);
			cuotaPuraField.setText("Seleccione una fecha");
			gciaDiaField.setText(null);
			cuotaPuraField.setDisable(true);
			gciaDiaField.setDisable(true);
			
			for ( CreditoModel cred : filteredItems ) {				
				cred.setMontoCuota(BigDecimal.valueOf(0));
				cred.setGciaXDia(BigDecimal.valueOf(0));
			}			
			
		} else {
			cuotaPuraField.setAlignment(Pos.CENTER_RIGHT);
			cuotaPuraField.setDisable(false);
			gciaDiaField.setDisable(false);	
			
			BigDecimal sumaCuotaPura = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP),
					   sumaGciaXDia = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP);
			
				for ( CreditoModel cred : filteredItems ) {
					sumaCuotaPura = sumaCuotaPura.add(cred.getMontoCuota(fechaFilterField.getValue()));
					sumaGciaXDia = sumaGciaXDia.add(cred.getGciaXDia(fechaFilterField.getValue()));
				}
	
	    	cuotaPuraField.setText(String.valueOf(sumaCuotaPura.setScale(2, RoundingMode.HALF_UP)));
	    	gciaDiaField.setText(String.valueOf(sumaGciaXDia.setScale(2, RoundingMode.HALF_UP)));
		}
		creditosTable.refresh();
	}

    private void initComboRuta() {
		rutaFilterCombo.setItems(listaRutas);

        rutaFilterCombo.setConverter(new StringConverter<RutaModel>() {
            @Override
            public String toString(RutaModel ruta) {
            	if (ruta == null)
            		return null;
				else
                	return ruta.getNombre() + " - " + ruta.getDescripcion();
            }

            @Override
            public RutaModel fromString(String string) {
                return null;
            }
        });
	}

    private void initComboCobrador() {
    	cobradorFilterCombo.setItems(listaCobradores);

        cobradorFilterCombo.setConverter(new StringConverter<CobradorModel>() {
            @Override
            public String toString(CobradorModel cobrador) {
            	if (cobrador == null)
            		return null;
            	else
                	return cobrador.getNombre() + " " + cobrador.getApellido();
            }

            @Override
            public CobradorModel fromString(String string) {
                return null;
            }
        });
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
        
		fechaCreacionColumn.setCellValueFactory(cellData -> cellData.getValue().getFecha());
		        
        DateTimeFormatter format = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
        fechaCreacionColumn.setCellFactory (getDateCell(format));                
        
        
        valorCuotaColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");
        montoCuotaColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");
        montoCuotaAcumuladoColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");
        gciaXDiaColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");
        saldoCapitalColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");
        montoCreditoColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");
        
	}

    public ObservableList<CreditoModel> buscarCreditos() {
    	
    	ObservableList<CreditoModel> creditosBD = FXCollections.observableArrayList();
    	
    	try {
			creditosBD = CreditoDAO.buscarCreditos();
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
    	    	
    	hashCreditos(creditosBD);
    	
    	return creditosBD;
    }

    private void hashCreditos(ObservableList<CreditoModel> listaCreditos) {
    	
    	for ( CreditoModel credito : listaCreditos )

			if (!hashCreditos.containsKey(credito.getId())) {
				hashCreditos.put(credito.getId(), credito);
			}
	}

	public ObservableList<PagoModel> buscarPagos(int cerrado) {

		ObservableList<PagoModel> pagos = FXCollections.observableArrayList();
		
		try {
			pagos = PagoDAO.buscarPagos(cerrado);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		return pagos;
	}

	public ObservableList<ClienteModel> buscarClientes() {
    	
		ObservableList<ClienteModel> clientes = FXCollections.observableArrayList();
		
		try {
			clientes = ClienteDAO.buscarClientes();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}		
    	
    	hashClientes(clientes);
    	
    	return clientes;
    }
    
    public ObservableList<RutaModel> buscarRutas() {    	
    	
    	ObservableList<RutaModel> rutas = FXCollections.observableArrayList();
    	
    	try {
			rutas = RutaDAO.buscarRutas();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
    	
    	hashRutas(rutas);
    	
    	return rutas;
    }    

    public ObservableList<CobradorModel> buscarCobradores() {    	
    	
    	ObservableList<CobradorModel> cobradores = FXCollections.observableArrayList();
    	
    	try {
			cobradores = CobradorDAO.buscarCobradores();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
    	
    	hashCobradores(cobradores);
    	
    	return cobradores;
    }        
    
    public ObservableList<CreditoModel> getCreditos() {
    	return creditos;
    }
    
    public void refreshTableView() {
    	creditosTable.refresh();
    }

    public void addItemToList(CreditoModel cred) {
    	creditos.add(cred);
    	
    	hashCreditos.put(cred.getId(), cred);
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

		fechaFilterField.setValue(fechaHoraActual.toLocalDate());
	}
	
	public static <ROW,T extends Temporal> Callback<TableColumn<ROW, T>, TableCell<ROW, T>> getDateCell (DateTimeFormatter format) {		
	  return column -> new TableCell<ROW, T> () {
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
	}		
}