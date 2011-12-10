package at.basketballsalzburg.bbstats.services;

import java.util.List;

import at.basketballsalzburg.bbstats.dto.CoachDTO;

public interface CoachService {

	public abstract void save(CoachDTO coach);

	public abstract void delete(CoachDTO coach);

	public abstract List<CoachDTO> findAll();

	public abstract CoachDTO findByName(String firstName, String lastName);

	public abstract CoachDTO findById(Long id);

}