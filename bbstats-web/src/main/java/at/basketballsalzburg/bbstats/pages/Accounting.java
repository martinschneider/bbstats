package at.basketballsalzburg.bbstats.pages;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.MixinClasses;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.LinkSubmit;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.joda.time.DateTime;

import at.basketballsalzburg.bbstats.commons.CssConstants;
import at.basketballsalzburg.bbstats.components.Box;
import at.basketballsalzburg.bbstats.components.DateTimeField;
import at.basketballsalzburg.bbstats.components.PageLayout;
import at.basketballsalzburg.bbstats.components.Select2;
import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.dto.CoachDTO;
import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.dto.PracticeDTO;
import at.basketballsalzburg.bbstats.mixins.GridColumnDecorator;
import at.basketballsalzburg.bbstats.security.Permissions;
import at.basketballsalzburg.bbstats.select2.ChoiceProvider;
import at.basketballsalzburg.bbstats.select2.Settings;
import at.basketballsalzburg.bbstats.select2.provider.CoachProvider;
import at.basketballsalzburg.bbstats.services.CoachService;
import at.basketballsalzburg.bbstats.services.PracticeService;

@RequiresPermissions(Permissions.accountingPage)
public class Accounting {
	@Component
	private PageLayout pageLayout;

	@Component(parameters = { "title=prop:resultsBoxTitle", "type=tablebox" })
	private Box resultsBox;

	@Component(parameters = { "title=message:selectBoxTitle" })
	private Box selectBox;

	@Component(parameters = { "value=dateFrom", "datePattern=dd.MM.yyyy" })
	private DateTimeField dateFromField;

	@Component(parameters = { "value=dateTo", "datePattern=dd.MM.yyyy" })
	private DateTimeField dateToField;

	@Component(parameters = { "value=salaryPerPractice" })
	private TextField salaryPerPracticeField;

	@Component
	private LinkSubmit submit;

	@Component
	private Zone resultsZone;

	@MixinClasses(GridColumnDecorator.class)
	@Component(parameters = { "source=practices",
			"empty=message:noPracticeData", "row=practice",
			"model=practiceModel", "rowsPerPage=30",
			"include=dateTime,duration,gym", "inplace=true", "add=ageGroups",
			"reorder=dateTime,gym,duration,ageGroups",
			"class=table table-striped table-condensed",
			"gridColumnDecorator.cssClassMap=cssClassMap" })
	private Grid practiceGrid;

	@Component(parameters = { "settings=settings", "type=literal:hidden",
			"provider=coachProvider", "singleValue=coach" })
	private Select2 coachSelect;

	@Property
	private Settings settings;

	@InjectComponent
	private Form form;

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
	private JavaScriptSupport javaScriptSupport;

	@Property
	@Persist
	private DateTime dateFrom;

	@Property
	@Persist
	private DateTime dateTo;

	@Property
	@Persist
	private CoachDTO coach;

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
	private AgeGroupDTO ageGroup;

	@SetupRender
	void onSetup() {
		if (dateTo == null) {
			dateTo = new DateTime();
		}
		if (dateFrom == null) {
			dateFrom = dateTo.minusMonths(1);
		}
		if (salaryPerPractice == null) {
			salaryPerPractice = BigDecimal.ZERO;
		}
		if (coach != null) {
			practices = practiceService.findPracticesForCoachBetweenDates(
					coach.getId(), dateFrom.toDate(), dateTo.toDate());
			numberOfPractices = practices.size();
			salary = new BigDecimal(numberOfPractices)
					.multiply(salaryPerPractice);
		}
		settings = new Settings();
		settings.setWidth("100%");
	}

	@Cached
	public ChoiceProvider<CoachDTO> getCoachProvider() {
		return new CoachProvider(coachService);
	}

	public BeanModel<PracticeDTO> getPracticeModel() {
		BeanModel<PracticeDTO> beanModel = beanModelSource.createDisplayModel(
				PracticeDTO.class, componentResources.getMessages());
		beanModel.add("gym", null).sortable(true);
		return beanModel;
	}

	public String getResultsBoxTitle() {
		return messages.get("sum") + ": " + numberOfPractices + ", "
				+ messages.get("salary") + ": " + salary;
	}

	public Map<String, String> getCssClassMap() {
		Map<String, String> values = new HashMap<String, String>();
		values.put("gym", CssConstants.HIDDEN_XS);
		return values;
	}

	Object onSuccessFromForm() {
		return resultsZone;
	}
}
