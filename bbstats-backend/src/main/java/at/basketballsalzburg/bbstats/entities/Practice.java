package at.basketballsalzburg.bbstats.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Martin Schneider
 */
@Entity
@Table(name = "bbstats_practice")
public class Practice
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "DATE")
    private Date dateTime;
    private Integer duration;
    private String comment;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "gymid")
    private Gym gym;

    @ManyToMany(targetEntity = Player.class, cascade = {CascadeType.PERSIST,
        CascadeType.MERGE})
    @JoinTable(name = "bbstats_practice_player", joinColumns = @JoinColumn(name = "practiceid"),
        inverseJoinColumns = @JoinColumn(name = "playerid"))
    private List<Player> players;

    @ManyToMany(targetEntity = Coach.class, cascade = {CascadeType.PERSIST,
        CascadeType.MERGE})
    @JoinTable(name = "bbstats_practice_coach", joinColumns = @JoinColumn(name = "practiceid"),
        inverseJoinColumns = @JoinColumn(name = "coachid"))
    private List<Coach> coaches;

    @ManyToMany(targetEntity = AgeGroup.class, cascade = {CascadeType.PERSIST,
        CascadeType.MERGE})
    @JoinTable(name = "bbstats_practice_agegroup", joinColumns = @JoinColumn(name = "practiceid"),
        inverseJoinColumns = @JoinColumn(name = "agegroupid"))
    private List<AgeGroup> ageGroups;

    private Integer guests;

    public List<Player> getPlayers()
    {
        return players;
    }

    public void setPlayers(List<Player> players)
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

    public Gym getGym()
    {
        return gym;
    }

    public void setGym(Gym gym)
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

    public List<Coach> getCoaches()
    {
        return coaches;
    }

    public void setCoaches(List<Coach> coaches)
    {
        this.coaches = coaches;
    }

    public List<AgeGroup> getAgeGroups()
    {
        return ageGroups;
    }

    public void setAgeGroups(List<AgeGroup> ageGroups)
    {
        this.ageGroups = ageGroups;
    }

    public Date getDateTime()
    {
        return dateTime;
    }

    public void setDateTime(Date dateTime)
    {
        this.dateTime = dateTime;
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
