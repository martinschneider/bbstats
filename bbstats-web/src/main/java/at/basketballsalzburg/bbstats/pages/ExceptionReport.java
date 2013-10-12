package at.basketballsalzburg.bbstats.pages;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.services.ExceptionReporter;

import at.basketballsalzburg.bbstats.components.Box;
import at.basketballsalzburg.bbstats.components.PageLayout;

/**
 * @author Martin Schneider
 */
public class ExceptionReport implements ExceptionReporter
{
    @Property
    private Throwable exception;

    public void reportException(Throwable exception)
    {
        this.exception = exception;
    }
    
    public String getExceptionOutput()
    {
    	return ExceptionUtils.getStackTrace(exception);
    }
    
    @Component
    private PageLayout pageLayout;
    
    @Component(parameters={"title=message:exceptionBoxTitle"})
    private Box exceptionBox;
}