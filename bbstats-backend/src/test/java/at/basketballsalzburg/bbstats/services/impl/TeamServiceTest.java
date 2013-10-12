package at.basketballsalzburg.bbstats.services.impl;

import org.testng.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import at.basketballsalzburg.bbstats.dto.TeamDTO;
import at.basketballsalzburg.bbstats.services.TeamService;

@ContextConfiguration("classpath:META-INF/db-test.xml")
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class TeamServiceTest extends
		AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private TeamService teamService;

	@Test
	public void addTeam() {
		int size = teamService.findAll().size();
		TeamDTO team = new TeamDTO();
		team.setName("BSC Salzburg");
		team.setShortName("BSC");
		teamService.save(team);
		Assert.assertEquals(size + 1, teamService.findAll().size());
	}
}
