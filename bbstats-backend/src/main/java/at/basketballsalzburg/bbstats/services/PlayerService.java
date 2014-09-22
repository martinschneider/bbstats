package at.basketballsalzburg.bbstats.services;

import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.dto.statistics.CompletePlayerStatisticDTO;

/**
 * @author Martin Schneider
 */
public interface PlayerService {

	public abstract void save(PlayerDTO player);

	public abstract SortedSet<PlayerDTO> findAll();
	
	public abstract SortedSet<PlayerDTO> findAllForAgegroup(AgeGroupDTO agegroup);
	
	public abstract SortedSet<PlayerDTO> findAllActiveSince(Date date);

	public abstract PlayerDTO findByName(String firstName, String lastName);

	public abstract PlayerDTO findById(Long playerId);

	public abstract void delete(PlayerDTO player);
	
	public abstract List<CompletePlayerStatisticDTO> getCompleteStatistics(Date fromDate, Date toDate);

	public abstract SortedSet<PlayerDTO> findAllWithAgeGroup();
	
	public abstract SortedSet<PlayerDTO> findAllWithoutAgeGroup();

}