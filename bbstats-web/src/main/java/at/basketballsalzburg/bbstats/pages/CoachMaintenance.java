package at.basketballsalzburg.bbstats.pages;

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
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import at.basketballsalzburg.bbstats.components.Box;
import at.basketballsalzburg.bbstats.components.CoachEditor;
import at.basketballsalzburg.bbstats.components.PageLayout;
import at.basketballsalzburg.bbstats.dto.CoachDTO;
import at.basketballsalzburg.bbstats.services.CoachService;

public class CoachMaintenance {

	@Component
	private PageLayout pageLayout;

	@Component
	private CoachEditor coachEditor;

	@Component(parameters = { "source=coachesList", "model=coachModel",
			"empty=message:noData", "row=coach", "rowsPerPage=9999",
			"include=firstName,lastName", "add=edit,delete",
			"reorder=lastName,firstName,edit,delete" })
	private Grid coachesGrid;

	@Component
	private Zone coachEditorZone;

	@Component
	private Zone coachGridZone;

	@Component(parameters = "title=message:coachEditorBoxTitle")
	private Box coachEditorBox;

	@Component(parameters = "title=message:coachGridBoxTitle")
	private Box coachGridBox;

	@Component(parameters = { "event=edit", "context=coach.id" })
	private EventLink editCoach;

	@Component(parameters = { "event=delete", "context=coach.id" })
	private EventLink deleteCoach;

	@Component(parameters = { "event=new" })
	private EventLink newCoach;

	@Inject
	private CoachService coachService;

	@Property
	private List<CoachDTO> coachesList;

	@Property
	@Persist
	private CoachDTO coach;

	@Property
	@Persist
	private boolean editorVisible;

	@OnEvent(value = "edit")
	Object onEdit(Long coachId) {
		coachEditor.setCoach(coachService.findById(coachId));
		editorVisible = true;
		return coachEditorZone;
	}

	@OnEvent(value = "delete")
	Object onDelete(Long coachId) {
		coachService.delete(coachService.findById(coachId));
		return coachGridZone;
	}

	@OnEvent(value = "new")
	Object onNew() {
		coachEditor.setCoach(new CoachDTO());
		editorVisible = true;
		return coachEditorZone;
	}

	@OnEvent(value = CoachEditor.COACH_EDIT_CANCEL)
	void onCancel() {
		editorVisible = false;
	}

	@OnEvent(value = CoachEditor.COACH_EDIT_SAVE)
	void onSave() {
		editorVisible = false;
	}

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	public BeanModel<CoachDTO> getCoachModel() {
		return beanModelSource.createDisplayModel(CoachDTO.class,
				componentResources.getMessages());
	}

	@SetupRender
	void setup() {
		coachesList = coachService.findAll();
		if (coachesGrid.getSortModel().getSortConstraints().isEmpty()) {
			coachesGrid.getSortModel().updateSort("lastname");
		}
	}
}