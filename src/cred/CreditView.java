package cred;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CreditView extends Stage {

	CreditoModel credito;
	CreditListView clntView;
	
	Scene scene;
	
    TextField fldCliente;
    TextField fldCantDias;
    TextField fldMontoCred;
    TextField fldMontoCuota;
    TextField fldTasaInt;
    TextField fldGciaXDia;
    TextField fldCobrador;

    protected GridPane gpane = new GridPane();

    TableRow<CreditoModel> row;
    
	public CreditView(CreditListView clntView) {

        this.getIcons().add(new Image("file:icons/1493858779_Business.png"));
        
		this.clntView = clntView;
		
		this.setTitle("Alta de crédito");
		
		Label lblCliente = new Label("Cliente");
        fldCliente = new TextField();
        
        Label lblCantDias = new Label("Cant. Días");
        fldCantDias = new TextField();
        
        Label lblMontoCred = new Label("Monto Crédito");
        fldMontoCred = new TextField();
        
        Label lblMontoCuota = new Label("Monto Cuota");
        fldMontoCuota = new TextField();
                
        Label lblTasaInt = new Label("Tasa interés");        
        fldTasaInt = new TextField();
           
        Label lblGciaXDia = new Label("Ganancia x día");
        fldGciaXDia = new TextField();
        
        Label lblCobrador = new Label("Cobrador");
        fldCobrador = new TextField();        
        
        fldCantDias.setMaxWidth(40);
                              
        gpane.getChildren().addAll(lblCliente, fldCliente, lblCantDias, fldCantDias,
        						   lblMontoCred, fldMontoCred,
        						   lblMontoCuota, fldMontoCuota,
        						   lblTasaInt, fldTasaInt,
        						   lblGciaXDia, fldGciaXDia,
        						   lblCobrador, fldCobrador);
    
        GridPane.setConstraints(lblCliente, 0, 0);
        GridPane.setConstraints(fldCliente, 1, 0);
        GridPane.setConstraints(lblCantDias, 0, 1);       
        GridPane.setConstraints(fldCantDias, 1, 1);   
        GridPane.setConstraints(lblMontoCred, 0, 2); 
        GridPane.setConstraints(fldMontoCred, 1, 2);     
        GridPane.setConstraints(lblMontoCuota, 0, 3);
        GridPane.setConstraints(fldMontoCuota, 1, 3);
        GridPane.setConstraints(lblTasaInt, 0, 4); 
        GridPane.setConstraints(fldTasaInt, 1, 4); 
        GridPane.setConstraints(lblGciaXDia, 0, 5); 
        GridPane.setConstraints(fldGciaXDia, 1, 5);
        GridPane.setConstraints(lblCobrador, 0, 6);      
        GridPane.setConstraints(fldCobrador, 1, 6);
        
//        GridPane.setHalignment(btnSimular, HPos.CENTER);        
//        GridPane.setHalignment(btnConfirmar, HPos.CENTER);
        
        gpane.setStyle("-fx-padding: 10;" +
//                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" );
//                "-fx-border-color: blue;");        
        
        gpane.setHgap(10); // hgap = 10px
        gpane.setVgap(5);  // vgap = 5px
        
        this.setHeight(300); setWidth(280);
        
        Scene scene = new Scene(gpane);
        setScene(scene);
        this.setMaxWidth(280);
        this.setMaxHeight(320);
        
        this.setMinWidth(280);
        this.setMinHeight(320);
        
        show();

	}



//	public GridPane getGpane() {
//		return gpane;
//	}	
	
}