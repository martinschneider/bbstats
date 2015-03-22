package at.basketballsalzburg.bbstats.pages;

import java.math.BigDecimal;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.LinkSubmit;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.SelectModelFactory;
import org.joda.time.DateTime;

import at.basketballsalzburg.bbstats.components.Box;
import at.basketballsalzburg.bbstats.components.DateTimeField;
import at.basketballsalzburg.bbstats.components.PageLayout;
import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.dto.CoachDTO;
import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.dto.PracticeDTO;
import at.basketballsalzburg.bbstats.security.Permissions;
import at.basketballsalzburg.bbstats.services.CoachService;
import at.basketballsalzburg.bbstats.services.PracticeService;
import at.basketballsalzburg.bbstats.utils.CoachValueEncoder;

@RequiresPermissions(Permissions.accountingPage)
public class Accounting
{
    @Component
    private PageLayout pageLayout;

    @Component(parameters = {"title=prop:resultsBoxTitle", "type=tablebox"})
    private Box resultsBox;

    @Component(parameters = {"title=message:selectBoxTitle"})
    private Box selectBox;

    @Component(parameters = {"value=selectedCoach",
        "model=coachSelectModel", "encoder=coachValueEncoder",
    })
    private Select coachSelect;

    @Component(parameters = {"value=dateFrom", "datePattern=dd.MM.yyyy"})
    private DateTimeField dateFromField;

    @Component(parameters = {"value=dateTo", "datePattern=dd.MM.yyyy"})
    private DateTimeField dateToField;

    @Component(parameters = {"value=salaryPerPractice"})
    private TextField salaryPerPracticeField;

    @Component
    private LinkSubmit submit;

    @Component
    private Zone resultsZone;

    @Component(parameters = {"source=practices",
        "empty=message:noPracticeData", "row=practice",
        "model=practiceModel", "rowsPerPage=30",
        "include=dateTime,duration,gym", "inplace=true",
        "add=ageGroups",
        "reorder=dateTime,gym,duration,ageGroups",
        "class=table table-striped table-condensed"})
    private Grid practiceGrid;

    @Inject
    private CoachService coachService;

    @Inject
    private PracticeService practiceService;

    @Inject
    private SelectModelFactory selectModelFactory;

    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private ComponentResources componentResources;

    @Inject
    private Messages messages;

    @Inject
    @Property
    private CoachValueEncoder coachValueEncoder;

    @Persist
    @Property
    private SelectModel coachSelectModel;

    @Property
    @Persist
    private DateTime dateFrom;

    @Property
    @Persist
    private DateTime dateTo;

    @Property
    @Persist
    private CoachDTO selectedCoach;

    @Property
    @Persist
    private int numberOfPractices;

    @Property
    @Persist
    private BigDecimal salary;

    @Property
    @Persist
    private BigDecimal salaryPerPractice;

    @Property
    @Persist
    private List<PracticeDTO> practices;

    @Property
    private PracticeDTO practice;

    @Property
    private PlayerDTO player;

    @Property
    private CoachDTO coach;

    @Property
    private AgeGroupDTO ageGroup;

    @SetupRender
    void onSetup()
    {
        if (dateTo == null)
        {
            dateTo = new DateTime();
        }
        if (dateFrom == null)
        {
            dateFrom = dateTo.minusMonths(1);
        }
        if (salaryPerPractice == null)
        {
            salaryPerPractice = BigDecimal.ZERO;
        }
        coachSelectModel = selectModelFactory.create(coachService.findAll(),
            "displayName");
        if (selectedCoach != null)
        {
            practices =
                practiceService.findPracticesForCoachBetweenDates(selectedCoach.getId(), dateFrom.toDate(),
                    dateTo.toDate());
            numberOfPractices = practices.size();
            salary = new BigDecimal(numberOfPractices).multiply(salaryPerPractice);
        }
    }

    public BeanModel<PracticeDTO> getPracticeModel()
    {
        BeanModel<PracticeDTO> beanModel = beanModelSource.createDisplayModel(
            PracticeDTO.class, componentResources.getMessages());
        beanModel.add("gym", null).sortable(true);
        return beanModel;
    }

    Object onSuccess()
    {
        return resultsZone;
    }

    public String getResultsBoxTitle()
    {
        return messages.get("sum") + ": " + numberOfPractices + ", "
            + messages.get("salary") + ": " + salary;
    }

}
