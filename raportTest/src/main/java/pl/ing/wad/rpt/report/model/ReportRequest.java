package pl.ing.wad.rpt.report.model;

/**
 * 
 * @author APRZECHO
 *
 */
public class ReportRequest {

	private Integer orderId;
	private String reportSql;
	private String header;
	private String generator;
	
	public ReportRequest() {};
	
	public ReportRequest(Integer orderId, String reportSql, String header,
			String generator) {
		super();
		this.orderId = orderId;
		this.reportSql = reportSql;
		this.header = header;
		this.generator = generator;
	}
	
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
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
