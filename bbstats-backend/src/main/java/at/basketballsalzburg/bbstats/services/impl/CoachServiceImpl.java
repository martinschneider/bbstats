package at.basketballsalzburg.bbstats.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.dao.CoachDAO;
import at.basketballsalzburg.bbstats.dto.CoachDTO;
import at.basketballsalzburg.bbstats.entities.Coach;
import at.basketballsalzburg.bbstats.services.CoachService;

/**
 * @author Martin Schneider
 */
@Repository
@Transactional
public class CoachServiceImpl implements CoachService
{
    private CoachDAO dao;
    private DozerBeanMapper mapper;

    @Autowired
    public void setMapper(DozerBeanMapper mapper)
    {
        this.mapper = mapper;
    }

    /** {@inheritDoc} **/
    @Autowired
    public void setDao(CoachDAO dao)
    {
        this.dao = dao;
    }

    /** {@inheritDoc} **/
    public void save(CoachDTO dto)
    {
        dao.saveAndFlush(mapper.map(dto, Coach.class));
    }

    /** {@inheritDoc} **/
    public List<CoachDTO> findAll()
    {
        List<CoachDTO> coaches = new ArrayList<CoachDTO>();
        for (Object coach : dao.findAll(new Sort(Sort.Direction.ASC, "lastName", "firstName")))
        {
            coaches.add(mapper.map(coach, CoachDTO.class));
        }
        return coaches;
    }

    /** {@inheritDoc} **/
    public CoachDTO findByName(String firstName, String lastName)
    {
        return mapper.map(
            dao.findByFirstNameAndLastName(firstName, lastName),
            CoachDTO.class);
    }

    public CoachDTO findById(Long id)
    {
        return mapper.map(dao.findOne(id), CoachDTO.class);
    }

    public void delete(CoachDTO coach)
    {
        dao.delete(mapper.map(coach, Coach.class));
    }

	@Override
	public List<CoachDTO> findByQuery(String query) {
		List<CoachDTO> coaches = new ArrayList<CoachDTO>();
        for (Object coach : dao.findByQuery(query))
        {
            coaches.add(mapper.map(coach, CoachDTO.class));
        }
        return coaches;
	}

	@Override
	public List<CoachDTO> findByIds(List<Long> ids) {
		List<CoachDTO> coaches = new ArrayList<CoachDTO>();
        for (Object coach : dao.findByIdIn(ids))
        {
            coaches.add(mapper.map(coach, CoachDTO.class));
        }
        return coaches;
	}
}