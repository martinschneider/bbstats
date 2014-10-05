package at.basketballsalzburg.bbstats.dto;

import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Martin Schneider
 */
public class CoachDTO implements Comparable<CoachDTO>
{
    private Long id;

    private String lastName;

    private List<AgeGroupDTO> ageGroups;

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    private String firstName;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getDisplayName()
    {
        return lastName + " " + firstName;
    }

    public List<AgeGroupDTO> getAgeGroups()
    {
        return ageGroups;
    }

    public void setAgeGroups(List<AgeGroupDTO> ageGroups)
    {
        this.ageGroups = ageGroups;
    }

    public int compareTo(CoachDTO rhs)
    {
        return new CompareToBuilder()
            .append(this.getLastName(), rhs.getLastName())
            .append(this.getFirstName(), rhs.getFirstName()).toComparison();
    }

    public boolean equals(Object rhs)
    {
        return EqualsBuilder.reflectionEquals(this, rhs);
    }

    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
