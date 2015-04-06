package at.basketballsalzburg.bbstats.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.entities.Coach;

/**
 * @author Martin Schneider
 */
@Transactional
public interface CoachDAO extends JpaRepository<Coach, Long> {
	Coach findByFirstNameAndLastName(String firstName, String lastName);

	@Query("select distinct c from Coach c where lower(firstName) like %:query% or lower(lastName) like %:query%")
	List<Coach> findByQuery(@Param("query") String query);

	List<Coach> findByIdIn(List<Long> ids);
}
