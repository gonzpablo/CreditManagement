package cred;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableRow;
import javafx.scene.layout.GridPane;

public class CreditPayView extends CreditView implements EventHandler<ActionEvent> {

	private Spinner<Integer> fldCuotasPagas;
	private Button btnConfirmar;
	
	public CreditPayView(CreditListView clntView) {
		super(clntView);
	}
	
	public void camposPropios() {
		Label lblCuotasPagas = new Label("Cuotas Pagas");
	
	//	Creates an integer spinner with 1 as min, 10 as max and 2 as initial value
		fldCuotasPagas = new Spinner<>(1, ( super.credito.getCantDias() - super.credito.getCuotasPagas()), 1);
	
	  	btnConfirmar = new Button("Confirmar");		
	  	btnConfirmar.setOnAction(this);
	  	
		super.gpane.getChildren().addAll(lblCuotasPagas, fldCuotasPagas, btnConfirmar);
	//	super.getGpane().getChildren().addAll(lblCuotasPagas, fldCuotasPagas, btnConfirmar);
		
		GridPane.setConstraints(lblCuotasPagas, 0, 7); // (c0, r0)
		GridPane.setConstraints(fldCuotasPagas, 1, 7); // (c0, r0)
	
		GridPane.setConstraints(btnConfirmar, 1, 8); // (c0, r0)					

		if (super.credito.getCuotasPagas() >= super.credito.getCantDias())
			fldCuotasPagas.setDisable(true);		
		
	}
	//When button is clicked, handle() gets called
	//Button click is an ActionEvent (also MouseEvents, TouchEvents, etc...)
	@Override
	public void handle(ActionEvent event) {
  	
	  float saldo = 0;
		
      if (event.getSource() == btnConfirmar ) {
//    	  	clntView.table.getItems().contains(super.credito);
//    	  	credito.setCuotasPagas(credito.getCuotasPagas() + fldCuotasPagas.getValue());
//    
//    	  	saldo = credito.getSaldoCapital() - (fldCuotasPagas.getValue() * credito.getMontoCuota());
//    	  	
//    	  	if (saldo >= 0) 
//    	  		credito.setSaldoCapital(saldo);
//    	  	else
//    	  		credito.setSaldoCapital(0);
//    	  	
//    	  	clntView.table.getItems().set(row.getIndex(), credito);    	  	
//	      	this.close();
	      }        	
	}
	
	public void setCredito(CreditoModel credito, TableRow<CreditoModel> row) {

		this.credito = credito;
		this.row = row;
		this.fldCliente.setText(credito.getCliente());
		this.fldCantDias.setText(Integer.toString(credito.getCantDias()));
		this.fldMontoCred.setText(credito.getMontoCredito().toString());
		this.fldMontoCuota.setText(credito.getMontoCuota().toString());
		this.fldTasaInt.setText(credito.getTasaInt().toString());
		this.fldGciaXDia.setText(credito.getGciaXDia().toString());
		this.fldCobrador.setText(credito.getCobrador());
		
		fldCliente.setDisable(true);
		fldCantDias.setDisable(true);
		fldMontoCred.setDisable(true);
		fldMontoCuota.setDisable(true);
		fldTasaInt.setDisable(true);
        fldGciaXDia.setDisable(true);
        fldCobrador.setDisable(true);

	}	
}
