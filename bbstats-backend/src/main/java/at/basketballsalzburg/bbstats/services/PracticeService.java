package at.basketballsalzburg.bbstats.services;

import java.util.Date;
import java.util.List;

import at.basketballsalzburg.bbstats.dto.PracticeDTO;
import at.basketballsalzburg.bbstats.dto.statistics.AgeGroupPracticeStatisticDTO;
import at.basketballsalzburg.bbstats.dto.statistics.CoachPracticeStatisticDTO;
import at.basketballsalzburg.bbstats.dto.statistics.GymPracticeStatisticDTO;
import at.basketballsalzburg.bbstats.dto.statistics.SimplePlayerStatisticDTO;

/**
 * @author Martin Schneider
 */
public interface PracticeService {

	public abstract void save(PracticeDTO practice);

	public abstract List<PracticeDTO> findAll();

	public abstract List<PracticeDTO> findBetween(Date dateFrom, Date dateTo);

	public abstract List<PracticeDTO> findAllPracticesForPlayer(Long playerId);
	
	public abstract List<PracticeDTO> findAllPracticesForCoach(Long coachId);

	public abstract PracticeDTO findById(Long practiceId);

	public abstract void delete(PracticeDTO practice);

	public List<SimplePlayerStatisticDTO> getPlayerStatistics(Date fromDate, Date toDate);
	
	public List<CoachPracticeStatisticDTO> getCoachStatistics(Date fromDate, Date toDate);
	
	public List<AgeGroupPracticeStatisticDTO> getAgeGroupStatistics(Date fromDate, Date toDate);
	
	public List<GymPracticeStatisticDTO> getGymStatistics(Date fromDate, Date toDate);
}