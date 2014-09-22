package at.basketballsalzburg.bbstats.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Martin Schneider
 */
public class PlayerDTO implements Comparable<PlayerDTO> {
	private Long id;

	private String firstName;
	private String lastName;
	private String adress;
	private String postalCode;
	private String city;
	private String country;
	private String phone;
	private String email;
	private Date birthday;
	private String nationality;

	private List<PracticeDTO> practices;
	private List<AgeGroupDTO> ageGroups;

	public PlayerDTO()
	{
		ageGroups = new ArrayList<AgeGroupDTO>(); 
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public List<PracticeDTO> getPractices() {
		return practices;
	}

	public void setPractices(List<PracticeDTO> practices) {
		this.practices = practices;
	}

	public String getDisplayName() {
		return lastName + " " + firstName;
	}

	public String getNationalityDisplayName(Locale locale) {
		try {
			return new Locale("", nationality).getDisplayCountry(locale);
		} catch (Exception e) {
			return null;
		}
	}

	public List<AgeGroupDTO> getAgeGroups() {
		return ageGroups;
	}

	public void setAgeGroups(List<AgeGroupDTO> ageGroups) {
		this.ageGroups = ageGroups;
	}

	public int compareTo(PlayerDTO rhs) {
		return new CompareToBuilder()
				.append(this.getLastName(), rhs.getLastName())
				.append(this.getFirstName(), rhs.getFirstName()).toComparison();
	}

	public boolean equals(Object rhs) {
		return EqualsBuilder.reflectionEquals(this, rhs);
	}

	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
