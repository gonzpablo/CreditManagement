package cred;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PagoController {

	@FXML
	private DatePicker fechaPagoField;	
	
	@FXML
	private TextField clienteField;
	
	public PagoController() {
		System.out.println("here");

		clienteField.setText("PEpe");
		
	
		
//		// Add some action (in Java 8 lambda syntax style).
//		fechaPagoField.setOnAction(event -> {
//		    LocalDate date = fechaPagoField.getValue();
//		    System.out.println("Selected date: " + date);
//		});

		
	      LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDate date1 = currentDateTime.toLocalDate();
//		fechaPagoField.setValue(LocalDate.of(2017,1,1));
//		fechaPagoField.setValue(date1);
//		fechaPagoField = date1;
//	    System.out.println("date1: " + date1);
	      				
	}

	public static final LocalDate LOCAL_DATE (String dateString){
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    LocalDate localDate = LocalDate.parse(dateString, formatter);
	    return localDate;
	}
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	
	@FXML
	private void initialize() {

//		fechaPagoField = new DatePicker();
//	      LocalDateTime currentDateTime = LocalDateTime.now();
//		LocalDate date1 = currentDateTime.toLocalDate();
//	      
//		fechaPagoField.setValue(date1);
	}
	
}
