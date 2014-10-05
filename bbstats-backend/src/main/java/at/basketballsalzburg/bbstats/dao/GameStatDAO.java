package at.basketballsalzburg.bbstats.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.entities.GameStat;

/**
 * @author Martin Schneider
 */
@Transactional
public interface GameStatDAO extends JpaRepository<GameStat, Long>
{
}
