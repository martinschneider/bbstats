package at.basketballsalzburg.bbstats.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;

/**
 * @author Martin Schneider
 */
public interface AgeGroupService
{

    public abstract void save(AgeGroupDTO ageGroup);

    public abstract List<AgeGroupDTO> findAll();

    public abstract AgeGroupDTO findByName(String name);

    public abstract void delete(AgeGroupDTO ageGroup);

    public abstract AgeGroupDTO findById(Long ageGroupId);

	public abstract List<AgeGroupDTO> findByQuery(String lowerCase);

	public abstract Collection<AgeGroupDTO> findByIds(
			ArrayList<Long> newArrayList);

}