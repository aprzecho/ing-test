package pl.ing.wad.rpt.report.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import pl.ing.wad.rpt.report.cfg.ReportConfigsDB;
import pl.ing.wad.rpt.report.consts.ConfigConsts;
import pl.ing.wad.rpt.report.consts.ConfigKeys;
import pl.ing.wad.rpt.report.filenet.FileNetCommunication;
import pl.ing.wad.rpt.report.model.ReportRequest;
import pl.ing.wad.rpt.report.tools.CsvUtils;

/**
 * 
 * @author APRZECHO
 *
 */
public class RptWorker {

	private static Logger log = Logger.getLogger(RptWorker.class.getName());	

	public String saveReportToFile(String[] labels, List<String[]> repData,
			ReportRequest request) throws IOException {

		PrintWriter out = null;

		String outFileName = "Report_rpt_" + ConfigConsts.sdf.format(new Date()) + ".csv";
		outFileName = request.getOrderId() + "_" + outFileName;

		String filePath = ReportConfigsDB.getInstance().getParamValue(
				ConfigKeys.outFolder)
				+ outFileName;
		try {
			
			// kodowanie odpowienie dla Excela pod windowsem
			out = new PrintWriter(new File(filePath), "windows-1250");

			// jesli skonfigurowano header to zostanie on umieszczony w
			// pierwszej linii
			if (request.getHeader() != null && !request.getHeader().isEmpty()) {
				String header = CsvUtils.escapeCsv(request.getHeader());
				out.print(header);
				out.print(CsvUtils.NEW_LINE);
			}

			String csvRow = CsvUtils.stringArrayToCVS(labels);
			out.print(csvRow);

			for (String[] row : repData) {

				String strRow = CsvUtils.stringArrayToCVS(row);

				// nowa linia przed dodaniem nowego wiersza, aby na koncu pliku
				// nie bylo nowej linii
				out.print(CsvUtils.NEW_LINE);
				out.print(strRow);
			}
			out.close();

			return outFileName;

		} finally {
			closeFileSilently(out);
		}

	}

	private void closeFileSilently(PrintWriter out) {
		if (out != null) {
			try {
				out.close();
			} catch (Exception e) {
				// silent
			}
		}
	}

	public String saveToFilNet(String filePath) {
		FileNetCommunication fileNetCommunication = new FileNetCommunication();
		return fileNetCommunication.createDocument(filePath);
	}

}
