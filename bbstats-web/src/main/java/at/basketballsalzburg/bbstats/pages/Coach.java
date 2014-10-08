package at.basketballsalzburg.bbstats.pages;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tapestry5.ComponentResources;
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
import at.basketballsalzburg.bbstats.dto.PracticeDTO;
import at.basketballsalzburg.bbstats.security.Permissions;
import at.basketballsalzburg.bbstats.services.CoachService;
import at.basketballsalzburg.bbstats.services.GameService;
import at.basketballsalzburg.bbstats.services.PracticeService;
import at.basketballsalzburg.bbstats.utils.GameDataSource;
import at.basketballsalzburg.bbstats.utils.GameMode;
import at.basketballsalzburg.bbstats.utils.PracticeDataSource;
import at.basketballsalzburg.bbstats.utils.PracticeMode;

@RequiresPermissions(Permissions.coachPage)
public class Coach
{
    @Inject
    private Request request;

    @Inject
    private CoachService coachService;

    @Inject
    private GameService gameService;

    @Inject
    private PracticeService practiceService;

    @Property
    @Persist
    private Long coachId;

    @Property
    @Persist
    private CoachDTO coach;

    @Property
    @Persist
    private PracticeDataSource practiceSource;

    @Property
    @Persist
    private GameDataSource gameSource;

    @Component(parameters = "title=prop:coach.displayName")
    private Box coachBox;

    @Component(parameters = {"title=message:gameGridBoxTitle", "type=tablebox"})
    private Box gameGridBox;

    @Component(parameters = {"title=message:practiceGridBoxTitle",
        "type=tablebox"})
    private Box practiceGridBox;

    @Component(parameters = {"source=gameSource", "empty=message:noGameData",
        "model=gameModel",
        "include=winloss,dateTime,teamA,teamB,result,stats",
        "reorder=winloss,dateTime,teamA,teamB,result,stats", "row=game",
        "rowsPerPage=20", "inplace=true",
        "class=table table-striped table-condensed"})
    private Grid gameGrid;

    @Component(parameters = {"source=practiceSource",
        "empty=message:noPracticeData", "row=practice",
        "model=practiceModel", "rowsPerPage=20",
        "include=dateTime,gym,duration,coaches,agegroups",
        "reorder=dateTime,gym,duration,coaches,agegroups", "inplace=true",
        "class=table table-striped table-condensed"})
    private Grid practiceGrid;

    @Property
    private GameStatDTO gameStat;

    @Component(parameters = {"source=game.stats", "empty=message:noStatsData",
        "row=gameStat", "include=points,fta,ftm,threes,fouls", "add=name",
        "reorder=name,points,fta,ftm,threes,fouls", "inplace=true", "class=table table-striped table-condensed"})
    private Grid statGrid;

    @Property
    private PracticeDTO practice;

    @Property
    private GameDTO game;

    @Property
    private AgeGroupDTO ageGroup;

    @Component
    private Zone practiceGridZone;

    @Component
    private Zone gameGridZone;

    @Component(parameters = {"page=coach"})
    private PageLink coachDetail;

    void onActivate(Long coachId)
    {
        this.coachId = coachId;
        coach = coachService.findById(coachId);
        practiceSource = new PracticeDataSource(practiceService,
            PracticeMode.COACH, coachId);
        gameSource = new GameDataSource(gameService, GameMode.COACH, coachId);
        if (gameGrid.getSortModel().getSortConstraints().isEmpty())
        {
            gameGrid.getSortModel().updateSort("dateTime");
            gameGrid.getSortModel().updateSort("dateTime");
        }
        if (practiceGrid.getSortModel().getSortConstraints().isEmpty())
        {
            practiceGrid.getSortModel().updateSort("dateTime");
            practiceGrid.getSortModel().updateSort("dateTime");
        }
    }

    public boolean isOT()
    {
        return game.isOT();
    }

    public boolean isWin()
    {
        return gameService.isWin(game);
    }

    @Inject
    private BeanModelSource beanModelSource;
    @Inject
    private ComponentResources componentResources;

    public BeanModel<PracticeDTO> getPracticeModel()
    {
        BeanModel<PracticeDTO> beanModel = beanModelSource.createDisplayModel(
            PracticeDTO.class, componentResources.getMessages());
        beanModel.add("gym", null).sortable(true);
        beanModel.add("ageGroups");
        beanModel.add("coaches");
        beanModel.addEmpty("stats");
        return beanModel;
    }

    public BeanModel<GameDTO> getGameModel()
    {
        BeanModel<GameDTO> beanModel = beanModelSource.createDisplayModel(
            GameDTO.class, componentResources.getMessages());
        beanModel.add("teamA", null).sortable(true);
        beanModel.add("teamB", null).sortable(true);
        beanModel.addEmpty("result");
        beanModel.addEmpty("winloss").sortable(true);
        beanModel.addEmpty("stats");
        return beanModel;
    }
}
