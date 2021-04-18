
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HttpRequest {

	final String VERSION = "HTTP/1.1";
	
	String method;
	String uri;
	Map<String, String> headers;
	String body;
	
	public HttpRequest(String method, String uri, String body) {
		this.method = method;
		this.uri = uri;
		this.body = body;
	}
	
	public void addHeader(String headerName, String headerValue) {
		if(headers == null) {
			headers = new HashMap<>();
		}
		headers.put(headerName, headerValue);
	}
	
	public String toRawRequest() {
		String requestLine = method + " " + uri + " " + VERSION + "\r\n";
		String requestHeader = "";
		for(Entry<String, String> header : headers.entrySet()) {
			requestHeader += header.getKey() + ": " + header.getValue() + "\r\n";
		}
		
		String rawRequest = requestLine + requestHeader + "\r\n" + body;
		return rawRequest;
		
	}
	
}
