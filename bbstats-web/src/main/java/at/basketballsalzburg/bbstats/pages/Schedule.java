package at.basketballsalzburg.bbstats.pages;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import at.basketballsalzburg.bbstats.components.Box;
import at.basketballsalzburg.bbstats.components.GameEditor;
import at.basketballsalzburg.bbstats.components.PageLayout;
import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.dto.CoachDTO;
import at.basketballsalzburg.bbstats.dto.GameDTO;
import at.basketballsalzburg.bbstats.dto.GameStatDTO;
import at.basketballsalzburg.bbstats.export.ExcelGameExporter;
import at.basketballsalzburg.bbstats.export.ICalGameExporter;
import at.basketballsalzburg.bbstats.mixins.Permission;
import at.basketballsalzburg.bbstats.services.GameService;
import at.basketballsalzburg.bbstats.services.GymService;
import at.basketballsalzburg.bbstats.utils.GenericStreamResponse;
import at.basketballsalzburg.bbstats.utils.GymGamePropertyConduit;
import at.basketballsalzburg.bbstats.utils.LeaguePropertyConduit;
import at.basketballsalzburg.bbstats.utils.TeamPropertyConduit;

public class Schedule {

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
	@Component(parameters = {
			"source=gameList",
			"empty=message:noGameData",
			"row=game",
			"rowsPerPage=20",
			"exclude=id,periods,scorea1,scorea2,scorea3,scorea4,scoreav,scoreb1,scoreb2,scoreb3,scoreb4,scorebv,scorea,scoreb,ot",
			"model=gameModel", "inplace=true" })
	private Grid gameGrid;

	@Inject
	private GameService gameService;

	@Inject
	private GymService gymService;

	@Inject
	private ExcelGameExporter xlsGameExporter;

	@Inject
	private ICalGameExporter iCalGameExporter;

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
	private List<GameDTO> gameList;

	@Component(parameters = { "event=edit", "context=game.id",
			"Permission.allowedPermissions=EDIT_GAME" })
	@MixinClasses(Permission.class)
	private EventLink editGame;

	@Component(parameters = { "event=delete", "context=game.id",
			"zone=gameGridZone", "Permission.allowedPermissions=DELETE_GAME" })
	@MixinClasses(Permission.class)
	private EventLink deleteGame;

	@Component(parameters = { "event=new",
			"Permission.allowedPermissions=NEW_GAME" })
	@MixinClasses(Permission.class)
	private EventLink newGame;

	@Component
	private Zone gameGridZone;

	@Component(parameters = { "event=downloadXLS" })
	private EventLink downloadXLS;

	@Component(parameters = { "event=downloadICal" })
	private EventLink downloadICal;

	@Property
	@Persist
	private boolean editorVisible;

	@SetupRender
	void setup() {
		gameList = gameService.findAfter(new Date());
	}

	@OnEvent(value = "new")
	Object onNew() {
		editorVisible = true;
		GameDTO newGame = new GameDTO();
		gameEditor.setGame(newGame);
		gameEditor.getGame().setPeriods(4);
		return this;
	}

	@OnEvent(value = GameEditor.GAME_EDIT_CANCEL)
	Object onCancel() {
		editorVisible = false;
		gameList = gameService.findAfter(new Date());
		return gameEditorZone.getBody();
	}

	@OnEvent(value = GameEditor.GAME_EDIT_SAVE)
	Object onSave() {
		editorVisible = false;
		gameList = gameService.findAfter(new Date());
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
		gameList = gameService.findAfter(new Date());
		return gameGridZone;
	}

	@OnEvent(value = "downloadXLS")
	Object onDownloadXLS() throws IOException {
		return new GenericStreamResponse("application/vnd.ms-excel", ".xls",
				xlsGameExporter.getFile(gameList), "spiele");
	}

	@OnEvent(value = "downloadICal")
	Object onDownloadICal() throws IOException {
		return new GenericStreamResponse("text/calendar", ".ical",
				iCalGameExporter.getFile(gameList), "spiele");
	}

	@Inject
	private BeanModelSource beanModelSource;
	@Inject
	private ComponentResources componentResources;

	public BeanModel<GameDTO> getGameModel() {
		BeanModel<GameDTO> beanModel = beanModelSource.createDisplayModel(
				GameDTO.class, componentResources.getMessages());
		beanModel.add("gym", new GymGamePropertyConduit()).sortable(true);
		beanModel.add("teama", new TeamPropertyConduit(1)).sortable(true);
		beanModel.add("teamb", new TeamPropertyConduit(2)).sortable(true);
		beanModel.add("league", new LeaguePropertyConduit()).sortable(true);
		if (pageLayout.isPermitted("editGame")) {
			beanModel.add("edit", null).sortable(false);
		}
		if (pageLayout.isPermitted("deleteGame")) {
			beanModel.add("delete", null).sortable(false);
		}
		return beanModel;
	}

	private GameDTO findGameById(Long gameId) {
		if (gameList == null) {
			setup();
		}
		for (GameDTO game : gameList) {
			if (game.getId().equals(gameId)) {
				return game;
			}
		}
		return null;
	}

	public boolean isHome() {
		return gameService.isHome(game);
	}

	public boolean isAway() {
		return gameService.isAway(game);
	}
}
