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
import at.basketballsalzburg.bbstats.components.LeagueEditor;
import at.basketballsalzburg.bbstats.components.PageLayout;
import at.basketballsalzburg.bbstats.dto.LeagueDTO;
import at.basketballsalzburg.bbstats.mixins.Permission;
import at.basketballsalzburg.bbstats.security.Permissions;
import at.basketballsalzburg.bbstats.services.LeagueService;

@RequiresPermissions(Permissions.leagueMaintenancePage)
public class LeagueMaintenance {
	@Component
	private PageLayout pageLayout;

	@Component
	private LeagueEditor leagueEditor;

	@Component(parameters = { "source=leagueList", "model=leagueModel",
			"empty=message:noData", "row=league", "rowsPerPage=9999",
			"include=shortname,name", "add=edit,delete",
			"reorder=shortName,name,edit,delete", "inplace=true" })
	private Grid leagueGrid;

	@Component(parameters = { "update=show" })
	private Zone leagueEditorZone;

	@Component(parameters = { "update=show" })
	private Zone leagueGridZone;

	@Component(parameters = "title=message:leagueEditorBoxTitle")
	private Box leagueEditorBox;

	@Component(parameters = {"title=message:leagueGridBoxTitle", "type=tablebox"})
	private Box leagueGridBox;

	@Component(parameters = { "event=edit", "context=league.id", "Permission.allowedPermissions=editLeague" })
	@MixinClasses(Permission.class)
	private EventLink editLeague;

	@Component(parameters = { "event=delete", "context=league.id", "Permission.allowedPermissions=deleteLeague" })
	@MixinClasses(Permission.class)
	private EventLink deleteLeague;

	@Component(parameters = { "event=new", "Permission.allowedPermissions=newLeague"})
	@MixinClasses(Permission.class)
	private EventLink newLeague;

	@Inject
	private LeagueService leagueService;

	@Property
	private List<LeagueDTO> leagueList;

	@Property
	@Persist
	private LeagueDTO league;

	@Property
	@Persist
	private boolean editorVisible;

	@OnEvent(value = "edit")
	Object onEdit(Long leagueId) {
		leagueEditor.setLeague(leagueService.findById(leagueId));
		editorVisible = true;
		return leagueEditorZone;
	}

	@OnEvent(value = "delete")
	Object onDelete(Long leagueId) {
		leagueService.delete(leagueService.findById(leagueId));
		return leagueGridZone;
	}

	@OnEvent(value = "new")
	Object onNew() {
		leagueEditor.setLeague(new LeagueDTO());
		editorVisible = true;
		return leagueEditorZone;
	}

	@OnEvent(value = LeagueEditor.LEAGUE_EDIT_CANCEL)
	void onCancel() {
		editorVisible = false;
	}

	@OnEvent(value = LeagueEditor.LEAGUE_EDIT_SAVE)
	void onSave() {
		editorVisible = false;
	}

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	public BeanModel<LeagueDTO> getLeagueModel() {
		return beanModelSource.createDisplayModel(LeagueDTO.class,
				componentResources.getMessages());
	}

	@SetupRender
	void setup() {
		leagueList = leagueService.findAll();
		if (leagueGrid.getSortModel().getSortConstraints().isEmpty()) {
			leagueGrid.getSortModel().updateSort("name");
		}
	}
}
