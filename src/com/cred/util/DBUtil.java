package com.cred.util;

import com.sun.rowset.CachedRowSetImpl;

import java.sql.*;

public class DBUtil {
    //Declare JDBC Driver
    private static final String JDBC_DRIVER = "org.sqlite.JDBC";
    //Connection
    private static Connection conn = null;

    //Connection String
    //Username=HR, Password=HR, IP=localhost, IP=1521, SID=xe
    private static final String connStr = "jdbc:sqlite:dbCred.db";

    
	static {

		try {
			if (!tieneEsquema()) {
				crearEsquema();
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
    
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
            System.out.println("Conexión a BD fallida! " + e);
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

		Log.show(updateStmt);

		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (SQLException e) {
			System.out.print("Error en Creación de Esquema de BD: " + e);
			throw e;
		} 	
	}

    public static void dbDisconnect() throws SQLException {

    	try {
            if (conn != null && !conn.isClosed())
                conn.close();

        } catch (Exception e){
           throw e;
        }                
    }

    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {

        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;
        
        try {
            dbConnect();
            
    		Log.show("SELECT: " + queryStmt + "\n");            

            stmt = conn.createStatement();

            resultSet = stmt.executeQuery(queryStmt);

            //CachedRowSet Implementation
            //In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
            //We are using CachedRowSet
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Ocurrió un problema en la operación: " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            dbDisconnect();
        }
        return crs;
    }

    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        try {
            dbConnect();
            stmt = conn.createStatement();
            stmt.executeUpdate(sqlStmt);
            
            Log.show(sqlStmt);
            
        } catch (SQLException e) {
            System.out.println("Ocurrió un problema en la operación: " + e);
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            dbDisconnect();
        }
    }
    
    public static int getLastRowId(String table) throws SQLException, ClassNotFoundException {
 
    	int rowId = 0;
		String selectStmt = "SELECT MAX(rowid) as lastId \n" +
							"FROM " + table + ";\n"; 

		Log.show(selectStmt);
		
        try {
            ResultSet rs = DBUtil.dbExecuteQuery(selectStmt);
 
            while (rs.next())	        	
                rowId = rs.getInt("lastId");
            
        } catch (SQLException e) {
            System.out.println("Ha fallado la operación select: " + e);
            throw e;
        }		
    	    	
        return rowId;
    }
    
	private static boolean tieneEsquema() throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT name FROM sqlite_master WHERE type='table' AND name='clientes'";
 
        Log.show(selectStmt);
        
        try {
            ResultSet rs = DBUtil.dbExecuteQuery(selectStmt);
 
			if (rs.next())
				return true;
			else
				return false;
			
        } catch (SQLException e) {
            System.out.println("Ha fallado la operación select: " + e);
            throw e;
        }
	}    
}