package at.basketballsalzburg.bbstats.services.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.Test;

import at.basketballsalzburg.bbstats.dto.GymDTO;
import at.basketballsalzburg.bbstats.dto.PracticeDTO;
import at.basketballsalzburg.bbstats.services.GymService;
import at.basketballsalzburg.bbstats.services.PracticeService;

@ContextConfiguration("classpath:META-INF/db-test.xml")
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class PracticeServiceTest extends
    AbstractTransactionalTestNGSpringContextTests
{

    @Autowired
    private PracticeService practiceService;

    @Autowired
    private GymService gymService;

    @Test
    public void addPractice()
    {
        int size = practiceService.findAll().size();
        PracticeDTO practice = new PracticeDTO();
        practice.setDateTime(new Date());
        gymService.save(new GymDTO());
        GymDTO gym = gymService.findAll().get(0);
        practice.setGym(gym);
        practiceService.save(practice);
        Assert.assertEquals(size + 1, practiceService.findAll().size());
    }
}
