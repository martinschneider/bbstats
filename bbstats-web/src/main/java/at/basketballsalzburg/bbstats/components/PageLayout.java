package at.basketballsalzburg.bbstats.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import at.basketballsalzburg.bbstats.commons.MenuItem;
import at.basketballsalzburg.bbstats.pages.AgeGroupMaintenance;
import at.basketballsalzburg.bbstats.pages.CoachMaintenance;
import at.basketballsalzburg.bbstats.pages.Results;
import at.basketballsalzburg.bbstats.pages.GymMaintenance;
import at.basketballsalzburg.bbstats.pages.LeagueMaintenance;
import at.basketballsalzburg.bbstats.pages.PlayerMaintenance;
import at.basketballsalzburg.bbstats.pages.PlayerStatistics;
import at.basketballsalzburg.bbstats.pages.PracticeStatistics;
import at.basketballsalzburg.bbstats.pages.Practices;
import at.basketballsalzburg.bbstats.pages.Schedule;
import at.basketballsalzburg.bbstats.pages.TeamMaintenance;

@Import(stylesheet = { "../styles/bootstrap.css", "../styles/bbstats.css",
		"../styles/jquery.vegas.css" })
public class PageLayout {

	@Component(parameters = { "homepageURL=literal:http://www.basketball-salzburg.at",
			"homepageName=literal:BSC Salzburg" })
	private Menu menu;

	@Inject
	private JavaScriptSupport javaScriptSupport;

	@Inject
	private Messages messages;

	@AfterRender
	void afterRender() {
		javaScriptSupport.importStack("bbstatsStack");
	}

	@SetupRender
	void buildMenu() {
		if (!menu.isInitialized()) {
			List<MenuItem> items = new ArrayList<MenuItem>();

			MenuItem games = new MenuItem(messages.get("games"), null, true);
			games.addItem(new MenuItem(messages.get("schedule"), Schedule.class
					.getSimpleName(), false));
			games.addItem(new MenuItem(messages.get("results"), Results.class
					.getSimpleName(), false));
			MenuItem practices = new MenuItem(messages.get("practices"),
					Practices.class.getSimpleName(), false);
			MenuItem statistics = new MenuItem(messages.get("statistics"),
					null, true);
			statistics.addItem(new MenuItem(messages.get("playerStatistics"),
					PlayerStatistics.class.getSimpleName(), false));
			statistics.addItem(new MenuItem(messages.get("practiceStatistics"),
					PracticeStatistics.class.getSimpleName(), false));
			MenuItem maintenance = new MenuItem(messages.get("maintenance"),
					null, true);
			maintenance.addItem(new MenuItem(messages.get("agegroups"),
					AgeGroupMaintenance.class.getSimpleName(), false));
			maintenance.addItem(new MenuItem(messages.get("coaches"),
					CoachMaintenance.class.getSimpleName(), false));
			maintenance.addItem(new MenuItem(messages.get("gyms"),
					GymMaintenance.class.getSimpleName(), false));
			maintenance.addItem(new MenuItem(messages.get("players"),
					PlayerMaintenance.class.getSimpleName(), false));
			maintenance.addItem(new MenuItem(messages.get("leagues"),
					LeagueMaintenance.class.getSimpleName(), false));
			maintenance.addItem(new MenuItem(messages.get("teams"),
					TeamMaintenance.class.getSimpleName(), false));

			items.add(games);
			if (SecurityUtils.getSubject().isAuthenticated()) {
				if (hasRole("coach")) {
					items.add(practices);
				}
				items.add(statistics);
				items.add(maintenance);
			}
			menu.setMenuItems(items);

		}
	}

	public boolean hasRole(final String role) {
		return SecurityUtils.getSubject().hasRole(role);
	}

	public boolean isPermitted(final String permission) {
		return SecurityUtils.getSubject().isPermitted(permission);
	}

	public boolean isAuthenticated() {
		return SecurityUtils.getSubject().isAuthenticated();
	}

}
