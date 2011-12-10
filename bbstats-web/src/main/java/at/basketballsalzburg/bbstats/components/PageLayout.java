package at.basketballsalzburg.bbstats.components;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(stylesheet = { "../styles/reset.css", "../styles/menu.css",
		"../styles/widgets.css", "../styles/text.css", "../styles/global.css",
		"../styles/form.css", "../styles/grid.css", "../styles/palette.css" })
public class PageLayout {

	@Inject
	private Request request;

	@Parameter
	@Property
	private boolean login;

	@Inject
	private JavaScriptSupport javaScriptSupport;

	@Inject
	@Property
	@Path("../libraries/jquery.js")
	private Asset jqueryJs;

	@Inject
	@Property
	@Path("../libraries/jquery.tools.min.js")
	private Asset jqueryToolsJs;

	@AfterRender
	void afterRender() {
		javaScriptSupport.importJavaScriptLibrary(jqueryJs);
		javaScriptSupport.importJavaScriptLibrary(jqueryToolsJs);
	}

	public String getContextPath() {
		return request.getContextPath();
	}

}
