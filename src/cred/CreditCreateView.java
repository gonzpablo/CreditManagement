package cred;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
	//      	System.out.println("confirm");        	
	//      	public CreditoModel(String cliente, int cantDias, float tasaInt, float montoCredito, float montoCuota, float gciaXDia) {        	
	      	clntView.addItemToList(new CreditoModel(this.fldCliente.getText().toString(),
	      											Integer.valueOf(this.fldCantDias.getText()),
	      											Float.valueOf(fldTasaInt.getText()),
	      											Float.valueOf(fldMontoCred.getText()),
	      											Float.valueOf(fldMontoCred.getText()),
	      											Float.valueOf(fldMontoCuota.getText()),
	      											Float.valueOf(fldGciaXDia.getText()),
	      											this.fldCobrador.getText().toString()));
	      	this.close();
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