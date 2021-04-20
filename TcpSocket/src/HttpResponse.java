
public class HttpResponse {

	private String httpVersion;
	private int statusCode;
	private String reasonPhrase;
	private String headers;
	private String body;
	
	public HttpResponse(String rawResponse) {
		String[] lines = rawResponse.split("\r\n");
		
		// Process status line
		String statusLine = lines[0];
		String[] statusLineParts = statusLine.split(" ");
		httpVersion = statusLineParts[0];
		statusCode = Integer.parseInt(statusLineParts[1]);
		reasonPhrase = statusLineParts[2];
		if(statusLineParts.length > 3) {
			for(int i = 3; i<statusLineParts.length; i++) {
				reasonPhrase += " " + statusLineParts[i];
			}
		}
		
		// Process header and body
		headers = "";
		body = "";
		boolean isHeader = true;
		for(int i=1; i<lines.length; i++) {
			String line = lines[i];
			if(line.isEmpty()) {
				isHeader = false;
			}
			if(isHeader) {
				headers += line + "\r\n";
			} else {
				body += line + "\r\n";
			}
		}
	}

	public String getHttpVersion() {
		return httpVersion;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}

	public String getHeaders() {
		return headers;
	}

	public String getBody() {
		return body;
	}
	
	
	
}
