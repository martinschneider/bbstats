package at.basketballsalzburg.bbstats.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.entities.Team;

/**
 * @author Martin Schneider
 */
@Transactional
public interface TeamDAO extends JpaRepository<Team, Long> {
	Team findById(final Long id);

	Team findByName(String name);
}
