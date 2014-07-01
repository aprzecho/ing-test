package pl.ing.wad.rpt.report.tools;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class DbDataTools {

	public static String extractValue(int columnType, ResultSet rs, int i)
			throws SQLException {

		String value = "";

		switch (columnType) {
		case Types.TINYINT:
		case Types.SMALLINT:
		case Types.INTEGER:
		case Types.BIGINT:
		case Types.FLOAT:
		case Types.REAL:
		case Types.DOUBLE:
		case Types.NUMERIC:
		case Types.DECIMAL:
			BigDecimal numericValue = rs.getBigDecimal(i);
			if (rs.wasNull()) {
				value = "";
			} else {
				value = numericValue.toString();
			}
			break;
		default:
			value = rs.getString(i);
			if (value == null) {
				value = "";
			}
			break;
		}

		return value;
	}	
}
