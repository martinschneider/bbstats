package at.basketballsalzburg.bbstats.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.dao.LeagueDAO;
import at.basketballsalzburg.bbstats.dto.LeagueDTO;
import at.basketballsalzburg.bbstats.entities.League;
import at.basketballsalzburg.bbstats.services.LeagueService;

/**
 * @author Martin Schneider
 */
@Repository
@Transactional
public class LeagueServiceImpl implements LeagueService
{

    private DozerBeanMapper mapper;
    private LeagueDAO dao;

    @Autowired
    public void setMapper(DozerBeanMapper mapper)
    {
        this.mapper = mapper;
    }

    @Autowired
    public void setDao(LeagueDAO dao)
    {
        this.dao = dao;
    }

    public void save(LeagueDTO leagueDTO)
    {
        dao.saveAndFlush(mapper.map(leagueDTO, League.class));
    }

    public List<LeagueDTO> findAll()
    {
        List<LeagueDTO> leagues = new ArrayList<LeagueDTO>();
        for (Object league : dao.findAll(new Sort(Sort.Direction.ASC, "name")))
        {
            leagues.add(mapper.map(league, LeagueDTO.class));
        }
        return leagues;
    }

    public LeagueDTO findByName(String name)
    {
        return mapper.map(dao.findByName(name), LeagueDTO.class);
    }

    public void delete(LeagueDTO league)
    {
        dao.delete(mapper.map(league, League.class));
    }

    public LeagueDTO findById(Long leagueId)
    {
        return mapper.map(dao.findOne(leagueId), LeagueDTO.class);
    }

}
