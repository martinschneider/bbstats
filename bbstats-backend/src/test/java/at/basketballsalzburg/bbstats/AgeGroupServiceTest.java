package at.basketballsalzburg.bbstats;

import junit.framework.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.services.AgeGroupService;

@ContextConfiguration("classpath:at/basketballsalzburg/bbstats/spring/spring.xml")
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class AgeGroupServiceTest extends
		AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private AgeGroupService ageGroupService;

	@Test
	public void addAgeGroup() {
		int size = ageGroupService.findAll().size();
		AgeGroupDTO ageGroup = new AgeGroupDTO();
		ageGroup.setName("test");
		ageGroupService.save(ageGroup);
		Assert.assertEquals(size + 1, ageGroupService.findAll().size());
	}
}
