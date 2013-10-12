package at.basketballsalzburg.bbstats.services;

import java.util.Date;
import java.util.List;

import at.basketballsalzburg.bbstats.dto.GameDTO;

/**
 * @author Martin Schneider
 */
public interface GameService {

	public abstract void save(GameDTO game);

	public abstract List<GameDTO> findAll();

	public abstract GameDTO findById(Long gameId);

	public abstract void delete(GameDTO gameId);

	public abstract List<GameDTO> findBefore(Date dateTo);
	
	public abstract List<GameDTO> findAfter(Date dateFrom);
	
	public abstract List<GameDTO> findBetween(Date dateFrom, Date dateTo);

	public abstract List<GameDTO> findAllGamesForPlayer(Long playerId);

	public abstract List<GameDTO> findAllGamesForCoach(Long coachId);

	public abstract boolean isAway(GameDTO game);

	public abstract boolean isHome(GameDTO game);

	public abstract boolean isWin(GameDTO game);
}