package at.basketballsalzburg.bbstats.services;

import java.util.List;

import at.basketballsalzburg.bbstats.dto.GymDTO;

public interface GymService {

	public abstract void save(GymDTO gym);

	public abstract List<GymDTO> findAll();

	public abstract GymDTO findByName(String name);

	public abstract GymDTO findById(Long gymId);

	public abstract void delete(GymDTO gym);

}