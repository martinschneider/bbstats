package at.basketballsalzburg.bbstats.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.dao.PlayerDAO;
import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.entities.Player;
import at.basketballsalzburg.bbstats.services.PlayerService;

import com.googlecode.genericdao.search.Search;

@Transactional
public class PlayerServiceImpl implements PlayerService {

	private PlayerDAO dao;
	private DozerBeanMapper mapper;

	@Autowired
	public void setMapper(DozerBeanMapper mapper) {
		this.mapper = mapper;
	}

	@Autowired
	public void setDao(PlayerDAO dao) {
		this.dao = dao;
	}

	public void save(PlayerDTO dto) {
		Player player = mapper.map(dto, Player.class);
		if (player.getId() == null) {
			dao.persist(player);
		} else {
			dao.merge(player);
		}
		dao.flush();
	}

	public List<PlayerDTO> findAll() {
		List<PlayerDTO> players = new ArrayList<PlayerDTO>();
		for (Object player : dao.search(new Search(Player.class).addSort(
				"lastName", false).addSort("firstName", false))) {
			players.add(mapper.map(player, PlayerDTO.class));
		}
		return players;
	}

	public PlayerDTO findByName(String firstName, String lastName) {
		if (firstName == null || lastName==null)
			return null;
		return mapper.map(
				dao.searchUnique(new Search().addFilterEqual("lastName", lastName).addFilterEqual("firstName", firstName)),
				PlayerDTO.class);
	}

	public PlayerDTO findById(Long id) {
		return mapper.map(dao.find(id), PlayerDTO.class);
	}

	public void delete(PlayerDTO player) {
		dao.remove(mapper.map(player, Player.class));
	}

}