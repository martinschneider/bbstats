package at.basketballsalzburg.bbstats.services.impl;

import static org.testng.Assert.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import at.basketballsalzburg.bbstats.dto.GymDTO;
import at.basketballsalzburg.bbstats.services.GymService;

@ContextConfiguration("classpath:META-INF/db-test.xml")
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class GymServiceTest extends
		AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private GymService gymService;

	@Test
	public void addGym() {
		int size = gymService.findAll().size();
		GymDTO gym = new GymDTO();
		gym.setName("my private gym");
		gym.setShortName("MPG");
		gym.setCity("los angeles");
		gymService.save(gym);
		assertEquals(size + 1, gymService.findAll().size());
	}
}
