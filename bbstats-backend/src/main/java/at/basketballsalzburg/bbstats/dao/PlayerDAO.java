package at.basketballsalzburg.bbstats.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.entities.Player;

/**
 * @author Martin Schneider
 */
@Transactional
public interface PlayerDAO extends JpaRepository<Player, Long> {
	Player findByFirstNameAndLastName(String firstName, String lastName);

	@Query("select distinct practice.players from Practice practice where practice.dateTime>:date")
	List<Player> findPlayersActiveSince(@Param("date") Date date);
}
