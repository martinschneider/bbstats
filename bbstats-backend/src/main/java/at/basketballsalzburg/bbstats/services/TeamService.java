package at.basketballsalzburg.bbstats.services;

import java.util.List;

import at.basketballsalzburg.bbstats.dto.TeamDTO;

/**
 * @author Martin Schneider
 */
public interface TeamService {

	public abstract void save(TeamDTO team);

	public abstract List<TeamDTO> findAll();

	public abstract TeamDTO findByName(String name);

	public abstract void delete(TeamDTO league);

	public abstract TeamDTO findById(Long teamId);
}
