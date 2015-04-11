package at.basketballsalzburg.bbstats.pages;

import java.util.Locale;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ExceptionReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.basketballsalzburg.bbstats.components.Box;
import at.basketballsalzburg.bbstats.components.PageLayout;
import at.basketballsalzburg.bbstats.services.ErrorReportService;

/**
 * @author Martin Schneider
 */
public class ExceptionReport implements ExceptionReporter {
	Logger LOGGER = LoggerFactory.getLogger("bbstats");

	@Inject
	private ErrorReportService errorReporter;

	@Property
	private Throwable exception;

	@Property
	private String logReference;

	public void reportException(Throwable exception) {
		this.exception = exception;
		logReference = RandomStringUtils.randomAlphanumeric(7).toUpperCase(
				Locale.US);
		LOGGER.error(logReference, exception);
		String username;
		if (SecurityUtils.getSubject() == null
				|| SecurityUtils.getSubject().getPrincipal() == null) {
			username = "anonymous";
		} else {
			username = SecurityUtils.getSubject().getPrincipal().toString();
		}

		errorReporter.sendErrorReport(username, logReference, exception);
	}

	@Component
	private PageLayout pageLayout;

	@Component(parameters = { "title=message:exceptionBoxTitle" })
	private Box exceptionBox;
}