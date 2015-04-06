package at.basketballsalzburg.bbstats.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.dao.AgeGroupDAO;
import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.entities.AgeGroup;
import at.basketballsalzburg.bbstats.services.AgeGroupService;

/**
 * @author Martin Schneider
 */
@Repository
@Transactional
public class AgeGroupServiceImpl implements AgeGroupService
{

    private DozerBeanMapper mapper;

    @Autowired
    private AgeGroupDAO dao;

    @Autowired
    public void setMapper(DozerBeanMapper mapper)
    {
        this.mapper = mapper;
    }

    @Autowired
    public void setDao(AgeGroupDAO dao)
    {
        this.dao = dao;
    }

    public void save(AgeGroupDTO ageGroupDTO)
    {
        dao.saveAndFlush(mapper.map(ageGroupDTO, AgeGroup.class));
    }

    public List<AgeGroupDTO> findAll()
    {
        List<AgeGroupDTO> ageGroups = new ArrayList<AgeGroupDTO>();
        for (Object ageGroup : dao.findAll(new Sort(Sort.Direction.ASC, "name")))
        {
            ageGroups.add(mapper.map(ageGroup, AgeGroupDTO.class));
        }
        return ageGroups;
    }

    public AgeGroupDTO findByName(String name)
    {
        return mapper.map(dao.findByName(name), AgeGroupDTO.class);
    }

    public void delete(AgeGroupDTO ageGroup)
    {
        dao.delete(mapper.map(ageGroup, AgeGroup.class));
    }

    public AgeGroupDTO findById(Long ageGroupId)
    {
        return mapper.map(dao.findOne(ageGroupId), AgeGroupDTO.class);
    }

	@Override
	public List<AgeGroupDTO> findByQuery(String query) {
		List<AgeGroupDTO> ageGroups = new ArrayList<AgeGroupDTO>();
        for (Object ageGroup : dao.findByQuery(query))
        {
            ageGroups.add(mapper.map(ageGroup, AgeGroupDTO.class));
        }
        return ageGroups;
	}

	@Override
	public Collection<AgeGroupDTO> findByIds(ArrayList<Long> ids) {
		List<AgeGroupDTO> ageGroups = new ArrayList<AgeGroupDTO>();
        for (Object ageGroup : dao.findByIdIn(ids))
        {
        	ageGroups.add(mapper.map(ageGroup, AgeGroupDTO.class));
        }
        return ageGroups;
	}
}
