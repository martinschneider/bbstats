package at.basketballsalzburg.bbstats.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.entities.Coach;

/**
 * @author Martin Schneider
 */
@Transactional
public interface CoachDAO extends JpaRepository<Coach, Long> {
	Coach findByFirstNameAndLastName(String firstName, String lastName);
}
