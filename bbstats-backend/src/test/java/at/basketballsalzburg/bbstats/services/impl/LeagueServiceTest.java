package at.basketballsalzburg.bbstats.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.Test;

import at.basketballsalzburg.bbstats.dto.LeagueDTO;
import at.basketballsalzburg.bbstats.services.LeagueService;

@ContextConfiguration("classpath:META-INF/db-test.xml")
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class LeagueServiceTest extends
    AbstractTransactionalTestNGSpringContextTests
{

    @Autowired
    private LeagueService leagueService;

    @Test
    public void addLeague()
    {
        int size = leagueService.findAll().size();
        LeagueDTO league = new LeagueDTO();
        league.setName("my private league");
        league.setShortName("league");
        leagueService.save(league);
        Assert.assertEquals(size + 1, leagueService.findAll().size());
    }
}
