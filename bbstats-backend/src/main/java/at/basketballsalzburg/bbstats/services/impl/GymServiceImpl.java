package at.basketballsalzburg.bbstats.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.dao.GymDAO;
import at.basketballsalzburg.bbstats.dto.GymDTO;
import at.basketballsalzburg.bbstats.entities.Gym;
import at.basketballsalzburg.bbstats.services.GymService;

/**
 * @author Martin Schneider
 */
@Repository
@Transactional
public class GymServiceImpl implements GymService {
	private GymDAO dao;
	private DozerBeanMapper mapper;

	@Autowired
	public void setMapper(DozerBeanMapper mapper) {
		this.mapper = mapper;
	}

	/** {@inheritDoc} **/
	@Autowired
	public void setDao(GymDAO dao) {
		this.dao = dao;
	}

	/** {@inheritDoc} **/
	public void save(GymDTO dto) {
		dao.saveAndFlush(mapper.map(dto, Gym.class));
	}

	/** {@inheritDoc} **/
	public List<GymDTO> findAll() {
		List<GymDTO> gyms = new ArrayList<GymDTO>();
		for (Gym gym : dao.findAll(new Sort(Sort.Direction.ASC, "city", "name"))) {
			gyms.add(mapper.map(gym, GymDTO.class));
		}
		return gyms;
	}

	/** {@inheritDoc} **/
	public GymDTO findByName(String name) {
		return mapper.map(dao.findByName(name), GymDTO.class);
	}

	public GymDTO findById(Long gymId) {
		return mapper.map(dao.findOne(gymId), GymDTO.class);
	}

	public void delete(GymDTO gym) {
		dao.delete(mapper.map(gym, Gym.class));
	}
}