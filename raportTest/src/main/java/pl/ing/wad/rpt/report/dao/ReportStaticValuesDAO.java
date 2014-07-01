package pl.ing.wad.rpt.report.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


public class ReportStaticValuesDAO extends ReportBaseDAO {
   
    public Map<String, String> loadAppParameters() throws SQLException {

        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        
    	Map<String, String> paramsMap = new HashMap<String, String>();
    	
        try {
        	
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM RPT_PARAMETERS");

            while (rs.next()) {
                paramsMap.put(rs.getString("PRM_NAME"), rs.getString("PRM_VALUE"));
            }
            
            return paramsMap;
            
        } finally {
           closeConnectionSilently(conn, stmt, rs);
        }
    }
    
}
