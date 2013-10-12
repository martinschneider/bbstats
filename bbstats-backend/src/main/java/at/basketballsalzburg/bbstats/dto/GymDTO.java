package at.basketballsalzburg.bbstats.dto;

import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * @author Martin Schneider
 */
public class GymDTO implements Comparable<GymDTO> {
	private Long id;

	private String name;
	private String shortName;
	private String adress;
	private String postalCode;
	private String city;
	private String country;

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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDisplayName() {
		return city + ", " + name;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int compareTo(GymDTO rhs) {
		return new CompareToBuilder().append(this.getDisplayName(),
				rhs.getDisplayName()).toComparison();
	}
}
