package cred;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ClientView extends Stage implements EventHandler<ActionEvent> {

	private String nombre;
	private String apellido;
	private String direccion;
	private String telefono;
	private String dni;
	
	private TextField fldNombre;
	private TextField fldApellido;
	private TextField fldDireccion;
	private TextField fldTelefono;
	private TextField fldDni;
	
	private Button btnGuardar;
	
	public ClientView() {

//		this.clntView = clntView;
		
		this.setTitle("Alta de Cliente");
		
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
                              
        gpane2.getChildren().addAll(lblNombre, fldNombre, lblApellido, fldApellido, lblDni, fldDni,	lblDireccion, fldDireccion,lblTelefono, fldTelefono, btnGuardar);
    
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
        

        gpane2.setStyle("-fx-padding: 10;" +
//                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" );
//                "-fx-border-color: blue;");        
        
//        secondStage = new Stage();()
        gpane2.setHgap(10); // hgap = 10px
        gpane2.setVgap(5);  // vgap = 5px
        
        this.setHeight(300); setWidth(280);
        
        Scene scene = new Scene(gpane2);
        setScene(scene);
        show();		
	}
	
    public ClientView(String nombre, String apellido, String direccion, String telefono, String dni) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.direccion = direccion;
		this.telefono = telefono;
		this.dni = dni;
		
		
		
		
	}

	//When button is clicked, handle() gets called
    //Button click is an ActionEvent (also MouseEvents, TouchEvents, etc...)
    @Override
    public void handle(ActionEvent event) {
    	
//        if (event.getSource() == btnSimular) {   
//        	
//        	
//        }
    }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}
    
    
}
