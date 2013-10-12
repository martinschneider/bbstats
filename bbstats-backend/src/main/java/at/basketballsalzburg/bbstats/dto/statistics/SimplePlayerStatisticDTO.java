package at.basketballsalzburg.bbstats.dto.statistics;

/**
 * @author Martin Schneider
 */
public class SimplePlayerStatisticDTO {

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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	private Long id;
	private String lastname;
	private String firstname;
	private int count;
}
