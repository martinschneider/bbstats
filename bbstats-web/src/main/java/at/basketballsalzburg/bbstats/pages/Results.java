package at.basketballsalzburg.bbstats.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import at.basketballsalzburg.bbstats.commons.CssConstants;
import at.basketballsalzburg.bbstats.components.Box;
import at.basketballsalzburg.bbstats.components.GameEditor;
import at.basketballsalzburg.bbstats.components.PageLayout;
import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.dto.CoachDTO;
import at.basketballsalzburg.bbstats.dto.GameDTO;
import at.basketballsalzburg.bbstats.dto.GameStatDTO;
import at.basketballsalzburg.bbstats.mixins.GridColumnDecorator;
import at.basketballsalzburg.bbstats.mixins.Permission;
import at.basketballsalzburg.bbstats.services.GameService;
import at.basketballsalzburg.bbstats.services.GymService;
import at.basketballsalzburg.bbstats.utils.GameDataSource;
import at.basketballsalzburg.bbstats.utils.GameMode;

public class Results {

	private static final String INVALID_PLAYER_STATS = "invalidPlayerStats";

	@Component
	private PageLayout pageLayout;

	@Component(parameters = { "title=message:gameGridBoxTitle", "type=tablebox" })
	private Box gameGridBox;

	@Component(parameters = { "zone=gameEditorZone" })
	private GameEditor gameEditor;

	@Component(parameters = { "title=message:gameEditorBoxTitle" })
	private Box gameEditorBox;

	@Component
	private Zone gameEditorZone;

	// columns depend on user roles. use custom model and exclude instead of
	// include
	@MixinClasses(GridColumnDecorator.class)
	@Component(parameters = {
			"source=gameSource",
			"empty=message:noGameData",
			"row=game",
			"rowsPerPage=20",
			"exclude=id,penalized,periods,scorea1,scorea2,scorea3,scorea4,scoreav,scoreb1,scoreb2,scoreb3,scoreb4,scorebv,scorea,scoreb,ot",
			"reorder=winloss", "model=gameModel", "inplace=true",
			"class=table table-striped table-condensed",
			"gridColumnDecorator.cssClassMap=cssClassMap" })
	private Grid gameGrid;

	@Component(parameters = { "source=game.stats", "empty=message:noStatsData",
			"row=gameStat", "include=points,fta,ftm,threes,fouls", "add=name",
			"reorder=name,points,fta,ftm,threes,fouls", "inplace=true",
			"class=table table-striped table-condensed", })
	private Grid statGrid;

	@Inject
	private Messages messages;

	@Inject
	private GameService gameService;

	@Inject
	private GymService gymService;

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
	private GameDataSource gameSource;

	@Component(parameters = { "event=edit", "context=game.id",
			"Permission.allowedPermissions=editGame" })
	@MixinClasses(Permission.class)
	private EventLink editGame;

	@Component(parameters = { "event=delete", "context=game.id",
			"zone=gameGridZone" })
	// @MixinClasses(Permission.class)
	/*
	 * The permission mixin leads to a strange bug when deleting the first
	 * entity in the grid. However, it is not necessary since rendering of the
	 * delete link is handled by the grid model.
	 */
	private EventLink deleteGame;

	@Component(parameters = { "event=new",
			"Permission.allowedPermissions=newGame" })
	@MixinClasses(Permission.class)
	private EventLink newGame;

	@Component(parameters = { "page=player" })
	private PageLink playerDetail;

	@Component(parameters = { "page=coach" })
	private PageLink coachDetail;

	@Component
	private Zone gameGridZone;

	@Property
	@Persist
	private boolean editorVisible;

	@SetupRender
	void setup() {
		gameSource = new GameDataSource(gameService, GameMode.RESULTS);
		if (gameGrid.getSortModel().getSortConstraints().isEmpty()) {
			gameGrid.getSortModel().updateSort("dateTime");
			gameGrid.getSortModel().updateSort("dateTime");
		}
	}

	@OnEvent(value = "new")
	Object onNew() {
		editorVisible = true;
		GameDTO newGame = new GameDTO();
		newGame.setAgeGroups(new ArrayList<AgeGroupDTO>());
		newGame.setCoaches(new ArrayList<CoachDTO>());
		gameEditor.setGame(newGame);
		gameEditor.getGame().setPeriods(4);
		return this;
	}

