package at.basketballsalzburg.bbstats.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Martin Schneider
 */
@Entity
@Table(name = "bbstats_game")
public class Game {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "DATE")
	private Date dateTime;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "leagueId")
	private League league;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "gymId")
	private Gym gym;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "teamAId")
	private Team teamA;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "teamBId")
	private Team teamB;

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
	
	private Boolean penalized;

	@OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<GameStat> stats;

	@ManyToMany(targetEntity = AgeGroup.class, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	@JoinTable(name = "bbstats_game_agegroup", joinColumns = @JoinColumn(name = "gameid"), inverseJoinColumns = @JoinColumn(name = "agegroupid"))
	private List<AgeGroup> ageGroups;

	@ManyToMany(targetEntity = Coach.class, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	@JoinTable(name = "bbstats_game_coach", joinColumns = @JoinColumn(name = "gameid"), inverseJoinColumns = @JoinColumn(name = "coachid"))
	private List<Coach> coaches;

	public void addStat(GameStat stat) {
		if (stats == null) {
			stats = new ArrayList<GameStat>();
		}
		stats.add(stat);
	}

	public Team getTeamA() {
		return teamA;
	}

	public void setTeamA(Team teamA) {
		this.teamA = teamA;
	}

	public Team getTeamB() {
		return teamB;
	}

	public void setTeamB(Team teamB) {
		this.teamB = teamB;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
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

	public List<GameStat> getStats() {
		return stats;
	}

	public void setStats(List<GameStat> stats) {
		this.stats = stats;
	}

	public Gym getGym() {
		return gym;
	}

	public void setGym(Gym gym) {
		this.gym = gym;
	}

	public List<Coach> getCoaches() {
		return coaches;
	}

	public void setCoaches(List<Coach> coaches) {
		this.coaches = coaches;
	}

	public List<AgeGroup> getAgeGroups() {
		return ageGroups;
	}

	public void setAgeGroups(List<AgeGroup> ageGroups) {
		this.ageGroups = ageGroups;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public Boolean getPenalized() {
		return penalized;
	}

	public void setPenalized(Boolean penalized) {
		this.penalized = penalized;
	}
}
