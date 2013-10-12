package at.basketballsalzburg.bbstats.pages;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.MixinClasses;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.PageLink;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import at.basketballsalzburg.bbstats.components.Box;
import at.basketballsalzburg.bbstats.components.PageLayout;
import at.basketballsalzburg.bbstats.components.PracticeEditor;
import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.dto.CoachDTO;
import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.dto.PracticeDTO;
import at.basketballsalzburg.bbstats.mixins.Permission;
import at.basketballsalzburg.bbstats.security.Permissions;
import at.basketballsalzburg.bbstats.services.GymService;
import at.basketballsalzburg.bbstats.services.PracticeService;
import at.basketballsalzburg.bbstats.utils.GymPracticePropertyConduit;

@RequiresPermissions(Permissions.practicesPage)
public class Practices {

	@Component
	private PageLayout pageLayout;

	@Component(parameters = "title=message:practiceEditorBoxTitle")
	private Box practiceEditorBox;

	@Component(parameters = {"title=message:practiceGridBoxTitle", "type=tablebox"})
	private Box practiceGridBox;

	@Component
	private PracticeEditor practiceEditor;

	@Component(parameters = { "source=practiceList",
			"empty=message:noPracticeData", "row=practice",
			"model=practiceModel", "rowsPerPage=20",
			"include=dateTime,duration,gym", "inplace=true",
			"add=players,coaches,ageGroups,edit,delete",
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
	private List<PracticeDTO> practiceList;

	@Component(parameters = { "event=edit", "context=practice.id", "Permission.allowedPermissions=editPractice" })
	@MixinClasses(Permission.class)
	private EventLink editPractice;
	
	@Component(parameters = { "event=delete", "context=practice.id", "Permission.allowedPermissions=deletePractice" })
	@MixinClasses(Permission.class)
	private EventLink deletePractice;

	@Component(parameters = { "event=new", "Permission.allowedPermissions=newPractice" })
	@MixinClasses(Permission.class)
	private EventLink newPractice;
	
	@Component(parameters = {"page=player"})
	private PageLink playerDetail;
	
	@Component(parameters = {"page=coach"})
	private PageLink coachDetail;

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
		practiceList = practiceService.findAll();
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
		practiceList = practiceService.findAll();
		editorVisible = false;
	}

	@OnEvent(value = PracticeEditor.PRACTICE_EDIT_SAVE)
	void onSave() {
		practiceList = practiceService.findAll();
		editorVisible = false;
	}
	
	@Inject
	private BeanModelSource beanModelSource;
	@Inject
	private ComponentResources componentResources;

	public BeanModel<PracticeDTO> getPracticeModel() {
		BeanModel<PracticeDTO> beanModel = beanModelSource.createDisplayModel(PracticeDTO.class,
				componentResources.getMessages());
		beanModel.add("gym", new GymPracticePropertyConduit()).sortable(true);
		return beanModel;
	}

	@SetupRender
	void setup() {
		if (practiceList == null) {
			practiceList = practiceService.findAll();
		}
	}
}
