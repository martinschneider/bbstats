package at.basketballsalzburg.bbstats.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.dao.AgeGroupDAO;
import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.entities.AgeGroup;
import at.basketballsalzburg.bbstats.services.AgeGroupService;

import com.googlecode.genericdao.search.Search;

@Transactional
public class AgeGroupServiceImpl implements AgeGroupService {

	private DozerBeanMapper mapper;
	private AgeGroupDAO dao;

	@Autowired
	public void setMapper(DozerBeanMapper mapper) {
		this.mapper = mapper;
	}

	@Autowired
	public void setDao(AgeGroupDAO dao) {
		this.dao = dao;
	}

	public void save(AgeGroupDTO ageGroupDTO) {
		AgeGroup ageGroup = mapper.map(ageGroupDTO, AgeGroup.class);
		if (ageGroup.getId() == null) {
			dao.persist(ageGroup);
		} else {
			dao.merge(ageGroup);
		}
		dao.flush();
	}

	public List<AgeGroupDTO> findAll() {
		List<AgeGroupDTO> ageGroups = new ArrayList<AgeGroupDTO>();
		for (Object ageGroup : dao.search(new Search(AgeGroup.class).addSort(
				"name", false))) {
			ageGroups.add(mapper.map(ageGroup, AgeGroupDTO.class));
		}
		return ageGroups;
	}

	public AgeGroupDTO findByName(String name) {
		if (name == null)
			return null;
		return mapper.map(
				dao.searchUnique(new Search().addFilterEqual("name", name)),
				AgeGroupDTO.class);
	}

	public void delete(AgeGroupDTO ageGroup) {
		dao.remove(mapper.map(ageGroup, AgeGroup.class));
	}

	public AgeGroupDTO findById(Long ageGroupId) {
		return mapper.map(dao.find(ageGroupId), AgeGroupDTO.class);
	}

}
