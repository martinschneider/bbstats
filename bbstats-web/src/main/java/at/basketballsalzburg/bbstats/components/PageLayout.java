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

import at.basketballsalzburg.bbstats.commons.MenuHelper;
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

@Import(stylesheet = { "../styles/bootstrap.min.css", "../styles/bbstats.css",
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

			// GAMES
			MenuItem games = new MenuItem(messages.get("games"), null, true);
			games = MenuHelper.addItem(games, messages.get("schedule"), Schedule.class);
			games = MenuHelper.addItem(games, messages.get("results"), Results.class);
			if (!games.getItems().isEmpty())
			{
				items.add(games);
			}
			
			// PRACTICES
			items = MenuHelper.addItem(items, messages.get("practices"),
					Practices.class);
			
			// STATISTICS
			MenuItem statistics = new MenuItem(messages.get("statistics"),
					null, true);
			statistics = MenuHelper.addItem(statistics, messages.get("playerStatistics"),
					PlayerStatistics.class);
			statistics = MenuHelper.addItem(statistics,messages.get("practiceStatistics"),
					PracticeStatistics.class);
			if (!statistics.getItems().isEmpty())
			{
				items.add(statistics);
			}
			
			// MAINTENANCE
			MenuItem maintenance = new MenuItem(messages.get("maintenance"),
					null, true);
			maintenance = MenuHelper.addItem(maintenance, messages.get("agegroups"),
					AgeGroupMaintenance.class);
			maintenance = MenuHelper.addItem(maintenance, messages.get("coaches"),
					CoachMaintenance.class);
			maintenance = MenuHelper.addItem(maintenance, messages.get("gyms"),
					GymMaintenance.class);
			maintenance = MenuHelper.addItem(maintenance, messages.get("players"),
					PlayerMaintenance.class);
			maintenance = MenuHelper.addItem(maintenance, messages.get("leagues"),
					LeagueMaintenance.class);
			maintenance = MenuHelper.addItem(maintenance, messages.get("teams"),
					TeamMaintenance.class);
			if (!maintenance.getItems().isEmpty())
			{
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
