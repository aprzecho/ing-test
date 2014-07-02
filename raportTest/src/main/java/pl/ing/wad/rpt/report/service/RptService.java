package pl.ing.wad.rpt.report.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import pl.ing.wad.rpt.report.consts.ReportStatus;
import pl.ing.wad.rpt.report.dao.ReportBaseDAO;
import pl.ing.wad.rpt.report.dao.ReportServiceDAO;
import pl.ing.wad.rpt.report.model.ReportRequest;

/**
 * 
 * @author APRZECHO
 * 
 */
public class RptService extends ReportBaseDAO {

	public void startService() throws SQLException, IOException {

		Connection conn = null;

		ReportServiceDAO dao = new ReportServiceDAO();
		
		// hold to mark potential error
		ReportRequest request = null;

		try {

			conn = getConnection();
			List<ReportRequest> taskList = dao.getReportRequests(conn);

			for (ReportRequest i : taskList) {

				request = i;

				// mark report as in progress				
				dao.markReport(conn, i, ReportStatus.IN_PROGRESS);
				conn.commit(); 
				
				// runs sql, gets data into memory, closes rs & stmt
				Pair<String[], List<String[]>> repData = dao.executeReport(
						conn, i.getReportSql());
				
				// (1) ends transaction before io operations
				// (2) in case of repetable reads isolation level
				//     no need to hold data snapshot longer
				conn.commit();

				// save to file & fileNet
				RptWorker worker = new RptWorker();
				String filePath = worker.saveReportToFile(repData.getLeft(),
						repData.getRight(), i);
				String documentId = worker.saveToFilNet(filePath);

				// mark report as done, finishing commit
				dao.markReport(conn, i, documentId, ReportStatus.DONE);
				conn.commit();
			}

		} catch (SQLException e) {
			// silent ops in order not to loose main exception
			rollbackSilently(conn);
			dao.markReportSilentlyAndCommit(conn, request, ReportStatus.ERROR);
			throw e;
		} catch (IOException e) {			
			// there is no transaction during io operations
			// so no need for rollback
			dao.markReportSilentlyAndCommit(conn, request, ReportStatus.ERROR);
			throw e;
		} finally {
			closeConnectionSilently(conn);
		}
	}

}
