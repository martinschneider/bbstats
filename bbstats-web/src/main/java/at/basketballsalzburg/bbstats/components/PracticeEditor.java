package at.basketballsalzburg.bbstats.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.LinkSubmit;
import org.apache.tapestry5.corelib.components.Palette;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;

import at.basketballsalzburg.bbstats.dto.PracticeDTO;
import at.basketballsalzburg.bbstats.entities.Coach;
import at.basketballsalzburg.bbstats.entities.Player;
import at.basketballsalzburg.bbstats.services.AgeGroupService;
import at.basketballsalzburg.bbstats.services.CoachService;
import at.basketballsalzburg.bbstats.services.GymService;
import at.basketballsalzburg.bbstats.services.PlayerService;
import at.basketballsalzburg.bbstats.services.PracticeService;
import at.basketballsalzburg.bbstats.utils.AgeGroupValueEncoder;
import at.basketballsalzburg.bbstats.utils.CoachValueEncoder;
import at.basketballsalzburg.bbstats.utils.GymSelectModel;
import at.basketballsalzburg.bbstats.utils.PlayerValueEncoder;

@Import(library = "PracticeEditor.js")
public class PracticeEditor {
	public static final String PRACTICE_EDIT_CANCEL = "practiceeditcancel";
	public static final String PRACTICE_EDIT_SAVE = "practiceeditsave";

	@Inject
	private PracticeService practiceService;

	@Inject
	private GymService gymService;

	@Inject
	private PlayerService playerService;

	@Inject
	private CoachService coachService;

	@Inject
	private AgeGroupService ageGroupService;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private SelectModelFactory selectModelFactory;

	@Inject
	@Property
	private CoachValueEncoder coachValueEncoder;

	@Inject
	@Property
	private PlayerValueEncoder playerValueEncoder;

	@Inject
	@Property
	private AgeGroupValueEncoder ageGroupValueEncoder;

	@Component
	private Form practiceEditForm;

	@Component(parameters = { "value=practice.dateTime",
			"datePattern=dd.MM.yyyy, HH:mm", "timePicker=true",
			"timePickerAdjacent=true", "use24hrs=true" })
	private DateTimeField date;

	@Component(parameters = { "value=practice.duration" })
	private TextField duration;

	@Component(parameters = { "value=practice.comment" })
	private TextField comment;

	@Component(parameters = { "value=gymId", "model=gymSelectModel" })
	private Select gymSelect;

	@Component(parameters = { "selected=practice.players",
			"model=playerSelectModel", "encoder=playerValueEncoder",
			"availableLabel=message:availablePlayers",
			"selectedLabel=message:selectedPlayers" })
	private Palette playerPalette;

	@Component(parameters = { "value=practice.guests" })
	private TextField guests;

	@Component(parameters = { "selected=practice.coaches",
			"model=coachSelectModel", "encoder=coachValueEncoder",
			"availableLabel=message:availableCoaches",
			"selectedLabel=message:selectedCoaches" })
	private Palette coachPalette;

	@Component(parameters = { "selected=practice.ageGroups",
			"model=ageGroupSelectModel", "encoder=ageGroupValueEncoder",
			"availableLabel=message:availableAgeGroups",
			"selectedLabel=message:selectedAgeGroups" })
	private Palette ageGroupPalette;

	@Property
	private Coach coach;

	@Property
	private Player player;

	@Property
	private SelectModel gymSelectModel;

	@Property
	private SelectModel playerSelectModel;

	@Property
	private SelectModel coachSelectModel;

	@Property
	private SelectModel ageGroupSelectModel;

	@Component
	private LinkSubmit submit;

	@Component(parameters = "event=cancel")
	private EventLink cancel;

	@Persist
	private PracticeDTO practice;

	@Property
	private Long gymId;

	public PracticeDTO getPractice() {
		return practice;
	}

	public void setPractice(PracticeDTO practice) {
		this.practice = practice;
	}

	void onSuccess() {
		practice.setGym(gymService.findById(gymId));
		practiceService.save(practice);
		componentResources.triggerEvent(PRACTICE_EDIT_SAVE, null, null);
	}

	@OnEvent(value = "cancel")
	void onEventFromCancel() {
		componentResources.triggerEvent(PRACTICE_EDIT_CANCEL, null, null);
	}

	@SetupRender
	void setupRender() {
		if (practice.getGym() != null && practice.getGym().getId() != null) {
			gymId = practice.getGym().getId();
		}
		gymSelectModel = new GymSelectModel(gymService.findAll());
		playerSelectModel = selectModelFactory.create(playerService.findAll(),
				"displayName");
		coachSelectModel = selectModelFactory.create(coachService.findAll(),
				"displayName");
		ageGroupSelectModel = selectModelFactory.create(
				ageGroupService.findAll(), "name");
	}
}
