package at.basketballsalzburg.bbstats.pages;

import java.lang.annotation.Annotation;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.PageLink;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Request;

import at.basketballsalzburg.bbstats.components.Box;
import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.dto.CoachDTO;
import at.basketballsalzburg.bbstats.dto.GameDTO;
import at.basketballsalzburg.bbstats.dto.GameStatDTO;
import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.dto.PracticeDTO;
import at.basketballsalzburg.bbstats.security.Permissions;
import at.basketballsalzburg.bbstats.services.GameService;
import at.basketballsalzburg.bbstats.services.PlayerService;
import at.basketballsalzburg.bbstats.services.PracticeService;
import at.basketballsalzburg.bbstats.utils.GymPracticePropertyConduit;
import at.basketballsalzburg.bbstats.utils.TeamPropertyConduit;

@RequiresPermissions(Permissions.playerPage)
public class Player {

	@Inject
	private Request request;

	@Inject
	private PlayerService playerService;

	@Inject
	private GameService gameService;

	@Inject
	private PracticeService practiceService;

	@Property
	@Persist
	private Long playerId;

	@Property
	@Persist
	private PlayerDTO player;

	@Property
	@Persist
	private List<PracticeDTO> practiceList;

	@Property
	@Persist
	private List<GameDTO> gameList;

	@Component(parameters = "title=prop:player.displayName")
	private Box playerBox;

	@Component(parameters = {"title=message:gameGridBoxTitle", "type=tablebox"})
	private Box gameGridBox;

	@Component(parameters = {"title=message:practiceGridBoxTitle", "type=tablebox"})
	private Box practiceGridBox;

	@Component(parameters = { "source=gameList", "empty=message:noGameData",
			"model=gameModel",
			"include=winloss,dateTime,teamA,teamB,result,points,stats",
			"reorder=winloss,dateTime,teamA,teamB,result,points,stats",
			"row=game", "rowsPerPage=9999", "inplace=true" })
	private Grid gameGrid;

	@Component(parameters = { "source=practiceList",
			"empty=message:noPracticeData", "row=practice",
			"model=practiceModel", "rowsPerPage=9999",
			"include=dateTime,gym,duration,coaches,agegroups",
			"reorder=dateTime,gym,duration,coaches,agegroups", "inplace=true" })
	private Grid practiceGrid;

	@Property
	private GameStatDTO gameStat;

	@Component(parameters = { "source=game.stats", "empty=message:noStatsData",
			"row=gameStat", "include=points,fta,ftm,threes,fouls", "add=name",
			"reorder=name,points,fta,ftm,threes,fouls", "inplace=true" })
	private Grid statGrid;

	@Property
	private PracticeDTO practice;

	@Property
	private GameDTO game;

	@Property
	private CoachDTO coach;

	@Property
	private AgeGroupDTO ageGroup;

	@Component
	private Zone practiceGridZone;

	@Component
	private Zone gameGridZone;
	
	@Component(parameters = {"page=coach"})
	private PageLink coachDetail;

	void onActivate(Long playerId) {
		this.playerId = playerId;
		player = playerService.findById(playerId);
		practiceList = practiceService.findAllPracticesForPlayer(playerId);
		gameList = gameService.findAllGamesForPlayer(playerId);
	}

	public int getPoints(GameDTO game, Long playerId) {
		for (GameStatDTO stat : game.getStats()) {
			if (stat.getPlayer().getId().equals(playerId)) {
				return stat.getPoints();
			}
		}
		return 0;
	}

	public int getPoints() {
		return getPoints(game, playerId);
	}

	public boolean isOT() {
		return game.isOT();
	}

	public boolean isWin() {
		return gameService.isWin(game);
	}

	@Inject
	private BeanModelSource beanModelSource;
	@Inject
	private ComponentResources componentResources;

	public BeanModel<PracticeDTO> getPracticeModel() {
		BeanModel<PracticeDTO> beanModel = beanModelSource.createDisplayModel(
				PracticeDTO.class, componentResources.getMessages());
		beanModel.add("gym", new GymPracticePropertyConduit()).sortable(true);
		beanModel.add("ageGroups");
		beanModel.add("coaches");
		beanModel.addEmpty("stats");
		return beanModel;
	}

	public BeanModel<GameDTO> getGameModel() {
		BeanModel<GameDTO> beanModel = beanModelSource.createDisplayModel(
				GameDTO.class, componentResources.getMessages());
		beanModel.add("teama", new TeamPropertyConduit(1)).sortable(true);
		beanModel.add("teamb", new TeamPropertyConduit(2)).sortable(true);
		beanModel.addEmpty("result");
		beanModel.addEmpty("winloss").sortable(true);
		beanModel.add("points", new PointPropertyConduit(playerId)).sortable(
				true);
		beanModel.addEmpty("stats");
		return beanModel;
	}

	public class PointPropertyConduit implements PropertyConduit {

		private Long playerId;

		public PointPropertyConduit(Long playerId) {
			this.playerId = playerId;
		}

		public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
			return null;
		}

		/**
		 * @see org.apache.tapestry5.PropertyConduit#get(java.lang.Object)
		 */
		public Object get(Object instance) {
			GameDTO game = (GameDTO) instance;
			return getPoints(game, playerId);
		}

		public Class<Integer> getPropertyType() {
			return Integer.class;
		}

		public void set(Object instance, Object value) {
			GameDTO game = (GameDTO) instance;
			for (GameStatDTO stat : game.getStats()) {
				if (stat.getPlayer().getId().equals(playerId)) {
					stat.setPoints((Integer) value);
					break;
				}
			}
		}

	}

	public String getCountry() {
		return player.getNationalityDisplayName(request.getLocale());
	}

}
