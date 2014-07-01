package pl.ing.wad.rpt.report.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import pl.ing.wad.rpt.report.consts.ReportStatus;
import pl.ing.wad.rpt.report.model.ReportRequest;
import pl.ing.wad.rpt.report.tools.DbDataTools;

/**
 * 
 * @author APRZECHO
 * 
 */
public class ReportServiceDAO {

	private static Logger log = Logger.getLogger(ReportServiceDAO.class
			.getName());

	public List<ReportRequest> getReportRequests(Connection conn)
			throws SQLException {

		String query = "SELECT ORD_QUERY AS reportSql, ORD_ID AS orderId, ORD_HEADER as header, ORD_GENERATOR as generator FROM RPT_ORDERED where ORD_OST_ID = ?";

		QueryRunner run = new QueryRunner();
		ResultSetHandler<List<ReportRequest>> h = new BeanListHandler<ReportRequest>(
				ReportRequest.class);
		List<ReportRequest> requests = run.query(conn, query, h, 1);

		return requests;
	}

	public void markReport(Connection conn, ReportRequest request,
			ReportStatus status) throws SQLException {

		PreparedStatement pstmt = conn
				.prepareStatement("update RPT_ORDERED SET ORD_OST_ID = ? WHERE ORD_ID = ?");
		pstmt.setInt(1, status.getOpCode());
		pstmt.setInt(2, request.getOrderId());
		pstmt.executeUpdate();
		conn.commit();
		pstmt.close();
	}

	public void markReport(Connection conn, ReportRequest request,
			String documentId, ReportStatus status) throws SQLException {

		PreparedStatement pstmt = conn
				.prepareStatement("update RPT_ORDERED SET ORD_OST_ID = ?, ORD_RESULT_PATH = ? WHERE ORD_ID = ?");
		pstmt.setInt(1, status.getOpCode());
		pstmt.setString(2, documentId);
		pstmt.setInt(3, request.getOrderId());
		pstmt.executeUpdate();
		conn.commit();
		pstmt.close();
	}

	public void markReportSilently(Connection conn, ReportRequest request,
			ReportStatus status) {

		if (request != null) {
			try {
				markReport(conn, request, status);
			} catch (SQLException e) {
				log.warn(
						"Request couldn't be set as error: "
								+ request.getOrderId(), e);
			}
		}
	}

	public Pair<String[], List<String[]>> executeReport(Connection con,
			String reportSql) throws SQLException {

		ResultSetHandler<Pair<String[], List<String[]>>> h = new ResultSetHandler<Pair<String[], List<String[]>>>() {
			public Pair<String[], List<String[]>> handle(ResultSet rs)
					throws SQLException {

				List<String[]> result = new ArrayList<String[]>();

				ResultSetMetaData meta = rs.getMetaData();
				int cols = meta.getColumnCount();
				String[] row = new String[cols];
				String[] labels = new String[cols];

				for (int i = 0; i < cols; i++) {
					labels[i] = meta.getColumnName(i + 1);
				}

				while (rs.next()) {
					for (int i = 1; i < cols; i++) {
						row[i] = DbDataTools.extractValue(
								meta.getColumnType(i), rs, i);
					}
					result.add(row);
				}

				return new ImmutablePair<String[], List<String[]>>(labels,
						result);
			}
		};

		QueryRunner run = new QueryRunner();

		return run.query(con, reportSql, h);
	}

}
