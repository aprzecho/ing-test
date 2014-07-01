package pl.ing.wad.rpt.report.cfg;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import pl.ing.wad.rpt.report.dao.ReportStaticValuesDAO;

public class ReportConfigsDB {

	private static Logger log = Logger.getLogger(ReportConfigsDB.class
			.getName());
	private static ReportConfigsDB instance;

	private Map<String, String> paramsMap;
	private boolean canRun;

	private ReportConfigsDB() {
		canRun = true;
		paramsMap = new HashMap<String, String>();
	}

	public static ReportConfigsDB getInstance() {
		if (instance == null) {
			instance = new ReportConfigsDB();
		}
		return instance;
	}

	public void loadConfiguration() throws Exception {
		try {
			ReportStaticValuesDAO reportStaticValues = new ReportStaticValuesDAO();
			paramsMap.putAll(reportStaticValues.loadAppParameters());
			log.info("Config from DB loaded: " + paramsMap.toString());
		} catch (Exception ex) {
			throw new Exception("Error while loading configuration DB. ", ex);
		}
	}
	
	public String getParamValue(String key) {
		String out = paramsMap.get(key);
		return (out==null ? "" : out);
	}

	public boolean isCanRun() {
		return canRun;
	}

	public String printParams() {
		return paramsMap.toString();
	}

}
