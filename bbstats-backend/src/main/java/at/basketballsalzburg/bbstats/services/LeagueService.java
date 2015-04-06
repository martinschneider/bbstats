package at.basketballsalzburg.bbstats.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import at.basketballsalzburg.bbstats.dto.LeagueDTO;

/**
 * @author Martin Schneider
 */
public interface LeagueService
{

    public abstract void save(LeagueDTO league);

    public abstract List<LeagueDTO> findAll();

    public abstract LeagueDTO findByName(String name);

    public abstract void delete(LeagueDTO league);

    public abstract LeagueDTO findById(Long leagueId);

	public abstract List<LeagueDTO> findByQuery(String lowerCase);

	public abstract Collection<LeagueDTO> findByIds(ArrayList<Long> newArrayList);
}
