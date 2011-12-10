package at.basketballsalzburg.bbstats.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.services.Response;

public class XLSStreamResponse implements StreamResponse {
	private InputStream is;
	private String filename = "default";

	public XLSStreamResponse(InputStream is, String filename) {
		this.is = is;
		this.filename = filename;
	}

	public String getContentType() {
		return "application/vnd.ms-excel";
	}

	public InputStream getStream() throws IOException {
		return is;
	}

	public void prepareResponse(Response arg0) {
		arg0.setHeader("Content-Disposition", "attachment; filename="
				+ filename + ".xls");
	}
}