	@OnEvent(value = GameEditor.GAME_EDIT_CANCEL)
	Object onCancel() {
		editorVisible = false;
		return gameEditorZone.getBody();
	}

	@OnEvent(value = GameEditor.GAME_EDIT_SAVE)
	Object onSave() {
		editorVisible = false;
		return gameEditorZone.getBody();
	}

	@OnEvent(value = "edit")
	Object onEdit(Long gameId) {
		editorVisible = true;
		gameEditor.setGame(findGameById(gameId));
		return this;
	}

	@OnEvent(value = "delete")
	Object onDelete(Long gameId) {
		gameService.delete(gameService.findById(gameId));
		return gameGridZone;
	}

	@Inject
	private BeanModelSource beanModelSource;
	@Inject
	private ComponentResources componentResources;

	public BeanModel<GameDTO> getGameModel() {
		BeanModel<GameDTO> beanModel = beanModelSource.createDisplayModel(
				GameDTO.class, componentResources.getMessages());
		beanModel.add("winloss", null).sortable(false);
		beanModel.add("gym", null).sortable(false);
		beanModel.add("teamA", null).sortable(true);
		beanModel.add("teamB", null).sortable(true);
		beanModel.add("league", null).sortable(true);
		beanModel.add("result", null).sortable(false);
		beanModel.add("stats", null).sortable(false);

		if (isEditRight()) {
			beanModel.add("statsErrors", null).sortable(false);
		}
		if (isEditRight() || isDeleteRight()) {
			beanModel.add("actions", null).sortable(false);
		}
		return beanModel;
	}

	public boolean isEditRight() {
		return pageLayout.isPermitted("editGame");
	}

	public boolean isDeleteRight() {
		return pageLayout.isPermitted("deleteGame");
	}

	public Map<String, String> getCssClassMap() {
		Map<String, String> values = new HashMap<String, String>();
		values.put("winloss", CssConstants.HIDDEN_XS);
		values.put("gym", CssConstants.HIDDEN_MD_SM_XS);
		values.put("league", CssConstants.HIDDEN_XS);
		values.put("stats", CssConstants.HIDDEN_XS);
		values.put("statsErrors", CssConstants.HIDDEN_SM_XS);
		values.put("dateTime", CssConstants.HIDDEN_XS);
		return values;
	}

	private GameDTO findGameById(Long gameId) {
		return gameService.findById(gameId);
	}

	public boolean isOT() {
		return game.isOT();
	}

	public boolean isWin() {
		return gameService.isWin(game);
	}

	public boolean isHome() {
		return gameService.isHome(game);
	}

	public boolean isAway() {
		return gameService.isAway(game);
	}

	public boolean isShowStats() {
		return (pageLayout.isPermitted("viewStats") || gameService
				.isShowStats(game));
	}

	public boolean isPublicMode() {
		return !pageLayout.isPermitted("viewStats");
	}

	public boolean isNoResult() {
		return gameService.isNoResult(game);
	}

	public boolean isError() {
		return !getErrorList().isEmpty();
	}

	public String getErrorList() {
		StringBuilder errors = new StringBuilder();
		int count = 0;
		if (gameService.isNoResult(game) && !game.getPenalized()) {
			errors.append(messages.get("missingStats"));
			count++;
		} else if (gameService.isInvalidPlayerStats(game)) {
			errors.append(getInvalidPlayerStatsMessage());
			count++;
		}
		if (isQuestionablePeriodStats()) {
			if (count > 0) {
				errors.append(" ");
			}
			errors.append(messages.get("questionablePeriodStats"));
			count++;
		}
		if (count == 1) {
			errors.insert(0, count + " " + messages.get("error") + ": ");
		} else if (count > 1) {
			errors.insert(0, count + " " + messages.get("errors") + ": ");
		}
		return errors.toString();
	}

	public boolean isInvalidPlayerStats() {
		return gameService.isInvalidPlayerStats(game)
				|| gameService.isNoResult(game);
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
