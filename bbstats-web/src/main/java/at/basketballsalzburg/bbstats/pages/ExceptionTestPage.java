package at.basketballsalzburg.bbstats.pages;

import org.apache.tapestry5.annotations.SetupRender;

public class ExceptionTestPage {
	@SetupRender
	void setup() {
		throw new RuntimeException("test exception");
	}
}
