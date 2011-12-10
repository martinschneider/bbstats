package at.basketballsalzburg.bbstats.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.dao.TeamDAO;
import at.basketballsalzburg.bbstats.dto.TeamDTO;
import at.basketballsalzburg.bbstats.entities.Team;
import at.basketballsalzburg.bbstats.services.TeamService;

import com.googlecode.genericdao.search.Search;

@Transactional
public class TeamServiceImpl implements TeamService {

	private DozerBeanMapper mapper;
	private TeamDAO dao;

	@Autowired
	public void setMapper(DozerBeanMapper mapper) {
		this.mapper = mapper;
	}

	@Autowired
	public void setDao(TeamDAO dao) {
		this.dao = dao;
	}

	public void save(TeamDTO TeamDTO) {
		Team team = mapper.map(TeamDTO, Team.class);
		if (team.getId() == null) {
			dao.persist(team);
		} else {
			dao.merge(team);
		}
		dao.flush();
	}

	public List<TeamDTO> findAll() {
		List<TeamDTO> teams = new ArrayList<TeamDTO>();
		for (Object team : dao.search(new Search(Team.class).addSort("name",
				false))) {
			teams.add(mapper.map(team, TeamDTO.class));
		}
		return teams;
	}

	public TeamDTO findByName(String name) {
		if (name == null)
			return null;
		return mapper.map(
				dao.searchUnique(new Search().addFilterEqual("name", name)),
				TeamDTO.class);
	}

	public void delete(TeamDTO team) {
		dao.remove(mapper.map(team, Team.class));
	}

	public TeamDTO findById(Long teamId) {
		return mapper.map(dao.find(teamId), TeamDTO.class);
	}

}
