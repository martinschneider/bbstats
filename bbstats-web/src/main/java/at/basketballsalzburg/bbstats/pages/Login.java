package at.basketballsalzburg.bbstats.pages;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.ActionLink;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;

import at.basketballsalzburg.bbstats.components.Box;
import at.basketballsalzburg.bbstats.components.PageLayout;

@Import(stylesheet = { "Login.css" })
public class Login {

	@Component(parameters = "login=true")
	private PageLayout pageLayout;

	@Component
	private Box loginBox;

	@Component
	private ActionLink loginButton;

	@Inject
	private Messages messages;

	public Object onActivate() {
		if (isLogin()) {
			return Index.class;
		} else {
			return null;
		}
	}

	@Inject
	private JavaScriptSupport javaScriptSupport;

	@Inject
	@Property
	@Path("Login.js")
	private Asset loginJs;

	@AfterRender
	void afterRender() {
		javaScriptSupport.importJavaScriptLibrary(loginJs);
	}

	@Inject
	private Request request;

	public String getContextPath() {
		return request.getContextPath();
	}

	public boolean isLogin() {

		return !hasRole("ROLE_ANONYMOUS");
	}

	private boolean hasRole(final String role) {
		return SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities().contains(new GrantedAuthorityImpl(role));
	}
}
