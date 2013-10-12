package at.basketballsalzburg.bbstats.dto.statistics;

import java.math.BigDecimal;

/**
 * @author Martin Schneider
 */
public class PointStatisticDTO {
	private String lastname;
	private String firstname;
	private int points;
	private BigDecimal ppg;

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
}
