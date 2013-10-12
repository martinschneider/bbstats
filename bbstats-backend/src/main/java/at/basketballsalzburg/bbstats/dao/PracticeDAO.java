package at.basketballsalzburg.bbstats.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.entities.Practice;

/**
 * @author Martin Schneider
 */
@Transactional
public interface PracticeDAO extends JpaRepository<Practice, Long> {
	
	List<Practice> findByDateTimeAfterAndDateTimeBeforeOrderByDateTimeDesc(Date dateFrom, Date dateTo);

	List<Practice> findByDateTimeAfterOrderByDateTimeDesc(Date dateFrom);

	List<Practice> findByDateTimeBeforeOrderByDateTimeDesc(Date dateTo);

	@Query("select p from Practice p join p.players as player with player.id=?1")
	List<Practice> findByPlayerOrderByDateTimeDesc(Long playerId);
	
	@Query("select p from Practice p join p.coaches as coach with coach.id=?1")
	List<Practice> findByCoachOrderByDateTimeDesc(Long coachId);
}
