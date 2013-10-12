package at.basketballsalzburg.bbstats.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.LinkSubmit;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;

import at.basketballsalzburg.bbstats.dto.CoachDTO;
import at.basketballsalzburg.bbstats.services.CoachService;

public class CoachEditor {
	public static final String COACH_EDIT_CANCEL = "coacheditcancel";
	public static final String COACH_EDIT_SAVE = "coacheditsave";

	@Inject
	private CoachService coachService;

	@Inject
	private ComponentResources componentResources;

	@Component
	private Form coachEditForm;

	@Component(parameters = { "value=coach.firstName" })
	private TextField firstName;

	@Component(parameters = { "value=coach.lastName" })
	private TextField lastName;

	@Component
	private LinkSubmit submit;

	@Component(parameters = "event=cancel")
	private EventLink cancel;

	@Persist
	private CoachDTO coach;

	void onSuccess() {
		coachService.save(coach);
		componentResources.triggerEvent(COACH_EDIT_SAVE, null, null);
	}

	public CoachDTO getCoach() {
		return coach;
	}

	public void setCoach(CoachDTO coach) {
		this.coach = coach;
	}

	@OnEvent(value = "cancel")
	void onEventFromCancel() {
		componentResources.triggerEvent(COACH_EDIT_CANCEL, null, null);
	}
}
