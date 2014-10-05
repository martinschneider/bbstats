package at.basketballsalzburg.bbstats.services;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;

import at.basketballsalzburg.bbstats.dto.PracticeDTO;
import at.basketballsalzburg.bbstats.dto.statistics.AgeGroupPracticeStatisticDTO;
import at.basketballsalzburg.bbstats.dto.statistics.CoachPracticeStatisticDTO;
import at.basketballsalzburg.bbstats.dto.statistics.GymPracticeStatisticDTO;
import at.basketballsalzburg.bbstats.dto.statistics.SimplePlayerStatisticDTO;

/**
 * @author Martin Schneider
 */
public interface PracticeService
{

    public void save(PracticeDTO practice);

    public List<PracticeDTO> findAll();

    public List<PracticeDTO> findBetween(Date dateFrom, Date dateTo);

    public List<PracticeDTO> findAllPracticesForPlayer(Long playerId);

    public List<PracticeDTO> findAllPracticesForCoach(Long coachId);

    public PracticeDTO findById(Long practiceId);

    public List<PracticeDTO> findPractices(int page, int size, Sort sort);

    public List<PracticeDTO> findPracticesForPlayer(Long playerId, int page, int size, Sort sort);

    public List<PracticeDTO> findPracticesForCoach(Long coachId, int page, int size, Sort sort);

    public void delete(PracticeDTO practice);

    public List<SimplePlayerStatisticDTO> getPlayerStatistics(Date fromDate, Date toDate);

    public List<CoachPracticeStatisticDTO> getCoachStatistics(Date fromDate, Date toDate);

    public List<AgeGroupPracticeStatisticDTO> getAgeGroupStatistics(Date fromDate, Date toDate);

    public List<GymPracticeStatisticDTO> getGymStatistics(Date fromDate, Date toDate);

    public long count();

    public int countByPlayer(Long playerId);

    public int countByCoach(Long coachId);
}