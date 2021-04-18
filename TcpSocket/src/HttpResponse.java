

public class HttpResponse {

	String version;
	int statusCode;
	String reasonPhrase;

	public HttpResponse(String rawResponse) {
		String[] lines = rawResponse.split("\r\n");
		String statusLine = lines[0];
		String[] statusLineParts = statusLine.split(" ");
		version = statusLineParts[0];
		statusCode = Integer.parseInt(statusLineParts[1]);
		reasonPhrase = statusLineParts[2];
	}

}
