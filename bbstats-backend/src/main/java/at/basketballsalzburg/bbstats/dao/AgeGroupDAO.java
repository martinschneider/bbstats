package at.basketballsalzburg.bbstats.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.entities.AgeGroup;

/**
 * @author Martin Schneider
 */
@Transactional
public interface AgeGroupDAO extends JpaRepository<AgeGroup, Long>
{
    AgeGroup findByName(final String name);
}
