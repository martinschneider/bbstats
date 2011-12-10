package at.basketballsalzburg.bbstats.pages;

import java.io.IOException;
import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
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
import at.basketballsalzburg.bbstats.components.PlayerEditor;
import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.export.ExcelPlayerExporter;
import at.basketballsalzburg.bbstats.services.PlayerService;
import at.basketballsalzburg.bbstats.utils.XLSStreamResponse;

public class PlayerMaintenance {

	@Component
	private PageLayout pageLayout;

	@Component
	private PlayerEditor playerEditor;

	@Component(parameters = {
			"source=playerList",
			"model=playerModel",
			"empty=message:noData",
			"row=player",
			"rowsPerPage=9999",
			"include=firstName,lastName,adress,postalCode,city,country,phone,email,birthday,nationality",
			"add=edit,delete",
			"reorder=lastName,firstName,adress,postalCode,city,country,phone,email,birthday,nationality,edit,delete" })
	private Grid playersGrid;

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

	@Component
	private Zone playerEditorZone;

	@Component
	private Zone playerGridZone;

	@Component(parameters = "title=message:playerEditorBoxTitle")
	private Box playerEditorBox;

	@Component(parameters = "title=message:playerGridBoxTitle")
	private Box playerGridBox;

	@Component(parameters = { "event=edit", "context=player.id" })
	private EventLink editPlayer;

	@Component(parameters = { "event=delete", "context=player.id" })
	private EventLink deletePlayer;

	@Component(parameters = { "event=new" })
	private EventLink newPlayer;

	@Component(parameters = { "event=downloadXLS" })
	private EventLink downloadXLS;

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
		return new XLSStreamResponse(playerExporter.getFile(playerList),
				"spielerliste");
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
		playerList = playerService.findAll();
		if (playersGrid.getSortModel().getSortConstraints().isEmpty()) {
			playersGrid.getSortModel().updateSort("lastname");
		}
	}

}