package at.basketballsalzburg.bbstats.components;

import java.util.Calendar;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.LinkSubmit;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;

import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.services.PlayerService;

public class PlayerEditor {
	public static final String PLAYER_EDIT_CANCEL = "playereditcancel";
	public static final String PLAYER_EDIT_SAVE = "playereditsave";

	@Inject
	private PlayerService playerService;

	@Inject
	private ComponentResources componentResources;

	@Component
	private Form playerEditForm;

	@Component(parameters = { "value=player.firstName" })
	private TextField firstName;

	@Component(parameters = { "value=player.lastName" })
	private TextField lastName;

	@Component(parameters = { "value=player.adress" })
	private TextField adress;

	@Component(parameters = { "value=player.postalCode" })
	private TextField postalCode;

	@Component(parameters = { "value=player.city" })
	private TextField city;

	@Component(parameters = { "value=player.country" })
	private TextField country;

	@Component(parameters = { "value=player.phone" })
	private TextField phone;

	@Component(parameters = { "value=player.email" })
	private TextField email;

	@Component(parameters = { "value=player.birthday", "datePattern=dd.MM.yyyy"})
	private DateTimeField birthday;

	@Component(parameters = { "value=player.nationality" })
	private TextField nationality;

	@Component
	private LinkSubmit submit;

	@Component(parameters = "event=cancel")
	private EventLink cancel;

	@Persist
	private PlayerDTO player;

	void onSuccess() {
		playerService.save(player);
		componentResources.triggerEvent(PLAYER_EDIT_SAVE, null, null);
	}

	public PlayerDTO getPlayer() {
		return player;
	}

	public void setPlayer(PlayerDTO player) {
		this.player = player;
	}

	@OnEvent(value = "cancel")
	void onEventFromCancel() {
		componentResources.triggerEvent(PLAYER_EDIT_CANCEL, null, null);
	}

	public int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}
}
