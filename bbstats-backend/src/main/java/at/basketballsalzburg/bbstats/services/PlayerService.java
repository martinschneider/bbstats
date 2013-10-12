package at.basketballsalzburg.bbstats.services;

import java.util.Date;
import java.util.List;

import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.dto.statistics.CompletePlayerStatisticDTO;

/**
 * @author Martin Schneider
 */
public interface PlayerService {

	public abstract void save(PlayerDTO player);

	public abstract List<PlayerDTO> findAll();
	
	public abstract List<PlayerDTO> findAllActiveSince(Date date);

	public abstract PlayerDTO findByName(String firstName, String lastName);

	public abstract PlayerDTO findById(Long playerId);

	public abstract void delete(PlayerDTO player);
	
	public List<CompletePlayerStatisticDTO> getCompleteStatistics(Date fromDate, Date toDate);

}