package at.basketballsalzburg.bbstats.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.entities.AgeGroup;

/**
 * @author Martin Schneider
 */
@Transactional
public interface AgeGroupDAO extends JpaRepository<AgeGroup, Long> {
	AgeGroup findByName(final String name);

	@Query("select distinct a from AgeGroup a where lower(name) like %:query%")
	List<AgeGroup> findByQuery(@Param("query") String query);

	List<AgeGroup> findByIdIn(List<Long> ids);
}
