package at.basketballsalzburg.bbstats;

import junit.framework.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import at.basketballsalzburg.bbstats.dto.CoachDTO;
import at.basketballsalzburg.bbstats.services.CoachService;

@ContextConfiguration("classpath:at/basketballsalzburg/bbstats/spring/spring.xml")
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class CoachServiceTest extends
		AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private CoachService coachService;

	@Test
	public void addCoach() {
		int size = coachService.findAll().size();
		CoachDTO coach = new CoachDTO();
		coach.setLastName("jones");
		coach.setFirstName("tom");
		coachService.save(coach);
		Assert.assertEquals(size + 1, coachService.findAll().size());
	}

	@Test
	public void editCoach() {
		CoachDTO coach = new CoachDTO();
		coach.setLastName("jones");
		coach.setFirstName("tom");
		coachService.save(coach);
		CoachDTO foundCoach = coachService.findByName("tom", "jones");
		foundCoach.setFirstName("tim");
		coachService.save(foundCoach);
		Assert.assertNotNull(coachService.findByName("tim", "jones"));
	}
}
