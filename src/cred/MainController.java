package cred;

import java.util.function.Predicate;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {
	
	@FXML
	private TextField cobradorFilterField;
	@FXML
	private TableView<CreditoModel> creditosTable;
	@FXML
	private TableColumn<CreditoModel, String> clienteColumn;
	@FXML	
    private TableColumn<CreditoModel, Float> montoCuotaColumn;	
	@FXML	
    private TableColumn<CreditoModel, Float> gciaXDiaColumn;	
	@FXML	
    private TableColumn<CreditoModel, Float> saldoCapitalColumn;   
	@FXML	
    private TableColumn<CreditoModel, Float> montoCreditoColumn;	    
	@FXML	
    private TableColumn<CreditoModel, Integer> cuotasPagasColumn;    
	@FXML	
    private TableColumn<CreditoModel, Integer> cantDiasColumn;    
	@FXML	
    private TableColumn<CreditoModel, String> cobradorColumn;
	@FXML	
    private TableColumn<CreditoModel, String> rutaColumn;	
	@FXML
	private ComboBox<String> rutaFilterCombo;
	@FXML
	private Button btnCleanFilters;

	@FXML
	private TextField cuotaPuraField;
	@FXML
	private TextField gciaDiaField;
	
	private ObservableList<CreditoModel> creditos = FXCollections.observableArrayList();
	
	private FilteredList<CreditoModel> filteredItems = new FilteredList<>(creditos, p -> true);

	
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
		
		initColumns();
		initComboRuta();
		
        ObjectProperty<Predicate<CreditoModel>> cobradorFilter = new SimpleObjectProperty<>();
        ObjectProperty<Predicate<CreditoModel>> rutaFilter = new SimpleObjectProperty<>();		
	
		cobradorFilter.bind(Bindings.createObjectBinding(() -> 
        credito -> credito.getCobrador().toLowerCase().contains(cobradorFilterField.getText().toLowerCase()), 
        cobradorFilterField.textProperty()));
		
        rutaFilter.bind(Bindings.createObjectBinding(() ->
        credito -> rutaFilterCombo.getValue() == null || rutaFilterCombo.getValue() == credito.getRuta(), 
        rutaFilterCombo.valueProperty()));		
		
//        FilteredList<CreditoModel> filteredItems = new FilteredList<>(creditos, p -> true);
        creditosTable.setItems(filteredItems);    
        
//		Esto hace que el filtro sea acumulativo (de los dos filtros) (ver el .and)        
        filteredItems.predicateProperty().bind(Bindings.createObjectBinding(
                () -> cobradorFilter.get().and(rutaFilter.get()), 
                cobradorFilter, rutaFilter));

        calc();
        
        btnCleanFilters.setOnAction(e -> {
            rutaFilterCombo.setValue(null);
            cobradorFilterField.clear();           
        });        
    
        rutaFilterCombo.setOnAction(e -> {
        	calc();
        });
        
	}
	
	private void calc() {
		float sum = 0;
		
		for ( CreditoModel cred : filteredItems ) {
			
			sum+=cred.getMontoCuota();
        	cuotaPuraField.setText(String.valueOf(sum));
        }		
	}
	@FXML
