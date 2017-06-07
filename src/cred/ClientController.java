package cred;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class ClientController {

	@FXML
	private TextField clienteNombreField;
	
	@FXML
	private TextField clienteApellidoField;
	
	@FXML
	private TextField clienteDniField;

	@FXML
	private TextField clienteDireccionField;
	
	@FXML
	private TextField clienteTelefonoField;

	@FXML
	private Button clienteGuardarButton;

	@FXML
	private TableView<ClientModel> clientesTable;	
	@FXML
	private TableColumn<ClientModel, String> clienteNombreColumn;
	@FXML
	private TableColumn<ClientModel, String> clienteApellidoColumn;
	@FXML
	private TableColumn<ClientModel, String> clienteDniColumn;
	@FXML
	private TableColumn<ClientModel, String> clienteDireccionColumn;
	@FXML
	private TableColumn<ClientModel, String> clienteTelefonoColumn;
	
    ObservableList<ClientModel> clientes = FXCollections.observableArrayList();	
	
	public ClientController() {
		clientes.add(new ClientModel("aa","bb","cc","dd","ee"));
	}
	
	@FXML
	private void initialize() {

		initColumns();		
		
		clientesTable.setItems(clientes);
			
		clienteGuardarButton.setOnAction((event) -> {
		    guardarCliente();
		    initFields();
		});		
		
		
//		Doble-click        
        clientesTable.setRowFactory( tv -> {

            TableRow<ClientModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {

                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    ClientModel rowData = row.getItem();
//                    System.out.println(rowData.getCliente());

                    clienteView(rowData, row);
                }
            });
            return row;
        });		
		
		
	}

	private void clienteView(ClientModel rowData, TableRow<ClientModel> row) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("ClienteCreditos.fxml"));
            GridPane page = (GridPane) loader.load();
            ClienteCreditosController controller = loader.<ClienteCreditosController>getController();

//            controller.setCredito(rowData, creditos);
            controller.setCliente(rowData);
//            controller.setMainController(this);            
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Creditos de Cliente");
          
            Scene scene = new Scene(page);

            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }		
		
	}

	private void guardarCliente() {
    	addItemToList(new ClientModel(clienteNombreField.getText(), clienteApellidoField.getText(), 
				  clienteDireccionField.getText(), clienteTelefonoField.getText(), 
				  clienteDniField.getText()));
	}

    private void addItemToList(ClientModel cliente) {
    	clientes.add(cliente);
//    	clientesTable.setItems(getClientes());
    }	

	private ObservableList<ClientModel> getClientes() {
		return clientes;
	}
	
	private void initColumns() {
		clienteNombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));		
        clienteApellidoColumn.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        clienteDniColumn.setCellValueFactory(new PropertyValueFactory<>("dni"));
        clienteDireccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        clienteTelefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));        
	}

	private void initFields() {
		clienteNombreField.clear();
		clienteApellidoField.clear();
		clienteDireccionField.clear();
		clienteDniField.clear();
		clienteTelefonoField.clear();
	}	
}