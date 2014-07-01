package pl.ing.wad.rpt.report.consts;

/**
 * 
 * @author APRZECHO
 *
 */
public enum ReportStatus {

	TODO("1"), DONE("2"), ERROR("3"), IN_PROGRESS("4");

	private String opCode;

	private ReportStatus(String opCode) {
		this.opCode = opCode;
	}

	public String getOpCode() {
		return opCode;
	}
}
