package at.basketballsalzburg.bbstats.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.dao.PracticeDAO;
import at.basketballsalzburg.bbstats.dto.PracticeDTO;
import at.basketballsalzburg.bbstats.entities.Practice;
import at.basketballsalzburg.bbstats.services.PracticeService;

import com.googlecode.genericdao.search.Search;

@Transactional
public class PracticeServiceImpl implements PracticeService {

	private PracticeDAO dao;
	private DozerBeanMapper mapper;

	@Autowired
	public void setMapper(DozerBeanMapper mapper) {
		this.mapper = mapper;
	}

	@Autowired
	public void setDao(PracticeDAO dao) {
		this.dao = dao;
	}

	public void save(PracticeDTO dto) {
		Practice practice = mapper.map(dto, Practice.class);
		dao.merge(practice);
		dao.flush();
	}

	public List<PracticeDTO> findAll() {
		List<PracticeDTO> practices = new ArrayList<PracticeDTO>();
		for (Practice practice : dao.findAll()) {
			practices.add(mapper.map(practice, PracticeDTO.class));
		}
		return practices;
	}

	public List<PracticeDTO> findAll(Date dateFrom, Date dateTo) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateFrom);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		dateFrom = cal.getTime();
		cal.setTime(dateTo);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		dateTo = cal.getTime();
		List<PracticeDTO> practices = new ArrayList<PracticeDTO>();
		List<Practice> entities = dao.search(new Search(Practice.class)
				.addFilterGreaterOrEqual("dateTime", dateFrom)
				.addFilterLessOrEqual("dateTime", dateTo));
		for (Practice practice : entities) {
			practices.add(mapper.map(practice, PracticeDTO.class));
		}
		Collections.sort(practices);
		Collections.reverse(practices);
		return practices;
	}

	public PracticeDTO findByName(String name) {
		if (name == null)
			return null;
		return mapper.map(
				dao.searchUnique(new Search().addFilterEqual("name", name)),
				PracticeDTO.class);
	}

	public PracticeDTO findById(Long id) {
		return mapper.map(dao.find(id), PracticeDTO.class);
	}

	public void delete(PracticeDTO practice) {
		dao.remove(mapper.map(practice, Practice.class));
	}

}
