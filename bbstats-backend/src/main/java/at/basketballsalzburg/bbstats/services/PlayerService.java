package at.basketballsalzburg.bbstats.services;

import java.util.List;

import at.basketballsalzburg.bbstats.dto.PlayerDTO;

public interface PlayerService {

	public abstract void save(PlayerDTO player);

	public abstract List<PlayerDTO> findAll();

	public abstract PlayerDTO findByName(String firstName, String lastName);

	public abstract PlayerDTO findById(Long id);

	public abstract void delete(PlayerDTO player);

}