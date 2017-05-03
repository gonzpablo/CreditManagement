package cred;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ClientView extends Stage implements EventHandler<ActionEvent> {
	
	private TextField fldNombre;
	private TextField fldApellido;
	private TextField fldDireccion;
	private TextField fldTelefono;
	private TextField fldDni;
	
	private Button btnGuardar;

    TableView<ClientModel> table;	
    ObservableList<ClientModel> clientes = FXCollections.observableArrayList();
    
	public ClientView() {

//		this.clntView = clntView;
		
		this.setTitle("Alta de Cliente");
		
        this.getIcons().add(new Image("file:1493858779_Business.png"));		
		
		GridPane gpane2 = new GridPane();
		
		Label lblNombre = new Label("Nombre");
        fldNombre = new TextField();
        
        Label lblApellido = new Label("Apellido");
        fldApellido = new TextField();
        
        Label lblDni = new Label("D.N.I.");
        fldDni = new TextField();
        
        Label lblDireccion = new Label("Dirección");
        fldDireccion = new TextField();
                
        Label lblTelefono = new Label("Telefono");        
        fldTelefono = new TextField();
                  
        btnGuardar = new Button("Guardar");      

        btnGuardar.setOnAction(this);
        
//        fldApellido.setMaxWidth(40);
                              
        TableView<ClientModel> tableCli = listaClientes();
        
        gpane2.getChildren().addAll(lblNombre, fldNombre, lblApellido, fldApellido, 
        							lblDni, fldDni,	lblDireccion, fldDireccion,
        							lblTelefono, fldTelefono, btnGuardar, tableCli);
    
     // Set the cells the buttons
        GridPane.setConstraints(lblNombre, 0, 0); // (c0, r0)
        GridPane.setConstraints(fldNombre, 1, 0); // (c0, r0)
        GridPane.setConstraints(lblApellido, 0, 1); // (c0, r0)        
        GridPane.setConstraints(fldApellido, 1, 1); // (c1, r0)        
        GridPane.setConstraints(lblDni, 0, 2); // (c0, r0)        
        GridPane.setConstraints(fldDni, 1, 2); // (c1, r0)        
        GridPane.setConstraints(lblDireccion, 0, 3); // (c1, r0)
        GridPane.setConstraints(fldDireccion, 1, 3); // (c1, r0)
        GridPane.setConstraints(lblTelefono, 0, 4); // (c1, r0)
        GridPane.setConstraints(fldTelefono, 1, 4); // (c1, r0)

        GridPane.setConstraints(btnGuardar, 0, 5); // (c1, r0)
        GridPane.setHalignment(btnGuardar, HPos.CENTER);
        
        GridPane.setConstraints(tableCli, 0, 6); // (c1, r0)
        
        GridPane.setColumnSpan(tableCli, 2);        
        
        gpane2.setStyle("-fx-padding: 10;" +
//                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" );
//                "-fx-border-color: blue;");        

        gpane2.setHgap(10); // hgap = 10px
        gpane2.setVgap(5);  // vgap = 5px
        
        this.setHeight(500); setWidth(500);
        
        Scene scene = new Scene(gpane2);
        setScene(scene);
        show();		
	}
	
	//When button is clicked, handle() gets called
    //Button click is an ActionEvent (also MouseEvents, TouchEvents, etc...)
    @Override
    public void handle(ActionEvent event) {
 
        if (event.getSource() == btnGuardar) { 
        	
        	addItemToList(new ClientModel(this.getFldNombre().getText(), this.getFldApellido().getText(), 
        								  this.getFldDireccion().getText(), this.getFldTelefono().getText(), 
        								  this.getFldDni().getText()));
        	initFields();
        }
    }

	private TableView<ClientModel> listaClientes() {

        // Nombre
        TableColumn<ClientModel, String> nombre = new TableColumn<>("Nombre");
        nombre.setMinWidth(100);
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        // Apellido
        TableColumn<ClientModel, String> apellido = new TableColumn<>("Apellido");
        apellido.setMinWidth(80);
        apellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));

        // Dirección
        TableColumn<ClientModel, String> direccion = new TableColumn<>("Dirección");
        direccion.setMinWidth(60);
        direccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
                
        // Telefono
        TableColumn<ClientModel, String> telefono = new TableColumn<>("Telefono");
        telefono.setMinWidth(60);
        telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        // DNI
        TableColumn<ClientModel, Float> dni = new TableColumn<>("DNI");
        dni.setMinWidth(100);
        dni.setCellValueFactory(new PropertyValueFactory<>("dni"));        
        
        table = new TableView<>();
        table.setItems(initClientes());
        table.getColumns().addAll(nombre, apellido, direccion, telefono, dni);
        
        return table;
	}
	
	private ObservableList<ClientModel> initClientes() {
		return clientes;
	}
	
	private ObservableList<ClientModel> getClientes() {
		return clientes;
	}
	
    private void addItemToList(ClientModel cliente) {
    	clientes.add(cliente);
    	table.setItems(getClientes());
    }

	public TextField getFldNombre() {
		return fldNombre;
	}

	public void setFldNombre(TextField fldNombre) {
		this.fldNombre = fldNombre;
	}

	public TextField getFldApellido() {
		return fldApellido;
	}

	public void setFldApellido(TextField fldApellido) {
		this.fldApellido = fldApellido;
	}

	public TextField getFldDireccion() {
		return fldDireccion;
	}

	public void setFldDireccion(TextField fldDireccion) {
		this.fldDireccion = fldDireccion;
	}

	public TextField getFldTelefono() {
		return fldTelefono;
	}

	public void setFldTelefono(TextField fldTelefono) {
		this.fldTelefono = fldTelefono;
	}

	public TextField getFldDni() {
		return fldDni;
	}

	public void setFldDni(TextField fldDni) {
		this.fldDni = fldDni;
	}
	
	private void initFields() {
		fldNombre.clear();
		fldApellido.clear();
		fldDireccion.clear();
		fldDni.clear();
		fldTelefono.clear();
	}
}