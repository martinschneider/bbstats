package at.basketballsalzburg.bbstats.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.entities.League;

/**
 * @author Martin Schneider
 */
@Transactional
public interface LeagueDAO extends JpaRepository<League, Long>
{
    League findByName(String name);
}
