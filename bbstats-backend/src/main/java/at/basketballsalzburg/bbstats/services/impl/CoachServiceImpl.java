package at.basketballsalzburg.bbstats.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.dao.CoachDAO;
import at.basketballsalzburg.bbstats.dto.CoachDTO;
import at.basketballsalzburg.bbstats.entities.Coach;
import at.basketballsalzburg.bbstats.services.CoachService;

import com.googlecode.genericdao.search.Search;

@Transactional
public class CoachServiceImpl implements CoachService {
	private CoachDAO dao;
	private DozerBeanMapper mapper;

	@Autowired
	public void setMapper(DozerBeanMapper mapper) {
		this.mapper = mapper;
	}

	/** {@inheritDoc} **/
	@Autowired
	public void setDao(CoachDAO dao) {
		this.dao = dao;
	}

	/** {@inheritDoc} **/
	public void save(CoachDTO dto) {
		Coach coach = mapper.map(dto, Coach.class);
		if (coach.getId() == null) {
			dao.persist(coach);
		} else {
			dao.merge(coach);
		}
		dao.flush();
	}

	/** {@inheritDoc} **/
	public List<CoachDTO> findAll() {
		List<CoachDTO> coaches = new ArrayList<CoachDTO>();
		for (Object coach : dao.search(new Search(Coach.class).addSort(
				"lastName", false).addSort("firstName", false))) {
			coaches.add(mapper.map(coach, CoachDTO.class));
		}
		return coaches;
	}

	/** {@inheritDoc} **/
	public CoachDTO findByName(String firstName, String lastName) {
		if (firstName == null && lastName == null)
			return null;
		return mapper.map(
				dao.searchUnique(new Search().addFilterEqual("firstName",
						firstName).addFilterEqual("lastName", lastName)),
				CoachDTO.class);
	}

	public CoachDTO findById(Long id) {
		return mapper.map(dao.find(id), CoachDTO.class);
	}

	public void delete(CoachDTO coach) {
		dao.remove(mapper.map(coach, Coach.class));
	}
}