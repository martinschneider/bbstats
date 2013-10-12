package at.basketballsalzburg.bbstats.dto.statistics;

import java.math.BigDecimal;

/**
 * @author Martin Schneider
 */
public class FoulStatisticDTO {
	private String lastname;
	private String firstname;
	private int fouls;
	private BigDecimal fpg;
	
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
}
