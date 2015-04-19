package at.basketballsalzburg.bbstats.pages;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import at.basketballsalzburg.bbstats.components.PageLayout;
import at.basketballsalzburg.bbstats.components.TeamEditor;
import at.basketballsalzburg.bbstats.dto.TeamDTO;
import at.basketballsalzburg.bbstats.mixins.Permission;
import at.basketballsalzburg.bbstats.security.Permissions;
import at.basketballsalzburg.bbstats.services.TeamService;

@RequiresPermissions(Permissions.teamMaintenancePage)
public class TeamMaintenance
{
    @Component
    private PageLayout pageLayout;

    @Component
    private TeamEditor teamEditor;

    @Component(parameters = {"source=teamList", "model=teamModel",
        "empty=message:noData", "row=team", "rowsPerPage=9999",
        "include=name", "add=actions",
        "reorder=name,actions", "inplace=true",
        "class=table table-striped table-condensed"})
    private Grid teamGrid;

    @Component
    private Zone teamEditorZone;

    @Component
    private Zone teamGridZone;

    @Component(parameters = "title=message:teamEditorBoxTitle")
    private Box teamEditorBox;

    @Component(parameters = {"title=message:teamGridBoxTitle", "type=tablebox"})
    private Box teamGridBox;

    @Component(parameters = {"event=edit", "context=team.id",
        "Permission.allowedPermissions=editTeam"})
    @MixinClasses(Permission.class)
    private EventLink editTeam;

    @Component(parameters = {"event=delete", "context=team.id",
        "Permission.allowedPermissions=deleteTeam"})
    @MixinClasses(Permission.class)
    private EventLink deleteTeam;

    @Component(parameters = {"event=new",
        "Permission.allowedPermissions=newTeam"})
    @MixinClasses(Permission.class)
    private EventLink newTeam;

    @Inject
    private TeamService teamService;

    @Property
    private List<TeamDTO> teamList;

    @Property
    @Persist
    private TeamDTO team;

    @Property
    @Persist
    private boolean editorVisible;

    @OnEvent(value = "edit")
    Object onEdit(Long teamId)
    {
        teamEditor.setTeam(teamService.findById(teamId));
        editorVisible = true;
        return teamEditorZone;
    }

    @OnEvent(value = "delete")
    Object onDelete(Long teamId)
    {
        teamService.delete(teamService.findById(teamId));
        return teamGridZone;
    }

    @OnEvent(value = "new")
    Object onNew()
    {
        teamEditor.setTeam(new TeamDTO());
        editorVisible = true;
        return teamEditorZone;
    }

    @OnEvent(value = TeamEditor.TEAM_EDIT_CANCEL)
    void onCancel()
    {
        editorVisible = false;
    }

    @OnEvent(value = TeamEditor.TEAM_EDIT_SAVE)
    void onSave()
    {
        editorVisible = false;
    }

    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private ComponentResources componentResources;

    public BeanModel<TeamDTO> getTeamModel()
    {
        return beanModelSource.createDisplayModel(TeamDTO.class,
            componentResources.getMessages());
    }

    @SetupRender
    void setup()
    {
        teamList = teamService.findAll();
        if (teamGrid.getSortModel().getSortConstraints().isEmpty())
        {
            teamGrid.getSortModel().updateSort("name");
        }
    }
}
