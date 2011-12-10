package at.basketballsalzburg.bbstats.components;

import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.LinkSubmit;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;

import at.basketballsalzburg.bbstats.dto.GameStatDTO;
import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.services.PlayerService;
import at.basketballsalzburg.bbstats.utils.PlayerSelectModel;
import at.basketballsalzburg.bbstats.utils.PlayerValueEncoder;

public class GameStatEditor {

	private static final String GAME_STAT_NEW = "gamestatnew";
	private static final String GAME_STAT_CANCEL = "gamestatcancel";
	private static final String GAME_STAT_DELETE = "gamestatdelete";
	private static final String GAME_STAT_EDIT = "gamestatedit";

	@Persist
	private List<GameStatDTO> gameStats;

	@Inject
	private PlayerService playerService;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private SelectModelFactory selectModelFactory;

	@Inject
	@Property
	private PlayerValueEncoder playerValueEncoder;

	@Component(parameters = { "source=gameStats", "empty=message:noStatsData",
			"row=gameStatForGrid", "include=points,threes,fouls",
			"add=name,ft,edit,delete",
			"reorder=name,points,ft,threes,fouls,edit,delete" })
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

	@Component(parameters = { "event=gamestatnew" })
	private EventLink newGameStat;

	@Component(parameters = { "event=gamestatedit", "context=gameStatIndex" })
	private EventLink editGameStat;

	@Component(parameters = { "event=gamestatdelete", "context=gameStatIndex" })
	private EventLink deleteGameStat;

	@Component(parameters = { "event=gamestatcancel" })
	private EventLink cancel;

	@Component
	private Zone gameStatEditFieldsZone;

	@Property
	private SelectModel playerSelectModel;

	@Component
	private LinkSubmit statSubmit;

	private boolean modified;

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

	@OnEvent(value = GAME_STAT_NEW)
	Object onNew() {
		gameStat = new GameStatDTO();
		playerId = null;
		edit = false;
		showEditor = true;
		return gameStatEditFieldsZone;
	}

	@OnEvent(value = GAME_STAT_CANCEL)
	Object onCancel() {
		showEditor = false;
		return gameStatEditFieldsZone;
	}

	@OnEvent(value = GAME_STAT_DELETE)
	void onDelete(int index) {
		gameStats.remove(index);
	}

	@OnEvent(value = GAME_STAT_EDIT)
	Object onEdit(int index) {
		gameStat = gameStats.get(index);
		playerId = gameStat.getPlayer().getId();
		edit = true;
		showEditor = true;
		return gameStatEditFieldsZone;
	}

	@SetupRender
	void setupRender() {
		playerSelectModel = new PlayerSelectModel(playerService.findAll());
		if (gameStat == null) {
			gameStat = new GameStatDTO();
			gameStat.setPlayer(new PlayerDTO());
		}
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
		this.gameStats=stats;
	}
}
