package at.basketballsalzburg.bbstats.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.services.Response;

/**
 * @author Martin Schneider
 */
public class GenericStreamResponse implements StreamResponse {
	private InputStream is;
	private String filename = "default";
	private String contentType;
	private String extension;

	public GenericStreamResponse(String contentType, String extension,
			InputStream is, String filename) {
		this.is = is;
		this.filename = filename;
		this.contentType = contentType;
		this.extension = extension;
	}

	public String getContentType() {
		return contentType;
	}

	public InputStream getStream() throws IOException {
		return is;
	}

	public void prepareResponse(Response arg0) {
		arg0.setHeader("Content-Disposition", "attachment; filename="
				+ filename + extension);
	}
}
