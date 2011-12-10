package at.basketballsalzburg.bbstats.dto;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class GameStatDTO implements Comparable<GameStatDTO> {
	private Long id;

	private PlayerDTO player;

	private GameDTO game;

	private Integer points;
	private Integer threes;
	private Integer fta;
	private Integer ftm;
	private Integer fouls;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PlayerDTO getPlayer() {
		return player;
	}

	public void setPlayer(PlayerDTO player) {
		this.player = player;
	}

	public GameDTO getGame() {
		return game;
	}

	public void setGame(GameDTO game) {
		this.game = game;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getThrees() {
		return threes;
	}

	public void setThrees(Integer threes) {
		this.threes = threes;
	}

	public Integer getFta() {
		return fta;
	}

	public void setFta(Integer fta) {
		this.fta = fta;
	}

	public Integer getFtm() {
		return ftm;
	}

	public void setFtm(Integer ftm) {
		this.ftm = ftm;
	}

	public Integer getFouls() {
		return fouls;
	}

	public void setFouls(Integer fouls) {
		this.fouls = fouls;
	}

	public int compareTo(GameStatDTO rhs) {
		return new CompareToBuilder()
				.append(this.getPoints(), rhs.getPoints())
				.append(this.getPlayer().getLastName(),
						rhs.getPlayer().getLastName())
				.append(this.getPlayer().getFirstName(),
						rhs.getPlayer().getFirstName()).toComparison();
	}

	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
