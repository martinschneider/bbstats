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

import at.basketballsalzburg.bbstats.components.AgeGroupEditor;
import at.basketballsalzburg.bbstats.components.Box;
import at.basketballsalzburg.bbstats.components.PageLayout;
import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.mixins.Permission;
import at.basketballsalzburg.bbstats.security.Permissions;
import at.basketballsalzburg.bbstats.services.AgeGroupService;

@RequiresPermissions(Permissions.ageGroupMaintenancePage)
public class AgeGroupMaintenance {

	@Component
	private PageLayout pageLayout;

	@Component
	private AgeGroupEditor ageGroupEditor;

	@Component(parameters = { "source=ageGroupsList", "model=ageGroupModel",
			"empty=message:noData", "row=ageGroup", "rowsPerPage=20",
			"include=name", "add=edit,delete", "reorder=name,edit,delete",
			"inplace=true","class=table table-striped table-condensed" })
	private Grid ageGroupGrid;

	@Component
	private Zone ageGroupEditorZone;

	@Component
	private Zone ageGroupGridZone;

	@Component(parameters = "title=message:ageGroupEditorBoxTitle")
	private Box ageGroupEditorBox;

	@Component(parameters = { "title=message:ageGroupGridBoxTitle",
			"type=tablebox" })
	private Box ageGroupGridBox;

	@Component(parameters = { "event=edit", "context=ageGroup.id",
			"Permission.allowedPermissions=editAgeGroup" })
	@MixinClasses(Permission.class)
	private EventLink editAgeGroup;

	@Component(parameters = { "event=delete", "context=ageGroup.id",
			"Permission.allowedPermissions=deleteAgeGroup" })
	@MixinClasses(Permission.class)
	private EventLink deleteAgeGroup;

	@Component(parameters = { "event=new",
			"Permission.allowedPermissions=newAgeGroup" })
	@MixinClasses(Permission.class)
	private EventLink newAgeGroup;

	@Inject
	private AgeGroupService ageGroupService;

	@Property
	private List<AgeGroupDTO> ageGroupsList;

	@Property
	@Persist
	private AgeGroupDTO ageGroup;

	@Property
	@Persist
	private boolean editorVisible;

	@OnEvent(value = "edit")
	Object onEdit(Long ageGroupId) {
		ageGroupEditor.setAgeGroup(ageGroupService.findById(ageGroupId));
		editorVisible = true;
		return ageGroupEditorZone;
	}

	@OnEvent(value = "delete")
	Object onDelete(Long ageGroupId) {
		ageGroupService.delete(ageGroupService.findById(ageGroupId));
		return ageGroupGridZone;
	}

	@OnEvent(value = "new")
	Object onNew() {
		ageGroupEditor.setAgeGroup(new AgeGroupDTO());
		editorVisible = true;
		return ageGroupEditorZone;
	}

	@OnEvent(value = AgeGroupEditor.AGEGROUP_EDIT_CANCEL)
	void onCancel() {
		editorVisible = false;
	}

	@OnEvent(value = AgeGroupEditor.AGEGROUP_EDIT_SAVE)
	void onSave() {
		editorVisible = false;
	}

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	public BeanModel<AgeGroupDTO> getAgeGroupModel() {
		return beanModelSource.createDisplayModel(AgeGroupDTO.class,
				componentResources.getMessages());
	}

	@SetupRender
	void setup() {
		ageGroupsList = ageGroupService.findAll();
		if (ageGroupGrid.getSortModel().getSortConstraints().isEmpty()) {
			ageGroupGrid.getSortModel().updateSort("name");
		}
	}
}