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
import at.basketballsalzburg.bbstats.components.GymEditor;
import at.basketballsalzburg.bbstats.components.PageLayout;
import at.basketballsalzburg.bbstats.dto.GymDTO;
import at.basketballsalzburg.bbstats.mixins.Permission;
import at.basketballsalzburg.bbstats.security.Permissions;
import at.basketballsalzburg.bbstats.services.GymService;

@RequiresPermissions(Permissions.gymMaintenancePage)
public class GymMaintenance {
	@Component
	private PageLayout pageLayout;

	@Component
	private GymEditor gymEditor;

	@Component(parameters = {
			"source=gymsList",
			"model=gymModel",
			"empty=message:noData",
			"row=gym",
			"rowsPerPage=20",
			"include=name,adress,postalCode,city,country,shortName",
			"add=edit,delete,route",
			"reorder=name,adress,postalCode,city,country,shortName,route,edit,delete",
			"inplace=true" })
	private Grid gymsGrid;

	@Component
	private Zone gymEditorZone;

	@Component
	private Zone gymGridZone;

	@Component(parameters = "title=message:gymEditorBoxTitle")
	private Box gymEditorBox;

	@Component(parameters = {"title=message:gymGridBoxTitle", "type=tablebox"})
	private Box gymGridBox;

	@Component(parameters = { "event=edit", "context=gym.id",
			"Permission.allowedPermissions=editGym" })
	@MixinClasses(Permission.class)
	private EventLink editGym;

	@Component(parameters = { "event=delete", "context=gym.id",
			"Permission.allowedPermissions=deleteGym" })
	@MixinClasses(Permission.class)
	private EventLink deleteGym;

	@Component(parameters = { "event=new", "Permission.allowedPermissions=newGym" })
	@MixinClasses(Permission.class)
	private EventLink newGym;

	@Inject
	private GymService gymService;

	@Property
	private List<GymDTO> gymsList;

	@Property
	@Persist
	private GymDTO gym;

	@Property
	@Persist
	private boolean editorVisible;

	@OnEvent(value = "edit")
	Object onEdit(Long gymId) {
		gymEditor.setGym(gymService.findById(gymId));
		editorVisible = true;
		return gymEditorZone;
	}

	@OnEvent(value = "delete")
	Object onDelete(Long gymId) {
		gymService.delete(gymService.findById(gymId));
		return gymGridZone;
	}

	@OnEvent(value = "new")
	Object onNew() {
		gymEditor.setGym(new GymDTO());
		editorVisible = true;
		return gymEditorZone;
	}

	@OnEvent(value = GymEditor.GYM_EDIT_CANCEL)
	void onCancel() {
		editorVisible = false;
	}

	@OnEvent(value = GymEditor.GYM_EDIT_SAVE)
	void onSave() {
		editorVisible = false;
	}

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	public BeanModel<GymDTO> getGymModel() {
		return beanModelSource.createDisplayModel(GymDTO.class,
				componentResources.getMessages());
	}

	@SetupRender
	void setup() {
		gymsList = gymService.findAll();
		if (gymsGrid.getSortModel().getSortConstraints().isEmpty()) {
			gymsGrid.getSortModel().updateSort("city");
		}
	}

	public String getGoogleMapsLink() {
		if (gym.getCity() != null) {

			StringBuilder link = new StringBuilder(
					"http://maps.google.de/maps?daddr=");
			link.append(gym.getName().replaceAll(" ", "+").trim());
			if (gym.getAdress() != null && !gym.getAdress().isEmpty()) {
				link.append("+");
				link.append(gym.getAdress().replaceAll(" ", "+").trim());
			}
			if (gym.getPostalCode() != null && !gym.getPostalCode().isEmpty()) {
				link.append("+");
				link.append(gym.getPostalCode().trim());
			}
			link.append("+");
			link.append(gym.getCity().replaceAll(" ", "+").trim());
			if (gym.getCountry() != null && !gym.getCountry().isEmpty()) {
				link.append("+");
				link.append(gym.getCountry().replaceAll(" ", "+").trim());
			}
			return link.toString();
		}
		return "#";
	}
}
