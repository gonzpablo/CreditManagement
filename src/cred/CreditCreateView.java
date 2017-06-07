package cred;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class CreditCreateView extends CreditView implements EventHandler<ActionEvent> {

	private Button btnSimular;
	private Button btnConfirmar;
	
	public CreditCreateView(CreditListView clntView) {
		super(clntView);

		btnSimular = new Button("Simular");
      	btnConfirmar = new Button("Confirmar");
      
      	btnSimular.setOnAction(this);
      	btnConfirmar.setOnAction(this);
				
		super.gpane.getChildren().addAll(btnSimular, btnConfirmar);
		
		GridPane.setConstraints(btnSimular, 0, 7); // (c0, r0)
		GridPane.setConstraints(btnConfirmar, 1, 7); // (c0, r0)	
	}
	
	//When button is clicked, handle() gets called
	//Button click is an ActionEvent (also MouseEvents, TouchEvents, etc...)
	@Override
	public void handle(ActionEvent event) {
  	
      if (event.getSource() == btnSimular) {         	
//      	System.out.println("simular");        	
    	  simular();
  	
	  } else if (event.getSource() == btnConfirmar ) {

		  	if (!validar())
		  		return;
		  
	      	clntView.addItemToList(new CreditoModel(this.fldCliente.getText().toString(),
	      											Integer.valueOf(this.fldCantDias.getText()),
	      											fldTasaInt.getText(),
	      											fldMontoCred.getText(),	      											
	      											this.fldCobrador.getText().toString(),
	      											"1"));
	      	this.close();
	      }        
  }

	private boolean validar() {
			
		if (fldCliente.getText().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Error");
			alert.setContentText("Por favor ingrese el cliente");
			alert.showAndWait();			
			return false;
		} 

		if (fldMontoCred.getText().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Error");
			alert.setContentText("Ingrese el monto del crédito");
			alert.showAndWait();							
			return false;
		}						
		
		if (fldCantDias.getText().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Error");
			alert.setContentText("Ingrese la cantidad de días");
			alert.showAndWait();							
			return false;
		}		

		if (fldTasaInt.getText().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Error");
			alert.setContentText("Ingrese la tasa de interés mensual");
			alert.showAndWait();							
			return false;
		}				
	
		if (fldCobrador.getText().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Atención");
			alert.setHeaderText("Error");
			alert.setContentText("Ingrese el Cobrador");
			alert.showAndWait();							
			return false;
		}					
		
		return true;
	}
	
	private void simular() {

		float montoCuota;
		float cuotaCapital;
		
	
		if (!validar())
			return;
		
		cuotaCapital = Float.valueOf(fldMontoCred.getText()) / Float.valueOf(fldCantDias.getText());
		
		montoCuota = cuotaCapital + ( cuotaCapital * ( Float.valueOf(fldTasaInt.getText()) / 100 )); //Float.valueOf("100") )); 

		fldMontoCuota.setText(String.valueOf(montoCuota));

		fldGciaXDia.setText(String.valueOf(montoCuota - cuotaCapital));
		
	}
	
	
}