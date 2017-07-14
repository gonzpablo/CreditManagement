package com.cred.util;

import com.cred.model.RutaModel;
import com.sun.rowset.CachedRowSetImpl;

import javafx.collections.ObservableList;

import java.sql.*;

public class DBUtil {
    //Declare JDBC Driver
    private static final String JDBC_DRIVER = "org.sqlite.JDBC";
    //Connection
    private static Connection conn = null;

    //Connection String
    //Username=HR, Password=HR, IP=localhost, IP=1521, SID=xe
    private static final String connStr = "jdbc:sqlite:dbCred.db";

//    private static boolean tieneEsquema = false;
    
	static {
		System.out.println("_INIT_");

		try {
			if (!tieneEsquema()) {
				crearEsquema();
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
    
    //Connect to DB
    public static void dbConnect() throws SQLException, ClassNotFoundException {
//		Setting SQlite JDBC Driver
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Error con Clase JDBC_DRIVER");
            e.printStackTrace();
            throw e;
        }
       
        //Establish the SQlite Connection using Connection String
        try {
            conn = DriverManager.getConnection(connStr);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console" + e);
            e.printStackTrace();
            throw e;
        }
    }

    private static void crearEsquema() throws SQLException, ClassNotFoundException {

//		Todas las tablas tienen ROWID y éste es por defecto la clave primaria    	
    	
		String updateStmt =
				"BEGIN TRANSACTION;\n" +
		
					"CREATE TABLE rutas ( \n" +
					"nombre INTEGER, \n" +
					"descripcion TEXT NOT NULL);\n" +

		    	"CREATE TABLE pagos ( \n" + 
		        		"idCredito	INTEGER NOT NULL, \n" + 
		        		"montoPago	INTEGER, \n" + 
		        		"fecha	INTEGER NOT NULL); \n" + 

	        	"CREATE TABLE creditos ( \n" + 
	        		"idCliente	INTEGER NOT NULL, \n" + 
	        		"idCobrador	INTEGER NOT NULL, \n" + 
	        		"idRuta	INTEGER NOT NULL, \n" + 
	        		"montoTotal	INTEGER, \n" + 
	        		"montoCuota	INTEGER, \n" + 
	        		"cantCuotas	INTEGER, \n" +
	        		"unidad	INTEGER, \n" + 
	        		"cerrado INTEGER); \n" +
	
	        	"CREATE TABLE cobradores ( \n" +
	        		"nombre	TEXT NOT NULL, \n" + 
	        		"apellido	TEXT, \n" +
	        		"PRIMARY KEY(nombre,apellido)); \n" +
	
	        	"CREATE TABLE clientes ( \n" +
	        		"nombre	TEXT, \n" +
	        		"apellido	TEXT, \n" + 
	        		"dni	TEXT, \n" + 
	        		"direccion	TEXT, \n" +
	        		"telefono	TEXT); \n"	+			
	
					"COMMIT;";

		System.out.println(updateStmt);

		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (SQLException e) {
			System.out.print("Error en Creación de Esquema de BD: " + e);
			throw e;
		} 	
	}

	//Close Connection
    public static void dbDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e){
           throw e;
        }                
    }

    //DB Execute Query Operation
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        //Declare statement, resultSet and CachedResultSet as null
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;
        
        try {
            //Connect to DB (Establish SQlite Connection)
            dbConnect();
            System.out.println("Select statement: " + queryStmt + "\n");

            //Create statement
            stmt = conn.createStatement();

            //Execute select (query) operation
            resultSet = stmt.executeQuery(queryStmt);

            //CachedRowSet Implementation
            //In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
            //We are using CachedRowSet
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (stmt != null) {
                //Close Statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
        //Return CachedRowSet
        return crs;
    }

    //DB Execute Update (For Update/Insert/Delete) Operation
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        //Declare statement as null
        Statement stmt = null;
        try {
            //Connect to DB (Establish SQlite Connection)
            dbConnect();
            //Create Statement
            stmt = conn.createStatement();
            //Run executeUpdate operation with given sql statement
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            if (stmt != null) {
                //Close statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
    }
    
    public static int getLastRowId(String table) throws SQLException, ClassNotFoundException {
 
    	int rowId = 0;
		String selectStmt = "SELECT MAX(rowid) as lastId \n" +
							"FROM " + table + ";\n"; 

        try {
            ResultSet rs = DBUtil.dbExecuteQuery(selectStmt);
 
            while (rs.next())	        	
                rowId = rs.getInt("lastId");

            System.out.println(rowId);
            
        } catch (SQLException e) {
            System.out.println("SQL select operation has failed: " + e);
            //Return exception
            throw e;
        }		
    	    	
        return rowId;
    }
    
	private static boolean tieneEsquema() throws SQLException, ClassNotFoundException {

        //Declare a SELECT statement
        String selectStmt = "SELECT name FROM sqlite_master WHERE type='table' AND name='clientes'";
 
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rs = DBUtil.dbExecuteQuery(selectStmt);
 
			if (rs.next())
				return true;
			else
				return false;
			
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }
	}    
}