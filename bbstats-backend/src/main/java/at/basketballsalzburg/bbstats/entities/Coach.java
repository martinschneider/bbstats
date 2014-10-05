package at.basketballsalzburg.bbstats.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * @author Martin Schneider
 */
@Entity
@Table(name = "bbstats_coach")
public class Coach
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(targetEntity = AgeGroup.class, cascade = {CascadeType.PERSIST,
        CascadeType.MERGE})
    @JoinTable(name = "bbstats_coach_agegroup", joinColumns = @JoinColumn(name = "coachid"),
        inverseJoinColumns = @JoinColumn(name = "agegroupid"))
    private List<AgeGroup> ageGroups;

    private String lastName;

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

    public List<AgeGroup> getAgeGroups()
    {
        return ageGroups;
    }

    public void setAgeGroups(List<AgeGroup> ageGroups)
    {
        this.ageGroups = ageGroups;
    }
}
