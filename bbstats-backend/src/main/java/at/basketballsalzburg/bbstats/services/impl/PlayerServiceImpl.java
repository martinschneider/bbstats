package at.basketballsalzburg.bbstats.services.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.dao.PlayerDAO;
import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.dto.statistics.CompletePlayerStatisticDTO;
import at.basketballsalzburg.bbstats.entities.AgeGroup;
import at.basketballsalzburg.bbstats.entities.Player;
import at.basketballsalzburg.bbstats.services.PlayerService;

/**
 * @author Martin Schneider
 */
@Repository
@Transactional
public class PlayerServiceImpl implements PlayerService {

	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private PlayerDAO dao;
	private DozerBeanMapper mapper;
	private EntityManager entityManager;

	@Autowired
	public void setMapper(DozerBeanMapper mapper) {
		this.mapper = mapper;
	}

	@Autowired
	public void setDao(PlayerDAO dao) {
		this.dao = dao;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void save(PlayerDTO dto) {
		entityManager.merge(mapper.map(dto, Player.class));
	}

	public SortedSet<PlayerDTO> findAll() {
		SortedSet<PlayerDTO> players = new TreeSet<PlayerDTO>();
		for (Object player : dao.findAll(new Sort(Sort.Direction.ASC,
				"lastName", "firstName"))) {
			players.add(mapper.map(player, PlayerDTO.class));
		}
		return players;
	}

	public SortedSet<PlayerDTO> findAllActiveSince(Date date) {
		SortedSet<PlayerDTO> players = new TreeSet<PlayerDTO>();
		for (Object player : dao.findPlayersActiveSince(date)) {
			players.add(mapper.map(player, PlayerDTO.class));
		}
		return players;
	}

	public PlayerDTO findByName(String firstName, String lastName) {
		return mapper.map(dao.findByFirstNameAndLastName(firstName, lastName),
				PlayerDTO.class);
	}

	public PlayerDTO findById(Long id) {
		return mapper.map(dao.findOne(id), PlayerDTO.class);
	}

	public void delete(PlayerDTO player) {
		dao.delete(mapper.map(player, Player.class));
	}

	public List<CompletePlayerStatisticDTO> getCompleteStatistics(
			Date fromDate, Date toDate) {
		List<CompletePlayerStatisticDTO> results = new ArrayList<CompletePlayerStatisticDTO>();
		for (Object o : entityManager
				.createNativeQuery(
						"SELECT BBSTATS_PLAYER.LASTNAME, BBSTATS_PLAYER.FIRSTNAME, BBSTATS_PLAYER.ID, COUNT(*) AS GAMES, SUM(POINTS) AS POINTS, CAST(SUM(POINTS) AS DOUBLE)/COUNT(*) AS PPG, SUM(FTA) AS FTA, SUM(FTM) AS FTM, CAST(SUM(FTM) AS DOUBLE)/NULLIF(SUM(FTA),0) AS FTPERCENTAGE, CAST(SUM(FTA) AS DOUBLE)/COUNT(*) AS FTAPG, CAST(SUM(FTM) AS DOUBLE)/COUNT(*) AS FTMPG, SUM(FOULS) AS FOULS, CAST(SUM(FOULS) AS DOUBLE)/COUNT(*) AS FPG, SUM(THREES) AS THREES, CAST(SUM(THREES) AS DOUBLE)/COUNT(*) AS THREESPG FROM BBSTATS_PLAYER, BBSTATS_GAME, BBSTATS_STAT WHERE BBSTATS_STAT.GAMEID=BBSTATS_GAME.ID AND BBSTATS_STAT.PLAYERID=BBSTATS_PLAYER.ID AND BBSTATS_GAME.DATE>='"
								+ dateFormat.format(fromDate)
								+ "' AND BBSTATS_GAME.DATE<='"
								+ dateFormat.format(toDate)
								+ "' GROUP BY BBSTATS_PLAYER.LASTNAME, BBSTATS_PLAYER.FIRSTNAME ORDER BY PPG DESC;")
				.getResultList()) {
			CompletePlayerStatisticDTO statistic = new CompletePlayerStatisticDTO();
			statistic.setLastname((String) ((Object[]) o)[0]);
			statistic.setFirstname((String) ((Object[]) o)[1]);
			statistic.setPlayerId((((BigInteger) ((Object[]) o)[2]))
					.longValue());
			statistic.setGames((((BigInteger) ((Object[]) o)[3])).intValue());
			statistic.setPoints((((BigInteger) ((Object[]) o)[4])).intValue());
			statistic.setPpg(new BigDecimal(((Double) ((Object[]) o)[5]))
					.setScale(2, BigDecimal.ROUND_HALF_UP));
			statistic.setFta((((BigInteger) ((Object[]) o)[6])).intValue());
			statistic.setFtm((((BigInteger) ((Object[]) o)[7])).intValue());
			Double ftPercentageDouble = (((Double) ((Object[]) o)[8]));
			BigDecimal ftPercentage = null;
			if (ftPercentageDouble != null) {
				ftPercentage = new BigDecimal(ftPercentageDouble).setScale(2,
						BigDecimal.ROUND_HALF_UP);
			}
			statistic.setFtPercentage(ftPercentage);
			statistic.setFtapg(new BigDecimal((((Double) ((Object[]) o)[9])))
					.setScale(2, BigDecimal.ROUND_HALF_UP));
			statistic.setFtmpg(new BigDecimal((((Double) ((Object[]) o)[10])))
					.setScale(2, BigDecimal.ROUND_HALF_UP));
			statistic.setFouls((((BigInteger) ((Object[]) o)[11])).intValue());
			statistic.setFpg(new BigDecimal((((Double) ((Object[]) o)[12])))
					.setScale(2, BigDecimal.ROUND_HALF_UP));
			statistic.setThrees((((BigInteger) ((Object[]) o)[13])).intValue());
			statistic.setThreesPg(new BigDecimal(
					(((Double) ((Object[]) o)[14]))).setScale(2,
					BigDecimal.ROUND_HALF_UP));
			results.add(statistic);
		}
		return results;
	}

	@Override
	public SortedSet<PlayerDTO> findAllForAgegroup(AgeGroupDTO agegroup) {
		SortedSet<PlayerDTO> players = new TreeSet<PlayerDTO>();
		for (Object player : dao.findByAgeGroupOrderByName(mapper.map(agegroup,
				AgeGroup.class))) {
			players.add(mapper.map(player, PlayerDTO.class));
		}
		return players;
	}

	@Override
	public SortedSet<PlayerDTO> findAllWithoutAgeGroup() {
		SortedSet<PlayerDTO> players = new TreeSet<PlayerDTO>();
		for (Object player : dao.findWithoutAgeGroup()) {
			players.add(mapper.map(player, PlayerDTO.class));
		}
		return players;
	}

	@Override
	public SortedSet<PlayerDTO> findAllWithAgeGroup() {
		SortedSet<PlayerDTO> players = new TreeSet<PlayerDTO>();
		for (Object player : dao.findWithAgeGroup()) {
			players.add(mapper.map(player, PlayerDTO.class));
		}
		return players;
	}
}