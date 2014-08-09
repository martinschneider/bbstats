package at.basketballsalzburg.bbstats.pages;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.LinkSubmit;
import org.apache.tapestry5.corelib.components.PageLink;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.joda.time.DateMidnight;

import at.basketballsalzburg.bbstats.components.Box;
import at.basketballsalzburg.bbstats.components.DateTimeField;
import at.basketballsalzburg.bbstats.components.PageLayout;
import at.basketballsalzburg.bbstats.dto.statistics.CompletePlayerStatisticDTO;
import at.basketballsalzburg.bbstats.security.Permissions;
import at.basketballsalzburg.bbstats.services.PlayerService;

@RequiresPermissions(Permissions.gameStatisticsPage)
public class GameStatistics {
	@Component
	private PageLayout pageLayout;

	@Component(parameters = { "value=fromDate", "datePattern=dd.MM.yyyy" })
	private DateTimeField fromDateField;

	@Component(parameters = { "value=toDate", "datePattern=dd.MM.yyyy" })
	private DateTimeField toDateField;

	@Component
	private LinkSubmit submit;

	@Component
	private Form form;

	@Component(parameters = { "title=message:datesBoxTitle" })
	private Box datesBox;

	@Component(parameters = { "title=message:playersBoxTitle", "type=tablebox" })
	private Box playersBox;

	@Component
	private Zone resultsZone;
	
	@Component(parameters = { "page=player" })
	private PageLink playerDetail;

	@Component(parameters = {
			"source=playerStatisticList",
			"empty=message:noData",
			"row=playerStatistic",
			"rowsPerPage=10",
			"include=games,fouls,fpg,fta,ftm,ftPercentage,ftapg,ftmpg,threes,threespg,points,ppg",
			"add=name",
			"reorder=name,games,points,ppg,fta,ftm,ftapg,ftmpg,ftPercentage,fouls,fpg",
			"inplace=true", 
			"class=table table-striped table-condensed" })
	private Grid playerStatisticsGrid;

	@Property
	private CompletePlayerStatisticDTO playerStatistic;

	@Property
	@Persist
	private DateMidnight fromDate;

	@Property
	@Persist
	private DateMidnight toDate;

	@Inject
	private PlayerService playerService;

	@SetupRender
	void onSetup() {
		if (toDate == null) {
			toDate = new DateMidnight();
		}
		if (fromDate == null) {
			fromDate = toDate.minusMonths(3);
		}
	}

	Object onSuccess() {
		return resultsZone;
	}

	public List<CompletePlayerStatisticDTO> getPlayerStatisticList() {
		return playerService.getCompleteStatistics(fromDate.toDate(),
				toDate.toDate());
	}
}
