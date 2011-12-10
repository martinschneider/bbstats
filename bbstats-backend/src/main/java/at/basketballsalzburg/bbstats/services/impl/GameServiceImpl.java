package at.basketballsalzburg.bbstats.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.dao.GameDAO;
import at.basketballsalzburg.bbstats.dto.GameDTO;
import at.basketballsalzburg.bbstats.entities.Game;
import at.basketballsalzburg.bbstats.services.GameService;

import com.googlecode.genericdao.search.Search;

@Transactional
public class GameServiceImpl implements GameService {

	private GameDAO dao;

	private DozerBeanMapper mapper;

	@Autowired
	public void setMapper(DozerBeanMapper mapper) {
		this.mapper = mapper;
	}

	@Autowired
	public void setDao(GameDAO dao) {
		this.dao = dao;
	}

	public void save(GameDTO dto) {
		Game game = mapper.map(dto, Game.class);
		dao.merge(game);
		dao.flush();
	}

	public List<GameDTO> findAll() {
		List<GameDTO> games = new ArrayList<GameDTO>();
		for (Game game : dao.findAll()) {
			games.add(mapper.map(game, GameDTO.class));
		}
		Collections.sort(games);
		Collections.reverse(games);
		return games;
	}

	public List<GameDTO> findAll(Date dateFrom, Date dateTo) {
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
		List<GameDTO> games = new ArrayList<GameDTO>();
		List<Game> entities = dao.search(new Search(Game.class)
				.addFilterGreaterOrEqual("dateTime", dateFrom)
				.addFilterLessOrEqual("dateTime", dateTo));
		for (Game game : entities) {
			games.add(mapper.map(game, GameDTO.class));
		}
		Collections.sort(games);
		Collections.reverse(games);
		return games;
	}

	public GameDTO findByName(String name) {
		if (name == null)
			return null;
		return mapper.map(
				dao.searchUnique(new Search().addFilterEqual("name", name)),
				GameDTO.class);
	}

	public GameDTO findById(Long gameId) {
		return mapper.map(dao.find(gameId), GameDTO.class);
	}

	public void delete(GameDTO gym) {
		dao.remove(mapper.map(gym, Game.class));
	}

}
