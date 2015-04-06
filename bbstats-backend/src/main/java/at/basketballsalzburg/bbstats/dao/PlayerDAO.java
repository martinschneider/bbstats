package at.basketballsalzburg.bbstats.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.entities.AgeGroup;
import at.basketballsalzburg.bbstats.entities.Player;

/**
 * @author Martin Schneider
 */
@Transactional
public interface PlayerDAO extends JpaRepository<Player, Long> {
	Player findByFirstNameAndLastName(String firstName, String lastName);

	@Query("select distinct practice.players from Practice practice where practice.dateTime>:date")
	List<Player> findPlayersActiveSince(@Param("date") Date date);

	@Query("select distinct p from Player p, AgeGroup agegroup where agegroup=:agegroup and agegroup member of p.ageGroups")
	List<Player> findByAgeGroupOrderByName(@Param("agegroup") AgeGroup agegroup);

	@Query("select distinct p from Player p where p.ageGroups is empty")
	List<Player> findWithoutAgeGroup();

	@Query("select distinct p from Player p where p.ageGroups is not empty")
	List<Player> findWithAgeGroup();

	@Query("select distinct p from Player p where lower(p.firstName) like %:query% or lower(p.lastName) like %:query%")
	List<Player> findByQuery(@Param("query") String query);
	
	List<Player> findByIdIn(List<Long> ids);
}
