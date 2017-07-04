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
}