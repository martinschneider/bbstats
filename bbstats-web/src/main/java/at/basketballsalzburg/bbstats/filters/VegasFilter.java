package at.basketballsalzburg.bbstats.filters;

import java.io.IOException;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;

public class VegasFilter implements RequestFilter {

	final private AssetSource assetSource;

	/**
	 * Single constructor of this class.
	 * 
	 * @param assetSource
	 *            an {@link AssetSource}.
	 */
	public VegasFilter(AssetSource assetSource) {
		super();
		this.assetSource = assetSource;
	}

	public boolean service(Request request, Response response,
			RequestHandler handler) throws IOException {

		String path = request.getPath();
		boolean handled = false;

		// we only handle requests for wymeditor asset URLs and which weren't
		// redirected already
		if (path.contains("background.jpg")
				&& request.getParameter("redirected") == null) {

			// create an Asset pointing to it. Its URL will contain the right
			// checksum
			// for this file
			Asset asset = assetSource
					.getClasspathAsset("META-INF/assets/images/background.jpg");

			// we create the redirection target URL with "/?redirected" in the
			// end
			// so we can spot and avoid redirection infinite loops.
			final String redirectionUrl = asset.toClientURL() + "/?redirected";

			// finally, we redirect.
			response.sendRedirect(redirectionUrl);
			handled = true;
		}

		// if we didn't redirect, we pass this request to the next RequestFilter
		// in the pipeline
		return handled ? handled : handler.service(request, response);

	}

}