package at.basketballsalzburg.bbstats.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.dao.GymDAO;
import at.basketballsalzburg.bbstats.dto.GymDTO;
import at.basketballsalzburg.bbstats.entities.Gym;
import at.basketballsalzburg.bbstats.services.GymService;

import com.googlecode.genericdao.search.Search;

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
		Gym gym = mapper.map(dto, Gym.class);
		if (gym.getId() == null) {
			dao.persist(gym);
		} else {
			dao.merge(gym);
		}
		dao.flush();
	}

	/** {@inheritDoc} **/
	public List<GymDTO> findAll() {
		List<GymDTO> gyms = new ArrayList<GymDTO>();
		for (Gym gym : dao.findAll()) {
			gyms.add(mapper.map(gym, GymDTO.class));
		}
		return gyms;
	}

	/** {@inheritDoc} **/
	public GymDTO findByName(String name) {
		if (name == null)
			return null;
		return mapper.map(
				dao.searchUnique(new Search().addFilterEqual("name", name)),
				GymDTO.class);
	}

	public GymDTO findById(Long gymId) {
		return mapper.map(dao.find(gymId), GymDTO.class);
	}

	public void delete(GymDTO gym) {
		dao.remove(mapper.map(gym, Gym.class));
	}
}