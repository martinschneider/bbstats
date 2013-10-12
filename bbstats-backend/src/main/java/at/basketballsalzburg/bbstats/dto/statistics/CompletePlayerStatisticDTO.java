package at.basketballsalzburg.bbstats.dto.statistics;

import java.math.BigDecimal;

/**
 * @author Martin Schneider
 */
public class CompletePlayerStatisticDTO {
	private String lastname;
	private String firstname;
	private Long playerId;
	private int games;
	private int fouls;
	private BigDecimal fpg;
	private int fta;
	private int ftm;
	private BigDecimal ftPercentage;
	private BigDecimal ftapg;
	private BigDecimal ftmpg;
	private int points;
	private BigDecimal ppg;
	private int threes;
	private BigDecimal threesPg;
	
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public int getGames() {
		return games;
	}

	public void setGames(int games) {
		this.games = games;
	}

	public int getFouls() {
		return fouls;
	}

	public void setFouls(int fouls) {
		this.fouls = fouls;
	}

	public BigDecimal getFpg() {
		return fpg;
	}

	public void setFpg(BigDecimal fpg) {
		this.fpg = fpg;
	}

	public int getFta() {
		return fta;
	}

	public void setFta(int fta) {
		this.fta = fta;
	}

	public int getFtm() {
		return ftm;
	}

	public void setFtm(int ftm) {
		this.ftm = ftm;
	}

	public BigDecimal getFtPercentage() {
		return ftPercentage;
	}

	public void setFtPercentage(BigDecimal ftPercentage) {
		this.ftPercentage = ftPercentage;
	}

	public BigDecimal getFtapg() {
		return ftapg;
	}

	public void setFtapg(BigDecimal ftapg) {
		this.ftapg = ftapg;
	}

	public BigDecimal getFtmpg() {
		return ftmpg;
	}

	public void setFtmpg(BigDecimal ftmpg) {
		this.ftmpg = ftmpg;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public BigDecimal getPpg() {
		return ppg;
	}

	public void setPpg(BigDecimal ppg) {
		this.ppg = ppg;
	}

	public BigDecimal getThreesPg() {
		return threesPg;
	}

	public void setThreesPg(BigDecimal threesPg) {
		this.threesPg = threesPg;
	}

	public int getThrees() {
		return threes;
	}

	public void setThrees(int threes) {
		this.threes = threes;
	}

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId=playerId;
	}
}
