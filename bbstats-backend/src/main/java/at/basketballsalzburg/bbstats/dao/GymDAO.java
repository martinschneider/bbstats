package at.basketballsalzburg.bbstats.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.entities.Gym;

/**
 * @author Martin Schneider
 */
@Transactional
public interface GymDAO extends JpaRepository<Gym, Long>
{
    Gym findByName(String name);
    
	@Query("select distinct g from Gym g where lower(g.city) like :query% or lower(g.name) like :query%")
	List<Gym> findByQuery(@Param("query") String query);
	
	List<Gym> findByIdIn(List<Long> ids);
}
