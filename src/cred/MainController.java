package cred;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {
	
	@FXML
	private TextField cobradorFilter;
	@FXML
	private TableView<CreditoModel> creditosTable;
	@FXML
	private TableColumn<CreditoModel, String> clienteColumn;
	@FXML
	private TableColumn<CreditoModel, String> montoCuotaColumn;
	
	ObservableList<CreditoModel> creditos = FXCollections.observableArrayList();

	
	/**
	 * Just add some sample data in the constructor.
	 */
	public MainController() {
		initCreditos();
	}	
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 * 
	 * Initializes the table columns and sets up sorting and filtering.
	 */
	@FXML
	private void initialize() {
		
		// 0. Initialize the columns.
		clienteColumn.setCellValueFactory(cellData -> cellData.getValue().clienteProperty());
//		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
		
		// 0. Initialize the columns.
//		clienteColumn.setCellValueFactory(new PropertyValueFactory<>("cliente"));
//		montoCuotaColumn.setCellValueFactory(new PropertyValueFactory<>("montoCuota"));
		
		// 1. Wrap the ObservableList in a FilteredList (initially display all data).
		FilteredList<CreditoModel> filteredData = new FilteredList<>(creditos, p -> true);
		
	     // 2. Set the filter Predicate whenever the filter changes.
        cobradorFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(credito -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (credito.getCobrador().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
//                } else if (creditoModel.getLastName().toLowerCase().contains(lowerCaseFilter)) {
//                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });   
		
		// 3. Wrap the FilteredList in a SortedList. 
//		SortedList<CreditoModel> sortedData = new SortedList<>(filteredData);
		
		// 4. Bind the SortedList comparator to the TableView comparator.
		// 	  Otherwise, sorting the TableView would have no effect.
//		sortedData.comparatorProperty().bind(creditos.comparatorProperty());
		
        creditosTable = new TableView<>();
        
//      table.setItems(initCreditos());
        creditosTable.setItems(filteredData);        
        
		// 5. Add sorted (and filtered) data to the table.
//		((TableView<CreditoModel>) creditos).setItems(filteredData);
	}
	
    //Get all of the products
    public ObservableList<CreditoModel> initCreditos(){
    	creditos.add(new CreditoModel("Carlos", 29, 45, 3000, 150, 45, 3000, "Luis"));
    	creditos.add(new CreditoModel("Juan", 15, 45, 2000, 150, 45, 2000, "Miguel"));
    	
        return creditos;
    }	

    public ObservableList<CreditoModel> getCreditos(){
        return creditos;
      }
}