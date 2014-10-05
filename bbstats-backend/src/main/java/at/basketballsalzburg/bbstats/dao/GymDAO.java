package at.basketballsalzburg.bbstats.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.entities.Gym;

/**
 * @author Martin Schneider
 */
@Transactional
public interface GymDAO extends JpaRepository<Gym, Long>
{
    Gym findByName(String name);
}
