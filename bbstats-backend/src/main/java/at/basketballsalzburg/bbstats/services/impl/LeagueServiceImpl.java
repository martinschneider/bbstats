package at.basketballsalzburg.bbstats.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.dao.LeagueDAO;
import at.basketballsalzburg.bbstats.dto.LeagueDTO;
import at.basketballsalzburg.bbstats.entities.League;
import at.basketballsalzburg.bbstats.services.LeagueService;

import com.googlecode.genericdao.search.Search;

@Transactional
public class LeagueServiceImpl implements LeagueService {

	private DozerBeanMapper mapper;
	private LeagueDAO dao;

	@Autowired
	public void setMapper(DozerBeanMapper mapper) {
		this.mapper = mapper;
	}

	@Autowired
	public void setDao(LeagueDAO dao) {
		this.dao = dao;
	}

	public void save(LeagueDTO LeagueDTO) {
		League league = mapper.map(LeagueDTO, League.class);
		if (league.getId() == null) {
			dao.persist(league);
		} else {
			dao.merge(league);
		}
		dao.flush();
	}

	public List<LeagueDTO> findAll() {
		List<LeagueDTO> leagues = new ArrayList<LeagueDTO>();
		for (Object league : dao.search(new Search(League.class).addSort(
				"name", false))) {
			leagues.add(mapper.map(league, LeagueDTO.class));
		}
		return leagues;
	}

	public LeagueDTO findByName(String name) {
		if (name == null)
			return null;
		return mapper.map(
				dao.searchUnique(new Search().addFilterEqual("name", name)),
				LeagueDTO.class);
	}

	public void delete(LeagueDTO league) {
		dao.remove(mapper.map(league, League.class));
	}

	public LeagueDTO findById(Long leagueId) {
		return mapper.map(dao.find(leagueId), LeagueDTO.class);
	}

}
