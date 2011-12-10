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

import at.basketballsalzburg.bbstats.dto.LeagueDTO;
import at.basketballsalzburg.bbstats.services.LeagueService;

public class LeagueEditor {
	public static final String LEAGUE_EDIT_CANCEL = "leagueeditcancel";
	public static final String LEAGUE_EDIT_SAVE = "leagueeditsave";

	@Inject
	private LeagueService leagueService;

	@Inject
	private ComponentResources componentResources;

	@Component
	private Form leagueEditForm;

	@Component(parameters = { "value=league.name" })
	private TextField name;

	@Component(parameters = { "value=league.shortname" })
	private TextField shortname;
	@Component
	private LinkSubmit submit;

	@Component(parameters = "event=cancel")
	private EventLink cancel;

	@Persist
	private LeagueDTO league;

	void onSuccess() {
		leagueService.save(league);
		componentResources.triggerEvent(LEAGUE_EDIT_SAVE, null, null);
	}

	public LeagueDTO getLeague() {
		return league;
	}

	public void setLeague(LeagueDTO league) {
		this.league = league;
	}

	@OnEvent(value = "cancel")
	void onEventFromCancel() {
		componentResources.triggerEvent(LEAGUE_EDIT_CANCEL, null, null);
	}

}
