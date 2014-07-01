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
		ReportRequest request = null;

		try {

			conn = getConnection();
			List<ReportRequest> taskList = dao.getReportRequests(conn);

			for (ReportRequest i : taskList) {

				request = i;

				// mark report, grab data, commit (release db snapshot)
				dao.markReport(conn, i, ReportStatus.IN_PROGRESS);
				Pair<String[], List<String[]>> repData = dao.executeReport(
						conn, i.getReportSql());
				conn.commit();

				// save to file & fileNet
				RptWorker worker = new RptWorker();
				String filePath = worker.saveReportToFile(repData.getLeft(),
						repData.getRight(), i);
				String documentId = worker.saveToFilNet(filePath);

				dao.markReport(conn, i, documentId, ReportStatus.DONE);
			}

		} catch (SQLException e) {
			rollbackSilently(conn);
			dao.markReportSilently(conn, request, ReportStatus.ERROR);
			throw e;
		} catch (IOException e) {
			rollbackSilently(conn);
			dao.markReportSilently(conn, request, ReportStatus.ERROR);
			throw e;
		} finally {
			closeConnectionSilently(conn);
		}
	}

}
