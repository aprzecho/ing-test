package pl.ing.wad.rpt.report;

import org.apache.log4j.Logger;

import pl.ing.wad.rpt.report.cfg.ReportConfigsDB;
import pl.ing.wad.rpt.report.cfg.ReportConfigsFile;
import pl.ing.wad.rpt.report.consts.ConfigConsts;
import pl.ing.wad.rpt.report.service.DeleteService;
import pl.ing.wad.rpt.report.service.RptService;

public class BatchReportGenMain {

	private static Logger log = Logger.getLogger(BatchReportGenMain.class.getName());

	public static void main(String[] args) {

		// get config file path
		String configFilePath = "";
		if (args.length == 0) {
			configFilePath = ConfigConsts.DEFAULT_CONFIG_FILE;
			log.info("Config file not specified, using default filename.");
		} else {
			configFilePath = args[0];
		}
		log.info("Loading configuration with file: " + configFilePath);

		// load configration from file and db
		try {
			ReportConfigsFile.getInstance().loadConfiguration(configFilePath);
			ReportConfigsDB.getInstance().loadConfiguration();
		} catch (Exception e) {
			log.error("Error during configuration", e);
			System.exit(1);
		}
		log.info("Configuration loaded successfully.");

		System.setProperty("java.security.auth.login.config", ReportConfigsDB
				.getInstance().getParamValue(("jaasPath")));
		
		if(!ReportConfigsDB.getInstance().isCanRun()) {
			log.error("No permission to run");
			System.exit(1);
		}
				
		try {
			log.info("Startuje serwis batchowy kasowania raportow RPT");
			new DeleteService().startService();
			log.info("Startuje serwis batchowy raportow RPT");
			new RptService().startService();
			log.info("Serwis batchowy raportow RPT zatrzymany...CU later :]");
		} catch (Exception e) {
			log.error("Error during processing", e);
		}
	}
}
