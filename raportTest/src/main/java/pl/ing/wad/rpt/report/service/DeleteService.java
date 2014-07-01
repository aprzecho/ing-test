package pl.ing.wad.rpt.report.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pl.ing.wad.rpt.report.dao.ReportBaseDAO;
import pl.ing.wad.rpt.report.filenet.FileNetCommunication;

public class DeleteService extends ReportBaseDAO {

	public void startService() throws SQLException {

		Number orderId = 0;
		boolean deleteReports = true;
		
		System.out.println("Start kasowania starych raportow.");
		
		while (deleteReports) {
			
			Connection connection = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			
			try {
				connection = getConnection();
//				pobieramy jeden wiersz do przetworzenia
				st = connection.prepareStatement("select * from RPT_ORDERED where ORD_OST_ID = 2 and ORD_EXP_DATE < trunc(SYSDATE)  and rownum = 1");
				rs = st.executeQuery();
				
				if(rs != null && rs.next()) {
					String resultPath = rs.getString("ORD_RESULT_PATH");
					orderId = rs.getInt("ORD_ID");
					FileNetCommunication fileNetCommunication = new FileNetCommunication();
					fileNetCommunication.deleteDocument(resultPath);
					System.out.println("Kasowanie zamowienia o id = " + orderId);
					st.executeUpdate("delete from RPT_ORDERED WHERE ORD_ID = "  + orderId);
					connection.commit();
					
					/**
					File deleteThis = new File(resultPath);
					if(deleteThis.delete()){
						System.out.println("Usunieto plik: " + deleteThis.getPath());
					}else{
						System.out.println("Nie udala sie proba usuniecia pliku: " + deleteThis.getPath());
					}
					**/

 
				} else {
					System.out.println("Brak raportow do usuniecia ...zamykam serwis batchowy");
					deleteReports = false;
				}
			
			} catch (SQLException e) {
				rollbackSilently(connection);
				deleteReports = false;
				throw e;
			} finally {
				closeConnectionSilently(connection, st, rs);
			}
		}
		
		
	}
	
}
