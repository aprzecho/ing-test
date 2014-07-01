package pl.ing.wad.rpt.report.model;

/**
 * 
 * @author APRZECHO
 *
 */
public class ReportRequest {

	private String orderId;
	private String reportSql;
	private String header;
	private String generator;
		
	private ReportRequest(String orderId, String reportSql, String header,
			String generator) {
		super();
		this.orderId = orderId;
		this.reportSql = reportSql;
		this.header = header;
		this.generator = generator;
	}
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getReportSql() {
		return reportSql;
	}
	public void setReportSql(String reportSql) {
		this.reportSql = reportSql;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getGenerator() {
		return generator;
	}
	public void setGenerator(String generator) {
		this.generator = generator;
	}

	@Override
	public String toString() {
		return "ReportRequest [orderId=" + orderId + ", reportSql=" + reportSql
				+ ", header=" + header + ", generator=" + generator + "]";
	}
		
}
