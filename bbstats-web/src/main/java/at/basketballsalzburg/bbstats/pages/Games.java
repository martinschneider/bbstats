package at.basketballsalzburg.bbstats.pages;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.LinkSubmit;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.chenillekit.tapestry.core.components.DateTimeField;

import at.basketballsalzburg.bbstats.components.Box;
import at.basketballsalzburg.bbstats.components.GameEditor;
import at.basketballsalzburg.bbstats.components.PageLayout;
import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.dto.CoachDTO;
import at.basketballsalzburg.bbstats.dto.GameDTO;
import at.basketballsalzburg.bbstats.dto.GameStatDTO;
import at.basketballsalzburg.bbstats.services.GameService;
import at.basketballsalzburg.bbstats.services.GymService;

public class Games {

	private static final String TEAM_NAME = "BSC Salzburg";

	@Component
	private PageLayout pageLayout;

	@Component(parameters = "title=message:gameFilterBoxTitle")
	private Box gameFilterBox;

	@Component(parameters = "title=message:gameEditorBoxTitle")
	private Box gameEditorBox;

	@Component(parameters = "title=message:gameGridBoxTitle")
	private Box gameGridBox;

	@Component(parameters = "title=message:statsBoxTitle")
	private Box statsBox;

	@Component
	private GameEditor gameEditor;

	@Component(parameters = { "value=dateFrom", "datePattern=dd.MM.yyyy" })
	private DateTimeField dateFromField;

	@Component(parameters = { "value=dateTo", "datePattern=dd.MM.yyyy" })
	private DateTimeField dateToField;

	@Component
	private LinkSubmit dateSubmit;

	@Component(parameters = { "source=gameList", "empty=message:noGameData",
			"row=game", "include=dateTime", "rowsPerPage=9999",
			"add=teama,teamb,result,stats,league,winloss,edit,delete",
			"reorder=winloss,dateTime,league,teama,teamb,result,stats,edit,delete" })
	private Grid gameGrid;

	@Component(parameters = { "source=game.stats", "empty=message:noStatsData",
			"row=gameStat", "include=points,fta,ftm,threes,fouls", "add=name",
			"reorder=name,points,fta,ftm,threes,fouls" })
	private Grid statGrid;

	@Inject
	private GameService gameService;

	@Inject
	private GymService gymService;

	@Inject
	private JavaScriptSupport javaScriptSupport;

	@Inject
	@Property
	@Path("Games.js")
	private Asset gamesJs;

	@Property
	private GameDTO game;

	@Property
	private GameStatDTO gameStat;

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
	private List<GameDTO> gameList;

	@Component(parameters = { "event=edit", "context=game.id" })
	private EventLink editGame;

	@Component(parameters = { "event=delete", "context=game.id" })
	private EventLink deleteGame;

	@Component(parameters = { "event=new" })
	private EventLink newGame;

	@Component
	private Zone gameEditorZone;

	@Component
	private Zone gameGridZone;

	@Property
	@Persist
	private boolean editorVisible;

	private boolean ot;

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
		if (gameList == null) {
			gameList = gameService.findAll(dateFrom, dateTo);
		}
	}

	@AfterRender
	void afterRender() {
		javaScriptSupport.importJavaScriptLibrary(gamesJs);
	}

	@OnEvent(value = "new")
	Object onNew() {
		GameDTO newGame = new GameDTO();
		gameEditor.setGame(newGame);
		editorVisible = true;
		return gameEditorZone;
	}

	@OnEvent(value = GameEditor.GAME_EDIT_CANCEL)
	void onCancel() {
		editorVisible = false;
	}

	@OnEvent(value = GameEditor.GAME_EDIT_SAVE)
	void onSave() {
		editorVisible = false;
	}

	@OnEvent(value = "edit")
	Object onEdit(Long gameId) {
		gameEditor.setGame(findGameById(gameId));
		editorVisible = true;
		return gameEditorZone;
	}

	@OnEvent(value = "delete")
	Object onDelete(Long gameId) {
		gameService.delete(gameService.findById(gameId));
		gameList = gameService.findAll(dateFrom, dateTo);
		return gameGridZone;
	}

	Object onSuccess() {
		gameList = gameService.findAll(dateFrom, dateTo);
		return gameGridZone;
	}

	private GameDTO findGameById(Long gameId) {
		for (GameDTO game : gameList) {
			if (game.getId().equals(gameId)) {
				return game;
			}
		}
		return null;
	}

	public String printEndResult(GameDTO game) {
		ot = false;
		int sumA = game.getScoreA1().intValue() + game.getScoreA2().intValue()
				+ game.getScoreA3().intValue() + game.getScoreA4().intValue();
		int sumB = game.getScoreB1().intValue() + game.getScoreB2().intValue()
				+ game.getScoreB3().intValue() + game.getScoreB4().intValue();
		if (sumA == sumB) {
			ot = true;
			sumA += game.getScoreAV();
			sumB += game.getScoreBV();
		}
		String resultStr = sumA + ":" + sumB;
		return resultStr;
	}

	public boolean isOT() {
		return ot;
	}

	public String printPeriodResults(GameDTO game) {
		boolean ot = false;
		int sumA = game.getScoreA1().intValue() + game.getScoreA2().intValue()
				+ game.getScoreA3().intValue() + game.getScoreA4().intValue();
		int sumB = game.getScoreB1().intValue() + game.getScoreB2().intValue()
				+ game.getScoreB3().intValue() + game.getScoreB4().intValue();
		if (sumA == sumB) {
			ot = true;
		}
		String resultStr = "";
		if (game.getPeriods().equals(4)) {
			resultStr += " (" + game.getScoreA1() + ":" + game.getScoreB1()
					+ ", " + game.getScoreA2() + ":" + game.getScoreB2() + ", "
					+ game.getScoreA3() + ":" + game.getScoreB3() + ", "
					+ game.getScoreA4() + ":" + game.getScoreB4();
		} else if (game.getPeriods().equals(2)) {
			resultStr += " (" + game.getScoreA1() + ":" + game.getScoreB1()
					+ ", " + game.getScoreA3() + ":" + game.getScoreB3();
		}
		if (ot) {
			resultStr += ", " + game.getScoreAV() + ":" + game.getScoreBV();
		}
		if (game.getPeriods().intValue() > 1) {
			resultStr += ")";
		}
		return resultStr;
	}

	public boolean isWin() {
		int sumA = game.getScoreA1().intValue() + game.getScoreA2().intValue()
				+ game.getScoreA3().intValue() + game.getScoreA4().intValue()
				+ game.getScoreAV().intValue();
		int sumB = game.getScoreB1().intValue() + game.getScoreB2().intValue()
				+ game.getScoreB3().intValue() + game.getScoreB4().intValue()
				+ game.getScoreAV().intValue();
		if ((game.getTeamA().getName().contains(TEAM_NAME) && sumA >= sumB)
				|| (game.getTeamB().getName().contains(TEAM_NAME) && sumB >= sumA)) {
			return true;
		}
		return false;
	}
}
