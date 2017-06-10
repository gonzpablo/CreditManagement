package cred;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ClienteSearchController {

	@FXML
	private TextField nombreFilterField;
	@FXML
	private Button limpiarFiltrosButton;
		
	@FXML
	private TableColumn<ClientModel, String> nombreColumn;
	@FXML
	private TableColumn<ClientModel, String> apellidoColumn;
	@FXML
	private TableColumn<ClientModel, String> direccionColumn;
	@FXML
	private TableColumn<ClientModel, String> telefonoColumn;
	@FXML
	private TableColumn<ClientModel, String> dniColumn;
	@FXML
	private TableView<ClientModel> clientesTable;
	
	private ObservableList<ClientModel> clientes; // = FXCollections.observableArrayList();	

	private CreditoController creditoController;
		

	public ClienteSearchController() {
		
	}
	
	@FXML
	private void initialize() {	
		initColumns();
		
//		Doble-click        
        clientesTable.setRowFactory( tv -> {

            TableRow<ClientModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {

                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    ClientModel rowData = row.getItem();
//                    System.out.println(rowData.getCliente());

                    setCliente(rowData, row);
                }
            });
            return row;
        });
		
	}

	private void setCliente(ClientModel rowData, TableRow<ClientModel> row) {
		creditoController.setCliente(rowData);		
	    // get a handle to the stage
	    Stage stage = (Stage) row.getScene().getWindow();
	    // do what you have to do
	    stage.close();		
	}

	private void initColumns() {
		nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));		
		apellidoColumn.setCellValueFactory(new PropertyValueFactory<>("apellido"));
		direccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));
		telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
		dniColumn.setCellValueFactory(new PropertyValueFactory<>("dni"));
	}

	public void setClientes(ObservableList<ClientModel> clientes) {
		this.clientes = clientes;
		clientesTable.setItems(this.clientes);
	}

	public void setMainController(CreditoController creditoController) {
		this.creditoController = creditoController;		
	}	
}