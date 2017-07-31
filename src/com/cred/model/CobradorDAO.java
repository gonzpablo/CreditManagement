package com.cred.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cred.util.DBUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CobradorDAO {

    public static ObservableList<CobradorModel> buscarCobradores() throws SQLException, ClassNotFoundException {

    	String selectStmt = "SELECT rowid, * FROM cobradores";

        try {

            ResultSet rsCobradores = DBUtil.dbExecuteQuery(selectStmt);
 
            ObservableList<CobradorModel> listaCobradores = getListaCobradores(rsCobradores);
 
            return listaCobradores;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
    }

	private static ObservableList<CobradorModel> getListaCobradores(ResultSet rs) throws SQLException, ClassNotFoundException {

        ObservableList<CobradorModel> listaCobradores = FXCollections.observableArrayList();
 
        while (rs.next()) {
	        	
            CobradorModel cobrador = new CobradorModel();
            		
            cobrador.setId(rs.getInt("ROWID"));
            cobrador.setNombre(rs.getString("NOMBRE"));
            cobrador.setApellido(rs.getString("APELLIDO"));
            
            listaCobradores.add(cobrador);
        }
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