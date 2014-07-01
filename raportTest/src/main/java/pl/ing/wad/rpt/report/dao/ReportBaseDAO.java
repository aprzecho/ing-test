package pl.ing.wad.rpt.report.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import pl.ing.wad.rpt.report.BatchReportGenMain;
import pl.ing.wad.rpt.report.cfg.ReportConfigsDB;
import pl.ing.wad.rpt.report.cfg.ReportConfigsFile;

public abstract class ReportBaseDAO {

	private static Logger log = Logger.getLogger(ReportBaseDAO.class.getName());
	
	protected static ReportConfigsFile reportConfigsFile;
	protected static ReportConfigsDB reportConfigsDB;
	
	static {
		reportConfigsFile = ReportConfigsFile.getInstance();
		reportConfigsDB = ReportConfigsDB.getInstance();
	}
	
	private Connection connection;

	protected Connection getConnection() throws SQLException {
		return openConnection();
	}

	protected Connection openConnection() throws SQLException {
		if (connection == null) {
				connection = DriverManager.getConnection(ReportConfigsFile
						.getInstance().getConnectionString(), ReportConfigsFile
						.getInstance().getUser(), ReportConfigsFile
						.getInstance().getPassword());
			
		}
		return connection;
	}

	protected void closeConnectionSilently(Connection connection) {
		DbUtils.closeQuietly(connection);
		connection = null;
	}
	
	protected void closeConnectionSilently(Connection connection, Statement stmt, ResultSet rs) {
		DbUtils.closeQuietly(rs);
		rs = null;
		DbUtils.closeQuietly(stmt);
		stmt = null;
		DbUtils.closeQuietly(connection);
		connection = null;
	}
	
	protected void rollbackSilently(Connection connection) {
		try {
			DbUtils.rollback(connection);
		} catch (SQLException e) {
			log.warn("Rollback failed.");
		}
	}
}