package cred;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CreditView extends Stage implements EventHandler<ActionEvent> {

	ClientView2 clntView;
	
	Scene scene;
	
    TextField fldCliente;
    TextField fldCantDias;
    TextField fldMontoCred;
    TextField fldMontoCuota;
    TextField fldTasaInt;
    TextField fldGciaXDia;
	
	Button btnSimular;
    Button btnConfirmar;
	
	
	public CreditView(ClientView2 clntView) {
		
		this.clntView = clntView;
		
		this.setTitle("Alta de crédito");
		
		GridPane gpane = new GridPane();
		
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
        
        btnSimular = new Button("Simular");
        btnConfirmar = new Button("Confirmar");
        
        btnSimular.setOnAction(this);
        
        fldCantDias.setMaxWidth(40);
                              
        gpane.getChildren().addAll(lblCliente, fldCliente, lblCantDias, fldCantDias,
        						   lblMontoCred, fldMontoCred,
        						   lblMontoCuota, fldMontoCuota,
        						   lblTasaInt, fldTasaInt,
        						   lblGciaXDia, fldGciaXDia,
        						   btnSimular, btnConfirmar);
    
     // Set the cells the buttons
        GridPane.setConstraints(lblCliente, 0, 0); // (c0, r0)
        GridPane.setConstraints(fldCliente, 1, 0); // (c0, r0)
        GridPane.setConstraints(lblCantDias, 0, 1); // (c0, r0)        
        GridPane.setConstraints(fldCantDias, 1, 1); // (c1, r0)        
        GridPane.setConstraints(lblMontoCred, 0, 2); // (c0, r0)        
        GridPane.setConstraints(fldMontoCred, 1, 2); // (c1, r0)        
        GridPane.setConstraints(lblMontoCuota, 0, 3); // (c1, r0)
        GridPane.setConstraints(fldMontoCuota, 1, 3); // (c1, r0)
        GridPane.setConstraints(lblTasaInt, 0, 4); // (c1, r0)
        GridPane.setConstraints(fldTasaInt, 1, 4); // (c1, r0)
        GridPane.setConstraints(lblGciaXDia, 0, 5); // (c1, r0)
        GridPane.setConstraints(fldGciaXDia, 1, 5); // (c1, r0)
        GridPane.setConstraints(fldGciaXDia, 1, 5); // (c1, r0)        
        GridPane.setConstraints(btnSimular, 0, 6); // (c1, r0)
        GridPane.setConstraints(btnConfirmar, 1, 6); // (c1, r0)
        GridPane.setHalignment(btnSimular, HPos.CENTER);        
        GridPane.setHalignment(btnConfirmar, HPos.CENTER);
        

        gpane.setStyle("-fx-padding: 10;" +
//                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" );
//                "-fx-border-color: blue;");        
        
//        secondStage = new Stage();()
        gpane.setHgap(10); // hgap = 10px
        gpane.setVgap(5);  // vgap = 5px
        
        this.setHeight(300); setWidth(280);
        
        Scene scene = new Scene(gpane);
        setScene(scene);
        show();
//        window.setScene(scene2);
//        window.setHeight(600);window.setWidth(600);
//        window.show();	        
//        button.setOnAction(this);		
		
	}

    //When button is clicked, handle() gets called
    //Button click is an ActionEvent (also MouseEvents, TouchEvents, etc...)
    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == btnSimular) { 
        	
        	simular();
        	
        } else {
//        	secondStage.close();
        }        
    }

	private void simular() {

		float montoCuota;
		float cuotaCapital;
		
		if (fldCliente.getText().equals("")) {
			System.out.println("Ingrese Cliente");
			
			return;
		} 
		
		if (fldCantDias.getText().equals("")) {
			System.out.println("Ingrese Cantidad de días");
			return;
		}		
		
		cuotaCapital = Float.valueOf(fldMontoCred.getText()) / Float.valueOf(fldCantDias.getText());
		
		montoCuota = cuotaCapital + ( cuotaCapital * ( Float.valueOf(fldTasaInt.getText()) / 100 )); //Float.valueOf("100") )); 

		fldMontoCuota.setText(String.valueOf(montoCuota));

		fldGciaXDia.setText(String.valueOf(montoCuota - cuotaCapital));
		
	}	
	
}
