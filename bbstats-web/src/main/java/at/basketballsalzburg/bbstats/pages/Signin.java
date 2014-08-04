package at.basketballsalzburg.bbstats.pages;

import java.io.IOException;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;
import org.tynamo.security.pages.Login;
import org.tynamo.security.services.PageService;
import org.tynamo.security.services.SecurityService;

public class Signin extends Login
{
	@Inject
	private Logger logger;

	@Property
	private String username;

	@Property
	private String password;

	@Property
	private boolean rememberMe;

	@Inject
	private Response response;

	@Inject
	private RequestGlobals requestGlobals;

	@Inject
	private SecurityService securityService;

	@Inject
	private PageService pageService;

	@Inject
	private AlertManager alertManager;

	@OnEvent(EventConstants.SUCCESS)
	public Object submit()
	{
		Subject currentUser = securityService.getSubject();

		if (currentUser == null)
		{
			throw new IllegalStateException("Subject can't be null");
		}

		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		token.setRememberMe(rememberMe);

		try
		{
			currentUser.login(token);
		} catch (UnknownAccountException e)
		{
			alertManager.error("Account not exists");
			return null;
		} catch (IncorrectCredentialsException e)
		{
			alertManager.error("Wrong password");
			return null;
		} catch (LockedAccountException e)
		{
			alertManager.error("Account locked");
			return null;
		} catch (AuthenticationException e)
		{
			alertManager.error("Authentication Error");
			return null;
		}

		SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(requestGlobals.getHTTPServletRequest());

		// TODO: try using shiro's own WebUtils.redirectToSavedRequest
		if (savedRequest != null && savedRequest.getMethod().equalsIgnoreCase("GET"))
		{
			try
			{
				response.sendRedirect(savedRequest.getRequestUrl());
				return null;
			} catch (IOException e)
			{
				logger.warn("Can't redirect to saved request.");
				return pageService.getSuccessPage();
			}
		} else
		{
			return pageService.getSuccessPage();
		}

	}
}