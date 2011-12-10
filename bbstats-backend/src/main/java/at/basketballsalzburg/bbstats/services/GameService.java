package at.basketballsalzburg.bbstats.services;

import java.util.Date;
import java.util.List;

import at.basketballsalzburg.bbstats.dto.GameDTO;

public interface GameService {

	public abstract void save(GameDTO game);

	public abstract List<GameDTO> findAll();

	public abstract GameDTO findByName(String name);

	public abstract GameDTO findById(Long gameId);

	public abstract void delete(GameDTO findById);

	public abstract List<GameDTO> findAll(Date dateFrom, Date dateTo);

}