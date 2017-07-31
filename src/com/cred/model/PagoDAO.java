package com.cred.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.cred.util.DBUtil;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PagoDAO {

	public static void agregarPago(int idCredito, PagoModel pago) throws SQLException, ClassNotFoundException {

		String insertStmt =
				"BEGIN;\n" +
					"INSERT INTO pagos\n" +
					"(IDCREDITO, MONTOPAGO, FECHA)\n" +
					"VALUES\n" +
					"(" + idCredito + "," + 
					pago.getMontoPago().multiply(BigDecimal.valueOf(100)) + "," +
					" (SELECT strftime('%s','" + pago.getFecha().getValue() + "')));\n" +  
				"COMMIT;";

		try {
			DBUtil.dbExecuteUpdate(insertStmt);
		} catch (SQLException e) {
			System.out.print("Error en INSERT Pago: " + e);
			throw e;
		}
	}

	public static void borrarPago(PagoModel pago) throws SQLException, ClassNotFoundException {

		String deleteStmt =
				"BEGIN;\n" +
					"DELETE FROM pagos\n" +
					"WHERE rowid = " + pago.getId() + ";\n" +  
				"COMMIT;";

		try {
			DBUtil.dbExecuteUpdate(deleteStmt);
		} catch (SQLException e) {
			System.out.print("Error en Delete Pago: " + e);
			throw e;
		}
	}
	
	public static ObservableList<PagoModel> buscarPagos(int cerrado) throws SQLException, ClassNotFoundException {

		String selectStmt = "SELECT p.rowid as rowid, " +
									"p.idcredito as idcredito, " +
									"p.montoPago as montoPago, " +
									"date(p.fecha, 'unixepoch') as fecha " +
							"FROM pagos as p " +
									"inner join creditos as c " +
									"on p.idCredito = c.rowid " +
							"WHERE c.cerrado = " + cerrado + " " +  
							"ORDER BY fecha desc;";		
		
		try {
			ResultSet rsPagos = DBUtil.dbExecuteQuery(selectStmt);
 
			ObservableList<PagoModel> listaPagos = getListaPagos(rsPagos);
 
			return listaPagos;
		} catch (SQLException e) {
			System.out.println("SQL select operation has failed: " + e);
			throw e;
		}
	}

	private static ObservableList<PagoModel> getListaPagos(ResultSet rs) throws SQLException, ClassNotFoundException {

		ObservableList<PagoModel> listaPagos = FXCollections.observableArrayList();

		while (rs.next()) {
			PagoModel pago = new PagoModel();

			pago.setId(rs.getInt("ROWID"));
			pago.setIdCredito(rs.getInt("IDCREDITO"));
			pago.setMontoPagoNumeric(BigDecimal.valueOf(rs.getInt("MONTOPAGO")).divide(BigDecimal.valueOf(100)));

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate.parse(rs.getString("FECHA"), formatter);

			pago.setFecha(new SimpleObjectProperty<LocalDate>(localDate));

			listaPagos.add(pago);
		}
		return listaPagos;
	}
}