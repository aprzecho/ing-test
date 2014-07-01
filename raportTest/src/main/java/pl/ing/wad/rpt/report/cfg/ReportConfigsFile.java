package pl.ing.wad.rpt.report.cfg;

import org.apache.log4j.Logger;

import pl.ing.wad.rpt.report.BatchReportGenMain;
import pl.ing.wad.rpt.report.tools.ConfigLoader;

public class ReportConfigsFile {

	static Logger log = Logger.getLogger(BatchReportGenMain.class.getName());

	private String connectionString;
	private String user;
	private String password;

	private static ReportConfigsFile instance = null;

	private ReportConfigsFile() {
	}

	public static ReportConfigsFile getInstance() {
		if (instance == null) {
			instance = new ReportConfigsFile();
		}
		return instance;
	}

	public void loadConfiguration(String filePath) throws Exception {
		
		try {
			
			ConfigLoader loader = new ConfigLoader();
			loader.loadConfigFile(filePath);
			
			connectionString = loader.getElementValue("dburl");
			user = loader.getElementValue("user");
			password = loader.getElementValue("pass");			
			
			log.info("Configuration from file " + filePath + " loaded.");
			log.info(this.toString());
			
		} catch (Exception ex) {
			throw new Exception("Error while loading configuration from file: " + filePath, ex);
		}
	}

	public String getConnectionString() {
		return connectionString;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "ReportConfigsFile [connectionString=" + connectionString
				+ ", user=" + user + "]";
	}			
	
}
