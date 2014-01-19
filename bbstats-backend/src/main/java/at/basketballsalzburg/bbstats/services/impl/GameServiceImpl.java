package at.basketballsalzburg.bbstats.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.dao.GameDAO;
import at.basketballsalzburg.bbstats.dto.GameDTO;
import at.basketballsalzburg.bbstats.entities.Game;
import at.basketballsalzburg.bbstats.services.GameService;

/**
 * @author Martin Schneider
 */
@Repository
@Transactional
public class GameServiceImpl implements GameService {

	private GameDAO dao;
	private DozerBeanMapper mapper;
	private EntityManager entityManager;

	@Value("#{'${teamNames}'.split(',')}")
	private List<String> teamNames;

	@Value("#{'${showStatistics}'.split(',')}")
	private List<String> showStatistics;

	@Autowired
	public void setMapper(DozerBeanMapper mapper) {
		this.mapper = mapper;
	}

	@Autowired
	public void setGameDao(GameDAO gameDao) {
		this.dao = gameDao;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void save(GameDTO dto) {
		entityManager.merge(mapper.map(dto, Game.class));
	}

	public List<GameDTO> findAll() {
		List<GameDTO> games = new ArrayList<GameDTO>();
		for (Game game : dao.findAll(new Sort(Sort.Direction.DESC, "dateTime"))) {
			games.add(mapper.map(game, GameDTO.class));
		}
		return games;
	}

	@Override
	public List<GameDTO> find(int page, int size, Sort sort) {
		List<GameDTO> games = new ArrayList<GameDTO>();
		for (Game game : dao.findAll(new PageRequest(page, size, sort))) {
			games.add(mapper.map(game, GameDTO.class));
		}
		return games;
	}

	public List<GameDTO> findBetween(Date dateFrom, Date dateTo) {
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
		List<Game> entities = dao
				.findByDateTimeAfterAndDateTimeBeforeOrderByDateTimeDesc(
						dateFrom, dateTo);
		for (Game game : entities) {
			games.add(mapper.map(game, GameDTO.class));
		}
		return games;
	}

	public List<GameDTO> findAfter(Date dateFrom) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateFrom);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		dateFrom = cal.getTime();
		List<GameDTO> games = new ArrayList<GameDTO>();
		List<Game> entities = dao
				.findByDateTimeAfterOrderByDateTimeAsc(dateFrom);
		for (Game game : entities) {
			games.add(mapper.map(game, GameDTO.class));
		}
		return games;
	}

	public List<GameDTO> findBefore(Date dateTo) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateTo);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		dateTo = cal.getTime();
		List<GameDTO> games = new ArrayList<GameDTO>();
		for (Game game : dao.findByDateTimeBeforeOrderByDateTimeDesc(dateTo)) {
			games.add(mapper.map(game, GameDTO.class));
		}
		return games;
	}

	public GameDTO findById(Long gameId) {
		return mapper.map(dao.findOne(gameId), GameDTO.class);
	}

	public void delete(GameDTO gym) {
		dao.delete(mapper.map(gym, Game.class));
	}

	public List<GameDTO> findAllGamesForPlayer(Long playerId) {
		List<GameDTO> games = new ArrayList<GameDTO>();
		for (Game game : dao.findByPlayerOrderByDateTimeDesc(playerId)) {
			games.add(mapper.map(game, GameDTO.class));
		}
		return games;
	}

	public List<GameDTO> findAllGamesForCoach(Long coachId) {
		List<GameDTO> games = new ArrayList<GameDTO>();
		for (Game game : dao.findByCoachOrderByDateTimeDesc(coachId)) {
			games.add(mapper.map(game, GameDTO.class));
		}
		return games;
	}

	public boolean isHome(GameDTO game) {
		for (String teamName : teamNames) {
			if ((game.getTeamA().getName().contains(teamName))) {
				return true;
			}
		}
		return false;
	}

	public boolean isAway(GameDTO game) {
		for (String teamName : teamNames) {
			if ((game.getTeamB().getName().contains(teamName))) {
				return true;
			}
		}
		return false;
	}

	public boolean isWin(GameDTO game) {
		int sumA = game.getScoreA1().intValue() + game.getScoreA2().intValue()
				+ game.getScoreA3().intValue() + game.getScoreA4().intValue()
				+ game.getScoreAV().intValue();
		int sumB = game.getScoreB1().intValue() + game.getScoreB2().intValue()
				+ game.getScoreB3().intValue() + game.getScoreB4().intValue()
				+ game.getScoreBV().intValue();
		for (String teamName : teamNames) {
			if ((game.getTeamA().getName().contains(teamName) && sumA >= sumB)
					|| (game.getTeamB().getName().contains(teamName) && sumB >= sumA)) {
				return true;
			}
		}
		return false;
	}

	public List<String> getTeamNames() {
		return teamNames;
	}

	public List<String> getShowStatistics() {
		return showStatistics;
	}

	@Override
	public long count() {
		return dao.count();
	}

	@Override
	public int countResults() {
		return dao.countByDateTimeBefore(new Date());
	}

	@Override
	public int countSchedule() {
		return dao.countByDateTimeAfter(new Date());
	}

	@Override
	public List<GameDTO> findResults(int page, int size, Sort sort) {
		List<GameDTO> games = new ArrayList<GameDTO>();
		for (Game game : dao.findByDateTimeBefore(new Date(), new PageRequest(
				page, size, sort))) {
			games.add(mapper.map(game, GameDTO.class));
		}
		return games;
	}

	@Override
	public List<GameDTO> findSchedule(int page, int size, Sort sort) {
		List<GameDTO> games = new ArrayList<GameDTO>();
		for (Game game : dao.findByDateTimeAfter(new Date(), new PageRequest(
				page, size, sort))) {
			games.add(mapper.map(game, GameDTO.class));
		}
		return games;
	}

	@Override
	public List<GameDTO> findGamesForPlayer(Long playerId, int page, int size,
			Sort sort) {
		List<GameDTO> games = new ArrayList<GameDTO>();
		for (Game game : dao.findByPlayer(playerId, new PageRequest(page, size,
				sort))) {
			games.add(mapper.map(game, GameDTO.class));
		}
		return games;
	}

	@Override
	public List<GameDTO> findGamesForCoach(Long coachId, int page, int size,
			Sort sort) {
		List<GameDTO> games = new ArrayList<GameDTO>();
		for (Game game : dao.findByCoach(coachId, new PageRequest(page, size,
				sort))) {
			games.add(mapper.map(game, GameDTO.class));
		}
		return games;
	}

	@Override
	public int countByPlayer(Long playerId) {
		return dao.countByPlayer(playerId);
	}

	@Override
	public int countByCoach(Long coachId) {
		return dao.countByCoach(coachId);
	}

	@Override
	public boolean isShowStats(GameDTO game) {
		for (String teamName : showStatistics) {
			if ((game.getTeamA().getName().contains(teamName))
					|| (game.getTeamB().getName().contains(teamName))) {
				return true;
			}
		}
		return false;
	}
}
