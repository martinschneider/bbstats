package at.basketballsalzburg.bbstats.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.Test;

import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.services.PlayerService;

@ContextConfiguration("classpath:META-INF/db-test.xml")
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class PlayerServiceTest extends
    AbstractTransactionalTestNGSpringContextTests
{

    @Autowired
    private PlayerService playerService;

    @Test
    public void addPlayer()
    {
        int size = playerService.findAll().size();
        PlayerDTO player = new PlayerDTO();
        player.setFirstName("Martin");
        player.setLastName("Schneider");
        playerService.save(player);
        Assert.assertEquals(size + 1, playerService.findAll().size());
    }
}
