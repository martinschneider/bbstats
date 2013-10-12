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

import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.services.AgeGroupService;

public class AgeGroupEditor {
	public static final String AGEGROUP_EDIT_CANCEL = "ageGroupeditcancel";
	public static final String AGEGROUP_EDIT_SAVE = "ageGroupeditsave";

	@Inject
	private AgeGroupService ageGroupService;

	@Inject
	private ComponentResources componentResources;

	@Component
	private Form ageGroupEditForm;

	@Component(parameters = { "value=ageGroup.name" })
	private TextField name;

	@Component
	private LinkSubmit submit;

	@Component(parameters = "event=cancel")
	private EventLink cancel;

	@Persist
	private AgeGroupDTO ageGroup;

	void onSuccess() {
		ageGroupService.save(ageGroup);
		componentResources.triggerEvent(AGEGROUP_EDIT_SAVE, null, null);
	}

	public AgeGroupDTO getAgeGroup() {
		return ageGroup;
	}

	public void setAgeGroup(AgeGroupDTO ageGroup) {
		this.ageGroup = ageGroup;
	}

	@OnEvent(value = "cancel")
	void onEventFromCancel() {
		componentResources.triggerEvent(AGEGROUP_EDIT_CANCEL, null, null);
	}
}
