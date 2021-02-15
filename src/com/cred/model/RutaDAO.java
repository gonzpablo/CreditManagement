package com.cred.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cred.util.DBUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RutaDAO {
	
    public static ObservableList<RutaModel> buscarRutas() throws SQLException, ClassNotFoundException {
        String selectStmt = "SELECT rowid, * FROM rutas";
 
        try {
            ResultSet rsRutas = DBUtil.dbExecuteQuery(selectStmt);

			return getListaRutas(rsRutas);
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }
    }

	private static ObservableList<RutaModel> getListaRutas(ResultSet rs) throws SQLException {

        ObservableList<RutaModel> listaRutas = FXCollections.observableArrayList();
 
        while (rs.next()) {
        	
            RutaModel ruta = new RutaModel();
            		
            ruta.setId(rs.getInt("ROWID"));
            ruta.setNombre(rs.getString("NOMBRE"));
            ruta.setDescripcion(rs.getString("DESCRIPCION"));
            
            listaRutas.add(ruta);
        }

        return listaRutas;
    }
	
	public static void agregarRuta(RutaModel ruta) throws SQLException, ClassNotFoundException {

		String insertStmt =
				"BEGIN;\n" +
					"INSERT INTO rutas\n" +
					"(NOMBRE, DESCRIPCION)\n" +
					"VALUES\n" +
					"('" + ruta.getNombre() + "'," + 
						"'" + ruta.getDescripcion() + "');" + 				
				"COMMIT;";

		try {
			DBUtil.dbExecuteUpdate(insertStmt);
		} catch (SQLException e) {
			System.out.print("Error en INSERT Ruta: " + e);
			throw e;
		}
	}
	
	public static void modificarRuta(RutaModel ruta) throws SQLException, ClassNotFoundException {

		String updateStmt =
				"BEGIN;\n" +
					"UPDATE rutas \n" +
					"set NOMBRE = '" + ruta.getNombre() + "', " +
						"DESCRIPCION = '" + ruta.getDescripcion() + "' " +
						"WHERE rowid = " + ruta.getId() + ";\n" +
				"COMMIT;";

		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (SQLException e) {
			System.out.print("Error en UPDATE Ruta: " + e);
			throw e;
		}
	}
	
	public static void borrarRuta(RutaModel ruta) throws SQLException, ClassNotFoundException {
		String deleteStmt =
				"BEGIN;\n" +
					"DELETE FROM rutas \n" +
					"WHERE rowid = " + ruta.getId() + ";\n" +
				"COMMIT;";

		try {
			DBUtil.dbExecuteUpdate(deleteStmt);
		} catch (SQLException e) {
			System.out.print("Error en DELETE Ruta: " + e);
			throw e;
		}					
	}			
}