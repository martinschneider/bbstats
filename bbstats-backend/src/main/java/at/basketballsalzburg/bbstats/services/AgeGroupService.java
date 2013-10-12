package at.basketballsalzburg.bbstats.services;

import java.util.List;

import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;

/**
 * @author Martin Schneider
 */
public interface AgeGroupService {

	public abstract void save(AgeGroupDTO ageGroup);

	public abstract List<AgeGroupDTO> findAll();

	public abstract AgeGroupDTO findByName(String name);

	public abstract void delete(AgeGroupDTO ageGroup);

	public abstract AgeGroupDTO findById(Long ageGroupId);

}