//	private void initialize2() {
//
//		// 0. Initialize the columns.		
//		initColumns();		
//
//		initComboRuta();
//		
//		// 1. Wrap the ObservableList in a FilteredList (initially display all data).
//		FilteredList<CreditoModel> filteredData = new FilteredList<>(creditos, p -> true);
//       
//        // Add a listener to the textProperty of the combobox editor. The
//        // listener will simply filter the list every time the input is changed
//        // as long as the user hasn't selected an item in the list.
////		rutaCombo.setEditable(true);
//		
//		rutaCombo.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
////        rutaCombo.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
//          	
//            	final TextField editor = rutaCombo.getEditor();
//                final String selected = rutaCombo.getSelectionModel().getSelectedItem();
//
//                // If filter text is empty, display all persons.
////                if (newValue == null || newValue.isEmpty()) {
////                	if (newValue == null) {                	
////                    return true;
////                }                
//                
//                if (selected == null || !selected.equals(editor.getText())) {
//                    filteredData.setPredicate(item -> {
//                        // We return true for any items that starts with the
//                        // same letters as the input. We use toUpperCase to
//                        // avoid case sensitivity.
////                        if (item.getRuta().toUpperCase().startsWith(newValue.toUpperCase())) {
//                    	
////                    	int test = intValue(newValue);
//                    	int test = newValue.intValue() + 1;
//                    	
//                    	
//                    	System.out.println(item.getRuta() + " - " + editor.getText());
//                        if (item.getRuta().toUpperCase().startsWith(String.valueOf(test)) )   {                        	
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    });
////                }
//                
//                
////                // If filter text is empty, display all persons.
////                if (newValue == null || newValue.isEmpty()) {
////                    return true;
////                }
////
////                // Compare first name and last name of every person with filter text.
////                String lowerCaseFilter = newValue.toLowerCase();
////
////                if (credito.getCobrador().toLowerCase().contains(lowerCaseFilter)) {
////                    return true; // Filter matches first name.
//////                } else if (creditoModel.getLastName().toLowerCase().contains(lowerCaseFilter)) {
//////                    return true; // Filter matches last name.
////                }
////                return false; // Does not match.
////            });
//        	
////        	final TextField editor = rutaCombo.getEditor();
////            final String selected = rutaCombo.getSelectionModel().getSelectedItem();
//
////            // This needs run on the GUI thread to avoid the error described
////            // here: https://bugs.openjdk.java.net/browse/JDK-8081700.
////            Platform.runLater(() -> {
////                // If the no item in the list is selected or the selected item
////                // isn't equal to the current input, we refilter the list.
////                if (selected == null || !selected.equals(editor.getText())) {
////                    filteredData.setPredicate(item -> {
////                        // We return true for any items that starts with the
////                        // same letters as the input. We use toUpperCase to
////                        // avoid case sensitivity.
////                        if (item.getRuta().toUpperCase().startsWith(newValue.toUpperCase())) {
////                            return true;
////                        } else {
////                            return false;
////                        }
////                    });
////                }
////            });
//                }
//        });
		
		
//        rutaCombo.getEditor().textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//            	
//            	
//                previousValue = oldValue;
//                final TextField editor = rutaCombo.getEditor();
//                final String selected = rutaCombo.getSelectionModel().getSelectedItem();
//
//                if (selected == null || !selected.equals(editor.getText())) {
////                    filterItems(newValue, currentInstance);
//
////                    currentInstance.show();
////                    if (currentInstance.getItems().size() == 1) {
////                        setUserInputToOnlyOption(currentInstance, editor);
////                    }
//                }
//            }
//        });		
		
//        cobradorFilter.textProperty().addListener((observable, oldValue, newValue) -> {
//            filteredData.setPredicate(credito -> {
//                // If filter text is empty, display all persons.
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//
//                // Compare first name and last name of every person with filter text.
//                String lowerCaseFilter = newValue.toLowerCase();
//
//                if (credito.getCobrador().toLowerCase().contains(lowerCaseFilter)) {
//                    return true; // Filter matches first name.
////                } else if (creditoModel.getLastName().toLowerCase().contains(lowerCaseFilter)) {
////                    return true; // Filter matches last name.
//                }
//                return false; // Does not match.
//            });
//        });   
        
        
		// 3. Wrap the FilteredList in a SortedList. 
//		SortedList<CreditoModel> sortedData = new SortedList<>(filteredData);
		
		// 4. Bind the SortedList comparator to the TableView comparator.
		// 	  Otherwise, sorting the TableView would have no effect.
//		sortedData.comparatorProperty().bind(creditos.comparatorProperty());
		
//        creditosTable = new TableView<>();
        
//      table.setItems(initCreditos());
//        creditosTable.setItems(filteredData);        
        
		// 5. Add sorted (and filtered) data to the table.
//		((TableView<CreditoModel>) creditos).setItems(filteredData);
	
		
//	}
	
    private void initComboRuta() {

		
		rutaFilterCombo.setItems(FXCollections.observableArrayList("1","2","3","4"));
		
	}

	private void initColumns() {
		clienteColumn.setCellValueFactory(new PropertyValueFactory<>("cliente"));		
        montoCuotaColumn.setCellValueFactory(new PropertyValueFactory<>("montoCuota"));
        gciaXDiaColumn.setCellValueFactory(new PropertyValueFactory<>("gciaXDia"));                
        saldoCapitalColumn.setCellValueFactory(new PropertyValueFactory<>("saldoCapital"));
        montoCreditoColumn.setCellValueFactory(new PropertyValueFactory<>("montoCredito"));               
        cuotasPagasColumn.setCellValueFactory(new PropertyValueFactory<>("cuotasPagas"));       
        cantDiasColumn.setCellValueFactory(new PropertyValueFactory<>("cantDias"));        
        cobradorColumn.setCellValueFactory(new PropertyValueFactory<>("cobrador"));
        rutaColumn.setCellValueFactory(new PropertyValueFactory<>("ruta"));
	}

    public ObservableList<CreditoModel> initCreditos(){
    	creditos.add(new CreditoModel("Carlos", 29, 45, 3000, 150, 45, 3000, "Luis", "1"));
    	creditos.add(new CreditoModel("Juan", 15, 45, 2000, 150, 45, 2000, "Miguel", "2"));
    	creditos.add(new CreditoModel("Jorge", 29, 45, 3000, 250, 45, 3000, "Luis", "1"));
    	creditos.add(new CreditoModel("Bernardo", 15, 45, 2000, 350, 45, 2000, "Ezequiel", "2"));
    	
        return creditos;
    }	

    public ObservableList<CreditoModel> getCreditos() {
        return creditos;
      }
}