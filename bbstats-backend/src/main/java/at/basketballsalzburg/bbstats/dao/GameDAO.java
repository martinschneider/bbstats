package at.basketballsalzburg.bbstats.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.entities.Game;

/**
 * @author Martin Schneider
 */
@Transactional
public interface GameDAO extends JpaRepository<Game, Long>
{
    List<Game> findByDateTimeAfterAndDateTimeBeforeOrderByDateTimeDesc(
        Date dateFrom, Date dateTo);

    List<Game> findByDateTimeAfterOrderByDateTimeAsc(Date dateFrom);

    List<Game> findByDateTimeBeforeOrderByDateTimeDesc(Date dateTo);

    List<Game> findByDateTimeAfter(Date dateFrom, Pageable pageable);

    List<Game> findByDateTimeBefore(Date dateTo, Pageable pageable);

    @Query("select g from Game g join g.stats as stats join stats.player as player with player.id=?1")
    List<Game> findByPlayerOrderByDateTimeDesc(Long playerId);

    @Query("select g from Game g join g.coaches as coach with coach.id=?1")
    List<Game> findByCoachOrderByDateTimeDesc(Long coachId);

    @Query("select g from Game g join g.stats as stats join stats.player as player with player.id=?1")
    List<Game> findByPlayer(Long playerId, Pageable pageable);

    @Query("select g from Game g join g.coaches as coach with coach.id=?1")
    List<Game> findByCoach(Long coachId, Pageable pageable);

    int countByDateTimeAfter(Date dateFrom);

    int countByDateTimeBefore(Date dateTo);

    @Query("select count(g) from Game g join g.coaches as coach with coach.id=?1")
    int countByCoach(Long coachId);

    @Query("select count(g) from Game g join g.stats as stats join stats.player as player with player.id=?1")
    int countByPlayer(Long playerId);

}
