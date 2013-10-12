package at.basketballsalzburg.bbstats.services.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:META-INF/db-test.xml")
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class PlayerStatsTest {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Test
	public void testPracticeStats()
	{
	}
}
