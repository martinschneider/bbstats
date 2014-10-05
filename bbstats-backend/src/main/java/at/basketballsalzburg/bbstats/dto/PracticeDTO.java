package at.basketballsalzburg.bbstats.dto;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Martin Schneider
 */
public class PracticeDTO implements Comparable<PracticeDTO>
{
    private Long id;

    private Date dateTime;
    private Integer duration;
    private String comment;

    private GymDTO gym;

    private List<PlayerDTO> players;

    private List<CoachDTO> coaches;

    private List<AgeGroupDTO> ageGroups;

    private Integer guests;

    public List<PlayerDTO> getPlayers()
    {
        if (players != null)
        {
            Collections.sort(players);
        }
        return players;
    }

    public void setPlayers(List<PlayerDTO> players)
    {
        this.players = players;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getDuration()
    {
        return duration;
    }

    public void setDuration(Integer duration)
    {
        this.duration = duration;
    }

    public GymDTO getGym()
    {
        return gym;
    }

    public void setGym(GymDTO gym)
    {
        this.gym = gym;
    }

    public Integer getGuests()
    {
        return guests;
    }

    public void setGuests(Integer guests)
    {
        this.guests = guests;
    }

    public List<CoachDTO> getCoaches()
    {
        if (coaches != null)
        {
            Collections.sort(coaches);
        }
        return coaches;
    }

    public void setCoaches(List<CoachDTO> coaches)
    {
        this.coaches = coaches;
    }

    public List<AgeGroupDTO> getAgeGroups()
    {
        if (ageGroups != null)
        {
            Collections.sort(ageGroups);
        }
        return ageGroups;
    }

    public void setAgeGroups(List<AgeGroupDTO> ageGroups)
    {
        this.ageGroups = ageGroups;
    }

    public int compareTo(PracticeDTO rhs)
    {
        return this.getDateTime().compareTo(rhs.getDateTime());
    }

    public boolean equals(Object rhs)
    {
        return EqualsBuilder.reflectionEquals(this, rhs);
    }

    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public Date getDateTime()
    {
        return dateTime;
    }

    public void setDateTime(Date dateTime)
    {
        this.dateTime = dateTime;
    }

    public int getNumberOfPlayers()
    {
        int nrOfPlayers = getPlayers().size();
        if (getGuests() != null)
        {
            nrOfPlayers += getGuests();
        }
        return nrOfPlayers;
    }

    public void setNumberOfPlayers()
    {
        // do nothing
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }
}
