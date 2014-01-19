package at.basketballsalzburg.bbstats.services;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;

import at.basketballsalzburg.bbstats.dto.GameDTO;

/**
 * @author Martin Schneider
 */
public interface GameService {

	public long count();
	
	public void save(GameDTO game);

	public List<GameDTO> findAll();
	
	public List<GameDTO> find(int page, int size, Sort sort);

	public GameDTO findById(Long gameId);

	public void delete(GameDTO gameId);

	public List<GameDTO> findBefore(Date dateTo);
	
	public List<GameDTO> findAfter(Date dateFrom);
	
	public List<GameDTO> findBetween(Date dateFrom, Date dateTo);

	public List<GameDTO> findAllGamesForPlayer(Long playerId);

	public List<GameDTO> findAllGamesForCoach(Long coachId);

	public boolean isAway(GameDTO game);

	public boolean isHome(GameDTO game);

	public boolean isWin(GameDTO game);
	
	public boolean isShowStats(GameDTO game);

	public int countResults();

	public int countSchedule();
	
	public int countByPlayer(Long playerId);
	
	public int countByCoach(Long coachId);

	public List<GameDTO> findResults(int page, int size, Sort sort);

	public List<GameDTO> findSchedule(int page, int size, Sort sort);
	
	public List<GameDTO> findGamesForPlayer(Long playerId, int page, int size, Sort sort);
	
	public List<GameDTO> findGamesForCoach(Long coachId, int page, int size, Sort sort);
}