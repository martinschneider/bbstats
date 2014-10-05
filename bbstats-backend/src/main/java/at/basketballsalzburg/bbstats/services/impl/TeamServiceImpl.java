package at.basketballsalzburg.bbstats.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.dao.TeamDAO;
import at.basketballsalzburg.bbstats.dto.TeamDTO;
import at.basketballsalzburg.bbstats.entities.Team;
import at.basketballsalzburg.bbstats.services.TeamService;

/**
 * @author Martin Schneider
 */
@Repository
@Transactional
public class TeamServiceImpl implements TeamService
{

    private DozerBeanMapper mapper;
    private TeamDAO dao;

    @Autowired
    public void setMapper(DozerBeanMapper mapper)
    {
        this.mapper = mapper;
    }

    @Autowired
    public void setDao(TeamDAO dao)
    {
        this.dao = dao;
    }

    public void save(TeamDTO dto)
    {
        dao.saveAndFlush(mapper.map(dto, Team.class));
    }

    public List<TeamDTO> findAll()
    {
        List<TeamDTO> teams = new ArrayList<TeamDTO>();
        for (Team team : dao.findAll(new Sort(Sort.Direction.ASC, "name")))
        {
            teams.add(mapper.map(team, TeamDTO.class));
        }
        return teams;
    }

    public TeamDTO findByName(String name)
    {
        return mapper.map(dao.findByName(name), TeamDTO.class);
    }

    public void delete(TeamDTO team)
    {
        dao.delete(mapper.map(team, Team.class));
    }

    public TeamDTO findById(Long teamId)
    {
        return mapper.map(dao.findOne(teamId), TeamDTO.class);
    }

}
