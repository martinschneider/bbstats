package at.basketballsalzburg.bbstats.pages;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.LinkSubmit;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.chenillekit.tapestry.core.components.DateTimeField;

import at.basketballsalzburg.bbstats.components.Box;
import at.basketballsalzburg.bbstats.components.PageLayout;
import at.basketballsalzburg.bbstats.components.PracticeEditor;
import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.dto.CoachDTO;
import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.dto.PracticeDTO;
import at.basketballsalzburg.bbstats.services.GymService;
import at.basketballsalzburg.bbstats.services.PracticeService;

public class Practices {

	@Component
	private PageLayout pageLayout;

	@Component(parameters = "title=message:practiceFilterBoxTitle")
	private Box practiceFilterBox;

	@Component(parameters = "title=message:practiceEditorBoxTitle")
	private Box practiceEditorBox;

	@Component(parameters = "title=message:practiceGridBoxTitle")
	private Box practiceGridBox;

	@Component
	private PracticeEditor practiceEditor;

	@Component(parameters = { "value=dateFrom", "datePattern=dd.MM.yyyy" })
	private DateTimeField dateFromField;

	@Component(parameters = { "value=dateTo", "datePattern=dd.MM.yyyy" })
	private DateTimeField dateToField;

	@Component
	private LinkSubmit dateSubmit;

	@Component(parameters = { "source=practiceList",
			"empty=message:noPracticeData", "row=practice",
			"model=practiceModel", "rowsPerPage=9999",
			"include=dateTime,duration",
			"add=players,coaches,ageGroups,gym,edit,delete",
			"reorder=dateTime,gym,duration,players,coaches,ageGroups,edit,delete" })
	private Grid practiceGrid;

	@Inject
	private PracticeService practiceService;

	@Inject
	private GymService gymService;

	@Property
	private PracticeDTO practice;

	@Property
	private PlayerDTO player;

	@Property
	private CoachDTO coach;

	@Property
	private AgeGroupDTO ageGroup;

	@Property
	@Persist
	private Date dateFrom;

	@Property
	@Persist
	private Date dateTo;

	@Property
	@Persist
	private List<PracticeDTO> practiceList;

	@Component(parameters = { "event=edit", "context=practice.id" })
	private EventLink editPractice;

	@Component(parameters = { "event=delete", "context=practice.id" })
	private EventLink deletePractice;

	@Component(parameters = { "event=new" })
	private EventLink newPractice;

	@Component
	private Zone practiceEditorZone;

	@Component
	private Zone practiceGridZone;

	@Property
	@Persist
	private boolean editorVisible;

	@OnEvent(value = "edit")
	Object onEdit(Long practiceId) {
		practiceEditor.setPractice(findPracticeById(practiceId));
		editorVisible = true;
		return practiceEditorZone;
	}

	@OnEvent(value = "delete")
	Object onDelete(Long practiceId) {
		practiceService.delete(practiceService.findById(practiceId));
		practiceList = practiceService.findAll(dateFrom, dateTo);
		return practiceGridZone;
	}

	private PracticeDTO findPracticeById(Long practiceId) {
		for (PracticeDTO practice : practiceList) {
			if (practice.getId().equals(practiceId)) {
				return practice;
			}
		}
		return null;
	}

	@OnEvent(value = "new")
	Object onNew() {
		PracticeDTO newPractice = new PracticeDTO();
		practiceEditor.setPractice(newPractice);
		editorVisible = true;
		return practiceEditorZone;
	}

	@OnEvent(value = PracticeEditor.PRACTICE_EDIT_CANCEL)
	void onCancel() {
		editorVisible = false;
	}

	@OnEvent(value = PracticeEditor.PRACTICE_EDIT_SAVE)
	void onSave() {
		editorVisible = false;
	}

	Object onSuccess() {
		practiceList = practiceService.findAll(dateFrom, dateTo);
		return practiceGridZone;
	}

	@Inject
	private BeanModelSource beanModelSource;
	@Inject
	private ComponentResources componentResources;

	public BeanModel<PracticeDTO> getPracticeModel() {
		return beanModelSource.createDisplayModel(PracticeDTO.class,
				componentResources.getMessages());
	}

	@SetupRender
	void setup() {
		if (dateTo == null) {
			dateTo = new Date();
		}
		if (dateFrom == null) {
			Calendar now = Calendar.getInstance();
			now.add(Calendar.MONTH, -1);
			dateFrom = now.getTime();
		}
		if (practiceList == null) {
			practiceList = practiceService.findAll(dateFrom, dateTo);
		}
	}
}
