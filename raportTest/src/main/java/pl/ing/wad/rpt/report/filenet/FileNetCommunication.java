package pl.ing.wad.rpt.report.filenet;


public class FileNetCommunication implements IFileNetCommunication {
	
	private ContentEngineSession contentEngineSession = null;

	
	public FileNetCommunication() {
		contentEngineSession = new ContentEngineSession();
		//TODO implement
	}
	
	public void deleteReport(String id) {
		//TODO implement
	}
	
	public void testFile() throws Exception {
		//TODO test
	
	}
	
	public void deleteDocument(String id) {
		//TODO implement
	}
	
	public String createDocument(String pathToSystemFile) {
		return null;
		//TODO implement
	}	
}
