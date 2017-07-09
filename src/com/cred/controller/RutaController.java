package com.cred.controller;

import java.sql.SQLException;

import com.cred.model.CobradorDAO;
import com.cred.model.CobradorModel;
import com.cred.model.RutaDAO;
import com.cred.model.RutaModel;
import com.cred.util.DBUtil;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class RutaController {

	@FXML
	private TextField rutaNombreField;	
	@FXML
	private TextArea rutaDescTextArea;
	
	@FXML
	private TableView<RutaModel> rutasTable;	
	@FXML
	private TableColumn<RutaModel, String> rutaNombreColumn;
	@FXML
	private TableColumn<RutaModel, String> rutaDescColumn;
		
	@FXML
	private Button rutaGuardarButton;
	@FXML
	private Button rutaNuevoButton;
	@FXML
	private Button rutaBorrarButton;		
	
	
	private ObservableList<RutaModel> rutas;
	
//	Referencia al cliente que se está editando (Nuevo o modificación)
	private RutaModel ruta = null;
	
	
	public RutaController() {}
	
	@FXML
	private void initialize() {

		initColumns();		

		rutaGuardarButton.setOnAction( event -> {
		    guardarRuta();
		    initFields();
        	grisarCampos(true);
		});		
		
		rutaNuevoButton.setOnAction( event -> { 
			nuevaRuta();
		});
		
		rutaBorrarButton.setOnAction( event -> { borrarRuta(); });
		
//		Doble-click        
        rutasTable.setRowFactory( tv -> {

            TableRow<RutaModel> row = new TableRow<>();
            
            row.setOnMouseClicked( event -> {

            	if (event.getClickCount() > 2 )
            		return;

                if (row.isEmpty())
                	return;

                rutaBorrarButton.setDisable(false);
                
                RutaModel rowData = row.getItem();

                switch (event.getClickCount()) {

	                case 1:
	                	grisarCampos(true);
	                	cargarRuta(rowData);
	                	break;
	                case 2:
//	                	clienteView(rowData);
	                	grisarCampos(false);
	                	cargarRuta(rowData);
	                	break;
                }                       
            });
            return row;
        });
        
        if (rutasTable.getSelectionModel().getSelectedItem() == null)
        	rutaBorrarButton.setDisable(true);	
        
	}

	private void nuevaRuta() {
		grisarCampos(false);
		initFields();
		this.ruta = null;		
	}

	private void cargarRuta(RutaModel ruta) {
		
		this.ruta = ruta;
		rutaNombreField.setText(ruta.getNombre());		
		rutaDescTextArea.setText(ruta.getDescripcion());
	}

	private void grisarCampos(Boolean value) {
		rutaNombreField.setDisable(value);		
		rutaDescTextArea.setDisable(value);
		
		rutaGuardarButton.setDisable(value);
	}		

	private void guardarRuta() {
		
		if (this.ruta == null) {

			RutaModel rutaNew = new RutaModel();
			
			rutaNew.setNombre(rutaNombreField.getText());
			rutaNew.setDescripcion(rutaDescTextArea.getText());
			
	    	try {
				RutaDAO.agregarRuta(rutaNew);
				rutaNew.setId(DBUtil.getLastRowId("rutas"));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}    	
			
			addItemToList(rutaNew);
			
		} else {
			try {
				
				this.ruta.setNombre(rutaNombreField.getText());
				this.ruta.setDescripcion(rutaDescTextArea.getText());
				
				RutaDAO.modificarRuta(ruta);			
				this.ruta = null;
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}			
		}  	
	}

    private void addItemToList(RutaModel ruta) {
    	rutas.add(ruta);
    }	
	
	private void initColumns() {
		rutaNombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());		
        rutaDescColumn.setCellValueFactory(cellData -> cellData.getValue().descripcionProperty());
	}

	private void initFields() {
		rutaNombreField.clear();
		rutaDescTextArea.clear();
	}

	public void setRutas(ObservableList<RutaModel> rutas) {
		this.rutas = rutas;
		rutasTable.setItems(rutas);
	}
	
	private void borrarRuta() {
		RutaModel ruta = rutasTable.getSelectionModel().getSelectedItem();
	
		if (ruta == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error al borrar una ruta");
			alert.setContentText("Por favor seleccione una ruta a borrar");
			alert.showAndWait();									
			return;
		}
			
//		if (cliente.tieneCreditos()) {
//			Alert alert = new Alert(AlertType.ERROR);
//			alert.setTitle("Error");
//			alert.setHeaderText("Error al borrar un cliente");
//			alert.setContentText("Por favor elimine los creditos del cliente");
//			alert.showAndWait();									
//			return;
//		}
		
		rutas.remove(ruta);
		ruta.borrar();
		initFields();		
		
		ruta = rutasTable.getSelectionModel().getSelectedItem();
		if (ruta == null)
			return;
        
		cargarRuta(ruta);
	}
}