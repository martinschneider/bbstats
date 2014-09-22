package at.basketballsalzburg.bbstats.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ajax.MultiZoneUpdate;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.LinkSubmit;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;

import at.basketballsalzburg.bbstats.dataimport.GameStatCSVImporter;
import at.basketballsalzburg.bbstats.dto.GameStatDTO;
import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.services.GameService;
import at.basketballsalzburg.bbstats.services.PlayerService;
import at.basketballsalzburg.bbstats.utils.PlayerSelectModel;
import at.basketballsalzburg.bbstats.utils.PlayerValueEncoder;

public class GameStatEditor {

	private static final String GAME_STAT_NEW = "gamestatnew";
	private static final String GAME_STAT_IMPORT = "gamestatimport";
	private static final String GAME_STAT_CANCEL = "gamestatcanceledit";
	private static final String GAME_STAT_CANCEL_IMPORT = "gamestatcancelimport";
	private static final String GAME_STAT_DELETE = "gamestatdelete";
	private static final String GAME_STAT_EDIT = "gamestatedit";

	@Persist
	private List<GameStatDTO> gameStats;

	@Persist
	private Long gameId;

	@Inject
	private PlayerService playerService;

	@Inject
	private GameService gameService;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private SelectModelFactory selectModelFactory;

	@Inject
	@Property
	private PlayerValueEncoder playerValueEncoder;

	@Inject
	private GameStatCSVImporter importer;

	@Component(parameters = { "value=data", "size=500" })
	private TextArea input;

	@Component(parameters = { "zone=literal:gameStatImporterZone" })
	private LinkSubmit importData;

	@Property
	@Persist
	private String data;

	@Component(parameters = { "source=gameStats", "empty=message:noStatsData",
			"row=gameStatForGrid", "include=points,threes,fouls",
			"add=name,ft,edit,delete",
			"reorder=name,points,ft,threes,fouls,edit,delete",
			"class=table table-striped table-condensed" })
	private Grid gameStatGrid;

	@Component(parameters = { "value=playerId", "model=playerSelectModel" })
	private Select playerSelect;

	@Component(parameters = { "value=gamestat.points", "nulls=zero" })
	private TextField points;

	@Component(parameters = { "value=gamestat.threes", "nulls=zero" })
	private TextField threes;

	@Component(parameters = { "value=gamestat.fta", "nulls=zero" })
	private TextField fta;

	@Component(parameters = { "value=gamestat.ftm", "nulls=zero" })
	private TextField ftm;

	@Component(parameters = { "value=gamestat.fouls", "nulls=zero" })
	private TextField fouls;

	@Component(parameters = { "event=gamestatnew", "zone=gameStatEditZone" })
	private EventLink newGameStat;

	@Component(parameters = { "event=gamestatimport", "zone=gameStatEditZone" })
	private EventLink importGameStats;

	@Component(parameters = { "event=gamestatedit", "context=gameStatIndex",
			"zone=gameStatEditZone" })
	private EventLink editGameStat;

	@Component(parameters = { "event=gamestatdelete", "context=gameStatIndex",
			"zone=gameStatEditZone" })
	private EventLink deleteGameStat;

	@Component(parameters = { "event=gamestatcanceledit",
			"zone=gameStatEditZone" })
	private EventLink cancelEdit;

	@Component(parameters = { "event=gamestatcancelimport",
			"zone=gameStatEditZone" })
	private EventLink cancelImport;

	@Component
	private Zone gameStatEditZone;

	@Component
	private Zone gameStatGridZone;

	@Property
	@Persist
	private SelectModel playerSelectModel;

	@Component
	private LinkSubmit statSubmit;

	private boolean modified;

	@Persist
	private boolean showImporter;

	@Persist
	private boolean edit;

	@Persist
	private boolean showEditor;

	@Property
	@Persist
	private GameStatDTO gameStat;

	@Property
	private GameStatDTO gameStatForGrid;

	@Property
	@Persist
	private Long playerId;

	void onSelectedFromStatSubmit() {
		modified = true;
		gameStat.setPlayer(playerService.findById(playerId));
		if (!gameStats.contains(gameStat)) {
			gameStats.add(gameStat);
		}
		showEditor = false;
	}

	void onSelectedFromImportData() {
		modified = true;
		gameStats.addAll(importer.importCSV(data));
		showImporter = false;
		showEditor = false;
	}

	Object onSucessFromStatSubmit() {
		return new MultiZoneUpdate(gameStatGridZone).add(gameStatEditZone);
	}

	Object onSucessFromImportData() {
		return new MultiZoneUpdate(gameStatGridZone).add(gameStatEditZone);
	}

	@OnEvent(value = GAME_STAT_NEW)
	Object onNew() {
		gameStat = new GameStatDTO();
		playerId = null;
		edit = false;
		showEditor = true;
		showImporter = false;
		return gameStatEditZone;
	}

	@OnEvent(value = GAME_STAT_IMPORT)
	Object onImport() {
		showImporter = true;
		return gameStatEditZone;
	}

	@OnEvent(value = GAME_STAT_CANCEL_IMPORT)
	Object onCancelImport() {
		showImporter = false;
		gameStats = gameService.findById(gameId).getStats();
		return gameStatEditZone;
	}

	@OnEvent(value = GAME_STAT_CANCEL)
	Object onCancelEdit() {
		showEditor = false;
		return gameStatEditZone;
	}

	@OnEvent(value = GAME_STAT_DELETE)
	Object onDelete(int index) {
		gameStats.remove(index);
		return gameStatGridZone;
	}

	@OnEvent(value = GAME_STAT_EDIT)
	Object onEdit(int index) {
		gameStat = gameStats.get(index);
		playerId = gameStat.getPlayer().getId();
		edit = true;
		showEditor = true;
		return gameStatEditZone;
	}

	@SetupRender
	void setupRender() {
		playerSelectModel = new PlayerSelectModel(new ArrayList<PlayerDTO>(
				playerService.findAllWithAgeGroup()));
	}

	public Integer getGameStatIndex() {
		for (int i = 0; i < gameStats.size(); i++) {
			if (gameStats.get(i).equals(gameStatForGrid)
					|| (gameStatForGrid.getId() == null && gameStats.get(i)
							.hashCode() == gameStats.hashCode())) {
				return i;
			}
		}
		throw new RuntimeException("GameStat could not be found in the list");
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public List<GameStatDTO> getGameStats() {
		return gameStats;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public boolean isShowEditor() {
		return showEditor;
	}

	public void setShowEditor(boolean showEditor) {
		this.showEditor = showEditor;
	}

	public void setGameStats(List<GameStatDTO> stats) {
		this.gameStats = stats;
	}

	public boolean isShowImporter() {
		return showImporter;
	}

	public void setShowImporter(boolean showImporter) {
		this.showImporter = showImporter;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
}
