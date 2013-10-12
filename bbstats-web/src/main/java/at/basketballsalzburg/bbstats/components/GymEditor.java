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

import at.basketballsalzburg.bbstats.dto.GymDTO;
import at.basketballsalzburg.bbstats.services.GymService;

public class GymEditor {
	public static final String GYM_EDIT_CANCEL = "gymeditcancel";
	public static final String GYM_EDIT_SAVE = "gymeditsave";

	@Inject
	private GymService gymService;

	@Inject
	private ComponentResources componentResources;

	@Component
	private Form gymEditForm;

	@Component(parameters = { "value=gym.name" })
	private TextField name;

	@Component(parameters = { "value=gym.adress" })
	private TextField adress;

	@Component(parameters = { "value=gym.postalCode" })
	private TextField postalCode;

	@Component(parameters = { "value=gym.city" })
	private TextField city;

	@Component(parameters = { "value=gym.country" })
	private TextField country;

	@Component(parameters = { "value=gym.shortName" })
	private TextField shortName;

	@Component
	private LinkSubmit submit;

	@Component(parameters = "event=cancel")
	private EventLink cancel;

	@Persist
	private GymDTO gym;

	void onSuccess() {
		gymService.save(gym);
		componentResources.triggerEvent(GYM_EDIT_SAVE, null, null);
	}

	public GymDTO getGym() {
		return gym;
	}

	public void setGym(GymDTO gym) {
		this.gym = gym;
	}

	@OnEvent(value = "cancel")
	void onEventFromCancel() {
		componentResources.triggerEvent(GYM_EDIT_CANCEL, null, null);
	}

}
