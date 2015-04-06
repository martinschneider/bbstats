package at.basketballsalzburg.bbstats.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.entities.AgeGroup;
import at.basketballsalzburg.bbstats.entities.League;

/**
 * @author Martin Schneider
 */
@Transactional
public interface LeagueDAO extends JpaRepository<League, Long>
{
    League findByName(String name);

	@Query("select distinct l from League l where lower(name) like %:query% or lower(shortName) like %:query%")
	List<AgeGroup> findByQuery(@Param("query") String query);

	List<AgeGroup> findByIdIn(List<Long> ids);
}
