package at.basketballsalzburg.bbstats.components;

import java.util.ArrayList;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Checkbox;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.LinkSubmit;
import org.apache.tapestry5.corelib.components.Palette;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

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

@Import(library = { "GameEditor.js" })
public class GameEditor {
	public static final String GAME_EDIT_CANCEL = "gameeditcancel";
	public static final String GAME_EDIT_SAVE = "gameeditsave";
	private static final String INVALID_PLAYER_STATS = "invalidPlayerStats";

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
	private JavaScriptSupport javaScriptSupport;

	@Inject
	@Property
	@Path("GameEditor.js")
	private Asset gameEditorJs;

	@Inject
	@Property
	private CoachValueEncoder coachValueEncoder;

	@Inject
	@Property
	private PlayerValueEncoder playerValueEncoder;

	@Inject
	@Property
	private AgeGroupValueEncoder ageGroupValueEncoder;

	private Object zoneToUpdate;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String zone;

	@Component(parameters = { "update=show" })
	private Zone periodsZone;

	@Component
	// (parameters = { "zone=gameEditZone" })
	private Form gameEditForm;

	@Component(parameters = "type=innerbox")
	private Box gameStatBox;

	@Component(parameters = { "value=game.dateTime",
			"datePattern=dd.MM.yyyy, HH:mm", "timePicker=true",
			"timePickerAdjacent=true", "use24hrs=true" })
	@Validate(value = "required")
	private DateTimeField date;

	@Component(parameters = { "value=game.periods", "model=literal:4,2,1",
			"zone=periodsZone", "blankOption=NEVER" })
	@Validate(value = "required")
	private Select periods;

	@Component(parameters = { "value=game.scoreA1", "nulls=zero" })
	@Validate(value = "min=0")
	private TextField scoreA;

	@Component(parameters = { "value=game.scoreB1", "nulls=zero" })
	@Validate(value = "min=0")
	private TextField scoreB;

	@Component(parameters = { "value=game.scoreA1", "nulls=zero" })
	@Validate(value = "min=0")
	private TextField scoreAFirstHalf;

	@Component(parameters = { "value=game.scoreB1", "nulls=zero" })
	@Validate(value = "min=0")
	private TextField scoreBFirstHalf;

	@Component(parameters = { "value=game.scoreA3", "nulls=zero" })
	@Validate(value = "min=0")
	private TextField scoreASecondHalf;

	@Component(parameters = { "value=game.scoreB3", "nulls=zero" })
	@Validate(value = "min=0")
	private TextField scoreBSecondHalf;

	@Component(parameters = { "value=game.scoreA1", "nulls=zero" })
	@Validate(value = "min=0")
	private TextField scoreAFirstQuarter;

	@Component(parameters = { "value=game.scoreB1", "nulls=zero" })
	@Validate(value = "min=0")
	private TextField scoreBFirstQuarter;

	@Component(parameters = { "value=game.scoreA2", "nulls=zero" })
	@Validate(value = "min=0")
	private TextField scoreASecondQuarter;

	@Component(parameters = { "value=game.scoreB2", "nulls=zero" })
	@Validate(value = "min=0")
	private TextField scoreBSecondQuarter;

	@Component(parameters = { "value=game.scoreA3", "nulls=zero" })
	@Validate(value = "min=0")
	private TextField scoreAThirdQuarter;

	@Component(parameters = { "value=game.scoreB3", "nulls=zero" })
	@Validate(value = "min=0")
	private TextField scoreBThirdQuarter;

	@Component(parameters = { "value=game.scoreA4", "nulls=zero" })
	@Validate(value = "min=0")
	private TextField scoreAFourthQuarter;

	@Component(parameters = { "value=game.scoreB4", "nulls=zero" })
	@Validate(value = "min=0")
	private TextField scoreBFourthQuarter;

	@Component(parameters = { "value=game.scoreAV", "nulls=zero" })
	@Validate(value = "min=0")
	private TextField scoreAOvertime;

	@Component(parameters = { "value=game.scoreBV", "nulls=zero" })
	@Validate(value = "min=0")
	private TextField scoreBOvertime;

