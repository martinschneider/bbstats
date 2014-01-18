package at.basketballsalzburg.bbstats.pages;

import java.io.IOException;
import java.util.Date;
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
import org.apache.tapestry5.corelib.components.Checkbox;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.LinkSubmit;
import org.apache.tapestry5.corelib.components.PageLink;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Request;
import org.joda.time.DateMidnight;

import at.basketballsalzburg.bbstats.components.Box;
import at.basketballsalzburg.bbstats.components.DateTimeField;
import at.basketballsalzburg.bbstats.components.PageLayout;
import at.basketballsalzburg.bbstats.components.PlayerEditor;
import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.export.ExcelPlayerExporter;
import at.basketballsalzburg.bbstats.mixins.Permission;
import at.basketballsalzburg.bbstats.security.Permissions;
import at.basketballsalzburg.bbstats.services.PlayerService;
import at.basketballsalzburg.bbstats.utils.GenericStreamResponse;

@RequiresPermissions(Permissions.playerMaintenancePage)
public class PlayerMaintenance {

	@Component
	private PageLayout pageLayout;

	@Inject
	@Property
	private Request request;

	@Component
	private PlayerEditor playerEditor;

	@Component(parameters = {
			"source=playerList",
			"model=playerModel",
			"empty=message:noData",
			"row=player",
			"rowsPerPage=20",
			"include=displayName,adress,postalCode,city,country,phone,email,birthday,nationality",
			"add=edit,delete",
			"reorder=displayName,adress,postalCode,city,country,phone,email,birthday,nationality,edit,delete",
			"inplace=true" })
	private Grid playersGrid;

	@Component(parameters = { "value=filterInactive" })
	private Checkbox filterInActive;

	@Component(parameters = { "value=activeSinceDate", "datePattern=dd.MM.yyyy" })
	private DateTimeField activeSinceField;

	@Component(parameters = { "title=message:filterBoxTitle" })
	private Box filterBox;

	@Property
	@Persist
	private Date activeSinceDate;

	@Component
	private Form form;

	@Component
	private LinkSubmit submit;

	@Property
	@Persist
	private boolean filterInactive;

	@Inject
	private PlayerService playerService;

	@Inject
	private ExcelPlayerExporter playerExporter;

	@Persist
	@Property
	private List<PlayerDTO> playerList;

	@Property
	@Persist
	private PlayerDTO player;

	@Component(parameters = { "update=show" })
	private Zone playerEditorZone;

	@Component(parameters = { "update=show" })
	private Zone playerGridZone;

	@Component(parameters = "title=message:playerEditorBoxTitle")
	private Box playerEditorBox;

	@Component(parameters = { "title=message:playerGridBoxTitle",
			"type=tablebox" })
	private Box playerGridBox;

	@Component(parameters = { "event=edit", "context=player.id",
			"Permission.allowedPermissions=editPlayer" })
	@MixinClasses(Permission.class)
	private EventLink editPlayer;

	@Component(parameters = { "event=delete", "context=player.id",
			"Permission.allowedPermissions=deletePlayer" })
	@MixinClasses(Permission.class)
	private EventLink deletePlayer;

	@Component(parameters = { "event=new",
			"Permission.allowedPermissions=newPlayer" })
	@MixinClasses(Permission.class)
	private EventLink newPlayer;

	@Component(parameters = { "event=downloadXLS",
			"Permission.allowedPermissions=DOWNLOAD_PLAYERLIST" })
	@MixinClasses(Permission.class)
	private EventLink downloadXLS;

	@Component(parameters = { "page=player" })
	private PageLink playerDetail;

	@Property
	@Persist
	private boolean editorVisible;

	@OnEvent(value = "edit")
	Object onEdit(Long playerId) {
		editorVisible = true;
		playerEditor.setPlayer(playerService.findById(playerId));
		return playerEditorZone;
	}

	@OnEvent(value = "delete")
	Object onDelete(Long playerId) {
		playerService.delete(playerService.findById(playerId));
		return playerGridZone;
	}

	@OnEvent(value = "new")
	Object onNew() {
		playerEditor.setPlayer(new PlayerDTO());
		editorVisible = true;
		return playerEditorZone;
	}

	@OnEvent(value = PlayerEditor.PLAYER_EDIT_CANCEL)
	void onCancel() {
		editorVisible = false;
	}

	@OnEvent(value = PlayerEditor.PLAYER_EDIT_SAVE)
	void onSave() {
		editorVisible = false;
	}

	@OnEvent(value = "downloadXLS")
	Object onDownloadXLS() throws IOException {
		return new GenericStreamResponse("application/vnd.ms-excel", ".xls",
				playerExporter.getFile(playerList), "players");
	}

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	public BeanModel<PlayerDTO> getPlayerModel() {
		return beanModelSource.createDisplayModel(PlayerDTO.class,
				componentResources.getMessages());
	}

	@SetupRender
	void setup() {
		if (activeSinceDate == null) {
			activeSinceDate = new DateMidnight().minusWeeks(2).toDate();
		}
		if (filterInactive) {
			playerList = playerService.findAllActiveSince(activeSinceDate);
		} else {
			playerList = playerService.findAll();
		}
		if (playersGrid.getSortModel().getSortConstraints().isEmpty()) {
			playersGrid.getSortModel().updateSort("displayName");
		}
	}

}