package com.cred.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.cred.util.DBUtil;

public class CreditoDAO {

	public static ObservableList<CreditoModel> buscarCreditos() throws SQLException, ClassNotFoundException {
	
		String selectStmt = "SELECT rowid, * FROM creditos WHERE cerrado = 0";
		
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsCreditos = DBUtil.dbExecuteQuery(selectStmt);
 
            //Send ResultSet to the getEmployeeList method and get employee object
            ObservableList<CreditoModel> listaCreditos = getListaCreditos(rsCreditos);
 
            //Return employee object
            return listaCreditos;
        } catch (SQLException e) {
            System.out.println("SQL select operation has failed: " + e);
            //Return exception
            throw e;
        }		
	}
	
	 public static void agregarCredito(CreditoModel credito) throws SQLException, ClassNotFoundException {

	    String insertStmt =
	            "BEGIN;\n" +
	                    "INSERT INTO creditos\n" +
	                    "(IDCLIENTE, IDCOBRADOR, IDRUTA, MONTOTOTAL, MONTOCUOTA, CANTCUOTAS, UNIDAD)\n" +
	                    "VALUES\n" +
	                    "(" + credito.getClienteRef().getId() + "," + 
	                    	  credito.getCobradorRef().getId() + "," +
	                    	  credito.getRutaRef().getId() + "," +	                    	  
	                    	  credito.getMontoCredito().multiply(BigDecimal.valueOf(100)) + "," +	                    	  
	                    	  credito.getValorCuota().multiply(BigDecimal.valueOf(100)) + "," +
							  credito.getCantCuotas() + "," +							  
							  CreditoModel.obtenerIdUnidad(credito.getUnidad()) + 
							  0 + " );\n" +	                    
	                    "COMMIT;";	    
	    
	    	System.out.println(insertStmt);

	    try {
	        DBUtil.dbExecuteUpdate(insertStmt);
	    } catch (SQLException e) {
	        System.out.print("Error en INSERT Creditos: " + e);
	        throw e;
	    }
	}	
	
	 public static void borrarCredito(CreditoModel credito) throws SQLException, ClassNotFoundException {

//		Borrar el crédito		 
		String deleteStmt =
		        "BEGIN;\n" +
		                "DELETE FROM creditos\n" +
		                "WHERE rowid = " + credito.getId() + ";\n" +	                    
		                "COMMIT;";	    
		
			System.out.println(deleteStmt);
		
		try {
		    DBUtil.dbExecuteUpdate(deleteStmt);
		} catch (SQLException e) {
		    System.out.print("Error en Delete Creditos: " + e);
		    throw e;
		}
		
//		Borrar los pagos asociados
		deleteStmt =
		        "BEGIN;\n" +
		                "DELETE FROM pagos\n" +
		                "WHERE idCredito = " + credito.getId() + ";\n" +	                    
		                "COMMIT;";	   

		System.out.println(deleteStmt);
		
		try {
		    DBUtil.dbExecuteUpdate(deleteStmt);
		} catch (SQLException e) {
		    System.out.print("Error en Delete pagos al borrar Creditos: " + e);
		    throw e;
		}		
	}		 
	 
	private static ObservableList<CreditoModel> getListaCreditos(ResultSet rs) throws SQLException, ClassNotFoundException {
        //Declare a observable List which comprises of Employee objects
        ObservableList<CreditoModel> listaCreditos = FXCollections.observableArrayList();
 
        while (rs.next()) {
	        	
            CreditoModel credito = new CreditoModel(rs.getInt("ROWID"),
            									    rs.getInt("IDCLIENTE"), 
            										rs.getInt("CANTCUOTAS"),
            										rs.getInt("UNIDAD"),
            										rs.getInt("MONTOCUOTA"),
            										rs.getInt("MONTOTOTAL"),
            										rs.getInt("IDCOBRADOR"),
            										rs.getInt("IDRUTA"));
            
            //Agregar crédito a lista de créditos
            listaCreditos.add(credito);
        }

        return listaCreditos;
    }

	public static void cerrarCredito(CreditoModel credito) throws SQLException, ClassNotFoundException {

//		Cerrar un crédito		 
		String updateStmt =
		        "BEGIN;\n" +
		                "UPDATE creditos \n" +
		        		"SET cerrado = 1 \n" +
		                "WHERE rowid = " + credito.getId() + ";\n" +	                    
		                "COMMIT;";	    
		
			System.out.println(updateStmt);
		
		try {
		    DBUtil.dbExecuteUpdate(updateStmt);
		} catch (SQLException e) {
		    System.out.print("Error en Delete Creditos: " + e);
		    throw e;
		}	
	}		
}