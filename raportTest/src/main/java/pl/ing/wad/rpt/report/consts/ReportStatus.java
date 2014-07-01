package pl.ing.wad.rpt.report.consts;

/**
 * 
 * @author APRZECHO
 *
 */
public enum ReportStatus {

	TODO(1), DONE(2), ERROR(3), IN_PROGRESS(4);

	private Integer opCode;

	private ReportStatus(Integer opCode) {
		this.opCode = opCode;
	}

	public Integer getOpCode() {
		return opCode;
	}
}
