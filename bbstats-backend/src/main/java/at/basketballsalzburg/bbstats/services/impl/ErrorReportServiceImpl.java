package at.basketballsalzburg.bbstats.services.impl;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import at.basketballsalzburg.bbstats.services.ErrorReportService;

/**
 * @author Martin Schneider
 */
public class ErrorReportServiceImpl implements ErrorReportService {

	@Value("${error.report.email.from}")
	private String from;

	@Value("${error.report.email.to}")
	private String to;

	@Value("${error.report.email.enabled}")
	private boolean emailEnabled;

	private MailSender mailSender;

	@Autowired
	public ErrorReportServiceImpl(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public void sendErrorReport(String username, String logReference,
			Throwable throwable) {
		if (emailEnabled) {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(from);
			message.setTo(to);
			message.setSubject("bbstats error report: " + logReference + ", "
					+ username);
			message.setText(ExceptionUtils.getStackTrace(throwable));
			mailSender.send(message);
		}
	}

}
