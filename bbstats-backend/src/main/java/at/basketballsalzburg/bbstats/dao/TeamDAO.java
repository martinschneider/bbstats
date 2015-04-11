package at.basketballsalzburg.bbstats.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.entities.AgeGroup;
import at.basketballsalzburg.bbstats.entities.Team;

/**
 * @author Martin Schneider
 */
@Transactional
public interface TeamDAO extends JpaRepository<Team, Long>
{
    Team findById(final Long id);

    Team findByName(String name);
    
	@Query("select distinct t from Team t where lower(name) like :query%")
	List<AgeGroup> findByQuery(@Param("query") String query);

	List<AgeGroup> findByIdIn(List<Long> ids);
}
