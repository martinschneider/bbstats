package at.basketballsalzburg.bbstats.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

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
import at.basketballsalzburg.bbstats.utils.GameDataSource;
import at.basketballsalzburg.bbstats.utils.GameMode;
import at.basketballsalzburg.bbstats.utils.GenericStreamResponse;

public class Schedule
{

    @Component
    private PageLayout pageLayout;

    @Component(parameters = {"title=message:gameGridBoxTitle", "type=tablebox"})
    private Box gameGridBox;

    @Component(parameters = {"zone=gameEditorZone"})
    private GameEditor gameEditor;

    @Component(parameters = {"title=message:gameEditorBoxTitle"})
    private Box gameEditorBox;

    @Component
    private Zone gameEditorZone;

    // columns depend on user roles. use custom model and exclude instead of
    // include
    @Component(
        parameters = {
            "source=gameSource",
            "empty=message:noGameData",
            "row=game",
            "rowsPerPage=20",
            "exclude=id,penalized,periods,scorea1,scorea2,scorea3,scorea4,scoreav,scoreb1,scoreb2,scoreb3,scoreb4,scorebv,scorea,scoreb,ot",
            "model=gameModel", "inplace=true", "class=table table-striped table-condensed"})
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
    private GameDataSource gameSource;

    @Component(parameters = {"event=edit", "context=game.id",
        "Permission.allowedPermissions=EDIT_GAME"})
    @MixinClasses(Permission.class)
    private EventLink editGame;

    @Component(parameters = {"event=delete", "context=game.id",
        "zone=gameGridZone", "Permission.allowedPermissions=DELETE_GAME"})
    @MixinClasses(Permission.class)
    private EventLink deleteGame;

    @Component(parameters = {"event=new",
        "Permission.allowedPermissions=NEW_GAME"})
    @MixinClasses(Permission.class)
    private EventLink newGame;

    @Component
    private Zone gameGridZone;

    @Component(parameters = {"event=downloadXLS"})
    private EventLink downloadXLS;

    @Component(parameters = {"event=downloadICal"})
    private EventLink downloadICal;

    @Property
    @Persist
    private boolean editorVisible;

    @SetupRender
    void setup()
    {
        gameSource = new GameDataSource(gameService, GameMode.SCHEDULE);
        if (gameGrid.getSortModel().getSortConstraints().isEmpty())
        {
            gameGrid.getSortModel().updateSort("dateTime");
        }
    }

    @OnEvent(value = "new")
    Object onNew()
    {
        editorVisible = true;
        GameDTO newGame = new GameDTO();
        newGame.setAgeGroups(new ArrayList<AgeGroupDTO>());
        newGame.setCoaches(new ArrayList<CoachDTO>());
        gameEditor.setGame(newGame);
        gameEditor.getGame().setPeriods(4);
        return this;
    }

    @OnEvent(value = GameEditor.GAME_EDIT_CANCEL)
    Object onCancel()
    {
        editorVisible = false;
        return gameEditorZone.getBody();
    }

    @OnEvent(value = GameEditor.GAME_EDIT_SAVE)
    Object onSave()
    {
        editorVisible = false;
        return gameEditorZone.getBody();
    }

    @OnEvent(value = "edit")
    Object onEdit(Long gameId)
    {
        editorVisible = true;
        gameEditor.setGame(findGameById(gameId));
        return this;
    }

    @OnEvent(value = "delete")
    Object onDelete(Long gameId)
    {
        gameService.delete(gameService.findById(gameId));
        return gameGridZone;
    }

    @OnEvent(value = "downloadXLS")
    Object onDownloadXLS() throws IOException
    {
        return new GenericStreamResponse("application/vnd.ms-excel", ".xls",
            xlsGameExporter.getFile(gameService.findAfter(new Date())), "spiele");
    }

    @OnEvent(value = "downloadICal")
    Object onDownloadICal() throws IOException
    {
        return new GenericStreamResponse("text/calendar", ".ical",
            iCalGameExporter.getFile(gameService.findAfter(new Date())), "spiele");
    }

    @Inject
    private BeanModelSource beanModelSource;
    @Inject
    private ComponentResources componentResources;

    public BeanModel<GameDTO> getGameModel()
    {
        BeanModel<GameDTO> beanModel = beanModelSource.createDisplayModel(
            GameDTO.class, componentResources.getMessages());
        beanModel.add("gym", null).sortable(true);
        beanModel.add("teamA", null).sortable(true);
        beanModel.add("teamB", null).sortable(true);
        beanModel.add("league", null).sortable(true);
        if (pageLayout.isPermitted("editGame"))
        {
            beanModel.add("edit", null).sortable(false);
        }
        if (pageLayout.isPermitted("deleteGame"))
        {
            beanModel.add("delete", null).sortable(false);
        }
        return beanModel;
    }

    private GameDTO findGameById(Long gameId)
    {
        return gameService.findById(gameId);
    }

    public boolean isHome()
    {
        return gameService.isHome(game);
    }

    public boolean isAway()
    {
        return gameService.isAway(game);
    }
}
