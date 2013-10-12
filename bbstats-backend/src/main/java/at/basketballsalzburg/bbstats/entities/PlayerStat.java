package at.basketballsalzburg.bbstats.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Martin Schneider
 */
@Entity
public class PlayerStat {
	@Id
	private Long id;
	
	private String playerName;
	
	private Integer count;

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
