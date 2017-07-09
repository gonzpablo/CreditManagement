package com.cred.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.cred.model.ClienteDAO;
import com.cred.model.ClienteModel;
import com.cred.model.CobradorDAO;
import com.cred.model.CobradorModel;
import com.cred.model.CreditoModel;
import com.cred.util.DBUtil;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class CobradorController {

	@FXML
	private TextField cobradorNombreField;	
	@FXML
	private TextField cobradorApellidoField;
	
	@FXML
	private TableView<CobradorModel> cobradoresTable;	
	@FXML
	private TableColumn<CobradorModel, String> cobradorNombreColumn;
	@FXML
	private TableColumn<CobradorModel, String> cobradorApellidoColumn;
		
	@FXML
	private Button cobradorGuardarButton;
	@FXML
	private Button cobradorNuevoButton;
	@FXML
	private Button cobradorBorrarButton;		
	
	
	
	private ObservableList<CobradorModel> cobradores;
	
//	Referencia al cliente que se está editando (Nuevo o modificación)
	private CobradorModel cobrador = null;
	
	
	public CobradorController() {}
	
	@FXML
	private void initialize() {

		initColumns();		

		cobradorGuardarButton.setOnAction( event -> {
		    guardarCobrador();
		    initFields();
        	grisarCampos(true);
		});		
		
		cobradorNuevoButton.setOnAction( event -> { 
			nuevoCobrador();
		});
		
		cobradorBorrarButton.setOnAction( event -> { borrarCobrador(); });
		
//		Doble-click        
        cobradoresTable.setRowFactory( tv -> {

            TableRow<CobradorModel> row = new TableRow<>();
            
            row.setOnMouseClicked( event -> {

            	if (event.getClickCount() > 2 )
            		return;

                if (row.isEmpty())
                	return;

                cobradorBorrarButton.setDisable(false);
                
                CobradorModel rowData = row.getItem();

                switch (event.getClickCount()) {

	                case 1:
	                	grisarCampos(true);
	                	cargarCobrador(rowData);
	                	break;
	                case 2:
//	                	clienteView(rowData);
	                	grisarCampos(false);
	                	cargarCobrador(rowData);
	                	break;
                }                       
            });
            return row;
        });
        
        if (cobradoresTable.getSelectionModel().getSelectedItem() == null)
        	cobradorBorrarButton.setDisable(true);	
        
	}

	private void nuevoCobrador() {
		grisarCampos(false);
		initFields();
		this.cobrador = null;		
	}

	private void cargarCobrador(CobradorModel cobrador) {
		
		this.cobrador = cobrador;
		cobradorNombreField.setText(cobrador.getNombre());		
		cobradorApellidoField.setText(cobrador.getApellido());
	}

	private void grisarCampos(Boolean value) {
		cobradorNombreField.setDisable(value);		
		cobradorApellidoField.setDisable(value);
		
		cobradorGuardarButton.setDisable(value);
	}		

	private void guardarCobrador() {
		
		if (this.cobrador == null) {

			CobradorModel cobradorNew = new CobradorModel();
			
			cobradorNew.setNombre(cobradorNombreField.getText());
			cobradorNew.setApellido(cobradorApellidoField.getText());
			
	    	try {
				CobradorDAO.agregarCobrador(cobradorNew);
				cobradorNew.setId(DBUtil.getLastRowId("clientes"));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}    	
			
			addItemToList(cobradorNew);
			
		} else {
			try {
				
				this.cobrador.setNombre(cobradorNombreField.getText());
				this.cobrador.setApellido(cobradorApellidoField.getText());
				
				CobradorDAO.modificarCobrador(cobrador);			
				this.cobrador = null;
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}			
		}  	
	}

    private void addItemToList(CobradorModel cobrador) {
    	cobradores.add(cobrador);
    }	
	
	private void initColumns() {
		cobradorNombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());		
        cobradorApellidoColumn.setCellValueFactory(cellData -> cellData.getValue().apellidoProperty());
	}

	private void initFields() {
		cobradorNombreField.clear();
		cobradorApellidoField.clear();
	}

	public void setCobradores(ObservableList<CobradorModel> cobradores) {
		this.cobradores = cobradores;
		cobradoresTable.setItems(cobradores);
	}
	
	private void borrarCobrador() {
		CobradorModel cobrador = cobradoresTable.getSelectionModel().getSelectedItem();
	
		if (cobrador == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error al borrar un cobrador");
			alert.setContentText("Por favor seleccione un cobrador a borrar");
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
		
		cobradores.remove(cobrador);
//		credito.calcular();
		cobrador.borrar();
		initFields();		
		
//        TableRow<ClienteModel> row = new TableRow<>();
//        
//        row.setOnMouseClicked( event -> {
//
//        	if (event.getClickCount() > 2 )
//        		return;
//
//            if (row.isEmpty())
//            	return;
//
//            clienteBorrarButton.setDisable(false);
//            
//            ClienteModel rowData = row.getItem();
//
//            switch (event.getClickCount()) {
//
//                case 1:
//                	grisarCampos(true);
		cobrador = cobradoresTable.getSelectionModel().getSelectedItem();
		if (cobrador == null)
			return;
                	cargarCobrador(cobrador);
//                	break;		
		
	}
}