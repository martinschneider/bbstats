package at.basketballsalzburg.bbstats.dto;

/**
 * @author Martin Schneider
 */
public class TeamDTO implements Comparable<TeamDTO>{

	private Long id;

	private String name;

	private String shortName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public int compareTo(TeamDTO rhs) {
		return name.compareTo(rhs.getName());
	}

	public String toString()
	{
		return name;
	}
}
