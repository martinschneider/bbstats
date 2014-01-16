package at.basketballsalzburg.bbstats.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;

public class BbstatsJSStack implements JavaScriptStack {

	private final AssetSource assetSource;

	public BbstatsJSStack(final AssetSource assetSource) {
		this.assetSource = assetSource;
	}

	public String getInitialization() {
		return null;
	}

	public List<Asset> getJavaScriptLibraries() {
		List<Asset> ret = new ArrayList<Asset>();

		ret.add(assetSource.getClasspathAsset(
				"at/basketballsalzburg/bbstats/libraries/jquery.js", null));
		
		ret.add(assetSource.getClasspathAsset(
				"at/basketballsalzburg/bbstats/libraries/jquery.vegas.js", null));
		
		ret.add(assetSource.getClasspathAsset(
				"at/basketballsalzburg/bbstats/libraries/bootstrap.min.js", null));
		
		ret.add(assetSource.getClasspathAsset(
				"at/basketballsalzburg/bbstats/libraries/prototype-fix.js", null));

		return ret;
	}

	public List<StylesheetLink> getStylesheets() {

		return Collections.emptyList();
	}

	public List<String> getStacks() {
		return Collections.emptyList();
	}

}
