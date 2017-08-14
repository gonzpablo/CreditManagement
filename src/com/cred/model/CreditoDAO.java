package com.cred.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.cred.util.DBUtil;

public class CreditoDAO {

	public static ObservableList<CreditoModel> buscarCreditos(int cerrado) throws SQLException, ClassNotFoundException {
	
		String selectStmt = "SELECT rowid, " +
							" idCliente, " +
							" cantCuotas," +
							" unidad," +
							" montoCuota," +
							" montoTotal," +
							" idCobrador," +
							" idRuta," +
							" cerrado," +
							" date(fechaCreacion, 'unixepoch') as fechaCreacion " +
				            " FROM creditos WHERE cerrado = " + cerrado + ";";		
		
        try {
            ResultSet rsCreditos = DBUtil.dbExecuteQuery(selectStmt);
 
            ObservableList<CreditoModel> listaCreditos = getListaCreditos(rsCreditos);
 
            return listaCreditos;
        } catch (SQLException e) {
            System.out.println("SQL select operation has failed: " + e);
            throw e;
        }		
	}
	
	 public static void agregarCredito(CreditoModel credito) throws SQLException, ClassNotFoundException {

	    String insertStmt =
	            "BEGIN;\n" +
	                    "INSERT INTO creditos\n" +
	                    "(IDCLIENTE, IDCOBRADOR, IDRUTA, FECHACREACION, MONTOTOTAL, MONTOCUOTA, CANTCUOTAS, UNIDAD, CERRADO)\n" +
	                    "VALUES\n" +
	                    "(" + credito.getClienteRef().getId() + "," + 
	                    	  credito.getCobradorRef().getId() + "," +
	                    	  credito.getRutaRef().getId() + "," +
	                    	  " (SELECT strftime('%s','" + credito.getFecha().getValue() + "')), " +  	                    	  
	                    	  credito.getMontoCredito().multiply(BigDecimal.valueOf(100)) + "," +	                    	  
	                    	  credito.getValorCuota().multiply(BigDecimal.valueOf(100)) + "," +
							  credito.getCantCuotas() + "," +							  
							  CreditoModel.obtenerIdUnidad(credito.getUnidad()) + "," + 
							  0 + " );\n" +	                    
	                    "COMMIT;";	    
	    
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
		
		try {
		    DBUtil.dbExecuteUpdate(deleteStmt);
		} catch (SQLException e) {
		    System.out.print("Error en Delete pagos al borrar Creditos: " + e);
		    throw e;
		}		
	}		 
	 
	private static ObservableList<CreditoModel> getListaCreditos(ResultSet rs) throws SQLException, ClassNotFoundException {

        ObservableList<CreditoModel> listaCreditos = FXCollections.observableArrayList();
 
        while (rs.next()) {
	        	
            CreditoModel credito = new CreditoModel(rs.getInt("ROWID"),
            									    rs.getInt("IDCLIENTE"), 
            										rs.getInt("CANTCUOTAS"),
            										rs.getInt("UNIDAD"),
            										rs.getInt("MONTOCUOTA"),
            										rs.getInt("MONTOTOTAL"),
            										rs.getInt("IDCOBRADOR"),
            										rs.getInt("IDRUTA"),
            										rs.getInt("CERRADO"));
                        
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate.parse(rs.getString("FECHACREACION"), formatter);

			credito.setFecha(new SimpleObjectProperty<LocalDate>(localDate));            
            
            listaCreditos.add(credito);
        }

        return listaCreditos;
    }

	public static void cerrarCredito(CreditoModel credito, boolean cerrar) throws SQLException, ClassNotFoundException {

		int cerrado = 0;
		
		if (cerrar)
			cerrado = 1;
		else
			cerrado = 0;
		
//		Cerrar un crédito		 
		String updateStmt =
		        "BEGIN;\n" +
		                "UPDATE creditos \n" +
		        		"SET cerrado = " + cerrado + " \n" +
		                "WHERE rowid = " + credito.getId() + ";\n" +	                    
		                "COMMIT;";	    
			
		try {
		    DBUtil.dbExecuteUpdate(updateStmt);
		} catch (SQLException e) {
		    System.out.print("Error en Delete Creditos: " + e);
		    throw e;
		}	
	}

	public static boolean buscarCreditoCliente(ClienteModel cliente) throws ClassNotFoundException, SQLException {

		boolean tieneCreditos = false;
		String selectStmt = "SELECT rowid FROM creditos WHERE idCliente = " + cliente.getId() + ";";
		
		try {
			ResultSet rsCreditos = DBUtil.dbExecuteQuery(selectStmt);

			if (rsCreditos.next())

				if (rsCreditos.getInt("ROWID") > 0)
					tieneCreditos = true;

		} catch (SQLException e) {
			System.out.println("SQL select operation has failed: " + e);
			throw e;
		}
		return tieneCreditos;
	}
	
	public static void modificarCredito(CreditoModel credito) throws SQLException, ClassNotFoundException {

//		Cerrar un crédito		 
		String updateStmt =
		        "BEGIN;\n" +
		                "UPDATE creditos \n" +
		        		"SET idCobrador = " + credito.getCobradorRef().getId() + ", \n" +
		                "idRuta = " + credito.getRutaRef().getId() + " \n" +
		                "WHERE rowid = " + credito.getId() + ";\n" +	                    
		                "COMMIT;";	
		
		try {
		    DBUtil.dbExecuteUpdate(updateStmt);
		} catch (SQLException e) {
		    System.out.print("Error en Update Creditos: " + e);
		    throw e;
		}	
	}	
}