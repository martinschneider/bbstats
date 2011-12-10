package at.basketballsalzburg.bbstats.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class GameDTO implements Comparable<GameDTO> {
	private Long id;
	private Date dateTime;
	private LeagueDTO league;
	private GymDTO gym;
	private TeamDTO teamA;
	private TeamDTO teamB;
	private Integer periods;
	private Integer scoreA1;
	private Integer scoreB1;
	private Integer scoreA2;
	private Integer scoreB2;
	private Integer scoreA3;
	private Integer scoreB3;
	private Integer scoreA4;
	private Integer scoreB4;
	private Integer scoreAV;
	private Integer scoreBV;

	private List<GameStatDTO> stats;

	private List<AgeGroupDTO> ageGroups;

	private List<CoachDTO> coaches;

	public void addAgeGroup(AgeGroupDTO ageGroup) {
		if (ageGroups == null) {
			ageGroups = new ArrayList<AgeGroupDTO>();
		}
		ageGroups.add(ageGroup);
	}

	public void addStat(GameStatDTO stat) {
		if (stats == null) {
			stats = new ArrayList<GameStatDTO>();
		}
		stats.add(stat);
	}

	public void addCoach(CoachDTO coach) {
		if (coaches == null) {
			coaches = new ArrayList<CoachDTO>();
		}
		coaches.add(coach);
	}

	public TeamDTO getTeamA() {
		return teamA;
	}

	public void setTeamA(TeamDTO teamA) {
		this.teamA = teamA;
	}

	public TeamDTO getTeamB() {
		return teamB;
	}

	public void setTeamB(TeamDTO teamB) {
		this.teamB = teamB;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LeagueDTO getLeague() {
		return league;
	}

	public void setLeague(LeagueDTO league) {
		this.league = league;
	}

	public Integer getPeriods() {
		return periods;
	}

	public void setPeriods(Integer periods) {
		this.periods = periods;
	}

	public Integer getScoreA1() {
		return scoreA1;
	}

	public void setScoreA1(Integer scoreA1) {
		this.scoreA1 = scoreA1;
	}

	public Integer getScoreB1() {
		return scoreB1;
	}

	public void setScoreB1(Integer scoreB1) {
		this.scoreB1 = scoreB1;
	}

	public Integer getScoreA2() {
		return scoreA2;
	}

	public void setScoreA2(Integer scoreA2) {
		this.scoreA2 = scoreA2;
	}

	public Integer getScoreB2() {
		return scoreB2;
	}

	public void setScoreB2(Integer scoreB2) {
		this.scoreB2 = scoreB2;
	}

	public Integer getScoreA3() {
		return scoreA3;
	}

	public void setScoreA3(Integer scoreA3) {
		this.scoreA3 = scoreA3;
	}

	public Integer getScoreB3() {
		return scoreB3;
	}

	public void setScoreB3(Integer scoreB3) {
		this.scoreB3 = scoreB3;
	}

	public Integer getScoreA4() {
		return scoreA4;
	}

	public void setScoreA4(Integer scoreA4) {
		this.scoreA4 = scoreA4;
	}

	public Integer getScoreB4() {
		return scoreB4;
	}

	public void setScoreB4(Integer scoreB4) {
		this.scoreB4 = scoreB4;
	}

	public Integer getScoreAV() {
		return scoreAV;
	}

	public void setScoreAV(Integer scoreAV) {
		this.scoreAV = scoreAV;
	}

	public Integer getScoreBV() {
		return scoreBV;
	}

	public void setScoreBV(Integer scoreBV) {
		this.scoreBV = scoreBV;
	}

	public List<GameStatDTO> getStats() {
		if (stats != null) {
			Collections.sort(stats);
			Collections.reverse(stats);
		}
		return stats;
	}

	public void setStats(List<GameStatDTO> stats) {
		this.stats = stats;
	}

	public GymDTO getGym() {
		return gym;
	}

	public void setGym(GymDTO gym) {
		this.gym = gym;
	}

	public List<CoachDTO> getCoaches() {
		return coaches;
	}

	public void setCoaches(List<CoachDTO> coaches) {
		this.coaches = coaches;
	}

	public List<AgeGroupDTO> getAgeGroups() {
		return ageGroups;
	}

	public void setAgeGroups(List<AgeGroupDTO> ageGroups) {
		this.ageGroups = ageGroups;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public int compareTo(GameDTO rhs) {
		return this.getDateTime().compareTo(rhs.getDateTime());
	}

	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public boolean equals(Object rhs) {
		return EqualsBuilder.reflectionEquals(this, rhs);
	}
}
