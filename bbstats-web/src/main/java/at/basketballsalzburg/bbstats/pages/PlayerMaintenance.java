package at.basketballsalzburg.bbstats.pages;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.MixinClasses;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.Loop;
import org.apache.tapestry5.corelib.components.PageLink;
import org.apache.tapestry5.corelib.components.Palette;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.SelectModelFactory;

import at.basketballsalzburg.bbstats.components.Box;
import at.basketballsalzburg.bbstats.components.PageLayout;
import at.basketballsalzburg.bbstats.components.PlayerEditor;
import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.export.ExcelPlayerExporter;
import at.basketballsalzburg.bbstats.mixins.PaletteObserve;
import at.basketballsalzburg.bbstats.mixins.Permission;
import at.basketballsalzburg.bbstats.security.Permissions;
import at.basketballsalzburg.bbstats.services.AgeGroupService;
import at.basketballsalzburg.bbstats.services.PlayerService;
import at.basketballsalzburg.bbstats.utils.AgeGroupValueEncoder;
import at.basketballsalzburg.bbstats.utils.GenericStreamResponse;

@RequiresPermissions(Permissions.playerMaintenancePage)
public class PlayerMaintenance
{

    private List<String> otherAgeGroups;

    @Component
    private PageLayout pageLayout;

    @Inject
    @Property
    private Request request;

    @Component
    private PlayerEditor playerEditor;

    @Component(parameters = {
        "source=players",
        "model=playerModel",
        "empty=message:noData",
        "row=player",
        "rowsPerPage=20",
        "include=displayName,adress,postalCode,city,country,phone,email,birthday,nationality",
        "add=agegroups,edit,delete",
        "reorder=displayName,adress,postalCode,city,country,phone,email,birthday,nationality,agegroups,edit,delete",
        "inplace=true", "class=table table-striped table-condensed"})
    private Grid playersGrid;

    @Component(parameters = {"title=message:filterBoxTitle"})
    private Box filterBox;

    @Component(parameters = {"selected=selectedAgeGroups",
        "model=ageGroupSelectModel", "encoder=ageGroupValueEncoder",
        "availableLabel=message:availableAgeGroups",
        "selectedLabel=message:selectedAgeGroups",
        "paletteObserve.event=literal:filter",
        "paletteObserve.zone=playerGridZone"})
    @MixinClasses(PaletteObserve.class)
    private Palette agegroupPalette;

    @Component
    private Form form;

    @Component(parameters = {"source=player.agegroups", "value=agegroup"})
    private Loop<AgeGroupDTO> ageGroupLoop;

    @Inject
    private SelectModelFactory selectModelFactory;

    @Inject
    private AgeGroupService ageGroupService;

    @Inject
    private PlayerService playerService;

    @Inject
    private ExcelPlayerExporter playerExporter;

    @Inject
    private Messages messages;

    @Persist
    @Property
    private Set<PlayerDTO> players;

    @Property
    @Persist
    private PlayerDTO player;

    @Property
    @Persist
    private AgeGroupDTO agegroup;

    @Component
    private Zone playerEditorZone;

    @Component
    private Zone playerGridZone;

    @Component(parameters = "title=message:playerEditorBoxTitle")
    private Box playerEditorBox;

    @Component(parameters = {"title=message:playerGridBoxTitle",
        "type=tablebox"})
    private Box playerGridBox;

    @Component(parameters = {"event=edit", "context=player.id",
        "Permission.allowedPermissions=editPlayer"})
    @MixinClasses(Permission.class)
    private EventLink editPlayer;

    @Component(parameters = {"event=delete", "context=player.id",
        "Permission.allowedPermissions=deletePlayer"})
    @MixinClasses(Permission.class)
    private EventLink deletePlayer;

    @Component(parameters = {"event=new",
        "Permission.allowedPermissions=newPlayer"})
    @MixinClasses(Permission.class)
    private EventLink newPlayer;

