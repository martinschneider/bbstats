package at.basketballsalzburg.bbstats.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.LinkSubmit;
import org.apache.tapestry5.corelib.components.Palette;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;
import org.chenillekit.tapestry.core.components.DateTimeField;

import at.basketballsalzburg.bbstats.dto.GameDTO;
import at.basketballsalzburg.bbstats.dto.GameStatDTO;
import at.basketballsalzburg.bbstats.services.AgeGroupService;
import at.basketballsalzburg.bbstats.services.CoachService;
import at.basketballsalzburg.bbstats.services.GameService;
import at.basketballsalzburg.bbstats.services.GymService;
import at.basketballsalzburg.bbstats.services.LeagueService;
import at.basketballsalzburg.bbstats.services.PlayerService;
import at.basketballsalzburg.bbstats.services.TeamService;
import at.basketballsalzburg.bbstats.utils.AgeGroupValueEncoder;
import at.basketballsalzburg.bbstats.utils.CoachValueEncoder;
import at.basketballsalzburg.bbstats.utils.GymSelectModel;
import at.basketballsalzburg.bbstats.utils.LeagueSelectModel;
import at.basketballsalzburg.bbstats.utils.PlayerValueEncoder;
import at.basketballsalzburg.bbstats.utils.TeamSelectModel;

public class GameEditor {
	public static final String GAME_EDIT_CANCEL = "gameeditcancel";
	public static final String GAME_EDIT_SAVE = "gameeditsave";

	@Inject
	private GameService gameService;

	@Inject
	private GymService gymService;

	@Inject
	private LeagueService leagueService;

	@Inject
	private PlayerService playerService;

	@Inject
	private CoachService coachService;

	@Inject
	private AgeGroupService ageGroupService;

	@Inject
	private TeamService teamService;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private SelectModelFactory selectModelFactory;

	@Inject
	@Property
	private CoachValueEncoder coachValueEncoder;

	@Inject
	@Property
	private PlayerValueEncoder playerValueEncoder;

	@Inject
	@Property
	private AgeGroupValueEncoder ageGroupValueEncoder;

	@Component
	// (parameters = { "zone=gameEditZone" })
	private Form gameEditForm;

	// @Component
	// private Zone gameEditZone;

	@Component
	private Box gameStatBox;

	@Component(parameters = { "value=game.dateTime",
			"datePattern=dd.MM.yyyy, HH:mm", "timePicker=true",
			"timePickerAdjacent=true" })
	private DateTimeField date;

	@Component(parameters = { "value=game.periods" })
	private TextField periods;

	@Component(parameters = { "value=game.scoreA1", "nulls=zero" })
	private TextField scoreA1;

	@Component(parameters = { "value=game.scoreB1", "nulls=zero" })
	private TextField scoreB1;

	@Component(parameters = { "value=game.scoreA2", "nulls=zero" })
	private TextField scoreA2;

	@Component(parameters = { "value=game.scoreB2", "nulls=zero" })
	private TextField scoreB2;

	@Component(parameters = { "value=game.scoreA3", "nulls=zero" })
	private TextField scoreA3;

	@Component(parameters = { "value=game.scoreB3", "nulls=zero" })
	private TextField scoreB3;

	@Component(parameters = { "value=game.scoreA4", "nulls=zero" })
	private TextField scoreA4;

	@Component(parameters = { "value=game.scoreB4", "nulls=zero" })
	private TextField scoreB4;

	@Component(parameters = { "value=game.scoreAV", "nulls=zero" })
	private TextField scoreAV;

	@Component(parameters = { "value=game.scoreBV", "nulls=zero" })
	private TextField scoreBV;

	@Component(parameters = { "value=gymId", "model=gymSelectModel" })
	private Select gymSelect;

	@Component(parameters = { "value=leagueId", "model=leagueSelectModel" })
	private Select leagueSelect;

	@Component(parameters = { "value=teamAId", "model=teamSelectModel" })
	private Select teamASelect;

	@Component(parameters = { "value=teamBId", "model=teamSelectModel" })
	private Select teamBSelect;

	@Component(parameters = { "selected=game.coaches",
			"model=coachSelectModel", "encoder=coachValueEncoder",
			"availableLabel=message:availableCoaches",
			"selectedLabel=message:selectedCoaches" })
	private Palette coachPalette;

	@Component(parameters = { "selected=game.ageGroups",
			"model=ageGroupSelectModel", "encoder=ageGroupValueEncoder",
			"availableLabel=message:availableAgeGroups",
			"selectedLabel=message:selectedAgeGroups" })
	private Palette ageGroupPalette;

	@Component(parameters = "gameStatsParameter=game.stats")
	private GameStatEditor gameStatEditor;

	@Property
	private SelectModel gymSelectModel;

	@Property
	private SelectModel leagueSelectModel;

	@Property
	private SelectModel playerSelectModel;

	@Property
	private SelectModel coachSelectModel;

	@Property
	private SelectModel ageGroupSelectModel;

	@Property
	private SelectModel teamSelectModel;

	@Component
	private LinkSubmit submitButton;

	@Component(parameters = "event=cancel")
	private EventLink cancel;

	@Persist
	private GameDTO game;

	@Property
	private Long gymId;

	@Property
	private Long leagueId;

	@Property
	private Long teamAId;

	@Property
	private Long teamBId;

	public GameDTO getGame() {
		return game;
	}

	public void setGame(GameDTO game) {
		this.game = game;
	}

	@OnEvent(value = "selected", component = "submitButton")
	void onSubmtButton() {
		gameStatEditor.setModified(false);
	}

	void onSuccessFromGameEditForm() {
		if (gameStatEditor.isModified()) {
			return;
		}
		game.setGym(gymService.findById(gymId));
		game.setLeague(leagueService.findById(leagueId));
		game.setTeamA(teamService.findById(teamAId));
		game.setTeamB(teamService.findById(teamBId));
		game.setStats(gameStatEditor.getGameStats());
		if (game.getStats() != null) {
			for (GameStatDTO stat : game.getStats()) {
				stat.setGame(game);
			}
		}
		gameService.save(game);
		componentResources.triggerEvent(GAME_EDIT_SAVE, null, null);
	}

	@OnEvent(value = "cancel")
	void onEventFromCancel() {
		componentResources.triggerEvent(GAME_EDIT_CANCEL, null, null);
	}

	@SetupRender
	void setupRender() {
		if (game.getGym() != null && game.getLeague().getId() != null) {
			gymId = game.getGym().getId();
		}
		if (game.getLeague() != null && game.getLeague().getId() != null) {
			leagueId = game.getLeague().getId();
		}
		if (game.getTeamA() != null && game.getTeamA().getId() != null) {
			teamAId = game.getTeamA().getId();
		}
		if (game.getTeamB() != null && game.getTeamB().getId() != null) {
			teamBId = game.getTeamB().getId();
		}
		gymSelectModel = new GymSelectModel(gymService.findAll());
		leagueSelectModel = new LeagueSelectModel(leagueService.findAll());
		teamSelectModel = new TeamSelectModel(teamService.findAll());
		playerSelectModel = selectModelFactory.create(playerService.findAll(),
				"displayName");
		coachSelectModel = selectModelFactory.create(coachService.findAll(),
				"displayName");
		ageGroupSelectModel = selectModelFactory.create(
				ageGroupService.findAll(), "name");
		gameStatEditor.setGameStats(game.getStats());
	}
}
