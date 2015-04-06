package at.basketballsalzburg.bbstats.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.LinkSubmit;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.dto.CoachDTO;
import at.basketballsalzburg.bbstats.dto.GymDTO;
import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.dto.PracticeDTO;
import at.basketballsalzburg.bbstats.entities.Coach;
import at.basketballsalzburg.bbstats.entities.Player;
import at.basketballsalzburg.bbstats.select2.ChoiceProvider;
import at.basketballsalzburg.bbstats.select2.Settings;
import at.basketballsalzburg.bbstats.select2.provider.AgeGroupProvider;
import at.basketballsalzburg.bbstats.select2.provider.CoachProvider;
import at.basketballsalzburg.bbstats.select2.provider.GymProvider;
import at.basketballsalzburg.bbstats.select2.provider.PlayerProvider;
import at.basketballsalzburg.bbstats.services.AgeGroupService;
import at.basketballsalzburg.bbstats.services.CoachService;
import at.basketballsalzburg.bbstats.services.GymService;
import at.basketballsalzburg.bbstats.services.PlayerService;
import at.basketballsalzburg.bbstats.services.PracticeService;

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
	private JavaScriptSupport javaScriptSupport;

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

	@Component(parameters = { "settings=settings", "type=literal:hidden",
			"provider=gymProvider", "singleValue=gym" })
	private Select2 gymSelect;

	@Component(parameters = { "settings=settings", "type=literal:hidden",
			"provider=playerProvider", "multiValue=practice.players" })
	private MultiSelect2 playerSelect;

	@Component(parameters = { "value=practice.guests" })
	private TextField guests;

	@Component(parameters = { "settings=settings", "type=literal:hidden",
			"provider=coachProvider", "multiValue=practice.coaches" })
	private MultiSelect2 coachSelect;

	@Component(parameters = { "settings=settings", "type=literal:hidden",
			"provider=ageGroupProvider", "multiValue=practice.ageGroups" })
	private MultiSelect2 ageGroupSelect;

	@Property
	private Coach coach;

	@Property
	private Player player;

	@Property
	private Settings settings;

	@Component
	private LinkSubmit submit;

	@Component(parameters = "event=cancel")
	private EventLink cancel;

	@Persist
	private PracticeDTO practice;

	@Property
	@Persist
	private GymDTO gym;

	public PracticeDTO getPractice() {
		return practice;
	}

	public void setPractice(PracticeDTO practice) {
		this.practice = practice;
	}

	@Cached
	public ChoiceProvider<GymDTO> getGymProvider() {
		return new GymProvider(gymService);
	}
	
	@Cached
	public ChoiceProvider<CoachDTO> getCoachProvider() {
		return new CoachProvider(coachService);
	}
	
	@Cached
	public ChoiceProvider<PlayerDTO> getPlayerProvider() {
		return new PlayerProvider(playerService);
	}
	
	@Cached
	public ChoiceProvider<AgeGroupDTO> getAgeGroupProvider() {
		return new AgeGroupProvider(ageGroupService);
	}

	void onSuccess() {
		practice.setGym(gym);
		practiceService.save(practice);
		componentResources.triggerEvent(PRACTICE_EDIT_SAVE, null, null);
	}

	@OnEvent(value = "cancel")
	void onEventFromCancel() {
		componentResources.triggerEvent(PRACTICE_EDIT_CANCEL, null, null);
	}

	@SetupRender
	void setupRender() {
		if (practice != null) {
			gym = practice.getGym();
		}
		settings = new Settings();
		settings.setWidth("100%");
	}
}
