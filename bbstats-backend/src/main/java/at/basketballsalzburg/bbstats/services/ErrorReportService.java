package at.basketballsalzburg.bbstats.services;

public interface ErrorReportService {
	void sendErrorReport(String username, String logRefrence, Throwable throwable);
}
