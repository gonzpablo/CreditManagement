package com.cred.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.cred.util.DBUtil;

public class CreditoDAO {

	public static ObservableList<CreditoModel> buscarCreditos() throws SQLException, ClassNotFoundException {
	
		String selectStmt = "SELECT * FROM creditos";
		
		
		//Execute SELECT statement
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
	
	private static ObservableList<CreditoModel> getListaCreditos(ResultSet rs) throws SQLException, ClassNotFoundException {
        //Declare a observable List which comprises of Employee objects
        ObservableList<CreditoModel> listaCreditos = FXCollections.observableArrayList();
 
        while (rs.next()) {
//        	public CreditoModel(String cliente, int cantCuotas, String unidad, String montoCuota, 
//					String montoCredito, String cobrador, String ruta) {
	        	
            CreditoModel credito = new CreditoModel(rs.getInt("IDCLIENTE"), 
            										rs.getInt("CANTCUOTAS"),
            										rs.getInt("UNIDAD"),
            										rs.getInt("MONTOCUOTA"),
            										rs.getInt("MONTOTOTAL"),
            										rs.getInt("IDCOBRADOR"),
            										rs.getInt("IDRUTA"));
            
            System.out.println(rs.getInt("MONTOTOTAL"));
            
            //Agregar crédito a lista de créditos
            listaCreditos.add(credito);
        }
        //return empList (ObservableList of Employees)
        return listaCreditos;
    }	
}
