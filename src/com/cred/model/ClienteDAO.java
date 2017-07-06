package com.cred.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cred.util.DBUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClienteDAO {

	public static ObservableList<ClienteModel> buscarClientes() throws SQLException, ClassNotFoundException {
		
		String selectStmt = "SELECT rowid, * FROM clientes";
		
		
		//Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsClientes = DBUtil.dbExecuteQuery(selectStmt);
 
            //Send ResultSet to the getEmployeeList method and get employee object
            ObservableList<ClienteModel> listaClientes = getListaClientes(rsClientes);
 
            //Return employee object
            return listaClientes;
        } catch (SQLException e) {
            System.out.println("SQL select operation has failed: " + e);
            //Return exception
            throw e;
        }		
	}
	
	private static ObservableList<ClienteModel> getListaClientes(ResultSet rs) throws SQLException, ClassNotFoundException {
        //Declare a observable List which comprises of Employee objects
        ObservableList<ClienteModel> listaClientes = FXCollections.observableArrayList();
 
        while (rs.next()) {
//        	public CreditoModel(String cliente, int cantCuotas, String unidad, String montoCuota, 
//					String montoCredito, String cobrador, String ruta) {
	        	
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
        //return empList (ObservableList of Employees)
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
}