	@Component(parameters = { "value=game.penalized" })
	private Checkbox penalized;

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

	@Inject
	private Messages messages;
	
	@Inject
	private Block onePeriod, twoPeriods, fourPeriods;

	@Component
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

	@Component(parameters = { "zone=prop:zone" })
	private LinkSubmit submitButton;

	@Component(parameters = { "event=cancel", "zone=prop:zone" })
	private EventLink cancel;

	@Persist
	private GameDTO game;

	@Property
	@Validate(value = "required")
	private Long gymId;

	@Property
	@Validate(value = "required")
	@Persist
	private Long leagueId;

	@Property
	@Validate(value = "required")
	@Persist
	private Long teamAId;

	@Property
	@Validate(value = "required")
	@Persist
	private Long teamBId;

	public GameDTO getGame() {
		return game;
	}

	public void setGame(GameDTO game) {
		this.game = game;
	}

	@AfterRender
	void afterRender() {
		javaScriptSupport.importJavaScriptLibrary(gameEditorJs);
	}

	@OnEvent(value = "selected", component = "submitButton")
	void onSubmitButton() {
		gameStatEditor.setModified(false);
	}

	@OnEvent(value = EventConstants.VALUE_CHANGED, component = "periods")
	Object onValueChangedFromPeriods(int value) {
		if (value == 1) {
			game.setScoreA2(0);
			game.setScoreA3(0);
			game.setScoreA4(0);
			game.setScoreB2(0);
			game.setScoreB3(0);
			game.setScoreB4(0);
		} else if (value == 2) {
			game.setScoreA2(0);
			game.setScoreA4(0);
			game.setScoreB2(0);
			game.setScoreB4(0);
		}
		return periodsZone;
	}

	Object onSuccessFromGameEditForm() {
		if (gameStatEditor.isModified()) {
			return false;
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
		componentResources.triggerEvent(GAME_EDIT_SAVE, null, getMyCallback());
		return false; // return zoneToUpdate
	}

	private ComponentEventCallback<Object> getMyCallback() {

		ComponentEventCallback<Object> callback = new ComponentEventCallback<Object>() {
			public boolean handleResult(Object result) {
				zoneToUpdate = result;
				return false;
			}
		};

		return callback;
	}

	@OnEvent(value = "cancel")
	Object onEventFromCancel() {
		componentResources
				.triggerEvent(GAME_EDIT_CANCEL, null, getMyCallback());
		return zoneToUpdate;
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

		if (game.getStats() == null) {
			game.setStats(new ArrayList<GameStatDTO>());
		}
		gameStatEditor.setGameStats(game.getStats());
		gameStatEditor.setGameId(game.getId());
	}

	public Object getPeriodResultFields() {
		switch (game.getPeriods().intValue()) {
		case 1:
			return onePeriod;
		case 2:
			return twoPeriods;
		case 4:
			return fourPeriods;
		default:
			return null;
		}
	}

	public boolean isNoResult() {
		return gameService.isNoResult(game);
	}

	public boolean isMissingPlayerStats() {
		return game.getId() != null && gameService.isMissingPlayerStats(game);
	}

	public boolean isInvalidPlayerStats() {
		return game.getId() != null && gameService.isInvalidPlayerStats(game);
	}

	public boolean isQuestionablePeriodStats() {
		return gameService.isQuestionablePeriodStats(game);
	}

	public String getInvalidPlayerStatsMessage() {
		int teamScore = 0;
		int playerScore = 0;
		if (gameService.isHome(game) && gameService.isAway(game)) {
			teamScore = game.getScoreA() + game.getScoreB();
		} else if (gameService.isHome(game)) {
			teamScore = game.getScoreA();
		} else if (gameService.isAway(game)) {
			teamScore = game.getScoreB();
		}
		for (GameStatDTO stat : game.getStats()) {
			playerScore += stat.getPoints();
		}
		return messages.format(INVALID_PLAYER_STATS, teamScore, playerScore);
	}

}
