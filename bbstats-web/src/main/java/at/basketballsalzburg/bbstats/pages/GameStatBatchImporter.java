package at.basketballsalzburg.bbstats.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.LinkSubmit;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.ioc.annotations.Inject;

import at.basketballsalzburg.bbstats.components.PageLayout;
import at.basketballsalzburg.bbstats.dataimport.GameStatCSVImporter;
import at.basketballsalzburg.bbstats.dataimport.ImportException;

public class GameStatBatchImporter {
	@Component
	private PageLayout pageLayout;

	@Inject
	private GameStatCSVImporter importer;

	@Component
	private Form importForm;

	@Component(parameters = { "value=data", "size=500" })
	private TextArea input;

	@Component(parameters = { "value=resultMessage", "size=500" })
	private TextArea result;

	@Component
	private LinkSubmit importData;

	@Property
	private String data;

	@Property
	@Persist
	private String resultMessage;

	void onSuccess() throws ImportException {
		resultMessage = importer.importCSV(data);
	}
}
