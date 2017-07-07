package com.cred.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.cred.util.DBUtil;

public class ClienteDAO {

	public static ObservableList<ClienteModel> buscarClientes() throws SQLException, ClassNotFoundException {
		
		String selectStmt = "SELECT rowid, * FROM clientes";
		
        try {
            ResultSet rsClientes = DBUtil.dbExecuteQuery(selectStmt);
 
            ObservableList<ClienteModel> listaClientes = getListaClientes(rsClientes);
 
            return listaClientes;
        } catch (SQLException e) {
            System.out.println("SQL select operation has failed: " + e);
            throw e;
        }		
	}
	
	private static ObservableList<ClienteModel> getListaClientes(ResultSet rs) throws SQLException, ClassNotFoundException {

        ObservableList<ClienteModel> listaClientes = FXCollections.observableArrayList();
 
        while (rs.next()) {
	        	
            ClienteModel cliente = new ClienteModel();
           
            cliente.setId(rs.getInt("ROWID"));
            cliente.setNombre(rs.getString("NOMBRE"));
            cliente.setApellido(rs.getString("APELLIDO"));
            cliente.setDireccion(rs.getString("DIRECCION"));
            cliente.setTelefono(rs.getString("TELEFONO"));
            cliente.setDni(rs.getString("DNI"));
                        
            //Agregar crédito a lista de créditos
            listaClientes.add(cliente);
        }
        return listaClientes;
    }
	
	public static void agregarCliente(ClienteModel cliente) throws SQLException, ClassNotFoundException {

		String insertStmt =
				"BEGIN;\n" +
					"INSERT INTO clientes\n" +
					"(NOMBRE, APELLIDO, DNI, DIRECCION, TELEFONO)\n" +
					"VALUES\n" +
					"('" + cliente.getNombre() + "'," + 
						"'" + cliente.getApellido() + "'," + 
						"'" + cliente.getDni() + "'," +
						"'" + cliente.getDireccion() + "'," +
						"'" + cliente.getTelefono() + "');" + 				
				"COMMIT;";

		System.out.println(insertStmt);

		try {
			DBUtil.dbExecuteUpdate(insertStmt);
		} catch (SQLException e) {
			System.out.print("Error en INSERT Cliente: " + e);
			throw e;
		}
	}
	
	public static void modificarCliente(ClienteModel cliente) throws SQLException, ClassNotFoundException {

		String updateStmt =
				"BEGIN;\n" +
					"UPDATE clientes \n" +
					"set NOMBRE = '" + cliente.getNombre() + "', " +
						"APELLIDO = '" + cliente.getApellido() + "', " +
						"DNI = '" + cliente.getDni() + "', " +
						"DIRECCION = '" + cliente.getDireccion() + "', " +
						"TELEFONO = '" + cliente.getTelefono() + "' " +
						"WHERE rowid = " + cliente.getId() + ";\n" +
				"COMMIT;";

		System.out.println(updateStmt);

		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (SQLException e) {
			System.out.print("Error en UPDATE Cliente: " + e);
			throw e;
		}
	}

	public static void borrarCliente(ClienteModel cliente) throws SQLException, ClassNotFoundException {
		String deleteStmt =
				"BEGIN;\n" +
					"DELETE FROM clientes \n" +
					"WHERE rowid = " + cliente.getId() + ";\n" +
				"COMMIT;";

		System.out.println(deleteStmt);

		try {
			DBUtil.dbExecuteUpdate(deleteStmt);
		} catch (SQLException e) {
			System.out.print("Error en DELETE Cliente: " + e);
			throw e;
		}					
	}		
}