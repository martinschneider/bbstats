package at.basketballsalzburg.bbstats.services.impl;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.basketballsalzburg.bbstats.dao.PracticeDAO;
import at.basketballsalzburg.bbstats.dto.PracticeDTO;
import at.basketballsalzburg.bbstats.dto.statistics.AgeGroupPracticeStatisticDTO;
import at.basketballsalzburg.bbstats.dto.statistics.CoachPracticeStatisticDTO;
import at.basketballsalzburg.bbstats.dto.statistics.GymPracticeStatisticDTO;
import at.basketballsalzburg.bbstats.dto.statistics.SimplePlayerStatisticDTO;
import at.basketballsalzburg.bbstats.entities.Practice;
import at.basketballsalzburg.bbstats.services.PracticeService;

/**
 * @author Martin Schneider
 */
@Repository
@Transactional
public class PracticeServiceImpl implements PracticeService
{

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private PracticeDAO practiceDao;
    private DozerBeanMapper mapper;
    private EntityManager entityManager;

    @Autowired
    public void setMapper(DozerBeanMapper mapper)
    {
        this.mapper = mapper;
    }

    @Autowired
    public void setPracticeDao(PracticeDAO practiceDao)
    {
        this.practiceDao = practiceDao;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    public void save(PracticeDTO dto)
    {
        entityManager.merge(mapper.map(dto, Practice.class));
    }

    public List<PracticeDTO> findAll()
    {
        List<PracticeDTO> practices = new ArrayList<PracticeDTO>();
        for (Practice practice : practiceDao.findAll(new Sort(
            Sort.Direction.DESC, "dateTime")))
        {
            practices.add(mapper.map(practice, PracticeDTO.class));
        }
        return practices;
    }

    public List<PracticeDTO> findBetween(Date dateFrom, Date dateTo)
    {
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
        for (Practice practice : practiceDao
            .findByDateTimeAfterAndDateTimeBeforeOrderByDateTimeDesc(
                dateFrom, dateTo))
        {
            practices.add(mapper.map(practice, PracticeDTO.class));
        }
        return practices;
    }

    public PracticeDTO findById(Long id)
    {
        return mapper.map(practiceDao.findOne(id), PracticeDTO.class);
    }

    public void delete(PracticeDTO practice)
    {
        practiceDao.delete(mapper.map(practice, Practice.class));
    }

    public List<PracticeDTO> findAllPracticesForPlayer(Long playerId)
    {
        List<PracticeDTO> practices = new ArrayList<PracticeDTO>();
        for (Practice practice : practiceDao
            .findByPlayerOrderByDateTimeDesc(playerId))
        {
            practices.add(mapper.map(practice, PracticeDTO.class));
        }
        return practices;
    }

    public List<PracticeDTO> findAllPracticesForCoach(Long coachId)
    {
        List<PracticeDTO> practices = new ArrayList<PracticeDTO>();
        for (Practice practice : practiceDao
            .findByCoachOrderByDateTimeDesc(coachId))
        {
            practices.add(mapper.map(practice, PracticeDTO.class));
        }
        return practices;
    }

    public List<SimplePlayerStatisticDTO> getPlayerStatistics(Date fromDate,
        Date toDate)
    {
        List<SimplePlayerStatisticDTO> results = new ArrayList<SimplePlayerStatisticDTO>();
        for (Object o : entityManager
            .createNativeQuery(
                "SELECT BBSTATS_PLAYER.ID, LASTNAME, FIRSTNAME, COUNT(PLAYERID) AS COUNT FROM BBSTATS_PRACTICE_PLAYER, BBSTATS_PLAYER, BBSTATS_PRACTICE WHERE BBSTATS_PLAYER.ID=BBSTATS_PRACTICE_PLAYER.PLAYERID AND BBSTATS_PRACTICE.ID=BBSTATS_PRACTICE_PLAYER.PRACTICEID AND BBSTATS_PRACTICE.DATE>='"
                    + dateFormat.format(fromDate)
                    + "' AND BBSTATS_PRACTICE.DATE<='"
                    + dateFormat.format(toDate)
                    + "' GROUP BY PLAYERID ORDER BY COUNT DESC")
            .getResultList())
        {
            SimplePlayerStatisticDTO statistic = new SimplePlayerStatisticDTO();
            statistic.setId((((BigInteger) ((Object[]) o)[0])).longValue());
            statistic.setLastname((String) ((Object[]) o)[1]);
            statistic.setFirstname((String) ((Object[]) o)[2]);
            statistic.setCount((((BigInteger) ((Object[]) o)[3])).intValue());
            results.add(statistic);
        }
        return results;
    }

    public List<CoachPracticeStatisticDTO> getCoachStatistics(Date fromDate,
        Date toDate)
    {
        List<CoachPracticeStatisticDTO> results = new ArrayList<CoachPracticeStatisticDTO>();
        for (Object o : entityManager
            .createNativeQuery(
                "SELECT BBSTATS_COACH.ID, LASTNAME, FIRSTNAME, COUNT(COACHID) AS COUNT FROM BBSTATS_PRACTICE_COACH, BBSTATS_COACH, BBSTATS_PRACTICE WHERE BBSTATS_COACH.ID=BBSTATS_PRACTICE_COACH.COACHID AND BBSTATS_PRACTICE.ID=BBSTATS_PRACTICE_COACH.PRACTICEID AND BBSTATS_PRACTICE.DATE>='"
                    + dateFormat.format(fromDate)
                    + "' AND BBSTATS_PRACTICE.DATE<='"
                    + dateFormat.format(toDate)
                    + "' GROUP BY COACHID ORDER BY COUNT DESC")
            .getResultList())
        {
            CoachPracticeStatisticDTO statistic = new CoachPracticeStatisticDTO();
            statistic.setId((((BigInteger) ((Object[]) o)[0])).longValue());
            statistic.setLastname((String) ((Object[]) o)[1]);
            statistic.setFirstname((String) ((Object[]) o)[2]);
            statistic.setCount((((BigInteger) ((Object[]) o)[3])).intValue());
            results.add(statistic);
        }
        return results;
    }

    public List<AgeGroupPracticeStatisticDTO> getAgeGroupStatistics(
        Date fromDate, Date toDate)
    {
        List<AgeGroupPracticeStatisticDTO> results = new ArrayList<AgeGroupPracticeStatisticDTO>();
        for (Object o : entityManager
            .createNativeQuery(
                "SELECT NAME, COUNT(AGEGROUPID) AS COUNT FROM BBSTATS_PRACTICE_AGEGROUP, BBSTATS_AGEGROUP, BBSTATS_PRACTICE WHERE BBSTATS_AGEGROUP.ID=BBSTATS_PRACTICE_AGEGROUP.AGEGROUPID AND BBSTATS_PRACTICE.ID=BBSTATS_PRACTICE_AGEGROUP.PRACTICEID AND BBSTATS_PRACTICE.DATE>='"
                    + dateFormat.format(fromDate)
                    + "' AND BBSTATS_PRACTICE.DATE<='"
                    + dateFormat.format(toDate)
                    + "' GROUP BY AGEGROUPID ORDER BY COUNT DESC;")
            .getResultList())
        {
            AgeGroupPracticeStatisticDTO statistic = new AgeGroupPracticeStatisticDTO();
            statistic.setName((String) ((Object[]) o)[0]);
            statistic.setCount((((BigInteger) ((Object[]) o)[1])).intValue());
            results.add(statistic);
        }
        return results;
    }

    public List<GymPracticeStatisticDTO> getGymStatistics(Date fromDate,
        Date toDate)
    {
        List<GymPracticeStatisticDTO> results = new ArrayList<GymPracticeStatisticDTO>();
        for (Object o : entityManager
            .createNativeQuery(
                "SELECT NAME, CITY, COUNT(GYMID) AS COUNT FROM BBSTATS_GYM, BBSTATS_PRACTICE WHERE BBSTATS_PRACTICE.GYMID=BBSTATS_GYM.ID AND BBSTATS_PRACTICE.DATE>='"
                    + dateFormat.format(fromDate)
                    + "' AND BBSTATS_PRACTICE.DATE<='"
                    + dateFormat.format(toDate)
                    + "' GROUP BY GYMID ORDER BY COUNT DESC")
            .getResultList())
        {
            GymPracticeStatisticDTO statistic = new GymPracticeStatisticDTO();
            statistic.setName((String) ((Object[]) o)[0]);
            statistic.setCity((String) ((Object[]) o)[1]);
            statistic.setCount((((BigInteger) ((Object[]) o)[2])).intValue());
            results.add(statistic);
        }
        return results;
    }

    @Override
    public long count()
    {
        return practiceDao.count();
    }

    @Override
    public List<PracticeDTO> findPractices(int page, int size, Sort sort)
    {
        List<PracticeDTO> practices = new ArrayList<PracticeDTO>();
        for (Practice practice : practiceDao.findAll(new PageRequest(page, size, sort)))
        {
            practices.add(mapper.map(practice, PracticeDTO.class));
        }
        return practices;
    }

    @Override
    public List<PracticeDTO> findPracticesForPlayer(Long playerId, int page, int size,
        Sort sort)
    {
        List<PracticeDTO> practices = new ArrayList<PracticeDTO>();
        for (Practice practice : practiceDao.findByPlayer(playerId, new PageRequest(page, size, sort)))
        {
            practices.add(mapper.map(practice, PracticeDTO.class));
        }
        return practices;
    }

    @Override
    public List<PracticeDTO> findPracticesForCoach(Long coachId, int page, int size,
        Sort sort)
    {
        List<PracticeDTO> practices = new ArrayList<PracticeDTO>();
        for (Practice practice : practiceDao.findByCoach(coachId, new PageRequest(page, size, sort)))
        {
            practices.add(mapper.map(practice, PracticeDTO.class));
        }
        return practices;
    }

    @Override
    public int countByPlayer(Long playerId)
    {
        return practiceDao.countByPlayer(playerId);
    }

    @Override
    public int countByCoach(Long coachId)
    {
        return practiceDao.countByCoach(coachId);
    }
}