    @Component(parameters = {"event=downloadXLS",
        "Permission.allowedPermissions=downloadPlayerList"})
    @MixinClasses(Permission.class)
    private EventLink downloadXLS;

    @Component(parameters = {"page=player"})
    private PageLink playerDetail;

    @Property
    private SelectModel ageGroupSelectModel;

    @Property
    @Persist
    private List<AgeGroupDTO> selectedAgeGroups;

    @Property
    @Persist
    private boolean filterPlayers;

    @Property
    @Persist
    private boolean editorVisible;

    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private ComponentResources componentResources;

    @Inject
    @Property
    private AgeGroupValueEncoder ageGroupValueEncoder;

    @OnEvent(value = "edit")
    Object onEdit(Long playerId)
    {
        editorVisible = true;
        playerEditor.setPlayer(playerService.findById(playerId));
        return playerEditorZone;
    }

    @OnEvent(value = "delete")
    Object onDelete(Long playerId)
    {
        playerService.delete(playerService.findById(playerId));
        return playerGridZone;
    }

    @OnEvent(value = "new")
    Object onNew()
    {
        playerEditor.setPlayer(new PlayerDTO());
        editorVisible = true;
        return playerEditorZone;
    }

    @OnEvent(value = PlayerEditor.PLAYER_EDIT_CANCEL)
    void onCancel()
    {
        editorVisible = false;
    }

    @OnEvent(value = PlayerEditor.PLAYER_EDIT_SAVE)
    void onSave()
    {
        editorVisible = false;
    }

    @OnEvent(value = "filter")
    Object onFilter(String ageGroups)
    {
        players.clear();
        selectedAgeGroups.clear();
        if (!ageGroups.isEmpty())
        {
            for (String ageGroupId : ageGroups.split(","))
            {
                if (ageGroupId.equals("-1"))
                {
                    players.addAll(playerService.findAllWithoutAgeGroup());
                    selectedAgeGroups.add(getOthers());
                }
                else
                {
                    AgeGroupDTO ageGroup = ageGroupService.findById(Long
                        .parseLong(ageGroupId));
                    players.addAll(playerService.findAllForAgegroup(ageGroup));
                    selectedAgeGroups.add(ageGroup);
                }
            }
        }
        return playerGridZone;
    }

    @OnEvent(value = "downloadXLS")
    Object onDownloadXLS() throws IOException
    {
        return new GenericStreamResponse("application/vnd.ms-excel", ".xls",
            playerExporter.getFile(players), "players");
    }

    public BeanModel<PlayerDTO> getPlayerModel()
    {
        return beanModelSource.createDisplayModel(PlayerDTO.class,
            componentResources.getMessages());
    }

    @SetupRender
    void setup()
    {
        if (otherAgeGroups == null)
        {
            otherAgeGroups = Arrays.asList("no team", "kein Team",
                messages.get("noAgeGroups"));
        }
        if (selectedAgeGroups == null)
        {
            selectedAgeGroups = ageGroupService.findAll();
            selectedAgeGroups.add(getOthers());
            players = playerService.findAll();
        }
        else
        {
            players.clear();
            for (AgeGroupDTO ageGroup : selectedAgeGroups)
            {
                if (ageGroup.equals(getOthers()))
                {
                    players.addAll(playerService.findAllWithoutAgeGroup());
                }
                else
                {
                    players.addAll(playerService.findAllForAgegroup(ageGroup));
                }
            }

        }
        if (playersGrid.getSortModel().getSortConstraints().isEmpty())
        {
            playersGrid.getSortModel().updateSort("displayName");
        }
        if (ageGroupSelectModel == null)
        {
            List<AgeGroupDTO> ageGroups = ageGroupService.findAll();
            ageGroups.add(getOthers());
            ageGroupSelectModel = selectModelFactory.create(ageGroups, "name");
        }
    }

    @Cached
    public AgeGroupDTO getOthers()
    {
        AgeGroupDTO others = new AgeGroupDTO();
        others.setName(messages.get("noAgeGroups"));
        others.setId(-1L);
        return others;
    }

}