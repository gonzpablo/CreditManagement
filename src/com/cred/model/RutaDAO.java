package com.cred.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cred.util.DBUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RutaDAO {

	
    public static ObservableList<RutaModel> buscarRutas() throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT rowid, * FROM rutas";
 
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsRutas = DBUtil.dbExecuteQuery(selectStmt);
 
            //Send ResultSet to the getEmployeeList method and get employee object
            ObservableList<RutaModel> listaRutas = getListaRutas(rsRutas);
 
            //Return employee object
            return listaRutas;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }
    }

	private static ObservableList<RutaModel> getListaRutas(ResultSet rs) throws SQLException, ClassNotFoundException {
        //Declare a observable List which comprises of Employee objects
        ObservableList<RutaModel> listaRutas = FXCollections.observableArrayList();
 
        while (rs.next()) {
//        	public CreditoModel(String cliente, int cantCuotas, String unidad, String montoCuota, 
//					String montoCredito, String cobrador, String ruta) {
	        	
            RutaModel ruta = new RutaModel();
            		
            ruta.setId(rs.getInt("ROWID"));
            ruta.setDescripcion(rs.getString("DESCRIPCION"));
            
            //Agregar crédito a lista de créditos
            listaRutas.add(ruta);
        }
        //return empList (ObservableList of Employees)
        return listaRutas;
    }	    
}
