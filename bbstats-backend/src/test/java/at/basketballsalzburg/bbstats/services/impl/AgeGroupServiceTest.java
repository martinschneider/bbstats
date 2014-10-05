package at.basketballsalzburg.bbstats.services.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.services.AgeGroupService;

@ContextConfiguration("classpath:META-INF/db-test.xml")
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class AgeGroupServiceTest extends
    AbstractTransactionalTestNGSpringContextTests
{

    private static final String AGEGROUP_NAME = "test";

    @Autowired
    private AgeGroupService ageGroupService;

    @Test
    public void addAgeGroup()
    {
        int size = ageGroupService.findAll().size();
        AgeGroupDTO ageGroup = new AgeGroupDTO();
        ageGroup.setName(AGEGROUP_NAME);
        ageGroupService.save(ageGroup);
        assertEquals(size + 1, ageGroupService.findAll().size());
        ageGroup = ageGroupService.findByName(AGEGROUP_NAME);
        assertNotNull(ageGroup);
        assertEquals(ageGroup.getName(), AGEGROUP_NAME);
    }
}
