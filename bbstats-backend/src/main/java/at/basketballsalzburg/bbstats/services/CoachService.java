package at.basketballsalzburg.bbstats.services;

import java.util.List;

import at.basketballsalzburg.bbstats.dto.CoachDTO;

/**
 * @author Martin Schneider
 */
public interface CoachService
{
    public abstract void save(CoachDTO coach);

    public abstract void delete(CoachDTO coach);

    public abstract List<CoachDTO> findAll();

    public abstract CoachDTO findByName(String firstName, String lastName);

    public abstract CoachDTO findById(Long coachId);
    
    public abstract List<CoachDTO> findByIds(List<Long> ids);
    
    public abstract List<CoachDTO> findByQuery(String query);

}