package at.basketballsalzburg.bbstats.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.LinkSubmit;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;

import at.basketballsalzburg.bbstats.dto.TeamDTO;
import at.basketballsalzburg.bbstats.services.TeamService;

public class TeamEditor
{
    public static final String TEAM_EDIT_CANCEL = "teameditcancel";
    public static final String TEAM_EDIT_SAVE = "teameditsave";

    @Inject
    private TeamService teamService;

    @Inject
    private ComponentResources componentResources;

    @Component
    private Form teamEditForm;

    @Component(parameters = {"value=team.name"})
    private TextField name;

    @Component
    private LinkSubmit submit;

    @Component(parameters = "event=cancel")
    private EventLink cancel;

    @Persist
    private TeamDTO team;

    void onSuccess()
    {
        teamService.save(team);
        componentResources.triggerEvent(TEAM_EDIT_SAVE, null, null);
    }

    public TeamDTO getTeam()
    {
        return team;
    }

    public void setTeam(TeamDTO team)
    {
        this.team = team;
    }

    @OnEvent(value = "cancel")
    void onEventFromCancel()
    {
        componentResources.triggerEvent(TEAM_EDIT_CANCEL, null, null);
    }

}
