package com.cred.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cred.util.DBUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CobradorDAO {


    public static ObservableList<CobradorModel> buscarCobradores() throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT rowid, * FROM cobradores";
 
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsCobradores = DBUtil.dbExecuteQuery(selectStmt);
 
            //Send ResultSet to the getEmployeeList method and get employee object
            ObservableList<CobradorModel> listaCobradores = getListaCobradores(rsCobradores);
 
            //Return employee object
            return listaCobradores;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }
    }

	private static ObservableList<CobradorModel> getListaCobradores(ResultSet rs) throws SQLException, ClassNotFoundException {
        //Declare a observable List which comprises of Employee objects
        ObservableList<CobradorModel> listaCobradores = FXCollections.observableArrayList();
 
        while (rs.next()) {
//        	public CreditoModel(String cliente, int cantCuotas, String unidad, String montoCuota, 
//					String montoCredito, String cobrador, String ruta) {
	        	
            CobradorModel cobrador = new CobradorModel();
            		
            cobrador.setId(rs.getInt("ROWID"));
            cobrador.setNombre(rs.getString("NOMBRE"));
            cobrador.setApellido(rs.getString("APELLIDO"));
            
            //Agregar crédito a lista de créditos
            listaCobradores.add(cobrador);
        }
        //return empList (ObservableList of Employees)
        return listaCobradores;
    }
	
	public static void agregarCobrador(CobradorModel cobrador) throws SQLException, ClassNotFoundException {

		String insertStmt =
				"BEGIN;\n" +
					"INSERT INTO cobradores\n" +
					"(NOMBRE, APELLIDO)\n" +
					"VALUES\n" +
					"('" + cobrador.getNombre() + "'," + 
						"'" + cobrador.getApellido() + "');" + 				
				"COMMIT;";

		try {
			DBUtil.dbExecuteUpdate(insertStmt);
		} catch (SQLException e) {
			System.out.print("Error en INSERT Cobrador: " + e);
			throw e;
		}
	}
	
	public static void modificarCobrador(CobradorModel cobrador) throws SQLException, ClassNotFoundException {

		String updateStmt =
				"BEGIN;\n" +
					"UPDATE cobradores \n" +
					"set NOMBRE = '" + cobrador.getNombre() + "', " +
						"APELLIDO = '" + cobrador.getApellido() + "' " +
						"WHERE rowid = " + cobrador.getId() + ";\n" +
				"COMMIT;";

		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (SQLException e) {
			System.out.print("Error en UPDATE Cobrador: " + e);
			throw e;
		}
	}
	
	public static void borrarCobrador(CobradorModel cobrador) throws SQLException, ClassNotFoundException {
		String deleteStmt =
				"BEGIN;\n" +
					"DELETE FROM cobradores \n" +
					"WHERE rowid = " + cobrador.getId() + ";\n" +
				"COMMIT;";

		try {
			DBUtil.dbExecuteUpdate(deleteStmt);
		} catch (SQLException e) {
			System.out.print("Error en DELETE Cobrador: " + e);
			throw e;
		}					
	}	
}