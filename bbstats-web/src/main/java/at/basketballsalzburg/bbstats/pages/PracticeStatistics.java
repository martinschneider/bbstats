package at.basketballsalzburg.bbstats.pages;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.LinkSubmit;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.joda.time.DateTime;

import at.basketballsalzburg.bbstats.components.Box;
import at.basketballsalzburg.bbstats.components.DateTimeField;
import at.basketballsalzburg.bbstats.components.PageLayout;
import at.basketballsalzburg.bbstats.dto.statistics.AgeGroupPracticeStatisticDTO;
import at.basketballsalzburg.bbstats.dto.statistics.CoachPracticeStatisticDTO;
import at.basketballsalzburg.bbstats.dto.statistics.GymPracticeStatisticDTO;
import at.basketballsalzburg.bbstats.dto.statistics.SimplePlayerStatisticDTO;
import at.basketballsalzburg.bbstats.security.Permissions;
import at.basketballsalzburg.bbstats.services.PracticeService;

@RequiresPermissions(Permissions.practicesPage)
public class PracticeStatistics
{

    @Component
    private PageLayout pageLayout;

    @Component(parameters = {"value=fromDate", "datePattern=dd.MM.yyyy"})
    private DateTimeField fromDateField;

    @Component(parameters = {"value=toDate", "datePattern=dd.MM.yyyy"})
    private DateTimeField toDateField;

    @Component
    private LinkSubmit submit;

    @Component
    private Form form;

    @Component(parameters = {"title=message:datesBoxTitle"})
    private Box datesBox;

    @Component(parameters = {"title=message:playersBoxTitle", "type=tablebox"})
    private Box playersBox;

    @Component(parameters = {"title=message:coachesBoxTitle", "type=tablebox"})
    private Box coachesBox;

    @Component(parameters = {"title=message:gymsBoxTitle", "type=tablebox"})
    private Box gymsBox;

    @Component(parameters = {"title=message:ageGroupsBoxTitle", "type=tablebox"})
    private Box ageGroupsBox;

    @Component
    private Zone resultsZone;

    @Component(parameters = {"source=playerPracticeList",
        "empty=message:noData", "row=playerPractice", "rowsPerPage=10",
        "include=count", "add=name", "reorder=name,count", "inplace=true", "class=table table-striped table-condensed"})
    private Grid playerPracticeGrid;

    @Property
    private SimplePlayerStatisticDTO playerPractice;

    @Component(parameters = {"source=coachPracticeList",
        "empty=message:noData", "row=coachPractice", "rowsPerPage=10",
        "include=count", "add=name", "reorder=name,count", "inplace=true", "class=table table-striped table-condensed"})
    private Grid coachPracticeGrid;

    @Property
    private CoachPracticeStatisticDTO coachPractice;

    @Component(parameters = {"source=ageGroupPracticeList",
        "empty=message:noData", "row=ageGroupPractice", "rowsPerPage=10",
        "include=name, count", "reorder=name,count", "inplace=true", "class=table table-striped table-condensed"})
    private Grid ageGroupPracticeGrid;

    @Property
    private AgeGroupPracticeStatisticDTO ageGroupPractice;

    @Component(parameters = {"source=gymPracticeList", "empty=message:noData",
        "row=gymPractice", "rowsPerPage=10", "include=count",
        "add=fullname", "reorder=fullname,count", "inplace=true", "class=table table-striped table-condensed"})
    private Grid gymPracticeGrid;

    @Property
    private GymPracticeStatisticDTO gymPractice;

    @Property
    @Persist
    private DateTime fromDate;

    @Property
    @Persist
    private DateTime toDate;

    @Inject
    private PracticeService practiceService;

    @SetupRender
    void onSetup()
    {
        if (toDate == null)
        {
            toDate = new DateTime().withTimeAtStartOfDay();
        }
        if (fromDate == null)
        {
            fromDate = toDate.minusMonths(1);
        }
    }

    Object onSuccess()
    {
        return resultsZone;
    }

    public List<SimplePlayerStatisticDTO> getPlayerPracticeList()
    {
        return practiceService.getPlayerStatistics(fromDate.toDate(),
            toDate.toDate());
    }

    public List<CoachPracticeStatisticDTO> getCoachPracticeList()
    {
        return practiceService.getCoachStatistics(fromDate.toDate(),
            toDate.toDate());
    }

    public List<AgeGroupPracticeStatisticDTO> getAgeGroupPracticeList()
    {
        return practiceService.getAgeGroupStatistics(fromDate.toDate(),
            toDate.toDate());
    }

    public List<GymPracticeStatisticDTO> getGymPracticeList()
    {
        return practiceService.getGymStatistics(fromDate.toDate(),
            toDate.toDate());
    }

}
