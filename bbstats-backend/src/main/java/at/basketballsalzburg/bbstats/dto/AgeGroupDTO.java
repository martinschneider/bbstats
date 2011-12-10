package at.basketballsalzburg.bbstats.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class AgeGroupDTO implements Comparable<AgeGroupDTO> {
	private Long id;

	private String name;

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

	public int compareTo(AgeGroupDTO rhs) {
		return this.getName().compareTo(rhs.getName());
	}

	public boolean equals(Object rhs) {
		return EqualsBuilder.reflectionEquals(this, rhs);
	}

	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
